package johnbryce.com.chefbro;

import android.app.DownloadManager;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GetTokenResult;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class ProfileActivity extends AppCompatActivity {

    private final String TAG="FirebaseProfile";
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseDatabase database;
    private DatabaseReference myRef;
    private Query query;
    private ValueEventListener postListener;
    private UserChef currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);


        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    mUser = user;
                    getUserDetailsFromDB();
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }

            }
        };
        mAuth.addAuthStateListener(mAuthListener);

    }



    private void getUserDetailsFromDB(){
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("users").child(mUser.getUid());

        postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get Post object and use the values to update the UI
                TextView tvHeader = (TextView) findViewById(R.id.TextViewProfileHeader);
                if(dataSnapshot.getValue(UserChef.class) == null)
                {
                    currentUser = new UserChef(mUser.getUid(),mUser.getEmail());
                    myRef.setValue(currentUser);
                    tvHeader.setText("Creating User...");
                }
                else
                {
                    currentUser = dataSnapshot.getValue(UserChef.class);
                    tvHeader.setText("Welcome " + currentUser.getEmail());
                    getUserProfilePic();
                }
                // ...
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
                // ...
            }
        };
        myRef.addValueEventListener(postListener);


        //TextView tvHeader = (TextView) findViewById(R.id.TextViewProfileHeader);
        //tvHeader.setText("Welcome " + user.getEmail());
    }

    private void getUserProfilePic(){
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference().child("UserProfilePics").child(currentUser.getPictureName());

        //TODO: Adjust users with no profile pic so they will have one
        final long ONE_MEGABYTE = 1024 * 1024;
        storageRef.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                // Data for "images/island.jpg" is returns, use this as needed
                ImageView imageView = (ImageView)findViewById(R.id.ImageViewProfilePic);
                imageView.setImageBitmap(BitmapFactory.decodeByteArray(bytes,0,bytes.length));
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
            }
        });


    }

    public void signout(View view) {
        mAuth.signOut();
        finish();
    }

    public void addRecipe(View view) {
        Intent intent = new Intent(getApplicationContext(),AddRecipeActivity.class);
        startActivity(intent);
    }

    public void viewRecipes(View view) {
        Intent intent = new Intent(getApplicationContext(),ViewRecipeActivity.class);
        startActivity(intent);
    }
}
