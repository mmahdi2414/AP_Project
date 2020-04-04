import java.io.Serializable;
import java.util.Date;

/**
 * Created by Asus on 6/23/2017.
 */
public class Message implements Serializable,Comparable<Message> {
    private String msg;
    private User sender;
    private Date time;
    public Message(String msg,User sender){
        this.msg=msg;
        this.sender=sender;
        time=new Date();
    }
    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public User getSender() {
        return sender;
    }

    public void setSender(User sender) {
        this.sender = sender;
    }

    public Date getTime() {
        return time;
    }
    @Override
    public int compareTo(Message m){
        if(this.getTime().getTime()<m.getTime().getTime())
            return -1;
        else if(this.getTime().getTime()>m.getTime().getTime())
            return 1;
        else
            return 0;
    }

    public void setTime(Date time) {
        this.time = time;
    }
}
