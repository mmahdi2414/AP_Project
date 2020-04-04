import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.*;

/**
 * Created by M.Mahdi2414 on 17/06/2017 at 03:16 PM.
 */
public class CameraController {
    public Label path;
    public Button ok;
    public TextArea caption;
    public CheckBox cmIsClose;
    @FXML
    Pane MainAnchorPane;
    File postFile =null;
    boolean savePhoto;

    @FXML
    public void initialize() {
        try {
            Stage stage = new Stage();
            FileChooser fileChooser = new FileChooser();
            postFile = fileChooser.showOpenDialog(stage);

            try {
                Client.stageShowed.writeUTF("posting");
                Client.stageShowed.flush();
                Client.objectOutputStream.reset();
                Client.objectOutputStream.writeObject(postFile);
                Client.objectOutputStream.flush();
                savePhoto = Client.objectInputStream.readBoolean();
                if (postFile.exists() && postFile!=null)
                    path.setText(postFile.getPath());
                else
                    ok.setText("back to profile");
                if (savePhoto && postFile.exists() && postFile!=null){

                    File file = new File(Client.ID+"'s Original Photo");
                    if (!file.isDirectory())
                        file.mkdir();
                    FileInputStream fileInputStream = new FileInputStream(postFile);
                    FileOutputStream fileOutputStream= new FileOutputStream(file+File.separator+postFile.getName()+".jpg");
                    System.out.println("copying...");


                            int i;
                            while ((i = fileInputStream.read()) != -1)
                                fileOutputStream.write(i);
                            fileInputStream.close();
                            fileOutputStream.close();
                            System.out.println("copied completely");

                }


            } catch (IOException e) {
                e.printStackTrace();
            }



            System.out.println("initializing done");
            //MainAnchorPane.getChildren().add(new ParallelCamera());

        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    @FXML
    void goToProfile() throws IOException {
        Client.objectOutputStream.writeUTF(caption.getText());
        Client.objectOutputStream.flush();
        Client.objectOutputStream.writeBoolean(cmIsClose.isSelected());
        Client.objectOutputStream.flush();
        Client.stageShowed.writeUTF("wantSendUser");
        Client.stageShowed.flush();
        Parent root = FXMLLoader.load(getClass().getResource("Profile.fxml"));
        Scene scene = new Scene(root);
        Client.stage.setScene(scene);
        Client.stage.show();
    }
}
