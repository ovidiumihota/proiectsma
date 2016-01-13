package ro.project.noname.myapplication;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.HashMap;
import java.util.Map;

import ro.project.noname.myapplication.domain.Photo;

/**
 * Created by ovidiumihota on 13/01/16.
 */
public class HashMapAdapter extends BaseAdapter {

    private Map<String, Photo> mData = new HashMap<String, Photo>();
    private String[] mKeys;

    private ImageView photoView;
    private ImageView photoGenerator;

    private Context context;

    private ProgressDialog mProgressDialog;

    public HashMapAdapter(Context context,Map<String, Photo> data) {
        mData = data;
        mKeys = mData.keySet().toArray(new String[data.size()]);
        this.context = context;
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Object getItem(int position) {
        return mData.get(mKeys[position]).getUrl();
    }

    @Override
    public long getItemId(int arg0) {
        return arg0;
    }

    @Override
    public View getView(int pos, View convertView, ViewGroup parent) {
        String key = mKeys[pos];
        String value = getItem(pos).toString();

        final View result;

        if (convertView == null) {
            result = LayoutInflater.from(parent.getContext()).inflate(R.layout.photo_view, parent, false);
        } else {
            result = convertView;
        }

        mProgressDialog = new ProgressDialog(context);
        mProgressDialog.setMessage("Chargement...");
        mProgressDialog.setIndeterminate(false);

        photoView = (ImageView) result.findViewById(R.id.profile_picture);


        Target target = new Target() {

            @Override
            public void onPrepareLoad(Drawable arg0) {

                mProgressDialog.show();
            }

            @Override
            public void onBitmapLoaded(Bitmap arg0, Picasso.LoadedFrom arg1) {

                photoGenerator.setImageBitmap(arg0);
                mProgressDialog.dismiss();
            }

            @Override
            public void onBitmapFailed(Drawable arg0) {
                // TODO Auto-generated method stub
                mProgressDialog.dismiss();
            }
        };

        Picasso.with(context)
                .load(value)
                .into(photoView);

        return result;
    }

}
