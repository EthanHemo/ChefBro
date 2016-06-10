package johnbryce.com.chefbro;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


class RecipeAdapter extends ArrayAdapter<Recipe> {

    private final String TAG = "ArrayAdapterLog";
    private Context mContext;
    private int mResource;
    private ArrayList<Recipe> mRecipes;


    public RecipeAdapter(Context context, int resource, List<Recipe> recipes ) {
        super(context, R.layout.list_view_recipe,recipes);
        mContext = context;
        mResource = resource;

        mRecipes = new ArrayList<Recipe>();
        mRecipes.addAll(recipes);
        Log.i(TAG, "Finish contractor. recipe have " + recipes.size());
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater layoutInflater = LayoutInflater.from(getContext());
        View customView = layoutInflater.inflate(R.layout.list_view_recipe, parent, false);



        LinearLayout layout = (LinearLayout)customView.findViewById(R.id.LinearLayoutListViewRecipe);
        TextView textViewName = (TextView)customView.findViewById(R.id.TextViewRecipeName);
        Recipe recipe = mRecipes.get(position);
        textViewName.setText(recipe.getName());
        for(Ingredient ingredient : recipe.getIngredients())
        {
            TextView textView = new TextView(customView.getContext());
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            textView.setText("--" + ingredient.getDetails());
            textView.setTextColor(Color.BLACK);
            layout.addView(textView,params);
        }

        View lineView = new View(customView.getContext());
        lineView.setBackgroundColor(Color.GRAY);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 10);
        layout.addView(lineView,params);


        Log.i(TAG, "Finish view at position" + position);
        return customView;

    }
}
