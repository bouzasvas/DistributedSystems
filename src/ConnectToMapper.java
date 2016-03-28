import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class ConnectToMapper {

	private double minX, maxX, minY, maxY;
	private String datetime;

	private Socket client = null;
	private ObjectInputStream in = null;
	private ObjectOutputStream out = null;

	public ConnectToMapper() {
		this.minX = -74.9;
		this.maxX = -73.9;
		this.minY = 40.6;
		this.maxY = 40.9;
		this.datetime = "2012-04-04 00:00:00";
	}

	public ConnectToMapper(double minX, double maxX, double minY, double maxY,
			String datetime) {
		this.minX = minX;
		this.maxX = maxX;
		this.minY = minY;
		this.maxY = maxY;
		this.datetime = datetime;
	}

	public void connect(String address, int port) {
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
		} finally {
			try {
				client.close();
				// in.close();
				out.close();
			} catch (IOException e) {
				System.err.println("Error closing streams...");
			}

		}
	}

}
