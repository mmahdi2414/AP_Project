import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
import javafx.scene.shape.StrokeType;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

import java.io.*;
import java.util.ArrayList;

/**
 * Created by M.Mahdi2414 on 21/06/2017 at 12:31 PM.
 */
public class HomeController {
    public ArrayList<Post> postsToShow;
    public ScrollPane postsPos;
    public ImageView like;
    public ScrollPane mainScroller;
    @FXML
    Pane paneOfPost;
    public boolean newNotif;
    double imageSize, paneSize1, paneSize2, hBoxSize1, hBoxSize2;
    ArrayList<Pane> posts;

    @FXML
    void initialize() {
        try {

            //debug
            Client.stageShowed.writeUTF("Home");
            Client.stageShowed.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        paneSize1 = 55;
        paneSize2 = 64;
        imageSize = 287;
        try {
            newNotif=Client.objectInputStream.readBoolean();
            if(newNotif)
            {
                like.setImage(new Image("like(notifes)2.jpg"));
            }
            else {
                like.setImage(new Image("like(notifs).jpg"));
            }
            postsToShow = (ArrayList<Post>) Client.objectInputStream.readObject();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }



        posts = new ArrayList<>();
        //for (int i = 0; i < postsToShow.size(); i++) {
        int i=0;

        for (Post p : postsToShow) {
            final Integer j = postsToShow.indexOf(p);
            int k = (p.getLikers().size());
            //final int l=k;
            Pane pane = new Pane();
            pane.setStyle("-fx-background-color: white;");
            ImageView imageView = createImageView(p.getTheFileOfPost());
            imageView.setFitWidth(imageSize);
            imageView.setFitHeight(imageSize);
            imageView.setLayoutX(-1);
            imageView.setLayoutY(55);
            VBox vbox = new VBox(30);
            vbox.setLayoutX(2);
            vbox.setLayoutY(344);
            HBox hBox = new HBox(60);
            hBox.prefHeight(32);
            hBox.prefWidth(488);
            Text caption = new Text();
//            caption.setLineSpacing(2);
            caption.setStrokeType(StrokeType.OUTSIDE);
            caption.setStrokeWidth(0);
            caption.setWrappingWidth(282.0000045001507);
            caption.setText(p.getCaption());
            caption.setFont(new Font("Comic Sans MS",14));
            if(p.getCaption().length()>0)
                vbox.getChildren().add(caption);
            HBox hBox1 = new HBox(20);
            hBox1.setLayoutX(-1);
            HBox hBox2 = new HBox(20);
            Button button = new Button("likes: ".concat(String.valueOf(k)));
            button.setMnemonicParsing(false);
            button.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent actionEvent) {
                    like(j);
                    boolean b = true;
                    try {
                        b = Client.objectInputStream.readBoolean();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    if (b) {
                        Button button1 = (Button) actionEvent.getSource();
                        int l = Integer.parseInt(button1.getText().split(" ", -1)[1]);
                        l++;
                        button1.setText("like: " + String.valueOf(l));
                    } else {
                        Button button1 = (Button) actionEvent.getSource();
                        int l = Integer.parseInt(button1.getText().split(" ", -1)[1]);
                        l--;
                        button1.setText("like: " + String.valueOf(l));
                    }

                }

            });
            //p=p1;
            k = p.getLikers().size();

            hBox2.getChildren().add(button);//to do badan imageView beshe

            Button button2 = new Button("cm");//to do mesle bala
            button2.setMnemonicParsing(false);
            button2.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent actionEvent) {

                    comment(j);
                }

            });
            if (!p.cmIsClose)
                hBox2.getChildren().add(button2);
            // Label label = new Label("id taraf")
            Button button3=new Button("Share");
            hBox2.getChildren().add(button3);
            button3.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent actionEvent) {
                    share(j);
                }
            });
            Label label = new Label();
            label.setAlignment(Pos.CENTER);
            label.setTextAlignment(TextAlignment.CENTER);
            label.setText(p.getDate());
            label.setFont(new Font("Comic Sans MS Bold" , 17));
