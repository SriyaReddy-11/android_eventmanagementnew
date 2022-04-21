package com.eventmanagement;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.eventmanagement.Adapters.RVAdapter;
import com.eventmanagement.Adapters.UserRVAdapter;
import com.eventmanagement.Entities.Event;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class EventsListActivity extends AppCompatActivity {
    RecyclerView eventsRecyclerView;
    UserRVAdapter rvAdapter;
    ArrayList<Event> events;
    DatabaseReference databaseReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events_list);
        getSupportActionBar().setTitle("Events");
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        eventsRecyclerView = findViewById(R.id.eventsrecyclerView);
        eventsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        rvAdapter = new UserRVAdapter(new UserRVAdapter.ClickListener() {
            @Override
            public void onClick(int position) {
                Intent i =new Intent(EventsListActivity.this,EventDetailsActivity.class);
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

       // Query query=databaseReference.orderByChild("orgId").equalTo(uid);

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
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
