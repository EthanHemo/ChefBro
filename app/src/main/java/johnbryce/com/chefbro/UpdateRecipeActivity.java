package johnbryce.com.chefbro;

import android.content.res.Resources;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import java.util.List;

public class UpdateRecipeActivity extends AppCompatActivity {

    private LinearLayout mLinearLayoutRows;
    private Recipe mCurrentRecipe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_recipe);
        mLinearLayoutRows = (LinearLayout)findViewById(R.id.LinearLayoutIngredientRows);
        mCurrentRecipe = (Recipe)getIntent().getSerializableExtra("currentRecipe");
        populateData();
        createNewRow();
    }

    private void populateData()
    {
        EditText editTextRecipeName = (EditText)findViewById(R.id.EditTextRecipeName);
        editTextRecipeName.setText(mCurrentRecipe.getName());

        List<Ingredient> ingredients = mCurrentRecipe.getIngredients();
        for(Ingredient i:ingredients)
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


            //editText.setHint(""+rowCounter);
            editText.setText(i.getDetails());
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


        //editText.setHint(""+rowCounter);
        editText.requestFocus();
        editText.setHint("Add Ingridient");
        button.setText("-");
        button.setOnClickListener(
                new Button.OnClickListener(){
                    public void onClick(View view){
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

    public void cancel(View view) {
        finish();
    }
}
