
/*
 * Created on 01-Mar-2016
 */
package rmi;

import java.io.IOException;
import java.net.MalformedURLException;
import java.rmi.*;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Arrays;

import common.*;

public class RMIServer extends UnicastRemoteObject implements RMIServerI {

    private int totalMessages = -1;
    private int[] receivedMessages;

    public RMIServer() throws RemoteException {
        super();
    }

    public void receiveMessage(MessageInfo msg) throws RemoteException {
        // DONE: On receipt of first message, initialise the receive buffer
        if (totalMessages == -1) {
            receivedMessages = new int[msg.totalMessages];
            totalMessages = 0;
        }

        // DONE: Log receipt of the message
        receivedMessages[msg.messageNum]++;
        System.out.println("Received message no.: " + msg.messageNum);
        totalMessages++;

        // DONE: If this is the last expected message, then identify
        //        any missing messages
        if (totalMessages == msg.totalMessages) {
            printMessageFailures();

            // Close server after receiving last message
            System.out.println("Received last message. Closing RMIServer.");
//			System.exit(0);
        }
    }

    public void printMessageFailures() {
        int lostMessages = 0;
        for (int i = 0; i < receivedMessages.length; i++) {
            if (receivedMessages[i] == 0) {
                System.out.println("Failed to receive message no.: " + i);
                lostMessages++;
            }
        }
        System.out.println("RMIServer lost " + lostMessages + " message(s) in total.");
    }

    public static void main(String[] args) {
        RMIServer rmis = null;

        // DONE: Initialise Security Manager
        if (System.getSecurityManager() == null) {
            System.setSecurityManager(new SecurityManager());
        }

        // DONE: Instantiate the server class
        try {
            rmis = new RMIServer();
        } catch (RemoteException e) {
            e.printStackTrace();
        }

        // DONE: Bind to RMI registry
        final String serverURL = "rmi://dhg18/RMIServer";
        rebindServer(serverURL, rmis);
    }

    protected static void rebindServer(String serverURL, RMIServer server) {
        // DONE:
        // Start / find the registry (hint use LocateRegistry.createRegistry(...)
        // If we *know* the registry is running we could skip this (eg run rmiregistry in the start script)
        Registry registry = null;
        int port = 2060;

        //TESTTTTTTTTTTTTTT
//        String cmd = "lsof -P | grep ':2060' | awk '{print $2}' | xargs kill -9";

        // Original code
        try {
            registry = LocateRegistry.createRegistry(port);
            System.out.println("Created registry");
        } catch (RemoteException e) {
            e.printStackTrace();
        }

        // DONE:
        // Now rebind the server to the registry (rebind replaces any existing servers bound to the serverURL)
        // Note - Registry.rebind (as returned by createRegistry / getRegistry) does something similar but
        // expects different things from the URL field.
        try {
            registry.rebind(serverURL, server);
            System.out.println("RMIServer bound and ready");
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
}













package server;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RMISecurityManager;
import java.rmi.registry.LocateRegistry;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import common.*;

public class RMIServer extends UnicastRemoteObject implements RMIServerI {

	private int totalMessages = -1;
	private int[] receivedMessages;

	public RMIServer() throws RemoteException {
	}

	public void receiveMessage(MessageInfo msg) throws RemoteException {

		// On receipt of first message, initialize the receive buffer
		if (receivedMessages == null) {
			totalMessages = msg.totalMessages;
			receivedMessages = new int[msg.totalMessages];
		}

		// Log receipt of the message
		receivedMessages[msg.messageNum] = 1;

		// If this is the last expected message, then identify
		//    any missing messages
		if (msg.messageNum + 1 == receivedMessages.length) {
			System.out.println("Messages being totaled....");

			String s = "Lost packet numbers: ";
			int count = 0;
			for (int i = 0; i < totalMessages; i++) {
				if (receivedMessages[i] != 1) {
					count++;
					s = s + " " + (i+1) + ", ";
				}
			}

			if (count == 0) s = s + "None";

			System.out.println("Total messages sent      : " + totalMessages);
			System.out.println("Total messages received  : " + (totalMessages - count));
			System.out.println("Total messages lost      : " + count);
			System.out.println(s);
			System.out.println("Test finished.");
			System.exit(0);
		}


	}


	public static void main(String[] args) {

		// Initialize Security Manager
		if(System.getSecurityManager()==null){
            System.setSecurityManager(new RMISecurityManager());
        }

		// Bind to RMI registry
		try {
			LocateRegistry.createRegistry( 1099 );
			Naming.rebind("RMIServer", new RMIServer());
		} catch (RemoteException e) {
			System.out.println("Error initializing registry or binding server.");
			System.exit(-1);
		} catch (MalformedURLException e) {
			System.out.println("Could not bind server to defined registry as the URL was malformed.");
			System.exit(-1);
		}
		System.out.println("Running...");
	}

}
