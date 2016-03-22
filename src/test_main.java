import java.io.*;
import java.net.*;

public class test_main {

	public static void main(String[] args) throws FileNotFoundException, IOException {
		// TODO Auto-generated method stub
		for (int i = 0; i<args.length; i++) {
			System.out.println(args[i]);
		}
		String funct = args[0];
		int fun = Integer.parseInt(funct);
		if (fun == 1) {
			Mapper mapper = new Mapper();
			mapper.connect();
		}
		if (fun == 2) {
			Client client = new Client();
			client.connect();
		}
	}

}
