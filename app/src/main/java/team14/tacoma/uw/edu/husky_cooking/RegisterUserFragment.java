package team14.tacoma.uw.edu.husky_cooking;


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

import team14.tacoma.uw.edu.husky_cooking.authenticate.SignInActivity;


/**
 * A simple {@link Fragment} subclass.
 */
public class RegisterUserFragment extends Fragment {
    private final static String USER_ADD_URL =
            "http://cssgate.insttech.washington.edu/~_450atm14/husky_cooking/addUser.php?";

    private final static String TAG = "UserAddFragment";

    private EditText mUserEmail;
    private EditText mPassword;
    private UserAddListener mListener;

    public RegisterUserFragment() {
        // Required empty public constructor
    }

    public interface UserAddListener{
        public void addUser(String url);

    }

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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_register_user, container, false);
        mUserEmail = (EditText) v.findViewById(R.id.new_user_email);
        mPassword = (EditText) v.findViewById(R.id.new_user_password);

        Button addUserButton = (Button) v.findViewById(R.id.register_button);
        addUserButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userId = mUserEmail.getText().toString();
                String pwd = mPassword.getText().toString();

                if(TextUtils.isEmpty(userId)){
                    Toast.makeText(v.getContext(), "Enter userid"
                            , Toast.LENGTH_SHORT)
                            .show();
                    mUserEmail.requestFocus();
                    return;
                }

                if(!userId.contains("@")){
                    Toast.makeText(v.getContext(), "Enter a valid email adresss"
                            , Toast.LENGTH_SHORT).show();
                    mUserEmail.requestFocus();
                    return;
                }

                if(TextUtils.isEmpty(pwd)){
                    Toast.makeText(v.getContext(), "Enter a password"
                            , Toast.LENGTH_SHORT)
                            .show();
                    mPassword.requestFocus();
                    return;
                }

                if(pwd.length() < 6){
                    Toast.makeText(v.getContext(), "Enter a passwork of at least 6 characters"
                            , Toast.LENGTH_SHORT)
                            .show();
                    mPassword.requestFocus();
                    return;
                }

                String url = buildUserUrl(v);
                ((SignInActivity) getActivity()).addUser(url);


            }
        });

        return v;
    }

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
