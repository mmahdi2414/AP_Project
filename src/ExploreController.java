import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by M.Mahdi2414 on 18/06/2017 at 03:08 PM.
 */

public class ExploreController implements Serializable{
    @FXML
    void showProfile(String id) {
        try {
            Client.stageShowed.writeUTF("showProfile");
            Client.stageShowed.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(!Client.ID.equals(id)) {
            try {
                Client.username.reset();
                Client.username.writeUTF(id);
                Client.username.flush();

            } catch (IOException e) {
                System.out.println("ioe write in show profile");
            }
            try {
                //System.out.println("get loader");
                Parent root = FXMLLoader.load(getClass().getResource("OthersProfile.fxml"));
                //System.out.println("loaeded");
                Client.stage.setScene(new Scene(root));
                Client.stage.show();

            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("fxml loader ioe");
            }
        }
        else{
            try {
                showThisProfile(id);

            }
            catch (IOException ioe){
                System.out.println("ioe go to pro from explore ioe exception");
            }
        }
    }
    @FXML
    TextField searchedUserNameTextField;
    @FXML
    ScrollPane MainPane;
    HBox[] hBox;
    int j;
    VBox vBox;
    @FXML
    void initialize (){
 j=1;
        vBox = new VBox(5);
    }
    @FXML
    void goToHome() throws IOException {
        Client.username.writeUTF("goToHome");
        Client.username.flush();
        Parent root = FXMLLoader.load(getClass().getResource("Home.fxml"));
        Client.stage.setScene(new Scene(root));
        Client.stage.show();
    }

    @FXML
    void showThisProfile(String id) throws IOException {
        Client.username.reset();
        Client.username.writeUTF(id);
        Client.username.flush();
        Client.objectInputStream.readBoolean();
        Parent root = FXMLLoader.load(getClass().getResource("Profile.fxml"));
        Scene scene = new Scene(root);
        Client.stage.setScene(scene);
        Client.stage.show();

    }
    @FXML
    void getText(){
        vBox=new VBox(5);
            try {
                Client.stageShowed.writeUTF("explore");
                Client.stageShowed.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        if(j>1){
            try {
                Client.stageShowed.writeUTF("explore");
                Client.stageShowed.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        j++;
            try {
                Client.username.reset();
                Client.username.writeUTF(searchedUserNameTextField.getText());
                Client.username.flush();
                ArrayList<String> a = (ArrayList<String>) Client.objectInputStream.readObject();
                if (a != null) {
                    hBox = new HBox[a.size()];
                    int i = 0;

                    for (String s : a) {
                        hBox[i] = new HBox();
                        hBox[i].getChildren().add(new Label(s));
                        hBox[i].setId(s);
                        hBox[i].setOnMouseClicked(new EventHandler<MouseEvent>() {
                            @Override
                            public void handle(MouseEvent event) {
                                //show profile
                                HBox h = (HBox) event.getSource();

                                showProfile(h.getId());
                            }
                        });
                        i++;
                        //adds to the list that shows profiles to the user
                    }
                    vBox.getChildren().addAll(hBox);

                    MainPane.setContent(vBox);
                }
                } catch(IOException e){
                    System.out.println("ioe explore controller get text");
                } catch(ClassNotFoundException e){
                    System.out.println("cnfe explore controller get text ");
                }



    }
    @FXML
    void goToCamera() throws IOException {
        Client.stageShowed.writeUTF("goToCamera");
        Client.stageShowed.flush();
        Client.username.writeUTF(Client.ID);
        Client.username.flush();
        //  Client.stage = camera1;
        Parent root = FXMLLoader.load(getClass().getResource("Camera.fxml"));
        Client.stage.setScene(new Scene(root));
        Client.stage.show();
    }
    @FXML
    void goToNotificationsLog() throws IOException{
        Client.objectOutputStream.writeUTF("goToNotificationsLog");
        Client.objectOutputStream.flush();
        try {
            Client.stageShowed.writeUTF("notificationsLog");
            Client.stageShowed.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Parent root = FXMLLoader.load(getClass().getResource("NotificationsLog.fxml"));
        Client.stage.setScene(new Scene(root));
        Client.stage.show();
    }

    @FXML
    void goToProfile() throws IOException {
        Client.stageShowed.writeUTF("goToProfile");
        Client.stageShowed.flush();
        Client.stageShowed.writeUTF("wantSendUser");
        Client.stageShowed.flush();
        Parent root = FXMLLoader.load(getClass().getResource("Profile.fxml"));
        Scene scene = new Scene(root);
        Client.stage.setScene(scene);
        Client.stage.show();
    }
}
