package nl.teumaas.nasaapp.controllers;

import android.nfc.Tag;
import android.os.TestLooperManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import es.dmoral.toasty.Toasty;
import nl.teumaas.nasaapp.R;
import nl.teumaas.nasaapp.database.PhotoDatabase;
import nl.teumaas.nasaapp.domain.Photo;
import nl.teumaas.nasaapp.utilities.Tags;

public class PhotoDetailActivity extends AppCompatActivity
        implements View.OnClickListener {

    private static final String TAG = PhotoSavedActivity.class.getSimpleName();

    private PhotoDatabase photoDB;
    private Photo mPhoto;
    private String mID;
    private String mTitle;
    private String mImageURL;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate aangeroepen.");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_detail);

        Bundle extras = getIntent().getExtras();

        mPhoto = (Photo) extras.getSerializable(Tags.PHOTO_ID);

        TextView tvCameraName = (TextView) findViewById(R.id.TextViewCameraNameDetailed);
        ImageView imPhoto = (ImageView) findViewById(R.id.ImageViewPhotoDetailed);
        setTitle(mPhoto.ID);

        // ImageView uit scherm koppelen

        tvCameraName.setText(mPhoto.cameraName);

        Picasso.with(this)
                .load( mPhoto.imgURL )
                .into( imPhoto );

        photoDB = new PhotoDatabase(this);

        Button saveImageBtn = (Button) findViewById(R.id.SaveImageBtn);
        saveImageBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        Log.d(TAG, "onClick aangeroepen.");
        //Custom toaster in gezet om een melding te geven dat de photo opgeslagen is.
        Toasty.normal(this, "Saved image!", getResources().getDrawable(R.drawable.ic_save_white_24dp)).show();

        Log.d(TAG, "onClick aangeroepen:");

        photoDB.addPhoto(mPhoto);
    }
}
