/*
 * Server App upon TCP
 * A thread is started to handle every client TCP connection to this server
 * Weiying Zhu
 */

import java.lang.reflect.Array;
import java.net.*;
import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class TCPMultiServerThread extends Thread {
    private Socket clientTCPSocket = null;
    final String dir = System.getProperty("user.dir");

    public TCPMultiServerThread(Socket socket) {
        super("TCPMultiServerThread");
        this.clientTCPSocket = socket;
    }

    public void run() {
        try {
            PrintWriter cSocketOut = new PrintWriter(clientTCPSocket.getOutputStream(), true);
            BufferedReader cSocketIn = new BufferedReader(
                    new InputStreamReader(
                            clientTCPSocket.getInputStream()));

            String fromClient, toClient = null, method, requestedFile = null, host, userAgent, httpVersion = null;
            String[] requestStatus = {"400 Bad Request", "404 File Not Found", "200 OK"};
            int count = 0, status = 0;
            boolean goodRequest = false;

        while(clientTCPSocket.isConnected())
            while ((fromClient = cSocketIn.readLine()) != null) {
                count++;
                switch (count) {
                    case 1:
                        method = fromClient.toUpperCase().substring(0, fromClient.indexOf(" "));
                        if (!(method.equals("GET"))) {
//							System.out.println("[ STATUS: --- "+ statusArr[ --- ]");
//							cSocketOut.println("[ STATUS: --- 400 Bad Request --- ]");
                            httpVersion = fromClient.substring(fromClient.indexOf("HTTP"));
                            break;
                        }
                        String userFileRequest = fromClient.substring(fromClient.indexOf(" "), fromClient.indexOf("/"));
                        httpVersion = fromClient.substring(fromClient.indexOf("HTTP"));
//						System.out.println(httpVersion);
                        requestedFile = dir + userFileRequest;
                        File reqFile = new File(requestedFile);
//						System.out.println(requestedFile);
                        if (reqFile.isFile()) {
                            System.out.println("file found!");
                            status = 2;
                            goodRequest = true;
                        } else {
                            requestedFile = null;
                            status = 1;
                            cSocketOut.println("[ STATUS: --- 404 File Not Found --- ]");
                            System.out.println("[ STATUS: --- 404 File Not Found --- ]");
                        }
                        break;
                    case 2:
                        host = fromClient;
                        break;
                    case 3:
                        userAgent = fromClient;
                        break;
                    default:
                        break;
                }


                DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
                Date date = new Date();
                String timeStamp = dateFormat.format(date);


                if (goodRequest) {
                    String fileData = returnFile(requestedFile);
                    if(fileData == null){continue;}
                    else if(fromClient.equals("")) {
                        status = 2;
                        toClient = httpVersion + " " + requestStatus[status] + "\r\n" + "Date: " + timeStamp + "\r\n" + "Server: Apache/2.2.14\r\n\r\n" + fileData ;
                        cSocketOut.println(toClient);
                        System.out.println(toClient);
                        cSocketOut.println("[ STATUS: --- 200 OK --- ]");
                    }
                } else {
					toClient = httpVersion + " " + requestStatus[status] + "\r\n" + "Date: " + timeStamp + "\r\n" + "Server: Apache/2.2.14\r\n\r\n";
                    cSocketOut.println(toClient);

                }
                goodRequest = false;
                count = 0;
                return;


            }

            cSocketOut.close();
            cSocketIn.close();
            clientTCPSocket.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String returnFile(String filepath) throws FileNotFoundException {
        StringBuilder contentBuilder = new StringBuilder();
        if(filepath == null){
            return null;
        }
        try(BufferedReader br = new BufferedReader(new FileReader(filepath))){
            String sCurrentLine;
            while((sCurrentLine = br.readLine()) != null){
                contentBuilder.append(sCurrentLine).append("\n");
            }
            contentBuilder.append("\r\n\r\n\r\n\r\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return contentBuilder.toString();
    }
}
