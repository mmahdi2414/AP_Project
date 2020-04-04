import java.io.*;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by Asus on 6/23/2017.
 */
public class Chat implements Serializable{

    private ArrayList<Message> messages;
    private User user1;
    private User user2;
    public Chat(User user1,User user2){
        messages=new ArrayList<>();
        this.user1=user1;
        this.user2=user2;
       // Server.chats.add(this);
        //Server.chatsFilesNames.add(user1.getid()+user2.getid());

    }
    //message ro b in chat add mikone va notif mide
    public void sendMessage(String s,User sender){
        messages.add(new Message(s,sender));
        Collections.sort(messages);
        if(sender.getid().equals(user1.getid())){
            user2.addActivity(user1,Type.NewMessage,this);
        }
        if(sender.getid().equals(user2.getid())){
            user1.addActivity(user2,Type.Followed.NewMessage,this);
        }
    }
    public void saveChat() {

        try {
            FileOutputStream fileOut =
                    new FileOutputStream( "chats"+File.separator+user1.getid()+user2.getid()+".ser");
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(this);
            out.flush();
            out.close();
            fileOut.close();
            // System.out.printf("Serialized data is saved in /user1id.user2id.ser");
        } catch (IOException i) {
            i.printStackTrace();
        }
    }
    public static Chat startAChat(User u1,User u2) {
        Chat c = new Chat(u1, u2);
        u1.getChattingWith().add(u2.getid());
        u2.getChattingWith().add(u1.getid());
        return c;
    }
//message ro az tarafe user 1 b user 2 mifreste va un chat ro dar chat ha peyda mikone ya b vojud miare va msg ro add mikone o notif mide
    public static Chat sendMessageTo(User u1,String s,User u2,ArrayList<Chat> chats) {
        boolean doesChatExists = false;
        Chat c=null;
        for (String st : u1.getChattingWith()) {
            if (st.equals(u2.getid())) {
                c = findChat(u2.getid(),u1.getid(),chats);
                doesChatExists = true;
            }
        }
        System.out.println(doesChatExists);
        if(doesChatExists)
        {
            System.out.println(c.getMessages().size());
            c.sendMessage(s,u1);
        }
        else{
            c=startAChat(u1,u2);
            (c).sendMessage(s,u1);
           // System.out.println(c.getMessages());
//            chats.add(c);
            Server.chats.add(c);
            //chats.add(c);
            //System.out.println(Server.chats);
        }
        return c;
    }
    public static Chat findChat(String id,String id2,ArrayList<Chat> chats){
        System.out.println(chats);
        for(Chat c:chats){
            if(c.getUser2().getid().equals(id) && c.getUser1().getid().equals(id2)){
                return c;
            }
            if(c.getUser1().getid().equals(id)&& c.getUser2().getid().equals(id2)){
                return c;
            }
        }
        return null;
    }
    public ArrayList<Message> getMessages() {
        return messages;
    }

    public void setMessages(ArrayList<Message> messages) {
        this.messages = messages;
    }

    public User getUser1() {
        return user1;
    }

    public void setUser1(User user1) {
        this.user1 = user1;
    }

    public User getUser2() {
        return user2;
    }

    public void setUser2(User user2) {
        this.user2 = user2;
    }
}
