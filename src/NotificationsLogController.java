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
import javafx.scene.layout.HBox;
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
 * Created by Asus on 6/24/2017.
 */
public class NotificationsLogController {
    public ScrollPane notifsPos;
    public VBox vbox = new VBox(2);
    public VBox vbox2 = new VBox(2);
    public VBox vboxMain = new VBox(100);
    public ImageView like;
    User user;
    ArrayList<Activity> activities = new ArrayList<>();

    @FXML
    public void initialize() {
        try {
            user = (User) Client.objectInputStream.readObject();
        } catch (IOException e) {
            //e.printStackTrace();
            System.out.println("oie read user notifs");
        } catch (ClassNotFoundException e) {
            //e.printStackTrace();
            System.out.println("cnfe read user notifs");
        }
        System.out.println(user.getid());
        activities = user.getActivities();
        for (Activity a : activities) {
            if (a.getType().equals(Type.Commented)) {
                if (a.getSeen()) {
                    HBox hbox = new HBox(2);
                    hbox.getChildren().add(createImageView(a.getNotifier().getProfileImageFile()));
                    Label l = makeLabel(a.getCm().getCommenter().getid() + " Commented on your post :" + a.getCm().getCm());
                    hbox.getChildren().add(l);
                    vbox.getChildren().add(hbox);
                } else {
                    HBox hbox = new HBox(2);
                    hbox.getChildren().add(createImageView(a.getNotifier().getProfileImageFile()));
                    Label l = makeLabel(a.getCm().getCommenter().getid() + " Commented on your post :" + a.getCm().getCm());
                    hbox.getChildren().add(l);
                    vbox2.getChildren().add(hbox);
                }
            } else if (a.getType().equals(Type.Liked)) {
                if (a.getSeen()) {
                    HBox hbox = new HBox(2);
                    hbox.getChildren().add(createImageView(a.getNotifier().getProfileImageFile()));
                    Label l = makeLabel(a.getNotifier().getid() + " Liked your post");
                    hbox.getChildren().add(l);
                    vbox.getChildren().add(hbox);
                } else {
                    HBox hbox = new HBox(2);
                    hbox.getChildren().add(createImageView(a.getNotifier().getProfileImageFile()));
                    Label l = makeLabel(a.getNotifier().getid() + " Liked your post");
                    hbox.getChildren().add(l);
                    vbox2.getChildren().add(hbox);
                }
            } else if (a.getType().equals(Type.Request)) {
                if (a.getSeen()) {
                    HBox hbox = new HBox(4);
                    hbox.getChildren().add(createImageView(a.getNotifier().getProfileImageFile()));
                    Label l = makeLabel(a.getNotifier().getid());
                    hbox.getChildren().add(l);
                    ImageView confirm;
                    ImageView delete;
                    hbox.getChildren().add(createImageView(new File("FFFFFF-0.png"),55,33));
                    hbox.getChildren().add(confirm=createImageView(new File("Confirm-Button.png"),55,33));
                    hbox.getChildren().add(delete=createImageView(new File("blue-delete-button-png-2.png"),55,36));
                    final Activity a1=a;
                    confirm.setOnMouseClicked(new EventHandler<MouseEvent>() {
                        @Override
                        public void handle(MouseEvent mouseEvent) {
                            confirmRequest(a1);
                        }
                    });
                    delete.setOnMouseClicked(new EventHandler<MouseEvent>() {
                        @Override
                        public void handle(MouseEvent mouseEvent) {
                            deleteRequest(a1);
                        }
                    });
                    vbox.getChildren().add(hbox);
                } else {
                    HBox hbox = new HBox(2);
                    hbox.getChildren().add(createImageView(a.getNotifier().getProfileImageFile()));
                    Label l = makeLabel(a.getNotifier().getid());
                    hbox.getChildren().add(l);
                    hbox.getChildren().add(createImageView(new File("Confirm-Button.png")));
                    hbox.getChildren().add(createImageView(new File("blue-delete-button-png-2.png")));
                    vbox2.getChildren().add(hbox);
                }
            } else if (a.getType().equals(Type.Followed)) {
                if (a.getSeen()) {
                    HBox hbox = new HBox(2);
                    hbox.getChildren().add(createImageView(a.getNotifier().getProfileImageFile()));
                    Label l = makeLabel(a.getNotifier().getid() + " Started following you");
                    hbox.getChildren().add(l);
                    vbox.getChildren().add(hbox);
                } else {
                    HBox hbox = new HBox(2);
                    hbox.getChildren().add(createImageView(a.getNotifier().getProfileImageFile()));
                    Label l = makeLabel(a.getNotifier().getid() + " Started following you");
                    hbox.getChildren().add(l);
                    vbox2.getChildren().add(hbox);
                }
            } else if (a.getType().equals(Type.Unfollowed)) {
                if (a.getSeen()) {
                    HBox hbox = new HBox(2);
                    hbox.getChildren().add(createImageView(a.getNotifier().getProfileImageFile()));
                    Label l = makeLabel(a.getNotifier().getid() + " Unfollowed you :| ");
                    hbox.getChildren().add(l);
                    vbox.getChildren().add(hbox);
                } else {
                    HBox hbox = new HBox(2);
                    hbox.getChildren().add(createImageView(a.getNotifier().getProfileImageFile()));
                    Label l = makeLabel(a.getNotifier().getid() + " Unfollowed you :| ");
                    hbox.getChildren().add(l);
                    vbox2.getChildren().add(hbox);
                }
            } else if (a.getType().equals(Type.NewMessage)) {
                HBox hbox = new HBox(2);
                hbox.getChildren().add(createImageView(a.getNotifier().getProfileImageFile()));
                Label l = makeLabel(a.getNotifier().getid() + " Sent you a direct message");
                hbox.getChildren().add(l);
                vbox2.getChildren().add(hbox);
            }

        }

        like.setImage(new Image("like(notifs).jpg"));
        if(!vbox2.getChildren().isEmpty())
        vboxMain.getChildren().add(vbox2);
        vboxMain.getChildren().add(vbox);
        notifsPos.setContent(vboxMain);
    }


//if (vbox2.getChildren().isEmpty())


