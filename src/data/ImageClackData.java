package data;
/**
 * This subclass holds Image information and related methods.
 * @author Anthony Lombardi
 *
 */
public class ImageClackData extends ClackData {
    private byte[] image;

    /**
     * This represents a constructor for the ImageClackData
     * @param username The username of the client
     * @param image an image that was converted to a byte array
     * @param type An integer representing the type of clackdata
     */
    public ImageClackData(String username, byte[] image, int type) {
        super(username,type);
        this.image = image;
    }

    /**
     * @return A byte array representing an image
     */
    public byte[] getImage() {
        return image;
    }

    @Override
    public String getData() {
        return "null";
    }

    @Override
    public String getData(String key) {
        return "null";
    }
}
