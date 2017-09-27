package ExamFramework_Adapter;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.fyshadows.examframework.examframework.ChatWindow;
import com.fyshadows.examframework.examframework.Exam_database;
import com.fyshadows.examframework.examframework.R;

import java.util.List;

import ExamFramework_Data.ChatRoomData;

/**
 * Created by Prasanna on 28-08-2015.
 */
public class ChatRoomAdapter extends ArrayAdapter<ChatRoomData> {
    private final Activity context;
    private static List<ChatRoomData> list = null;


    Exam_database db;


    public ChatRoomAdapter(Activity context,
                           List<ChatRoomData> list) {
        super(context, R.layout.chatroomview, list);
        this.context = context;
        ChatRoomAdapter.list = list;
    }

    @Override
    public int getCount() {
        Log.i("a", "a" + list.size());
        return list.size();
    }

    public static ChatRoomData getChatRoomDetailsPosition(int position) {
        return list.get(position);
    }


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        db = new Exam_database(parent.getContext());
        View view = null;
        Log.i("a", "into get view");
        if (convertView == null) {
            LayoutInflater inflator = context.getLayoutInflater();
            view = inflator.inflate(R.layout.chatroomview, null);
            final ViewHolder viewHolder = new ViewHolder();
            viewHolder.txtRoomName = (TextView) view.findViewById(R.id.txtRoomName);
            viewHolder.txtRoomName.setTextColor(Color.WHITE);
            viewHolder.txtChatCount = (TextView) view.findViewById(R.id.txtChatCount);
            viewHolder.txtCreatedBy = (TextView) view.findViewById(R.id.txtCreatedBy);
            viewHolder.toggleNotification = (ToggleButton) view.findViewById(R.id.toggleNotification);
            viewHolder.FavButton = (ToggleButton) view.findViewById(R.id.FavButton);
            viewHolder.topbase = (RelativeLayout) view.findViewById(R.id.topbase);
            viewHolder.bottombar = (LinearLayout) view.findViewById(R.id.bottombar);

            view.setTag(viewHolder);
        } else {
            view = convertView;
        }

        final ViewHolder holder = (ViewHolder) view.getTag();

        //Start : Setting the value
        holder.txtRoomName.setText(Html.fromHtml(list.get(position).getRoomName().toString().toUpperCase()));
        holder.txtChatCount.setText(String.valueOf(list.get(position).getChatCount()));
        holder.txtCreatedBy.setText(String.valueOf(list.get(position).getCreatedBy()));

        Log.i("not-enabled", String.valueOf(list.get(position).getNotificationEnabled()));
        if (list.get(position).getNotificationEnabled() == 1) {
            holder.toggleNotification.setChecked(true);
        } else {
            holder.toggleNotification.setChecked(false);
        }
        Log.i("nottt-enabled", String.valueOf(list.get(position).getFavEnabled()));
        if (list.get(position).getFavEnabled() == 1) {
            holder.FavButton.setChecked(true);
        } else {
            holder.FavButton.setChecked(false);
        }

        if (position % 2 == 0) {
            holder.topbase.setBackgroundColor(Color.parseColor("#03a9f4"));
            holder.bottombar.setBackgroundColor(Color.parseColor("#039be5"));
        } else if (position % 3 == 0) {
            holder.topbase.setBackgroundColor(Color.parseColor("#ce5250"));
            holder.bottombar.setBackgroundColor(Color.parseColor("#c24949"));
        } else if (position % 5 == 0) {
            holder.topbase.setBackgroundColor(Color.parseColor("#53d37e"));
            holder.bottombar.setBackgroundColor(Color.parseColor("#47c772"));
        } else {
            holder.topbase.setBackgroundColor(Color.parseColor("#7988e5"));
            holder.bottombar.setBackgroundColor(Color.parseColor("#5969c8"));
        }

        //End : Setting the value


        holder.toggleNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (holder.toggleNotification.isChecked()) {
                    Log.i("sql-noti", "Came in On");
                    db.updateToggleNotification(list.get(position).getid(), 1);
                } else {
                    db.updateToggleNotification(list.get(position).getid(), 0);
                }

            }

        });

        holder.FavButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (holder.FavButton.isChecked()) {
                    db.updateToggleFavourite(list.get(position).getid(), 1);
                } else {
                    db.updateToggleFavourite(list.get(position).getid(), 0);
                }

            }

        });

        holder.txtRoomName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(context, ChatWindow.class);
                Bundle bundle = new Bundle();
                bundle.putInt("RoomId", list.get(position).getid());
                bundle.putString("RoomName", list.get(position).getRoomName());
                i.putExtras(bundle);
                context.startActivity(i);

            }

        });


        return view;


    }

    static class ViewHolder {
        protected TextView txtRoomName;
        protected TextView txtChatCount;
        protected ToggleButton toggleNotification;
        protected ToggleButton FavButton;
        protected TextView txtCreatedBy;
        protected RelativeLayout topbase;
        protected LinearLayout bottombar;

    }

}
