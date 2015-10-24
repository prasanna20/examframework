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
import android.widget.TextView;

import com.fyshadows.examframework.examframework.Exam_database;
import com.fyshadows.examframework.examframework.R;

import java.util.List;

import ExamFramework_Data.DailyArticleData;

/**
 * Created by Prasanna on 30-08-2015.
 */


public class DailyArticleAdapter extends ArrayAdapter<DailyArticleData> {
    private final Activity context;
    private static List<DailyArticleData> list = null;


    Exam_database db;


    public DailyArticleAdapter(Activity context,
                               List<DailyArticleData> list) {
        super(context, R.layout.dailyarticleview, list);
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        Log.i("a", "a" + list.size());
        return list.size();
    }

    public static String getDailyArticlePosition(int position) {
         return String.valueOf(list.get(position).getArticleNo());
    }


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        db = new Exam_database(parent.getContext());
        View view = null;
        Log.i("a", "into get view");
        if (convertView == null) {
            LayoutInflater inflator = context.getLayoutInflater();
            view = inflator.inflate(R.layout.dailyarticleview, null);
            final ViewHolder viewHolder = new ViewHolder();
            viewHolder.ArticleTopic = (TextView) view.findViewById(R.id.txtArticleTopic);
            viewHolder.ArticleDesc = (TextView) view.findViewById(R.id.txtArticleDesc);
            view.setTag(viewHolder);
        } else {
            view = convertView;
        }

        ViewHolder holder = (ViewHolder) view.getTag();
        if (!list.get(position).toString().trim().equalsIgnoreCase("")) {
            holder.ArticleTopic.setText(Html.fromHtml(list.get(position).getTopic().toString()));
            holder.ArticleDesc.setText(Html.fromHtml(list.get(position).getArticleDesc().toString()));
        }

        return view;


    }

    static class ViewHolder {
        protected TextView ArticleTopic;
        protected TextView ArticleDesc;
    }

}