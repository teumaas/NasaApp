package nl.teumaas.nasaapp.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.PriorityQueue;

import nl.teumaas.nasaapp.domain.Photo;

/**
 * Created by tmsmi on 14-3-2018.
 */

public class PhotoDatabase extends SQLiteOpenHelper {

    private final static String TAG = "PhotoDatabase";
    private final static String DB_NAME = "Photo.db";
    private final static int DB_VERSION = 5;

    private final static String TABLE_NAME = "Photo";

    private final static String COLUM_ID = "id";
    private final static String COLUM_PHOTO_ID = "photo_id";
    private final static String COLUM_CAMERANAME = "cameraName";
    private final static String COLUM_ROBOTNAME = "robotName";
    private final static String COLUM_IMAGEDATE = "imageDate";
    private final static String COLUM_IMAGE_URL = "imgURL";
    private final static String COLUM_TOTAL_PICTURES = "total_photos";

    public PhotoDatabase(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    //Maakt de table en velden.
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        Log.d(TAG, "onCreate aangeroepen.");

        sqLiteDatabase.execSQL("CREATE TABLE " + TABLE_NAME + " (" +
                COLUM_ID + " INT PRIMARY KEY AUTOINCREMENT," +
                COLUM_PHOTO_ID + " TEXT NOT NULL," +
                COLUM_CAMERANAME + " TEXT NOT NULL," +
                COLUM_ROBOTNAME + " TEXT NOT NULL," +
                COLUM_IMAGEDATE + " TEXT NOT NULL," +
                COLUM_IMAGE_URL + " TEXT NOT NULL," +
                COLUM_TOTAL_PICTURES + " BOOL NOT NULL" +
                ");");
    }

    //Bij een nieuw versienummer wordt dit weergegeven.
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        Log.d(TAG, "onUpgrade aangeroepen.");

        // Drop de oude database tabel.
        String query = "DROP TABLE " + TABLE_NAME + ";";
        sqLiteDatabase.execSQL(query);

        // Maak de database tabel opnieuw.
        onCreate(sqLiteDatabase);
    }

    // ID worden in ContentValues geplaatst.
    public void addPhoto(Photo photo) {
        Log.d(TAG, "addPhoto aangeroepen.");

        ContentValues values = new ContentValues();
        values.put(COLUM_PHOTO_ID, photo.ID);
        values.put(COLUM_TOTAL_PICTURES, photo.totalPhotos);
        values.put(COLUM_CAMERANAME, photo.cameraName);
        values.put(COLUM_ROBOTNAME, photo.robotName);
        values.put(COLUM_IMAGEDATE, photo.photoDate);
        values.put(COLUM_IMAGE_URL, photo.imgURL);

        SQLiteDatabase db = this.getWritableDatabase();
        Long id = db.insert(TABLE_NAME, null, values);
        db.close();
    }

    //Geeft alle photos en zet deze in photos ArrayList.
    public ArrayList<Photo> getAllPhotos() {
        Log.d(TAG, "getAllPhotos aangeroepen.");

        ArrayList<Photo> photos = new ArrayList<>();

        String query = "SELECT * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {

            String id = cursor.getString(cursor.getColumnIndex(COLUM_PHOTO_ID));
            String cameraName = cursor.getString(cursor.getColumnIndex(COLUM_CAMERANAME));
            String robotName = cursor.getString(cursor.getColumnIndex(COLUM_ROBOTNAME));
            String photoDate = cursor.getString(cursor.getColumnIndex(COLUM_IMAGEDATE));
            String imgURL = cursor.getString(cursor.getColumnIndex(COLUM_IMAGE_URL));
            String totalPhotos = cursor.getString(cursor.getColumnIndex(COLUM_TOTAL_PICTURES));

            Photo newPhoto = new Photo(id, cameraName, robotName, photoDate, totalPhotos, imgURL);
            photos.add(newPhoto);
            cursor.moveToNext();
        }

        return photos;
    }
}