    @FXML
    void reload() {
        vbox = new VBox(2);
        vbox2 = new VBox(2);
        vboxMain.getChildren().clear();
        try {
            Client.stageShowed.writeUTF("notificationsLog");
            Client.stageShowed.flush();
           // user = (User) Client.objectInputStream.readObject();
        } catch (IOException e) {
            //e.printStackTrace();
            System.out.println("oie read user notifs");
        }
        try {
            user = (User) Client.objectInputStream.readObject();
        } catch (IOException e) {
            //e.printStackTrace();
            System.out.println("oie read user notifs");
        } catch (ClassNotFoundException e) {
            //e.printStackTrace();
            System.out.println("cnfe read user notifs");
        }
        System.out.println(user.getid());
        activities = user.getActivities();
        for (Activity a : activities) {
            if (a.getType().equals(Type.Commented)) {
                if (a.getSeen()) {
                    HBox hbox = new HBox(2);
                    hbox.getChildren().add(createImageView(a.getNotifier().getProfileImageFile()));
                    Label l = makeLabel(a.getCm().getCommenter().getid() + " Commented on your post :" + a.getCm().getCm());
                    hbox.getChildren().add(l);
                    vbox.getChildren().add(hbox);
                } else {
                    HBox hbox = new HBox(2);
                    hbox.getChildren().add(createImageView(a.getNotifier().getProfileImageFile()));
                    Label l = makeLabel(a.getCm().getCommenter().getid() + " Commented on your post :" + a.getCm().getCm());
                    hbox.getChildren().add(l);
                    vbox2.getChildren().add(hbox);
                }
            } else if (a.getType().equals(Type.Liked)) {
                if (a.getSeen()) {
                    HBox hbox = new HBox(2);
                    hbox.getChildren().add(createImageView(a.getNotifier().getProfileImageFile()));
                    Label l = makeLabel(a.getNotifier().getid() + " Liked your post");
                    hbox.getChildren().add(l);
                    vbox.getChildren().add(hbox);
                } else {
                    HBox hbox = new HBox(2);
                    hbox.getChildren().add(createImageView(a.getNotifier().getProfileImageFile()));
                    Label l = makeLabel(a.getNotifier().getid() + " Liked your post");
                    hbox.getChildren().add(l);
                    vbox2.getChildren().add(hbox);
                }
            } else if (a.getType().equals(Type.Request)) {
                if (a.getSeen()) {
                    HBox hbox = new HBox(4);
                    hbox.getChildren().add(createImageView(a.getNotifier().getProfileImageFile()));
                    Label l = makeLabel(a.getNotifier().getid());
                    hbox.getChildren().add(l);
                    ImageView confirm;
                    ImageView delete;
                    hbox.getChildren().add(createImageView(new File("FFFFFF-0.png"),55,33));
                    hbox.getChildren().add(confirm=createImageView(new File("Confirm-Button.png"),55,33));
                    hbox.getChildren().add(delete=createImageView(new File("blue-delete-button-png-2.png"),55,36));
                    final Activity a1=a;
                    confirm.setOnMouseClicked(new EventHandler<MouseEvent>() {
                        @Override
                        public void handle(MouseEvent mouseEvent) {
                            confirmRequest(a1);
                        }
                    });
                    delete.setOnMouseClicked(new EventHandler<MouseEvent>() {
                        @Override
                        public void handle(MouseEvent mouseEvent) {
                            deleteRequest(a1);
                        }
                    });
                    vbox.getChildren().add(hbox);
                } else {
                    HBox hbox = new HBox(2);
                    hbox.getChildren().add(createImageView(a.getNotifier().getProfileImageFile()));
                    Label l = makeLabel(a.getNotifier().getid());
                    hbox.getChildren().add(l);
                    hbox.getChildren().add(createImageView(new File("Confirm-Button.png")));
                    hbox.getChildren().add(createImageView(new File("blue-delete-button-png-2.png")));
                    vbox2.getChildren().add(hbox);
                }
            } else if (a.getType().equals(Type.Followed)) {
                if (a.getSeen()) {
                    HBox hbox = new HBox(2);
                    hbox.getChildren().add(createImageView(a.getNotifier().getProfileImageFile()));
                    Label l = makeLabel(a.getNotifier().getid() + " Started following you");
                    hbox.getChildren().add(l);
                    vbox.getChildren().add(hbox);
                } else {
                    HBox hbox = new HBox(2);
                    hbox.getChildren().add(createImageView(a.getNotifier().getProfileImageFile()));
                    Label l = makeLabel(a.getNotifier().getid() + " Started following you");
                    hbox.getChildren().add(l);
                    vbox2.getChildren().add(hbox);
                }
            } else if (a.getType().equals(Type.Unfollowed)) {
                if (a.getSeen()) {
                    HBox hbox = new HBox(2);
                    hbox.getChildren().add(createImageView(a.getNotifier().getProfileImageFile()));
                    Label l = makeLabel(a.getNotifier().getid() + " Unfollowed you :| ");
                    hbox.getChildren().add(l);
                    vbox.getChildren().add(hbox);
                } else {
                    HBox hbox = new HBox(2);
                    hbox.getChildren().add(createImageView(a.getNotifier().getProfileImageFile()));
                    Label l = makeLabel(a.getNotifier().getid() + " Unfollowed you :| ");
                    hbox.getChildren().add(l);
                    vbox2.getChildren().add(hbox);
                }
            } else if (a.getType().equals(Type.NewMessage)) {
                HBox hbox = new HBox(2);
                hbox.getChildren().add(createImageView(a.getNotifier().getProfileImageFile()));
                Label l = makeLabel(a.getNotifier().getid() + " Sent you a direct message");
                hbox.getChildren().add(l);
                vbox2.getChildren().add(hbox);
            }

        }

        like.setImage(new Image("like(notifs).jpg"));
        if(!vbox2.getChildren().isEmpty())
            vboxMain.getChildren().add(vbox2);
        vboxMain.getChildren().add(vbox);
        notifsPos.setContent(vboxMain);
    }

