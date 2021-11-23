package test;

import main.ClackClient;

public class TestClackClient {
    public static void main(String[] args) {
        /*
            List of inputs for each instance of start:
            SENDFILE tst.txt - invalid file
            SENDFILE test.txt - valid file
            This is a message! - a message
            DONE - stops each instance of start
         */
        System.out.println("Testing the ClackClient Object with all 3 args given to the constructor and with a non-existent file being sent, an existent file, and then a message");
        ClackClient c = new ClackClient("Username","hostname",1111);
        c.start();
        System.out.println();

        System.out.println("Testing the ClackClient Object with the username and hostname given as args to the constructor and with a non-existent file being sent, an existent file, and then a message");
        ClackClient c1 = new ClackClient("UserName1","hostname1");
        c1.start();
        System.out.println();

        System.out.println("Testing the ClackClient Object with the username given args to the constructor and with a non-existent file being sent, an existent file, and then a message");
        ClackClient c2 = new ClackClient("UserName2");
        c2.start();
        System.out.println();

        //Testing the argument exception methods
        try {
            ClackClient c3 = new ClackClient("Username", "hostname", 3);
        } catch (IllegalArgumentException IAE) {
            System.err.println(IAE.getMessage());
        }
        try {
            ClackClient c4 = new ClackClient(null, "hostname", 1111);
        } catch (IllegalArgumentException IAE) {
            System.err.println(IAE.getMessage());
        }
        try {
            ClackClient c5 = new ClackClient("Username", null, 1111);
        } catch (IllegalArgumentException IAE) {
            System.err.println(IAE.getMessage());
        }
        try {
            ClackClient c6 = new ClackClient(null);
        } catch (IllegalArgumentException IAE) {
            System.err.println(IAE.getMessage());
        }





        /*
        System.out.println("Testing out Each Version of the Constructor: ");
        ClackClient c = new ClackClient("Username1", "Hostname1", 25556);
        System.out.println(c);
        System.out.println(c.hashCode());
        System.out.println(c.getUserName()+" "+c.getHostName()+" "+c.getPort());
        System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
        c = new ClackClient("Username2", "Hostname2");
        System.out.println(c);
        System.out.println(c.hashCode());
        System.out.println(c.getUserName()+" "+c.getHostName()+" "+c.getPort());
        System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
        c = new ClackClient("Username3");
        System.out.println(c);
        System.out.println(c.hashCode());
        System.out.println(c.getUserName()+" "+c.getHostName()+" "+c.getPort());
        System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
        c = new ClackClient(); //default constructor
        System.out.println(c);
        System.out.println(c.hashCode());
        System.out.println(c.getUserName()+" "+c.getHostName()+" "+c.getPort());
        System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");



        System.out.println("\n\nTesting out the equals method:");
        ClackClient cc = new ClackClient("Username1", "Hostname1", 25556);
        System.out.println("Comparing: "+c+" to: "+ cc);
        System.out.println(c.equals(cc));
        c = new ClackClient("Username1", "Hostname1", 25556);
        System.out.println("Comparing: "+c+" to: "+ cc);
        System.out.println(c.equals(cc));
        System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");




        System.out.println("\n\nAdding negative numbers to port and null values to username and hostname:");
        c = new ClackClient("Username", "Hostname1", -1);
        System.out.println(c);
        //System.out.println(c.hashCode());
        System.out.println(c.getUserName()+" "+c.getHostName()+" "+c.getPort());
        System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
        c = new ClackClient(null, "hostname", 25556);
        System.out.println(c);
        //System.out.println(c.hashCode());
        System.out.println(c.getUserName()+" "+c.getHostName()+" "+c.getPort());
        System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
        c = new ClackClient("username", null, 25556);
        System.out.println(c);
        //System.out.println(c.hashCode());
        System.out.println(c.getUserName()+" "+c.getHostName()+" "+c.getPort());
        System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
        c = new ClackClient(null, null, -1);
        System.out.println(c);
        //System.out.println(c.hashCode());
        System.out.println(c.getUserName()+" "+c.getHostName()+" "+c.getPort());
        System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
        */


    }

}