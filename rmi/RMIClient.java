/*
 * Created on 01-Mar-2016
 */
 package rmi;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RMISecurityManager;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import common.MessageInfo;

public class RMIClient {

	public static void main(String[] args) {

		RMIServerI iRMIServer = null;

		// Check arguments for Server host and number of messages
		if (args.length < 2){
			System.out.println("Needs 2 arguments: ServerHostName/IPAddress, TotalMessageCount");
			System.exit(-1);
		}

		String urlServer = new String("rmi://" + "dhg18" + "/RMIServer"); // Replaced args[0] with "dhg18"
		int numMessages = Integer.parseInt(args[1]);

		// DONE: Initialise Security Manager
		if (System.getSecurityManager() == null) {
			System.setSecurityManager(new SecurityManager());
		}

		// DONE: Bind to RMIServer
		try {
			int port = 2060;

			// Connect to remote host by getting registry using specified IP of RMI server and port number
			Registry registry = LocateRegistry.getRegistry(args[0], port);
			iRMIServer = (RMIServerI)registry.lookup(urlServer);
			System.out.println("RMIClient bound and ready");
		} catch (NotBoundException e) {
			e.printStackTrace();
		} catch (RemoteException e) {
			e.printStackTrace();
		}

		// DONE: Attempt to send messages the specified number of times
		for (int i = 0; i < numMessages; i++) {
			MessageInfo msg = new MessageInfo(numMessages, i);
			try {
				iRMIServer.receiveMessage(msg);
				System.out.println("Sending message no.: " + i);
			} catch (RemoteException e) {
				e.printStackTrace();
			}
		}

		// Close client after sending last message
		System.out.println("Sent last message. Closing RMIClient.");
	}
}
 /*
package rmi;


import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RMISecurityManager;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import common.MessageInfo;
public class RMIClient {

	public static void main(String[] args) {

		RMIServerI iRMIServer = null;

		// Check arguments for Server host and number of messages
		if (args.length < 2){
			System.out.println("Needs 2 arguments: ServerHostName/IPAddress, TotalMessageCount");
			System.exit(-1);
		}

		String urlServer = new String("rmi://" + args[0] + "/RMIServer");
		int numMessages = Integer.parseInt(args[1]);

		// TO-DO: Initialise Security Manager
		if (System.getSecurityManager()==null){
			System.setSecurityManager(new SecurityManager());
		}
		// TO-DO: Bind to RMIServer
		try {
			//gandh99
			int port =2060;
			Registry reg = LocateRegistry.getRegistry(args[0]);
			iRMIServer = (RMIServerI)reg.lookup(urlServer);
		}  catch (RemoteException e){
			System.out.println("remote exception error");
		} catch (NotBoundException e){
			System.out.println("error not bound exception");
		}
		// TO-DO: Attempt to send messages the specified number of times
		for (int i = 0; i < numMessages; i++) {
			MessageInfo msg = new MessageInfo(numMessages, i);
			try {
				iRMIServer.receiveMessage(msg);
				System.out.println("Sending message no.: " + i);
			} catch (RemoteException e) {
				e.printStackTrace();
			}
		}

	}
}
*/
