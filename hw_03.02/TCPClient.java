/*
 * Client App upon TCP
 *
 * Weiying Zhu
 *
 */

import java.io.*;
import java.net.*;
import java.util.Date;
import java.util.Scanner;

public class TCPClient {
    public static void main(String[] args) throws IOException {

        Socket tcpSocket = null;
        boolean continueCheck = true;
        PrintWriter socketOut = null;
        BufferedReader socketIn = null;
        BufferedReader sysIn = new BufferedReader(new InputStreamReader(System.in));

        System.out.println("Please enter DNS name or IP of the HTTP server.");
        String serverInput;
        while (!(serverInput = sysIn.readLine()).equals("cs3700a.msudenver.edu")) {
            System.out.println("Please enter DNS name or IP of the HTTP server.");
            System.out.println("Hint: cs3700a.msudenver.edu");
        }

        long startTime = System.currentTimeMillis();
        try {
            tcpSocket = new Socket(serverInput, 5160);
            socketOut = new PrintWriter(tcpSocket.getOutputStream(), true);
            socketIn = new BufferedReader(new InputStreamReader(tcpSocket.getInputStream()));
        } catch (UnknownHostException e) {
            System.err.println("Do not recognize host: " + serverInput);
            System.exit(1);
        } catch (IOException e) {
            System.err.println("Could not get I/O for the connection at: " + serverInput);
            System.exit(1);
        }
        boolean endOfFile = false;
        long endTime = System.currentTimeMillis();
        long rtt = endTime - startTime;
        System.out.println("\n[ RTT ] : * * * [" + rtt + " ms ] * * *\n");

        while (continueCheck) {
            String userAnswers = askUserQuestions(serverInput);
            System.out.println("------------------------------------------------------------");
            System.out.print(userAnswers.substring(0, userAnswers.lastIndexOf("\r")));
            System.out.println("------------------------------------------------------------\n");
            System.out.println("... \t ... \t ... \t ... \t ... \t ... \t ... \t ...");
            System.out.println("... \t ... \t ... \t ... \t ... \t ... \t ... \t ...\n");
            socketOut.print(userAnswers);
            socketOut.flush();


            String fromServer;
            String fromUser;


            long requestStart = System.currentTimeMillis();
            while ((fromServer = socketIn.readLine()) != null) {
                if(fromServer.equals(")*()*("))
                    break;
                System.out.println(fromServer);

            }
            long requestEnd = System.currentTimeMillis() - requestStart;
            System.out.println("[ RTT   : ---  " + requestEnd + " ms --- ]");
            System.out.println("Would you like to try again? (N to quit, any to continue)");
            Scanner stdIn = new Scanner(System.in);
            String userInput = stdIn.nextLine();
            if (userInput.equals("N") || userInput.equals("n")) {
                continueCheck = false;
            }
        }
//            while ((fromUser = sysIn.readLine()) != null) {
//                System.out.println("Client: " + fromUser);
//                socketOut.println(fromUser);
//                if ((fromServer = socketIn.readLine()) != null) {
//                    System.out.println("Server: " + fromServer);
//                } else {
//                    System.out.println("Server replies nothing!");
//                    break;
//                }
//
//                if (fromUser.equals("Bye."))
//                    break;
//
//            }


        socketOut.close();
        socketIn.close();
        sysIn.close();
        tcpSocket.close();
    }

    public static String askUserQuestions(String host) throws IOException {
        BufferedReader sysIn = new BufferedReader(new InputStreamReader(System.in));
        String temp;
        String httpRequest;
        System.out.println("Please input the HTTP method type.");
        while ((temp = sysIn.readLine()).isEmpty()) {
            System.out.println("Item cannot be blank.");
        }
        httpRequest = temp.toUpperCase() + " /";
        System.out.println("File name? (.htm)");
        while ((temp = sysIn.readLine()).isEmpty()) {
            System.out.println("Item cannot be blank.");
        }
        httpRequest = httpRequest + temp + " HTTP/";

        System.out.println("HTTP version?");
        while ((temp = sysIn.readLine()).isEmpty()) {
            System.out.println("Item cannot be blank.");
        }
        httpRequest = httpRequest + temp + "\r\n";
        httpRequest = httpRequest + "Host: " + host + "\r\n";
        System.out.println("User Agent?");
        while ((temp = sysIn.readLine()).isEmpty()) {
            System.out.println("Item cannot be blank.");
        }
        httpRequest = httpRequest + "User-Agent: " + temp + "\r\n\r\n";
        System.out.println("... \t ... \t ... \t Thank You! \t ... \t ... \t ...\n" +
                "... \t ... \t ...  Sending Request! \t ... \t ... \t ...\n");
        return httpRequest;
    }
}
