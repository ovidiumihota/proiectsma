package ro.project.noname.myapplication;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ro.project.noname.myapplication.domain.Photo;

/**
 * Created by ovidiumihota on 13/01/16.
 */
public class PhotosAdapter extends BaseAdapter {

    private Context context;
    private List<Photo> photos = new ArrayList<>();

    public PhotosAdapter(Context context, List<Photo> photos) {
        this.context = context;
        this.photos = photos;
    }

    @Override
    public int getCount() {
        return photos.size();
    }

    @Override
    public Object getItem(int position) {
        return photos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int pos, View convertView, ViewGroup parent) {
        View rowView = convertView;
        PhotoHolder photoHolder;
        if (rowView == null) {
            rowView = ((Activity) context).getLayoutInflater().inflate(R.layout.photo_view, null);
            photoHolder = new PhotoHolder(rowView);
            rowView.setTag(photoHolder);
        }
        photoHolder = (PhotoHolder) rowView.getTag();

        Photo photo = photos.get(pos);
        if (photo != null) {
            photoHolder.totalLikesTextView.setText(String.valueOf(photo.getTotalLikes()));
            Picasso.with(context)
                    .load(photo.getUrl())
                    .into(photoHolder.photoImageView);
        }
        return rowView;
    }


    class PhotoHolder {
        public TextView totalLikesTextView;
        public ImageView photoImageView;

        public PhotoHolder(View view) {
            totalLikesTextView = (TextView) view.findViewById(R.id.totalLikesTextView);
            photoImageView = (ImageView) view.findViewById(R.id.photoImageView);
        }
    }

}
