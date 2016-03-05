package igor.study.layout;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.firebase.client.Firebase;

public class MainActivity extends AppCompatActivity {
    private final String DATABASE_URL =  "https://glaring-heat-9487.firebaseio.com/";
    private Firebase myFirebase;
    private EditText nameEditText;
    private EditText ageEditText;
    private Button addToBaseButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Firebase.setAndroidContext(this);

        myFirebase = new Firebase(DATABASE_URL);
        nameEditText = (EditText) findViewById(R.id.name);
        ageEditText = (EditText) findViewById(R.id.age);
        addToBaseButton = (Button) findViewById(R.id.addToBaseButton);

        nameEditText.requestFocus();

        addToBaseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = nameEditText.getText().toString();
                int age = Integer.parseInt(ageEditText.getText().toString());

                myFirebase.child(name).setValue(age);

                nameEditText.setText("");
                ageEditText.setText("");
                nameEditText.requestFocus();
            }
        });
    }
}
