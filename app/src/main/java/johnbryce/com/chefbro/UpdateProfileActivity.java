package johnbryce.com.chefbro;

import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class UpdateProfileActivity extends AppCompatActivity {

    UserChef mCurrentUser;
    EditText etEmail;
    EditText etPassword;
    EditText etPasswordComfirm;
    ImageView ivProfilePic;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_profile);

        byte[] data;

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

        etEmail = (EditText)findViewById(R.id.EditTextEmail);
        etPassword= (EditText)findViewById(R.id.EditTextPassword);
        etPasswordComfirm = (EditText)findViewById(R.id.EditTextConfirmPassword);
        ivProfilePic = (ImageView)findViewById(R.id.ImageViewProfilePic);

        etEmail.setText(mCurrentUser.getEmail());
        ivProfilePic.setImageBitmap(BitmapFactory.decodeByteArray(data, 0, data.length));


    }
}
