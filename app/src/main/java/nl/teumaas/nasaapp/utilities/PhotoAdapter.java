package nl.teumaas.nasaapp.utilities;

import android.content.Context;
import android.content.Intent;
import android.nfc.Tag;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.ArrayList;

import nl.teumaas.nasaapp.R;
import nl.teumaas.nasaapp.controllers.PhotoDetailActivity;
import nl.teumaas.nasaapp.domain.Photo;

/**
 * Created by tmsmi on 13-3-2018.
 */

public class PhotoAdapter extends RecyclerView.Adapter<PhotoAdapter.ViewHolder> {

    private static final String TAG = PhotoAdapter.class.getSimpleName();

    private ArrayList<Photo> mDataset;
    private Context mContext;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        // The view (list_row_item) that contains the view items
        public View view;

        // The view items that our view displays
        public TextView mTextViewPhotoID;
        public ImageView mImageViewPhoto;

        public ViewHolder(View v) {
            super(v);
            this.view = v;
            this.view.setOnClickListener(this);

            mTextViewPhotoID = (TextView) v.findViewById(R.id.TextViewPhotoID);
            mImageViewPhoto = (ImageView) v.findViewById(R.id.ImageViewPhoto);
        }

        @Override
        public void onClick(View view) {
            Log.d(TAG, "onClick aangeroepen.");

            int position = getAdapterPosition();
            Photo mProduct = mDataset.get(position);

            Intent photoDetailIntent = new Intent(
                    view.getContext().getApplicationContext(),
                    PhotoDetailActivity.class);

            photoDetailIntent.putExtra(Tags.PHOTO_ID, mProduct);
            photoDetailIntent.putExtra(Tags.CAMERA_NAME, mProduct);
            photoDetailIntent.putExtra(Tags.PHOTO_IMAGE_URL, mProduct);

            view.getContext().startActivity(photoDetailIntent);
        }
    }

    // Provide a suitable constructor (depending on the kind of dataset)
    public PhotoAdapter(Context context, ArrayList<Photo> myDataset) {
        mContext = context;
        mDataset = myDataset;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public PhotoAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                        int viewType) {
        Log.d(TAG, "onCreateViewHolder aangeroepen.");
        // Create a new view.
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.list_row_item, parent, false);

        ViewHolder vh = new ViewHolder(view);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Log.d(TAG, "onBindViewHolder aangeroepen.");

        // - get element from your dataset at this position
        // - replace the contents of the view with that element

        Photo mPhoto = mDataset.get(position);
        holder.mTextViewPhotoID.setText(mPhoto.ID);

        Picasso.with(mContext)
                .load(mPhoto.imgURL)
                .into(holder.mImageViewPhoto);
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}
