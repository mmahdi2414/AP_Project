import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.io.IOException;


/**
 * Created by M.Mahdi2414 on 30/05/2017 at 03:19 PM.
 */
public class ForgetPasswordController {

    @FXML
    TextField userNameField,emailOrPhoneField;
    @FXML
    Label error;
    @FXML
    void backToLogin() throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("login.fxml"));
        Client.stageShowed.writeUTF("login");
        Client.stageShowed.flush();
        Client.stage.setScene(new Scene(root));
        Client.stage.show();
    }
    @FXML
    void goToChangePassword() throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("ChangePassword.fxml"));
        Client.stage.setScene(new Scene(root));
        Client.stage.show();
    }
    @FXML
    void recover() throws IOException {
        error.setVisible(false);
        Client.stageShowed.writeUTF("forget");
        Client.stageShowed.flush();
        Client.username.writeUTF(userNameField.getText());
        Client.username.flush();
        Client.objectOutputStream.writeUTF("");
        Client.objectOutputStream.flush();
        Client.objectOutputStream.writeUTF("");
        Client.objectOutputStream.flush();
        Client.objectOutputStream.writeUTF(emailOrPhoneField.getText());
        Client.objectOutputStream.flush();
        Client.ID = Client.myID.readUTF();
        Client.name = Client.myName.readUTF();
        String s = "";
        System.out.println(s = Client.objectInputStream.readUTF());
        if (s.equals("recover"))
        {
            goToChangePassword();
        }
        else {
            error.setVisible(true);
        }
    }
    @FXML
    void goToSignUp() throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("SignUp.fxml"));
        Client.stage.setScene(new Scene(root));
        Client.stage.show();
    }
}
