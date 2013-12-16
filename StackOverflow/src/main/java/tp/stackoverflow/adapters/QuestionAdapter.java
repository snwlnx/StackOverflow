package tp.stackoverflow.adapters;

/**
 * Created by korolkov on 11/28/13.
 */
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Calendar;
import java.util.List;

import tp.stackoverflow.database_entities.DbEntity;
import tp.stackoverflow.entity_view.FullQuestion;
import tp.stackoverflow.database_entities.Question;
import tp.stackoverflow.R;

public class QuestionAdapter extends ArrayAdapter<DbEntity> {
    private  Activity       activity;
    private  List<DbEntity> questions;

    static class ViewHolder {
        public ImageView image;
        public TextView  title;
        public TextView  date;
        public TextView  id;
    }

    public QuestionAdapter(Activity activity,List<DbEntity> questions) {
        super(activity, R.layout.question,questions);
        this.activity  = activity;
        this.questions = questions;

    }

    public void updateData(List<DbEntity> questions){
        this.questions = questions;
    }

    public Question getQuestion(int position){
        return (Question)questions.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View rowView = convertView;
        if (rowView == null) {
            LayoutInflater inflater = activity.getLayoutInflater();
            rowView = inflater.inflate(R.layout.question, null);
            ViewHolder viewHolder = new ViewHolder();
            viewHolder.title = (TextView) rowView.findViewById(R.id.title);
            viewHolder.date = (TextView) rowView.findViewById(R.id.date);
            viewHolder.image = (ImageView) rowView
                    .findViewById(R.id.image);
            rowView.setTag(viewHolder);
        }

        ViewHolder holder = (ViewHolder) rowView.getTag();
        FullQuestion question = (FullQuestion)questions.get(position);
        //TODO calendar
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(Integer.parseInt(question.getDate()) * 1000);
        holder.title.setText(question.getTitle());
        holder.date.setText(cal.getTime().toString());
        //holder.date.setText(Integer.toString(question.getQuestionId()));
        holder.image.setImageResource(R.drawable.ic_launcher);

        return rowView;
    }
}