import java.io.*;
import java.net.*;

public class BotmasterMain {
    public static void main (String a[]) throws IOException {
    int q_len = 6;
    int port = 4444;
    Socket sock;
    
    ServerSocket servsock = new ServerSocket (port, q_len);
    
while (true) {
    //Wait for the next client connection
    sock = servsock.accept();
    new Botmaster (sock).start();
    }
 }
    
}
