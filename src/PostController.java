import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class PostController {

    public ImageView profile;
    public Label ID;
    public MenuItem remove;
    public ImageView post;
    public Text caption;
    public Button like;
    public Label time;
    public Button cm;
    Post thisPost;
    public Label lks;

    @FXML
    void initialize() {

        try {

            thisPost = (Post) Client.objectInputStream.readObject();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        post.setImage(createImageView(thisPost.getTheFileOfPost()).getImage());

        caption.setText(thisPost.getCaption());
        caption.setFont(new Font("Comic Sans MS", 14));

        like.setText("likes: ");
        final String likers = String.valueOf(thisPost.getLikers().size());
        lks.setText(likers);
        lks.setAlignment(Pos.CENTER);
        lks.setTextAlignment(TextAlignment.CENTER);
        lks.setFont(new Font("Comic Sans MS Bold" , 14));
        lks.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                try {
                        Client.objectOutputStream.writeUTF("goToLikers");
                        Client.objectOutputStream.flush();
                        Client.objectOutputStream.reset();
                        Client.objectOutputStream.writeObject(thisPost);
                        Client.objectOutputStream.flush();
                        Parent root = FXMLLoader.load(getClass().getResource("Likers.fxml"));
                        Client.stage.setScene(new Scene(root));
                        Client.stage.show();

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
        });
        like.setId(likers);

        like.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                like();
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

        if (!thisPost.cmIsClose) {
            cm.setVisible(true);
            cm.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent actionEvent) {

                    comment();
                }

            });
        } else
            cm.setVisible(false);
        // Label label = new Label("id taraf");

        time.setText(thisPost.getDate());
        ID.setText(thisPost.getPosterUser().getid());


        profile.setImage(createImageView(thisPost.getPosterUser().getProfileImageFile()).getImage());
    }

    public void comment() {
        try {
            //request
            Client.objectOutputStream.writeUTF("request to make a comment on a post");
            Client.objectOutputStream.flush();
            Client.objectOutputStream.reset();
            Client.objectOutputStream.writeObject(thisPost);
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

    public void like() {
        try {
            Client.objectOutputStream.writeUTF("request to like a post");
            Client.objectOutputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("ioe like method homecontroller ");
        }
        try {
            System.out.println("j is");
            Client.objectOutputStream.reset();
            Client.objectOutputStream.writeObject(thisPost);
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

            image = new Image(new FileInputStream(imageFile), 1080, 1080, true, true);
            imageView = new ImageView(image);

        } catch (FileNotFoundException ex) {
            //    Logger.getLogger(GalleryController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return imageView;

    }

    @FXML
    void remove() {
        try {
            Client.objectOutputStream.writeUTF("backToPro");
            Client.objectOutputStream.flush();
            Client.stageShowed.writeUTF("removePost");
            Client.stageShowed.flush();
            Client.objectOutputStream.reset();
            Client.objectOutputStream.writeObject(thisPost);
            Client.objectOutputStream.flush();
            goToProfile();
        } catch (IOException e) {

        }
    }

    @FXML
    void goToProfile() throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("Profile.fxml"));
        Client.stage.setScene(new Scene(root));
        Client.stage.show();
    }

    @FXML
    void back() throws IOException {
        Client.objectOutputStream.writeUTF("backToPro");
        Client.objectOutputStream.flush();
        Client.stageShowed.writeUTF("wantSendUser");
        Client.stageShowed.flush();
        goToProfile();
    }
}

