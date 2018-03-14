package nl.teumaas.nasaapp.controllers;

import android.content.Intent;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.util.ArrayList;

import nl.teumaas.nasaapp.R;
import nl.teumaas.nasaapp.api.PhotoTask;
import nl.teumaas.nasaapp.api.interfaces.OnPhotoAvailable;
import nl.teumaas.nasaapp.database.PhotoDatabase;
import nl.teumaas.nasaapp.domain.Photo;
import nl.teumaas.nasaapp.utilities.PhotoAdapter;
import nl.teumaas.nasaapp.utilities.Tags;

import static android.support.v4.view.MenuItemCompat.getActionView;

public class MainActivity extends AppCompatActivity
        implements OnPhotoAvailable, AdapterView.OnItemSelectedListener {

    private static final String TAG = MainActivity.class.getSimpleName();

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ArrayList<Photo> mPhotoList;
    private Spinner mSpinner;

    private static final String[]mPaths = {"Select camera...", "fhaz", "rhaz", "mast", "chemcam", "mahli", "navcam"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate aangeroepen.");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Create a list of dummy mPhotoList to fill our RecyclerView.
        // These will be fetched from a web service later.
        mPhotoList = new ArrayList<>();

        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        executeGetRequest(null, Tags.SOL_AMOUNT);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        // specify an adapter (see also next example)
        mAdapter = new PhotoAdapter(this, mPhotoList);
        mRecyclerView.setAdapter(mAdapter);

        mSpinner = (Spinner)findViewById(R.id.spinner);
        ArrayAdapter<String>adapter = new ArrayAdapter<String>(MainActivity.this,
                android.R.layout.simple_spinner_item,mPaths);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinner.setAdapter(adapter);
        mSpinner.setOnItemSelectedListener(this);
    }

    //Checkt of de PhotoList is ingeladen
    @Override
    public void OnPhotoAvailable(Photo photo) {
        Log.d(TAG, "OnPhotoAvailable aangeroepen.");

        mPhotoList.add(photo);
        mAdapter.notifyDataSetChanged();
    }

    //Losse methode die hoort bij de spinner hier hoeft niks in alleen moet wel bestaan.
    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
    }

    //Zet op basis van de Spinner status de juiste request.
    public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {
        Log.d(TAG, "onItemSelected aangeroepen.");

        switch (position) {
            case 0:
                break;
            case 1:
                executeGetRequest(mPaths[1].toString(), Tags.SOL_AMOUNT);
                break;
            case 2:
                executeGetRequest(mPaths[2].toString(), Tags.SOL_AMOUNT);
                break;
            case 3:
                executeGetRequest(mPaths[3].toString(), Tags.SOL_AMOUNT);
                break;
            case 4:
                executeGetRequest(mPaths[4].toString(), Tags.SOL_AMOUNT);
                break;
            case 5:
                executeGetRequest(mPaths[5].toString(), Tags.SOL_AMOUNT);
                break;
            case 6:
                executeGetRequest(mPaths[6].toString(), Tags.SOL_AMOUNT);
                break;
        }
    }

    //De uitvoer van de request naar de API met het camerapunt en de aantal photos.
    private void executeGetRequest(String cameraPoint, int solAmount) {
        Log.d(TAG, "executeGetRequest aangeroepen.");

        String camera = cameraPoint;
        int amount = solAmount;
        String[] params = null;

        if (camera == null) {
            params = new String[]{"https://api.nasa.gov/mars-photos/api/v1/rovers/curiosity/photos?sol="+ amount +"&api_key=" + Tags.API_KEY};
        } else {
            params = new String[]{"https://api.nasa.gov/mars-photos/api/v1/rovers/curiosity/photos?sol="+ amount +"&camera="+ camera + "&api_key=" + Tags.API_KEY};
        }

        PhotoTask photoTask = new PhotoTask(this);
        mPhotoList.clear();
        photoTask.execute(params);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Log.d(TAG, "onCreateOptionsMenu aangeroepen.");
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_items, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.d(TAG, "onOptionsItemSelected aangeroepen.");
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_saved) {
            Intent settingsIntent = new Intent(getApplicationContext(), PhotoSavedActivity.class);
            startActivity(settingsIntent);
        }

        return super.onOptionsItemSelected(item);
    }
}
