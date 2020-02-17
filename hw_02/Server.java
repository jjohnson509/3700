/*
 * Server App upon UDP
 * Weiying Zhu
 */

import java.io.*;
import java.net.*;
import java.util.*;

public class Server {
public static void main(String[] args) throws IOException {

        String[] itemID = {"00001", "00002", "00003", "00004", "00005", "00006"};
        String[] itemDescr = {"New Inspiron 15", "New Inspiron 17",
                              "New Inspiron 15R", "New Inspiron 15z Ultrabook",
                              "XPS 14 Ultrabook", "New XPS 12 UltrabookXPS"};
        String[] itemPrice = {"$379.99", "$449.99", "$549.99", "$749.99",
                              "$999.99", "$1199.99"};
        String[] itemInv = {"157", "128", "202", "315", "261", "178"};

        DatagramSocket udpServerSocket = null;
        BufferedReader in = null;
        DatagramPacket udpPacket = null, udpPacket2 = null;
        String fromClient = null;
        String toClient = null;
        boolean morePackets = true;

        byte[] buf = new byte[256];

        udpServerSocket = new DatagramSocket(5160);
        Date currentDate = new Date();
        while (morePackets) {
                try {

                        // receive UDP packet from client
                        udpPacket = new DatagramPacket(buf, buf.length);
                        udpServerSocket.receive(udpPacket);

                        fromClient = new String(
                                udpPacket.getData(), 0, udpPacket.getLength());

                        // get the response
                        toClient = returnItems(fromClient, currentDate.getTime());

                        // send the response to the client at "address" and "port"
                        InetAddress address = udpPacket.getAddress();
                        int port = udpPacket.getPort();
                        byte[] buf2 = toClient.getBytes();
                        udpPacket2 = new DatagramPacket(buf2, buf2.length, address, port);
                        udpServerSocket.send(udpPacket2);

                } catch (IOException e) {
                        e.printStackTrace();
                        morePackets = false;
                }
        }

        udpServerSocket.close();

}

public static String returnItems(String itemNum, long timeReceived){
        long timeDiff = 0;
        String[] itemID = {"00001", "00002", "00003", "00004", "00005", "00006"};
        String[] itemDescr = {"New Inspiron 15", "New Inspiron 17",
                              "New Inspiron 15R", "New Inspiron 15z Ultrabook",
                              "XPS 14 Ultrabook", "New XPS 12 UltrabookXPS"};
        String[] itemPrice = {"$379.99", "$449.99", "$549.99", "$749.99",
                              "$999.99", "$1199.99"};
        String[] itemInv = {"157", "128", "202", "315", "261", "178"};
        int num = Integer.parseInt(itemNum) - 1;
        String temp = itemID[num] + "," + itemDescr[num] + "," +itemPrice[num]+
                      "," +itemInv[num]+ "," +timeDiff;
        return temp;
}

// public static void quoteToClient(String itemNum){
//
//         String format = "|%1$-10s|%2$-30s|%3$-20s|%4$-20s|\n";
//         String format1 = "+%1$-10s+%2$-30s+%3$-20s+%4$-20s+\n";
//         System.out.format(format1, "----------", "-----------------------------",
//                           "--------------------", "--------------------");
//         System.out.format(format, "Item ID", "Item Description", "Unit Price", "Inventory");
//         System.out.format(format1, "----------", "-----------------------------",
//                           "--------------------", "--------------------");
//         int temp = Integer.parseInt(itemNum) - 1;
//         System.out.format(format, itemID[temp], itemDescr[temp], itemPrice[temp], itemInv[temp]);
//         System.out.format(format1, "----------", "-----------------------------",
//                           "--------------------", "--------------------");
// }
}
