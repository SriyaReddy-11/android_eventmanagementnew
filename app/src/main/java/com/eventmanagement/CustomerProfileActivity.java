package com.eventmanagement;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.eventmanagement.Entities.CustomerEnitity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class CustomerProfileActivity extends AppCompatActivity {

    EditText et_email,et_name,et_password,et_phone_no;
    Button btn_updateprofile;
    ProgressDialog loadingBar;
    DatabaseReference RootRef;
    private String parentDbName = "Customers";
    String uName;
    SharedPreferences sp;
    String name,email,phone,password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_profile);
        getSupportActionBar().setTitle("My Profile");
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        RootRef = FirebaseDatabase.getInstance().getReference("Customers");
        sp = getSharedPreferences("AA", 0);
        uName = sp.getString("uname", "-");


        et_name = (EditText) findViewById(R.id.et_name);
        et_email = (EditText) findViewById(R.id.et_email);
        et_password = (EditText) findViewById(R.id.et_password);
        et_phone_no = (EditText) findViewById(R.id.et_phone_no);

        GetUserValues();

    }

    private void GetUserValues() {

        Query query=RootRef.orderByChild("email").equalTo(uName);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // CustomerEnitity usersData = snapshot.child(parentDbName).child(uName).getValue(CustomerEnitity.class);

                for (DataSnapshot data : snapshot.getChildren()) {
                    CustomerEnitity usersData = data.getValue(CustomerEnitity.class);
                    et_name.setText(usersData.getName());
                    et_email.setText(usersData.getEmail());
                    et_password.setText(usersData.getPassword());
                    et_phone_no.setText(usersData.getPhoneNo());
                }


            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}