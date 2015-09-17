package ExamFramework_Adapter;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.fyshadows.examframework.examframework.DailyArticle;
import com.fyshadows.examframework.examframework.Exam_database;
import com.fyshadows.examframework.examframework.HomeActivity;
import com.fyshadows.examframework.examframework.Questionhome;
import com.fyshadows.examframework.examframework.R;

import java.util.List;

import ExamFramework_Data.DailyExam;

/**
 * Created by Prasanna on 28-08-2015.
 */
public class DailyExamAdapter extends ArrayAdapter<String> {
    private final Activity context;
    private static List<String> list = null;


    Exam_database db;


    public DailyExamAdapter(Activity context,
                              List<String> list) {
        super(context, R.layout.dailyexamview, list);
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        Log.i("a", "a" + list.size());
        return list.size();
    }

    public static String getDailyExamDetailsPosition(int position) {
        return list.get(position);
    }


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        db= new Exam_database(parent.getContext());
        View view = null;
        Log.i("a", "into get view");
        if (convertView == null) {
            LayoutInflater inflator = context.getLayoutInflater();
            view = inflator.inflate(R.layout.dailyexamview, null);
            final ViewHolder viewHolder = new ViewHolder();
            viewHolder.Date = (TextView) view.findViewById(R.id.txtDate);
            viewHolder.Date.setTextColor(Color.BLACK);
            viewHolder.Article = (TextView) view.findViewById(R.id.txtArticle);
            viewHolder.Article.setTextColor(Color.BLACK);
            viewHolder.Score = (TextView) view.findViewById(R.id.txtScore);
            viewHolder.Score.setTextColor(Color.BLACK);

            view.setTag(viewHolder);
        } else {
            view = convertView;
        }

        ViewHolder holder = (ViewHolder) view.getTag();
        if (!list.get(position).toString().trim().equalsIgnoreCase("")) {
            String Score=db.getscore(1, list.get(position).toString());
            holder.Date.setText(list.get(position).toString());
            if(Score.length() > 0)
            {
                holder.Score.setText(Score);
            }
            else
            {
                holder.Score.setVisibility(view.INVISIBLE);
            }

        }


        holder.Date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Log.i("Selected Date",list.get(position).toString());
                Intent i = new Intent(context, Questionhome.class);
                Bundle bundle = new Bundle();
                bundle.putInt("FromScreen", 1);
                bundle.putString("ExamDate", list.get(position).toString());
                i.putExtras(bundle);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK
                        | Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(i);

            }

        });

        holder.Article.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Log.i("Selected Date",list.get(position).toString());
                Intent i = new Intent(context, DailyArticle.class);
                Bundle bundle = new Bundle();
                bundle.putString("ExamDate", list.get(position).toString());
                i.putExtras(bundle);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK
                        | Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(i);

            }

        });

        return view;


    }

    static class ViewHolder {
        protected TextView Date;
        protected TextView Article;
        protected TextView Score;
    }

}
