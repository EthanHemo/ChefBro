package johnbryce.com.chefbro;

import android.app.DownloadManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Comment;

import java.util.ArrayList;

public class RetrieveDataActivity extends AppCompatActivity {

    String TAG = "Firebase";
    FirebaseDatabase database;
    DatabaseReference myRef;
    Query query;
    ValueEventListener postListener;
    ArrayList<String> students;
    TextView textViewArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_retrieve_data);

        students = new ArrayList<>();

        textViewArray = (TextView)findViewById(R.id.TextViewArray);
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("test-8aded/Students");
        query = myRef.orderByValue().equalTo("Ethan");


        postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get Post object and use the values to update the UI
                PostRerieve(dataSnapshot);
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



        /*S*/

    }

    public void AddStudent(View view) {
        String key = myRef.push().getKey();
        DatabaseReference myNewRef = database.getReference("test-8aded/Students/Student" + key);
        myNewRef.setValue("Ethan");
    }

    public void PostRerieve(DataSnapshot dataSnapshot)
    {
        students.clear();
        for(DataSnapshot data : dataSnapshot.getChildren())
        {
            students.add(data.getValue(String.class));
        }

        String temp = "";
        for(String x : students)
        {
            temp += x + "\n";
        }
        //textViewArray.setText(""+temp);
        textViewArray.setText(temp + dataSnapshot.getRef().toString());

        //myRef.removeEventListener(postListener);
    }
}
