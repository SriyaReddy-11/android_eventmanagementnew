package com.eventmanagement;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.eventmanagement.Entities.CustomerEnitity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class OrganizerRegisterActivity extends AppCompatActivity {
    EditText name;
    EditText email;
    EditText phoneNo;
    EditText password;
    Button createAccount;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_organizer_register);
        name=findViewById(R.id.et_name);
        email=findViewById(R.id.et_email);
        phoneNo=findViewById(R.id.et_phone_no);
        password=findViewById(R.id.et_password);
        createAccount=findViewById(R.id.bt_reg);
        mAuth = FirebaseAuth.getInstance();


        createAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.createUserWithEmailAndPassword(email.getText().toString(),password.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            CustomerEnitity customerEnitity=new CustomerEnitity(name.getText().toString(),email.getText().toString(),password.getText().toString(),phoneNo.getText().toString(),"Male");

                            FirebaseDatabase.getInstance().getReference("Organiser").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .setValue(customerEnitity).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        Toast.makeText(OrganizerRegisterActivity.this,"Organiser Registered Successfull",Toast.LENGTH_LONG).show();
                                        finish();

                                    }else{
                                        Toast.makeText(OrganizerRegisterActivity.this,"Failed to register",Toast.LENGTH_LONG).show();

                                    }
                                }
                            });

                        }else{
                            Toast.makeText(OrganizerRegisterActivity.this,"Failed to register",Toast.LENGTH_LONG).show();

                        }
                    }
                });
            }
        });
    }
}
