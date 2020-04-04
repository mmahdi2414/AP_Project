import java.io.File;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Asus on 6/18/2017.
 */
public class Post implements Serializable,Comparable<Post>
{
    //public String name;
    private File theFileOfPost;
    //public Date timePosted;
    //public String idOfPoster;
    private User posterUser;

    //public int likes;
    private ArrayList<User> likers=new ArrayList<User>();
    private ArrayList<Comment> comments=new ArrayList<Comment>();
    private String caption;
    private String location;
    //public File postInfo;
    private String time;
    private String date;
    private long timeSec;
    public boolean cmIsClose;
    public Post(File theFileOfPost, User posterUser, String caption, String location, Boolean cmIsClose){
        //this.idOfPoster=idofPoster;
        //this.timePosted=timePosted;

        this.posterUser=posterUser;
        this.cmIsClose = cmIsClose;
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        SimpleDateFormat sdf1 = new SimpleDateFormat("YYYY:MM:dd");
        date=( sdf.format(cal.getTime()) );
        time=(sdf1.format(cal.getTime()));
        timeSec=new Date().getTime();
        this.caption=caption;
        this.location=location;
        //this.likes=likes;
        //this.timePosted = new Date();
        //this.name=name;
        likers=new ArrayList<User>();
        comments=new ArrayList<Comment>();
        this.theFileOfPost=theFileOfPost;
    }
    public boolean like(User u){
        if(!likers.contains(u)){
            likers.add(u);
            System.out.println(u.getFullName()+"(user) liked this post");
            posterUser.addActivity(u,Type.Liked,this);
            return true;
        }
        else{
            likers.remove(u);
            posterUser.removeActivity(u);
            System.out.println(u.getFullName()+"(user) unliked this post");
            return false;
        }
    }

    public void comment(User u,String cm){
        Comment c=new Comment(cm,u);
        comments.add(c);
        posterUser.addActivity(u,Type.Commented,c);
        //should make changes to server files
        // File file = new File()
    }

    public File getTheFileOfPost() {
        return theFileOfPost;
    }

    public void setTheFileOfPost(File theFileOfPost) {
        this.theFileOfPost = theFileOfPost;
    }

    public User getPosterUser() {
        return posterUser;
    }

    public void setPosterUser(User posterUser) {
        this.posterUser = posterUser;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDate() {
        return date;
    }
    public void setDate(String date) {
        this.date = date;
    }

    public long getTimeSec() {
        return timeSec;
    }

    public void setTimeSec(long timeSec) {
        this.timeSec = timeSec;
    }
    public ArrayList<User>  getLikers(){
        return likers;
    }
    public void  setLikers(ArrayList<User>Likers){
        this.likers=likers;
    }

    public ArrayList<Comment> getComments() {
        return comments;
    }

    public void setComments(ArrayList<Comment> comments) {
        this.comments= comments;
    }
    @Override
    public int compareTo(Post p){
        if(p.timeSec >this.timeSec)
        {
            return 1;
        }
        else if(p.timeSec<this.timeSec){
            return -1;
        }
        else {
            return 0;
        }
    }

}



