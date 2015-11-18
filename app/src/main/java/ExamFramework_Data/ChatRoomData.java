package ExamFramework_Data;

/**
 * Created by Prasanna on 28-08-2015.
 */
public class ChatRoomData {

    private  int id,ChatCount,NotificationEnabled,FavEnabled;
    private String RoomName,CreatedBy,Description;

    public ChatRoomData() {
    }

    public ChatRoomData(int id, int ChatCount, int NotificationEnabled, String RoomName, String CreatedBy,int FavEnabled,String Description)
        {
        super();
        this.id=id;
        this.ChatCount=ChatCount;
        this.NotificationEnabled=NotificationEnabled;
        this.RoomName=RoomName;
        this.CreatedBy=CreatedBy;
        this.FavEnabled=FavEnabled;
        this.Description=Description;

    }

    public int getid() {return id;}

    public void setid(int id) {this.id = id;}

    public int getChatCount() {return ChatCount;}

    public void setChatCount(int ChatCount) {this.ChatCount = ChatCount;}

    public int getNotificationEnabled() {return NotificationEnabled;}

    public void setNotificationEnabled(int NotificationEnabled) {this.NotificationEnabled = NotificationEnabled;}

    public int getFavEnabled() {return FavEnabled;}

    public void setFavEnabled(int FavEnabled) {this.FavEnabled = FavEnabled;}

    public String getRoomName() {return RoomName;}

    public void setRoomName(String RoomName) {this.RoomName = RoomName;}

    public String getCreatedBy() {return CreatedBy;}

    public void setCreatedBy(String CreatedBy) {this.CreatedBy = CreatedBy;}

    public String getDescription() {return Description;}

    public void setDescription(String Description) {this.Description = Description;}

}
