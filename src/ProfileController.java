import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
import javafx.scene.shape.StrokeType;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by M.Mahdi2414 on 02/06/2017 at 05:25 PM.
 */
enum howWantToShowPost{
    GRIDPANE,
    INAROW;
}
public class ProfileController {
    static howWantToShowPost postToShow = howWantToShowPost.GRIDPANE;
    public Label numberOfPost;
    static boolean newNotifs = false;
    public Label numberOfFollower;
    public Label numberOfFollowing;
    public ImageView profileImageView;
    public ScrollPane infoPos;
    public ImageView gridView;
    public ImageView inRow;
    static String followOrFollowing;
    Label fullName;
    Text bio;
    Hyperlink website;
    double imageSize = 89.0;
    @FXML
    ImageView profile, home, search, camera, setting, like;
    @FXML
    Label id;
    @FXML
    ScrollPane postsPos;
    VBox vBox = new VBox(5);
    HBox[] hBoxes;
    ArrayList<ImageView> pics = new ArrayList<>();
    ArrayList<Image> images = new ArrayList<>();
    static User user;
    private Label lks;

    //  ObservableList<AnchorPane> POSTS;
    @FXML
    public void initialize() {
        try {
            user = (User) Client.objectInputStream.readObject();
            System.out.println(user.getid());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        try {

            for (Activity a : user.getActivities()) {
                if (!a.getSeen())
                    newNotifs = true;
            }
            if (newNotifs) {
                like.setImage(new Image("like(notifes)2.jpg"));
            } else {
                like.setImage(new Image("like(notifs).jpg"));
            }
            Client.allPostsList = new ArrayList<>();
            profileImageView.setImage(createImageView(user.getProfileImageFile()).getImage());
            numberOfFollower.setText(String.valueOf(user.getFollowers().size()));
            numberOfFollowing.setText(String.valueOf(user.getFollowings().size()));
            numberOfPost.setText(String.valueOf(user.getPosts().size()));
            VBox vBox = new VBox(2);
            fullName = makeLabel(user.getFullName(), 14);
            vBox.getChildren().add(fullName);

            if (!user.getBio().equals("")) {
                bio = makeText(user.getBio(), 14);
                vBox.getChildren().add(bio);
            }
            if (!user.getWebsite().equals("")) {
                website = makeHyperLink(user.getWebsite(), 14);
                vBox.getChildren().add(website);
            }
            infoPos.setContent(vBox);
            System.out.println(user.getPosts().size());
            for (Post p : user.getPosts()) {
                Client.allPostsList.add(p.getTheFileOfPost());
            }
            // Client.allPosts = new File(Client.ID+"PostsInClient");

            // if (!Client.allPosts.isDirectory())
            //     Client.allPosts.mkdir();

            //else if (Client.allPostsList.isEmpty()){
            //    File[] posts = Client.allPosts.listFiles();
            //     for (int i = 0; i < posts.length; i++) {
            //        Client.allPostsList.add(posts[i]);
            //  }
            //}

            initializeId();
            hBoxes = new HBox[(Client.allPostsList.size() - 1) / 3 + 1];
            try {
                if(postToShow.equals(howWantToShowPost.GRIDPANE))
                    addAllPhotoInGridView();
                else addAllPhotoInRow();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    private void initializeId() {
        id.setVisible(true);
        id.setText(user.getid());
        id.setAlignment(Pos.CENTER);
        id.setContentDisplay(ContentDisplay.CENTER);
        id.setLayoutX(60.0);
        id.setPrefHeight(37.0);
        id.setPrefWidth(167.0);
        id.setTextFill(Paint.valueOf("#154ab5"));
    }
    Text makeText(String txt, int fontSize) {
        Text text = new Text(txt);
        text.setTextAlignment(TextAlignment.LEFT);
        text.setFill(Paint.valueOf("#000000"));
        Font font = new Font("Comic Sans MS", fontSize);
        text.setFont(font);
        return text;

    }
    Hyperlink makeHyperLink(String text, int fontSize) {
        Hyperlink label = new Hyperlink(text);
        label.setAlignment(Pos.CENTER);
        label.setContentDisplay(ContentDisplay.CENTER);
        label.setTextAlignment(TextAlignment.CENTER);
        label.setTextFill(Paint.valueOf("#000000"));
        Font font = new Font("Comic Sans MS", fontSize);
        label.setFont(font);
        return label;

    }
    Label makeLabel(String text, int fontSize) {
        Label label = new Label(text);
        label.setAlignment(Pos.CENTER);
        label.setContentDisplay(ContentDisplay.CENTER);
        label.setTextAlignment(TextAlignment.CENTER);
        label.setTextFill(Paint.valueOf("#000000"));
        Font font = new Font("Comic Sans MS", fontSize);
        label.setFont(font);
        return label;

    }
    @FXML
    void goToSetting(Event event) throws IOException {
        Client.objectOutputStream.writeUTF("backToPro");
        Client.objectOutputStream.flush();
        Parent root = FXMLLoader.load(getClass().getResource("ProfileSetting.fxml"));
        Client.stage.setScene(new Scene(root));
        Client.stage.show();
    }
    @FXML
    void addAllPhotoInRow(){
        try {
            Client.stageShowed.writeUTF("PhotoInRowMyProfile");
            Client.stageShowed.flush();
        }
        catch (IOException e){
            e.printStackTrace();
        }
        postToShow = howWantToShowPost.INAROW;
        int i =0;
        double imageSize, paneSize1, paneSize2, hBoxSize1, hBoxSize2;
        paneSize1 = 55;
        paneSize2 = 64;
        imageSize = 287;
        ArrayList<Pane> posts  =new ArrayList<>();
        for ( Post p : user.getPosts() ) {
            final Post p1=p;
            //final int l=k;
            MenuButton menuButton = new MenuButton("more");
            final MenuItem remove = new MenuItem("remove"),edit = new MenuItem("edit");
//            final MenuItem share = new MenuItem("Share");
            remove.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    remove(p1);
                }
            });
            menuButton.getItems().addAll(remove,edit);

            Pane pane = new Pane();
            pane.setStyle("-fx-background-color: white;");
            ImageView imageView = createImageView(p.getTheFileOfPost());
            imageView.setFitWidth(imageSize);
            imageView.setFitHeight(imageSize);
            imageView.setLayoutX(-1);
            imageView.setLayoutY(55);
            VBox vbox = new VBox(30);
            vbox.setLayoutX(2);
            vbox.setLayoutY(344);
            HBox hBox = new HBox(60);
            hBox.prefHeight(32);
            hBox.prefWidth(488);
            Text caption = new Text();
            //caption.setLineSpacing(2);
            caption.setStrokeType(StrokeType.OUTSIDE);
            caption.setStrokeWidth(0);
            caption.setWrappingWidth(282.0000045001507);
            caption.setText(p.getCaption());
            caption.setFont(new Font("Comic Sans MS",14));
            if(p.getCaption().length()>0)
                vbox.getChildren().add(caption);
            HBox hBox1 = new HBox(45);
            hBox1.setLayoutX(-1);
            HBox hBox2 = new HBox(20);
            Button button = new Button("likes: ");
            button.setMnemonicParsing(false);
            final String likers = String.valueOf(p.getLikers().size());
            lks = new Label(likers);
            lks.setAlignment(Pos.CENTER);
            lks.setTextAlignment(TextAlignment.CENTER);
            lks.setFont(new Font("Comic Sans MS Bold" , 14));
            button.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent actionEvent) {
                    like(p1);
                    boolean b = true;
                    try {
                        b = Client.objectInputStream.readBoolean();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    if (b) {
                        Button button1 = (Button) actionEvent.getSource();
                        int l = Integer.parseInt(likers);
                        l++;
                        lks.setText(String.valueOf(l));
                    } else {
                        Button button1 = (Button) actionEvent.getSource();
                        int l = Integer.parseInt(likers);

                        lks.setText(String.valueOf(l));
                        l--;
                    }

                }



            });
            //p=p1;

            lks.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    goToLikers(p1);
                }
            });
            Button button2 = new Button("cm");//to do mesle bala
            button2.setMnemonicParsing(false);
            button2.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent actionEvent) {

                    comment(p1);
                }

            });
            hBox2.getChildren().add(button);
            hBox2.getChildren().add(lks);
            if (!p.cmIsClose)
                hBox2.getChildren().add(button2);

            // Label label = new Label("id taraf");
            Label label = new Label();
            label.setAlignment(Pos.CENTER);
            label.setTextAlignment(TextAlignment.CENTER);
            label.setText(p.getDate());
            label.setFont(new Font("Comic Sans MS Bold" , 17));
