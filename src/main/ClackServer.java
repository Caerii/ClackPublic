package main;

import data.ClackData;
import data.MessageClackData;

import java.io.*;
import java.net.*;
import java.util.ArrayList;

/**
 * This class represents the server, you can construct a server object by sending the port number over as an integer
 * or by using the default constructor
 * This class contains the following methods: start, broadcast, remove ,getPort, hashCode, equals, toString, and a main
 * @author Anthony Lombardi
 */
public class ClackServer {
    private int port;  /**An Integer for the server port number */
    private boolean closeConnection; /**A boolean to tell whether the connection is closed */
    private ArrayList<ServerSideClientIO> serverSideClientIOList; /**An ArrayList of ServerSideClientIO Objects*/


    /**
     * Constructor for the ClackServer
     * @param port This is an integer representing the port the server will use
     */
    public ClackServer(int port) throws IllegalArgumentException {
        if(port < 1024)
            throw new IllegalArgumentException("Input is invalid!");
        this.port = port; this.closeConnection = false; this.serverSideClientIOList = new ArrayList<>();
    }

    /**
     * Default Constructor for the ClackServer inits port to 7000
     */
    public ClackServer() {
        this(7000);
    }

    /**
     * This method creates a server socket and accepts client sockets, makes a ServerSideClientIO object and adds it
     * to an arraylist of other ServerSideClientIO objects. The method also creates and starts a thread around the
     * ServerSideClientIO object.
     */
    public void start() {
        try {
            ServerSocket sskt = new ServerSocket(this.port);
            Socket clientSocket;
            while (!closeConnection) {
                clientSocket = sskt.accept();
                ServerSideClientIO serverSideClientIO = new ServerSideClientIO(this, clientSocket);
                this.serverSideClientIOList.add(serverSideClientIO);
                new Thread(serverSideClientIO).start();
                System.out.println("A Client has Connected");
            }
            sskt.close();
        } catch (IOException ioe) {
            System.err.println("IOException: "+ioe.getMessage());
        }
    }

    /**
     This method iterates through the serverSideClientIOList to send the given ClackData object to each Client.
     * @param dataToBroadCastToClients This is a ClackData object containing the information that the server wishes to
     * send to each of the connected clients.
     */
    public synchronized void broadcast(ClackData dataToBroadCastToClients) {
        for(ServerSideClientIO client : this.serverSideClientIOList) {
            client.setDataToSendToClient(dataToBroadCastToClients);
            client.sendData();
        }
    }


    /**
     * This method removes a given ServerSideClientIO object from the serverSideClientIOList.
     * @param serverSideClientToRemove This is a ServerSideClientIO object that is going to be removed from the server.
    */
    public synchronized void remove(ServerSideClientIO serverSideClientToRemove) {
        this.serverSideClientIOList.remove(serverSideClientToRemove);
    }

    /**
     * Sends the port of the client as an int
     * @return port
     */
    public int getPort() {
        return this.port;
    }

    /**
     * This method returns the hashcode for a ClackServer object
     * @return int
     */
    @Override
    public int hashCode() {
        int hash = 17;
        hash = 31 * hash + this.getPort();
        return hash;
    }

    /**
     * This method checks to see if two ClackServer objects are the same returns true if they are and false otherwise
     * @param obj an object to compare the ClackServer to
     * @return boolean
     */
    @Override
    public boolean equals(Object obj) {
        if(this == obj) {return true;}
        else if(obj == null || obj.getClass() != this.getClass()) {return false;}
        ClackServer serverObj = (ClackServer) obj;
        return this.port == serverObj.getPort();
    }

    /**
     * This method prints out a string of all the class data
     * @return String
     */
    @Override
    public String toString() {
        return "This Class represents the server \n Port:"+this.port;
    }

    /**
     * This method is the main method that will start up the server. Must be called from command line with either a
     * port number or nothing as the arguments
     * @param args
     */
    public static void main(String[] args) {
        ClackServer s;
        if(args.length == 0)
            s = new ClackServer();
        else if (args.length == 1)
            s = new ClackServer(Integer.parseInt(args[0]));
        else
            throw new IllegalArgumentException("Error Too many inputs");
        System.out.println(s);
        s.start();
    }
}