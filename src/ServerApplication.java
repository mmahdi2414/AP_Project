import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.*;
import javafx.stage.Stage;

import java.awt.*;
import java.awt.Image;
import java.io.IOException;



public class ServerApplication extends Application {
    static Stage stage;

    @Override
    public void start(Stage primaryStage) {

        try {
            Parent root = FXMLLoader.load(getClass().getResource("Server.fxml"));
            primaryStage.setTitle("Server :)");
            primaryStage.setScene(new Scene(root));
            primaryStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }




    public static void main(String[] args) {
        // write your code here
        try {
            Server server = new Server(7612);

            server.start();
            //  ReloadThread reloadThread = new ReloadThread();
            // reloadThread.start();
            launch(args);
            stage = null;
            for(User u:Server.users){
                u.saveUser();
            }
            for(Chat c:Server.chats){
                c.saveChat();
            }

        } catch (Exception e) {
            System.out.println("main for server exception");
        }

    }
}
