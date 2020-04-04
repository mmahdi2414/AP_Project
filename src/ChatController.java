import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Collections;

/**
 * Created by Asus on 6/26/2017.
 */
public class ChatController {
    public ScrollPane mainScroller;
    public TextArea textArea;
    public Label label;
    Chat chat;
    String reciever;
    VBox vbox=new VBox(1);

    @FXML
    public void initialize() {
        label.setText(DMController.chattingWith);
        label.setTextFill(Paint.valueOf("#FFB6C1"));
        Font font = (javafx.scene.text.Font.font("Comic Sans MS",18));
        label.setFont(font);

        try {
            try {
                Client.stageShowed.writeUTF("chat");
                Client.stageShowed.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                Client.objectOutputStream.reset();
                Client.objectOutputStream.writeUTF(DMController.chattingWith);
                Client.objectOutputStream.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                chat = (Chat) Client.objectInputStream.readObject();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }

            if (chat != null) {
                for (Message m : chat.getMessages()) {
                    if (m.getSender().getid().equals(Client.ID)) {
                        Pane p = makePane(m.getSender().getProfileImageFile(), m.getMsg(),m.getSender().getid());
                        vbox.getChildren().add(p);
                        System.out.println(m.getSender().getid() + " said : " + m.getMsg());
                    } else {
                        Pane p = makePane2(m.getSender().getProfileImageFile(), m.getMsg(),m.getSender().getid());
                        vbox.getChildren().add(p);
                        System.out.println(m.getSender().getid() + " said : " + m.getMsg());
                    }
                }

            }
            mainScroller.setContent(vbox);
            // the owner's node of your scrollPane;
            mainScroller.layout();
// the maxValue for scrollPane bar ( 1.0 it's the default value )
            mainScroller.setVvalue( 1.0d );
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    @FXML
    public Pane makePane(File f, String s,String id){
        Pane p=new Pane();
        p.prefHeight(70);
        p.prefWidth(287);
        ImageView image=createImageView(f);
        image.setFitHeight(70);
        image.setFitWidth(70);
        TextArea t=makeTextArea(s);
        Label t1=makeLabel(id);
        t.setLayoutX(92);
        t.setLayoutY(17);
        t.prefHeight(37);
        t.prefWidth(124);
        p.getChildren().add(image);
       // p.getChildren().add(t1);
        p.getChildren().add(t);
        return p;
    }
    @FXML
    public Pane makePane2(File f, String s,String id){
        Pane p=new Pane();
        p.prefHeight(70);
        p.prefWidth(287);
        ImageView image=createImageView(f);
        image.setFitHeight(70);
        image.setFitWidth(70);
        TextArea t=makeTextArea2(s);
        Label t1=makeLabel(id);
//        t.setLayoutX(92);
//        t.setLayoutY(17);
//        t.prefHeight(37);
//        t.prefWidth(124);
        p.getChildren().add(image);
        //p.getChildren().add(t1);
        p.getChildren().add(t);
        return p;
    }
    TextArea makeTextArea(String text) {
        TextArea t=new TextArea(text);

        t.setStyle(
                "-fx-text-fill: #b7ffff ;"+
                        "-fx-background-color: black;"+
                        "-fx-font-family: " + javafx.scene.text.Font.font("Comic Sans MS",16)+ ";"
        );
       // t.setWrapText(true);
        t.setLayoutX(92);
        t.setLayoutY(17);
        t.setPrefRowCount(3);
        return t;
    }
    TextArea makeTextArea2(String text) {
        TextArea t=new TextArea(text);

        t.setStyle(
                "-fx-text-fill: #FFB6C1 ;"+
                        "-fx-background-color: black;"+
                        "-fx-font-family: " + javafx.scene.text.Font.font("Comic Sans MS",16)+ ";"
        );
        //t.setWrapText(true);
        t.setLayoutX(92);
        t.setLayoutY(17);
        t.setPrefRowCount(3);
        return t;
    }
    private ImageView createImageView(final File imageFile) {
        ImageView imageView = null;
        try {
            final Image image;

            image = new Image(new FileInputStream(imageFile),1080,1080, true, true);
            imageView = new ImageView(image);

        } catch (FileNotFoundException ex) {
            //    Logger.getLogger(GalleryController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return imageView;
    }
    @FXML
    public void sendMessage() {
        try {
            Client.objectOutputStream.writeUTF("wantToSendMessage");
            Client.objectOutputStream.flush();
            Client.objectOutputStream.writeUTF(Client.ID);
            Client.objectOutputStream.flush();
            Client.objectOutputStream.writeUTF(DMController.chattingWith);
            Client.objectOutputStream.flush();
            Client.objectOutputStream.writeUTF(textArea.getText());
            Client.objectOutputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            chat = (Chat) Client.objectInputStream.readObject();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        User u=(chat.getUser2().getid().equals(Client.ID))?chat.getUser2():chat.getUser1();
        Collections.sort(chat.getMessages());
        vbox.getChildren().clear();

            for (Message m : chat.getMessages()) {
                if (m.getSender().getid().equals(Client.ID)) {
                    Pane p = makePane(m.getSender().getProfileImageFile(), m.getMsg(),m.getSender().getid());
                    vbox.getChildren().add(p);
                    System.out.println(m.getSender().getid() + " said : " + m.getMsg());
                } else {
                    Pane p = makePane2(m.getSender().getProfileImageFile(), m.getMsg(),m.getSender().getid());
                    vbox.getChildren().add(p);
                    System.out.println(m.getSender().getid() + " said : " + m.getMsg());
                }
                // the owner's node of your scrollPane;
                mainScroller.layout();
// the maxValue for scrollPane bar ( 1.0 it's the default value )
                mainScroller.setVvalue( 1.0d );
            }

//       //vbox.getChildren().(makePane(u.getProfileImageFile(),textArea.getText(),Client.ID));
//        textArea.clear();
        mainScroller.onSwipeDownProperty();
        //chat updated;
        }
    @FXML
    public void back()throws IOException{
        System.out.println("called");
        Client.objectOutputStream.writeUTF("back");
        Client.objectOutputStream.flush();

        Parent root = FXMLLoader.load(getClass().getResource("DM.fxml"));
        Client.stage.setScene(new Scene(root));
        Client.stage.show();
    }
    Label makeLabel(String text) {
        Label label = new Label(text);
        label.setAlignment(Pos.BASELINE_LEFT);
        label.setContentDisplay(ContentDisplay.LEFT);
        //label.setPrefHeight(43);
        label.setTextAlignment(TextAlignment.LEFT);
        label.setTextFill(Paint.valueOf("#FFB6C1"));
        Font font = (javafx.scene.text.Font.font("Comic Sans MS",16));
        label.setFont(font);
        return label;

    }
}
