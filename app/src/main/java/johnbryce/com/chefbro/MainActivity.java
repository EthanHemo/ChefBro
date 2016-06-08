package johnbryce.com.chefbro;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    String TAG = "Firebase";
    FirebaseDatabase database;
    DatabaseReference myRef;
    TextView textViewDisplay;
    Recipe currentRecipe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        currentRecipe = new Recipe("Toast","Very Yummy");
        textViewDisplay = (TextView)findViewById(R.id.textViewDisplay);
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("test-8aded");

        // Read from the database
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                Recipe value = dataSnapshot.getValue(Recipe.class);
                if(value != null) {
                    textViewDisplay.setText(value.toString());
                    Log.d(TAG, "Value is: " + value);
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });



    }

    public void SendText(View view) {
        EditText editTextInput = (EditText) findViewById(R.id.editTextInput);
        currentRecipe.setName(editTextInput.getText().toString());
        myRef.setValue(currentRecipe);

    }
}
