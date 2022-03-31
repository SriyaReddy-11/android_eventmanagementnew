package com.eventmanagement;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.eventmanagement.Entities.BookEntity;
import com.eventmanagement.Entities.Event;
import com.eventmanagement.Entities.EventStudent;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class EventDetailsActivity extends AppCompatActivity {
TextView header;
TextView desc;
Button submit,btn_book;
    DatabaseReference databaseReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_details);
        Event ievent = getIntent().getParcelableExtra("event");

        header=findViewById(R.id.eventName);
        desc=findViewById(R.id.desc);
        submit=findViewById(R.id.btn_join);
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        databaseReference = db.getReference(EventStudent.class.getSimpleName());
        String uid=  FirebaseAuth.getInstance().getCurrentUser().getUid();
        EventStudent eventStudent=new EventStudent(ievent,uid,FirebaseAuth.getInstance().getCurrentUser().getEmail());

        btn_book=findViewById(R.id.btn_book);



        if(ievent.getUpperKey()!=null){
            submit.setText("unsubscribe");
        }

        if(ievent!=null){
            header.setText(ievent.getEventName());
            desc.setText(ievent.getDescription());
            submit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (ievent.getUpperKey() == null) {
                        databaseReference.push().setValue(eventStudent).addOnSuccessListener(suc -> {
                           // Toast.makeText(EventDetailsActivity.this, "created", Toast.LENGTH_LONG).show();
                            Intent intentg= new Intent(EventDetailsActivity.this, UserHomeActivity.class);
                            startActivity(intentg);
                            // finish();
                        }).addOnFailureListener(fail -> {
                            Toast.makeText(EventDetailsActivity.this, "Failed", Toast.LENGTH_LONG).show();
                        });
                    }else{
                        databaseReference.child(ievent.getUpperKey()).removeValue().addOnSuccessListener(suc->{
                            Toast.makeText(EventDetailsActivity.this, "deleted Event",Toast.LENGTH_LONG).show();
                            finish();

                        }).addOnFailureListener(er->{
                            Toast.makeText(EventDetailsActivity.this, er.getMessage(),Toast.LENGTH_LONG).show();



                        });
                    }
                }
            });

            BookEntity bookEntity = new BookEntity(ievent, uid, FirebaseAuth.getInstance().getCurrentUser().getEmail(),"pending");

            btn_book.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FirebaseDatabase db = FirebaseDatabase.getInstance();
                    databaseReference = db.getReference(BookEntity.class.getSimpleName());

                    databaseReference.push().setValue(bookEntity).addOnSuccessListener(suc -> {
                        Toast.makeText(EventDetailsActivity.this, "Event Booked Successfully", Toast.LENGTH_LONG).show();
                        finish();
                    }).addOnFailureListener(fail -> {
                        Toast.makeText(EventDetailsActivity.this, "Failed", Toast.LENGTH_LONG).show();
                    });
                }
            });
        }
    }
}
