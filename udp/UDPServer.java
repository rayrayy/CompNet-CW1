/*
 * Created on 01-Mar-2016
 */
package udp;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.Arrays;

import common.MessageInfo;

public class UDPServer {

	private DatagramSocket recvSoc;
	private int totalMessages = -1;
	private int[] receivedMessages;

	private void run() {
		int				pacSize;
		byte[]			pacData;
		DatagramPacket 	pac;

		// TO-DO: Receive the messages and process them by calling processMessage(...).
		//        Use a timeout (e.g. 30 secs) to ensure the program doesn't block forever
		pacSize = 500;
		pacData = new byte [pacSize];
		pac = new DatagramPacket(pacData, pacSize);

		while(true){

			try{
				recvSoc.setSoTimeout(30000);
				recvSoc.receive(pac);
			}

			catch (SocketTimeoutException time_out_exception){
				System.out.println("Server timed out! " + '\n' + "Server is closed.");
				System.exit(-1);
			}

			catch (IOException io_exception){
				System.out.println("IOException! "+ '\n' + "Server is closed.");
				System.exit(-1);
			}

			processMessage(new String(pac.getData()));

		}

	}

	public void processMessage(String data) {

		MessageInfo msg = null;

		// TO-DO: Use the data to construct a new MessageInfo object
		try{
					msg = new MessageInfo(data.trim());
		}
		catch (Exception exception){
					System.out.println("Failed to create MessageInfo! "+ '\n' + "Server is closed.");
					System.exit(-1);
		}
		// TO-DO: On receipt of first message, initialise the receive buffer
		if ((receivedMessages == null) || (msg.totalMessages != totalMessages)){
			totalMessages = msg.totalMessages;
			receivedMessages = new int[msg.totalMessages];
		}

		// TO-DO: Log receipt of the message
		receivedMessages[msg.messageNum] = 1;
		// TO-DO: If this is the last expected message, then identify
		//        any missing messages
		if (msg.messageNum + 1 == receivedMessages.length) {	//when last message is being received
					int missing = 0;
					for (int i=0; i < totalMessages; i++) {
						if (receivedMessages[i] != 1) {
							missing++;
						}
					}
					System.out.println("Number of missing messages: " + missing);
		}

	}


	public UDPServer(int rp) {
		// TO-DO: Initialise UDP socket for receiving data
		System.out.println("Initializing UDP socket.");
		try{
			recvSoc = new DatagramSocket(rp);
		}
		catch (SocketException socket_exception){
			System.out.println("SocketException! " + '\n' + "Server is closed");
			System.exit(-1);
		}
		// Done Initialisation
		System.out.println("UDPServer initialized.");
	}



	public static void main(String args[]) {
		int	recvPort;

		// Get the parameters from command line
		if (args.length < 1) {
			System.err.println("Please enter the IP as argument.");
			System.exit(-1);
		}
		recvPort = Integer.parseInt(args[0]);

		// TO-DO: Construct Server object and start it by calling run().
		UDPServer UDP_server = new UDPServer(recvPort);

		UDP_server.run();

	}

}
