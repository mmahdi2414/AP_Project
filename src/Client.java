

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

public class Client extends Application {
    static Stage stage;
    private Socket socket;
    static ObjectOutputStream stageShowed;
    static ObjectOutputStream username;
    static ObjectOutputStream password;
    static ObjectOutputStream fullname;
    static ObjectOutputStream emailOrPhone;
    static ObjectInputStream objectInputStream;
    static ObjectInputStream myName,myID;
    static ObjectOutputStream objectOutputStream;
    static String name="",ID="";
    static File allPosts;
    static ArrayList<File> allPostsList;
    @FXML
    private void startAgain(){
        try {
            socket = new Socket("localhost", 7612);
            stageShowed = new ObjectOutputStream(socket.getOutputStream());
            username = new ObjectOutputStream(socket.getOutputStream());
            password = new ObjectOutputStream(socket.getOutputStream());
            fullname = new ObjectOutputStream(socket.getOutputStream());
            emailOrPhone = new ObjectOutputStream(socket.getOutputStream());
            objectInputStream = new ObjectInputStream(socket.getInputStream());
            myName = new ObjectInputStream(socket.getInputStream());
            myID = new ObjectInputStream(socket.getInputStream());
            objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            Parent root = FXMLLoader.load(getClass().getResource("login.fxml"));
            stage.setTitle("Minstagram ;)");
            stage.setScene(new Scene(root));
            stage.show();

        }
        catch (Exception e){
            Parent root = null;
            try {
                root = FXMLLoader.load(getClass().getResource("Retry.fxml"));
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            stage.setTitle("Minstagram ;)");
            stage.setScene(new Scene(root));
            stage.show();
        }
    }
    @Override
    public void start(Stage primaryStage) {
        allPostsList = new ArrayList<>();
        stage = primaryStage;
        try {
            socket = new Socket("localhost", 7612);
            stageShowed = new ObjectOutputStream(socket.getOutputStream());
            username = new ObjectOutputStream(socket.getOutputStream());
            password = new ObjectOutputStream(socket.getOutputStream());
            fullname = new ObjectOutputStream(socket.getOutputStream());
            emailOrPhone = new ObjectOutputStream(socket.getOutputStream());
            objectInputStream = new ObjectInputStream(socket.getInputStream());
            myName = new ObjectInputStream(socket.getInputStream());
            myID = new ObjectInputStream(socket.getInputStream());
            objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            Parent root = FXMLLoader.load(getClass().getResource("login.fxml"));
            stage.setTitle("Minstagram ;)");
            stage.setScene(new Scene(root));
            stage.show();
        }
        catch (Exception e){
            Parent root = null;
            try {
                root = FXMLLoader.load(getClass().getResource("Retry.fxml"));
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            stage.setTitle("Minstagram ;)");
            stage.setScene(new Scene(root));
            stage.show();
        }

    }

    public static void main(String[] args) {
        //RandomAccessFile randomAccessFile = new RandomAccessFile("")
        launch(args);
        try {
            stageShowed.writeUTF("disconnect me");
            stageShowed.flush();
            stageShowed.close();
            username.close();
            password.close();
            fullname.close();
            emailOrPhone.close();
            objectInputStream.close();
            myID.close();
            myName.close();
            objectInputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
