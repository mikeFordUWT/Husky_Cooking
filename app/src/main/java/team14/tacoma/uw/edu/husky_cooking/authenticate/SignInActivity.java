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

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import team14.tacoma.uw.edu.husky_cooking.R;
import team14.tacoma.uw.edu.husky_cooking.RecipeActivity;

public class SignInActivity extends AppCompatActivity
        implements LogInFragment.LoginInteractionListener{
    private SharedPreferences  mSharedPreferences;
    private EditText mUserName, mPwd;
    private Button mLoginButton;



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
                    .add(R.id.fragment_container, new LogInFragment())
                    .commit();
        }else{
            Intent i = new Intent(this, RecipeActivity.class);
            startActivity(i);
            finish();
        }





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
