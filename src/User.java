import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;

/**
 * Created by Asus on 6/18/2017.
 */
public class User implements Serializable {
    private ArrayList<Activity> activities;
    private String id;
    private ArrayList<Story> stories=new ArrayList<>();
    private Date lastVisited;
    private String fullName;
    private String emailOrPhone;
    private String password;
    private ArrayList<Post> posts;
    private boolean loggedIn = false;
    public ArrayList<User> followers;
    public ArrayList<String> followersIds;
    public ArrayList<User> followings;
    private String bio="";
    private String website="";
    private boolean isPrivate = false;
    private File fileOfThisUserInServer;
    private File profileImageFile;
    private ArrayList<Chat> chats = new ArrayList<>();
    private ArrayList<String> chattingWith = new ArrayList<>();
    private boolean saveOriginalPhoto = true;
    public User(String id, String fullName, String emailOrPhone, String password) {
        this.id = id;
        this.emailOrPhone = emailOrPhone;
        this.fullName = fullName;
        this.password = password;
        this.followers = new ArrayList<>();
        this.followings = new ArrayList<>();
        this.posts = new ArrayList<>();
        this.followersIds = new ArrayList<>();
        this.profileImageFile = new File("defaultProfileImage.jpg");
        this.activities = new ArrayList<>();

    }

    public User(String id, String fullName, String emailOrPhone, String password, boolean loggedIn) {
        this(id, fullName, emailOrPhone, password);
        this.loggedIn = loggedIn;
        if (loggedIn) {
            lastVisited = new Date();
        }
    }

    public void setLoggedIn(boolean loggedIn) {
        this.loggedIn = loggedIn;
        Collections.sort(activities);
        if(activities.size()>30) {

            activities = (ArrayList<Activity>) activities.subList(0, 30);

        }
    }

    public String getid() {
        return id;
    }

    public ArrayList<User> getFollowers() {
        return followers;
    }

    public ArrayList<User> getFollowings() {
        return followings;
    }


    public ArrayList<Post> getPosts() {
        return posts;
    }


    public void setPosts(ArrayList<Post> posts) {
        this.posts = posts;
    }

    public String getFullName() {
        return fullName;
    }

    public User follow(User user) {
        System.out.println("follow called");
        if (!user.followersIds.contains(id) && !user.isPrivate) {
            boolean c=followings.add(user);
            boolean b = user.followers.add(this);
            boolean d=user.followersIds.add(this.id);
            if (b && d && c) {
                System.out.println(fullName + " followed " + user.fullName);
                user.addActivity(this, Type.Followed);
                System.out.println("now this users follow " + user.id + ": ");
                for (User u : user.followers) {
                    System.out.print(u.getid().toString() + " ");
                }
            }
        } else if (!user.followersIds.contains(id) && user.isPrivate) {
            System.out.println(fullName + " sends a request to " + user.fullName);
            user.addActivity(this, Type.Request);
        } else {
            System.out.println(fullName + "already follows" + user.fullName);
        }
        return user;


    }

    //this unfollows user
    public User unFollow(User user) {
        System.out.println("unfollow called");
        boolean f=false;
        for(String i:user.followersIds){
            if(this.id.equals(i)){
                f=true;
            }
        }
        if (f) {
            System.out.println("f true");
            boolean d=false;
            boolean b=false;
            boolean c=false;
            Iterator<String> iter = user.followersIds.iterator();

            while (iter.hasNext()) {
                String str = iter.next();
                if(this.id.equals(str))
                { iter.remove();
                d=true;}
            }
            Iterator<User> iter2 = user.followers.iterator();
            while (iter2.hasNext()) {
                User str = iter2.next();
                if(this.id.equals(str.getid()))
                { iter2.remove();
                    b=true;}
            }
            Iterator<User> iter3 = followings.iterator();

            while (iter3.hasNext()) {
                User str = iter3.next();
                if(str.getid().equals(user.getid()))
                { iter3.remove();
                    c=true;}
            }
            System.out.println(d);
            System.out.println(b);
            System.out.println(c);
            if (b && c && d) {
                System.out.println(fullName + "unfollowed" + user.fullName);
                user.addActivity(this, Type.Unfollowed); Collections.sort(user.activities);
                for(Activity a: user.activities) {
                    System.out.println(a.getType() + " " +a.getNotifier().getid());
                }
                System.out.println("now this users follows " + user.id + ": ");
                for (User u : user.followers) {
                    System.out.print(u.getid().toString() + " ");
                }
            }
        }
        else if(user.isPrivate() && !user.followersIds.contains(id)){
            boolean b=user.removeActivity(this);
            if(b){
                System.out.println("follow request notification removed from "+ user.getid() + "'s notifications" );
            }
        }
        return user;
    }

