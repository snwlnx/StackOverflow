package tp.stackoverflow;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.SearchManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import tp.stackoverflow.fragments.AnswersFragment;
import tp.stackoverflow.fragments.AnswersFragmentDeprecated;
import tp.stackoverflow.fragments.ProgressWheelFragment;
import tp.stackoverflow.fragments.QuestionsFragment;
import tp.stackoverflow.views.SlidingLayer;

public class MainActivity extends ActionBarActivity  /*implements LoaderManager.LoaderCallbacks<List<Question>>*/{

/*    private int              requestId;
    private QuestionAdapter  questionAdapter;*/
    //private ListView         lw;
    public DownloadService.DownloadBinder binder;
    private boolean          mBound;
    private QuestionReceiver receiver;
    private AnswersReceiver  answerReceiver;


    private DrawerLayout     mDrawerLayout;
    private ListView         mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;

    private CharSequence     mDrawerTitle;
    private CharSequence     mTitle;
    private String[]         mInsetTitles;
    private SlidingLayer sl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*questionAdapter = new QuestionAdapter(this);*/
        /*lw = (ListView)findViewById(R.id.listView);*/
        /*lw.setAdapter(questionAdapter);*/
        mTitle = mDrawerTitle = getTitle();

        mInsetTitles  = getResources().getStringArray(R.array.inset_array);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList   = (ListView) findViewById(R.id.left_drawer);

        // set a custom shadow that overlays the main content when the drawer opens
        mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
        // set up the drawer's list view with items and click listener
        mDrawerList.setAdapter(new ArrayAdapter<String>(this,
                R.layout.drawer_list_item, mInsetTitles));
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

        // enable ActionBar app icon to behave as action to toggle nav drawer
        getActionBar().setDisplayHomeAsUpEnabled(true);
        //getActionBar().setHomeButtonEnabled(true);

