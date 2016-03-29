import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.List;

public class ConnectToMapper extends Thread {

	private double minX, maxX, minY, maxY;
	private String datetime;

	private String address;
	private int port;

	private Socket client = null;
	private ObjectInputStream in = null;
	private ObjectOutputStream out = null;

	//This constructor is used only for Testing
	public ConnectToMapper(String address, int port) {
		this(-74.9, -73.9, 40.6, 40.9, "2012-04-04 00:00:00");
		this.address = address;
		this.port = port;
	}

	public ConnectToMapper(double minX, double maxX, double minY, double maxY, String datetime, String address, int port) {
		setValues(minX, maxX, minY, maxY, datetime);
		this.address = address;
		this.port = port;
	}
	
	public ConnectToMapper(List<Double> values, String datetime, String address, int port) {
		setValues(values.get(0), values.get(1), values.get(2), values.get(3), datetime);
		this.address = address;
		this.port = port;
	}
	
	public ConnectToMapper(double minX, double maxX, double minY, double maxY, String datetime) {
		setValues(minX, maxX, minY, maxY, datetime);
	}
	
	public void setValues(double minX, double maxX, double minY, double maxY, String datetime) {
		this.minX = minX;
		this.maxX = maxX;
		this.minY = minY;
		this.maxY = maxY;
		this.datetime = datetime;
	}
	
	public void connect() {	//this method establishes the connection
		try {
			client = new Socket(InetAddress.getByName(address), port);
			// in = new ObjectInputStream(client.getInputStream());
			out = new ObjectOutputStream(client.getOutputStream());

			out.writeDouble(minX);
			out.flush();
			out.writeDouble(maxX);
			out.flush();
			out.writeDouble(minY);
			out.flush();
			out.writeDouble(maxY);
			out.flush();
			out.writeObject(datetime);
			out.flush();
		} catch (UnknownHostException e) {
			System.err.println("Could not find host...");
		} catch (IOException e) {
			System.err.println("Could not connect...");
		}
	}

	public void run() { //this method runs the thread
		synchronized (this) {
			connect();
		}
	}

}
