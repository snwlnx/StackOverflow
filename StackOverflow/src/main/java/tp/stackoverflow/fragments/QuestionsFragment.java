package tp.stackoverflow.fragments;

import android.app.Fragment;
import android.app.LoaderManager;
import android.content.Loader;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import tp.stackoverflow.adapters.QuestionAdapter;
import tp.stackoverflow.entity_view.ListViewEntity;
import tp.stackoverflow.loaders.QuestionLoader;
import tp.stackoverflow.MainActivity;
import tp.stackoverflow.R;
import tp.stackoverflow.service_requests.RequestDetails;
import tp.stackoverflow.views.EndlessScrollListener;
import tp.stackoverflow.views.PullToRefreshListView;

/**
 * Created by korolkov on 11/29/13.
 */
public class QuestionsFragment extends Fragment implements LoaderManager.LoaderCallbacks<List<ListViewEntity>>,EndlessScrollListener.ScrollListener, PullToRefreshListView.OnRefreshListener {
    public static final String INSET_NUMBER   = "inset_number";
    public static final String REQUEST_NUMBER = "request_number";
    public static final String REQUEST_KEY = "request_key";
    public static final String REQUEST_PAGE = "request_page";

    public static String INTENT_FILTER = "question.fragment.intent.filter";


    private int              requestId;
    private List<Integer>    pageRequestId = new ArrayList<Integer>();
    private int              page;
    private String           requestKey;

    private boolean scrollListnerSet = false;
    private QuestionAdapter       questionAdapter;
    private  List<ListViewEntity> questionsSet;
    private ListView         lw;

    TextView v;

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
        questionsSet    = new ArrayList<ListViewEntity>();
        questionAdapter = new QuestionAdapter(getActivity(),questionsSet);

        lw = (ListView)getActivity().findViewById(R.id.listView);
        v =  new TextView(getActivity());
        v.setText("please wait");
        lw.addFooterView(v);

        lw.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Toast.makeText(getActivity(), "Please wait", Toast.LENGTH_SHORT).show();
                ((MainActivity)getActivity()).getAnswers(questionAdapter.getQuestionId(position));
            }
        });
        lw.setAdapter(questionAdapter);


        getLoaderManager().initLoader(1,null,this);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.question_fragment, container, false);
        Bundle args = getArguments();
        requestId   = args.getInt(REQUEST_NUMBER);
        pageRequestId.add(requestId);
        page       = args.getInt(REQUEST_PAGE);
        requestKey = args.getString(REQUEST_KEY);

/*        String inset = getResources().getStringArray(R.array.inset_array)[i];
        getActivity().setTitle(inset);*/
        return rootView;
    }

    @Override
    public Loader<List<ListViewEntity>> onCreateLoader(int i, Bundle bundle) {
        return new QuestionLoader(getActivity().getApplicationContext(),requestId,pageRequestId);
    }

    @Override
    public void onLoadFinished(Loader<List<ListViewEntity>> listLoader, List<ListViewEntity> questions) {
        //TODO adapter

        //questionAdapter = new QuestionAdapter(getActivity(),questions);
        questionAdapter.addQuestions(questions);
        questionAdapter.notifyDataSetChanged();

        //lw.setAdapter(questionAdapter);
        /*questionAdapter.updateData(questions);
        questionAdapter.notifyDataSetChanged();*/
        if(!scrollListnerSet) {
            lw.setOnScrollListener(new EndlessScrollListener(this));
            scrollListnerSet = true;
           // ((PullToRefreshListView)lw).setOnRefreshListener(this);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<ListViewEntity>> listLoader) {
        questionAdapter.updateData(null);
    }



    @Override
    public void onLoadMore(int page, int totalItemsCount) {
        Log.v("my_listener",Integer.toString(page));
        RequestDetails details = new RequestDetails(requestKey,requestId,page);
        ((MainActivity) getActivity()).getQuestions(details);


        //v.setVisibility(View.GONE);

    }

    @Override
    public void onLoadFinish() {

     //   ((PullToRefreshListView)lw).onRefreshComplete();
        //v.setVisibility(View.VISIBLE);
    }

    public void addRequestId(int requestId){
        pageRequestId.add(requestId);
    }

    @Override
    public void onRefresh() {

    }
}
