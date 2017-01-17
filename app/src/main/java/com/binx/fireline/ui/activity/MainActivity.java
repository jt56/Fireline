package com.binx.fireline.ui.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.binx.fireline.R;
import com.binx.fireline.adapter.IncidentAdapter;
import com.binx.fireline.model.Incident;
import com.binx.fireline.retrofit.api.ApiService;
import com.binx.fireline.retrofit.api.RetroClient;
import com.binx.fireline.utils.InternetConnection;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.crash.FirebaseCrash;
import com.mikepenz.aboutlibraries.Libs;
import com.mikepenz.aboutlibraries.LibsBuilder;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private FirebaseAnalytics mFirebaseAnalytics;

    private SwipeRefreshLayout swipeRefreshLayout;
    private TextView textViewLastUpdate;
    private ListView listView;
    private View parentView;

    private ArrayList<Incident> incidentList;
    private IncidentAdapter adapter;
    private final static String LOG_TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Obtain the FirebaseAnalytics instance.
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swiperefresh);
        textViewLastUpdate = (TextView) findViewById(R.id.textViewLastUpdate);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        incidentList = new ArrayList<>();

        parentView = findViewById(R.id.parentLayout);

        /**
         * Getting list and setting list adapter
         */
        listView = (ListView) findViewById(R.id.listView);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(view.getContext(), MapsActivity.class);
                ArrayList<Incident> incidentSingle = new ArrayList<>();
                incidentSingle.add(incidentList.get(position));
                intent.putExtra("Incidents", incidentSingle);
                view.getContext().startActivity(intent);
            }
        });

        FloatingActionButton fabMap = (FloatingActionButton) findViewById(R.id.fabMap);
        assert fabMap != null;
        fabMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(@NonNull final View view) {
                Intent intent = new Intent(view.getContext(), MapsActivity.class);
                intent.putExtra("Incidents", incidentList);
                view.getContext().startActivity(intent);
            }
        });

        /*
        * Sets up a SwipeRefreshLayout.OnRefreshListener that is invoked when the user
        * performs a swipe-to-refresh gesture.
        */
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                                                    @Override
                                                    public void onRefresh() {
                                                        Log.i(LOG_TAG, "onRefresh called from SwipeRefreshLayout");
                                                        fetchFirelineJSON(false);
                                                        swipeRefreshLayout.setRefreshing(false);
                                                    }
                                                }
        );

        fetchFirelineJSON(true);
    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        switch (item.getItemId()) {
            // Check if user triggered a refresh:
            case R.id.action_refresh:
                fetchFirelineJSON(true);
                return true;

            case R.id.action_settings:
                Log.i(LOG_TAG, "settings");
                return true;

            case R.id.action_about:
                new LibsBuilder()
                        //provide a style (optional) (LIGHT, DARK, LIGHT_DARK_TOOLBAR)
                        .withActivityStyle(Libs.ActivityStyle.LIGHT_DARK_TOOLBAR)
                        .withAboutIconShown(true)
                        .withAboutVersionShown(true)
                        .withAboutDescription(getString(R.string.string_about))
                        //start the activity
                        .start(this);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void fetchFirelineJSON(boolean showDialog) {
        /**
         * Checking Internet Connection
         */
        if (InternetConnection.checkConnection(getApplicationContext())) {
            /**
             * Progress Dialog for User Interaction
             */
            final ProgressDialog dialog = new ProgressDialog(MainActivity.this);
            if (showDialog) {
                dialog.setTitle(getString(R.string.string_getting_json_title));
                dialog.setMessage(getString(R.string.string_getting_json_message));
                dialog.show();
            }

            //Creating an object of our api interface
            ApiService api = RetroClient.getApiService();

            /**
             * Calling JSON
             */
            Call<ArrayList<Incident>> call = api.getFirelineJSON();

            /**
             * Enqueue Callback will be call when get response...
             */
            call.enqueue(new Callback<ArrayList<Incident>>() {
                @Override
                public void onResponse(Call<ArrayList<Incident>> call, Response<ArrayList<Incident>> response) {
                    //Dismiss Dialog
                    if (dialog.isShowing()) {
                        dialog.dismiss();
                    }
                    if (response.isSuccessful()) {
                        /**
                         * Got Successfully
                         */
                        incidentList = response.body();

                        /**
                         * Binding that List to Adapter
                         */
                        adapter = new IncidentAdapter(MainActivity.this, incidentList);
                        listView.setAdapter(adapter);

                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd - HH:mm:ss");
                        String currentDateandTime = sdf.format(new Date());
                        textViewLastUpdate.setText(currentDateandTime);

                    } else {
                        Snackbar.make(parentView, R.string.string_some_thing_wrong, Snackbar.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onFailure(Call<ArrayList<Incident>> call, Throwable t) {
                    if (dialog.isShowing()) {
                        dialog.dismiss();
                    }
                    FirebaseCrash.logcat(Log.ERROR, LOG_TAG, "Failed to get fireline json");
                    FirebaseCrash.report(t);
                }
            });

        } else {
            Snackbar.make(parentView, R.string.string_internet_connection_not_available, Snackbar.LENGTH_LONG).show();
        }
    }
}