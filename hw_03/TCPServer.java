/*
 * Server App upon TCP
 */ 

import java.net.*;
import java.io.*;

public class TCPServer {
    public static void main(String[] args) throws IOException {

        ServerSocket serverTcpSocket = null;
        try {
            serverTcpSocket = new ServerSocket(5160);
        } catch (IOException e) {
            System.err.println("Could not listen on port: 4567.");
            System.exit(1);
        }

        Socket clientTcpSocket = null;
        try {
            clientTcpSocket = serverTcpSocket.accept();
        } catch (IOException e) {
            System.err.println("Accept failed.");
            System.exit(1);
        }

        PrintWriter cTcpSocketOut = new PrintWriter(clientTcpSocket.getOutputStream(), true);
        BufferedReader cTcpSocketIn = new BufferedReader(
				new InputStreamReader(
				clientTcpSocket.getInputStream()));
        String fromClient, toClient;
        
		  while ((fromClient = cTcpSocketIn.readLine()) != null)
		  {
		       toClient =fromClient.toUpperCase();
          
				 cTcpSocketOut.println(toClient);
				 
             if (fromClient.equals("Bye."))
                break;
        }
		  
        cTcpSocketOut.close();
        cTcpSocketIn.close();
        clientTcpSocket.close();
        serverTcpSocket.close();
    }
	 
	 
}
