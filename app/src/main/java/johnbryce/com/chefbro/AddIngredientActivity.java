package johnbryce.com.chefbro;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class AddIngredientActivity extends AppCompatActivity {

    FirebaseDatabase database;
    DatabaseReference ref;
    AutoCompleteTextView actvCatgory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_ingredient);

        actvCatgory = (AutoCompleteTextView)findViewById(R.id.AutoCompleteIngredientCategory);
        database = FirebaseDatabase.getInstance();
        ref = database.getReference("/IngredientCategories");
        ValueEventListener postListener = new ValueEventListener(){
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(AddIngredientActivity.this, "Error getting categiry data: "+databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<String> categories = new ArrayList<>();
                for(DataSnapshot data: dataSnapshot.getChildren())
                {
                    IngredientCategory temp = data.getValue(IngredientCategory.class);
                    categories.add(temp.getName());
                }
                Toast.makeText(AddIngredientActivity.this, "Total: "+ categories.size(), Toast.LENGTH_SHORT).show();
                //ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,countries);
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_list_item_1,categories);
                actvCatgory.setAdapter(adapter);
                actvCatgory.setTextColor(Color.RED);
            }
        };

        ref.addListenerForSingleValueEvent(postListener);

    }
}
