package com.eventmanagement;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.eventmanagement.Entities.BookEntity;
import com.eventmanagement.Entities.Event;
import com.eventmanagement.Entities.EventStudent;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class EventDetailsActivity extends AppCompatActivity {
    TextView header;
    TextView desc,eventName;
    Button submit, btn_book,btn_share;
    DatabaseReference databaseReference;
    String key=null;
    Event ievent;
    ImageView image_view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_details);
        getSupportActionBar().setTitle("Event Details");
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
       ievent = getIntent().getParcelableExtra("event");

        header = findViewById(R.id.eventName);
        desc = findViewById(R.id.desc);
        submit = findViewById(R.id.btn_join);
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        databaseReference = db.getReference(EventStudent.class.getSimpleName());
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        EventStudent eventStudent = new EventStudent(ievent.getKey(), ievent, uid, FirebaseAuth.getInstance().getCurrentUser().getEmail());

        btn_book = findViewById(R.id.btn_book);
        eventName=(TextView)findViewById(R.id.eventName);
        image_view=findViewById(R.id.image_view);
        Glide.with(this).load(ievent.getImageUrl()).into(image_view);


        btn_share=(Button) findViewById(R.id.btn_share);
        btn_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent sendIntent =new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT,"Greetings From Event App\n"+"---------------------------------------\n"+"Event Name : "+eventName.getText().toString()+"\n");
                sendIntent.setType("text/plain");
                Intent shareIntent = Intent.createChooser(sendIntent,null);
                startActivity(shareIntent);
            }
        });

       getSubscriptionDetails(uid);
       getBookingDetails(uid);



//        if (ievent.getUpperKey() != null) {
//            submit.setText("unsubscribe");
//        }

        if (ievent != null) {
            header.setText(ievent.getEventName());
            desc.setText(ievent.getDescription());
            submit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (submit.getText().equals("Subscribe")) {
                        databaseReference.push().setValue(eventStudent).addOnSuccessListener(suc -> {
                             Toast.makeText(EventDetailsActivity.this, "created", Toast.LENGTH_LONG).show();
                            Intent intentg = new Intent(EventDetailsActivity.this, UserHomeActivity.class);
                            startActivity(intentg);
                            // finish();
                        }).addOnFailureListener(fail -> {
                            Toast.makeText(EventDetailsActivity.this, "Failed", Toast.LENGTH_LONG).show();
                        });
                    } else {
                        databaseReference.child(key).removeValue().addOnSuccessListener(suc -> {
                            Toast.makeText(EventDetailsActivity.this, "deleted Event", Toast.LENGTH_LONG).show();
                            finish();

                        }).addOnFailureListener(er -> {
                            Toast.makeText(EventDetailsActivity.this, er.getMessage(), Toast.LENGTH_LONG).show();


                        });
                    }
                }
            });

            BookEntity bookEntity = new BookEntity(ievent, uid, FirebaseAuth.getInstance().getCurrentUser().getEmail(), "pending", ievent.getOrgId());

            btn_book.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(btn_book.getText().equals("Booked")){
                        Toast.makeText(EventDetailsActivity.this,"Already Booked",Toast.LENGTH_LONG).show();
                        return;
                    }
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

    public boolean getSubscriptionDetails(String uid) {
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference  databaseReference = db.getReference(EventStudent.class.getSimpleName());
        Query query = databaseReference.orderByChild("customer").equalTo(uid);
        final boolean[] status = {false};
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot data : snapshot.getChildren()) {
                    EventStudent event=data.getValue(EventStudent.class);
                    event.setKey(data.getKey());

                    if(event.getEvent().getKey().equals(ievent.getKey())){
                        key=data.getKey();
                        status[0] = true;
                        submit.setText("unsubscribe");

                    }

                }



            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        return status[0];
    }


    public boolean getBookingDetails(String uid) {
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = db.getReference(BookEntity.class.getSimpleName());
        Query query = databaseReference.orderByChild("customer").equalTo(uid);
        final boolean[] status = {false};
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot data : snapshot.getChildren()) {
                    BookEntity event=data.getValue(BookEntity.class);
                    event.setKey(data.getKey());

                    if(event.getEvent().getKey().equals(ievent.getKey())){
                        status[0] = true;
                        btn_book.setText("Booked");
                     //   Toast.makeText(EventDetailsActivity.this, "booked", Toast.LENGTH_LONG).show();
//                        if(!event.getStatus().equals("pending"))
//                        {
//                           // status[0] = true;
//                            btn_book.setText("Booked");
//                        }
                    }

                }




            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        return status[0];
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
