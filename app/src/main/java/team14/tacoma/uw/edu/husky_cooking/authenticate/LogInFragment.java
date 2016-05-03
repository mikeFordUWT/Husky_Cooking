package team14.tacoma.uw.edu.husky_cooking.authenticate;


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

import team14.tacoma.uw.edu.husky_cooking.R;
import team14.tacoma.uw.edu.husky_cooking.RegisterUserFragment;

/**
 * A simple {@link Fragment} subclass.
 */
public class LogInFragment extends Fragment {
    private static final String LOGIN_URL =
            "http://cssgate.insttech.washington.edu/~_450atm14/husky_cooking/login.php?";

    private static final String USER_ADD_URL =
            "http://cssgate.insttech.washington.edu/~_450atm14/husky_cooking/addUser.php?";

    private static final String TAG = "LogInFragment";
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";

    private EditText mUserName, mPwd,mRegisterEmail, mRegisterPassword;


    public LogInFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_log_in, container, false);
        mUserName = (EditText) v.findViewById(R.id.user_id);
        mPwd = (EditText) v.findViewById(R.id.pwd);



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
                ((SignInActivity) getActivity()).login(url);

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

    public interface LoginInteractionListener {
        public void login(String url);
        public void signup(String url);

    }



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
