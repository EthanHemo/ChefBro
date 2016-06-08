package johnbryce.com.chefbro;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

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
        final LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT );
        final LinearLayout.LayoutParams editTextParams = new LinearLayout.LayoutParams(500,LinearLayout.LayoutParams.WRAP_CONTENT );
        final LinearLayout.LayoutParams buttonParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT );

        LinearLayout linearLayout = new LinearLayout(this);
        EditText editText = new EditText(this);
        Button button = new Button(this);

        linearLayout.setLayoutParams(layoutParams);
        editText.setLayoutParams(editTextParams);
        button.setLayoutParams(buttonParams);

        linearLayout.setId(START_LAYOUT_ID+rowCounter);
        editText.setId(START_EDITTEXT_ID+rowCounter);
        button.setId(START_BUTTON_ID+rowCounter);

        editText.setHint(""+rowCounter);
        //editText.setHint("Add Ingridient");
        button.setText("+");
        button.setOnClickListener(
                new Button.OnClickListener(){
                    public void onClick(View view){
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
        String msg = "";
        for (int i = 0; i < mLinearLayoutRows.getChildCount();i++)
        {
            LinearLayout layout = (LinearLayout)mLinearLayoutRows.getChildAt(i);
            EditText editText = (EditText)layout.getChildAt(0);
            msg = msg + editText.getText().toString() + ", ";
        }

        Toast.makeText(AddRecipeActivity.this, msg, Toast.LENGTH_SHORT).show();

    }
}
