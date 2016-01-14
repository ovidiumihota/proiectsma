package ro.project.noname.myapplication;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import com.facebook.AccessToken;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ro.project.noname.myapplication.domain.Photo;

public class PhotosActivity extends AppCompatActivity {

    private List<Photo> photos = new ArrayList<>();
    private PhotosAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_photos);


        Bundle albumParameters = new Bundle();
        albumParameters.putString("fields", "name, id");

        GraphRequest albumGraphRequest = new GraphRequest(
                AccessToken.getCurrentAccessToken(),
                "/me/albums",
                albumParameters,
                HttpMethod.GET,
                new GraphRequest.Callback() {
                    public void onCompleted(GraphResponse response) {
                        System.out.println(response);
                        if (response != null) {
                            JSONObject responseJSONObject = response.getJSONObject();
                            try {
                                JSONArray dataJsonArray = new JSONArray(responseJSONObject.getString("data"));
                                System.out.println(dataJsonArray);
                                for (int i = 0; i < dataJsonArray.length(); i++) {
                                    if (dataJsonArray.getJSONObject(i).getString("name").equals("Profile Pictures")) {
                                        getPhotosByAlbumId(dataJsonArray.getJSONObject(i).getString("id"));
                                    }

                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                }
        );

        albumGraphRequest.executeAsync();

        ListView listView = (ListView) findViewById(R.id.photo_list);
        adapter = new PhotosAdapter(PhotosActivity.this, photos);
        listView.setAdapter(adapter);
    }


    private void getPhotosByAlbumId(String id) {

        Bundle albumParameters = new Bundle();
        albumParameters.putString("fields", "id");
        albumParameters.putString("limit", "100");

        GraphRequest albumGraphRequest = new GraphRequest(
                AccessToken.getCurrentAccessToken(),
                "/" + id + "/photos",
                albumParameters,
                HttpMethod.GET,
                new GraphRequest.Callback() {
                    public void onCompleted(GraphResponse response) {
                        System.out.println(response);
                        if (response != null) {
                            JSONObject responseJSONObject = response.getJSONObject();
                            try {
                                JSONArray dataJsonArray = new JSONArray(responseJSONObject.getString("data"));
                                for (int i = 0; i < dataJsonArray.length(); i++) {
                                    getPhotoById(dataJsonArray.getJSONObject(i).getString("id"), i);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    }
                }
        );

        albumGraphRequest.executeAsync();
    }

    private void getPhotoById(String id, final int index) {
        photos.add(index, new Photo(id));
        getLikes(id, index);
        getUrl(id, index);
    }

    private void getUrl(final String id, final int index) {
        Bundle albumParameters = new Bundle();
        albumParameters.putString("fields", "url");
        albumParameters.putBoolean("redirect", false);

        GraphRequest albumGraphRequest = new GraphRequest(
                AccessToken.getCurrentAccessToken(),
                id + "/picture",
                albumParameters,
                HttpMethod.GET,
                new GraphRequest.Callback() {
                    public void onCompleted(GraphResponse response) {
                        System.out.println(response);
                        if (response != null) {

                            try {
                                String picUrlString = (String) response.getJSONObject().getJSONObject("data").get("url");
                                Photo photo = photos.get(index);
                                photo.setUrl(picUrlString);
                                photos.set(index, photo);
                                adapter.notifyDataSetChanged();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    }
                }
        );

        albumGraphRequest.executeAsync();
    }

    private void getLikes(final String id, final int index) {
        Bundle albumParameters = new Bundle();
        albumParameters.putString("fields", "id");
        albumParameters.putString("summary", "true");

        GraphRequest albumGraphRequest = new GraphRequest(
                AccessToken.getCurrentAccessToken(),
                id + "/likes",
                albumParameters,
                HttpMethod.GET,
                new GraphRequest.Callback() {
                    public void onCompleted(GraphResponse response) {
                        System.out.println(response);
                        if (response != null) {
                            JSONObject responseJSONObject = response.getJSONObject();
                            try {
                                JSONObject summaryJsonObject = new JSONObject(responseJSONObject.getString("summary"));
                                Photo photo = photos.get(index);
                                photo.setTotalLikes(summaryJsonObject.getInt("total_count"));
                                photos.set(index, photo);
                                adapter.notifyDataSetChanged();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    }
                }
        );

        albumGraphRequest.executeAsync();
    }

}
