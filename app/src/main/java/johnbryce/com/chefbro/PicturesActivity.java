package johnbryce.com.chefbro;

import android.graphics.BitmapFactory;
import android.media.Image;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class PicturesActivity extends AppCompatActivity {

    FirebaseStorage storage;
    StorageReference storageRef;
    ImageView imageViewDisplay;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pictures);

        imageViewDisplay = (ImageView)findViewById(R.id.ImageViewDisplay);

        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReferenceFromUrl("gs://test-8aded.appspot.com/");
        StorageReference storagePicRef = storageRef.child("dog3.jpg");

        final long ONE_MEGABYTE = 1024 * 1024;
        storagePicRef.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                imageViewDisplay.setImageBitmap(BitmapFactory.decodeByteArray(bytes, 0, bytes.length));
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
            }
        });


    }
}
