/*
 * Mike Ford and Ian Skyles
 * TCSS450 – Spring 2016
 * Recipe Project
 */
package team14.tacoma.uw.edu.husky_cooking.authenticate;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import team14.tacoma.uw.edu.husky_cooking.R;

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

import java.util.Arrays;

/**
 * This class controls the user logging into our application.
 * It interfaces with our database hosted on CSSGATE.
 * It is the class that is used to display the sign in
 * forms dispalyed on the app.
 *
 * @author Mike Ford
 * @author Ian Skyles
 * @version 5/2/2016
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
    private String Name;

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
        View v = inflater.inflate(R.layout.fragment_log_in, container, false);
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
            @Override
            public void onSuccess(LoginResult loginResult) {
                AccessToken accessToken = loginResult.getAccessToken();
                Profile profile = Profile.getCurrentProfile();



                // Facebook Email address
                GraphRequest request = GraphRequest.newMeRequest(
                        accessToken,
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
                                    Toast.makeText(getActivity().getApplicationContext(), "Email " + FEmail, Toast.LENGTH_LONG).show();


                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,name,email,gender, birthday");
                request.setParameters(parameters);
                request.executeAsync();


            }

            @Override
            public void onCancel() {
                // App code
            }

            @Override
            public void onError(FacebookException exception) {
                // App code
            }
        });


        Button signInButton = (Button) v.findViewById(R.id.signin_button);
        signInButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                String userId = mUserName.getText().toString();
                String pwd = mPwd.getText().toString();
                if(TextUtils.isEmpty(userId)){
                    Toast.makeText(v.getContext(), "Enter a userid",
                            Toast.LENGTH_SHORT).show();
                    mUserName.requestFocus();
                    return;
                }
                if(!userId.contains("@")){
                    Toast.makeText(v.getContext(), "Enter a valid email address",
                            Toast.LENGTH_SHORT).show();
                    mUserName.requestFocus();
                    return;
                }
                if(TextUtils.isEmpty(pwd)){
                    Toast.makeText(v.getContext(), "Enter a password",
                            Toast.LENGTH_SHORT).show();
                    mPwd.requestFocus();
                    return;
                }
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
        signUp.setOnClickListener(new View.OnClickListener() {
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

}
