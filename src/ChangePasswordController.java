import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.image.ImageView;

import java.io.IOException;

/**
 * Created by M.Mahdi2414 on 25/06/2017 at 07:27 AM.
 */
public class ChangePasswordController {
    public PasswordField newPassword;
    public PasswordField newPasswordAgain;
    public PasswordField currentPassword;
    public ImageView image;
    public Label error;
    User user;
    boolean wantToRecover;

    @FXML
    void initialize() {
        try {
            wantToRecover = Client.objectInputStream.readBoolean();
            user = (User) Client.objectInputStream.readObject();
            if (wantToRecover) {
                currentPassword.setVisible(false);
                image.setVisible(false);
            } else {
                currentPassword.setVisible(true);
                image.setVisible(true);
            }
            error.setVisible(false);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void ok() {
        error.setVisible(false);
        String s1 = currentPassword.getText(), s2 = newPassword.getText(), s3 = newPasswordAgain.getText();
        if (s2.length()>=8) {
            if (wantToRecover) {
                if (s2.equals(s3)) {
                    try {
                        Client.objectOutputStream.writeUTF("ok");

                        Client.objectOutputStream.flush();
                        Client.objectOutputStream.writeUTF(s2);
                        Client.objectOutputStream.flush();
                        goToProfile();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    error.setText("Password do not match");
                    error.setLayoutX(10);
                    error.setVisible(true);
                }
            } else {
                System.out.println(user.getid());
                if (s1.equals(user.getPassword())) {
                    if (s2.equals(s3)) {
                        try {
                            Client.objectOutputStream.writeUTF("ok");
                            Client.objectOutputStream.flush();
                            Client.objectOutputStream.writeUTF(s2);
                            Client.objectOutputStream.flush();
                            goToSetting();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } else {
                        error.setText("Password do not match");
                        error.setLayoutX(10);
                        error.setVisible(true);

                    }

                } else {
                    error.setText("Old password entered incorrectly");
                    error.setLayoutX(10);
                    error.setVisible(true);
                }
            }
        }
            else
            {
                error.setText("Password must be more than 8 character");
                error.setLayoutX(10);
                error.setVisible(true);
            }

    }
    @FXML
    void cancel() {
        try {

            Client.objectOutputStream.writeUTF("cancel");
            Client.objectOutputStream.flush();
        } catch (IOException e){
            e.printStackTrace();
        }

        if (!wantToRecover){
            try {
                goToSetting();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else {
            try {
                goToLogin();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    @FXML
    void goToSetting() throws IOException {

        Parent root = FXMLLoader.load(getClass().getResource("ProfileSetting.fxml"));
        Client.stage.setScene(new Scene(root));
        Client.stage.show();
    }
    @FXML
    void goToLogin() throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("login.fxml"));
        Client.stage.setScene(new Scene(root));
        Client.stage.show();
    }
    @FXML
    void goToProfile() throws IOException {
        Client.stageShowed.writeUTF("wantSendUser");
        Client.stageShowed.flush();
        Parent root = FXMLLoader.load(getClass().getResource("Profile.fxml"));
        Client.stage.setScene(new Scene(root));
        Client.stage.show();
    }
}

