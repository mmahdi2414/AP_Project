import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.IOException;

/**
 * Created by M.Mahdi2414 on 24/06/2017 at 04:36 PM.
 */
public class ProfileSettingController {
    public ImageView PrivateAccount;
    public ImageView saveOriginalPhotos;
    public Label idLabel;
    User user;
    @FXML
    void initialize(){

        try {
            Client.stageShowed.writeUTF("ProfileSetting");
            Client.stageShowed.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            user = (User) Client.objectInputStream.readObject();
            idLabel.setText(user.getid());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        if (user.isPrivate())
            PrivateAccount.setImage(new Image("PrivateOn.jpg"));
        else PrivateAccount.setImage(new Image("PrivateOff.jpg"));
        if (user.isSaveOriginalPhoto())
            saveOriginalPhotos.setImage(new Image("SaveOriginalPhotoOn.jpg"));
        else saveOriginalPhotos.setImage(new Image("SaveOriginalPhotoOff.jpg"));
    }
    @FXML
    void goToProfile() throws IOException {
        Client.objectOutputStream.writeUTF("goToProfile");
        Client.objectOutputStream.flush();
        Client.stageShowed.writeUTF("wantSendUser");
        Client.stageShowed.flush();
        Parent root = FXMLLoader.load(getClass().getResource("Profile.fxml"));
        Client.stage.setScene(new Scene(root));
        Client.stage.show();
    }
    @FXML
    void changePassword(){
        try {
            Client.objectOutputStream.writeUTF("changePassword");
            Client.objectOutputStream.flush();
            Client.stageShowed.writeUTF("changePassword");
            Client.stageShowed.flush();
            Parent root = FXMLLoader.load(getClass().getResource("ChangePassword.fxml"));
            Client.stage.setScene(new Scene(root));
            Client.stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void goToEditProfile() throws IOException {
        Client.objectOutputStream.writeUTF("editProfile");
        Client.objectOutputStream.flush();
        Parent root = FXMLLoader.load(getClass().getResource("EditProfile.fxml"));
        Client.stage.setScene(new Scene(root));
        Client.stage.show();
    }
    @FXML
    void changePrivacy(){
        try {
            Client.objectOutputStream.writeUTF("changePrivacy");
            Client.objectOutputStream.flush();

            if(user.isPrivate())
                PrivateAccount.setImage(new Image("PrivateOff.jpg"));
            else
                PrivateAccount.setImage(new Image("PrivateOn.jpg"));
            user.setIsPrivate(!user.isPrivate());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void changeSavePhoto(){
        try {
            Client.objectOutputStream.writeUTF("changeSavePhoto");
            Client.objectOutputStream.flush();

            if (user.isSaveOriginalPhoto())
                saveOriginalPhotos.setImage(new Image("SaveOriginalPhotoOff.jpg"));
            else saveOriginalPhotos.setImage(new Image("SaveOriginalPhotoOn.jpg"));
            user.setSaveOriginalPhoto(!user.isSaveOriginalPhoto());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @FXML
    void addAccount(){
        try {
            Client.objectOutputStream.writeUTF("addAccount");
            Client.objectOutputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @FXML
    void logOut(){
        try {
            Client.objectOutputStream.writeUTF("logOut");
            Client.objectOutputStream.flush();
            Client.stageShowed.writeUTF("logOut");
            Client.stageShowed.flush();
            goToLogin();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @FXML
    void goToLogin() throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("login.fxml"));
        Client.stage.setScene(new Scene(root));
        Client.stage.show();
    }
}
