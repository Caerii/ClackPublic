package main;

import data.*;

import java.io.*;
import java.util.Date;
import java.util.Scanner;

import java.net.*;
import java.net.Socket;

import static main.ClackClientGUI.*;

/**
 * This Class represents a client user. This class has four constructors, you can pass in a username, hostname
 * and port number. You can also passed a few combinations of those data points, where you remove the right most piece
 * of data. (username,hostname,port) to (username,hostname) to etc.
 * This class contains the following methods: start, readClientData, sendData, printData, getUserName, getHostName,
 * getPort, hashCode, equals, and toString
 * @author Anthony Lombardi
 */
public class ClackClient {

    private String userName; /**A String for the client username */
    private String hostName; /**A String for the client hostname */
    private int port; /**An Integer for the client port number */
    private boolean closeConnection; /**A boolean to tell whether the connection is closed */
    private ClackData dataToSendToServer; /**A ClackData object for the data that needs to be sent to the server*/
    private ClackData dataToReceiveFromServer; /**A ClackData Object for the data being received from the server */
    private Scanner inFromStd; /**A Scanner object represents the standard input stream  */
    private final static String KEY = "TIME"; /**A constant String that stores the encryption / decryption key */
    private ObjectOutputStream outToServer; /**An instance variable that allows for sending data packets  */
    private ObjectInputStream inFromServer; /**An instance variable that allows for receiving data packets  */
    private ClackClientGUI gui;
    /**
     *
     * Constructs a ClackClient object with the username, hostname, and port being initialized by another section
     * of this program.
     * Throws an IllegalArgumentException if the userName or hostName is null or if the port is less than 1024
     * @param userName A String for the clients username
     * @param hostName A String for the Clients hostname
     * @param port An Integer for the clients port number
     */
    public ClackClient(String userName, String hostName, int port) throws IllegalArgumentException {
        if (userName == null || hostName == null || port < 1024)
            throw new IllegalArgumentException("Input is invalid!");
        this.userName = userName; this.hostName = hostName; this.port = port; this.dataToSendToServer = null; this.dataToReceiveFromServer = null;
        this.inFromServer = null; this.outToServer = null;
    }

    /**
     * Constructs a ClackClient object with the username and hostname being initialized by another section of this
     * program and the port being initialized to 7000.
     * @param userName A String for the clients username
     * @param hostName A String for the Clients hostname
     */
    public ClackClient(String userName,String hostName ) {
        this(userName,hostName,7000);
    }

    /**
     * Constructs a ClackClient object with the username being initialized by another section of this and the hostname
     * being initialized to localhost.
     * Throws an IllegalArgumentException if the userName is null
     * @param username A String for the clients username
     */
    public ClackClient(String userName) throws IllegalArgumentException  {
        if (userName == null )
            throw new IllegalArgumentException("Input is invalid!");
        this.userName = userName; this.hostName = "localhost";
        this.inFromServer = null; this.outToServer = null;
    }

    /**
     * Constructs a ClackClient object with the username being initialized to anonymous.
     */
    public ClackClient() {
        this("anonymous");
    }


    /**
     * This method starts the connection to the ClackServer
     * Opens a new Socket that connects to the server at the port number stored as
     * an instance variable in the client object. It gets the output and input streams
     * for the client-server connection from the socket, and wraps them in ObjectOutputStream
     * and ObjectInputStream constructors to create new objects assigned to 'outToServer'
     * and 'inFromServer'
     */
    public void start() {
        //creates a Scanner object
        inFromStd = new Scanner(System.in);
        try {
            //new socket is created
            Socket skt = new Socket(hostName, port);
            //socket is wrapped in ObjectOutputStream constructor
            outToServer = new ObjectOutputStream(skt.getOutputStream());
            //socket is wrapped in ObjectInputStream constructor
            inFromServer = new ObjectInputStream(skt.getInputStream());

            //creating a new thread that runs the listener by creating a Runnable object
            //and starting a new thread with the Runnable object
            Thread clientThread = new Thread (new ClientSideServerListener(this) );
            clientThread.start();
            //loops through until the connection is closed
            while (!this.closeConnection) {
                //this.dataToReceiveFromServer = null;
                //this.dataToSendToServer = null;
                //this.readClientData(); //reads in something from the keyboard
                //this.sendData(); //this sends the data to the server
            }
            skt.shutdownOutput();
            skt.shutdownInput();
            skt.close();
            inFromStd.close();
            System.out.println("Disconnected From Server");
        } catch (IOException e) {
            System.err.println("Error: UnknownHost");
        }

    }

