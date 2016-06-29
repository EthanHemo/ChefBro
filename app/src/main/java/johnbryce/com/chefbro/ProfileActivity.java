package johnbryce.com.chefbro;

import android.app.DownloadManager;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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

import java.io.ByteArrayOutputStream;

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
    private int backButtonCount;

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
                    //setUserListener();
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

    private void setUserListener(){
        mUser.getToken(true).addOnCompleteListener(this, new OnCompleteListener<GetTokenResult>() {
            @Override
            public void onComplete(@NonNull Task<GetTokenResult> task) {
                if (task.isSuccessful()) {
                    Log.d(TAG, "token=" + task.getResult().getToken());
                } else {
                    Toast.makeText(ProfileActivity.this, "Error in user login", Toast.LENGTH_SHORT).show();
                    Log.e(TAG, "exception=" + task.getException().toString());
                    mAuth.signOut();
                    startActivity(new Intent(getApplication(), LoginActivity.class));
                }
            }
        });
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

                    Log.e(TAG, "Data wasn't found in DB");
                }
                else
                {
                    currentUser = dataSnapshot.getValue(UserChef.class);
                    tvHeader.setText("Welcome " + currentUser.getEmail());
                    getUserProfilePic();
                    Log.i(TAG, "getting information from DB");
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
        myRef.addListenerForSingleValueEvent(postListener);


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
                Bitmap userPicture = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                imageView.setImageBitmap(userPicture);
                //imageView.setImageBitmap(getRoundedCornerBitmap(userPicture));
                Log.i(TAG, "got user profile pic");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
                Log.e(TAG, "Couldn't get user profile pic");
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

    /**
     * Back button listener.
     * Will close the application if the back button pressed twice.
     */
    @Override
    public void onBackPressed()
    {
        if(backButtonCount >= 1)
        {
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
        else
        {
            Toast.makeText(this, "Press the back button once again to close the application.", Toast.LENGTH_SHORT).show();
            backButtonCount++;
        }
    }

    public void updateProfile(View view) {
    }

    public void redirectApp(View view){
        Intent intent;
        switch(view.getId())
        {
            case R.id.ButtonUpdateProfile:
                ImageView imageView = (ImageView)findViewById(R.id.ImageViewProfilePic);
                imageView.destroyDrawingCache();
                imageView.buildDrawingCache();
                Bitmap bitmap = imageView.getDrawingCache();
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                byte[] data = baos.toByteArray();
                intent = new Intent(getApplicationContext(), UpdateProfileActivity.class);
                intent.putExtra("currentUser",currentUser);
                intent.putExtra("profilePic",data);
                break;
            case R.id.ButtonAddRecipe:
                intent = new Intent(getApplicationContext(),AddRecipeActivity.class);
                break;
            case R.id.ButtonViewRecipes:
                intent = new Intent(getApplicationContext(),ViewRecipeActivity.class);
                break;
            case R.id.ButtonAddIngredient:
                intent = new Intent(getApplicationContext(),AddIngredientActivity.class);
                break;
            case R.id.ButtonAddIngredientCategory:
                intent = new Intent(getApplicationContext(),AddIngredientCategoryActivity.class);
                break;
            default:
                Toast.makeText(ProfileActivity.this, "No correct button choosen", Toast.LENGTH_SHORT).show();
                intent = new Intent();
        }

        startActivity(intent);
    }

    public static Bitmap getRoundedCornerBitmap(Bitmap bitmap) {
        int pixels = 360;
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap
                .getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);
        final float roundPx = pixels;

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);

        return output;
    }

       /*
    color base:
    prime: 640909
    B44949
    8D2424
    400000
    200000

    color palette
    http://paletton.com/#uid=1000u0kt7czj9mHo2hFAy7ZNe41
    640909

    designs:
    http://theultralinx.com/2012/11/15-examples-profile-ui-design-inspiration/

     */
}
