
package data;

/**
 * This subclass holds IM information and related methods.
 * @author Alif_Jakir
 *
 */
public class MessageClackData extends ClackData {

	private String message; /** String representing instant message. */
	

	/** Constructor for immediately encrypting the message using the key */
	public MessageClackData( String userName, String message, String key, int type ) {
		super(userName, type);
		this.message = super.encrypt(message, key);
	}
	

	/** Constructor for instantiating object of MessageClackData */
	public MessageClackData(String userName, String message, int type) {
		super(userName, type);
		this.message = message;
	}
	
	/** Default constructor for MessageClackData */
	public MessageClackData() {
		super();
		this.message = "NULL";
	}
	
	/**
	 * Implemented here to return instant message.
	 */
	public String getData( ) {
		return this.message;
	}
	
	/**Decrypts the string in 'message' and returns the decrypted string   */
	@Override 
	public String getData( String key ) {
		return super.decrypt(this.message,key);
	}
	
	/**
     * This method returns the hashcode for a MessageClackData object
     * @return int
     */
    @Override
    public int hashCode() {
        int hash = 17;
        hash = 31 * hash + super.getUserName().hashCode();
        hash = 31 * hash + this.message.hashCode();
        return hash;
    }
	
    /**
     * This method checks to see if two MessageClackData methods
     * are equal returns true if they are and false otherwise.
     * @param obj A ClackClient object
     * @return boolean
     */
    @Override
    public boolean equals(Object other) { 
        if(!(other instanceof MessageClackData)) return false; //checks type of object to make sure it is of MessageClackData
        MessageClackData otherMessageClackData = (MessageClackData)other; //type cast
   
        if(otherMessageClackData == null || otherMessageClackData.getClass() != this.getClass()) {return false;} //verifies reference and class type
        return super.getUserName() == otherMessageClackData.getUserName() && this.message == otherMessageClackData.getData(); //return true if variables match
        
        
    }
	
	/**
	 * Should be overridden to return a full description of the class with all instance
	 * variables, including those in super class
	 */
	@Override
	public String toString() {
		return "Message Description. \n Username: " + super.getUserName() + " Type: " +
				super.getType() + " Date: " + super.getDate() + " Message: " + this.message;
	}	
	
	
}