//            hBox2.getChildren().add(button2);
            // Label label = new Label("id taraf");
            Label label1 = new Label(p.getPosterUser().getid());
            label1.setAlignment(Pos.CENTER);
            label1.setTextAlignment(TextAlignment.CENTER);
            label1.setFont(new Font("Comic Sans MS Bold" , 17));
            hBox.getChildren().add(hBox2);
            hBox.getChildren().add(label);
            vbox.getChildren().add(hBox);
            ImageView imageView1 = createImageView(p.getPosterUser().getProfileImageFile());
            imageView.setPreserveRatio(false);
            imageView1.setFitWidth(paneSize1);
            imageView1.setFitHeight(paneSize2);
            hBox1.getChildren().add(imageView1);
            hBox1.getChildren().add(label1);//to do user bgire ax o inashm add kone
            hBox1.setId(p.getPosterUser().getid());
            hBox1.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    showProfile(((HBox) (event.getSource())).getId());
                }
            });
            pane.getChildren().add(0, hBox1);
            pane.getChildren().add(1, imageView);
            pane.getChildren().add(2, vbox);

            // pane.getChildren()
            posts.add(pane);
            i++;
        }
        VBox vbox1 = new VBox(2);
        vbox1.getChildren().addAll(posts);
        postsPos.setContent(vbox1);
