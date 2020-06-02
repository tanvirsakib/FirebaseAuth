package com.example.firebaseauth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText SignIneEmailEditText, SignInPasswordEditText;
    private Button signInButton;
    private TextView signupTextView;
    private ProgressBar progressBar;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.setTitle("Sign In");

        mAuth = FirebaseAuth.getInstance();

        SignIneEmailEditText = findViewById(R.id.SignInEmailEditText);
        SignInPasswordEditText = findViewById(R.id.SignInPasswordEditText);
        signInButton = findViewById(R.id.SignInButttonID);
        signupTextView = findViewById(R.id.SignUpTextID);
        progressBar = findViewById(R.id.progressbarID);

        signInButton.setOnClickListener(this);
        signupTextView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.SignInButttonID:
                userLogin();
                break;

            case R.id.SignUpTextID:
                Intent intent = new Intent(getApplicationContext(),SignUpActivity.class);
                startActivity(intent);
                break;
        }

    }

    private void userLogin() {
        String email = SignIneEmailEditText.getText().toString().trim();
        String password = SignInPasswordEditText.getText().toString().trim();

        if(email.isEmpty()){
            SignIneEmailEditText.setError("Enter an email address");
            SignIneEmailEditText.requestFocus();
            return;
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            SignIneEmailEditText.setError("Enter an valid email address");
            SignIneEmailEditText.requestFocus();
            return;
        }


        if(password.isEmpty()){
            SignInPasswordEditText.setError("Enter a password");
            SignInPasswordEditText.requestFocus();
            return;
        }


        if(password.length()<6){
            SignInPasswordEditText.setError("Minimum length of a password should be 6");
            SignInPasswordEditText.requestFocus();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);

        mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                progressBar.setVisibility(View.GONE);
                SignIneEmailEditText.setText("");
                SignInPasswordEditText.setText("");
                if (task.isSuccessful()){
                    finish();
                    Intent intent = new Intent(getApplicationContext(),ProfileActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                }else{
                    Toast.makeText(getApplicationContext(),"Login Unsuccessful",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
