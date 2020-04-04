import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Asus on 6/27/2017.
 */
public class Story implements Serializable,Comparable<Story> {
    private File theFileOfStory;
    private User posterUser;
    private Date date;
    private ArrayList<User> seenBy=new ArrayList<>();
    public Story(User posterUser,File theFileOfStory){
        this.theFileOfStory=theFileOfStory;
        this.posterUser=posterUser;
        date=new Date();
    }
    public boolean isStoryExpired(){
        Date d=new Date();
        if(86400000<d.getTime()-date.getTime() && d.getTime()-date.getTime()<86400000 ){
            return true;
        }
        else{
            return false;
        }
    }
    public void seeStory(User u){
        this.seenBy.add(u);
    }
    @Override
    public int compareTo(Story s){
        if(s.date.getTime()>this.date.getTime())
            return 1;
        else if(s.date.getTime()<this.date.getTime())
            return -1;
        else
            return 0;
    }

    public File getTheFileOfStory() {
        return theFileOfStory;
    }

    public void setTheFileOfStory(File theFileOfStory) {
        this.theFileOfStory = theFileOfStory;
    }

    public User getPosterUser() {
        return posterUser;
    }

    public void setPosterUser(User posterUser) {
        this.posterUser = posterUser;
    }

    public ArrayList<User> getSeenBy() {
        return seenBy;
    }

    public void setSeenBy(ArrayList<User> seenBy) {
        this.seenBy = seenBy;
    }
}

