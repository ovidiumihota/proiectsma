package ro.project.noname.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.parse.GetCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

public class LoginActivity extends FragmentActivity {

    private LoginButton loginButton;
    private String userToken;
    private String currentUserID;
    private CallbackManager callbackManager;
    private AccessToken curretUserAccessToken;
    private AccessToken alreadyLoggedToken;
    private TextView loginFirstPlease;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();
        Parse.initialize(this, "5Z3ShmDZpdRdhoNZYchimLJozTC37BcsKTR8ljP2", "KUIpD2s7iONzSsiirNZUKNizgDHwj0u0fg9wpz4I");

        setContentView(R.layout.activity_login);

        loginButton = (LoginButton) findViewById(R.id.login_button);
        loginButton.setReadPermissions("public_profile email user_photos");
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {

                        userToken = loginResult.getAccessToken().getToken();
                        curretUserAccessToken = loginResult.getAccessToken();
                        currentUserID = loginResult.getAccessToken().getUserId();

                        ParseQuery<ParseObject> checkUser = ParseQuery.getQuery("FacebookUser");
                        checkUser.getInBackground(currentUserID, new GetCallback<ParseObject>() {
                            public void done(ParseObject object, ParseException e) {
                                if (object == null) {
                                    ParseObject addUser = new ParseObject("FacebookUser");
                                    addUser.put("facebookID", currentUserID);
                                    addUser.put("score", 0);
                                    addUser.saveInBackground();
                                }
                            }
                        });

                        loginFirstPlease = (TextView) findViewById(R.id.loginFirstPlease);
                        loginFirstPlease.setText(null);
                    }

                    @Override
                    public void onCancel() {

                    }

                    @Override
                    public void onError(FacebookException e) {
                        e.printStackTrace();
                    }
                }

        );

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    public void startGame(View view) {
        AccessToken alreadyLoggedToken = AccessToken.getCurrentAccessToken();
        if (curretUserAccessToken != null || alreadyLoggedToken != null) {
            Intent startGameActivity = new Intent(this, MainActivity.class);
            startActivity(startGameActivity);
        } else {
            loginFirstPlease = (TextView) findViewById(R.id.loginFirstPlease);
            loginFirstPlease.setText("You must login with Facebook in order to play");
        }
    }

    public void instructions(View view) {
        Intent instructionsActivity = new Intent(this, InstructionsActivity.class);
        startActivity(instructionsActivity);
    }

    public void myPictures(View view){
        Intent photosActivity = new Intent(this, PhotosActivity.class);
        startActivity(photosActivity);
    }
}
