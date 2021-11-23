package main;

import data.ClackData;
import data.MessageClackData;

import java.io.*;
import java.net.*;

/**
 * This class takes over the responsibility of receiving data from the server and printing
 * it in parallel to the ClackClient class, and the ClackClient class only reads data
 * from the user and sends it. 
 * @author Alif_Jakir
 */
public class ClientSideServerListener implements Runnable {
	
	/** ClackClient instance variable representing a client that calls
	 * this class to make a threaded Runnable object. */
	private ClackClient client = new ClackClient();

	
	/** Constructor that sets the ClackClient instance variable 'this.client'
	 * in this class to the value of the ClackClient object 'client' fed as a parameter to it.*/
	public ClientSideServerListener(ClackClient client) {
		this.client = client;
	}
	
	/**
	 * While the connection is still open, the method uses the receiveData()
	 * and the printData() methods in the 'client' object to receive data
	 * from the server, and print it.
	 *  */
	@Override
	public void run() {
		while ( !client.isConnected() ) {
			client.receiveData(); //gets the echo from the server
			client.printData(); //calls printData if the dataToReceiveFromServer Object is not null
		}
	}
} 
