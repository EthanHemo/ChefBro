package johnbryce.com.chefbro;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AddActionCategoryActivity extends AppCompatActivity {

    private final String DB_LOCATION="/ActionCategory";
    private final String MEMBER_KEY="ActionCategory";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_action_category);
    }

    public void submit(View view) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference(DB_LOCATION);

        EditText editText = (EditText)findViewById(R.id.EditTextActionCategory);
        ActionCategory ag = new ActionCategory(editText.getText().toString());

        String key = MEMBER_KEY + ref.push().getKey();
        ag.setID(key);
        ref = database.getReference(DB_LOCATION + "/"+ key);
        ref.setValue(ag);
        Toast.makeText(getApplicationContext(), "Action Category Saved", Toast.LENGTH_SHORT).show();
        finish();
    }
}
