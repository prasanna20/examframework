package ExamFramework_Adapter;

import android.app.Activity;
import android.graphics.Color;
import android.net.ParseException;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.fyshadows.examframework.examframework.R;

import java.text.SimpleDateFormat;
import java.util.List;

import ExamFramework_Data.notificationtable;

public class notificationcustomadapter extends ArrayAdapter<notificationtable> {

    private final Activity context;
    private static List<notificationtable> list = null;


    public notificationcustomadapter(Activity context,
                                     List<notificationtable> list) {
        super(context, R.layout.notification_row, list);
        this.context = context;
        notificationcustomadapter.list = list;
    }

    @Override
    public int getCount() {
        Log.i("a", "a" + list.size());
        return list.size();
    }

    public static notificationtable getnotificationtablePosition(int position) {
        return list.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = null;
        Log.i("a", "into get view");
        if (convertView == null) {
            LayoutInflater inflator = context.getLayoutInflater();
            view = inflator.inflate(R.layout.notification_row, null);
            final ViewHolder viewHolder = new ViewHolder();
            viewHolder.text = (TextView) view.findViewById(R.id.notitification_text);
            viewHolder.text.setTextColor(Color.BLACK);
            viewHolder.text1 = (TextView) view.findViewById(R.id.notitification_time);
            viewHolder.text1.setTextColor(Color.BLACK);
            view.setTag(viewHolder);
        } else {
            view = convertView;
        }

        ViewHolder holder = (ViewHolder) view.getTag();
        holder.text.setText(Html.fromHtml(list.get(position).getMessage()));
        holder.text1.setText(formateDateFromstring("yyyy-MM-dd hh:mm:ss", "dd MMM yyyy hh:mm:ss", list.get(position).gettimestamp()));

        return view;

    }

    public static String formateDateFromstring(String inputFormat, String outputFormat, String inputDate) {

        java.util.Date parsed = null;
        String outputDate = "";

        SimpleDateFormat df_input = new SimpleDateFormat(inputFormat, java.util.Locale.getDefault());
        SimpleDateFormat df_output = new SimpleDateFormat(outputFormat, java.util.Locale.getDefault());

        try {
            try {
                parsed = df_input.parse(inputDate);
            } catch (java.text.ParseException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            outputDate = df_output.format(parsed);
            Log.i("date", outputDate);

        } catch (ParseException e) {
            Log.i("date", "ParseException - dateFormat");
        }

        return outputDate;

    }

    static class ViewHolder {

        protected TextView text;
        protected TextView text1;

    }


}
