package nl.teumaas.nasaapp.controllers;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Spinner;

import java.util.ArrayList;

import nl.teumaas.nasaapp.R;
import nl.teumaas.nasaapp.database.PhotoDatabase;
import nl.teumaas.nasaapp.domain.Photo;
import nl.teumaas.nasaapp.utilities.PhotoAdapter;

public class PhotoSavedActivity extends AppCompatActivity {

    private static final String TAG = PhotoSavedActivity.class.getSimpleName();

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ArrayList<Photo> mPhotoList;
    private Spinner mSpinner;

    private PhotoDatabase personDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate aangeroepen.");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        personDb = new PhotoDatabase(this);

        setTitle("NasaApp - Saved photos");

        // Create a list of dummy mPhotoList to fill our RecyclerView.
        // These will be fetched from a web service later.
        mPhotoList = personDb.getAllPhotos();
        mSpinner = (Spinner) findViewById(R.id.spinner);
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mSpinner.setVisibility(View.GONE);
        mRecyclerView.setHasFixedSize(true);

        // use a LinearLayout manager
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        // specify an Adapter (see also next example)
        mAdapter = new PhotoAdapter(this, mPhotoList);
        mRecyclerView.setAdapter(mAdapter);
    }
}
