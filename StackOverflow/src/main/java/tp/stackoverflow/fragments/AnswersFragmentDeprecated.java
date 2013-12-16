package tp.stackoverflow.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.app.LoaderManager;
import android.content.Loader;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.List;

import tp.stackoverflow.adapters.AnswerAdapterDeprecated;
import tp.stackoverflow.database_entities.Answer;
import tp.stackoverflow.database_entities.Question;
import tp.stackoverflow.loaders.AnswerLoaderDeprecated;
import tp.stackoverflow.R;
import tp.stackoverflow.views.SlidingLayer;

/**
 * Created by korolkov on 12/3/13.
 */
public class AnswersFragmentDeprecated extends Fragment implements LoaderManager.LoaderCallbacks<List<Answer>> {

    public static final String INSET_NUMBER = "inset_number";
    public static final String QUESTION     = "question";

    private SlidingLayer  sl;
    //private int           requestId;
    private Question      question;
    private AnswerAdapterDeprecated answerAdapter;
    private ListView      listView;
    private Activity      activity;


    public AnswersFragmentDeprecated() {
        // Empty constructor required for fragment subclasses
    }

    public void showAnswers(){
        getLoaderManager().initLoader(1,null,this);
    }

    @Override
    public void onResume() {
        super.onResume();
        sl = (SlidingLayer)activity.findViewById(R.id.slidingLayer);
        listView = (ListView)sl.findViewById(R.id.answerLw);//(ListView)getActivity().findViewById(R.id.answerLw);



/*

 IntentFilter filter = new IntentFilter("question.intent.filter");
        receiver = new ResponseReceiver();
        getActivity().registerReceiver(receiver, filter);*/
        //getLoaderManager().initLoader(1,null,this);
/*       ((TextView)activity.findViewById(R.id.questTitle)).setText(question.getTitle());
        ((TextView)activity.findViewById(R.id.questBody)).setText(question.getBody());*/
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        question = getArguments().getParcelable(AnswersFragmentDeprecated.QUESTION);
        activity = getActivity();
/*        String inset = getResources().getStringArray(R.array.inset_array)[i];
        getActivity().setTitle(inset);*/
        return null;
    }

    @Override
    public Loader<List<Answer>> onCreateLoader(int i, Bundle bundle) {
        return new AnswerLoaderDeprecated(getActivity().getApplicationContext(),question.getQuestionId());
    }

    @Override
    public void onLoadFinished(Loader<List<Answer>> listLoader, List<Answer> answers) {
        //TODO adapter
        answerAdapter = new AnswerAdapterDeprecated(getActivity(),answers);
        sl.openLayer(true);
        listView.setAdapter(answerAdapter);
    }

    @Override
    public void onLoaderReset(Loader<List<Answer>> listLoader) {
        answerAdapter.updateData(null);
    }
}
