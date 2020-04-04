import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.io.IOException;

/**
 * Created by M.Mahdi2414 on 24/06/2017 at 06:16 PM.
 */
public class EditProfileController {
    public TextField Fullname;
    public TextField Username;
    public TextField Website;
    public TextArea bio;
    public TextField EmailOrPhoneNumber;
    User user;
    public Label error;

    @FXML
    void initialize(){
        error.setVisible(false);
        try {
            user = (User) Client.objectInputStream.readObject();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        try {
            Fullname.setText(user.getFullName());
            Username.setText(user.getid());
            Website.setText(user.getWebsite());
            bio.setText(user.getBio());
            EmailOrPhoneNumber.setText(user.getEmailOrPhone());
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
    @FXML
    void ok() throws IOException {
        Client.objectOutputStream.writeUTF("ok");
        Client.objectOutputStream.flush();
        Client.objectOutputStream.writeUTF(Fullname.getText());
        Client.objectOutputStream.flush();
        Client.objectOutputStream.writeUTF(Username.getText());
        Client.objectOutputStream.flush();
        Client.objectOutputStream.writeUTF(bio.getText());
        Client.objectOutputStream.flush();
        Client.objectOutputStream.writeUTF(Website.getText());
        Client.objectOutputStream.flush();
        Client.objectOutputStream.writeUTF(EmailOrPhoneNumber.getText());
        Client.objectOutputStream.flush();

        String s = "";
        if ((s =Client.objectInputStream.readUTF()).equals("ok")) {
            Client.ID = Username.getText();
            goToSetting();
        }
        else {
            error.setText(s);
            error.setVisible(true);
        }
    }
    @FXML
    void cancel() throws IOException {
        Client.objectOutputStream.writeUTF("cancel");
        Client.objectOutputStream.flush();
        goToSetting();
    }
    @FXML
    void goToSetting() throws IOException {

        Parent root = FXMLLoader.load(getClass().getResource("ProfileSetting.fxml"));
        Client.stage.setScene(new Scene(root));
        Client.stage.show();
    }

}
