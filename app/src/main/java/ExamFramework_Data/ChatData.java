package ExamFramework_Data;

/**
 * Created by Prasanna on 28-08-2015.
 */
public class ChatData {

    private int id, RoomId;
    private String username, Email, ChatMessage, TimeStamp, RoomName;

    public ChatData() {
    }

    public ChatData(int id, int RoomId, String username, String Email, String ChatMessage, String TimeStamp) {
        super();
        this.id = id;
        this.RoomId = RoomId;
        this.username = username;
        this.Email = Email;
        this.ChatMessage = ChatMessage;
        this.TimeStamp = TimeStamp;
    }

    public ChatData(int id, int RoomId, String RoomName, String username, String Email, String ChatMessage, String TimeStamp) {
        super();
        this.id = id;
        this.RoomId = RoomId;
        this.username = username;
        this.Email = Email;
        this.ChatMessage = ChatMessage;
        this.TimeStamp = TimeStamp;
        this.RoomName = RoomName;
    }

    public int getid() {
        return id;
    }

    public void setid(int id) {
        this.id = id;
    }

    public int getRoomId() {
        return RoomId;
    }

    public void setRoomId(int RoomId) {
        this.RoomId = RoomId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String Email) {
        this.Email = Email;
    }

    public String getChatMessage() {
        return ChatMessage;
    }

    public void setChatMessage(String ChatMessage) {
        this.ChatMessage = ChatMessage;
    }

    public String getTimeStamp() {
        return TimeStamp;
    }

    public void setTimeStamp(String TimeStamp) {
        this.TimeStamp = TimeStamp;
    }

    public String getRoomName() {
        return RoomName;
    }

    public void setRoomName(String RoomName) {
        this.RoomName = RoomName;
    }

}
