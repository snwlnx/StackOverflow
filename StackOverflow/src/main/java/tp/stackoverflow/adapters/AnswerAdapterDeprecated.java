package tp.stackoverflow.adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Calendar;
import java.util.List;

import tp.stackoverflow.database_entities.Answer;
import tp.stackoverflow.R;

/**
 * Created by korolkov on 12/3/13.
 */
public class AnswerAdapterDeprecated extends ArrayAdapter<Answer> {

    private Activity activity;
    private List<Answer> answers;

    static class ViewHolder {
        public ImageView image;
        public TextView  body;
        public TextView  date;
//        public TextView  id;
    }

    public AnswerAdapterDeprecated(Activity activity, List<Answer> answers) {
        super(activity,R.layout.question, answers);
        this.activity  = activity;
        this.answers = answers;
    }

    public void updateData(List<Answer> answers){
        this.answers = answers;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View rowView = convertView;
        if (rowView == null) {
            LayoutInflater inflater = activity.getLayoutInflater();
            rowView = inflater.inflate(R.layout.answer, null);
            ViewHolder viewHolder = new ViewHolder();
            viewHolder.body = (TextView) rowView.findViewById(R.id.answerBody);
            viewHolder.date = (TextView) rowView.findViewById(R.id.answerDate);
            viewHolder.image = (ImageView) rowView
                    .findViewById(R.id.answerImage);
            rowView.setTag(viewHolder);
        }

        ViewHolder holder = (ViewHolder) rowView.getTag();
        Answer answer = answers.get(position);
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(Integer.parseInt(answer.getDate()) * 1000);
        holder.body.setText(answer.getBody());
        holder.date.setText(cal.getTime().toString());
        holder.image.setImageResource(R.drawable.ic_launcher);

        return rowView;
    }
}
