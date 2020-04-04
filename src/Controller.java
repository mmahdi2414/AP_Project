import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;


public class Controller{


    public Label cantLogin;

    @FXML
    void goToSignUpPage() throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("SignUp.fxml"));

        Client.stage.setScene(new Scene(root));
        Client.stage.show();
    }
    @FXML TextField userNameLoginTextField;
    @FXML PasswordField passwordLoginField;

    @FXML
    void goToHome() throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("Home.fxml"));
        Client.stage.setScene(new Scene(root));
        Client.stage.show();
    }
    @FXML
    void getUserName(){
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        SimpleDateFormat sdf1 = new SimpleDateFormat("YYYY:MM:dd");
        System.out.println( sdf.format(cal.getTime()) );
        System.out.println(sdf1.format(cal.getTime()));
        cantLogin.setVisible(false);
        try {
            Client.stageShowed.writeUTF("login");
            Client.stageShowed.flush();
            Client.username.writeUTF(userNameLoginTextField.getText());
            Client.username.flush();
            Client.password.writeUTF(passwordLoginField.getText());
            Client.password.flush();
            Client.fullname.writeUTF("");
            Client.fullname.flush();
            Client.emailOrPhone.writeUTF("");
            Client.emailOrPhone.flush();
            Client.ID = Client.myID.readUTF();
            Client.name = Client.myName.readUTF();
            String what = Client.objectInputStream.readUTF();
            if (what.equals("login")) {

                System.out.println(what);
                System.out.println(Client.ID);

                goToProfile();
            }
            else if(what.equals("can't login"))
                cantLogin.setVisible(true);
        } catch (IOException e) {
        }
    }
    @FXML
    void goToProfile() throws IOException {

        Parent root = FXMLLoader.load(getClass().getResource("Profile.fxml"));
        Scene scene = new Scene(root);
        Client.stage.setScene(scene);
        Client.stage.show();
//        Client.stageShowed.writeUTF("Profile");
//        Client.stageShowed.flush();
    }
    @FXML
    void goToForgetPasswordPage() throws IOException {

        Parent root = FXMLLoader.load(getClass().getResource("forgetPassword.fxml"));
        Client.stage.setScene(new Scene(root));
        Client.stage.show();
    }
    @FXML
    void goToCamera() throws IOException {

        //  Client.stage = camera1;
        Parent root = FXMLLoader.load(getClass().getResource("Camera.fxml"));
        Client.stage.setScene(new Scene(root));
        Client.stage.show();
    }


}
