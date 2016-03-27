import java.util.*;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.net.*;
import java.sql.*;
import java.io.*;

public class Mapper implements MapWorker {

	private List<Integer> checkins = null;
	private ServerSocket mapper = null;
	private Socket client = null;
	private int mapper_port = 0;
	private double minX, maxX, minY, maxY = 0; // X is Longtitude, Y is Latitude
	private String datetime;

	public Mapper(int port, double minX, double maxX, double minY, double maxY, String datetime) {
		this.mapper_port = port;
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
		checkins = new ArrayList<Integer>();

		Connection con = null;
		java.sql.PreparedStatement pst = null;
		ResultSet rs = null;

		String url = "jdbc:mysql://localhost:3306/sys?autoReconnect=true&useSSL=false";
		String user = "admin";
		String password = "admin";

		try {
			con = DriverManager.getConnection(url, user, password);
			pst = con.prepareStatement("select id" + "from checkins where (latitude between "+minY+" and "+maxY+") "
					+ "and (longitude between "+ minX+ " and "+ maxX+") "
					+ "and time > STR_TO_DATE('"+datetime+"', '%Y-%m-%d %H:%i:%s')" + "limit 5;");
			rs = pst.executeQuery();

			while (rs.next()) {
				String tmp;
				tmp = rs.getString(1);
				checkins.add(Integer.parseInt(tmp));
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
			readFromDB();
			mapper = new ServerSocket(mapper_port);
			client = mapper.accept();
			InputStream is = client.getInputStream();
			InputStreamReader ir = new InputStreamReader(is);
			BufferedReader br = new BufferedReader(ir);
			System.out.println(br.readLine());
		} catch (IOException e) {
			System.err.println("Could not initialize server");
		} finally {
			try {
				client.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void waitForTasksThread() {
		// TODO Auto-generated method stub

	}

	@Override
	public Map<Integer, Object> map(Object key, Object value) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void notifyMaster() {
		// TODO Auto-generated method stub

	}

	@Override
	public void sendToReducers(Map<Integer, Object> toReducer) {
		// TODO Auto-generated method stub

	}

}
