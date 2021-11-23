package data;
/**
 * This class is simply used as a flag to the server inorder to remove a disconnected client.
 * @author Anthony Lombardi
 */
public class LogOut extends ClackData{
    @Override
    public String getData() {
        return null;
    }

    @Override
    public String getData(String key) {
        return null;
    }
}