//        System.out.println(paneOfPost.getChildren().toString());
    }


    private ImageView createImageView(final File imageFile) {
        // DEFAULT_THUMBNAIL_WIDTH is a constant you need to define
        // The last two arguments are: preserveRatio, and use smooth (slower) resizing

        ImageView imageView = null;
        try {

            final Image image;

            image = new Image(new FileInputStream(imageFile), 1080, 1080, true, true);
            imageView = new ImageView(image);

        } catch (FileNotFoundException ex) {
            //    Logger.getLogger(GalleryController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return imageView;

    }

    @FXML
    void goToCamera() throws IOException {
        Client.objectOutputStream.writeUTF("goToCamera");
        Client.objectOutputStream.flush();
        //  Client.stage = camera1;
        Parent root = FXMLLoader.load(getClass().getResource("Camera.fxml"));
        Client.stage.setScene(new Scene(root));
        Client.stage.show();
    }
    @FXML
    void goToCamera2() throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("Camera2.fxml"));
        Client.stage.setScene(new Scene(root));
        Client.stage.show();
    }
    @FXML
    void goToDMs() throws IOException {
        Client.objectOutputStream.writeUTF("goToDMs");
        Client.objectOutputStream.flush();
        Parent root = FXMLLoader.load(getClass().getResource("DM.fxml"));
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
    void goToExplore() throws IOException {
        Client.objectOutputStream.writeUTF("goToExplore");
        Client.objectOutputStream.flush();
        Parent root = FXMLLoader.load(getClass().getResource("Explore.fxml"));
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
    @FXML
    void goToCommentPage() throws IOException {
//        Client.objectOutputStream.writeUTF("goToCommentPage");
//        Client.objectOutputStream.flush();
        Parent root = FXMLLoader.load(getClass().getResource("CommentPage.fxml"));
        Client.stage.setScene(new Scene(root));
        Client.stage.show();
    }

    public void comment(int j){
        try {
            //request
            Client.objectOutputStream.writeUTF("request to make a comment on a post");
            Client.objectOutputStream.flush();
            Client.objectOutputStream.reset();
            Client.objectOutputStream.write(j);
            Client.objectOutputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("ioe comment request");
        }
        try {
            goToCommentPage();
        } catch (IOException e) {
            System.out.println("ioe fxml loading comment page");
        }
    }
    public void share(int j){
        try {
            //request
            Client.objectOutputStream.writeUTF("request to share the post");
            Client.objectOutputStream.flush();
            //Client.objectOutputStream.reset();
            Client.objectOutputStream.writeInt(j);
            Client.objectOutputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("ioe share request");
        }
        try {
            goToCamera2();
        } catch (IOException e) {
            System.out.println("ioe fxml loading comment page");
        }
    }
    public void like(Integer j){
        try {
            Client.objectOutputStream.writeUTF("request to like a post");
            Client.objectOutputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("ioe like method homecontroller ");
        }
        try {
            System.out.println("j is"+ j);
            Client.objectOutputStream.reset();
            Client.objectOutputStream.writeObject(j);
            Client.objectOutputStream.flush();

        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("ioe write post to server in like method");
        }


    }
    @FXML
    void showProfile(String id) {
        try {

            Client.objectOutputStream.writeUTF("goToOthersProfile");
            Client.objectOutputStream.flush();
            Client.stageShowed.writeUTF("showProfile");
            Client.stageShowed.flush();
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
    Label makeLabel(String text) {
        Label label = new Label(text);
        label.setAlignment(Pos.CENTER);
        label.setContentDisplay(ContentDisplay.CENTER);
        label.setPrefHeight(43);
        label.setTextAlignment(TextAlignment.CENTER);
        label.setTextFill(Paint.valueOf("#002bff"));
        Font font = new Font("System Bold", 21);
        label.setFont(font);
        return label;

    }
    @FXML
    void reload(){
        try {

            Client.objectOutputStream.writeUTF("reload");
            Client.objectOutputStream.flush();
            Client.stageShowed.writeUTF("Home");
            Client.stageShowed.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        paneSize1 = 55;
        paneSize2 = 64;
        imageSize = 287;
        try {
            newNotif=Client.objectInputStream.readBoolean();
            if(newNotif)
            {
                like.setImage(new Image("like(notifes)2.jpg"));
            }
            else {
                like.setImage(new Image("like(notifs).jpg"));
            }
            postsToShow = (ArrayList<Post>) Client.objectInputStream.readObject();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }



        posts = new ArrayList<>();
        //for (int i = 0; i < postsToShow.size(); i++) {
        int i=0;

        for (Post p : postsToShow) {
            final Integer j = postsToShow.indexOf(p);
            int k = (p.getLikers().size());
            //final int l=k;
            Pane pane = new Pane();
            pane.setStyle("-fx-background-color: white;");
            ImageView imageView = createImageView(p.getTheFileOfPost());
            imageView.setFitWidth(imageSize);
            imageView.setFitHeight(imageSize);
            imageView.setLayoutX(-1);
            imageView.setLayoutY(55);
            VBox vbox = new VBox(30);
            vbox.setLayoutX(2);
            vbox.setLayoutY(344);
            HBox hBox = new HBox(60);
            hBox.prefHeight(32);
            hBox.prefWidth(488);
            Text caption = new Text();
//            caption.setLineSpacing(2);
            caption.setStrokeType(StrokeType.OUTSIDE);
            caption.setStrokeWidth(0);
            caption.setWrappingWidth(282.0000045001507);
            caption.setText(p.getCaption());
            caption.setFont(new Font("Comic Sans MS",14));
            if(p.getCaption().length()>0)
                vbox.getChildren().add(caption);
            HBox hBox1 = new HBox(20);
            hBox1.setLayoutX(-1);
            HBox hBox2 = new HBox(20);
            Button button = new Button("likes: ".concat(String.valueOf(k)));
            button.setMnemonicParsing(false);
            button.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent actionEvent) {
                    like(j);
                    boolean b = true;
                    try {
                        b = Client.objectInputStream.readBoolean();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    if (b) {
                        Button button1 = (Button) actionEvent.getSource();
                        int l = Integer.parseInt(button1.getText().split(" ", -1)[1]);
                        l++;
                        button1.setText("like: " + String.valueOf(l));
                    } else {
                        Button button1 = (Button) actionEvent.getSource();
                        int l = Integer.parseInt(button1.getText().split(" ", -1)[1]);
                        l--;
                        button1.setText("like: " + String.valueOf(l));
                    }

                }

            });
            //p=p1;
            k = p.getLikers().size();

            hBox2.getChildren().add(button);//to do badan imageView beshe

            Button button2 = new Button("cm");//to do mesle bala
            button2.setMnemonicParsing(false);
            button2.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent actionEvent) {

                    comment(j);
                }

            });
            if (!p.cmIsClose)
                hBox2.getChildren().add(button2);
            // Label label = new Label("id taraf");
            Label label = new Label();
            label.setAlignment(Pos.CENTER);
            label.setTextAlignment(TextAlignment.CENTER);
            label.setText(p.getDate());
            label.setFont(new Font("Comic Sans MS Bold" , 17));
//            hBox2.getChildren().add(button2);
            // Label label = new Label("id taraf");
            Label label1 = new Label(p.getPosterUser().getid());
            label1.setAlignment(Pos.CENTER);
            label1.setTextAlignment(TextAlignment.CENTER);
            label1.setFont(new Font("Comic Sans MS Bold" , 17));
            hBox.getChildren().add(hBox2);
            hBox.getChildren().add(label);
            vbox.getChildren().add(hBox);
            ImageView imageView1 = createImageView(p.getPosterUser().getProfileImageFile());
            imageView.setPreserveRatio(false);
            imageView1.setFitWidth(paneSize1);
            imageView1.setFitHeight(paneSize2);
            hBox1.getChildren().add(imageView1);
            hBox1.getChildren().add(label1);//to do user bgire ax o inashm add kone
            hBox1.setId(p.getPosterUser().getid());
            hBox1.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    showProfile(((HBox) (event.getSource())).getId());
                }
            });
            pane.getChildren().add(0, hBox1);
            pane.getChildren().add(1, imageView);
            pane.getChildren().add(2, vbox);

            // pane.getChildren()
            posts.add(pane);
            i++;
        }
        VBox vbox1 = new VBox(2);
        vbox1.getChildren().addAll(posts);
        postsPos.setContent(vbox1);
//        System.out.println(paneOfPost.getChildren().toString());
    }
}
