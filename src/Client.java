import java.net.*;
import java.io.*;


public class Client {
	
	public void connect() throws IOException, FileNotFoundException {
		File file = new File("port_example");
		FileReader reader = new FileReader(file);
		BufferedReader bf = new BufferedReader(reader);
		String line = bf.readLine();
		int port = Integer.parseInt(line);
		
		Socket client = new Socket(InetAddress.getByName("127.0.0.1"), port);
		OutputStream out = client.getOutputStream();
		OutputStreamWriter ow = new OutputStreamWriter(out);
		ow.write("Hello from Client");
		ow.flush();
		out.flush();
		out.close();
		client.close();
	}
}
