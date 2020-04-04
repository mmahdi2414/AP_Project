import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Asus on 6/19/2017.
 */
public class Comment implements Serializable{
    private String cm;
    private User commenter;
    //private Date timeCommented;
    private String time;
    private String date;
    private long timeSec;


    public Comment(String cm, User commenter){

        this.cm = cm;
        this.commenter = commenter;
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        SimpleDateFormat sdf1 = new SimpleDateFormat("YYYY:MM:dd");
        time=( sdf.format(cal.getTime()) );
        date=(sdf1.format(cal.getTime()));
        timeSec=new Date().getTime();
        //this.timeCommented = timeCommented;
    }

    public String getCm() {
        return cm;
    }

    public void setCm(String cm) {
        this.cm = cm;
    }

    public User getCommenter() {
        return commenter;
    }

    public void setCommenter(User commenter) {
        this.commenter = commenter;
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
//
//    public Date getTimeCommented() {
//        return timeCommented;
//    }
//
//    public void setTimeCommented(Date timeCommented) {
//        this.timeCommented = timeCommented;
//    }
}

