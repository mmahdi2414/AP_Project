import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.util.Duration;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;

/**
 * Created by M.Mahdi2414 on 23/06/2017 at 12:01 PM.
 */



public class ServerController {

    public ScrollPane vBox1;
    public ScrollPane vBox2;
    public ScrollPane vBox3;
    public ScrollPane vBox4;
    //  public Label allUsers;
    public Label onlineUsers;
    public Label userInfo;
    public Label offlineUsers;
    public Label allUsers;
    ArrayList<Label>[] labels;
    VBox[] vBox;
    boolean buttonClicked = true;
    Timeline timeline;
   // Timeline timeline2;
    @FXML
    void initialize() {
        vBox = new VBox[4];
        labels = new ArrayList[4];
        for (int i = 0; i < 4; i++) {
            if (i != 1) {
                labels[i] = new ArrayList<>();
                vBox[i] = new VBox(5);
            }

        }
        labels[0].add(allUsers);

            vBox[1] = new VBox(5);
            labels[1] = new ArrayList<>();

            vBox[1].getChildren().removeAll();
            vBox[1].getChildren().add(userInfo);
            vBox2.setContent(vBox[1]);

        vBox[2].getChildren().add(onlineUsers);
        vBox[3].getChildren().add(offlineUsers);

        for (User u : Server.users) {
            Label label1 = makeLabel(u.getid(), 18);
            Label label2 = makeLabel(u.getid(), 18);
            label1.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    createVboxInfo(((Label) (event.getSource())).getText());
                }
            });
            labels[0].add(label1);

            if (u.isLoggedIn()) {
                labels[2].add(label2);
            } else {

                labels[3].add(makeLabel(label2.getText() + " last seen " + u.getLastVisited().toString(), 18));
            }
        }
        for (int i = 0; i < 4; i++) {
            if (i != 1) {
                vBox[i].getChildren().addAll(labels[i]);

            }
        }
       // ArrayList<HBox> hBoxes = new ArrayList<>();
        vBox1.setContent(vBox[0]);
        // vBox2.setContent(vBox[1]);
        vBox3.setContent(vBox[2]);
        vBox4.setContent(vBox[3]);
     //   ReloadThread reloadThread = new ReloadThread();
     //   if (ServerApplication.stage!=null)
     //       Platform.runLater(reloadThread);
        timeline = new Timeline(
                new KeyFrame(Duration.seconds(2),
                        new EventHandler<ActionEvent>() {
                            @Override
                            public void handle(ActionEvent actionEvent) {
                                // Call update method for every 2 sec.
                                reload();
                            }
                        }));
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();

    }

    Label makeLabel(String text, int fontSize) {
        Label label = new Label(text);
        label.setAlignment(Pos.CENTER);
        label.setContentDisplay(ContentDisplay.CENTER);
        label.setTextAlignment(TextAlignment.CENTER);
        label.setTextFill(Paint.valueOf("#002bff"));
        Font font = new Font("System Bold", fontSize);
        label.setFont(font);
        return label;

    }

    @FXML
    void reload() {

        vBox = new VBox[4];
        labels = new ArrayList[4];
        for (int i = 0; i < 4; i++) {
            if (i != 1) {
                labels[i] = new ArrayList<>();
                vBox[i] = new VBox(5);
            }

        }
        labels[0].add(allUsers);
        if (buttonClicked) {
            vBox[1] = new VBox(5);
            labels[1] = new ArrayList<>();

            vBox[1].getChildren().removeAll();
            vBox[1].getChildren().add(userInfo);
            vBox2.setContent(vBox[1]);
        }
        vBox[2].getChildren().add(onlineUsers);
        vBox[3].getChildren().add(offlineUsers);

        for (User u : Server.users) {
            Label label1 = makeLabel(u.getid(), 18);
            Label label2 = makeLabel(u.getid(), 18);
            label1.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    createVboxInfo(((Label) (event.getSource())).getText());
                }
            });
            labels[0].add(label1);
            if (u.isLoggedIn()) {
                labels[2].add(label2);
            } else {

                labels[3].add(makeLabel(label2.getText() + " last seen " + u.getLastVisited().toString(), 18));
            }
        }
        for (int i = 0; i < 4; i++) {
            if (i != 1) {
                vBox[i].getChildren().addAll(labels[i]);

            }
        }
        // ArrayList<HBox> hBoxes = new ArrayList<>();
        vBox1.setContent(vBox[0]);
        //System.out.println(vBox1.getContent().toString());
        // vBox2.setContent(vBox[1]);
        vBox3.setContent(vBox[2]);
        vBox4.setContent(vBox[3]);
        //   ReloadThread reloadThread = new ReloadThread();
        //   if (ServerApplication.stage!=null)
        //       Platform.runLater(reloadThread);
        buttonClicked=false;
    }

    @FXML
    void makeButtonClickedBooleanTrue(){
        buttonClicked =true;
    }
    public void createVboxInfo(String id) {

        //
        // firstTime = false;
       // if (firstTime) {
            vBox[1] = new VBox(5);
            labels[1] = new ArrayList<>();

            vBox[1].getChildren().removeAll();
            vBox[1].getChildren().add(userInfo);
            vBox2.setContent(vBox[1]);
        //}
        User u = Server.findUser(id, Server.users);
        ImageView proPic = createImageView(u.getProfileImageFile());
        proPic.setFitWidth(50);
        proPic.setFitHeight(50);
        vBox[1] = new VBox(5);
        vBox[1].getChildren().add(userInfo);
        vBox[1].getChildren().add(proPic);
        vBox[1].getChildren().add(makeLabel("Fullname is: " + u.getFullName(), 14));
        vBox[1].getChildren().add(makeLabel("Number of followers: " + u.getFollowers().size(), 14));
        vBox[1].getChildren().add(makeLabel("Number of followings: " + u.getFollowings().size(), 14));
        vBox[1].getChildren().add(makeLabel("Number of posts: " + u.getPosts().size(), 14));
        vBox2.setContent(vBox[1]);

        // initialize();

    }

    private ImageView createImageView(final File imageFile) {
        // DEFAULT_THUMBNAIL_WIDTH is a constant you need to define
        // The last two arguments are: preserveRatio, and use smooth (slower) resizing

        ImageView imageView = null;
        try {
            final Image image;

            image = new Image(new FileInputStream(imageFile), 1080, 1080, true, true);
            imageView = new ImageView(image);

        } catch (FileNotFoundException ex) {
            //    Logger.getLogger(GalleryController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return imageView;

    }
}
