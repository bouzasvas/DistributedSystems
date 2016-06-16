package core;

import java.util.*;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.net.*;
import java.sql.*;
import java.io.*;

public class Mapper implements MapWorker {

	Scanner input = new Scanner(System.in);
	
	private List<ListOfCheckins> Checkins_Area;
	
	private String reducer_address;
	
	private ServerSocket mapper = null;
	private Socket reducer = null;
	private Socket client = null;
	
	private int mapper_port;
	private int reducer_port;
	
	private int cores;
	private int topK;
	
	private double minX, maxX, minY, maxY = 0; // X is Longtitude, Y is Latitude
	private String minDatetime, maxDatetime;

	public Mapper(int mapper_port, String reducer_address, int reducer_port) {
		if (!checkPortAvailability(mapper_port)) {
			System.out.println("Port is in use");
			System.out.println("Run again the program with another port");
			System.exit(-1);
		} else
			this.mapper_port = mapper_port;
		this.reducer_address = reducer_address;
		this.reducer_port = reducer_port;
	}

	public Mapper(int port, double minX, double maxX, double minY, double maxY, String minDatetime, String maxDatetime) {
		this.mapper_port = port;
		setPosition(minX, maxX, minY, maxY);
		setDate(minDatetime, maxDatetime);
	}

	public void setValues(double minX, double maxX, double minY, double maxY, String minDatetime, String maxDatetime) {
		setPosition(minX, maxX, minY, maxY);
		setDate(minDatetime, maxDatetime);
	}

	private void setPosition(double minX, double maxX, double minY, double maxY) {
		this.minX = minX;
		this.maxX = maxX;
		this.minY = minY;
		this.maxY = maxY;
	}

	private void setDate(String minDatetime, String maxDatetime) {
		this.minDatetime = minDatetime;
		this.maxDatetime = maxDatetime;
	}
	
	public void printCheckinsArea() {
		for ( ListOfCheckins check : Checkins_Area) {
			check.printCheckins();
		}
	}

	@Override
	public void initialize() {
		try {
			cores = Runtime.getRuntime().availableProcessors();
			System.out.println("Mapper has "+cores+" cores");
			
			mapper = new ServerSocket(mapper_port);
			
			while (true) {
				client = mapper.accept();
				
				waitForTasksThread();
			}
			
		} catch (IOException e) {
			System.err.println("Could not initialize server...");
		}
	}
	
