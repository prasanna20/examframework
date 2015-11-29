package ExamFramework_Adapter;

import android.app.Activity;
import android.graphics.Color;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fyshadows.examframework.examframework.Exam_database;
import com.fyshadows.examframework.examframework.R;

import java.util.List;

import ExamFramework_Data.ChatData;

/**
 * Created by Prasanna on 29-11-2015.
 */
public class ChatWindowAdapter  extends ArrayAdapter<ChatData> {

    private final Activity context;
    private static List<ChatData> list = null;


    Exam_database db;


    public ChatWindowAdapter(Activity context,
                           List<ChatData> list) {
        super(context, R.layout.chatrow, list);
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    public static ChatData getChatWindowDetailsPosition(int position) {
        return list.get(position);
    }


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        db= new Exam_database(parent.getContext());
        View view = null;
        Log.i("a", "into get view");
        if (convertView == null) {
            LayoutInflater inflator = context.getLayoutInflater();
            view = inflator.inflate(R.layout.chatrow, null);
            final ViewHolder viewHolder = new ViewHolder();
            viewHolder.txtChatMessage = (TextView) view.findViewById(R.id.txtChatMessage);
            viewHolder.txtChatMessage.setTextColor(Color.BLACK);
            viewHolder.txtMessageTime = (TextView) view.findViewById(R.id.txtMessageTime);
            viewHolder.txtMessageTime.setTextColor(Color.BLACK);
            viewHolder.txtMessageUser = (TextView) view.findViewById(R.id.txtMessageUser);
            viewHolder.txtMessageUser.setTextColor(Color.BLACK);

            view.setTag(viewHolder);
        } else {
            view = convertView;
        }

        final ViewHolder holder = (ViewHolder) view.getTag();

        //Start : Setting the value
        holder.txtChatMessage.setText(Html.fromHtml(list.get(position).getChatMessage().toString()));
        holder.txtMessageTime.setText(String.valueOf(list.get(position).getTimeStamp()));
        holder.txtMessageUser.setText(String.valueOf(list.get(position).getUsername()));

        LinearLayout ll = (LinearLayout) view.findViewById(R.id.messlinear);

        //End : Setting the value
        if (list.get(position).getEmail() == db.GetEmailDetails(this.context)) {

            ll.setGravity(Gravity.RIGHT);

        }
        else
        {
            ll.setGravity(Gravity.LEFT);
        }

        return view;




    }

    static class ViewHolder {
        protected TextView txtChatMessage;
        protected TextView txtMessageTime;
        protected TextView txtMessageUser;

    }
}
