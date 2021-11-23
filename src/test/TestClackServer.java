
package test;

import main.ClackServer;

public class TestClackServer {
    public static void main(String[] args) {
        System.out.println("Testing each constructor: ");
        ClackServer s = new ClackServer(25556);
        System.out.println(s);
        System.out.println(s.getPort());
        System.out.println(s.hashCode());
        System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
        ClackServer s1 = new ClackServer();
        System.out.println(s1);
        System.out.println(s.getPort());
        System.out.println(s.hashCode());
        System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");



        System.out.println("\n\nTesting out equals method:");
        ClackServer s2 = new ClackServer(25556);
        System.out.println(s.equals(s2)); //expected result true
        System.out.println(s.equals(s1)); //expected result false
        System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");




        System.out.println("\n\nAdding negative values to ClackServer object:");
        s = new ClackServer(-1);
        System.out.println(s.getPort());
        System.out.println(s.hashCode());
        System.out.println(s);


    }

}