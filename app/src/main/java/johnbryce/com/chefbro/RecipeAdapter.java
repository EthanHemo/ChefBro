package johnbryce.com.chefbro;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;


class RecipeAdapter extends ArrayAdapter<Recipe> {

    private final String TAG = "ArrayAdapterLog";
    private Context mContext;
    private int mResource;
    private String mUid;


    public RecipeAdapter(Context context, int resource, List<Recipe> recipes, String uid ) {
        super(context, resource ,recipes);
        mContext = context;
        mResource = resource;
        mUid = uid;
        Log.i(TAG, "Finish contractor. recipe have " + recipes.size());
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater layoutInflater = LayoutInflater.from(getContext());
        View customView = layoutInflater.inflate(R.layout.list_view_recipe, parent, false);



        LinearLayout layout = (LinearLayout)customView.findViewById(R.id.LinearLayoutListViewRecipe);
        TextView textViewName = (TextView)customView.findViewById(R.id.TextViewRecipeName);
        Recipe recipe = getItem(position);
        textViewName.setText(recipe.getName());
        if(recipe.getOwnerUID().equals(mUid))
        {
            textViewName.setTextColor(Color.GREEN);
        }

        for(Ingredient ingredient : recipe.getIngredients())
        {
            TextView textView = new TextView(mContext);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            textView.setText("--" + ingredient.getName());
            textView.setTextColor(Color.BLACK);
            layout.addView(textView,params);
        }

        View lineView = new View(mContext);
        lineView.setBackgroundColor(Color.GRAY);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 10);
        layout.addView(lineView,params);


        Log.i(TAG, "Finish view at position" + position);
        return customView;

    }
}
