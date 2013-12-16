package tp.stackoverflow.adapters;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Calendar;
import java.util.List;

import tp.stackoverflow.R;
import tp.stackoverflow.entity_view.ListViewEntity;

/**
 * Created by korolkov on 12/12/13.
 */


public class QuestAdapter extends ArrayAdapter<ListViewEntity> {
    private Activity activity;
    private List<ListViewEntity> questions;

    static class ViewHolder {
        public ImageView image;
        public TextView title;
        public TextView  displayName;
        public TextView  id;
    }

    public QuestAdapter(Activity activity,List<ListViewEntity> questions) {
        super(activity, R.layout.question,questions);
        this.activity  = activity;
        this.questions = questions;

    }

    public void updateData(List<ListViewEntity> questions){
        this.questions = questions;
    }

    public int getQuestionId(int position){
        return questions.get(position).getEntityId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View rowView = convertView;
        if (rowView == null) {
            LayoutInflater inflater = activity.getLayoutInflater();
            rowView = inflater.inflate(R.layout.question, null);
            ViewHolder viewHolder = new ViewHolder();
            viewHolder.title = (TextView) rowView.findViewById(R.id.title);
            viewHolder.displayName = (TextView) rowView.findViewById(R.id.date);
            viewHolder.image = (ImageView) rowView
                    .findViewById(R.id.image);
            rowView.setTag(viewHolder);
        }

        ViewHolder holder = (ViewHolder) rowView.getTag();
        ListViewEntity question = questions.get(position);
        //TODO calendar
        Calendar cal = Calendar.getInstance();
        //cal.setTimeInMillis(Integer.parseInt(question.getDate()) * 1000);
        holder.title.setText(question.getTitle());
        holder.displayName.setText(question.getDisplayName());
        //holder.date.setText(Integer.toString(question.getQuestionId()));
        byte[] bytes = question.getImage();
        if (bytes != null && bytes.length != 0) {
            Bitmap bitmap = BitmapFactory.decodeByteArray(bytes,0,bytes.length);
            holder.image.setImageBitmap(bitmap);
        } else {
            holder.image.setImageResource(R.drawable.ic_launcher);
        }



        return rowView;
    }
}