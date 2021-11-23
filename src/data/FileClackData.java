package data;

import java.io.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/**
 * Subclass inheriting from ClackData, holds file information and file modification,
 * file reading methods.
 * @author Alif_Jakir
 *
 */

public class FileClackData extends ClackData {

	private String fileName; /**String representing name of file. */
	private String fileContents; /**String representing contents of file. */
	
	/**
	 * Constructor to set up username, fileName and type, should call superconstructor,
	 * fileContents initialized to null.
	 * @param userName of type String.
	 * @param fileName of type String.
	 * @param type of type int
	 */
	public FileClackData(String userName, String fileName, int type) {
		super(userName, type);
		this.fileName = fileName; this.fileContents = "null";
	}
	
	/**
	 * Default constructor to set up an empty object of class FileClackData
	 */
	public FileClackData() {
		super();
		this.fileName = "null"; this.fileContents = "null";
	}
	
	
	/**
	 * Sets the file name in this object
	 * @param fileName of type String.
	 */
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	
	/**
	 * Returns the file name.
	 */
	public String getFileName() {
		return this.fileName;
	}
	
	/**
	 * Implemented here to return file contents.
	 */
	public String getData() {
		return this.fileContents;
	}

	/**Decrypts the string in 'fileContents' and returns the decrypted string.   */
	@Override
	public String getData( String key ) {
		return super.decrypt(this.fileContents,key);
	}
	
	/**
	 * Helper method to read files with fileName into the string fileContents. 
	 * @param fileName - This is the file that is being read from.
	 * @param fileContents - The String where all the info is being read into.
	 * @throws IOException
	 */
	private void fileReading(String fileName, String fileContents) throws IOException {
		final String EOS = null;
		
		//creates a FileReader wrapped with a BufferedReader
		BufferedReader breader = new BufferedReader( new FileReader(fileName) );
		
		String input = "";
		this.fileContents = "";
		while ( (input = breader.readLine()) != EOS ) {				
			this.fileContents += input; //adds each line to the fileContents until reaches end of file
		} 
		breader.close(); //it is important to close the buffered reader after it is done
	}
	
	/**
	 * Does non-secure file reads, opens file pointed to by instance variable
	 * 'fileName', reads contents of the file to instance variable 'fileContents'
	 * and closes the file.
	 */
	public void readFileContents()  {
		
		try {
			//reads file into fileContents
			fileReading(this.fileName, this.fileContents);
			System.out.println( "File read successfully.");
			
		} catch (FileNotFoundException fnfe ) {
			System.err.println( fnfe.getMessage() );		
		} catch (IOException ioe ) {
			System.err.println( "Issue with reading.");
		}
	}
	

	/**
	 * Does secure file reads, opens file pointed to by instance variable
	 * 'fileName', reads contents of the file, encrypts contents to the instance variable
	 * 'fileContents' using "key", then closes the file.
	 */
	public void readFileContents(String key) {
		try {
			//reads file into fileContents
			fileReading(this.fileName, this.fileContents);
			System.out.println( "File read successfully.");
			
			//encrypts fileContents
			this.fileContents = encrypt(this.fileContents, key);
			
		} catch (FileNotFoundException fnfe ) {
			System.err.println( fnfe.getMessage() );		
		} catch (IOException ioe ) {
			System.err.println( "Issue with reading.");
		} 
	}
	
	/**
	 * Helper method to write files
	 * @param fileName - This is the file that is being written into.
	 * @param fileContents - The String where all the info is being written from.
	 * @throws IOException
	 */
	private void fileWriting(String fileName, String fileContents) throws IOException {
		//creates a FileWriter wrapped with a BufferedWriter
		BufferedWriter bwriter = new BufferedWriter( new FileWriter(fileName) );
		
		//writes to file
		bwriter.write(fileContents);
				
		bwriter.close(); //it is important to close the buffered writer after it is done
	}
	
	/**
	 * Does non-secure file writes. Opens file pointed to by instance variable 'fileName'
	 * and writes the contents of the instance variable 'fileContents' to the file,
	 * closes the file.
	 */
	public void writeFileContents() {
		try {
			
			fileWriting(this.fileName, this.fileContents);
			System.out.println( "File written successfully.");
			
		} catch (IOException ioe ) {
			System.err.println( "Issue with writing.");
		} catch (IllegalArgumentException iae) {
			System.err.println( iae.getMessage() );
		}
	}
	

	/**
	 * Does secure file writes. Opens file pointed to by instance variable 'fileName'
	 * and decrypts the contents of the instance variable 'fileContents' and writes
	 * to the file, then it closes the file.
	 */
	public void writeFileContents(String key) {
		try {
			
			//decrypts fileContents before writing to file
			this.fileContents = decrypt(this.fileContents, key);
			
			fileWriting(this.fileName, this.fileContents);
			System.out.println( "File written successfully.");
			
		} catch (IOException ioe ) {
			System.err.println( "Issue with writing.");
		} catch (IllegalArgumentException iae) {
			System.err.println( iae.getMessage() );
		}
	}
	
	/**
     * This method returns the hashcode for a FileClackData object
     * @return int
     */
    @Override
    public int hashCode() {
        int hash = 17;
        hash = 31 * hash + super.getUserName().hashCode();
        hash = 31 * hash + this.fileName.hashCode();
        return hash;
    }
    
	/**
     * This method checks to see if two FileClackData methods
     * are equal returns true if they are and false otherwise.
     * @param obj A FileClackData object
     * @return boolean
     */
    @Override
    public boolean equals(Object other) {
    	
        if(!(other instanceof FileClackData)) return false; //checks type of object to make sure it is of FileClackData
        FileClackData otherFileClackData = (FileClackData)other; //type cast
        
        if(otherFileClackData == null || otherFileClackData.getClass() != this.getClass()) {return false;} //verifies reference and class type
        return super.getUserName() == otherFileClackData.getUserName() && this.fileName == otherFileClackData.getFileName(); //return true if variables match
    }
	
	/**
	 * Should be overridden to return a full description of the class with all instance
	 * variables, including those in super class
	 */
	@Override
	public String toString() {
		return "File Description. \n Username: " + super.getUserName() + " Type: " +
				super.getType() + " Date: " + super.getDate() + " Filename: "  +
				this.fileName + " File Contents: " + this.fileContents; 
	}
}
