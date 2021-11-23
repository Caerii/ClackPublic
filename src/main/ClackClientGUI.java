package main;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Slider;
import javafx.scene.control.TextArea;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import javax.imageio.ImageIO;
import java.nio.file.Files;



/**
 * This is a class the contains and drives the GUI from the clackclient
 * @author Anthony Lombardi
 */
public class ClackClientGUI extends Application {
    private ClackClient client; /**Variable representing the clack client that the gui is driving*/
    private TextFlow textAreaHistory; /**Variable to represent the text flow that the message history will be displayed in*/
    private TextArea textAreaFriends; /**Variable to represent the text area that the friends in the server will be displayed in*/
    private String username; /**A String that is the clients username*/
    private ArrayList<String> friendList; /**ArrayList to represent the current friends in the server*/
    private ScrollPane scrollPane; /**Variable to represent the scrollpane that the message history will be displayed on*/
    private URL url; /**Variable to represent the URL for the Image and media*/
    private String ext; /**Variable to represent extension of the URL*/
    private boolean stopRequested = false; /**Bool for the pause play button of the media player*/

    /**
     * Main method that runs the GUI
     * @param primaryStage
     * @throws Exception
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        BorderPane root = new BorderPane();


        VBox history = new VBox(); //Holds the history textField
        textAreaHistory = new TextFlow(); //A part of the history
        Button enterButton = new Button("Send"); //A part of the inputBox
        Button multiMediaButton = new Button("Media"); //A part of the inputBox
        TextArea textField = new TextArea(); //A part of the inputBox

        VBox loginTmp = new VBox();
        TextArea loginText = new TextArea("Username@hostname:port"); //Where we get user connection data as Username@Hostname:Port
        Button loginButton = new Button("Login"); //after this button is pressed, login info disappears

        //textAreaHistory.setEditable(false);
        textAreaHistory.setPrefSize(800,650);
        textAreaHistory.getStyleClass().add("ipad-dark-grey");
        //textAreaHistory.setAlignment(TOP_LEFT);

        scrollPane = new ScrollPane();
        scrollPane.setId("presentationScrollPane");
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setFitToWidth(true);
        scrollPane.setContent(textAreaHistory);


        textField.setPrefSize(700,60);

        history.setMaxSize(1100,800);

        loginTmp.getChildren().add(loginText);
        loginTmp.getChildren().add(loginButton);
        loginButton.getStyleClass().add("buttontheme");
        root.setCenter(loginTmp);

        enterButton.getStyleClass().add("buttontheme");
        multiMediaButton.getStyleClass().add("buttontheme");

        history.getChildren().add(scrollPane); //message history for all users
        history.getChildren().add(textField); //user will input messages here
        history.getChildren().add(enterButton); //send messages using this button
        history.getChildren().add(multiMediaButton); //send multimedia using this button

        VBox friends = new VBox(); //Holds the list of friends in the channel
        textAreaFriends = new TextArea(); //A part of the friends
        friendList = new ArrayList<>();

        friends.getStyleClass().add("ipad-darker");
        textAreaFriends.getStyleClass().add("ipad");
        friends.setPrefSize(120,800);
        textAreaFriends.setPrefSize(10,710);
        textAreaFriends.setEditable(false);
        loginText.setPrefSize(10,50);
        friends.getChildren().add(textAreaFriends);

        root.setRight(friends);
        root.getStyleClass().add("ipad-darker-grey");

        //Enter Button Handler
        enterButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                client.readClientData(textField.getText());
                client.sendData();
                textField.setText("");
            }
        });

        //MultiMedia Button Handler
        multiMediaButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    FileChooser fileChooser = new FileChooser();
                    File multiMedia = fileChooser.showOpenDialog(primaryStage);
                    url = new URL("file:///" + multiMedia.toString());
                    ext = (url.toString().split("\\."))[1];
                    if (ext.equals("jpg"))
                        client.readClientData("SEND_IMAGE");
                    else if (ext.equals("mp4"))
                        client.readClientData("SEND_MEDIA");
                    client.sendData();
                } catch (MalformedURLException mue) {
                    System.err.println(mue.getMessage());
                }
            }
        });

        Service<String> service = new Service<String>() {
            @Override
            protected Task<String> createTask() {
                Task<String> task = new Task<String>() {
                    @Override
                    protected String call() {
                        client.start();
                        return "done";
                    }
                };
                return task;
            }
        };

        loginButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                String input = loginText.getText();
                //login screen removed after successful login
                if (input.contains("@") && input.contains(":")) {
                    String[] parsedAt = input.split("@");
                    String[] parsedCol = parsedAt[1].split(":");
                    username = parsedAt[0];
                    client = new ClackClient(parsedAt[0],parsedCol[0],Integer.parseInt(parsedCol[1]));
                } else if (input.contains("@")) {
                    String[] parsedAt = input.split("@");
                    username = parsedAt[0];
                    client = new ClackClient(parsedAt[0],parsedAt[1]);
                } else {
                    username = input;
                    client = new ClackClient(input);
                }
                root.setCenter(history);
                service.start();
                setGUI();
                //requires error checking for if it's in correct format
            }
        });

        textAreaHistory.getChildren().addListener((ListChangeListener<Node>) ((change) -> {
            textAreaHistory.layout();
            scrollPane.layout();
            scrollPane.setVvalue(1.0f);
        }));
        scrollPane.setContent(textAreaHistory);

        Scene scene = new Scene(root,1200,800);
        scene.getStylesheets().add(getClass().getResource("main.css").toExternalForm());
        primaryStage.setTitle("Clack");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /**
     * Called in the ClackClient to update the friends list on the gui
     * @param friend A String that is the username of a friend
     */
    public void updateFriends(String friend) {
        System.out.println(friend);
        if(!friendList.contains(friend)) {
            textAreaFriends.setText(textAreaFriends.getText() + "\n" + friend);
            friendList.add(friend);
        }
    }


