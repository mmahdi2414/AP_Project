import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.Scanner;

/**
 * Created by Asus on 6/18/2017.
 */
class exitThread extends Thread implements Runnable {
    public boolean socketOn = true;
    Server server;
    Scanner cin = new Scanner(System.in);

    public exitThread(Server server) {
        this.server = server;
    }

    @Override
    public void run() {
        String s="";
        while (socketOn) {
            if ((s = cin.nextLine()).equals("exit")) {
                socketOn = false;
                try {
                    server.serverSocket.close();
                } catch (IOException e) {
                    // e.printStackTrace();
                }
            }
            else if (s.equals("Show all user")){
                for (User u : Server.users)
                    System.out.println(u.getid()+ "'s email or phone numbe is "+u.getEmailOrPhone());
            }
        }

    }

}


class MyThread extends Thread implements Runnable {
    public String id, name;
    boolean iWantRecover = false;
    Socket socket = null;
    private User userOfThisThread;
    private ObjectInputStream stageShowed;
    private ObjectInputStream username;
    private ObjectInputStream password;
    private ObjectInputStream fullname;
    private ObjectInputStream emailOrPhone;
    private ObjectOutputStream objectOutputStream;
    private ObjectInputStream objectInputStream;
    private ObjectOutputStream Name, ID;
    private File clientFolder;
    private File followingTextFile, followerTextFile, bioTextFile, postsInfo, notificationsTextFile, ProfilePic;

    MyThread(Socket socket) throws FileNotFoundException {
        this.socket = socket;

    }

