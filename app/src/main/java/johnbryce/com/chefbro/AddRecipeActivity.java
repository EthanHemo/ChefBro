package johnbryce.com.chefbro;

import android.content.res.Resources;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class AddRecipeActivity extends AppCompatActivity {

    private final int START_LAYOUT_ID = 200;
    private final int START_EDITTEXT_ID = 600;
    private final int START_BUTTON_ID = 1000;
    private int rowCounter = 0;
    LinearLayout mLinearLayoutRows;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_recipe);

        mLinearLayoutRows = (LinearLayout)findViewById(R.id.LinearLayoutIngredientRows);
        createNewRow();

    }


    private void createNewRow()
    {
        int editTextSize = 250;
        Resources r = getResources();
        float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, editTextSize, r.getDisplayMetrics());

        final LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT );
        final LinearLayout.LayoutParams editTextParams = new LinearLayout.LayoutParams((int)px,LinearLayout.LayoutParams.WRAP_CONTENT );
        final LinearLayout.LayoutParams buttonParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT );

        LinearLayout linearLayout = new LinearLayout(this);
        EditText editText = new EditText(this);
        Button button = new Button(this);

        linearLayout.setLayoutParams(layoutParams);
        editText.setLayoutParams(editTextParams);
        button.setLayoutParams(buttonParams);

        linearLayout.setId(START_LAYOUT_ID + rowCounter);
        editText.setId(START_EDITTEXT_ID + rowCounter);
        button.setId(START_BUTTON_ID + rowCounter);

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

        rowCounter++;


    }

    private void changeButton(View view)
    {
        Button button = (Button)view;
        button.setText("-");
        button.setOnClickListener(
                new Button.OnClickListener(){
                        public void onClick(View view){
                        int id = view.getId();
                        //LinearLayout layout = (LinearLayout)findViewById(id+START_LAYOUT_ID);
                        mLinearLayoutRows.removeView((View)view.getParent());
                        //Toast.makeText(AddRecipeActivity.this, ""+(id-START_BUTTON_ID), Toast.LENGTH_SHORT).show();
                    }
                }
        );
    }


    public void SubmitIngredients(View view) {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Recipes");

        ArrayList<Ingredient> ingredients = new ArrayList<Ingredient>();
        Recipe recipe;

        EditText editTextRecipeName = (EditText)findViewById(R.id.EditTextRecipeName);
        for (int i = 0; i < mLinearLayoutRows.getChildCount();i++)
        {
            LinearLayout layout = (LinearLayout)mLinearLayoutRows.getChildAt(i);
            EditText editText = (EditText)layout.getChildAt(0);
            ingredients.add(new Ingredient(editText.getText().toString()));
        }


        recipe = new Recipe(editTextRecipeName.getText().toString(), user.getUid(), ingredients);

        String key = myRef.push().getKey();
        myRef = database.getReference("Recipes/Recipe"+key);
        myRef.setValue(recipe);

        Toast.makeText(AddRecipeActivity.this, "Recipe added", Toast.LENGTH_SHORT).show();
        finish();


    }
}
