package johnbryce.com.chefbro;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class ViewRecipeActivity extends AppCompatActivity {

    private String TAG = "Firebase";
    private FirebaseDatabase database;
    private DatabaseReference myRef;
    private Query query;
    private ValueEventListener postListener;
    private ArrayList<Recipe> recipes;

    private ListView listViewRecipes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_recipe);

        recipes = new ArrayList<>();
        Ingredient ingredient = new Ingredient("Pizza");
        ArrayList<Ingredient> ingredients = new ArrayList<Ingredient>();
        ingredients.add(ingredient);
        recipes.add( new Recipe("Chef pizza","xxxxxxx",ingredients ));

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("Recipes");
        query = myRef.orderByValue();

        listViewRecipes = (ListView)findViewById(R.id.ListViewRecipes);




        postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get Post object and use the values to update the UI
                for(DataSnapshot data : dataSnapshot.getChildren())
                {
                    recipes.add(data.getValue(Recipe.class));
                }
                ListAdapter adapter =  new RecipeAdapter(getApplicationContext(),R.layout.list_view_recipe,recipes);
                listViewRecipes.setAdapter(adapter);


                // ...
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
                // ...
            }
        };
        query.addValueEventListener(postListener);

    }
}