    /**
     * This method reads in from the inFromStd scanner and initializes the dataToSendToServer ClackData object
     * related to a Command sent in the beginning of the input stream
     */
    public void readClientData(String nextToken) {
        System.out.println("Write a line to send to the server: ");
        //String nextToken = this.inFromStd.next();
        if (nextToken.equals("DONE")) {
            this.closeConnection = true;
            this.dataToSendToServer = new LogOut();
        }
        else if (nextToken.equals("SENDFILE")) {
            String filePath = this.inFromStd.nextLine().trim();
            if (new File(filePath).exists()) {
                this.dataToSendToServer = new FileClackData(this.userName, filePath, ClackData.CONSTANT_SENDFILE);
                ((FileClackData) this.dataToSendToServer).readFileContents(KEY);
            }
            else {
                this.dataToSendToServer = null;
                System.err.println("Error: the file,"+ filePath+",cannot be found!");
            }
        }
        else if (nextToken.equals("SEND_IMAGE")) {
            this.dataToSendToServer = new ImageClackData(this.userName,gui.getImage(),ClackData.CONSTANT_SENDIMAGE);
        }
        else if (nextToken.equals("SEND_MEDIA")) {
            this.dataToSendToServer = new ImageClackData(this.userName,gui.getMedia(),ClackData.CONSTANT_SENDMEDIA);
        }
        else if (nextToken.equals("LISTUSERS")) { //creates a list of the users that are currently connected to server
            this.dataToSendToServer = new MessageClackData(this.userName,"LISTUSERS",ClackData.CONSTANT_LISTUSERS);
        } else {
            this.dataToSendToServer = new MessageClackData(this.userName, nextToken, ClackData.CONSTANT_SENDMESSAGE);
        }
    }


    /** Writes out the ClackData object in 'dataToSendToServer' to the ObjectOutputStream 'outToServer'. */
    public void sendData() {
        try {
            this.outToServer.writeObject(this.dataToSendToServer);
            this.outToServer.flush();
        } catch(NotSerializableException nse) {
            System.err.println("NotSerializableException: " + nse.getMessage());
        } catch (InvalidClassException ice) {
            System.err.println("InvalidClassException: " + ice.getMessage());
        } catch (IOException e) {
            System.err.println("IOException: "+e.getMessage());
        }
    }

    /** Reads in a ClackData object from ObjectInputStream 'inFromServer' into 'dataToReceiveFromServer */
    public void receiveData() {
        try {
            if(inFromServer != null)
                this.dataToReceiveFromServer = (ClackData) inFromServer.readObject();
            if(this.dataToReceiveFromServer.getData().equals("SENDUSERNAME")) {
                this.dataToSendToServer = new MessageClackData(this.userName,this.userName,ClackData.CONSTANT_LISTUSERS);
                this.sendData();
            }

        } catch (EOFException eofe) {
            return;
        } catch (IOException e) {
            if(e.getMessage().equals("Connection reset")) {
                this.closeConnection = true;
                System.err.println("Error: Server Disconnected");
            } else if(e.getMessage().equals("Socket closed"))
                return;
            else
                System.err.println("IOException: "+e.getMessage());
            } catch (ClassNotFoundException cnfe) {
            System.err.println("ClassNotFoundException: "+cnfe.getMessage());
        }
    }