        // ActionBarDrawerToggle ties together the the proper interactions
        // between the sliding drawer and the action bar app icon
        mDrawerToggle = new ActionBarDrawerToggle(
                this,                  /* host Activity */
                mDrawerLayout,         /* DrawerLayout object */
                R.drawable.ic_drawer,  /* nav drawer image to replace 'Up' caret */
                R.string.drawer_open,  /* "open drawer" description for accessibility */
                R.string.drawer_close  /* "close drawer" description for accessibility */
        ) {
            public void onDrawerClosed(View view) {
                getActionBar().setTitle(mTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            public void onDrawerOpened(View drawerView) {
                getActionBar().setTitle(mDrawerTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };
        mDrawerLayout.setDrawerListener(mDrawerToggle);

        if (savedInstanceState == null) {
            selectItem(0);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(receiver);
        unregisterReceiver(answerReceiver);
    }


/*
    public void getAnswers(int entityId){
        binder.getAnswers(entityId);
    }
*/

    @Override
    public void onResume() {
        super.onResume();
        IntentFilter filter = new IntentFilter(QuestionsFragment.INTENT_FILTER);
        receiver = new QuestionReceiver();
        registerReceiver(receiver, filter);

        filter         = new IntentFilter(AnswersFragment.INTENT_FILTER);
        answerReceiver = new AnswersReceiver();
        registerReceiver(answerReceiver, filter);
    }

    @Override
    protected void onStart() {
        super.onStart();
       // Bind to LocalService*
        Intent intent = new Intent(this, DownloadService.class);
        bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onStop() {
        super.onStop();
        // Unbind from the service
/*        if (mBound) {
            unbindService(mConnection);
            mBound = false;
        }*/
    }

    @Override
    public void setTitle(CharSequence title) {
        mTitle = title;
        getActionBar().setTitle(mTitle);
    }

    /**
     * When using the ActionBarDrawerToggle, you must call it during
     * onPostCreate() and onConfigurationChanged()...
     */

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggles
        mDrawerToggle.onConfigurationChanged(newConfig);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // The action bar home/up action should open or close the drawer.
        // ActionBarDrawerToggle will take care of this.
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        // Handle action buttons
        switch(item.getItemId()) {
            case R.id.action_websearch:
                // create intent to perform web search for this planet
                Intent intent = new Intent(Intent.ACTION_WEB_SEARCH);
                intent.putExtra(SearchManager.QUERY, getActionBar().getTitle());
                // catch event that there's no activity to handle intent
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivity(intent);
                } else {
                    Toast.makeText(this, R.string.app_not_available, Toast.LENGTH_LONG).show();
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    /* Defines callbacks for service binding, passed to bindService() */
    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName className,IBinder iBinder) {
            // We've bound to LocalService, cast the IBinder and get LocalService instance
            binder = (DownloadService.DownloadBinder) iBinder;
            mBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            mBound = false;
        }
    };

    public void search(View view) {
        String search = ((EditText)findViewById(R.id.edit_text)).getText().toString();

        if(isOnline()){
            if(binder != null && search != null && !search.equals("")){
                binder.getQuestions(search);
            }
        }else {
            Toast.makeText(this, "You are offline", Toast.LENGTH_LONG).show();
        }
    }




    /* The click listener for ListView in the navigation drawer */
    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            selectItem(position);
        }
    }

    private void selectItem(int position) {
        // update the main content by replacing fragments
        Fragment fragment = new Fragment(){


            @Override
            public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                     Bundle savedInstanceState) {
                return inflater.inflate(R.layout.start_fragment, container, false);
            }

        };
/*
        Bundle args = new Bundle();
        args.putInt(QuestionsFragment.INSET_NUMBER, position);
        fragment.setArguments(args);
*/

        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();

        // update selected item and title, then close the drawer
        mDrawerList.setItemChecked(position, true);
        setTitle(mInsetTitles[position]);
        mDrawerLayout.closeDrawer(mDrawerList);
    }

    public boolean isOnline() {
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        boolean isWifiConn = networkInfo.isConnected();
        networkInfo = connMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        boolean isMobileConn = networkInfo.isConnected();
        return (isWifiConn || isMobileConn);
    }

    public class QuestionReceiver extends BroadcastReceiver {
        QuestionsFragment questionsFragment;
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.v("myIntentResponseAction", intent.getAction());
            ResponseMessage msg = intent.getParcelableExtra(Processor.RESPONSE_MESSAGE);
            Log.v("myIntentResponseCode", msg.status.name());

            switch (msg.status){
                case COMPLETE:{
                    questionsFragment = new QuestionsFragment();
                    Bundle args = new Bundle();
                    args.putInt(QuestionsFragment.REQUEST_NUMBER, msg.requestId);
                    questionsFragment.setArguments(args);

                    FragmentManager fragmentManager = getFragmentManager();
                    fragmentManager.beginTransaction().replace(R.id.content_frame, questionsFragment).commit();
                    break;

                }
                case PENDING:{
                    Fragment fragment = new ProgressWheelFragment();
                    FragmentManager fragmentManager = getFragmentManager();
                    fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();
                    break;
                }
                case REFRESH:{

                    questionsFragment.updateAdapter();
                    break;
                }
            }
        }
    }

    public class AnswersReceiver extends BroadcastReceiver {
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

                    answersFragment.showAnswers();
                    break;

                }
                case PENDING:{/*
                    Fragment fragment = new ProgressWheelFragment();
                    FragmentManager fragmentManager = getFragmentManager();
                    fragmentManager.beginTransaction().add(fragment,"1").commit();*/
                    break;
                }
                case REFRESH:{
                    answersFragment.updateAdapter();
                    break;
                }
            }
        }
    }

    //private AnswersFragmentDeprecated answersFragment;
    private AnswersFragment answersFragment;

    public void getAnswers(int questionId){
        answersFragment   = new AnswersFragment();
        Bundle   args     = new Bundle();

        //args.putParcelable(AnswersFragmentDeprecated.QUESTION, question);
        args.putInt(AnswersFragmentDeprecated.QUESTION, questionId);
        answersFragment.setArguments(args);

        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction().add(answersFragment,"answer_fragment").commit();

        binder.getAnswers(questionId);


    }

/*
    private void startLoader(){
        getLoaderManager().initLoader(1,null,this);
    }*/
/*    public void start(View view) {
        //TODO ...

            fragment.start(view);

            //binder.getQuestions("Activity error");
    }*/

}