    public Post post(File f, String caption, String location, Boolean cmIsClose) {
        Post p = new Post(f, this, caption, location,cmIsClose);
        posts.add(p);
        return p;
    }
    public Story makeStory(File f){
        Story s=new Story(this,f);
        stories.add(s);
        return s;
    }
    public void saveUser() {
        try {
            FileOutputStream fileOut =
                    new FileOutputStream(id + File.separator + id + ".ser");
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(this);
            out.flush();
            out.close();
            fileOut.close();
            // System.out.printf("Serialized data is saved in /tmp/employee.ser");
        } catch (IOException i) {
            i.printStackTrace();
        }
    }

    public boolean isLoggedIn() {
        return loggedIn;
    }

    public Post searchPost(Post p) {
        String path = (p.getTheFileOfPost().getPath());
        for (Post p1 : posts) {
            if (p1.getTheFileOfPost().getPath().equals(path)) {
                return p1;
            }
        }
        return null;
    }

    public String getEmailOrPhone() {
        return emailOrPhone;
    }

    public void setEmailOrPhone(String emailOrPhone) {
        this.emailOrPhone = emailOrPhone;
    }

    public String getPassword() {
        return password;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setIsPrivate(boolean isPrivate) {
        this.isPrivate = isPrivate;
    }

    public boolean isPrivate() {
        return isPrivate;
    }

    public File getProfileImageFile() {
        return profileImageFile;
    }

    public void setProfileImageFile(File profileImageFile) {
        this.profileImageFile = profileImageFile;
    }

    public Date getLastVisited() {
        return lastVisited;
    }

    public void setLastVisited() {
        this.lastVisited = new Date();
    }

    public ArrayList<Activity> getActivities() {
        return activities;
    }

    public void addActivity(User notifier, Type type,Chat chat) {
        Activity a = new Activity(notifier, type);
        a.setChat(chat);
        activities.add(a);
        Collections.sort(activities);
    }
    public void addActivity(User notifier, Type type,Post post) {
        Activity a = new Activity(notifier, type);
        a.setPost(post);
        activities.add(a);
        Collections.sort(activities);
    }
    public void addActivity(User notifier, Type type,Comment cm) {
        Activity a = new Activity(notifier, type);
        a.setCm(cm);
        activities.add(a);
        Collections.sort(activities);
    }
    public void addActivity(User notifier, Type type) {
        Activity a = new Activity(notifier, type);
        activities.add(a);
        Collections.sort(activities);
    }

    public boolean removeActivity(User notifier){
        Iterator<Activity> itr =activities.iterator();
        while(itr.hasNext())
        {
            Activity a=itr.next();
            if(a.getType().equals(Type.Request) && notifier.getid().equals(a.getNotifier().getid())) {
                itr.remove();
                return true;
            }
            else if(a.getType().equals(Type.Liked) && notifier.getid().equals(a.getNotifier().getid()))
            {
                itr.remove();
                return true;
            }
            }
            return false;
        }


    public ArrayList<Chat> getChats() {
        return chats;
    }
    public void setChats(ArrayList<Chat> chats) {
        this.chats = chats;
    }
    public ArrayList<String> getChattingWith() {
        return chattingWith;
    }
    public void setChattingWith(ArrayList<String> chattingWith) {
        this.chattingWith = chattingWith;
    }
    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public boolean isSaveOriginalPhoto() {
        return saveOriginalPhoto;
    }

    public void setSaveOriginalPhoto(boolean saveOriginalPhoto) {
        this.saveOriginalPhoto = saveOriginalPhoto;
    }

    public void setStories(ArrayList<Story> stories) {
        this.stories = stories;
    }
}
