import java.io.Serializable;
import java.util.Date;

/**
 * Created by Asus on 6/23/2017.
 */
enum Type implements Serializable{
    Liked,
    Commented,
    Followed,
    Request,
    NewMessage,
    Unfollowed;

}

public class Activity  implements Serializable,Comparable<Activity>{
    private User notifier;
    private Boolean isSeen;
    private Type type;
    private Date date;
    private Post post=null;
    private Chat chat=null;
    private Comment cm=null;
    public Activity(User notifier,Type type){
        this.notifier=notifier;
        isSeen=false;
        this.type=type;
        date=new Date();
    }
    public User getNotifier() {
        return notifier;
    }

    public void setNotifier(User notifier) {
        this.notifier = notifier;
    }

    public Boolean getSeen() {
        return isSeen;
    }

    public void setSeen(Boolean seen) {
        isSeen = seen;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
    @Override
    public int compareTo(Activity a){
        if(a.getDate().getTime()<this.getDate().getTime()){
            return -1;
        }
        else if(a.getDate().getTime()>this.getDate().getTime()){
            return 1;
        }
        else {
            return 0;
        }
    }

    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }

    public Chat getChat() {
        return chat;
    }

    public void setChat(Chat chat) {
        this.chat = chat;
    }

    public Comment getCm() {
        return cm;
    }

    public void setCm(Comment cm) {
        this.cm = cm;
    }
}
