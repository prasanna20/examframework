package ExamFramework_Data;

public class notificationtable {


    private String _notificationid;

    private String _notification_message;
    private String _timestamp;

    static boolean isStatusMessage;

    public notificationtable() {


    }


    public notificationtable(String notificationid, String notification_message) {
        super();
        this._notification_message = notification_message;
        this._notificationid = notificationid;

    }

    public notificationtable(String notificationid, String notification_message, String timestamp) {
        super();
        this._notification_message = notification_message;
        this._notificationid = notificationid;
        this._timestamp = timestamp;

    }

    public String getMessage() {
        return this._notification_message;
    }

    public String getnotificationid() {
        return this._notificationid;
    }

    public String gettimestamp() {
        return this._timestamp;
    }


}
