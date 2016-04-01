package core;
import java.util.*;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.net.*;
import java.sql.*;
import java.io.*;

public class Mapper implements MapWorker {

	//private Map<String, Integer> checkins = null;
	
	private List<ListOfCheckins> Checkins_Area = new ArrayList<ListOfCheckins>();
	
	private ServerSocket mapper = null;
	private Socket client = null;
	private int mapper_port = 0;
	
	private int cores;
	
	private double minX, maxX, minY, maxY = 0; // X is Longtitude, Y is Latitude
	private String datetime;

	public Mapper(int port) {
		if (!checkPortAvailability(port)) {
			System.out.println("Port is in use");
			System.out.println("Run again the program with another port");
			System.exit(-1);
		} else
			this.mapper_port = port;
	}

	public Mapper(int port, double minX, double maxX, double minY, double maxY, String datetime) {
		this.mapper_port = port;
		setPosition(minX, maxX, minY, maxY);
		setDate(datetime);
	}

	public void setValues(double minX, double maxX, double minY, double maxY, String datetime) {
		setPosition(minX, maxX, minY, maxY);
		setDate(datetime);
	}

	private void setPosition(double minX, double maxX, double minY, double maxY) {
		this.minX = minX;
		this.maxX = maxX;
		this.minY = minY;
		this.maxY = maxY;
	}

	private void setDate(String datetime) {
		this.datetime = datetime;
	}

	@Override
	public void initialize() {
		try {
			cores = Runtime.getRuntime().availableProcessors();
			System.out.println("Mapper has "+cores+" cores");
			
			mapper = new ServerSocket(mapper_port);
			client = mapper.accept();
			
			ObjectOutputStream ack = new ObjectOutputStream(client.getOutputStream());
			ack.writeObject("Succesfully connected to "+client.getInetAddress()+
					" at port "+client.getLocalPort());
		} catch (IOException e) {
			System.err.println("Could not initialize server...");
		}
		Thread init = new Thread(initValues()); //To-Review
		init.start();
		
		seperateMap(cores);
		map(Checkins_Area);
	}

	@Override
	public void waitForTasksThread() {
		// TODO Auto-generated method stub

	}
	
	public void seperateMap(int coresNo) {
		if (coresNo == 1)
			Checkins_Area.add(readFromDB(minY, maxY));
		else {
			maxY = ((maxY - minY)/coresNo)+minY;
			for (int k = 1; k <= coresNo; k++) {
				Checkins_Area.add(readFromDB(minY, maxY));
				double minYtmp = minY;
				minY = maxY;
				maxY = maxY + minYtmp;
			}
		}
		for ( ListOfCheckins check : Checkins_Area) {
			check.printCheckins();
			System.out.println("**************************************************");
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
					+ ") " + "and (longitude between " + minX + " and " + maxX + ") " + "and time > STR_TO_DATE('"
					+ datetime + "', '%Y-%m-%d %H:%i:%s') limit 50;");
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
				//checkins.put(tmp, 1);
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
	public Map<String, Integer> map(List<ListOfCheckins> checkins) {		
		Map<String, Integer> intermediateMap = new HashMap<String, Integer>();
		int numberOfCheckins;
					
		//intermediateMap = checkins.stream().parallel().filter(p -> p.getCheckin(0).getPOI().contains("4")).map(p -> p.count(p.getCheckin(0).getPOI())).collect(Collectors.groupingBy(Checkin::getPOI));
//		numberOfCheckins = lines.stream().parallel().filter(p -> p.getLine().contains("test"))
//							.map(p -> p.count("test")).reduce((sum, p) -> sum + p).get();
			
		return intermediateMap;
	}

	@Override
	public void notifyMaster() {
		// TODO Auto-generated method stub

	}

	@Override
	public void sendToReducers(Map<Integer, Object> toReducer) {
		// TODO Auto-generated method stub

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

	public Runnable initValues() {
		Runnable runnable = new Runnable() {
			@Override
			public void run() {
				synchronized (client) {
					//here or as class members??
					ObjectInputStream in = null;
					ObjectOutputStream out = null;
					try {
						in = new ObjectInputStream(client.getInputStream());
						out = new ObjectOutputStream(client.getOutputStream());
					} catch (IOException e3) {
						System.err.println("Could not initialize streams...");
					}
					try {
						minX = in.readDouble();
						maxX = in.readDouble();
						minY = in.readDouble();
						maxY = in.readDouble();
					} catch (IOException e1) {
						System.err.println("Error loading values...");
						e1.printStackTrace();
					}
					try {
						datetime = (String) in.readObject();
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
			};
		};
		return runnable;
	}
}