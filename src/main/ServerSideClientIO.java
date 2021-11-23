package main;

import data.ClackData;
import data.LogOut;
import data.MessageClackData;

import java.io.*;
import java.net.Socket;

/**
 * This Class handles the sending and receiving of data. On the server side of things.
 * This class contains the following methods: A constructor, run, receiveData, and sendData
 * @author Anthony Lombardi
 */
public class ServerSideClientIO implements Runnable{
    private boolean closeConnection; /**A boolean to tell whether the connection is closed */
    private ClackData dataToReceiveFromClient; /**A ClackData Object for the data being received from the client */
    private ClackData dataToSendToClient; /**A ClackData object for the data that needs to be sent to the client*/
    private ObjectInputStream inFromClient; /**A ObjectInputStream object to receive data from the client */
    private ObjectOutputStream outToClient; /**ObjectOutputStream object to send data off to the client */
    private ClackServer server; /**A ClackServer object used as the server the client is communicating with */
    private Socket clientSocket; /**A Socket object representing the client that is talking to the server */

    /**
     * Constructor for ServerSideClients takes the following two arguments, and sets there corresponding instance
     * variables equal to them, and sets the rest of the instances variables to either false or null.
     * @param server A ClackServer object that is the server that the client is talking to.
     * @param clientSocket A Socket object that represents the client that is taking to a server.
     */
    public ServerSideClientIO (ClackServer server, Socket clientSocket) {
        this.server = server; this.clientSocket = clientSocket; this.closeConnection = false;
        this.dataToReceiveFromClient = null; this.dataToSendToClient = null; this.inFromClient = null; this.outToClient = null;
    }

    /**
     * Gets the object output and input streams of the client from 'clientSocket', and contains a while loop
     * that runs as long as the connection is open. In the loop, the method calls 'receiveData()' to get data
     * from the associated client (which is in ServerSideClientIO.java) and calls 'broadcast()' in the 'this.server' object
     * to broadcast the data from the associated client to all clients. The 'broadcast()' method in the server will
     * in turn call the 'sendData()' method of each ServerSideClientIO object used in threading to send broadcast
     * data to each client. This will enable all clients to receive the data sent from any particular client.
     */
    @Override
    public void run() {
        try {
            this.inFromClient = new ObjectInputStream(this.clientSocket.getInputStream());
            this.outToClient = new ObjectOutputStream(this.clientSocket.getOutputStream());
            while(!this.closeConnection) {
                this.receiveData();
                if(this.dataToReceiveFromClient != null) {
                    if (this.dataToReceiveFromClient.getData().equals("LISTUSERS")) {
                        this.server.broadcast(new MessageClackData(this.dataToReceiveFromClient.getUserName(), "SENDUSERNAME", ClackData.CONSTANT_SENDMESSAGE));
                    } else {
                        System.out.println(this.dataToReceiveFromClient);
                        this.dataToSendToClient = this.dataToReceiveFromClient;
                        this.server.broadcast(this.dataToSendToClient);
                    }
                }
            }
            clientSocket.close();
            this.inFromClient.close();
            this.outToClient.close();
        } catch (IOException ioe) {
            System.err.println("IOException: "+ioe.getMessage());
        }
    }

    /**
     * This method reads the data in from the client using the inFromClient ObjectInputStream object and and reads in
     * the data into the dataToReceiveFromClient ClackData object.
     */
    public void receiveData() {
        try {
            this.dataToReceiveFromClient = (ClackData) inFromClient.readObject();
            if (this.dataToReceiveFromClient instanceof LogOut) {
                this.closeConnection = true; this.server.remove(this); this.dataToReceiveFromClient = null;
                System.out.println("Client Disconnected");
            }
        } catch (IOException ioe) {
            System.err.println("IOException: "+ioe.getMessage());
            if(ioe.getMessage().equals("Connection reset")) {
                this.closeConnection = true; this.server.remove(this);
                System.err.println("Error: Client Disconnected Unexpectedly");
            }
        } catch (ClassNotFoundException cnfe) {
            System.err.println("ClassNotFoundException: "+cnfe.getMessage());
        }
    }

    /**
     * This method sends the data out to the client using the outToClient ObjectOutputStream object and and gets the
     * data the needs to be sent from the dataToSendToClient ClackData object.
     */
    public void sendData() {
        try {
            if(this.dataToSendToClient != null) {
                this.outToClient.writeObject(this.dataToSendToClient);
                this.outToClient.flush();
            }
        } catch (InvalidClassException ice) {
            System.err.println("InvalidClassException: "+ice.getMessage());
        } catch(NotSerializableException nse) {
            System.err.println("NotSerializableException: "+nse.getMessage());
        } catch (IOException ioe) {
            System.err.println("IOException: "+ioe.getMessage());
        }
    }

    /**
     * This method is mutator that sets the instance variable dataToSendToClient to the variable passed as an argument
     * to this method.
     */
    public void setDataToSendToClient(ClackData dataToSendToClient) {
        this.dataToSendToClient = dataToSendToClient;
    }
}
