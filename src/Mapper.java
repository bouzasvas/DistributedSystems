import java.util.Map;
import java.net.*;
import java.io.*;

public class Mapper implements MapWorker {

	public void connect() throws IOException, FileNotFoundException {
		File file = new File("port_example");
		FileReader reader = new FileReader(file);
		BufferedReader bf = new BufferedReader(reader);
		String line = bf.readLine();
		int port = Integer.parseInt(line);
		
		ServerSocket server = new ServerSocket(port);
		Socket client;
		while (true) {
			client = server.accept();
			InputStream in = client.getInputStream();
			InputStreamReader isr = new InputStreamReader(in);
			BufferedReader bsr = new BufferedReader(isr);
			System.out.print("Client> "+bsr.readLine());
			client.close();
			in.close();
		}
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
