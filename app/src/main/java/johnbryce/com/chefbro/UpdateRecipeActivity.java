package johnbryce.com.chefbro;

import android.content.res.Resources;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class UpdateRecipeActivity extends AppCompatActivity {

    private LinearLayout mLinearLayoutRows;
    private Recipe mCurrentRecipe;
    private final String TAG = "FirebaseUpdateRecipe";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_recipe);
        mLinearLayoutRows = (LinearLayout) findViewById(R.id.LinearLayoutIngredientRows);
        mCurrentRecipe = (Recipe) getIntent().getSerializableExtra("currentRecipe");
        populateData();
        createNewRow();
    }

    private void populateData() {
        EditText editTextRecipeName = (EditText) findViewById(R.id.EditTextRecipeName);
        editTextRecipeName.setText(mCurrentRecipe.getName());

        List<Ingredient> ingredients = mCurrentRecipe.getIngredients();
        for (Ingredient i : ingredients) {
            int editTextSize = 250;
            Resources r = getResources();
            float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, editTextSize, r.getDisplayMetrics());

            final LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            final LinearLayout.LayoutParams editTextParams = new LinearLayout.LayoutParams((int) px, LinearLayout.LayoutParams.WRAP_CONTENT);
            final LinearLayout.LayoutParams buttonParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

            LinearLayout linearLayout = new LinearLayout(this);
            EditText editText = new EditText(this);
            Button button = new Button(this);

            linearLayout.setLayoutParams(layoutParams);
            editText.setLayoutParams(editTextParams);
            button.setLayoutParams(buttonParams);


            //editText.setHint(""+rowCounter);
            editText.setText(i.getName());
            button.setText("-");
            button.setOnClickListener(
                    new Button.OnClickListener() {
                        public void onClick(View view) {
                            int id = view.getId();
                            //LinearLayout layout = (LinearLayout)findViewById(id+START_LAYOUT_ID);
                            mLinearLayoutRows.removeView((View) view.getParent());
                            //Toast.makeText(AddRecipeActivity.this, ""+(id-START_BUTTON_ID), Toast.LENGTH_SHORT).show();
                        }
                    }
            );


            linearLayout.addView(editText);
            linearLayout.addView(button);

            mLinearLayoutRows.addView(linearLayout);
        }
    }

    private void createNewRow() {
        int editTextSize = 250;
        Resources r = getResources();
        float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, editTextSize, r.getDisplayMetrics());

        final LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        final LinearLayout.LayoutParams editTextParams = new LinearLayout.LayoutParams((int) px, LinearLayout.LayoutParams.WRAP_CONTENT);
        final LinearLayout.LayoutParams buttonParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        LinearLayout linearLayout = new LinearLayout(this);
        EditText editText = new EditText(this);
        Button button = new Button(this);

        linearLayout.setLayoutParams(layoutParams);
        editText.setLayoutParams(editTextParams);
        button.setLayoutParams(buttonParams);


        //editText.setHint(""+rowCounter);
        editText.requestFocus();
        editText.setHint("Add Ingridient");
        button.setText("+");
        button.setOnClickListener(
                new Button.OnClickListener() {
                    public void onClick(View view) {
                        createNewRow();
                        changeButton(view);
                    }
                }
        );

        linearLayout.addView(editText);
        linearLayout.addView(button);

        mLinearLayoutRows.addView(linearLayout);


    }

    private void changeButton(View view) {
        Button button = (Button) view;
        button.setText("-");
        button.setOnClickListener(
                new Button.OnClickListener() {
                    public void onClick(View view) {
                        int id = view.getId();
                        //LinearLayout layout = (LinearLayout)findViewById(id+START_LAYOUT_ID);
                        mLinearLayoutRows.removeView((View) view.getParent());
                        //Toast.makeText(AddRecipeActivity.this, ""+(id-START_BUTTON_ID), Toast.LENGTH_SHORT).show();
                    }
                }
        );
    }

    public void cancel(View view) {
        finish();
    }

    private void onEmptyRecipeID() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Recipes");
        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get Post object and use the values to update the UI
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    Recipe currentRecipe = data.getValue(Recipe.class);
                    ;
                    if (currentRecipe.getName() != null && currentRecipe.getName().equals(mCurrentRecipe.getName())) {
                        mCurrentRecipe.setID(data.getKey());
                        //Toast.makeText(UpdateRecipeActivity.this, "Found " + currentRecipe.getName() + "at" + mCurrentRecipe.getID() , Toast.LENGTH_SHORT).show();
                    }
                }
                if (mCurrentRecipe.getID() == null) {
                    Toast.makeText(UpdateRecipeActivity.this, "Error finding Recipe in DB", Toast.LENGTH_SHORT).show();
                } else {
                    SubmitIngredients(new View(getApplication()));
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

    }

    public void SubmitIngredients(View view) {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        if (mCurrentRecipe.getID() == null) {
            onEmptyRecipeID();
            return;
        }
        DatabaseReference myRef = database.getReference("Recipes").child(mCurrentRecipe.getID());

        ArrayList<Ingredient> ingredients = new ArrayList<Ingredient>();
        Recipe recipe;

        EditText editTextRecipeName = (EditText) findViewById(R.id.EditTextRecipeName);
        for (int i = 0; i < mLinearLayoutRows.getChildCount(); i++) {
            LinearLayout layout = (LinearLayout) mLinearLayoutRows.getChildAt(i);
            EditText editText = (EditText) layout.getChildAt(0);
            ingredients.add(new Ingredient(editText.getText().toString()));
        }


        recipe = new Recipe(editTextRecipeName.getText().toString(), user.getUid(), ingredients);


        recipe.setID(mCurrentRecipe.getID());
        DatabaseReference ref = database.getReference("/Recipes").child(mCurrentRecipe.getID());
        ref.removeValue();
        ref.setValue(recipe);


        //String key = myRef.push().getKey();
        //myRef = database.getReference("Recipes/Recipe"+key);
        //myRef.setValue(recipe);

        Toast.makeText(getApplicationContext(), "Recipe " + recipe.getID() + " was modified", Toast.LENGTH_SHORT).show();
        finish();
    }
}
