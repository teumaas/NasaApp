package nl.teumaas.nasaapp.api;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

import nl.teumaas.nasaapp.api.interfaces.OnPhotoAvailable;
import nl.teumaas.nasaapp.domain.Photo;

/**
 * Created by tmsmi on 13-3-2018.
 */

public class PhotoTask extends AsyncTask<String, Void, String> {

    // Callback
    private OnPhotoAvailable listener = null;

    // Statics
    private static final String TAG = PhotoTask.class.getSimpleName();

    // Constructor, set listener
    public PhotoTask(OnPhotoAvailable listener) {
        this.listener = listener;
    }

    /**
     * doInBackground is de methode waarin de aanroep naar een service op
     * het Internet gedaan wordt.
     *
     * @param params
     * @return
     */

    @Override
    protected String doInBackground(String... params) {

        InputStream inputStream = null;
        int responsCode = -1;
        // De URL die we via de .execute() meegeleverd krijgen
        String photoUrl = params[0];
        // Het resultaat dat we gaan retourneren
        String response = "";

        Log.i(TAG, "doInBackground - " + photoUrl);
        try {
            // Maak een URL object
            URL url = new URL(photoUrl);
            // Open een connection op de URL
            URLConnection urlConnection = url.openConnection();

            if (!(urlConnection instanceof HttpURLConnection)) {
                return null;
            }

            // Initialiseer een HTTP connectie
            HttpURLConnection httpConnection = (HttpURLConnection) urlConnection;
            httpConnection.setAllowUserInteraction(false);
            httpConnection.setInstanceFollowRedirects(true);
            httpConnection.setRequestMethod("GET");

            // Voer het request uit via de HTTP connectie op de URL
            httpConnection.connect();

            // Kijk of het gelukt is door de response code te checken
            responsCode = httpConnection.getResponseCode();
            if (responsCode == HttpURLConnection.HTTP_OK) {
                inputStream = httpConnection.getInputStream();
                response = getStringFromInputStream(inputStream);
                // Log.i(TAG, "doInBackground response = " + response);
            } else {
                Log.e(TAG, "Error, invalid response");
            }
        } catch (MalformedURLException e) {
            Log.e(TAG, "doInBackground MalformedURLEx " + e.getLocalizedMessage());
            return null;
        } catch (IOException e) {
            Log.e("TAG", "doInBackground IOException " + e.getLocalizedMessage());
            return null;
        }

        // Hier eindigt deze methode.
        // Het resultaat gaat naar de onPostExecute methode.
        return response;
    }

    /**
     * onPostExecute verwerkt het resultaat uit de doInBackground methode.
     *
     * @param response
     */
    protected void onPostExecute(String response) {
        Log.i(TAG, "onPostExecute " + response);

        // Check of er een response is
        if(response == null || response == "") {
            Log.e(TAG, "onPostExecute kreeg een lege response!");
            return;
        }


        // Het resultaat is in ons geval een stuk tekst in JSON formaat.
        // Daar moeten we de info die we willen tonen uit filteren (parsen).
        // Dat kan met een JSONObject.
        JSONObject jsonObject;
        try {
            // Top level json object
            jsonObject = new JSONObject(response);

            // Get all users and start looping
            JSONArray photosArray = jsonObject.getJSONArray("photos");
            for(int idx = 0; idx < photosArray.length(); idx++) {
                // array level objects and get photos
                JSONObject photoObject = photosArray.getJSONObject(idx);

                // Get id, camera, en verkrijgt rover array ( name, max_date, total_photos
                String id = photoObject.getString("id");
                String cameraName =  photoObject.getJSONObject("camera").getString("full_name");
                String robotName = photoObject.getJSONObject("rover").getString("name");
                String imageDate = photoObject.getJSONObject("rover").getString("max_date");
                String imageTotal = photoObject.getJSONObject("rover").getString("total_photos");

                Log.i(TAG, "Got photo " + id + " " + cameraName);

                // Get image url
                String imageURL = photoObject.getString("img_src");
                Log.i(TAG, imageURL);

                // Create new Product object
                // Builder Design Pattern
                Photo photo = new Photo.PhotoBuilder(id, cameraName, robotName, imageDate, imageTotal, imageURL)
                        .setID(id)
                        .setCameraName(cameraName)
                        .setRobotName(robotName)
                        .setImageURL(imageURL)
                        .setImageInformation(imageDate, imageTotal)
                        .build();

                //
                // call back with new Photo data
                //
                listener.OnPhotoAvailable(photo);
            }
        } catch( JSONException ex) {
            Log.e(TAG, "onPostExecute JSONException " + ex.getLocalizedMessage());
        }
    }

    //
    // convert InputStream to String
    //
    private static String getStringFromInputStream(InputStream is) {
        Log.d(TAG, "getStringFromInputStream aangeroepen.");

        BufferedReader br = null;
        StringBuilder sb = new StringBuilder();

        String line;
        try {

            br = new BufferedReader(new InputStreamReader(is));
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return sb.toString();
    }
}
