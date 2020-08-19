

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class Client {

	public static void main(String[] args) throws UnknownHostException, IOException {
		Scanner input = new Scanner(System.in); 
		// TODO Auto-generated method stub
	      // create a client 
	      Socket Client = new Socket("localhost", 1337);
	      
	      // time to write to the server
	      
	      OutputStream out = Client.getOutputStream();
	      while (true) {
	      System.out.println("your message : ");
	      out.write(input.nextLine().getBytes());
	      out.flush();
	      // time to read from the server
	      DataInputStream in = new DataInputStream(Client.getInputStream());
	      int read = -1;
	     
	      while ((read = in.read()) != -1) {
	    	 // System.out.println(read = in.read());
	          System.out.print((char)in.read());
	      }
	      System.out.print("fin read");
	      }
	}

}