    /** Sends the status of the connection. */
    public boolean isConnected(){
        return this.closeConnection;
    }

    /**
     * This method prints out the contents of the dataToSendToServer ClackData object by calling the toString method.
     */
    public void printData() {
        //System.out.println(this.dataToReceiveFromServer);
        if (this.dataToReceiveFromServer.getType() == 4) {
            gui.updateImage(((ImageClackData)this.dataToReceiveFromServer).getImage(),this.dataToReceiveFromServer.getUserName());
        }
        else if ((this.dataToReceiveFromServer.getType() == 5)) {
            gui.updateMedia(((ImageClackData)this.dataToReceiveFromServer).getImage(),this.dataToReceiveFromServer.getUserName());
        }
        else if(this.dataToReceiveFromServer != null && !this.dataToReceiveFromServer.getData().equals("SENDUSERNAME") && this.dataToReceiveFromServer.getType() != ClackData.CONSTANT_LISTUSERS) {
           gui.updateHistory(this.dataToReceiveFromServer.getUserName(),this.dataToReceiveFromServer.getData(),this.dataToReceiveFromServer.getDate());
        } else if (!this.dataToReceiveFromServer.getData().equals("SENDUSERNAME")){
            gui.updateFriends(this.dataToReceiveFromServer.getData());
        }

    }

    /**
     * Method that gets the gui from the gui class
     * @param gui an instance of the gui class
     */
    public void getGUI(ClackClientGUI gui) {
        this.gui = gui;
    }


    /**
     * Sends the username of the client as a String
     * @return String
     */
    public String getUserName() {
        return this.userName;
    }

    /**
     * Send the hostName of the client as a String
     * @return String
     */
    public String getHostName() {
        return this.hostName;
    }

    /**
     * Sends the port of the client as an int
     * @return int
     */
    public int getPort() {
        return this.port;
    }

    /**
     * This method returns the hashcode for a Clackclient object
     * @return int
     */
    @Override
    public int hashCode() {
        int hash = 17;
        hash = 31 * hash + this.userName.hashCode();
        hash = 31 * hash + this.hostName.hashCode();
        hash = 31 * hash + this.getPort();
        return hash;
    }

    /**
     * This method checks to see if two ClackClient methods are equal returns true if they are and false otherwise.
     * @param obj A ClackClient object
     * @return boolean
     */
    @Override
    public boolean equals(Object obj) {
        if(this == obj) {return true;}
        else if(obj == null || obj.getClass() != this.getClass()) {return false;}
        ClackClient clientObj = (ClackClient)obj;
        return this.userName == clientObj.getUserName() && this.hostName == clientObj.getHostName() && this.port == clientObj.getPort();
    }

    /**
     * This method returns a string with all of the instance variables of the class.
     * @return String
     */
    @Override
    public String toString() {
        return "This Class represents a client user. \n Current User: "+this.userName+" Host Name: "+this.hostName+" Port: "+this.port ;
    }


    /**
     * This is the main method that will create a client object and start the connection to the server. Must be called
     * with one or none argument(s) that take any of the following forms:
     * Username
     * Username@HostName  or
     * UserName@HostName:PortNumber
     * @param args
     */
    public static void main(String[] args) {
        ClackClient c;
        if(args.length == 0)
            c = new ClackClient();
        else if (args.length == 1) {
            if (args[0].contains("@") && args[0].contains(":")) {
                String[] parsedAt = args[0].split("@");
                String[] parsedCol = parsedAt[1].split(":");
                c = new ClackClient(parsedAt[0],parsedCol[0],Integer.parseInt(parsedCol[1]));
            } else if (args[0].contains("@")) {
                String[] parsedAt = args[0].split("@");
                c = new ClackClient(parsedAt[0],parsedAt[1]);
            } else {
                c = new ClackClient(args[0]);
            }
        } else
            throw new IllegalArgumentException("Error Too many inputs");
        System.out.println(c);
        c.start();
    }
}