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

import com.fyshadows.examframework.examframework.Exam_database;
import com.fyshadows.examframework.examframework.Questionhome;
import com.fyshadows.examframework.examframework.R;

import java.util.List;

/**
 * Created by Prasanna on 12-11-2016.
 */

public class MonthlyExamAdapter extends ArrayAdapter<String> {
    private final Activity context;
    private static List<String> list = null;


    Exam_database db;


    public MonthlyExamAdapter(Activity context,
                              List<String> list) {
        super(context, R.layout.monthlyexamview, list);
        this.context = context;
        MonthlyExamAdapter.list = list;
    }

    @Override
    public int getCount() {
        Log.i("a", "a" + list.size());
        return list.size();
    }

    public static String getMonthlyExamDetailsPosition(int position) {
        return list.get(position);
    }


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        db = new Exam_database(parent.getContext());
        View view = null;
        Log.i("a", "into get view");
        if (convertView == null) {
            LayoutInflater inflator = context.getLayoutInflater();
            view = inflator.inflate(R.layout.monthlyexamview, null);
            final MonthlyExamAdapter.ViewHolder viewHolder = new MonthlyExamAdapter.ViewHolder();
            viewHolder.Date = (TextView) view.findViewById(R.id.txtmonth);
            viewHolder.Date.setTextColor(Color.BLACK);
            viewHolder.Score = (TextView) view.findViewById(R.id.txtScore);
            viewHolder.Score.setTextColor(Color.BLACK);
            viewHolder.TakeExam = (TextView) view.findViewById(R.id.txtTakeExam);
            viewHolder.TakeExam.setTextColor(Color.BLACK);

            view.setTag(viewHolder);
        } else {
            view = convertView;
        }

        MonthlyExamAdapter.ViewHolder holder = (MonthlyExamAdapter.ViewHolder) view.getTag();
        if (!list.get(position).toString().trim().equalsIgnoreCase("")) {
            String Score = db.getscore(2, list.get(position).toString(), "");
            holder.Date.setText(list.get(position).toString());
            if (Score.length() > 0) {
                holder.Score.setText("Score:" + Score);
            } else {
                holder.Score.setVisibility(View.INVISIBLE);
            }

        }


        holder.TakeExam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Log.i("Selected Date", list.get(position).toString());
                Intent i = new Intent(context, Questionhome.class);
                Bundle bundle = new Bundle();
                bundle.putInt("FromScreen", 2);
                bundle.putString("ExamDate", list.get(position).toString());
                i.putExtras(bundle);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK
                        | Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(i);

            }

        });

        holder.Date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Log.i("Selected Date", list.get(position).toString());
                Intent i = new Intent(context, Questionhome.class);
                Bundle bundle = new Bundle();
                bundle.putInt("FromScreen", 2);
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
        protected TextView Score;
        protected TextView TakeExam;
    }

}
