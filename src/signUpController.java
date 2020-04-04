import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;

import java.io.IOException;
import java.util.ArrayList;


/**
 * Created by M.Mahdi2414 on 29/05/2017 at 09:58 PM at 12:04 AM.
 */
public class signUpController {

    @FXML
    TextField userNameField,fullNameField,emailOrPhoneNumberField;
    @FXML
    PasswordField passwordField;
    @FXML
    CheckBox rulesCheckBox;
    @FXML
    Label invalidUserName,invalidPassword,invalidEmailOrPhone;
    @FXML
    void goToLogin() throws IOException {
        setAllVisible(false);
        Parent root = FXMLLoader.load(getClass().getResource("login.fxml"));
        Client.stage.setScene(new Scene(root));
        Client.stage.show();
    }
    void setAllVisible (boolean visible){
        invalidEmailOrPhone.setVisible(visible);
        invalidPassword.setVisible(visible);
        invalidUserName.setVisible(visible);
    }
    @FXML
    void create() throws IOException {
        setAllVisible(false);
        if (passwordField.getText().length()<8)
            invalidPassword.setVisible(true);
        try {
            long number = Long.parseLong(emailOrPhoneNumberField.getText());
            long size = Long.parseLong("9999999999");
            long size0 = Long.parseLong("999999999");
            if (number>size||number<size0)
                invalidEmailOrPhone.setVisible(true);
        }
        catch (Exception e)
        {
            String email = emailOrPhoneNumberField.getText();
            ArrayList<Character> characters = new ArrayList<>();
            for (int i = 0; i < email.length(); i++)
                characters.add(email.charAt(i));
            if (!(email.endsWith(".com"))||!characters.contains('@'))
                invalidEmailOrPhone.setVisible(true);
        }
        if (userNameField.getText().split(" ",-1).length>1)
            invalidUserName.setVisible(true);
        if(rulesCheckBox.isSelected()&&!invalidUserName.isVisible()&&!invalidPassword.isVisible()&&!invalidEmailOrPhone.isVisible()) {
            Client.stageShowed.writeUTF("signUp");
            Client.stageShowed.flush();
            Client.username.writeUTF(userNameField.getText());
            Client.username.flush();
            Client.objectOutputStream.writeUTF(passwordField.getText());
            Client.objectOutputStream.flush();
            Client.objectOutputStream.writeUTF(fullNameField.getText());
            Client.objectOutputStream.flush();
            Client.objectOutputStream.writeUTF(emailOrPhoneNumberField.getText());
            Client.objectOutputStream.flush();
            Client.ID= Client.objectInputStream.readUTF();
            Client.name = Client.objectInputStream.readUTF();
            String what= Client.objectInputStream.readUTF();
            if(what.equals("invalid username"))
                invalidUserName.setVisible(true);
            else if (what.equals("signUp"))
            {

                goToProfile();
            }
        }
    }

    void goToProfile() throws IOException {
        setAllVisible(false);
        Parent root = FXMLLoader.load(getClass().getResource("Profile.fxml"));
        Client.stage.setScene(new Scene(root));
        Client.stage.show();
    }
}
