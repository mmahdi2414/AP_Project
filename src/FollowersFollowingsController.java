import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Asus on 6/25/2017.
 */
public class FollowersFollowingsController {
    public ScrollPane mainScroller;
    public ImageView like;
    public ArrayList<User> following=new ArrayList<>();
    public ArrayList<User> follower=new ArrayList<>();
    public VBox vbox=new VBox(2);
    public Label followerOrwhat;


    @FXML
    public void  initialize(){
        if (OthersProfileController.newNotifs) {
            like.setImage(new Image("like(notifes)2.jpg"));
        } else {
            like.setImage(new Image("like(notifs).jpg"));
        }
        if(OthersProfileController.followOrFollowing.equals("follower")){
            follower=OthersProfileController.theUser.getFollowers();
            followerOrwhat.setText("Followers");
            followerOrwhat.setAlignment(Pos.BASELINE_CENTER);
            followerOrwhat.setContentDisplay(ContentDisplay.CENTER);
            followerOrwhat.setTextAlignment(TextAlignment.CENTER);
            followerOrwhat.setTextFill(Paint.valueOf("#009999"));
            Font font = (javafx.scene.text.Font.font("Comic Sans MS",20));
            followerOrwhat.setFont(font);
            for(User u:follower){
                final User u2=u;
                Pane p= makePane(u.getProfileImageFile(),u.getid());
                p.setOnMouseClicked(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent mouseEvent) {
                        showProfile(u2.getid());
                    }
                });
                vbox.getChildren().add(p);
            }
            mainScroller.setContent(vbox);
        }
        else
            {
            following=OthersProfileController.theUser.getFollowings();
                followerOrwhat.setText("Followings");
                followerOrwhat.setAlignment(Pos.BASELINE_CENTER);
                followerOrwhat.setContentDisplay(ContentDisplay.CENTER);
                followerOrwhat.setTextAlignment(TextAlignment.CENTER);
                followerOrwhat.setTextFill(Paint.valueOf("#009999"));
                Font font = (javafx.scene.text.Font.font("Comic Sans MS",20));
                followerOrwhat.setFont(font);
                for(User f:following){
                    for(User u:following){
                        final User u2=u;
                        Pane p=makePane(u.getProfileImageFile(),u.getid());
                        p.setOnMouseClicked(new EventHandler<MouseEvent>() {
                            @Override
                            public void handle(MouseEvent mouseEvent) {
                                showProfile(u2.getid());
                            }
                        });
                        vbox.getChildren().add(p);

                    }
                    mainScroller.setContent(vbox);
                }
            }

    }
    @FXML
    void goToCamera() throws IOException {
        Client.objectOutputStream.writeUTF("goToCamera");
        Client.objectOutputStream.flush();
        //  Client.stage = camera1;
        Parent root = FXMLLoader.load(getClass().getResource("Camera.fxml"));
        Client.stage.setScene(new Scene(root));
        Client.stage.show();
    }
    @FXML
    void goToNotificationsLog() throws IOException{
        Client.objectOutputStream.writeUTF("goToNotificationsLog");
        Client.objectOutputStream.flush();
        try {
            Client.stageShowed.writeUTF("notificationsLog");
            Client.stageShowed.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Parent root = FXMLLoader.load(getClass().getResource("NotificationsLog.fxml"));
        Client.stage.setScene(new Scene(root));
        Client.stage.show();
    }

    @FXML
    void goToExplore() throws IOException {
        Client.objectOutputStream.writeUTF("goToExplore");
        Client.objectOutputStream.flush();
        Parent root = FXMLLoader.load(getClass().getResource("Explore.fxml"));
        Client.stage.setScene(new Scene(root));
        Client.stage.show();
    }
    @FXML
    void goToProfile() throws IOException {
        Client.stageShowed.writeUTF("goToProfile");
        Client.stageShowed.flush();
        Client.stageShowed.writeUTF("wantSendUser");
        Client.stageShowed.flush();
        Parent root = FXMLLoader.load(getClass().getResource("Profile.fxml"));
        Scene scene = new Scene(root);
        Client.stage.setScene(scene);
        Client.stage.show();
    }
    @FXML
    void showProfile(String id) {
        try {
            Client.objectOutputStream.writeUTF("goToOthersProfile");
            Client.objectOutputStream.flush();
            Client.stageShowed.writeUTF("showProfile");
            Client.stageShowed.flush();
            Client.username.writeUTF(id);
            Client.username.flush();
        } catch (IOException e) {
            System.out.println("ioe write in show profile");
        }


        try {
            //System.out.println("get loader");
            Parent root = FXMLLoader.load(getClass().getResource("OthersProfile.fxml"));
            //System.out.println("loaeded");
            Client.stage.setScene(new Scene(root));
            Client.stage.show();

        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("fxml loader ioe");
        }

    }
    @FXML
    public Pane makePane(File f,String s){
        Pane p=new Pane();
        p.prefHeight(70);
        p.prefWidth(287);
        ImageView image=createImageView(f);
        image.setFitHeight(70);
        image.setFitWidth(70);
        Label l=makeLabel(s);
        l.setLayoutX(92);
        l.setLayoutY(17);
        l.prefHeight(37);
        l.prefWidth(124);
        p.getChildren().add(image);
        p.getChildren().add(l);
        return p;
    }
    Label makeLabel(String text) {
        Label label = new Label(text);
        label.setAlignment(Pos.BASELINE_LEFT);
        label.setContentDisplay(ContentDisplay.LEFT);
        //label.setPrefHeight(43);
        label.setTextAlignment(TextAlignment.LEFT);
        label.setTextFill(Paint.valueOf("#009999"));
        Font font = (javafx.scene.text.Font.font("Comic Sans MS",16));
        label.setFont(font);
        return label;
    }
    private ImageView createImageView(final File imageFile) {
        // DEFAULT_THUMBNAIL_WIDTH is a constant you need to define
        // The last two arguments are: preserveRatio, and use smooth (slower) resizing

        ImageView imageView = null;
        try {

            final Image image;

            image = new Image(new FileInputStream(imageFile),1080,1080, true, true);
            imageView = new ImageView(image);

        } catch (FileNotFoundException ex) {
            //    Logger.getLogger(GalleryController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return imageView;
    }

}
