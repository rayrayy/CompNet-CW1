/*
 * Created on 01-Mar-2016
 */
package rmi;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.rmi.registry.Registry;
import java.util.Arrays;

import common.MessageInfo;

public class RMIServer extends UnicastRemoteObject implements RMIServerI {

	private int totalMessages = 0;
	private int[] receivedMessages;

	public RMIServer() throws RemoteException {
	}

	public void receiveMessage(MessageInfo msg) throws RemoteException {

		// TO-DO: On receipt of first message, initialise the receive buffer
		if ((receivedMessages == null) || (msg.totalMessages != totalMessages)) {
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
					System.out.println("Received: " + (receivedMessages.length-missing));
					System.out.println("Missing:  " + missing);
		}
	}



	public static void main(String[] args) {

		RMIServer rmis = null;

		// TO-DO: Initialise Security Manager
		if (System.getSecurityManager() == null){
			System.setSecurityManager(new SecurityManager());
		}

		// TO-DO: Instantiate the server class
		try{
			rmis = new RMIServer();
		}
		catch (RemoteException rmt_excp){
			System.out.println("Failed to instantiate server class due to remote exception!");
			System.exit(-1);
		}
		// TO-DO: Bind to RMI registry
		rebindServer("rmi://RMIServer", rmis);

	}

	protected static void rebindServer(String serverURL, RMIServer server) {

		// TO-DO:
		// Start / find the registry (hint use LocateRegistry.createRegistry(...)
		// If we *know* the registry is running we could skip this (eg run rmiregistry in the start script)
		Registry registry = null;
		int recvPort = 1099;

		try {
				registry = LocateRegistry.createRegistry(recvPort);
				System.out.println("Created registry");
		} catch (RemoteException rmt_excp) {
			System.out.println("Remote Exception!");
			System.exit(-1);
		}
		// TO-DO:
		// Now rebind the server to the registry (rebind replaces any existing servers bound to the serverURL)
		// Note - Registry.rebind (as returned by createRegistry / getRegistry) does something similar but
		// expects different things from the URL field.
		try{
			registry.rebind(serverURL, server);
			System.out.println("RMIServer initialized.");
		}
		catch(RemoteException rmt_excp){
			System.out.println("Failed to bind server!");
			System.exit(-1);
		}
	}
}
