/*
 * Mike Ford and Ian Skyles
 * TCSS450 – Spring 2016
 * Recipe Project
 */
package team14.tacoma.uw.edu.husky_cooking.authenticate;


import android.content.Context;
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


/**
 * This class will be the splash screen for users when
 * they go to register.
 * From here they can type in user and password
 * and be registered for an account.
 * @author Mike Ford
 * @author Ian Skyles
 * @version 6/4/2016
 */
public class RegisterUserFragment extends Fragment {
    /** Web url address for backend storage of users. */
    private final static String USER_ADD_URL =
            "http://cssgate.insttech.washington.edu/~_450atm14/husky_cooking/addUser.php?";

    /** Tag for debugging using log */
    private final static String TAG = "UserAddFragment";

    /** Allows user to enter email */
    private EditText mUserEmail;
    /** Allows user to enter password */
    private EditText mPassword;
    /** Confirms password entered is correct*/
    private EditText mConfirmPassword;
    /** Listener interface for adding user. */
    private UserAddListener mListener;

    /**
     * Required constructor.
     */
    public RegisterUserFragment() {
        // Required empty public constructor
    }

    /**
     * Interface that requires implementing addUser
     */
    public interface UserAddListener{
        void addUser(String url);

    }

    /**
     * Attaches add user
     * @param context what to attach
     */
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if(context instanceof UserAddListener){
            mListener = (UserAddListener) context;
        }else{
            throw new RuntimeException(context.toString()
                    + "must implement UserAddListener");
        }
    }

    /**
     * Creates the view that will be shown to the user.
     * Attaches listeners to the buttons defined in the XML.
     * Gives users toasts to notify them of various things such has
     * invalid password/user or an error due to connection.
     * @param inflater instantiate layout XML file into its corresponding View object
     * @param container item to contain other views
     * @param savedInstanceState save state so we can resume later
     * @return The view (user interface)
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_register_user, container, false);
        mUserEmail = (EditText) v.findViewById(R.id.new_user_email);
        mPassword = (EditText) v.findViewById(R.id.new_user_password);
        mConfirmPassword = (EditText) v.findViewById(R.id.confirm_user_password);

        Button addUserButton = (Button) v.findViewById(R.id.register_button);
        addUserButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userId = mUserEmail.getText().toString();
                String pwd = mPassword.getText().toString();
                String confirmPWD = mConfirmPassword.getText().toString();

                //remind user to enter id
                if(TextUtils.isEmpty(userId)){
                    Toast.makeText(v.getContext(), "Enter userid"
                            , Toast.LENGTH_SHORT)
                            .show();
                    mUserEmail.requestFocus();
                    return;
                }

                //remind user to enter id with @
                if(!userId.contains("@")){
                    Toast.makeText(v.getContext(), "Enter a valid email adresss"
                            , Toast.LENGTH_SHORT).show();
                    mUserEmail.requestFocus();
                    return;
                }

                //remind user to enter password
                if(TextUtils.isEmpty(pwd)){
                    Toast.makeText(v.getContext(), "Enter a password"
                            , Toast.LENGTH_SHORT)
                            .show();
                    mPassword.requestFocus();
                    return;
                }

                //remind user to enter password with at least 6 characters
                if(pwd.length() < 6){
                    Toast.makeText(v.getContext(), "Enter a passwork of at least 6 characters"
                            , Toast.LENGTH_SHORT)
                            .show();
                    mPassword.requestFocus();
                    mPassword.setText("");
                    return;
                }

                //remind user to enter same pass in both prompts.
                if(!pwd.equals(confirmPWD)){
                    Toast.makeText(v.getContext(), "Passwords don't match, please try again"
                            , Toast.LENGTH_SHORT)
                            .show();
                    mPassword.requestFocus();
                    mPassword.setText("");
                    mConfirmPassword.setText("");
                    return;
                }

                String url = buildUserUrl(v);
                ((SignInActivity) getActivity()).addUser(url);


            }
        });

        return v;
    }

    /**
     * Builds a url (for the database/users php)
     * based on email and password.
     * Basically inserts user into our table
     * so they can sign in later.
     * @param v to create a toast incase of error
     * @return the url for adding a new user to the database
     */
    private String buildUserUrl(View v) {
        StringBuilder sb = new StringBuilder(USER_ADD_URL);

        try{
            String email = mUserEmail.getText().toString();
            sb.append("email=");
            sb.append(email);

            String pwd = mPassword.getText().toString();
            sb.append("&pwd=");
            sb.append(pwd);

            Log.i(TAG, sb.toString());
        }catch (Exception e) {
            Toast.makeText(v.getContext(), "Something wrong with the url " + e.getMessage(),
                    Toast.LENGTH_LONG).show();
        }

        return sb.toString();
    }

}
