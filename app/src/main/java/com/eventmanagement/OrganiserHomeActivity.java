package com.eventmanagement;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.eventmanagement.Adapters.RVAdapter;
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

public class OrganiserHomeActivity extends AppCompatActivity {
    DrawerLayout drawerLayout;
    NavigationView navigationView;

    RecyclerView eventsRecyclerView;
    RVAdapter rvAdapter;
    ArrayList<Event> events;
    DatabaseReference databaseReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_organiser_home);
        Toolbar toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawerLayout=findViewById(R.id.drawe_layout);
        navigationView=findViewById(R.id.nv); ActionBarDrawerToggle toggle=new
                ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.open,R.string.close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();


        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.createEvents:

                        Intent intent =new Intent(OrganiserHomeActivity.this,AddEventActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        startActivity(intent);

                        break;
                    case R.id.myevents:
                        Intent intent1 =new Intent(OrganiserHomeActivity.this,OrganiserEventsActivity.class);
                        intent1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        startActivity(intent1);

                        break;
                    case R.id.profile:
                        Intent p =new Intent(OrganiserHomeActivity.this,OrganiserProfileActivity.class);
                        p.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        startActivity(p);

                        break;

                    case R.id.logout:
                        Intent logout =new Intent(OrganiserHomeActivity.this,OrganiserLoginActivity.class);
//                        logout.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        startActivity(logout);

                        break;


//                    case R.id.log_out:
//                        navigationView.setCheckedItem(R.id.log_out);
//                        Intent intent2 =new Intent(findpeople.this,Login.class);
//                        intent2.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                        SharedPreferences sharedPreferences = getSharedPreferences(Constants.registeredpref, Context.MODE_PRIVATE);
//                        SharedPreferences.Editor et=sharedPreferences.edit();
//                        et.clear();
//                        et.apply();
//
//                        startActivity(intent2);
//
//                        break;

                }
                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
        });


        eventsRecyclerView = findViewById(R.id.eventsrecyclerView);
        eventsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        rvAdapter = new RVAdapter(new RVAdapter.ClickListener() {
            @Override
            public void onClick(int position) {
                Intent i =new Intent(OrganiserHomeActivity.this,AddEventActivity.class);
                i.putExtra("event",events.get(position));
                startActivity(i);
            }

            @Override
            public void delete(int position) {

                databaseReference.child(events.get(position).getKey()).removeValue().addOnSuccessListener(suc->{
                    Toast.makeText(OrganiserHomeActivity.this, "deleted Event",Toast.LENGTH_LONG).show();
                    rvAdapter.removeItem(position);

                }).addOnFailureListener(er->{
                    Toast.makeText(OrganiserHomeActivity.this, er.getMessage(),Toast.LENGTH_LONG).show();




                });
                Toast.makeText(OrganiserHomeActivity.this, events.get(position).getKey(),Toast.LENGTH_LONG).show();
            }

            @Override
            public void detailsPage(int position) {
                Intent i =new Intent(OrganiserHomeActivity.this,GuestsListActivity.class);
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
    @Override
    public void onBackPressed() {
        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }else {
            super.onBackPressed();
        }
    }
}