//            hBox2.getChildren().add(button2);
            // Label label = new Label("id taraf");
            Label label1 = new Label(p.getPosterUser().getid());
            label1.setAlignment(Pos.CENTER);
            label1.setTextAlignment(TextAlignment.CENTER);
            label1.setFont(new Font("Comic Sans MS Bold" , 17));
            hBox.getChildren().add(hBox2);
            hBox.getChildren().add(label);

            vbox.getChildren().add(hBox);
            ImageView imageView1 = createImageView(p.getPosterUser().getProfileImageFile());
            imageView1.setPreserveRatio(false);
            imageView1.setFitWidth(paneSize1);
            imageView1.setFitHeight(paneSize2);
            hBox1.getChildren().add(imageView1);
            hBox1.getChildren().add(label1);//to do user bgire ax o inashm add kone
            hBox1.getChildren().add(menuButton);
            hBox1.setId(p.getPosterUser().getid());
            pane.getChildren().add(0, hBox1);
            pane.getChildren().add(1, imageView);
            pane.getChildren().add(2, vbox);

            // pane.getChildren()
            posts.add(pane);
            i++;
        }
        VBox vbox1 = new VBox(2);
        vbox1.getChildren().addAll(posts);
        postsPos.setContent(vbox1);
//        System.out.println(paneOfPost.getChildren().toString());
    }

    private void goToLikers(Post p) {
        try {
            Client.objectOutputStream.writeUTF("goToLikers");
            Client.objectOutputStream.flush();
            Client.objectOutputStream.reset();
            Client.objectOutputStream.writeObject(p);
            Client.objectOutputStream.flush();
            Parent root = FXMLLoader.load(getClass().getResource("Likers.fxml"));
            Client.stage.setScene(new Scene(root));
            Client.stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void remove(Post p){
        try {
            Client.objectOutputStream.writeUTF("backToPro");
            Client.objectOutputStream.flush();
            Client.stageShowed.writeUTF("removePost");
            Client.stageShowed.flush();
            Client.objectOutputStream.reset();
            Client.objectOutputStream.writeObject(p);
            Client.objectOutputStream.flush();
            Parent root = FXMLLoader.load(getClass().getResource("Profile.fxml"));
            Client.stage.setScene(new Scene(root));
            Client.stage.show();
        }
        catch (IOException e){

        }
    }
    @FXML
    void addAllPhotoInGridView() throws FileNotFoundException {
try {
          Client.objectOutputStream.writeUTF("backToPro");
          Client.objectOutputStream.flush();
//            Client.stageShowed.writeUTF("PhotoInRow");
  //Client.stageShowed.flush();
        } catch (IOException e) {
      e.printStackTrace();
    }
//        try {
//            Client.stageShowed.writeUTF("PhotoInRow");
//            Client.stageShowed.flush();
//        } catch (IOException e) {
//        e.printStackTrace();
//    }
        vBox.getChildren().clear();
        postToShow =howWantToShowPost.GRIDPANE;
        int j = 0;
        for (Post p : user.getPosts()) {
            final Post p1 = p;
            // System.out.println(i.getPath());
            // images.add(new Image(i.getPath()));
            ImageView imageView = createImageView(p.getTheFileOfPost());
            imageView.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    goToPost(p1);
                }
            });
            pics.add(imageView);
            pics.get(j).setFitHeight(imageSize);
            pics.get(j).setFitWidth(imageSize);
            if (j % 3 == 0)
                hBoxes[j / 3] = new HBox(5);
            hBoxes[j / 3].getChildren().add(pics.get(j));
            j++;

        }
        if (j > 0) {

            vBox.getChildren().addAll(hBoxes);

            postsPos.setContent(vBox);

            System.out.println("finished add photo");
        }
        ;
    }

    private void goToPost(Post p) {
        try {
            Client.objectOutputStream.writeUTF("backToPro");
            Client.objectOutputStream.flush();
            Client.stageShowed.writeUTF("Post");
            Client.stageShowed.flush();
            Client.objectOutputStream.reset();
            Client.objectOutputStream.writeObject(p);
            Client.objectOutputStream.flush();
            Parent root = FXMLLoader.load(getClass().getResource("post.fxml"));
            Client.stage.setScene(new Scene(root));
            Client.stage.show();

        } catch (IOException e) {
           e.printStackTrace();
        }
    }

    public void comment(Post p){
        try {
            //request
            Client.objectOutputStream.writeUTF("request to make a comment on a post");
            Client.objectOutputStream.flush();
            Client.objectOutputStream.reset();
            Client.objectOutputStream.writeObject(p);
            Client.objectOutputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("ioe comment request");
        }
        try {
            goToCommentPage();
        } catch (IOException e) {
            System.out.println("ioe fxml loading comment page");
        }
    }

    @FXML
    void goToCommentPage() throws IOException {
//        Client.objectOutputStream.writeUTF("goToCommentPage");
//        Client.objectOutputStream.flush();
        Parent root = FXMLLoader.load(getClass().getResource("CommentPage1.fxml"));
        Client.stage.setScene(new Scene(root));
        Client.stage.show();
    }
    public void like(Post p) {
        try {
            Client.objectOutputStream.writeUTF("request to like a post");
            Client.objectOutputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("ioe like method homecontroller ");
        }
        try {

            Client.objectOutputStream.reset();
            Client.objectOutputStream.writeObject(p);
            Client.objectOutputStream.flush();

        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("ioe write post to server in like method");
        }
    }
    private ImageView createImageView(final File imageFile) {
        // DEFAULT_THUMBNAIL_WIDTH is a constant you need to define
        // The last two arguments are: preserveRatio, and use smooth (slower) resizing

        ImageView imageView = null;
        try {

            final Image image;

            image = new Image(new FileInputStream(imageFile), 1080,1080, true, true);
            imageView = new ImageView(image);

        } catch (FileNotFoundException ex) {
            //    Logger.getLogger(GalleryController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return imageView;

    }

    @FXML
    void goToHome() throws IOException {
        Client.objectOutputStream.writeUTF("backToPro");
        Client.objectOutputStream.flush();
        Parent root = FXMLLoader.load(getClass().getResource("Home.fxml"));
        Client.stage.setScene(new Scene(root));
        Client.stage.show();
    }

    @FXML
    void goToCamera() throws IOException {
        Client.objectOutputStream.writeUTF("backToPro");
        Client.objectOutputStream.flush();
        //  Client.stage = camera1;
        Parent root = FXMLLoader.load(getClass().getResource("Camera.fxml"));
        Client.stage.setScene(new Scene(root));
        Client.stage.show();
    }

    @FXML
    void goToExplore() throws IOException {
        Client.objectOutputStream.writeUTF("backToPro");
        Client.objectOutputStream.flush();
        Parent root = FXMLLoader.load(getClass().getResource("Explore.fxml"));
        Client.stage.setScene(new Scene(root));
        Client.stage.show();

    }

    public void changeProfilePicture() {
        Stage stage = new Stage();
        FileChooser fileChooser = new FileChooser();
        File file = fileChooser.showOpenDialog(stage);

        try {
            Client.objectOutputStream.writeObject(file);
            Client.objectOutputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @FXML
    void goToAcceptChangingProfile() throws IOException {
        try {
            Client.objectOutputStream.writeUTF("backToPro");
            Client.objectOutputStream.flush();
            Client.stageShowed.writeUTF("changeProfile");
            Client.stageShowed.flush();
            changeProfilePicture();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Parent root = FXMLLoader.load(getClass().getResource("AcceptChangingProfilePicture.fxml"));
        Client.stage.setScene(new Scene(root));
        Client.stage.show();
    }

    @FXML
    void goToNotificationsLog() throws IOException{
        Client.objectOutputStream.writeUTF("backToPro");
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
    void goToDMs() throws IOException {
        Client.objectOutputStream.writeUTF("backToPro");
        Client.objectOutputStream.flush();
        Parent root = FXMLLoader.load(getClass().getResource("DM.fxml"));
        Client.stage.setScene(new Scene(root));
        Client.stage.show();
    }
    @FXML
    void showFollowers() throws IOException{
        followOrFollowing="follower";
        Parent root = FXMLLoader.load(getClass().getResource("FollowersFollowings2.fxml"));
        Client.stage.setScene(new Scene(root));
        Client.stage.show();
    }
    @FXML
    void showFollowings()throws IOException{
        followOrFollowing="following";
        Parent root = FXMLLoader.load(getClass().getResource("FollowersFollowings2.fxml"));
        Client.stage.setScene(new Scene(root));
        Client.stage.show();
    }
}
/*
import java.util.*;
import javax.mail.*;
import javax.mail.internet.*;
import javax.activation.*;

public class SendEmail {

   public static void main(String [] args) {

      // Recipient's email ID needs to be mentioned.
      String to = "abcd@gmail.com";

      // Sender's email ID needs to be mentioned
      String from = "web@gmail.com";

      // Assuming you are sending email from localhost
      String host = "localhost";

      // Get system properties
      Properties properties = System.getProperties();

      // Setup mail server
      properties.setProperty("mail.smtp.host", host);

      // Get the default Session object.
      Session session = Session.getDefaultInstance(properties);

      try{
         // Create a default MimeMessage object.
         MimeMessage message = new MimeMessage(session);

         // Set From: header field of the header.
         message.setFrom(new InternetAddress(from));

         // Set To: header field of the header.
         message.addRecipient(Message.RecipientType.TO,
                                  new InternetAddress(to));

         // Set Subject: header field
         message.setSubject("This is the Subject Line!");

         // Now set the actual message
         message.setText("This is actual message");

         // Send message
         Transport.send(message);
         System.out.println("Sent message successfully....");
      }catch (MessagingException mex) {
         mex.printStackTrace();
      }
   }
}
 */