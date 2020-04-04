import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

import java.io.*;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by M.Mahdi2414 on 19/06/2017 at 12:51 PM.
 */
public class OthersProfileController implements Serializable {
    public ImageView like;
    double imageSize = 89.0;
    @FXML
    VBox vBox = new VBox(5);
    static String id;
    HBox[] hBoxes;
    public Label ID;
    public Pane postPane;
    public Label numberOfPost;
    public Pane followerPane;
    public Label numberOfFollower;
    public Pane followingPane;
    public Label numberOfFollowing;
    public ScrollPane pagePost;
    public Button followButton;
    public Pane bioPane;
    public Label fullName;
    static boolean newNotifs;
    static User theUser;
    static String followOrFollowing;
    ArrayList<ImageView> pics = new ArrayList<>();

    @FXML
    void initialize() {
        // System.out.println("initializing profile fxml");
        Client.allPostsList = new ArrayList<>();
        try {
            newNotifs=Client.objectInputStream.readBoolean();
            theUser = (User) Client.objectInputStream.readObject();
            id=theUser.getid();

            if (newNotifs) {
                like.setImage(new Image("like(notifes)2.jpg"));
            } else {
                like.setImage(new Image("like(notifs).jpg"));
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        for (Post p : theUser.getPosts()) {
            Client.allPostsList.add(p.getTheFileOfPost());
        }

        hBoxes = new HBox[Client.allPostsList.size()/3+1];
        try {
            addAllPhoto();
        } catch (FileNotFoundException e) {
        }
        //asli
        ID.setText(theUser.getid());
        int i = theUser.followers.size();
        if(theUser.followersIds.contains(Client.ID)) {
            followButton.setText("Unfollow");
        }
        else if(!theUser.followersIds.contains(Client.ID) && theUser.isPrivate())
        {
            for(Activity a:theUser.getActivities()){
                if(a.getType().equals(Type.Request) && a.getNotifier().getid().equals(Client.ID)){
                    followButton.setText("Requested");
                }
            }
        }
        numberOfFollower.setText(String.valueOf(i));
        numberOfFollowing.setText(String.valueOf(theUser.getFollowings().size()));
        numberOfPost.setText(String.valueOf(theUser.getPosts().size()));
        fullName.setText(theUser.getFullName());
    }
    @FXML
    void goToExplore() throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("Explore.fxml"));
        Client.stage.setScene(new Scene(root));
        Client.stage.show();
    }

    void changeFollowButton() {
        String s = followButton.getText();
        //int tedadFollowing = Integer.parseInt(numberOfFollowing.getText());
        // to do : request
        if (s.equals("Follow"))
        {
            //send info
            if(!theUser.isPrivate())
                followButton.setText("Unfollow");
            else
                followButton.setText("Requested");
        }
        else if(s.equals("Requested")) {
            followButton.setText("Unfollow");
        }
        else{
            followButton.setText("Follow");
        }
        String followerNum= String.valueOf(theUser.followers.size());
        numberOfFollower.setText(followerNum);
        System.out.println("followers and followings numbers button updated");

    }

    @FXML
    void Follow() {

        try {
            Client.stageShowed.writeUTF("othersProfile");
            Client.stageShowed.flush();
            //writing howwant
            Client.username.writeUTF(followButton.getText());
            Client.username.flush();
            //writing object
            Client.username.writeObject(theUser);
            Client.username.flush();
            //theUser = (User) Client.objectInputStream.readObject();
            String s=Client.objectInputStream.readUTF();
            theUser= (User) Client.objectInputStream.readObject();
            changeFollowButton();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
    @FXML
    void goToProfile() throws IOException {
        Client.stageShowed.writeUTF("wantSendUser");
        Client.stageShowed.flush();
        Parent root = FXMLLoader.load(getClass().getResource("Profile.fxml"));
        Scene scene = new Scene(root);
        Client.stage.setScene(scene);
        Client.stage.show();
    }
    @FXML
    void goToHome() throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("Home.fxml"));
        Client.stage.setScene(new Scene(root));
        Client.stage.show();
    }
    @FXML
    void showFollowers() throws IOException{
        followOrFollowing="follower";
        Parent root = FXMLLoader.load(getClass().getResource("FollowersFollowings.fxml"));
        Client.stage.setScene(new Scene(root));
        Client.stage.show();
    }
    @FXML
    void showFollowings()throws IOException{
        followOrFollowing="following";
        Parent root = FXMLLoader.load(getClass().getResource("FollowersFollowings.fxml"));
        Client.stage.setScene(new Scene(root));
        Client.stage.show();
    }


    void addAllPhoto() throws FileNotFoundException {
        int j=0;
        for (File i : Client.allPostsList) {
            pics.add(createImageView(i));
            pics.get(j).setFitHeight(imageSize);
            pics.get(j).setFitWidth(imageSize);
            if (j%3==0)
                hBoxes[j/3] = new HBox(5);
            hBoxes[j/3].getChildren().add(pics.get(j));
            j++;

        }
        if(j>0) {

            vBox.getChildren().addAll(hBoxes);

            pagePost.setContent(vBox);

        }
        ;
    }
    private ImageView createImageView(final File imageFile) {
        // DEFAULT_THUMBNAIL_WIDTH is a constant you need to define
        // The last two arguments are: preserveRatio, and use smooth (slower) resizing

        ImageView imageView = null;
        try {

            final Image image;

            image = new Image(new FileInputStream(imageFile), imageSize, 99, true, true);
            imageView = new ImageView(image);

        } catch (FileNotFoundException ex) {
            //    Logger.getLogger(GalleryController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return imageView;

    }
    @FXML
    void goToCamera() throws IOException {

        //  Client.stage = camera1;
        Parent root = FXMLLoader.load(getClass().getResource("Camera.fxml"));
        Client.stage.setScene(new Scene(root));
        Client.stage.show();
    }
    @FXML
    void sendMessage() throws IOException {
        DMController.chattingWith=id;
//        Client.objectOutputStream.writeUTF(chattingWith);
//        Client.stageShowed.flush();
        Parent root = FXMLLoader.load(getClass().getResource("Chat.fxml"));
        Client.stage.setScene(new Scene(root));
        Client.stage.show();
    }
}


