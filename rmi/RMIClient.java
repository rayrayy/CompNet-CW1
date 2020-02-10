/*
 * Created on 01-Mar-2016
 */
package rmi;

import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

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
		} catch (MalformedURLException e){
			System.out.println("Error Malformed Hostname");
		} catch (RemoteException e){
			System.out.println("remote exception error");
		} catch (NotBoundException e){
			System.out.println("error not bound exception");
		}
		// TO-DO: Attempt to send messages the specified number of times

	}
}
