package core;
import java.util.*;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.net.*;
import java.sql.*;
import java.io.*;

public class Mapper implements MapWorker {

	private Map<String, Integer> checkins = null;
	private ServerSocket mapper = null;
	private Socket client = null;
	private int mapper_port = 0;
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

	public void readFromDB() {
		checkins = new HashMap<String, Integer>();

		Connection con = null;
		java.sql.PreparedStatement pst = null;
		ResultSet rs = null;

		String url = "jdbc:mysql://83.212.117.76:3306/ds_systems_2016?autoReconnect=true&useSSL=false";
		String user = "omada8";
		String password = "omada8db";

		try {
			con = DriverManager.getConnection(url, user, password);
			pst = con.prepareStatement("select POI" + " from checkins where (latitude between " + minY + " and " + maxY
					+ ") " + "and (longitude between " + minX + " and " + maxX + ") " + "and time > STR_TO_DATE('"
					+ datetime + "', '%Y-%m-%d %H:%i:%s');");
			rs = pst.executeQuery();

			while (rs.next()) {
				String tmp;
				tmp = rs.getString(1);
				checkins.put(tmp, 1);
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
	}

	@Override
	public void initialize() {
		try {
			// readFromDB();
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
		readFromDB();
		map(checkins);
	}

	@Override
	public void waitForTasksThread() {
		// TODO Auto-generated method stub

	}

	@Override
	public Map<String, Integer> map(Map<String, Integer> checkins) {
			
		Map<String, Integer> intermediateMap = new HashMap<String, Integer>();
		int numberOfCheckins;
		String POI;
		
		Iterator it = checkins.entrySet().iterator();
		
		while (it.hasNext()) {
			Map.Entry pair = (Map.Entry) it.next();
			POI = (String) pair.getKey();
			numberOfCheckins = checkins.get(POI);
			
			Map.Entry current = pair;
			while(current.getKey().equals(POI)) {
			numberOfCheckins++;
			
//			 numberOfCheckins = lines.stream().parallel().filter(p -> p.getLine().contains("test"))
//		               .map(p -> p.count("test")).reduce((sum, p) -> sum + p).get();
			
			System.out.println(pair.getKey()+"||"+pair.getValue());
			pair = (Map.Entry) it.next();
		}
			intermediateMap.put((String) pair.getKey(), numberOfCheckins);
			it.remove();
		}
		
		Iterator it1 = intermediateMap.entrySet().iterator();
		
		while (it.hasNext()) {
			Map.Entry pair = (Map.Entry) it.next();
			System.out.println(pair.getKey()+"||"+pair.getValue());
		}
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
