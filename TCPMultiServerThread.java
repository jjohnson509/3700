/*
 * Server App upon TCP
 * A thread is started to handle every client TCP connection to this server
 * Weiying Zhu
 */ 

import java.net.*;
import java.io.*;

public class TCPMultiServerThread extends Thread {
    private Socket clientTCPSocket = null;
    final String dir = System.getProperty("user.dir");
    public TCPMultiServerThread(Socket socket) {
		super("TCPMultiServerThread");
		clientTCPSocket = socket;
    }

    public void run() {

		try {
	 	   PrintWriter cSocketOut = new PrintWriter(clientTCPSocket.getOutputStream(), true);
	  		BufferedReader cSocketIn = new BufferedReader(
				    new InputStreamReader(
				    clientTCPSocket.getInputStream()));

	      String fromClient, toClient, method, requestedFile, host, userAgent, httpVersion;
	      int count = 0;
	      boolean goodRequest = true;

			  
	 	   while ((fromClient = cSocketIn.readLine()) != null && goodRequest) {
	 	   		count++;
				switch (count){
					case 1:
						method = fromClient.toUpperCase().substring(0, fromClient.indexOf(" "));
						if(!(method.equals("GET"))){
							System.out.println("[ STATUS: --- 400 Bad Request --- ]");
							cSocketOut.println("[ STATUS: --- 400 Bad Request --- ]");
							goodRequest = false;
							break;
						}
						String userFileRequest = fromClient.substring(fromClient.indexOf("/"), fromClient.lastIndexOf(" "));
						httpVersion = fromClient.substring(fromClient.indexOf("HTTP"));
						System.out.println(httpVersion);
						requestedFile = dir + userFileRequest;
						File reqFile = new File(requestedFile);
						System.out.println(requestedFile);
						if(reqFile.isFile()){
							System.out.println("file found!");
							break;
						}
						else {
							cSocketOut.println("[ STATUS: --- 404 File Not Found --- ]");
							System.out.println("[ STATUS: --- 404 File Not Found --- ]");
							goodRequest = false;
							break;
						}
					case 2:
						host = fromClient;
						break;
					case 3:
						userAgent = fromClient;
						break;
					default:
						break;	
				}
				if(goodRequest) {
					toClient = fromClient;
					if (fromClient.equals("")) {
						System.out.println("[ STATUS: --- 200 OK --- ]");
						cSocketOut.println("[ STATUS: --- 200 OK --- ]");
						break;
					}
					cSocketOut.println(toClient);
					System.out.println(toClient);
				}
	 	   }
			
		   cSocketOut.close();
		   cSocketIn.close();
		   clientTCPSocket.close();

		} catch (IOException e) {
		    e.printStackTrace();
		}
    }
}
