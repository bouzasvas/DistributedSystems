import java.util.Scanner;
import java.net.*;
import java.io.*;

public class Master {
	static Socket client;
	static Mapper mapper;
	static Reducer reducer;
	static int port;
	static int function;
	static Scanner input = new Scanner(System.in);

		public static void main(String[] args) {
			
			System.out.println("Press 1 for Client\n2 for Mapper\nand 3 for Reducer");
			function = input.nextInt();
			
			if (function == 1) {
				client
			}
			else if (function == 2) {
				System.out.println("Which port would you like to use for Mapper?");
				port = input.nextInt();
				mapper = new Mapper(port);
				mapper.initialize();
			}
			else if (function == 3) {
				System.out.println("Which port would you like to use for Reducer?");
				port = input.nextInt();
				Reducer reducer = new Reducer(port);
				reducer.initialize();
			}
			
	
		}

}
