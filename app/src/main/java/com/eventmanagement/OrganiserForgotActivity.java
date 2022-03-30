package com.eventmanagement;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class OrganiserForgotActivity extends AppCompatActivity {

    EditText name;
    EditText email;

    EditText password;
    Button bt_reset;
    DatabaseReference databaseReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_organiser_forgot);

        name=findViewById(R.id.et_name);
        email=findViewById(R.id.et_email);

        password=findViewById(R.id.et_password);
        bt_reset=findViewById(R.id.bt_reset);

        bt_reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (name.getText().toString().isEmpty())
                {
                    Toast.makeText(OrganiserForgotActivity.this, "write your Name...", Toast.LENGTH_SHORT).show();
                }
                else if (email.getText().toString().isEmpty())
                {
                    Toast.makeText(OrganiserForgotActivity.this, "write your Email...", Toast.LENGTH_SHORT).show();
                }

                else if (password.getText().toString().isEmpty())
                {
                    Toast.makeText(OrganiserForgotActivity.this, "write your New Password...", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    final DatabaseReference RootRef;
                    RootRef = FirebaseDatabase.getInstance().getReference("Organiser");
                    //  Query query=RootRef.orderByChild("name").equalTo(et_USERNAME.getText().toString());


                    HashMap<String, Object> hashMap = new HashMap<>();
                    hashMap.put("password", password.getText().toString());


                    RootRef.child(name.getText().toString()).updateChildren(hashMap).addOnSuccessListener(suc ->
                    {
                        Toast.makeText(OrganiserForgotActivity.this, "Password updated successfully", Toast.LENGTH_SHORT).show();
                        Intent intent=new Intent(getApplicationContext(), OrganiserLoginActivity.class);
                        startActivity(intent);
                    }).addOnFailureListener(er ->
                    {
                        Toast.makeText(OrganiserForgotActivity.this, "" + er.getMessage(), Toast.LENGTH_SHORT).show();
                    });
                }
            }
        });



    }
}