	public void receiveDataFromClient() {
		ObjectInputStream in = null;
		ObjectOutputStream out = null;
		try {
			in = new ObjectInputStream(client.getInputStream());
			out = new ObjectOutputStream(client.getOutputStream());
		} catch (IOException e3) {
			System.err.println("Could not initialize streams...");
		}
		try {
			topK = in.readInt();
			minX = in.readDouble();
			maxX = in.readDouble();
			minY = in.readDouble();
			maxY = in.readDouble();
			setPosition(minX, maxX, minY, maxY);
		} catch (IOException e1) {
			System.err.println("Error loading values...");
			e1.printStackTrace();
		}
		try {
			minDatetime = (String) in.readObject();
			maxDatetime = (String) in.readObject();
		} catch (IOException | ClassNotFoundException e) {
			System.err.println("Error reading values");
			e.printStackTrace();
		} finally {
			try {
				//Maybe not close here!
				in.close();
				out.close();
				client.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	@Override
	public void waitForTasksThread() {
		Runnable threadRunnable = new Runnable() {
				
			@Override
			public void run() {
				synchronized (client) {
					receiveDataFromClient();
					System.out.println("\nValues have received succefully!");
					
					seperateMap(cores);
					System.out.println("\nMap Proccess is ready to begin......");
					
			        sendToReducers(map(Checkins_Area));
			        System.out.println("\nMap Complete! The intermediate results will be sent to Reducer.....\n");
				}
			}
		};
		
		Thread operation = new Thread(threadRunnable);
		operation.start();		
	}
	
	public void seperateMap(int coresNo) {
		Checkins_Area = new ArrayList<ListOfCheckins>();
		if (coresNo == 1)
			Checkins_Area.add(readFromDB(minY, maxY));
		else {
			double coreLength = (maxY - minY)/coresNo;
			maxY = coreLength+minY;
			for (int k = 1; k <= coresNo; k++) {
				Checkins_Area.add(readFromDB(minY, maxY));
				minY = maxY;
				maxY = maxY + coreLength;
			}
		}
	}
	
	public ListOfCheckins readFromDB(double CoreMinY, double CoreMaxY) {
		//checkins = new HashMap<String, Integer>();
		ListOfCheckins checkins = new ListOfCheckins();

		Connection con = null;
		java.sql.PreparedStatement pst = null;
		ResultSet rs = null;

		String url = "jdbc:mysql://83.212.117.76:3306/ds_systems_2016?autoReconnect=true&useSSL=false";
		String user = "omada8";
		String password = "omada8db";

		try {
			con = DriverManager.getConnection(url, user, password);
			pst = con.prepareStatement("select POI, POI_name, POI_category, POI_category_id, latitude, longitude, time, photos" 
					+" from checkins where (latitude between " + CoreMinY + " and " + CoreMaxY
					+ ") " + "and (longitude between " + minX + " and " + maxX + ") " + "and time between \'" + minDatetime +"\' and \'"+ maxDatetime +"\';");
			rs = pst.executeQuery();

			String POI, POI_name, POI_category, POI_category_id, time, photos;
			double latitude, longitude;
			
			while (rs.next()) {
				POI = rs.getString(1);
				POI_name = rs.getString(2);
				POI_category = rs.getString(3);
				POI_category_id = rs.getString(4);
				latitude = rs.getDouble(5);
				longitude = rs.getDouble(6);
				time = rs.getString(7);
				photos = rs.getString(8);
				checkins.addCheckin(POI, POI_name, POI_category, POI_category_id, longitude, latitude, time, photos);
			}
		} catch (SQLException ex) {
			Logger lgr = Logger.getLogger(Mapper.class.getName());
			lgr.log(Level.SEVERE, ex.getMessage(), ex);

		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (pst != null) {
					pst.close();
				}
				if (con != null) {
					con.close();
				}

			} catch (SQLException ex) {
				Logger lgr = Logger.getLogger(Mapper.class.getName());
				lgr.log(Level.WARNING, ex.getMessage(), ex);
			}
		}
		return checkins;
	}

	@Override
	public Map<Object, List> map(List<ListOfCheckins> checkins) {
		Map<Object, List> intermediateMap = new LinkedHashMap<Object, List>();
		List<Map.Entry<Object, List<Checkin>>> intermediateList = new ArrayList<Map.Entry<Object, List<Checkin>>>();
		
		intermediateList = checkins.stream().flatMap(s->s.getCheckinsList().stream())
				.collect(Collectors.groupingBy(Checkin::getPOI)).entrySet()
				.stream().collect(Collectors.toList());
				
						
		intermediateList = intermediateList.stream().parallel()
				.sorted((e1, e2) -> Integer.compare(e2.getValue().size(), e1.getValue().size()))
				.collect(Collectors.toList());
				 
		//check if user input is greater than List Size
		if (topK > intermediateList.size()) {
			topK = intermediateList.size();
		}
		
		if (topK != 0) {
	        for(int top = 0; top < this.topK; top++){
	        	Map.Entry<Object, List<Checkin>> item = intermediateList.get(top);
	        	List<String> photo_list = item.getValue().stream().map(h->h.getPhotoURL())
	        			.collect(Collectors.toList());
	        	
	        	//Data for each Checkin based on intermediateList
	        	List<Object> checkinDetails = new ArrayList<Object>();
	        	checkinDetails.add(item.getValue().get(0).getPOI());
	        	checkinDetails.add(item.getValue().get(0).getPOI_name());
	        	checkinDetails.add(photo_list);
	        	checkinDetails.add(photo_list.size());
	        	checkinDetails.add(item.getValue().get(0).getLongitude());
	        	checkinDetails.add(item.getValue().get(0).getLatitude());
	        	
	        	//Put every checkin with its details into intermediaMap
	        	intermediateMap.put(item.getKey(), checkinDetails);
	        }
		}
        
		return intermediateMap;
    }

	@Override
	public void notifyMaster() {
		// TODO Auto-generated method stub

	}

	@Override
	public void sendToReducers(Map<Object, List> toReducer) {
		ObjectInputStream in = null;
		ObjectOutputStream out = null;
		
		try {
			reducer = new Socket(InetAddress.getByName(reducer_address), reducer_port);
			
			out = new ObjectOutputStream(reducer.getOutputStream());
			in = new ObjectInputStream(reducer.getInputStream());
				
				out.writeObject(toReducer);
				out.flush();
				
				out.writeInt(topK);
				out.flush();
				
				try {
					String msg = (String) in.readObject();
					System.out.println("\n"+msg);
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				}
		}
		catch (IOException e) {
			System.err.println("Could not connect to Reducer...");
		}
		finally {
			if (!reducer_address.equals("localhost")) {
				try {
					out.close();
					in.close();
					reducer.close();
				}
				catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public boolean checkPortAvailability(int port) {
		ServerSocket available = null;
		try {
			available = new ServerSocket(port);
			available.close();
			return true;
		} catch (IOException e) {
			return false;
		}
	}
}
