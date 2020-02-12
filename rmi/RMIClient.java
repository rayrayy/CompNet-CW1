/*
 * Created on 01-Mar-2016
 */
 package rmi;

import java.rmi.Naming;
import java.net.MalformedURLException;
import java.rmi.RemoteException;
import java.rmi.NotBoundException;
import java.rmi.registry.Registry;
import java.rmi.registry.LocateRegistry;


import common.MessageInfo;
import java.rmi.RMISecurityManager;


public class RMIClient {

	public static void main(String[] args) {

		RMIServerI iRMIServer = null;

		// Check arguments for Server host and number of messages
		if (args.length < 2){
			System.out.println("Arguments required: ServerHostName/IPAddress, message count");
			System.exit(-1);
		}

		String urlServer = new String("rmi://RMIServer");
		int numMessages = Integer.parseInt(args[1]);

		// TO-DO: Initialise Security Manager
		if (System.getSecurityManager() == null) {
			System.setSecurityManager(new SecurityManager());
		}

		// TO-DO: Bind to RMIServer
		try {
			int recvPort = 1099;

			// Connect to remote host by getting registry using specified IP of RMI server and port number
			Registry registry = LocateRegistry.getRegistry(args[0], recvPort);

      iRMIServer = (RMIServerI)registry.lookup(urlServer);

      System.out.println("RMIClient initialised");
		}
    catch (RemoteException rmt_excp) {
			System.out.println("Remote exception" + rmt_excp.getMessage());
		}
    catch (NotBoundException nb_excp) {
			System.out.println("Not Bound Exception" + nb_excp.getMessage());
		}

		// TO-DO: Attempt to send messages the specified number of times
		for (int i = 0; i<numMessages; i++) {
			MessageInfo message = new MessageInfo(numMessages, i);
			try {
				iRMIServer.receiveMessage(message);
				System.out.println("Message: " + i);
			}
      catch (RemoteException rmt_excp) {
        System.out.println("Remote Exception" + rmt_excp.getMessage());
			}
		}

		// Close client after sending last message
		System.out.println("Process completed.");
	}
}