    @Override
    public void run() {
        String stage = "", usernameString = "", fullnameString = "", passwordString = "", emailOrPhoneString = "";
        try {
            stageShowed = new ObjectInputStream(socket.getInputStream());
        } catch (IOException e) {
            System.out.println("stage showed IO");
        }
        try {
            username = new ObjectInputStream(socket.getInputStream());
        } catch (IOException e) {
            System.out.println("username IO");
        }
        try {
            password = new ObjectInputStream(socket.getInputStream());
        } catch (IOException e) {
            System.out.println("password IO");
        }
        try {
            fullname = new ObjectInputStream(socket.getInputStream());
        } catch (IOException e) {
            System.out.println("fullname IO");
        }
        try {
            emailOrPhone = new ObjectInputStream(socket.getInputStream());
        } catch (IOException e) {
            System.out.println("email or phone IO");
        }
        try {
            objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            Name = new ObjectOutputStream(socket.getOutputStream());
            ID = new ObjectOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            System.out.println("object output stream IO");
        }
        try {
            objectInputStream = new ObjectInputStream(socket.getInputStream());
        } catch (IOException ioe) {
            System.out.println("ioe");
        }


        while (!stage.equals("disconnect me") && !socket.isClosed()) {
            while (!stage.equals("disconnect me") && !socket.isClosed()) {
                if (!socket.isClosed()) {
                    try {

                        stage = stageShowed.readUTF();
                        if (stage.equals("disconnect me") || socket.isClosed())
                            break;
                        usernameString = username.readUTF().toLowerCase();
                        passwordString = password.readUTF();
                        fullnameString = fullname.readUTF();
                        emailOrPhoneString = emailOrPhone.readUTF().toLowerCase();

                    } catch (EOFException eof) {
                        System.out.println("catched eof get info");
                        break;
                    } catch (IOException e) {
                        System.out.println("get info IO");
                    }
                    User loginUser = Server.findUser(usernameString, Server.users);
                    if (stage.equals("login")) {
                        try {

                            if (loginUser != null && loginUser.getPassword().equals(passwordString)) {
                                id = usernameString;
                                ID.writeUTF(id);
                                ID.flush();
                                name = loginUser.getFullName();
                                Name.writeUTF(name);
                                Name.flush();
                                objectOutputStream.writeUTF("login");
                                objectOutputStream.flush();
                                for (User u : Server.users) {
                                    if (u.getid().equals(id)) {
                                        u.setLoggedIn(true);
                                        break;
                                    }
                                }
                                break;
                            } else {
                                ID.writeUTF("");
                                ID.flush();
                                Name.writeUTF("");
                                Name.flush();
                                objectOutputStream.writeUTF("can't login");
                                objectOutputStream.flush();

                            }
                        } catch (IOException e) {
                            System.out.println("login IO");
                        }
                    } else if (stage.equals("signUp")) {
                        try {
                            System.out.println("in sign up");
                            id = usernameString;
                            name = fullnameString;
                            ID.writeUTF(id);
                            ID.flush();
                            Name.writeUTF(name);
                            Name.flush();
                            if (!Server.allUserNameA.contains(usernameString)) {
                                synchronized (Server.allUserNameA) {
                                    Server.allUserNameA.add(usernameString);
                                    Server.users.add(new User(usernameString, fullnameString, emailOrPhoneString, passwordString, true));
                                }

                                objectOutputStream.writeUTF("signUp");
                                objectOutputStream.flush();
                                break;

                            } else {
                                objectOutputStream.writeUTF("invalid username");
                                objectOutputStream.flush();
                            }

                        } catch (IOException e) {
                            System.out.println("sign up IO");
                        }
                    } else if (stage.equals("forget")) {
                        try {
                            System.out.println("in forget");

                            if (Server.allUserNameA.contains(usernameString) && Server.findUser(usernameString, Server.users).getEmailOrPhone().equals(emailOrPhoneString)) {
                                id = usernameString;
                                ID.writeUTF(id);
                                ID.flush();
                                name = Server.findUser(usernameString, Server.users).getFullName();
                                Name.writeUTF(name);
                                Name.flush();
                                objectOutputStream.writeUTF("recover");
                                objectOutputStream.flush();
                                iWantRecover = true;
                                stage = "changePassword";
                                break;
                            } else {
                                ID.writeUTF("");
                                ID.flush();
                                Name.writeUTF("");
                                Name.flush();
                                objectOutputStream.writeUTF("can't recover");
                                objectOutputStream.flush();
                            }
                        } catch (IOException e) {
                            System.out.println("forget IO");
                        }
                    }
                }
            }
            System.out.println(stage);
            if (stage.equals("disconnect me") || socket.isClosed())
                break;
            //send user of this thread to client for profileController(initialize() )
            userOfThisThread = Server.findUser(id, Server.users);
            try {
                if (!iWantRecover) {
                    System.out.println(userOfThisThread.getid());

                    objectOutputStream.reset();
                    objectOutputStream.writeObject(userOfThisThread);

                    objectOutputStream.flush();
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
            //enter profile
            try {
                clientFolder = new File(id);
                if (!clientFolder.isDirectory())
                    clientFolder.mkdir();
                String path = clientFolder.getPath() + File.separator;
                ProfilePic = new File(path + "ProfilePic.png");
                if (!ProfilePic.exists())
                    ProfilePic.createNewFile();
            } catch (IOException ioe) {
                System.out.println("ioe making info files for client");
            }


            while (!stage.equals("disconnect me") && !socket.isClosed()) {

                if (!socket.isClosed()) {
                    try {
                        if (!iWantRecover)
                            stage = stageShowed.readUTF();
                        System.out.println("Stage is " + stage);
                    } catch (EOFException eofe) {
                        System.out.println("eofe catched");
                        stage="disconnect me";
                        break;
                    } catch (IOException e) {
                        System.out.println("ioe reading stage from stageshowed in server");

                        //stage="disconnects me";
                        // e.printStackTrace();
                    }
                    if (stage.equals("disconnect me") || socket.isClosed()) {
                        for (User u : Server.users) {
                            if (u.getid().equals(id)) {
                                u.setLoggedIn(true);
                                break;
                            }
                        }
                        break;
                    } else if (stage.equals("logOut")) {
                        userOfThisThread.setLoggedIn(false);
                        userOfThisThread.setLastVisited();
                        break;
                    } else if (stage.equals("explore")) {
                        try {
                            String searchedUser = username.readUTF().toLowerCase();
                            ArrayList<String> temp = new ArrayList<>();
                            for (String s : Server.allUserNameA) {
                                if (s.startsWith(searchedUser)) {
                                    // sends a list of strings to explore controller
                                    //System.out.println("in the starts with if");
                                    temp.add(s);
                                }
                            }
                            objectOutputStream.reset();
                            objectOutputStream.writeObject(temp);
                            objectOutputStream.flush();
                            String whatNow = stageShowed.readUTF();
                            //reads whatnow if it's show profile reads an id then calls then checks if it's own profile or others and loads others profile or profile server finds the user and sends it back
                            if (whatNow.equals("showProfile")) {
                                String tempID = username.readUTF();

                                boolean newNotifs = false;
                                for (Activity a : userOfThisThread.getActivities()) {
                                    if (!a.getSeen())
                                        newNotifs = true;
                                }
                                try {
                                    objectOutputStream.writeBoolean(newNotifs);
                                    objectOutputStream.flush();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                User tempUser = Server.findUser(tempID, Server.users);
                                objectOutputStream.reset();
                                objectOutputStream.writeObject(tempUser);
                                objectOutputStream.flush();
                            }
                            System.out.println("got out of explore");
                            // shows searched user's profile if clicked
                            // to do :send profile picture to client

                        } catch (EOFException eof) {
                            System.out.println("catched eofe");
                            stage = "disconnect me";
                            break;
                        } catch (IOException e) {
                            System.out.println("ioe in explore stage get user");
                            e.printStackTrace();
                        }

                    } else if (stage.equals("othersProfile")) {
                        String howWant = "";
                        User user = null;
                        try {
                            howWant = username.readUTF();
                            user = (User) username.readObject();
                            Server.users.set(Server.users.indexOf(Server.findUser(user.getid(), Server.users)), user);
                            Server.users.set(Server.users.indexOf(Server.findUser(userOfThisThread.getid(), Server.users)), userOfThisThread);
                            String s = user.getid();
                            user = Server.findUser(s, Server.users);
                            userOfThisThread = Server.findUser(userOfThisThread.getid(), Server.users);
                        } catch (IOException e) {
                            e.printStackTrace();
                            System.out.println("follow ioe exception howwant and user writing");
                        } catch (ClassNotFoundException e) {
                            e.printStackTrace();
                        }
                        System.out.println(howWant);
                        Server.users.set(Server.users.indexOf(Server.findUser(user.getid(), Server.users)), user);
                        Server.users.set(Server.users.indexOf(Server.findUser(userOfThisThread.getid(), Server.users)), userOfThisThread);
                        String s = user.getid();
                        user = Server.findUser(s, Server.users);
                        userOfThisThread = Server.findUser(userOfThisThread.getid(), Server.users);
                        //     Server.users.set(Server.users.indexOf(Server.findUser(user.getid(),Server.users)),user);
                        //    Server.users.set(Server.users.indexOf(Server.findUser(userOfThisThread.getid(),Server.users)),userOfThisThread);
                        howWant.equals("Unfollow");
                        if (howWant.equals("Follow"))
                            user = userOfThisThread.follow(user);

                        else if (howWant.equals("Unfollow") || howWant.equals("Requested")) {
                            user = userOfThisThread.unFollow(user);
                        }
                        Server.users.set(Server.users.indexOf(Server.findUser(user.getid(), Server.users)), user);
                        System.out.println("follow/unfollow done");
                        if (howWant.equals("Follow") || howWant.equals("Unfollow") || howWant.equals("Requested")) {
                            try {
                                objectOutputStream.writeUTF("done following you can call change button,now we send the user");
                                objectOutputStream.flush();
                                //System.out.println(howWant);
                                Server.users.set(Server.users.indexOf(Server.findUser(user.getid(), Server.users)), user);
                                Server.users.set(Server.users.indexOf(Server.findUser(userOfThisThread.getid(), Server.users)), userOfThisThread);
                                s = user.getid();
                                user = Server.findUser(s, Server.users);
                                userOfThisThread = Server.findUser(userOfThisThread.getid(), Server.users);
                                for (User u : Server.users) {
                                    u.saveUser();
                                }
                                objectOutputStream.reset();
                                objectOutputStream.writeObject(user);
                                objectOutputStream.flush();
                            } catch (EOFException eof) {
                                System.out.println("catched eofe");
                                stage = "disconnect me";
                                break;
                            } catch (IOException e) {
                                e.printStackTrace();
                                System.out.println("ioe server writing green light for changing follow button");
                            }
                        }
                        // System.out.println("no run");
                        //System.out.println(user.getid());
                        Server.users.set(Server.users.indexOf(Server.findUser(user.getid(), Server.users)), user);
                        Server.users.set(Server.users.indexOf(Server.findUser(userOfThisThread.getid(), Server.users)), userOfThisThread);
                        s = user.getid();
                        user = Server.findUser(s, Server.users);
                        userOfThisThread = Server.findUser(userOfThisThread.getid(), Server.users);
                        //  System.out.println(userOfThisThread.getid());

                    } else if (stage.equals("posting")) {
                        try {
                            System.out.println("now in posting");
                            File p = (File) objectInputStream.readObject();
                            System.out.println("p created");
                           // objectOutputStream.reset();
                            objectOutputStream.writeBoolean(userOfThisThread.isSaveOriginalPhoto());
                            objectOutputStream.flush();
                            String caption = objectInputStream.readUTF();
                            Boolean cmIsClose = objectInputStream.readBoolean();
                            if (p!=null)
                                userOfThisThread.post(p,caption,"",cmIsClose);
                            System.out.println(p.getPath() + "posted");
                            System.out.println(userOfThisThread.getPosts().size());
                            Server.users.set(Server.users.indexOf(Server.findUser(userOfThisThread.getid(), Server.users)), userOfThisThread);
                            System.out.println("out of posting ");
                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (ClassNotFoundException e) {
                            e.printStackTrace();
                        } catch (NullPointerException e) {

                        }
}
                    else if (stage.equals("Home")) {
                        //debug
                        System.out.println("server else if home");
                        boolean newNotifs = false;
                        for (Activity a : userOfThisThread.getActivities()) {
                            if (!a.getSeen()) {
                                newNotifs = true;
                                break;
                            }
                        }
                        try {
                            objectOutputStream.reset();
                            objectOutputStream.writeBoolean(newNotifs);
                            objectOutputStream.flush();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        System.out.println("new notifs in home wroten and is" + newNotifs);

                        ArrayList<Post> postsToShow = new ArrayList<>();
                        ArrayList<User> followings = userOfThisThread.getFollowings();
                        for (User u : followings) {
                            u = Server.findUser(u.getid(), Server.users);
                            ArrayList<Post> postsOfThisUser = u.getPosts();
                            for (Post p : postsOfThisUser) {
                                postsToShow.add(p);
                            }

                        }
                        //sorts posts by time
                        Collections.sort(postsToShow);
                        System.out.println(postsToShow);
                        try {
                            objectOutputStream.reset();
                            objectOutputStream.writeObject(postsToShow);
                            objectOutputStream.flush();
                            System.out.println("posttoshow wroten");
                        }
                        catch (IOException e) {
                            // e.printStackTrace();
                            System.out.println("ioe writing posts arraylist");
                        }
                        System.out.println("feed showed");
                        //raft too feed
                        //shayad bekhad dokme like bezane
                        //shayadam explore o camera pas ye string migire hamishe
                        String s1;
                        try {
                            s1 = objectInputStream.readUTF();
                            while (!s1.equals("goToExplore") && !s1.equals("Posting")&& !s1.equals("goToCamera") && !s1.equals("goToDMs") && !s1.equals("goToHome") && !s1.equals("disconnect me") && !s1.equals("goToProfile") && !s1.equals("goToNotificationsLog") && !s1.equals("goToDMs") &&!s1.equals("reload")) {
                                if (s1.equals("goToOthersProfile")) {
                                    break;
                                }
                                String s2 = "";
                                if (s1.equals("request to like a post")) {
                                    Integer i = null;
                                    try {
                                        i = (Integer) objectInputStream.readObject();
                                    } catch (ClassNotFoundException e) {
                                        e.printStackTrace();
                                    }
                                    boolean isLiked;
                                    isLiked = postsToShow.get(i).like(userOfThisThread);
                                    objectOutputStream.writeBoolean(isLiked);
                                    objectOutputStream.flush();

                                    Post p = Server.findUser(postsToShow.get(i).getPosterUser().getid(), Server.users).searchPost(postsToShow.get(i));
                                    p = postsToShow.get(i);
                                    //comment called
                                    //recieves request
                                }
                                else if(s1.equals("request to share the post")){
                                    s2=("Posting");
                                    s1=("Posting");
                                    int i = objectInputStream.readInt();
                                    System.out.println(i);
                                    //fxml loads
                                    //in initializing fxml:sends the post
                                    objectOutputStream.reset();
                                    objectOutputStream.writeObject(postsToShow.get(i));
                                    objectOutputStream.flush();

                                }else if (s1.equals("request to make a comment on a post")) {

                                    //i recieves
                                    int i = objectInputStream.read();
                                    String s;
                                    //fxml loads
                                    //in initializing fxml:sends the post
                                    objectOutputStream.reset();
                                    objectOutputStream.writeObject(postsToShow.get(i));
                                    objectOutputStream.flush();
                                    //recieves the comment from the text box if send is clicked
                                    //recieves gotohome gotoexplore and gotocamera else
                                    s = objectInputStream.readUTF();

                                    while (!s.equals("goToCamera") && !s.equals("Posting")&& !s.equals("goToDMs") && !s.equals("goToExplore") && !s.equals("goToHome") && !s.equals("goToNotificationsLog") && !s.equals("disconnect me") && !s.equals("goToProfile")) {
                                        postsToShow.get(i).comment(userOfThisThread, s);
                                        Post p = Server.findUser(postsToShow.get(i).getPosterUser().getid(), Server.users).searchPost(postsToShow.get(i));
                                        p = postsToShow.get(i);
                                        s = objectInputStream.readUTF();
                                    }
                                    if (s.equals("goToCamera")) {
                                        s2 = "Posting";
                                        s1 = "goToCamera";
                                    } else if (s.equals("goToDMs")) {
                                        s2 = "DM";
                                        s1 = "goToDMs";

                                    } else if (s.equals("goToExplore")) {
                                        s2 = "Explore";
                                        s1 = "goToExplore";
                                    } else if (s.equals("goToHome")) {
                                        s2 = "Home";
                                        s1 = "goToHome";
                                    } else if (s.equals("goToProfile")) {
                                        s2 = "wantSendUser";
                                        s1 = "goToProfile";
                                    } else if (s.equals("goToNotificationsLog")) {
                                        s2 = "notificationsLog";
                                        s1 = "goToNotificationsLog";
                                    } else {
                                        s2 = "disconnect me";
                                        stage = "disconnect me";
                                        s1 = "disconnect me";
                                        break;
                                    }
                                }
                                if (!s2.equals("Posting") && !s2.equals("Explore") && !s2.equals("Home") && !s2.equals("disconnect me") && !s2.equals("wantSendUser") && !s2.equals("notificationsLog")) {
                                    System.out.println("omad");
                                    s1 = objectInputStream.readUTF();
                                }
                            }
                            System.out.println("got out of the loop");
                        } catch (IOException e) {
                            // e.printStackTrace();
                            System.out.println("catched eofe");
                            stage = "disconnect me";
                            break;
                        }

                    } else if (stage.equals("wantSendUser")) {
                        try {
                            userOfThisThread = Server.findUser(userOfThisThread.getid(), Server.users);
                            objectOutputStream.reset();
                            objectOutputStream.writeObject(userOfThisThread);
                            objectOutputStream.flush();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } else if (stage.equals("changeProfile")) {
                        try {

                            File proPic = (File) objectInputStream.readObject();
                            if (proPic.exists()) {
                                userOfThisThread.setProfileImageFile(proPic);
                                Server.users.set(Server.users.indexOf(Server.findUser(userOfThisThread.getid(), Server.users)), userOfThisThread);
                                objectOutputStream.reset();
                                objectOutputStream.writeObject(proPic);
                                objectOutputStream.flush();
                                objectOutputStream.reset();
                                objectOutputStream.writeObject(userOfThisThread);
                                objectOutputStream.flush();
                            }
                        } catch (IOException e) {
                            System.out.println("ioe get profile picture");
                        } catch (ClassNotFoundException e) {
                            System.out.println("CNFE get profile picture");
                        }
                    } else if (stage.equals("notificationsLog")) {
                        try {
                            userOfThisThread = Server.findUser(userOfThisThread.getid(), Server.users);
                            objectOutputStream.reset();
                            objectOutputStream.writeObject(userOfThisThread);
                            objectOutputStream.flush();
                            for (Activity a : userOfThisThread.getActivities()) {
                                if (!a.getSeen()) {
                                    a.setSeen(true);
                                }
                            }
                            Server.users.set(Server.users.indexOf(Server.findUser(userOfThisThread.getid(), Server.users)), userOfThisThread);
                            userOfThisThread = Server.findUser(userOfThisThread.getid(), Server.users);
                        } catch (IOException e) {
                            System.out.println("catched eofe");
                            stage = "disconnect me";
                            break;
                        }
                        System.out.println("out of notificationslog else if");
                    } else if (stage.equals("ProfileSetting")) {
                        try {
                            objectOutputStream.reset();
                            objectOutputStream.writeObject(userOfThisThread);
                            objectOutputStream.flush();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        String howWant = "";
                        while (!howWant.equals("goToProfile") && !howWant.equals("logOut") && !howWant.equals("changePassword")) {
                            try {
                                howWant = objectInputStream.readUTF();
                            } catch (IOException e) {
                                stage = "disconnect me";
                                break;
                                // e.printStackTrace();
                            }
                            if (howWant.equals("changePassword")) {
                                iWantRecover = false;
                            } else if (howWant.equals("changePrivacy")) {
                                userOfThisThread.setIsPrivate(!userOfThisThread.isPrivate());
                                Server.users.set(Server.users.indexOf(Server.findUser(userOfThisThread.getid(), Server.users)), userOfThisThread);
                                userOfThisThread = Server.findUser(userOfThisThread.getid(), Server.users);

                            } else if (howWant.equals("changeSavePhoto")) {
                                userOfThisThread.setSaveOriginalPhoto(!userOfThisThread.isSaveOriginalPhoto());

                            } else if (howWant.equals("editProfile")) {
                                try {
                                    objectOutputStream.reset();
                                    objectOutputStream.writeObject(userOfThisThread);
                                    objectOutputStream.flush();
                                    String action = objectInputStream.readUTF();
                                    if (action.equals("ok")) {
                                        String web = "", bio = "";
                                        fullnameString = objectInputStream.readUTF();
                                        usernameString = objectInputStream.readUTF();
                                        bio = objectInputStream.readUTF();
                                        web = objectInputStream.readUTF();
                                        emailOrPhoneString = objectInputStream.readUTF();
                                        boolean isInvalid = false;
                                        String invalid = "";
                                        if (Server.allUserNameA.contains(usernameString) && !userOfThisThread.getid().equals(usernameString)) {
                                            isInvalid = true;
                                            invalid.concat("invalid username");
                                        }
                                        try {
                                            long number = Long.parseLong(emailOrPhoneString);
                                            long size = Long.parseLong("9999999999");
                                            long size0 = Long.parseLong("999999999");
                                            if (number > size || number < size0) {
                                                if (isInvalid)
                                                    invalid.concat("and Email or Phone number");
                                                else
                                                    invalid.concat("invalid Email or Phone number");
                                            }
                                        } catch (Exception e) {
                                            String email = emailOrPhoneString;
                                            ArrayList<Character> characters = new ArrayList<>();
                                            for (int i = 0; i < email.length(); i++)
                                                characters.add(email.charAt(i));
                                            if (!(email.endsWith(".com")) || !characters.contains('@')) {
                                                if (isInvalid)
                                                    invalid.concat("and Email or Phone number");
                                                else {
                                                    invalid.concat("invalid Email or Phone number");
                                                    isInvalid = true;
                                                }
                                            }
                                        }
                                        if (isInvalid) {
                                            objectOutputStream.writeUTF(invalid);
                                            objectOutputStream.flush();
                                        } else {
                                            objectOutputStream.writeUTF("ok");
                                            objectOutputStream.flush();
                                            String lastId = userOfThisThread.getid();
                                            userOfThisThread.setId(usernameString);
                                            userOfThisThread.setFullName(fullnameString);
                                            userOfThisThread.setWebsite(web);
                                            userOfThisThread.setBio(bio);
                                            userOfThisThread.setEmailOrPhone(emailOrPhoneString);
                                            Server.allUserNameA.set(Server.allUserNameA.indexOf(lastId), userOfThisThread.getid());
                                            File file = new File(lastId);
                                            File file1 = new File(userOfThisThread.getid());
                                            file.renameTo(file1);
                                            file = new File(userOfThisThread.getid() + File.separator + lastId + ".ser");
                                            file1 = new File(userOfThisThread.getid() + File.separator + userOfThisThread.getid() + ".ser");
                                            file.renameTo(file1);
                                        }
                                    }

                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                try {
                                    System.out.println(userOfThisThread.getid());
                                    objectOutputStream.reset();
                                    objectOutputStream.writeObject(userOfThisThread);
                                    objectOutputStream.flush();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            } else if (howWant.equals("addAccount")) {
                                System.out.println("you can login by another account");
                            }
                        }

                    } else if (stage.equals("showProfile")) {
                        try {
                            String tempID = username.readUTF();
                            if (!tempID.equals("goToHome") && !tempID.equals("explore") && !tempID.equals("goToCamera") && !tempID.equals("goToDMs") && !tempID.equals("goToProfile") && !tempID.equals("goToNotificationsLog")) {
                                User tempUser = Server.findUser(tempID, Server.users);
                                System.out.println(tempID);
                                boolean newNotifs = false;
                                for (Activity a : userOfThisThread.getActivities()) {
                                    if (!a.getSeen())
                                        newNotifs = true;
                                }
                                objectOutputStream.writeBoolean(newNotifs);
                                objectOutputStream.flush();
                                objectOutputStream.reset();
                                objectOutputStream.writeObject(tempUser);
                                objectOutputStream.flush();

                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } else if (stage.equals("DM")) {
                        userOfThisThread = Server.findUser(userOfThisThread.getid(), Server.users);
                        System.out.println("dm");
                        ArrayList<Chat> chats = new ArrayList<>();
                        System.out.println(userOfThisThread.getChattingWith());
                        System.out.println(Server.chats);
                        if (userOfThisThread.getChattingWith() != null) {
                            for (String s : userOfThisThread.getChattingWith()) {
                                Chat c = Chat.findChat(s, userOfThisThread.getid(), Server.chats);
                                if (c == null) {
                                    System.out.println("oh shit");
                                }
                                chats.add(c);
                            }
                            try {
                                objectOutputStream.reset();
                                objectOutputStream.writeObject(chats);
                                objectOutputStream.flush();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        } else {
                            try {
                                objectOutputStream.reset();
                                objectOutputStream.writeObject(null);
                                objectOutputStream.flush();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
//                        String whatNow="";
//                        try {
//                            whatNow=objectInputStream.readUTF();
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                        }
//                        if(whatNow.equals("showChat")){
//
//                        }
                    } else if (stage.equals("chat")) {
                        String chattingWith = "";
                        try {
                            chattingWith = objectInputStream.readUTF();
                            System.out.println("chatting with recieved and is " + chattingWith);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        Chat chat = Chat.findChat(chattingWith, userOfThisThread.getid(), Server.chats);
                        try {
                            objectOutputStream.reset();
                            objectOutputStream.writeObject(chat);
                            objectOutputStream.flush();
                        } catch (IOException e) {
                            e.printStackTrace();

                        }
                        System.out.println("chat passed to the client");
                        try {
                            String whatNow = objectInputStream.readUTF();
                            System.out.println("what now" + whatNow);
                            while (!whatNow.equals("back")){
                                //should send a message
                                String sender, reciever, msg;
                                sender = objectInputStream.readUTF();
                                // System.out.println("sender recieved and is :"+sender);
                                reciever = objectInputStream.readUTF();
                                //System.out.println("reciever recieved and is :"+reciever);
                                msg = objectInputStream.readUTF();
                                // System.out.println("msg recieved and is :"+msg);
                                User senderUser = Server.findUser(sender, Server.users);
                                User recieverUser = Server.findUser(reciever, Server.users);
//                                System.out.println(senderUser.getid());
//                                System.out.println(recieverUser.getid());
                                chat = Chat.sendMessageTo(senderUser, msg, recieverUser, Server.chats);
                                //message sent,new chat should be sent to client
                                try {
                                    objectOutputStream.reset();
                                    objectOutputStream.writeObject(chat);
                                    objectOutputStream.flush();
                                    whatNow = objectInputStream.readUTF();
                                }
                                catch (SocketException e1) {
                                    System.out.println("socket e catched");
                                }catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        } catch (EOFException eofe) {
                            System.out.println("eofe catched");
                            stage = "disconnect me";
                            stage = "disconnect me";
                            break;
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } else if (stage.equals("changePassword")) {
                        String command = "";
                        try {
                            objectOutputStream.writeBoolean(iWantRecover);
                            objectOutputStream.flush();
                            objectOutputStream.reset();
                            objectOutputStream.writeObject(userOfThisThread);
                            objectOutputStream.flush();
                            command = objectInputStream.readUTF();
                            objectOutputStream.flush();
                        } catch (IOException e) {
                            e.printStackTrace();
                            stage = "disconnect me";
                            break;
                        }
                        if (!iWantRecover) {

                            if (command.equals("ok")) {
                                try {
                                    userOfThisThread.setPassword(objectInputStream.readUTF());
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        } else {
                            iWantRecover = false;
                            if (command.equals("ok")) {
                                try {
                                    userOfThisThread.setPassword(objectInputStream.readUTF());
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }

                            } else {
                                break;
                            }
                        }
                    }
                    else if (stage.equals("removePost"))
                    {
                        try {
                            Post p =(Post) objectInputStream.readObject();
                            Iterator<Post> iterator = userOfThisThread.getPosts().iterator();
                            for (int i = 0; i < userOfThisThread.getPosts().size(); i++) {
                                if(iterator.next().getTime().equals(p.getTime())){
                                    iterator.remove();
                                    break;
                                }
                            }
                            objectOutputStream.reset();
                            objectOutputStream.writeObject(userOfThisThread);
                            objectOutputStream.flush();
                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (ClassNotFoundException e) {
                            e.printStackTrace();
                        }
                    }
                    else if (stage.equals("PhotoInRowMyProfile")){
                        try {
                            Post p = null;
                            String s1;
                            try {
                                //debug
                                System.out.println("want to get s1");
                                s1 = objectInputStream.readUTF();
                                System.out.println("got it");
                                while (!s1.equals("backToPro")) {

                                    String s2="";
                                    if (s1.equals("request to like a post")) {
                                        try {
                                            p = (Post) objectInputStream.readObject();
                                        } catch (ClassNotFoundException e) {
                                            e.printStackTrace();
                                        }
                                        boolean isLiked =false;

                                        Iterator<Post> iterator = userOfThisThread.getPosts().iterator();
                                        for (int i = 0; i < userOfThisThread.getPosts().size(); i++) {
                                            Post p1;
                                            if ((p1 = iterator.next()).getTime().equals(p.getTime())){
                                                isLiked = p1.like(userOfThisThread);
                                                break;
                                            }
                                        }
                                        objectOutputStream.writeBoolean(isLiked);
                                        objectOutputStream.flush();

                                        //comment called
                                        //recieves request
                                    } else if (s1.equals("request to make a comment on a post")) {
                                        //i recieves
                                        p = (Post) objectInputStream.readObject();
                                        String s;
                                        //fxml loads
                                        //in initializing fxml:sends the post
                                        objectOutputStream.reset();
                                        objectOutputStream.writeObject(p);
                                        objectOutputStream.flush();
                                        //recieves the comment from the text box if send is clicked
                                        //recieves gotohome gotoexplore and gotocamera else
                                        s = objectInputStream.readUTF();

                                        while (!s.equals("backToPost")) {
                                            Iterator<Post> iterator = userOfThisThread.getPosts().iterator();
                                            for (int i = 0; i < userOfThisThread.getPosts().size(); i++) {
                                                Post p1;
                                                if ((p1 = iterator.next()).getTime().equals(p.getTime())){
                                                    p1.comment(userOfThisThread,s);
                                                    break;
                                                }
                                            }
                                            s = objectInputStream.readUTF();
                                        }
                                        if(s.equals("backToPost")){
                                            Iterator<Post> iterator = userOfThisThread.getPosts().iterator();
                                            for (int i = 0; i < userOfThisThread.getPosts().size(); i++) {
                                                Post p1;
                                                if ((p1 = iterator.next()).getTime().equals(p.getTime())){
                                                    objectOutputStream.reset();
                                                    objectOutputStream.writeObject(p1);
                                                    objectOutputStream.flush();
                                                    break;
                                                }
                                            }
                                        }

                                    }
                                    else if(s1.equals("goToLikers")){
                                        p = (Post) objectInputStream.readObject();
                                        Iterator<Post> iterator = userOfThisThread.getPosts().iterator();
                                        for (int i = 0; i < userOfThisThread.getPosts().size(); i++) {
                                            Post p1;
                                            if ((p1 = iterator.next()).getTime().equals(p.getTime())){
                                                objectOutputStream.reset();
                                                objectOutputStream.writeObject(p1);
                                                objectOutputStream.flush();
                                                break;
                                            }
                                        }
                                        objectOutputStream.writeUTF("Profile");
                                        objectOutputStream.flush();
                                        break;
                                    }
                                    s1 = objectInputStream.readUTF();

                                }
                                System.out.println("got out of the loop");
                            } catch (IOException e) {
                                // e.printStackTrace();
                                System.out.println("catched eofe");
                                stage = "disconnect me";
                                break;
                            }
                        }
                        catch (ClassNotFoundException e) {
                            e.printStackTrace();
                        }
                    }
                    else if (stage.equals("Post")){
                        try {
                            Post p = (Post) objectInputStream.readObject();
                            objectOutputStream.reset();
                            objectOutputStream.writeObject(p);
                            objectOutputStream.flush();
                            String s1;
                            try {
                                //debug
                                System.out.println("want to get s1");
                                s1 = objectInputStream.readUTF();
                                System.out.println("got it");
                                while (!s1.equals("backToPro")) {

                                    String s2="";
                                    if (s1.equals("request to like a post")) {
                                        try {
                                            p = (Post) objectInputStream.readObject();
                                        } catch (ClassNotFoundException e) {
                                            e.printStackTrace();
                                        }
                                        boolean isLiked =false;

                                        Iterator<Post> iterator = userOfThisThread.getPosts().iterator();
                                        for (int i = 0; i < userOfThisThread.getPosts().size(); i++) {
                                            Post p1;
                                            if ((p1 = iterator.next()).getTime().equals(p.getTime())){
                                                isLiked = p1.like(userOfThisThread);
                                                break;
                                            }
                                        }
                                        objectOutputStream.writeBoolean(isLiked);
                                        objectOutputStream.flush();

                                        //comment called
                                        //recieves request
                                    } else if (s1.equals("request to make a comment on a post")) {
                                        //i recieves
                                        p = (Post) objectInputStream.readObject();
                                        String s;
                                        //fxml loads
                                        //in initializing fxml:sends the post
                                        objectOutputStream.reset();
                                        objectOutputStream.writeObject(p);
                                        objectOutputStream.flush();
                                        //recieves the comment from the text box if send is clicked
                                        //recieves gotohome gotoexplore and gotocamera else
                                        s = objectInputStream.readUTF();

                                        while (!s.equals("backToPost")) {
                                            Iterator<Post> iterator = userOfThisThread.getPosts().iterator();
                                            for (int i = 0; i < userOfThisThread.getPosts().size(); i++) {
                                                Post p1;
                                                if ((p1 = iterator.next()).getTime().equals(p.getTime())){
                                                    p1.comment(userOfThisThread,s);
                                                    break;
                                                }
                                            }
                                            s = objectInputStream.readUTF();
                                        }
                                        if(s.equals("backToPost")){
                                            Iterator<Post> iterator = userOfThisThread.getPosts().iterator();
                                            for (int i = 0; i < userOfThisThread.getPosts().size(); i++) {
                                                Post p1;
                                                if ((p1 = iterator.next()).getTime().equals(p.getTime())){
                                                    objectOutputStream.reset();
                                                    objectOutputStream.writeObject(p1);
                                                    objectOutputStream.flush();
                                                    break;
                                                }
                                            }
                                        }

                                    }
                                    else if(s1.equals("goToLikers")){
                                        p = (Post) objectInputStream.readObject();
                                        Iterator<Post> iterator = userOfThisThread.getPosts().iterator();
                                        for (int i = 0; i < userOfThisThread.getPosts().size(); i++) {
                                            Post p1;
                                            if ((p1 = iterator.next()).getTime().equals(p.getTime())){
                                                objectOutputStream.reset();
                                                objectOutputStream.writeObject(p1);
                                                objectOutputStream.flush();
                                                break;
                                            }
                                        }
                                        objectOutputStream.writeUTF("Post");
                                        objectOutputStream.flush();
                                        Iterator<Post> iterator1 = userOfThisThread.getPosts().iterator();
                                        for (int i = 0; i < userOfThisThread.getPosts().size(); i++) {
                                            Post p1;
                                            if ((p1 = iterator1.next()).getTime().equals(p.getTime())){
                                                objectOutputStream.reset();
                                                objectOutputStream.writeObject(p1);
                                                objectOutputStream.flush();
                                                break;
                                            }
                                        }
                                    }
                                    s1 = objectInputStream.readUTF();

                                }
                                System.out.println("got out of the loop");
                            } catch (IOException e) {
                                // e.printStackTrace();
                                System.out.println("catched eofe");
                                stage = "disconnect me";
                                break;
                            }
                        }
                        catch (IOException e)
                        {
                            e.printStackTrace();
                        } catch (ClassNotFoundException e) {
                            e.printStackTrace();
                        }
                    }
                    if (stage.equals("disconnect me")) {
                        break;
                    }
                }
            }
        }
        if (userOfThisThread != null) {
            userOfThisThread.setLoggedIn(false);
            userOfThisThread.setLastVisited();
        }
    }
}


class Server extends Thread implements Runnable, Serializable {

    static ArrayList<String> allUserNameA;
    static ArrayList<User> users = new ArrayList<User>();
    static ArrayList<Chat> chats = new ArrayList<>();
    //static ArrayList<String > chatsFilesNames=new ArrayList<>();

    public ServerSocket serverSocket;
    exitThread checkServer;
    private int port;

    Server(int port) throws IOException {
        this.port = port;
        System.out.println("Port was set");
        serverSocket = new ServerSocket(this.port);
        System.out.println("the server socket connected on port " + this.port);
        System.out.println("waiting for client to connect to the port");
        checkServer = new exitThread(this);
        try {

            allUserNameA = new ArrayList<>();
            File file = new File("allUsername.ser");

            if (file.exists()) {
                FileInputStream fileIn = new FileInputStream(file);

                ObjectInputStream in = new ObjectInputStream(fileIn);

                try {
                    allUserNameA = (ArrayList<String>) in.readObject();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
                in.close();
                fileIn.close();
            }

            for (String id1 : allUserNameA) {
                try {
                    //File f1=new File("its_parmiss");
                    File f2 = new File(id1 + File.separator + id1 + ".ser");
                    // f2.mkdirs();
                    //f2.createNewFile();
                    //  if (!f2.exists())
                    //f2.createNewFile();
                    FileInputStream fileIn = new FileInputStream(f2);

                    ObjectInputStream in = new ObjectInputStream(fileIn);
                    User u = null;
                    u = (User) in.readObject();
                    users.add(u);
                    in.close();
                    fileIn.close();
                    System.out.println(u.getFullName());
                } catch (IOException i) {
                    i.printStackTrace();

                } catch (ClassNotFoundException c) {
                    c.printStackTrace();

                }
            }
            File chatsFile = new File("chats");
            if (!chatsFile.exists()) {
                chatsFile.mkdir();
            }
            File[] chatfiles = chatsFile.listFiles();
            for (File f2 : chatfiles) {
                try {
                    FileInputStream fileIn = new FileInputStream(f2);
                    ObjectInputStream in = new ObjectInputStream(fileIn);
                    Chat c = null;
                    c = (Chat) in.readObject();
                    chats.add(c);
                    in.close();
                    fileIn.close();
                    System.out.println(c.getUser1().getid() + c.getUser2().getid());
                } catch (IOException i) {
                    i.printStackTrace();

                } catch (ClassNotFoundException c) {
                    c.printStackTrace();

                }
            }
//            File file1 = new File("chatsFilesNames.ser");
//            if(file1.exists()) {
//                FileInputStream fileIn = new FileInputStream(file1);
//
//                ObjectInputStream in = new ObjectInputStream(fileIn);
//
//                try {
//                    chatsFilesNames = (ArrayList<String>) in.readObject();
//                } catch (ClassNotFoundException e) {
//                    e.printStackTrace();
//                }
//                in.close();
//                fileIn.close();
//            }

//            for (String s:chatsFilesNames) {
//                try {
//                    FileInputStream fileIn = new FileInputStream("chats"+File.separator+s);
//                    ObjectInputStream in = new ObjectInputStream(fileIn);
//                    Chat c = null;
//                    c = (Chat) in.readObject();
//                    chats.add(c);
//                    in.close();
//                    fileIn.close();
//                } catch (IOException i) {
//                    i.printStackTrace();
//
//                } catch (ClassNotFoundException c) {
//                    c.printStackTrace();
//
//                }
//            }
//    File f1=new File("s.s");
//    File[] fs=f1.listFiles();
// for (File f: fs ) {
//        try {

        } catch (FileNotFoundException e) {
        }
        checkServer.start();
    }
//
//            FileInputStream fileIn = new FileInputStream(f2);
//
//            ObjectInputStream in = new ObjectInputStream(fileIn);
//            User u = null;
//            u = (User) in.readObject();
//            users.add(u);
//            in.close();
//            fileIn.close();
//            System.out.println(u.getFullName());
//        } catch (IOException i) {
//            i.printStackTrace();
//
//        } catch (ClassNotFoundException c) {
//            c.printStackTrace();
//
//        }
//    }
//
//} catch (FileNotFoundException e) {
//        }
//        checkServer.start();
//        }

    public static User findUser(String usersName, ArrayList<User> users) {
        for (User u : users) {
            if (u.getid().equals(usersName)) {
                return u;
            }
        }
        return null;
    }


//    public static Chat findChat(String userName1, String username2,ArrayList<Chat> chats) {
//        for (Chat c : chats) {
//            if (c.getUser1().equals(userName1) && c.getUser2().equals(username2)) {
//                return c;
//            }
//            else if(c.getUser2().equals(userName1) && c.getUser1().equals(username2))
//            { return c;}
//        }
//        User u1=Server.findUser(userName1,Server.users);
//        User u2=Server.findUser(username2,Server.users);
//        Chat c=new Chat(u1,u2);
//        return c;
//    }
//
//    public static void main(String[] args) throws IOException {
//        try {
//            Server server = new Server(7612);
//            server.start();
//        } catch (Exception e) {
//            System.out.println("main for server exception");
//        }
//
//    }

    @Override
    public void run() {
        try {

            ArrayList<MyThread> allThreads = new ArrayList<>();
            while (checkServer.socketOn) {
                try {
                    Socket socket = serverSocket.accept();
                    System.out.println("a new client connected");
                    MyThread thread = new MyThread(socket);
                    allThreads.add(thread);
                    thread.start();
                } catch (SocketException e) {
                }
                // checkServer = new exitThread();
            }
            for (User u : users) {
                u.saveUser();
            }
            for (Chat c : chats) {
                c.saveChat();
            }
            try {
                FileOutputStream fileOut =
                        new FileOutputStream("allUsername.ser");
                ObjectOutputStream out = new ObjectOutputStream(fileOut);
                out.writeObject(allUserNameA);
                out.flush();
                out.close();
                fileOut.close();
                // System.out.printf("Serialized data is saved in /tmp/employee.ser");
            } catch (IOException i) {
                i.printStackTrace();
            }
//            try {
//                FileOutputStream fileOut =
//                        new FileOutputStream("chatsFilesNames.ser");
//                ObjectOutputStream out = new ObjectOutputStream(fileOut);
//                out.writeObject(chatsFilesNames);
//                System.out.println(chatsFilesNames);
//                out.flush();
//                out.close();
//                fileOut.close();
//            } catch (IOException i) {
//                i.printStackTrace();
//            }

            serverSocket.close();
            for (MyThread s : allThreads) {
                s.socket.close();
            }
            //get serialized class
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception exception) {
            System.out.println("exception server run not ioe");
            for (User u : users) {
                u.saveUser();
            }
            for (Chat c : chats) {
                c.saveChat();
            }

        }

    }


}