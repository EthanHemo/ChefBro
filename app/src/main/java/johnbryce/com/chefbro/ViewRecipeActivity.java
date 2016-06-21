package johnbryce.com.chefbro;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GetTokenResult;
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
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private FirebaseAuth.AuthStateListener mAuthListener;

    private ListView listViewRecipes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_recipe);

        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    mUser = user;
                    getRecipeFromDB();
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
                // ...
            }
        };
        mAuth.addAuthStateListener(mAuthListener);


    }

    private void getRecipeFromDB() {

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("Recipes");
        query = myRef.orderByValue();

        listViewRecipes = (ListView) findViewById(R.id.ListViewRecipes);
        listViewRecipes.setOnItemLongClickListener(
                new AdapterView.OnItemLongClickListener() {
                    @Override
                    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                        Recipe recipe = (Recipe)parent.getItemAtPosition(position);
                        Intent intent = new Intent(getApplicationContext(), UpdateRecipeActivity.class);
                        intent.putExtra("currentRecipe",recipe);
                        startActivity(intent);
                        return true;
                    }
                }

        );


        postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get Post object and use the values to update the UI
                recipes = new ArrayList<>();
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    recipes.add(data.getValue(Recipe.class));
                }
                //ListAdapter adapter =  new RecipeAdapter(getApplicationContext(),R.layout.list_view_recipe,recipes, mUser.getUid());
                ListAdapter adapter = new RecipeAdapter(getApplicationContext(), R.layout.list_view_recipe, recipes, mUser.getUid());
                listViewRecipes.setAdapter(adapter);
                listViewRecipes.invalidateViews();


                // ...
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
                // ...
            }
        };
        query.addListenerForSingleValueEvent(postListener);
    }
}
