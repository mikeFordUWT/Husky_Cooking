/*
 * Mike Ford and Ian Skyles
 * TCSS450 – Spring 2016
 * Recipe Project
 */
package team14.tacoma.uw.edu.husky_cooking.authenticate;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;

import team14.tacoma.uw.edu.husky_cooking.R;
import team14.tacoma.uw.edu.husky_cooking.recipe.RecipeActivity;

/**
 * This class controls the user logging into our application.
 * It interfaces with our database hosted on CSSGATE.
 * It is the class that is used to display the sign in
 * forms dispalyed on the app.
 *
 * @author Mike Ford
 * @author Ian Skyles
 * @version 6/3/2016
 */
public class LogInFragment extends Fragment {
    /** CSSGATE login url */
    private static final String LOGIN_URL =
            "http://cssgate.insttech.washington.edu/~_450atm14/husky_cooking/login.php?";


    /** CSSGATE user add url url */
    private static final String USER_ADD_URL =
            "http://cssgate.insttech.washington.edu/~_450atm14/husky_cooking/addUser.php?";
    /** A tag for debugging */
    private static final String TAG = "LogInFragment";
    /** A tag for succesful debugging */
    private static final String TAG_SUCCESS = "success";
    /** A tag for debugging */
    private static final String TAG_MESSAGE = "message";
    /**Facebook login button*/
    private LoginButton loginButton;

    /**Facebook call back manager*/
    private CallbackManager callbackManager = null;
    /**Facebook access token tracker*/
    private AccessTokenTracker mtracker = null;
    /**Facebook profile tracker*/
    private ProfileTracker mprofileTracker = null;
    /**
     * Holds the facebook user's name.
     */
    private String Name;

    /**
     * String for holding facebook email.
     */
    private String FEmail;

    /**
     * Edit Text for allowing entry by user of username and password.
     */
    private EditText mUserName, mPwd,mRegisterEmail, mRegisterPassword;


    /**
     * Required empty public constructor
     */
    public LogInFragment() {
        // Required empty public constructor
    }


    /**
     * Saves instance on creation of method of fragment/app.
     * @param savedInstanceState state of the instance to be saved
     */
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        callbackManager = CallbackManager.Factory.create();
        FacebookSdk.sdkInitialize(getActivity().getApplicationContext());


        mtracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken) {

                Log.v("AccessTokenTracker", "oldAccessToken=" + oldAccessToken + "||" + "CurrentAccessToken" + currentAccessToken);
            }
        };


        mprofileTracker = new ProfileTracker() {
            @Override
            protected void onCurrentProfileChanged(Profile oldProfile, Profile currentProfile) {

                Log.v("Session Tracker", "oldProfile=" + oldProfile + "||" + "currentProfile" + currentProfile);
                //pass profile to a another activity/frag here


            }
        };

        mtracker.startTracking();
        mprofileTracker.startTracking();
    }


    /**
     * Creates the view that will be shown to the user.
     * Attaches listeners to the buttons defined in the XML.
     * Gives users toasts to notify them of various things such has
     * them entering no user/pass or an invalid one.
     * @param inflater instantiate layout XML file into its corresponding View object
     * @param container item to contain other views
     * @param savedInstanceState save state so we can resume later
     * @return The view (user interface)
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        FacebookSdk.sdkInitialize(this.getContext());

        final View v = inflater.inflate(R.layout.fragment_log_in, container, false);
        mUserName = (EditText) v.findViewById(R.id.user_id);
        mPwd = (EditText) v.findViewById(R.id.pwd);
        loginButton = (LoginButton) v.findViewById(R.id.login_button);
        loginButton.setReadPermissions(Arrays.asList(
                "public_profile", "email", "user_birthday", "user_friends"));
        // If using in a fragment
        loginButton.setFragment(this);
        // Other app specific specialization

        // Callback registration
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            /**
             * This method handles a succesful logon with the facebook button
             * @param loginResult
             */
            @Override
            public void onSuccess(LoginResult loginResult) {
                final SharedPreferences sharedPreferences = getActivity().getSharedPreferences(getString(R.string.LOGIN_PREFS)
                        , Context.MODE_PRIVATE);

                final String hello;
                //get profile info and access token from fb
                AccessToken accessToken = loginResult.getAccessToken();
                Profile profile = Profile.getCurrentProfile();



                // Get Facebook Email address and name
                GraphRequest request = GraphRequest.newMeRequest(
                        accessToken,

                        /**
                         * This handles making a request to facebook and parsing for user info.
                         */
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(
                                    JSONObject object,
                                    GraphResponse response) {
                                Log.v("LoginActivity Response ", response.toString());

                                try {
                                    Name = object.getString("name");

                                    FEmail = object.getString("email");
                                    Log.v("Email = ", " " + FEmail);

                                    sharedPreferences.edit()
                                            .putString(getString(R.string.LOGGED_USER), FEmail).apply();

                                    sharedPreferences.edit().putBoolean(getString(R.string.LOGGEDIN)
                                            ,true).apply();
                                    sharedPreferences.edit()
                                            .putString(getString(R.string.LOGIN_METHOD),
                                                    getString(R.string.FACE)).apply();
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,name,email,gender, birthday");
                request.setParameters(parameters);
                request.executeAsync();



                Intent i  = new Intent(getActivity(), RecipeActivity.class);
                startActivity(i);

                getActivity().finish();
            }

            /**
             * What happens if user hits the cancel button after clicking fb login.
             */
            @Override
            public void onCancel() {
                // App code
            }

            /**
             * What happens if fb login produces an exception.
             * It deafults by telling the user they entered wrong information.
             */
            @Override
            public void onError(FacebookException exception) {
                // App code
            }
        });


        Button signInButton = (Button) v.findViewById(R.id.signin_button);
        /**
         * This on click listener handles the sign in button response.
         */
        signInButton.setOnClickListener(new View.OnClickListener(){
            /**
             * Controls what happens when the user clicks the button.
             * @param v
             */
            @Override
            public void onClick(View v){
                String userId = mUserName.getText().toString();
                String pwd = mPwd.getText().toString();
                //require some text for user id.
                if(TextUtils.isEmpty(userId)){
                    Toast.makeText(v.getContext(), "Enter a userid",
                            Toast.LENGTH_SHORT).show();
                    mUserName.requestFocus();
                    return;
                }
                //require @ sign for user id
                if(!userId.contains("@")){
                    Toast.makeText(v.getContext(), "Enter a valid email address",
                            Toast.LENGTH_SHORT).show();
                    mUserName.requestFocus();
                    return;
                }
                //require a password for  user account to sign in
                if(TextUtils.isEmpty(pwd)){
                    Toast.makeText(v.getContext(), "Enter a password",
                            Toast.LENGTH_SHORT).show();
                    mPwd.requestFocus();
                    return;
                }
                //require password length over 6
                if(pwd.length() < 6){
                    Toast.makeText(v.getContext(), "Enter a password of at least 6 characters"
                            , Toast.LENGTH_SHORT)
                            .show();
                    mPwd.requestFocus();
                    return;
                }
                String url = buildLogInURL(v);
                ((SignInActivity) getActivity()).login(url, userId);

            }
        });

        Button signUp = (Button) v.findViewById(R.id.sign_up_button);
        /**
         * Handles the sign up button events.
         */
        signUp.setOnClickListener(new View.OnClickListener() {
            /**
             * Handles clicks on the button
             * @param v
             */
            @Override
            public void onClick(View v) {
                RegisterUserFragment regUser = new RegisterUserFragment();
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container,regUser)
                        .addToBackStack(null)
                        .commit();
            }
        });

        mRegisterEmail = (EditText) v.findViewById(R.id.new_user_email);
        mRegisterPassword = (EditText) v.findViewById(R.id.new_user_password);


        return v;
    }

    /**
     * forward the login results to the callbackManager created in onCreate():
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * An interface which requires implementing login and
     * signup (both accepting Strings).
     */
    public interface LoginInteractionListener {
        void login(String url, String user);
    }

    /**
     * This builds a url for user login based on email and password
     * @param v the view to display error toasts in
     * @return String of the url for logging in
     */
    private String buildLogInURL(View v){
        StringBuilder sb = new StringBuilder(LOGIN_URL);
        try{
            String email = mUserName.getText().toString();
            sb.append("email=");
            sb.append(email);

            String pwd = mPwd.getText().toString();
            sb.append("&pwd=");
            sb.append(pwd);

            Log.e("LogInFragment", sb.toString());
        }catch (Exception e) {
            Toast.makeText(v.getContext(), "Something wrong with the url " + e.getMessage(),
                    Toast.LENGTH_LONG).show();
        }
        return sb.toString();
    }


    /**
     * Checks if facebook user is in our facebook database.
     * Controls response if they are or aren't in it.
     */
    private class FacebookCheck extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute(){
            super.onPreExecute();

        }

        /**
         * Finds out if the user is in the database
         * @param urls A url to run in the background
         * @return repsonse string
         */
        @Override
        protected String doInBackground(String... urls){
            String response = "";
            HttpURLConnection urlConnection = null;
            for(String url:urls){
                try {
                    URL urlObject = new URL(url);
                    urlConnection = (HttpURLConnection) urlObject.openConnection();
                    InputStream content = urlConnection.getInputStream();
                    BufferedReader buffer = new BufferedReader(new InputStreamReader(content));
                    String s ="";
                    while((s=buffer.readLine())!=null){
                        response +=s;
                    }
                }catch (Exception e){
                    response = "Unable to login, Reason: " + e.getMessage();
                }finally{
                    if(urlConnection !=null){
                        urlConnection.disconnect();
                    }
                }
            }
            return response;
        }

        /**
         * Checks the String returned from doInBackground to see if the log in was successful.
         * @param result
         */
        @Override
        protected void onPostExecute(String result) {

            if(result.startsWith("Unable to")){
                Toast.makeText(getActivity().getApplicationContext(), result, Toast.LENGTH_LONG)
                        .show();
                return;
            }

            if(result!=null){
                Log.e("SignInActivity", result.toString());
            }
        }
    }

}
