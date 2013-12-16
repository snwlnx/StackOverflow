package tp.stackoverflow.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import tp.stackoverflow.Processor;
import tp.stackoverflow.ResponseMessage;

/**
 * Created by korolkov on 12/12/13.
 */
public class AnswerReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        Log.v("myIntentResponseAction", intent.getAction());
        ResponseMessage msg = intent.getParcelableExtra(Processor.RESPONSE_MESSAGE);
        Log.v("myIntentResponseCode", msg.status.name());

        switch (msg.status){
            case COMPLETE:{
/*                    Fragment fragment = new AnswersFragmentDeprecated();
                    Bundle args = new Bundle();
                    args.putInt(QuestionsFragment.REQUEST_NUMBER, msg.requestId);
                    fragment.setArguments(args);

                    FragmentManager fragmentManager = getFragmentManager();
                    fragmentManager.beginTransaction().add( fragment,"1").commit();*/

                //answersFragment.showAnswers();
                break;

            }
            case PENDING:{/*
                    Fragment fragment = new ProgressWheelFragment();
                    FragmentManager fragmentManager = getFragmentManager();
                    fragmentManager.beginTransaction().add(fragment,"1").commit();*/
                break;
            }
        }
    }
}