package johnbryce.com.chefbro;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;

public class SignInActivity extends AppCompatActivity {

    private final String TAG = "FirebaseSignin";

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseUser mUser;
    private ProgressDialog mProgressDialog;
    private UserChef mCurrentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);


        mAuth = FirebaseAuth.getInstance();

        /*
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // When user is signed in or already signed in, direct him to his profile
                    if(mCurrentUser != null) {
                        Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                        Intent intent = new Intent(getApplicationContext(), ProfileActivity.class);
                        startActivity(intent);
                    }
                    else {
                        signUserToDB();
                    }
                } else {
                    // When user isn't logged in, waiting for either signup or signin
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
            }
        };

        */
    }

    public void browsePicture(View view) {
        Intent intent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, 0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);

        ImageView targetImage = (ImageView)findViewById(R.id.ImageViewProfilePic);

        if (resultCode == RESULT_OK) {
            Uri targetUri = data.getData();
            Bitmap bitmap;
            try {
                bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(targetUri));
                targetImage.setImageBitmap(bitmap);
            } catch (FileNotFoundException e) {

                e.printStackTrace();
            }
        }
    }

    public void back(View view) {
        finish();
    }

    public void signin(View view) {







        EditText etEmail = (EditText)findViewById(R.id.EditTextEmail);
        EditText etPassword = (EditText)findViewById(R.id.EditTextPassword);
        EditText etConfirmPassword = (EditText)findViewById(R.id.EditTextConfirmPassword);
        final String email = etEmail.getText().toString();
        final String password = etPassword.getText().toString();
        final String passwordConfirm = etConfirmPassword.getText().toString();

        //TODO: Validate


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

                            signUserToDB();

                            Toast.makeText(getApplicationContext(), "Authentication Succeeded.",
                                    Toast.LENGTH_SHORT).show();
                        }


                        // ...
                    }
                });


    }

    private void signUserToDB()
    {
        FirebaseUser user = mAuth.getCurrentUser();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("users").child(user.getUid());

        EditText etEmail = (EditText)findViewById(R.id.EditTextEmail);
        final String email = etEmail.getText().toString();

        mCurrentUser = new UserChef(user.getUid(),email);
        mCurrentUser.setPictureName(user.getUid());

        myRef.setValue(mCurrentUser);

        uploadProfilePic(user.getUid());




    }

    private void uploadProfilePic(String picName)
    {

        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference().child("UserProfilePics").child(picName);

        showProgressDialog();

        ImageView imageViewProfilePic = (ImageView)findViewById(R.id.ImageViewProfilePic);
        imageViewProfilePic.destroyDrawingCache();
        imageViewProfilePic.buildDrawingCache();
        Bitmap bitmap = imageViewProfilePic.getDrawingCache();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        UploadTask uploadTask = storageRef.putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
                hideProgressDialog();
                Toast.makeText(SignInActivity.this, exception.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                hideProgressDialog();
                Uri downloadUrl = taskSnapshot.getDownloadUrl();
                Toast.makeText(SignInActivity.this, downloadUrl.toString(), Toast.LENGTH_SHORT).show();
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
}
