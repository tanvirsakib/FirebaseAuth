package com.example.firebaseauth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ProfileActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private EditText nameEditText,ageEditText;
    private Button saveButton,loadDataButton;
    DatabaseReference databaseReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        mAuth = FirebaseAuth.getInstance();

        databaseReference = FirebaseDatabase.getInstance().getReference("students");
        saveButton = findViewById(R.id.SaveButtonId);
        loadDataButton = findViewById(R.id.loadDataButtonId);
        nameEditText = findViewById(R.id.nameTextId);
        ageEditText = findViewById(R.id.ageTextId);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveData();
            }
        });

        loadDataButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileActivity.this,DetailsActivity.class);
                startActivity(intent);
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_layout,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId()==R.id.signOutMenuId){
            FirebaseAuth.getInstance().signOut();
            finish();
            Intent intent = new Intent(getApplicationContext(),MainActivity.class);
            startActivity(intent);
        }

        if (item.getItemId()==R.id.addImageMenuId){
            Intent intent = new Intent(getApplicationContext(),AddImage_Activity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }


    private void saveData() {
        String name = nameEditText.getText().toString().trim();
        String age = ageEditText.getText().toString().trim();

        String key = databaseReference.push().getKey();

        Student student = new Student(name,age);
        databaseReference.child(key).setValue(student);
        nameEditText.setText("");
        ageEditText.setText("");
        Toast.makeText(getApplicationContext(),"Saved",Toast.LENGTH_SHORT).show();

    }
}
