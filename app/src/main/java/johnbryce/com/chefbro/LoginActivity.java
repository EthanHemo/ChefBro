package johnbryce.com.chefbro;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class LoginActivity extends AppCompatActivity {

    private final String TAG="FirebaseLogin";

    private ProgressDialog mProgressDialog;
    private TextView tvUserState;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Set textview for direct output
        tvUserState = (TextView)findViewById(R.id.TextViewUserLogin);

        // Initialize authentication object and listener
        //The listener is attached at onStart and onResume
        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // When user is signed in or already signed in, direct him to his profile
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                    Intent intent = new Intent(getApplicationContext(),ProfileActivity.class);
                    startActivity(intent);
                } else {
                    // When user isn't logged in, waiting for either signup or signin
                    tvUserState.setText("User IS NOT logged in");
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
            }
        };


    }

    @Override
    public void onStart() {
        super.onStart();

        // Set Authentication object to listener
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Set Authentication object to listener
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();

        // Remove the listener from Authenticate object
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    private boolean validateEmailAndPassword(String email, String password)
    {
        final String EMAIL_PATTERN =
                "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                        + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        final int PASSWORD_LENGTH = 6;
        String msg = "";

        //check Email
        if(!email.matches(EMAIL_PATTERN))
        {
            msg = "Email isn't correct";
        }
        //Check Password
        else if(password.length()<PASSWORD_LENGTH)
        {
            msg = "Password too short, need at least 8 characters";
        }

        if (!msg.equals(""))
        {
            Toast.makeText(LoginActivity.this, msg, Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;


    }


    public void login(View view) {
        // Get user input
        EditText etEmail = (EditText)findViewById(R.id.EditTextEmail);
        EditText etPassword = (EditText)findViewById(R.id.EditTextPassword);
        final String email = etEmail.getText().toString();
        final String password = etPassword.getText().toString();

        if(!validateEmailAndPassword(email,password))
        {
            return;
        }

        //TODO: Add validation of Email and password
        showProgressDialog();


        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "signInWithEmail:onComplete:" + task.isSuccessful());

                        hideProgressDialog();
                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "signInWithEmail", task.getException());
                            Toast.makeText(getApplicationContext(), "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                        else if(task.isSuccessful()) {
                            Log.w(TAG, "signInWithEmail succeed");

                            Toast.makeText(getApplicationContext(), "Authentication Succeeded.",
                                    Toast.LENGTH_SHORT).show();
                        }


                    }
                });


    }

    public void Signup(View view) {

        //TODO: Create screen for create user.
        EditText etEmail = (EditText)findViewById(R.id.EditTextEmail);
        EditText etPassword = (EditText)findViewById(R.id.EditTextPassword);
        final String email = etEmail.getText().toString();
        final String password = etPassword.getText().toString();

        showProgressDialog();

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "createUserWithEmail:onComplete:" + task.isSuccessful());
                        hideProgressDialog();
                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Toast.makeText(getApplicationContext(), "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                        else if(task.isSuccessful()) {
                            Log.w(TAG, "signInWithEmail succeed");
                            Toast.makeText(getApplicationContext(), "Authentication Succeeded.",
                                    Toast.LENGTH_SHORT).show();
                        }


                        // ...
                    }
                });

    }

    public void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setCancelable(false);
            mProgressDialog.setMessage("Loading...");
        }

        mProgressDialog.show();
    }

    public void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }

    public void Signout(View view) {
        mAuth.signOut();
    }
}
