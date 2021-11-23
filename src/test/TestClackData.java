package test;

import data.ClackData;
import data.FileClackData;
import data.MessageClackData;

public class TestClackData {
	public static void main(String[] args) {

	//---------------------------------------------------------testing messageClackData\
	//initializing objects of type MessageClackData
	ClackData v1 = new MessageClackData("Lullaby","ALIFWASHERE","TIME", ClackData.CONSTANT_SENDMESSAGE);
	ClackData r1 = new MessageClackData("Crabs are a thing", "brave new world HEY","TIME", -999);
	ClackData j1 = new MessageClackData();

	System.out.println(v1);
	
	//showing data directly and after it is decrypted
	System.out.println( ((MessageClackData)v1).getData() );
	System.out.println( ((MessageClackData)v1).getData("TIME") );
	System.out.println( ((MessageClackData)r1).getData() );
	System.out.println( ((MessageClackData)r1).getData("TIME") );
	System.out.println( ((MessageClackData)j1).getData() );
	System.out.println( ((MessageClackData)j1).getData("TIME") );
	
	ClackData e1 = new MessageClackData("mario hates penguins", "super duper WORLD", -999);
	//without encrypting the message contents first it just decrypts an already readable text
	System.out.println( ((MessageClackData)e1).getData() );
	System.out.println( ((MessageClackData)e1).getData("TIME") );
	
	
		
	//--------------------------------------------------------- file reading test below
	ClackData z = new FileClackData("username", "Part2_document.txt", ClackData.CONSTANT_LISTUSERS);
	ClackData z1 = new FileClackData();


	ClackData z2 = new FileClackData("username", "art2_document.txt", ClackData.CONSTANT_LISTUSERS);
	((FileClackData) z2).readFileContents(); //throws file not found
	
	//unsafe file read
	((FileClackData) z).readFileContents();
	System.out.println( ((FileClackData)z).getData() );
	//unsafe file read
	((FileClackData) z1).readFileContents();
	System.out.println( ((FileClackData)z).getData() );
	
	ClackData y = new FileClackData("username", "Part2_document.txt", ClackData.CONSTANT_LISTUSERS);
	ClackData y1 = new FileClackData();
	
	//safe file read with encryption key "TIME"
	((FileClackData) y).readFileContents("TIME");
	System.out.println( ((FileClackData)y).getData() );
	
	//safe file read with encryption key "TIME"
	((FileClackData) y1).readFileContents("TIME");
	System.out.println( ((FileClackData)y).getData() );
	//--------------------------------------------------------- file writing test below
	ClackData x = new FileClackData("username", "WritingTest1.txt", ClackData.CONSTANT_LISTUSERS);
	ClackData x1 = new FileClackData();
	
	//unsafe file write
	((FileClackData) x).writeFileContents();
	//file should contain "null"
	
	//unsafe file write
	((FileClackData) x1).writeFileContents();
	//file should contain "null"
	
	ClackData w = new FileClackData("username", "WritingTest2.txt", ClackData.CONSTANT_LISTUSERS);
	ClackData w1 = new FileClackData();
	
	//safe file write with encryption key "TIME"
	((FileClackData) w).writeFileContents("TIME");
	//file should contain "umzh" because it decrypts "null"
	
	//safe file write with encryption key "TIME"
	((FileClackData) w1).writeFileContents("TIME");
	//file should contain "umzh" because it decrypts "null"
		
	
		/**
	//initializing object of type FileClackData
	ClackData f = new FileClackData("Scooby", "Gimme some snax boy lemme get em HNGGGHHHHH >:}}", ClackData.CONSTANT_LISTUSERS);
	ClackData g = new FileClackData("Shaggy", "her u r sc0b her is ur sn4aAAAAAACk. zoinks.............", ClackData.CONSTANT_SENDFILE);
	ClackData h = new FileClackData();
	
	System.out.println("FileClackData object 1 testing accessors: ");
	//testing getters
	System.out.println("Username: " + f.getUserName());
	System.out.println("Message: " + f.getData());
	System.out.println("Type: " + f.getType());
	System.out.println("Date: " + f.getDate());
	System.out.println("Filename: " + ((FileClackData) f).getFileName());
	System.out.println("Hashcode: " + f.hashCode());
	System.out.println("toString: " + f);
	((FileClackData) f).setFileName("Scooby Snacks");
	System.out.println("After changing Filename: " + ((FileClackData) f).getFileName() + "\n");
	
	System.out.println("FileClackData object 2 testing accessors: ");
	//testing getters
	System.out.println("Username: " + g.getUserName());
	System.out.println("Message: " + g.getData());
	System.out.println("Type: " + g.getType());
	System.out.println("Date: " + g.getDate());
	System.out.println("Filename: " + ((FileClackData) g).getFileName());
	System.out.println("Hashcode: " + g.hashCode());
	System.out.println("toString: " + g);
	((FileClackData) g).setFileName("Shaggy Response");
	System.out.println("After changing Filename: " + ((FileClackData) g).getFileName() + "\n");
	
	System.out.println("FileClackData object 3 testing accessors: ");
	//testing getters
	System.out.println("Username: " + h.getUserName());
	System.out.println("Message: " + h.getData());
	System.out.println("Date: " + h.getDate());
	System.out.println("Filename: " + ((FileClackData) h).getFileName());
	System.out.println("Hashcode: " + h.hashCode());
	System.out.println("toString: " + h);
	((FileClackData) h).setFileName("Default");
	System.out.println("After changing Filename: " + ((FileClackData) h).getFileName());
	
	//testing equals
		System.out.println("\nIs object 1 and 2 the same? " + f.equals(g) + "\n");
	
	//initializing objects of type MessageClackData
	ClackData v = new MessageClackData("PersonGuy","I do not exist on this plane of reality, what flavor ice cream would you like?", ClackData.CONSTANT_SENDMESSAGE);
	ClackData w = new MessageClackData("SentientFireAlarm", "It is my pleasure to be able to alarm human beings, all hail ALARMCORP!", -999);
	ClackData j = new MessageClackData();
	
	System.out.println("MessageClackData object 1 testing accessors: ");
	//testing getters
	System.out.println("Username: " + v.getUserName());
	System.out.println("Message: " + v.getData());
	System.out.println("Type: " + v.getType());
	System.out.println("Date: " + v.getDate());
	System.out.println("Hashcode: " + v.hashCode());
	System.out.println("toString: " + v);
	
	System.out.println("\nMessageClackData object 2 testing accessors: ");
	//testing getters
	System.out.println("Username: " + w.getUserName());
	System.out.println("Message: " + w.getData());
	System.out.println("Type: " + w.getType());
	System.out.println("Date: " + w.getDate());
	System.out.println("Hashcode: " + w.hashCode());
	System.out.println("toString: " + w);
	
	System.out.println("\nMessageClackData object 3 (default) testing accessors: ");
	//testing getters
	System.out.println("Username: " + j.getUserName());
	System.out.println("Message: " + j.getData());
	System.out.println("Type: " + j.getType());
	System.out.println("Date: " + j.getDate());
	System.out.println("Hashcode: " + j.hashCode());
	System.out.println("toString: " + j);
	
	//testing equals
	System.out.println("\nIs object 1 and 2 the same? " + v.equals(w));
	
	
	*/

	}

}
