package com.example.firebaseauth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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
import com.google.firebase.auth.FirebaseAuthUserCollisionException;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener {


    private EditText SignUpEmailEditText, SignUpPasswordEditText;
    private Button signUpButton;
    private TextView signInTextView;
    private ProgressBar progressBar;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        this.setTitle("Sign up");

        mAuth = FirebaseAuth.getInstance();

        SignUpEmailEditText = findViewById(R.id.SignUpEmailEditText);
        SignUpPasswordEditText = findViewById(R.id.SignUpPasswordEditText);
        signUpButton = findViewById(R.id.SignUpButttonID);
        signInTextView = findViewById(R.id.SignInTextID);
        progressBar = findViewById(R.id.progressbarID);

        signUpButton.setOnClickListener(this);
        signInTextView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.SignUpButttonID:
                userRegister();
                break;

            case R.id.SignInTextID:
                Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                startActivity(intent);
                break;
        }

    }

    private void userRegister() {
        String email = SignUpEmailEditText.getText().toString().trim();
        String password = SignUpPasswordEditText.getText().toString().trim();

        if(email.isEmpty()){
            SignUpEmailEditText.setError("Enter an email address");
            SignUpEmailEditText.requestFocus();
            return;
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            SignUpEmailEditText.setError("Enter an valid email address");
            SignUpEmailEditText.requestFocus();
            return;
        }


        if(password.isEmpty()){
            SignUpPasswordEditText.setError("Enter a password");
            SignUpPasswordEditText.requestFocus();
            return;
        }
        progressBar.setVisibility(View.VISIBLE);


        if(password.length()<6){
            SignUpPasswordEditText.setError("Minimum length of a password should be 6");
            SignUpPasswordEditText.requestFocus();
            return;
        }

        mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                progressBar.setVisibility(View.GONE);
                if (task.isSuccessful()) {
                    Toast.makeText(getApplicationContext(),"Sign Up Successful",Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                    startActivity(intent);
                } else {

                    if(task.getException() instanceof FirebaseAuthUserCollisionException){
                        Toast.makeText(getApplicationContext(),"Already registerd with this email",Toast.LENGTH_SHORT).show();
                    }else {
                        Toast.makeText(getApplicationContext(),"Error: "+task.getException(),Toast.LENGTH_SHORT).show();
                    }

                }
            }
        });

    }


}
