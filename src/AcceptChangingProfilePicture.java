import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * Created by M.Mahdi2414 on 21/06/2017 at 09:25 PM.
 */
public class AcceptChangingProfilePicture {
    public Button changeThis;
    public ImageView profileImage;
    File file;
    @FXML
    void initialize(){
        try {
            file = (File)Client.objectInputStream.readObject();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        if (file.exists())
            profileImage.setImage(createImageView(file).getImage());

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
    @FXML
    public void goToProfile() throws IOException {
     //   System.out.println(Client.objectInputStream.readUTF());
        Parent root = FXMLLoader.load(getClass().getResource("Profile.fxml"));
        Client.stage.setScene(new Scene(root));
        Client.stage.show();
    }
}
