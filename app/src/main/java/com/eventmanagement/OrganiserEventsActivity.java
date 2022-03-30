package com.eventmanagement;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.eventmanagement.Adapters.RVAdapter;
import com.eventmanagement.Entities.Event;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class OrganiserEventsActivity extends AppCompatActivity {
    RecyclerView eventsRecyclerView;
    RVAdapter rvAdapter;
    ArrayList<Event> events;
    DatabaseReference databaseReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_organiser_events);
        eventsRecyclerView = findViewById(R.id.eventsrecyclerView);
        eventsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        rvAdapter = new RVAdapter(new RVAdapter.ClickListener() {
            @Override
            public void onClick(int position) {
                Intent i =new Intent(OrganiserEventsActivity.this,AddEventActivity.class);
                i.putExtra("event",events.get(position));
                startActivity(i);
            }

            @Override
            public void delete(int position) {

                databaseReference.child(events.get(position).getKey()).removeValue().addOnSuccessListener(suc->{
                    Toast.makeText(OrganiserEventsActivity.this, "deleted Event",Toast.LENGTH_LONG).show();
                    rvAdapter.removeItem(position);

                }).addOnFailureListener(er->{
                    Toast.makeText(OrganiserEventsActivity.this, er.getMessage(),Toast.LENGTH_LONG).show();




                });
                Toast.makeText(OrganiserEventsActivity.this, events.get(position).getKey(),Toast.LENGTH_LONG).show();
            }

            @Override
            public void detailsPage(int position) {
                Intent i =new Intent(OrganiserEventsActivity.this,GuestsListActivity.class);
                i.putExtra("event",events.get(position));
                startActivity(i);
            }
        });
        rvAdapter.setItems(new ArrayList<>());

        eventsRecyclerView.setAdapter(rvAdapter);


        FirebaseDatabase db = FirebaseDatabase.getInstance();
        databaseReference = db.getReference(Event.class.getSimpleName());
        fetchItems();

    }

    public void fetchItems(){
        String uid=  FirebaseAuth.getInstance().getCurrentUser().getUid();

        Query query=databaseReference.orderByChild("orgId").equalTo(uid);

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                events=new ArrayList<>();
                for(DataSnapshot data:snapshot.getChildren()){
                    Event event=data.getValue(Event.class);
                    event.setKey(data.getKey());
                    events.add(event);
                }
                rvAdapter.setItems(events);
                rvAdapter.notifyDataSetChanged();
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