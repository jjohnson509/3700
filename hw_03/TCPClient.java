/*
 * Client App upon TCP
 *
 * Weiying Zhu
 *
 */ 

import java.io.*;
import java.net.*;
import java.util.Date;

public class TCPClient {
    public static void main(String[] args) throws IOException {

        Socket tcpSocket = null;
        PrintWriter socketOut = null;
        BufferedReader socketIn = null;
        BufferedReader sysIn = new BufferedReader(new InputStreamReader(System.in));

        System.out.println("Please enter DNS name or IP of the HTTP server.");
        String serverInput;
        while(!(serverInput = sysIn.readLine()).equals("cs3700a.msudenver.edu")){
            System.out.println("Please enter DNS name or IP of the HTTP server.");
            System.out.println("Hint: cs3700a.msudenver.edu");
        }

        long startTime = System.currentTimeMillis();
        try {
            tcpSocket = new Socket(serverInput, 5160);
            socketOut = new PrintWriter(tcpSocket.getOutputStream(), true);
            socketIn = new BufferedReader(new InputStreamReader(tcpSocket.getInputStream()));
        } catch (UnknownHostException e) {
            System.err.println("Don't know about host: " + serverInput);
            System.exit(1);
        } catch (IOException e) {
            System.err.println("Couldn't get I/O for the connection to: " + serverInput);
            System.exit(1);
        }
        long endTime = System.currentTimeMillis();
        long rtt = endTime - startTime;
        System.out.println("RTT: " + rtt  + " ms");
        String userAnswers = askUserQuestions(serverInput);
        socketOut.print(userAnswers);
        socketOut.flush();

        String fromServer;
        String fromUser;
        if ((fromServer = socketIn.readLine()) != null)
        {
            System.out.println("Server: " + fromServer);
        }
        while ((fromUser = sysIn.readLine()) != null) {
		      System.out.println("Client: " + fromUser);
            socketOut.println(fromUser);
				
				if ((fromServer = socketIn.readLine()) != null)
				{
					System.out.println("Server: " + fromServer);
				}
				else {
                System.out.println("Server replies nothing!");
                break;
				}
		    
			   if (fromUser.equals("Bye."))
					break;
         
        }

        socketOut.close();
        socketIn.close();
        sysIn.close();
        tcpSocket.close();
    }

    public static String askUserQuestions(String host) throws IOException {
        BufferedReader sysIn = new BufferedReader(new InputStreamReader(System.in));
        String temp;
        String httpRequest = null;
        System.out.println("Please input the HTTP method type.");
        while((temp = sysIn.readLine()).isEmpty()){
            System.out.println("Item cannot be blank.");
        }
        httpRequest = temp +" /";
        System.out.println("File name? (.htm)");
        while((temp = sysIn.readLine()).isEmpty()){
            System.out.println("Item cannot be blank.");
        }
        httpRequest = httpRequest + temp + " HTTP/";
        System.out.println("HTTP version?");
        while((temp = sysIn.readLine()).isEmpty()){
            System.out.println("Item cannot be blank.");
        }
        httpRequest = httpRequest + temp + "\r\n";
        httpRequest = httpRequest + "Host: " + host + "\r\n";
        System.out.println("User Agent?");
        while((temp = sysIn.readLine()).isEmpty()){
            System.out.println("Item cannot be blank.");
        }
        httpRequest = httpRequest + "User Agent: " + temp + "\r\n";
        System.out.println("Thank You! \t ... \t ... \t ...\n" +
                           "Sending Request! \t ... \t ... \t ...");
        return httpRequest;
    }
}
