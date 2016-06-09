package johnbryce.com.chefbro;

import android.app.DownloadManager;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class ProfileActivity extends AppCompatActivity {

    private final String TAG="FirebaseProfile";
    private FirebaseAuth mAuth;
    FirebaseUser mUser;
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
        if(mAuth.getCurrentUser() == null)
        {
            finish();
        }
        else {
            mUser = mAuth.getCurrentUser();
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

    }

    public void signout(View view) {
        mAuth.signOut();
        finish();
    }

    public void addRecipe(View view) {
        Intent intent = new Intent(getApplicationContext(),AddRecipeActivity.class);
        startActivity(intent);
    }
}
