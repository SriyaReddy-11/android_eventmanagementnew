package com.eventmanagement;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Toast;

import com.eventmanagement.Adapters.GuestAdapter;
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

public class GuestsListActivity extends AppCompatActivity {
    RecyclerView guestRecylcerView;
    DatabaseReference databaseReference;
    GuestAdapter guestAdapter;
    ArrayList<EventStudent> events = new ArrayList<>();
    Event ievent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guests_list);
         ievent = getIntent().getParcelableExtra("event");
        Toast.makeText(GuestsListActivity.this,ievent.getKey(),Toast.LENGTH_LONG).show();
        guestRecylcerView=findViewById(R.id.guestRecyclerView);

        guestRecylcerView.setLayoutManager(new LinearLayoutManager(this));

        guestAdapter=new GuestAdapter(new GuestAdapter.ClickListener() {
            @Override
            public void delete(int position) {
                databaseReference.child(events.get(position).getKey()).removeValue().addOnSuccessListener(suc->{
                    Toast.makeText(GuestsListActivity.this, "deleted Event",Toast.LENGTH_LONG).show();
                    guestAdapter.removeItem(position);

                }).addOnFailureListener(er->{
                    Toast.makeText(GuestsListActivity.this, er.getMessage(),Toast.LENGTH_LONG).show();




                });
            }
        });
        guestRecylcerView.setAdapter(guestAdapter);
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        databaseReference = db.getReference(EventStudent.class.getSimpleName());

        fetchItems();
    }

    public void fetchItems(){
        String uid=  FirebaseAuth.getInstance().getCurrentUser().getUid();



        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                events=new ArrayList<>();
                for(DataSnapshot data:snapshot.getChildren()){
                    EventStudent event=data.getValue(EventStudent.class);
                    event.setKey(data.getKey());
                if(event.getEvent().getKey().equals(ievent.getKey())){
                        Toast.makeText(GuestsListActivity.this,event.getCustomerName(),Toast.LENGTH_LONG).show();
                    events.add(event);
                }


                }


                guestAdapter.setItems(events);
                guestAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    @Override
    protected void onResume() {
        super.onResume();
        fetchItems();
    }
}
