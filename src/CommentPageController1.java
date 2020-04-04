import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
/**
 * Created by Asus on 6/23/2017.
 */
public class CommentPageController1 implements Serializable {
    double imageSize = 89.0;
    @FXML
    VBox vBox;
    //HBox hbox;
    String temp;
    public TextArea cmTextField;
    public ScrollPane commentsPane;
    public Button sendButton;
    Post thePost;
    ArrayList<ImageView> pics = new ArrayList<>();
    @FXML
    void initialize() {

        try {
            thePost = (Post) Client.objectInputStream.readObject();

        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        ArrayList<Comment> cms1=(thePost.getComments());
        ArrayList<Text> cms=new ArrayList<>();
        for(Comment c:cms1){
            Text t=new Text(c.getCm());

            t.setStyle(
                    "-fx-text-fill: LightSeaGreen ;"+
                            "-fx-background-color: WhiteSmoke;"+
                            "-fx-font-family: " + javafx.scene.text.Font.font("Comic Sans MS",16)+ ";"
                            );
            cms.add(t);
        }
            vBox=new VBox(cms.size());

            for(Text t:cms){
                HBox hbox=new HBox(2);
                Label t1= makeLabel(Client.ID+": ");
                //TextField t1=new TextField((Client.ID+" said:"));
                hbox.getChildren().add(t1);
                hbox.getChildren().add(t);

                vBox.getChildren().add(hbox);

            }
            commentsPane.setContent(vBox);
        }
    public void sendComment(){
        HBox hbox=new HBox(2);
        Text t=new Text(cmTextField.getText());

        t.setStyle(
                "-fx-text-fill: LightSeaGreen ;"+
                        "-fx-background-color: WhiteSmoke;"+
                        "-fx-font-family: " + javafx.scene.text.Font.font("Comic Sans MS",16)+ ";"
        );
        Label t1= makeLabel(Client.ID+": ");

        hbox.getChildren().add(t1);
        hbox.getChildren().add(t);
        vBox.getChildren().add(hbox);
        temp=cmTextField.getText();
        cmTextField.setText("");
        try {
            Client.objectOutputStream.writeUTF(temp);
            Client.objectOutputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void goToPost() throws IOException {
        System.out.println("here");
        Client.objectOutputStream.writeUTF("backToPost");
        Client.objectOutputStream.flush();
        //  Client.stage = camera1;
        Parent root = FXMLLoader.load(getClass().getResource("Post.fxml"));
        Client.stage.setScene(new Scene(root));
        Client.stage.show();
    }


    Label makeLabel(String text) {
        Label label = new Label(text);
        label.setAlignment(Pos.BASELINE_LEFT);
        label.setContentDisplay(ContentDisplay.LEFT);
        //label.setPrefHeight(43);
        label.setTextAlignment(TextAlignment.LEFT);
        label.setTextFill(Paint.valueOf("#009999"));
        Font font = (javafx.scene.text.Font.font("Comic Sans MS",16));
        label.setFont(font);
        return label;

    }
    }
