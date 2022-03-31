package com.eventmanagement;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class OrganiserLoginActivity extends AppCompatActivity {
    TextView registerButton,tvforgot;
    EditText email, password;
    Button login;
    Button customerLogin;
    FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_organiser_login);

        mAuth = FirebaseAuth.getInstance();
        registerButton = findViewById(R.id.registerButton);
        email = findViewById(R.id.et_email);
        password = findViewById(R.id.et_password);

        tvforgot=findViewById(R.id.tvforgot);
        tvforgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(OrganiserLoginActivity.this, OrganiserForgotActivity.class);
                startActivity(i);
            }
        });


        login = findViewById(R.id.btn_login);
        customerLogin = findViewById(R.id.custLogin);

        customerLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(OrganiserLoginActivity.this, CustomerLoginActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(i);
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signInWithEmailAndPassword(email.getText().toString(),password.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isComplete()){
                            Toast.makeText(OrganiserLoginActivity.this,email.getText() + password.getText().toString(),Toast.LENGTH_LONG).show();
                            Intent i = new Intent(OrganiserLoginActivity.this, OrganiserHomeActivity.class);
                            SharedPreferences sp = getSharedPreferences("AA", 0);
                            SharedPreferences.Editor et = sp.edit();
                            et.putString("uname", email.getText().toString());
                            et.commit();

                            startActivity(i);
                        }else{
                            Toast.makeText(OrganiserLoginActivity.this,"failed to Login",Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(OrganiserLoginActivity.this, OrganizerRegisterActivity.class);
                startActivity(i);
            }
        });
    }
}
