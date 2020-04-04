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
 * Created by Asus on 6/26/2017.
 */
public class DMController {
    public ScrollPane mainScroller;
    static String chattingWith;
    Boolean newNotif;
    static ArrayList<Chat> chats;
    VBox vbox=new VBox(1);
    @FXML
    public void initialize() {
        try {
            Client.stageShowed.writeUTF("DM");
            Client.stageShowed.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            chats = (ArrayList<Chat>) Client.objectInputStream.readObject();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        String s;
        if(chats!=null) {
            for (Chat c : chats) {
                if (c.getUser2().getid().equals(Client.ID)) {
                    s = c.getUser1().getid();
                    Pane p = makePane(c.getUser2().getProfileImageFile(), s);
                    final String s1 = s;
                    final Chat c1 = c;
                    p.setOnMouseClicked(new EventHandler<MouseEvent>() {
                        @Override
                        public void handle(MouseEvent mouseEvent) {
                            try {
                                showChat(c1, s1);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                    vbox.getChildren().add(p);
                } else {
                    s = c.getUser2().getid();
                    Pane p = makePane(c.getUser1().getProfileImageFile(), s);
                    final String s1 = s;
                    final Chat c1 = c;
                    p.setOnMouseClicked(new EventHandler<MouseEvent>() {
                        @Override
                        public void handle(MouseEvent mouseEvent) {
                            try {
                                showChat(c1, s1);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                    vbox.getChildren().add(p);
                }
            }
        }
        mainScroller.setContent(vbox);

    }


    @FXML
    public void showChat(Chat c, String chattingW) throws IOException {
//        try {
//            Client.objectOutputStream.writeUTF("showChat");
//            Client.objectOutputStream.flush();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        chattingWith=chattingW;
//        Client.stageShowed.writeUTF("chat");
//        Client.stageShowed.flush();
//        Client.objectOutputStream.writeUTF(chattingWith);
//        Client.stageShowed.flush();
        Parent root = FXMLLoader.load(getClass().getResource("Chat.fxml"));
        Client.stage.setScene(new Scene(root));
        Client.stage.show();
    }

    @FXML
    public Pane makePane(File f, String s) {
        Pane p = new Pane();
        p.prefHeight(70);
        p.prefWidth(287);
        ImageView image = createImageView(f);
        image.setFitHeight(70);
        image.setFitWidth(70);
        Label l = makeLabel(s);
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

        } catch (FileNotFoundException ex) {
            //    Logger.getLogger(GalleryController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return imageView;
    }
    @FXML
    void goToHome() throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("Home.fxml"));
        Client.stage.setScene(new Scene(root));
        Client.stage.show();
    }
}