    /**
     *  Method to update the history with a new message from the server
     * @param username Username of the user who sent the message
     * @param message The message sent by the user
     * @param date The Date the message was sent
     */
    public void updateHistory(String username, String message, Date date) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                String formattedMessage = username+": "+message+"\n"+"["+date+"]\n";
                Text txt = new Text(formattedMessage);
                textAreaHistory.getChildren().add(txt);
            }
        });
    }

    /**
     * sends an instance of `this` to the client
     */
    public void setGUI() {
        client.getGUI(this);
    }

    /**
     * This method converts a URL to a byte array
     * @return A byte array that is an image
     */
    public byte[] getImage() {
        try {
            BufferedImage bImg = ImageIO.read(url);
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ImageIO.write(bImg,ext,bos);
            byte[] data = bos.toByteArray();
            bos.close();
            return data;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * This method converts a URL to a byte array
     * @return A byte array that is a media
     */
    public byte[] getMedia() {
        try {
            System.out.println(url);
            System.out.println(new File(url.toString().substring(6)));
            return Files.readAllBytes((new File(url.toString().substring(6))).toPath());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * This methods receives a byte array from the server and turns it into an image to bediplayed
     * @param image A byte array that is an image
     * @param username Username of the client that sent the image
     */
    public void updateImage(byte[] image,String username) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                try {
                    ByteArrayInputStream bis = new ByteArrayInputStream(image);
                    BufferedImage bImg = ImageIO.read(bis);
                    textAreaHistory.getChildren().add(new Text(username+": \n"));
                    textAreaHistory.getChildren().add(new ImageView(SwingFXUtils.toFXImage(bImg, null)));
                    textAreaHistory.getChildren().add(new Text("\n["+new Date()+"]\n"));
                    bis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    /**
     * This method turns a byte array into a media object to be displayed
     * @param data A byte array
     * @param username A username from the sending client
     */
    public void updateMedia(byte[] data, String username) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                try {
                    File dir = new File("tmp\\test");
                    dir.mkdirs();
                    File tmp = new File(dir, "tmp.mp4");
                    tmp.createNewFile();
                    OutputStream os = new FileOutputStream(tmp);
                    os.write(data);
                    textAreaHistory.getChildren().add(new Text(username+": \n"));
                    String pathToMedia = URI.create("file:///" + tmp.getAbsolutePath().replace(" ","%20").replace("\\","/" )).toString();
                    MediaPlayer mediaPlayer = new MediaPlayer(new Media(pathToMedia));
                    mediaPlayer.setAutoPlay(true);
                    Slider slider = new Slider();
                    slider.valueProperty().addListener(new ChangeListener<Number>() {
                        @Override
                        public void changed(ObservableValue<? extends Number> observable,
                                            Number oldValue, Number newValue) {
                            //if you want to go to a specific frame in your video
                            mediaPlayer.seek( new Duration( newValue.doubleValue() * 1000 ));
                        }
                    });
                    VBox mediaBox = new VBox();
                    mediaBox.setPrefSize(500, 500);
                    MediaView mediaView = new MediaView(mediaPlayer);
                    mediaView.setPreserveRatio(true);
                    mediaView.setFitHeight(800);
                    mediaView.setFitWidth(800);
                    mediaBox.getChildren().add(mediaView);

                    Button playButton = new Button("||");
                    playButton.getStyleClass().add("buttontheme");

                    playButton.setOnAction(new EventHandler<ActionEvent>() {
                        public void handle(ActionEvent e) {
                            MediaPlayer.Status status = mediaPlayer.getStatus();

                            if (status == MediaPlayer.Status.UNKNOWN  || status == MediaPlayer.Status.HALTED)
                            {
                                // don't do anything in these states
                                return;
                            }

                            if ( status == MediaPlayer.Status.PAUSED
                                    || status == MediaPlayer.Status.READY
                                    || status == MediaPlayer.Status.STOPPED)
                            {
                                // rewind the movie if we're sitting at the end
                                mediaPlayer.play();
                            } else {
                                mediaPlayer.pause();
                            }
                        }
                    });

                    mediaPlayer.setOnPlaying(new Runnable() {
                        public void run() {
                            if (stopRequested) {
                                mediaPlayer.pause();
                                stopRequested = false;
                            } else {
                                playButton.setText("||");
                            }
                        }
                    });

                    mediaPlayer.setOnPaused(new Runnable() {
                        public void run() {
                            playButton.setText(">");
                        }
                    });

                    mediaBox.getChildren().add(playButton);
                    mediaBox.getChildren().add(slider);
                    textAreaHistory.getChildren().add(mediaBox); //"file:///D:/Documents/CS242%20Stuff/Clack/tmp/test/tmp.mp4"
                    textAreaHistory.getChildren().add(new Text("\n["+new Date()+"]\n"));
                    os.close();

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public static void main(String[] args) {
        launch(args);
    }

}
