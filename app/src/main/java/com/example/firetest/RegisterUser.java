package com.example.firetest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.PatternMatcher;
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
import com.google.firebase.database.FirebaseDatabase;

public class RegisterUser extends AppCompatActivity implements View.OnClickListener {

    private TextView banner;
    private Button registerUser;
    private EditText edit_full_name, edit_age, edit_email, edit_pass;
    private ProgressBar progressBar;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_user);
        mAuth = FirebaseAuth.getInstance();
        banner = (TextView) findViewById(R.id.banner_register);
        banner.setOnClickListener(this);
        registerUser = (Button) findViewById(R.id.btn_register);
        registerUser.setOnClickListener(this);
        edit_full_name = (EditText) findViewById(R.id.edit_full_name);
        edit_age = (EditText) findViewById(R.id.edit_age);
        edit_email = (EditText) findViewById(R.id.edit_email);
        edit_pass = (EditText) findViewById(R.id.edit_pass);
        progressBar = (ProgressBar) findViewById(R.id.progressBar_register);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.banner_register:
                startActivity(new Intent(this, MainActivity.class));
                break;
            case R.id.btn_register:
                register_user();
                break;
        }
    }

    private void register_user() {
        String email = edit_email.getText().toString().trim();
        String password = edit_pass.getText().toString().trim();
        String age = edit_age.getText().toString().trim();
        String fullName = edit_full_name.getText().toString().trim();
        if(email.isEmpty()||password.isEmpty()||age.isEmpty()||fullName.isEmpty()){
            Toast.makeText(this, "Please fill out all the above fields", Toast.LENGTH_LONG).show();
            return;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            edit_email.setError("Please provide valid email");
            edit_email.requestFocus();
            return;
        }
        progressBar.setVisibility(View.VISIBLE);
        mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Employee emp = new Employee(fullName,age,email);
                    FirebaseDatabase.getInstance().getReference("Employee")
                            .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                            .setValue(emp).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        Toast.makeText(RegisterUser.this, "Employee has been registered!", Toast.LENGTH_SHORT).show();
                                        progressBar.setVisibility(View.GONE);
                                    }else{
                                        Toast.makeText(RegisterUser.this, "Failed to register!", Toast.LENGTH_SHORT).show();
                                        progressBar.setVisibility(View.GONE);
                                    }
                                }
                            });
                }else{
                    Toast.makeText(RegisterUser.this, "Failed to register!", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                }
            }
        });
    }
}