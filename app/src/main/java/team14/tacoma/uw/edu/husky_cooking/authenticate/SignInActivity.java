package team14.tacoma.uw.edu.husky_cooking.authenticate;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import team14.tacoma.uw.edu.husky_cooking.R;
import team14.tacoma.uw.edu.husky_cooking.RecipeActivity;
import team14.tacoma.uw.edu.husky_cooking.RegisterUserFragment;

public class SignInActivity extends AppCompatActivity
        implements LogInFragment.LoginInteractionListener
        , RegisterUserFragment.UserAddListener{
    private SharedPreferences  mSharedPreferences;
    private EditText mUserName, mPwd;
    private Button mLoginButton;
    private Button mRegisterButton;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_sign_in);
        mUserName = (EditText) findViewById(R.id.user_id);
        mPwd = (EditText) findViewById(R.id.pwd);
        mLoginButton = (Button) findViewById(R.id.signin_button);



        mSharedPreferences = getSharedPreferences(getString(R.string.LOGIN_PREFS)
                , Context.MODE_PRIVATE);
        if(!mSharedPreferences.getBoolean(getString(R.string.LOGGEDIN), false)) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.sign_in_fragment_container, new LogInFragment())
                    .commit();
            mRegisterButton = (Button) findViewById(R.id.sign_up_button);


        }else{
            Intent i = new Intent(this, RecipeActivity.class);
            startActivity(i);
            finish();

        }





    }

    public void addUser(String url){
        AddUserTask task = new AddUserTask();
        task.execute(new String[]{url.toString()});
        getSupportFragmentManager().popBackStackImmediate();
    }

    public void login(String userId, String pwd){
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            //Check if the login and password are valid

//            new LoginTask().execute(url);
            try {
                OutputStreamWriter outputStreamWriter = new OutputStreamWriter(
                        openFileOutput(getString(R.string.LOGIN_FILE)
                                , Context.MODE_PRIVATE));
                outputStreamWriter.write("email = " + userId + ";");
                outputStreamWriter.close();
                Toast.makeText(this,"Stored in File Successfully!", Toast.LENGTH_LONG)
                        .show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        else {
            Toast.makeText(this, "No network connection available. Cannot authenticate user",
                    Toast.LENGTH_SHORT) .show();
            return;
        }

        mSharedPreferences
                .edit()
                .putBoolean(getString(R.string.LOGGEDIN), true)
                .commit();

        Intent i  = new Intent(this, RecipeActivity.class);
        startActivity(i);
        finish();
    }


    private class AddUserTask extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute(){
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... urls){
            String response ="";
            HttpURLConnection urlConnection=null;
            for(String url: urls){
                try{
                    URL urlObject = new URL(url);
                    urlConnection = (HttpURLConnection) urlObject.openConnection();

                    InputStream content = urlConnection.getInputStream();

                    BufferedReader buffer = new BufferedReader(new InputStreamReader(content));
                    String s = "";
                    while((s = buffer.readLine())!= null){
                        response += s;
                    }
                } catch (Exception e) {
                    response = "Unable to add user, Reason: "
                            + e.getMessage();
                } finally {
                    if(urlConnection != null){
                        urlConnection.disconnect();
                    }
                }
            }
            return response;
        }

        /**
         * It checks to see if there was a problem with the URL(Network) which is when an
         * exception is caught. It tries to call the parse Method and checks to see if it was successful.
         * If not, it displays the exception.
         *
         * @param result
         */
        @Override
        protected void onPostExecute(String result){
            try {
                JSONObject jsonObject = new JSONObject(result);
                String status = (String) jsonObject.get("result");
                if(status.equals("success")){
                    Toast.makeText(getApplicationContext(), "User successfully added!",
                            Toast.LENGTH_LONG)
                            .show();
                }else {
                    Toast.makeText(getApplicationContext(), "Failed to add: "
                                    + jsonObject.get("error")
                            ,Toast.LENGTH_LONG)
                            .show();
                }
            }catch (JSONException e){
                Toast.makeText(getApplicationContext(), "Something wrong with the data" +
                        e.getMessage(), Toast.LENGTH_LONG).show();
            }
        }

    }


    class DownloadUserTask extends AsyncTask<String, Void, String> {
        boolean failure= false;

        @Override
        protected void onPreExecute(){
            super.onPreExecute();

        }

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


//        @Override
//        protected void onPostExecute(String result) {
//            if(result.startsWith("Unable to")){
//                Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG)
//                        .show();
//                return;
//            }
//            String user;
//            result =
//        }
    }

}
