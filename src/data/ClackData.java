package data;

import java.util.Date;
import java.io.*;

/**
 * Class ClackData is a superclass that represents the data sent between the client and the server.
 *  An object of type ClackData consists of the user name of the client user,
 *  the date and time at which the data was sent and the data itself,
 *  which can either be a message (MessageClackData)
 *  or the name and contents of a file (FileClackData).
 *  MessageClackData and FileClackData are both children of ClackData.
 * @author Alif_Jakir
 *
 */

public abstract class ClackData implements Serializable {

	
	/** Enables server to provide client with targeted service */
	public final static int CONSTANT_LISTUSERS = 0; 
	/** Enables server to provide client with targeted service */
	public final static int CONSTANT_LOGOUT = 1; 
	/** Enables server to provide client with targeted service */
	public final static int CONSTANT_SENDMESSAGE = 2; 
	/** Enables server to provide client with targeted service */
	public final static int CONSTANT_SENDFILE = 3;
	/** Enables server to provide client with targeted service */
	public final static int CONSTANT_SENDIMAGE = 4;
	/** Enables server to provide client with targeted service */
	public final static int CONSTANT_SENDMEDIA = 5;
	
	private String userName; /**String representing name of the client user */
	private int type; /**represents the kind of data exchanged between the client and the server,
	 					enables the server to provide the client with targeted service */
	private Date date; /**Date object representing date when ClackData object was created */
	
	/**
	 * Constructor to set up userName and type, date created automatically here
	 */
	public ClackData(String userName, int type) {
		this.userName = userName;
		this.type = type;
		this.date = new Date();
	}
	
	/**
	 * Constructor to create anonymous user, whose name is "Anon", calls another constructor
	 */
	public ClackData(int type) {
		this("Anon", type);
	}
	
	/**
	 * Default constructor, calls another constructor
	 */
	public ClackData() {
		this("Anon", CONSTANT_LISTUSERS);
	}
	
	/** Takes in an input string to encrypt using a key, and outputs the encrypted
	 * string, uses the Vignere cipher to perform the encryption.
	 * 
	 * @param inputStringToEncrypt - The original String that must be encrypted
	 * @param key - The String key to encrypt the message
	 * @return - A String that contains the encrypted message
	 */
	protected String encrypt( String inputStringToEncrypt, String key ) {
			
		String repeatedKey = "";
		
	    for (int x = 0, j = 0; x < inputStringToEncrypt.length(); x++) {	
	    	
	    	char c = inputStringToEncrypt.charAt(x);


	        if(! Character.isAlphabetic(c) ) {
	        	repeatedKey += c;
	        }
	        
	        //check for lowercase
	        else if (c >= 'a' && c <= 'z') {
	    	   repeatedKey += ((char)((c + key.toLowerCase().charAt(j) - 2 * 'a') % 26 + 'a'));
	           j = ++j % key.length();
	        }
	        
	       //checks for uppercase
	        else if (c >= 'A' && c <= 'Z')  {
	           c = inputStringToEncrypt.toUpperCase().charAt(x);
	           repeatedKey += (char)((c + key.charAt(j) - 2 * 'A' ) % 26 + 'A');
	           j = ++j % key.length();	
	        }	 
	        
	    }
	    return repeatedKey;
	}
	
	/** Takes in an input string to decrypt using a key, and outputs the decrypted
	 * string, uses the Vignere cipher to perform the decryption.
	 * 
	 * @param inputStringToDecrypt - The encrypted String that must be decrypted
	 * @param key - The String key to encrypt the message
	 * @return - A String containing the decrypted message
	 */
	protected String decrypt( String inputStringToDecrypt, String key ) {    
		String decryptedInput = "";
		for(int x = 0, y = 0; x<inputStringToDecrypt.length();x++) {
			char c = inputStringToDecrypt.charAt(x);
			y %= key.length();
			if (! Character.isAlphabetic(c) )
				decryptedInput += c;
			else if (c >= 'a' && c <= 'z') {
				decryptedInput += (char)(((c-key.toLowerCase().charAt(y)+26)%26)+'a');
				y++;
			} else if (c >= 'A' && c <= 'Z')  {
				decryptedInput += (char)(((c-key.charAt(y)+26)%26)+'A');
				y++;
			}

		}
		return decryptedInput;
	}
	
	
	/**
	 * Returns the type
	 */
	public int getType() {
		return this.type;
	}
	
	/**
	 * Returns the username
	 */
	public String getUserName() {
		return this.userName;
	}
	
	/**
	 * Returns the date
	 */
	public Date getDate() {
		return this.date;
	}
	
	/**
	 * Abstract method to return the data contained in this 
	 * class(contents of instant message or contents of a file)
	 */
	public abstract String getData();
	
	/** Returns the original data in an encrypted string */
	public abstract String getData( String key );


}
