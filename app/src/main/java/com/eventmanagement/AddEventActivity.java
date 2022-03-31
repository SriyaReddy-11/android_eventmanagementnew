package com.eventmanagement;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.eventmanagement.Entities.Event;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class AddEventActivity extends AppCompatActivity {
    DatabaseReference databaseReference;
    EditText name, desc, date, time, location;
    Button submit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);

        Event ievent = getIntent().getParcelableExtra("event");


        name = findViewById(R.id.et_name);
        desc = findViewById(R.id.et_desc);
        date = findViewById(R.id.et_date);
        time = findViewById(R.id.et_time);
        location=findViewById(R.id.et_loc);
        submit=findViewById(R.id.submir);
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        databaseReference = db.getReference(Event.class.getSimpleName());
      String uid=  FirebaseAuth.getInstance().getCurrentUser().getUid();

        if(ievent!=null){{
            System.out.println(ievent.getEventName());
            name.setText(ievent.getEventName());
            desc.setText(ievent.getDescription());
            date.setText(ievent.getDate());
            time.setText(ievent.getTime());
            location.setText(ievent.getLocation());
        }}

        submit.setOnClickListener(v -> {
            if(ievent!=null){


                        HashMap<String, Object> hashMap = new HashMap<>();
                        hashMap.put("eventName", name.getText().toString());
                        hashMap.put("date", date.getText().toString());
                        hashMap.put("description", desc.getText().toString());
                        hashMap.put("time", time.getText().toString());
                        hashMap.put("location", location.getText().toString());
                         databaseReference.child(ievent.getKey().toString()).updateChildren(hashMap).addOnSuccessListener(suc ->
                        {
                            Toast.makeText(AddEventActivity.this, "Record is updated", Toast.LENGTH_SHORT).show();

                            Intent i =new Intent(AddEventActivity.this,EventUpdatedActivity.class);
                            startActivity(i);

                            // finish();
                        }).addOnFailureListener(er ->
                        {
                             Toast.makeText(AddEventActivity.this, "" + er.getMessage(), Toast.LENGTH_SHORT).show();
                        });
                    }else {
                Event event = new Event(name.getText().toString(), desc.getText().toString(), date.getText().toString(), time.getText().toString(), location.getText().toString(), uid);
                databaseReference.push().setValue(event).addOnSuccessListener(suc -> {
                    Toast.makeText(AddEventActivity.this, "created", Toast.LENGTH_LONG).show();
                    Intent i =new Intent(AddEventActivity.this,EventSuccessActivity.class);
                    startActivity(i);
                }).addOnFailureListener(fail -> {
                    Toast.makeText(AddEventActivity.this, "Failed", Toast.LENGTH_LONG).show();
                });
            }



        });


    }
}

                           