    @FXML
    void goToHome() throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("Home.fxml"));
        Client.stage.setScene(new Scene(root));
        Client.stage.show();
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
    void goToCamera() throws IOException {

        //  Client.stage = camera1;
        Parent root = FXMLLoader.load(getClass().getResource("Camera.fxml"));
        Client.stage.setScene(new Scene(root));
        Client.stage.show();
    }

    @FXML
    void goToExplore() throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("Explore.fxml"));
        Client.stage.setScene(new Scene(root));
        Client.stage.show();
    }

    Label makeLabel(String text) {
        Label label = new Label(text);
        label.setAlignment(Pos.BASELINE_LEFT);
        label.setContentDisplay(ContentDisplay.LEFT);
        //label.setPrefHeight(43);
        label.setTextAlignment(TextAlignment.LEFT);
        label.setTextFill(Paint.valueOf("#009999"));
        Font font = (javafx.scene.text.Font.font("Comic Sans MS", 16));
        label.setFont(font);
        return label;

    }

    private ImageView createImageView(final File imageFile) {
        // DEFAULT_THUMBNAIL_WIDTH is a constant you need to define
        // The last two arguments are: preserveRatio, and use smooth (slower) resizing

        ImageView imageView = null;
        try {

            final Image image;

            image = new Image(new FileInputStream(imageFile), 1080, 1080, true, true);
            imageView = new ImageView(image);
            imageView.setFitWidth(50);
            imageView.setFitHeight(50);
        } catch (FileNotFoundException ex) {
            //    Logger.getLogger(GalleryController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return imageView;
    }
    public void confirmRequest(Activity a){
        Activity a1=new Activity(a.getNotifier(),Type.Followed);
        user.getActivities().set(user.getActivities().indexOf(a),a1);
    }
    public void deleteRequest(Activity a){
        user.removeActivity(a.getNotifier());
    }
    private ImageView createImageView(final File imageFile,int x,int y) {
        ImageView imageView = null;
        try {
            final Image image;
            image = new Image(new FileInputStream(imageFile), 1080, 1080, true, true);
            imageView = new ImageView(image);
            imageView.setFitWidth(x);
            imageView.setFitHeight(y);
            imageView.setPreserveRatio(false);
        } catch (FileNotFoundException ex) {
        }
        return imageView;
    }
}
