package ro.project.noname.myapplication;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.login.LoginManager;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by ovidiumihota on 29/12/15.
 */
public class MainActivity extends Activity {

    private TextView displayName;
    private ImageView profilePicture;
    private ImageView profilePictureLoader;

    private ProgressDialog mProgressDialog;

    private String profilePicUrl;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_main);

        displayName = (TextView) findViewById(R.id.displayName);

        GraphRequest request = GraphRequest.newMeRequest(AccessToken.getCurrentAccessToken(),
                new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject json, GraphResponse response) {
                        if (response != null) {
                            try {
                                JSONObject data = response.getJSONObject();
                                if (data.has("id")) {
                                    String profilePicUrl = "http://graph.facebook.com/" + data.getString("id") + "/picture?type=large";
                                    loadImageInBackground(profilePicUrl);
                                }
                                if (data.has("name")) {

                                    String name = data.getString("name");
                                    displayName.setText(name);
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,name,email,picture,photos");
        request.setParameters(parameters);
        request.executeAsync();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_login, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case (R.id.action_logout):
                LoginManager.getInstance().logOut();
                this.startActivity(new Intent(this, LoginActivity.class));

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void loadImageInBackground(String profilePicUrl) {

        mProgressDialog = new ProgressDialog(MainActivity.this);
        mProgressDialog.setMessage("Chargement...");
        mProgressDialog.setIndeterminate(false);

        profilePicture = (ImageView) findViewById(R.id.profile_picture);


        Target target = new Target() {

            @Override
            public void onPrepareLoad(Drawable arg0) {

                mProgressDialog.show();
            }

            @Override
            public void onBitmapLoaded(Bitmap arg0, Picasso.LoadedFrom arg1) {

                profilePictureLoader.setImageBitmap(arg0);
                mProgressDialog.dismiss();
            }

            @Override
            public void onBitmapFailed(Drawable arg0) {
                // TODO Auto-generated method stub
                mProgressDialog.dismiss();
            }
        };

        Picasso.with(MainActivity.this)
                .load(profilePicUrl)
                .into(profilePicture);
    }
}
