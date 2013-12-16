package tp.stackoverflow.fragments;

import android.app.Fragment;
import android.app.LoaderManager;
import android.content.Loader;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.List;

import tp.stackoverflow.adapters.QuestAdapter;
import tp.stackoverflow.entity_view.ListViewEntity;
import tp.stackoverflow.loaders.QuestionLoader;
import tp.stackoverflow.MainActivity;
import tp.stackoverflow.R;

/**
 * Created by korolkov on 11/29/13.
 */
public class QuestionsFragment extends Fragment implements LoaderManager.LoaderCallbacks<List<ListViewEntity>> {
    public static final String INSET_NUMBER   = "inset_number";
    public static final String REQUEST_NUMBER = "request_number";

    public static String INTENT_FILTER = "question.fragment.intent.filter";


    private int              requestId;
    private QuestAdapter     questionAdapter;
    private ListView         lw;


    public QuestionsFragment() {
        // Empty constructor required for fragment subclasses
    }

    public void updateAdapter(){
        //questionAdapter

        getLoaderManager().restartLoader(1, null, this);
    }

    @Override
    public void onResume() {
        super.onResume();
        lw = (ListView)getActivity().findViewById(R.id.listView);
        lw.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Toast.makeText(getActivity(), "Please wait", Toast.LENGTH_SHORT).show();
                ((MainActivity)getActivity()).getAnswers(questionAdapter.getQuestionId(position));
            }
        });
        getLoaderManager().initLoader(1,null,this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.question_fragment, container, false);
        requestId = getArguments().getInt(REQUEST_NUMBER);
/*        String inset = getResources().getStringArray(R.array.inset_array)[i];
        getActivity().setTitle(inset);*/
        return rootView;
    }

    @Override
    public Loader<List<ListViewEntity>> onCreateLoader(int i, Bundle bundle) {
        return new QuestionLoader(getActivity().getApplicationContext(),requestId);
    }

    @Override
    public void onLoadFinished(Loader<List<ListViewEntity>> listLoader, List<ListViewEntity> questions) {
        //TODO adapter
        questionAdapter = new QuestAdapter(getActivity(),questions);
        lw.setAdapter(questionAdapter);
        /*questionAdapter.updateData(questions);
        questionAdapter.notifyDataSetChanged();*/
    }

    @Override
    public void onLoaderReset(Loader<List<ListViewEntity>> listLoader) {
        questionAdapter.updateData(null);
    }


}
