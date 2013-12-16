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

import tp.stackoverflow.adapters.ViewEntityAdapter;
import tp.stackoverflow.entity_view.ListViewEntity;
import tp.stackoverflow.loaders.AnswerLoader;
import tp.stackoverflow.R;
import tp.stackoverflow.views.SlidingLayer;

/**
 * Created by korolkov on 12/7/13.
 */

public class AnswersFragment extends Fragment implements LoaderManager.LoaderCallbacks<List<ListViewEntity>> {

    public static final String INSET_NUMBER = "inset_number";
    public static final String QUESTION     = "question";

    public static String INTENT_FILTER = "answers.fragment.intent.filter";

    private SlidingLayer sl;
    //private int           requestId;
    //protected Question question;
    private int questionId;
    private ViewEntityAdapter answerAdapter;
    private ListView listView;
    protected Activity activity;


    public AnswersFragment() {
        // Empty constructor required for fragment subclasses
    }

    public void updateAdapter(){
        //questionAdapter
        getLoaderManager().restartLoader(1,null,this);
    }



    public void showAnswers(){
        getLoaderManager().initLoader(1,null,this);
    }

    @Override
    public void onResume() {
        super.onResume();
        sl       = (SlidingLayer)activity.findViewById(R.id.slidingLayer);
        listView = (ListView)sl.findViewById(R.id.answerLw);//(ListView)getActivity().findViewById(R.id.answerLw);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        questionId = getArguments().getInt(AnswersFragmentDeprecated.QUESTION);
        activity = getActivity();
/*        String inset = getResources().getStringArray(R.array.inset_array)[i];
        getActivity().setTitle(inset);*/
        return null;
    }

    @Override
    public Loader<List<ListViewEntity>> onCreateLoader(int i, Bundle bundle) {
        return new AnswerLoader(getActivity().getApplicationContext(),questionId);
    }

    @Override
    public void onLoadFinished(Loader<List<ListViewEntity>> listLoader, List<ListViewEntity> dbEntities) {
        answerAdapter = new ViewEntityAdapter(getActivity(),dbEntities);
        sl.openLayer(true);
        listView.setAdapter(answerAdapter);
    }

    @Override
    public void onLoaderReset(Loader<List<ListViewEntity>> listLoader) {

    }
}
