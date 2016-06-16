package johnbryce.com.chefbro;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;

public class UpdateProfileActivity extends AppCompatActivity {

    UserChef mCurrentUser;
    TextView tvEmail;
    EditText etPassword;
    EditText etPasswordComfirm;
    ImageView ivProfilePic;

    private FirebaseAuth mAuth;
    private FirebaseDatabase database;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_profile);

        byte[] data;

        database = FirebaseDatabase.getInstance();

        if(getIntent().getSerializableExtra("currentUser") != null
                && getIntent().getByteArrayExtra("profilePic") != null)
        {
            mCurrentUser = (UserChef)getIntent().getSerializableExtra("currentUser");
            data = getIntent().getByteArrayExtra("profilePic");

        }
        else
        {
            Toast.makeText(UpdateProfileActivity.this, "Error on getting user", Toast.LENGTH_SHORT).show();
            data = new byte[1];
            finish();
        }

        tvEmail = (TextView)findViewById(R.id.TextViewEmail);
        etPassword= (EditText)findViewById(R.id.EditTextPassword);
        etPasswordComfirm = (EditText)findViewById(R.id.EditTextConfirmPassword);
        ivProfilePic = (ImageView)findViewById(R.id.ImageViewProfilePic);

        tvEmail.setText(mCurrentUser.getEmail());
        ivProfilePic.setImageBitmap(BitmapFactory.decodeByteArray(data, 0, data.length));


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

    public void update(View view) {
        //DatabaseReference ref = database.getReference("users").child(mCurrentUser.getUid());

        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference().child("UserProfilePics").child(mCurrentUser.getPictureName());



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

                Toast.makeText(getApplicationContext(), exception.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.

                Uri downloadUrl = taskSnapshot.getDownloadUrl();
                Toast.makeText(getApplicationContext(), downloadUrl.toString(), Toast.LENGTH_SHORT).show();
                finish();
            }
        });



        //Map<String, Object> childUpdates = new HashMap<>();
        //childUpdates.put("/users/"+mCurrentUser.getUid(), mCurrentUser);

    }
}
