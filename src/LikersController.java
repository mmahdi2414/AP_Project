import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by M.Mahdi2414 on 26/06/2017 at 08:45 PM.
 */
public class LikersController {
    public ScrollPane scrollPane;
    Post post ;
    @FXML
    void initialize(){
        try {
            post = (Post) Client.objectInputStream.readObject();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        ArrayList<HBox> hBoxes = new ArrayList<>();
        for (User u : post.getLikers()){
            HBox hBox = new HBox(45);
            Label label1 = new Label(u.getid());
            label1.setAlignment(Pos.CENTER);
            label1.setTextAlignment(TextAlignment.CENTER);
            label1.setFont(new Font("Comic Sans MS Bold" , 17));
            ImageView imageView1 = createImageView(u.getProfileImageFile());
            imageView1.setPreserveRatio(false);
            imageView1.setFitWidth(55);
            imageView1.setFitHeight(64);
            hBox.getChildren().add(imageView1);
            hBox.getChildren().add(label1);
            hBoxes.add(hBox);
        }
        VBox vBox = new VBox(2);
        vBox.getChildren().addAll(hBoxes);
        scrollPane.setContent(vBox);

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
    void back(){
        try {
            String backToWhere = Client.objectInputStream.readUTF();
            if (backToWhere.equals("Profile")) {
                Client.stageShowed.writeUTF("wantSendUser");
                Client.stageShowed.flush();
                Parent root = FXMLLoader.load(getClass().getResource("Profile.fxml"));
                Client.stage.setScene(new Scene(root));
                Client.stage.show();
            }
            else if(backToWhere.equals("Home"))
            {
                Parent root = FXMLLoader.load(getClass().getResource("Home.fxml"));
                Client.stage.setScene(new Scene(root));
                Client.stage.show();
            }
            else if(backToWhere.equals("Post")){
                Client.stageShowed.writeUTF("Post");
                Client.stageShowed.flush();
                Parent root = FXMLLoader.load(getClass().getResource("post.fxml"));
                Client.stage.setScene(new Scene(root));
                Client.stage.show();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
