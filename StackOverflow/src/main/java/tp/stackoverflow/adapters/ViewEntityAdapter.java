package tp.stackoverflow.adapters;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import tp.stackoverflow.R;
import tp.stackoverflow.entity_view.ListViewEntity;

/**
 * Created by korolkov on 12/15/13.
 */
public class ViewEntityAdapter extends ArrayAdapter<ListViewEntity> {

    private Activity activity;
    private List<ListViewEntity> answers;

    static class ViewHolder {
        public ImageView image;
        public TextView  body;
        public TextView  date;
        public TextView  userName;
    }

    public ViewEntityAdapter(Activity activity, List<ListViewEntity> answers) {
        super(activity, R.layout.answer, answers);
        this.activity  = activity;
        this.answers = answers;
    }

/*    public void updateData(List<ListViewEntity> answers){
        this.answers = answers;
    }*/

    private String processRawString(String body,Map<Integer,Integer> indexes){
        final String startCode = "&^", endCode = "^&";
        body = body.replaceAll("code","&^").replaceAll("code","^&");
        body = body.replaceAll("\\<.*?>","");
        for(int lastIndex,firstIndex=body.indexOf(startCode,0);firstIndex >= 0;){
            body       = body.replaceFirst(startCode, "");
            lastIndex  = body.indexOf(endCode,firstIndex);//-"<code>".length();
            indexes.put(firstIndex,lastIndex);
            body       = body.replaceFirst(endCode, "");
            firstIndex = body.indexOf(startCode,lastIndex);
        }

        return body;

    }

    private SpannableString markupString(SpannableString spanString, HashMap<Integer,Integer> spanIndexes) {
        for (Map.Entry<Integer,Integer> entry :spanIndexes.entrySet()){
            spanString.setSpan(new ForegroundColorSpan(Color.RED),entry.getKey(),entry.getValue(),0);
        }
        return spanString;
    }

    public SpannableString emphasizeСode(String body){
        HashMap<Integer,Integer> spanIndexes = new HashMap<Integer,Integer>();
        SpannableString spanString = new SpannableString(processRawString(body,spanIndexes));
        if (!spanIndexes.isEmpty()) {
            markupString(spanString,spanIndexes);
        }
        return spanString;
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
            viewHolder.userName = (TextView) rowView.findViewById(R.id.userName);

            viewHolder.image = (ImageView) rowView
                    .findViewById(R.id.answerImage);
            rowView.setTag(viewHolder);
        }

        ViewHolder holder = (ViewHolder) rowView.getTag();
        ListViewEntity answer = answers.get(position);

        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(Integer.parseInt(answer.getDate()) * 1000);
        String body = answer.getBody();

        holder.body.setText(emphasizeСode(body), TextView.BufferType.SPANNABLE);
        holder.date.setText(cal.getTime().toString());
        holder.userName.setText(answer.getDisplayName());

        byte[] bytes = answer.getImage();
        if (bytes != null && bytes.length != 0) {
            Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
            holder.image.setImageBitmap(bitmap);
        } else {
            holder.image.setImageResource(R.drawable.ic_launcher);
        }
       //holder.image.setImageResource(R.drawable.ic_launcher);

        return rowView;
    }
}
