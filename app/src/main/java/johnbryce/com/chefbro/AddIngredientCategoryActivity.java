package johnbryce.com.chefbro;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AddIngredientCategoryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_ingredient_category);
    }

    public void submit(View view) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("/IngredientCategory");

        EditText editText = (EditText)findViewById(R.id.EditTextIngredientCategory);
        IngredientCategory ig = new IngredientCategory(editText.getText().toString());

        String key = ref.push().getKey();
        ig.setID("IngredientCategory" + key );
        ref = database.getReference("/IngredientCategories/IngredientCategory"+key);
        ref.setValue(ig);
        Toast.makeText(AddIngredientCategoryActivity.this, "Ingredient Category Saved", Toast.LENGTH_SHORT).show();
        finish();
    }
}
