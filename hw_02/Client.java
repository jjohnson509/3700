/*
 * Client App upon UDP
 * Weiying Zhu
 */

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.Date;

public class Client {
public static void main(String[] args) throws IOException {

        // create a UDP socket
        DatagramSocket udpSocket = new DatagramSocket();
        BufferedReader sysIn = new BufferedReader(new InputStreamReader(System.in));
        String[] itemID = {"00001", "00002", "00003", "00004", "00005", "00006"};
        String[] itemDescr = {"New Inspiron 15", "New Inspiron 17",
                              "New Inspiron 15R", "New Inspiron 15z Ultrabook",
                              "XPS 14 Ultrabook", "New XPS 12 UltrabookXPS"};
        String fromServer;
        String fromUser;
        String host_dns;
        String userInput = null;
        int attempts = 0;
        boolean carryOn = false;
        boolean tryAgain = true;

        //gets Server DNS from user
        System.out.println("Please input the DNS or IP of the server machine.");
        while((host_dns = sysIn.readLine()).isEmpty()) {
                System.out.println("Please enter something, perhaps valid input?");
                System.out.println("Hint:(cs3700a.msudenver.edu)");
        }


        while(tryAgain) {
                //displays table of items for user to pick from
                System.out.println("\n");
                tableGen(itemID, itemDescr);
                System.out.println("\n");

                //starts while loop, checking for valid itemID input
                while(carryOn == false) {
                        attempts++;
                        System.out.println("Please enter an Item ID listed above:");
                        while((userInput = sysIn.readLine()).isEmpty()) {
                                System.out.println("Please enter something, perhaps a listed Item ID?");
                        }
                        for(String value: itemID) {
                                if(value.equals(userInput)) {
                                        System.out.println("Input is valid!");
                                        attempts = 0;
                                        carryOn = true;
                                }
                        }
                        //if user inputs wrong 5 times, reprints table
                        if(attempts > 4) {
                                System.out.println("Please enter one of the listed Item ID's.");
                                tableGen(itemID, itemDescr);
                                attempts = 0;
                        }
                }

                //send request to target from user input;
                InetAddress address = InetAddress.getByName(host_dns);
                byte[] bufOut = userInput.getBytes();
                DatagramPacket udpPacketOut = new DatagramPacket(bufOut, bufOut.length, address, 5160);
                Date currentDate = new Date();
                long sendTime = currentDate.getTime();
                udpSocket.send(udpPacketOut);


                //receive quote data from server
                byte[] bufIn = new byte[256];
                DatagramPacket udpPacketIn = new DatagramPacket(bufIn, bufIn.length, address, 5160);
                udpSocket.receive(udpPacketIn);
                fromServer = new String(udpPacketIn.getData(), 0, udpPacketIn.getLength());
                String[] values = fromServer.split(",");

                //get receive time of packet and calculate total time difference
                Date currentDate2 = new Date();
                long receiveTime = currentDate2.getTime();
                long timeDiff = receiveTime - sendTime;

                //display method to print data to console in appropriate format
                dispQuoteFromServer(values, timeDiff);

                //Present user option to quit
                System.out.println("Press N to quit, any key to continue.");
                String input = sysIn.readLine();
                if (input.equals("n") || input.equals("N")) {
                        tryAgain = false;
                }
                else{carryOn = false;}
        }
        //close the socket, yeah!!
        udpSocket.close();
}


public static void dispQuoteFromServer(String[] item, long time){

        String format = "|%1$-10s|%2$-26s|%3$-10s|%4$-10s|%5$-14s|\n";
        String format1 = "+%1$-10s+%2$-26s+%3$-10s+%4$-10s+%5$-14s+\n";
        System.out.format(format1, "----------", "--------------------------",
                          "----------", "----------", "--------------");
        System.out.format(format, "Item ID", "Item Description", "Unit Price", "Inventory", "RTT of Query");
        System.out.format(format1, "----------", "--------------------------",
                          "----------", "----------", "--------------");
        System.out.format(format, item[0], item[1], item[2], item[3], " "+time+" ms");
        System.out.format(format1, "----------", "--------------------------",
                          "----------", "----------", "--------------");
}

public static void tableGen(String[] headers, String[] items){
        String format = "|%1$-10s|%2$-30s|\n";
        String format1 = "+%1$-10s+%2$-30s+\n";
        System.out.format(format1, "----------", "------------------------------");
        System.out.format(format, "Item ID", "Item Description");
        System.out.format(format1, "----------", "------------------------------");
        for(int i = 0; i < 6; i++) {
                System.out.format(format, headers[i], items[i]);
        }
        System.out.format(format1, "----------", "------------------------------");
}
}
