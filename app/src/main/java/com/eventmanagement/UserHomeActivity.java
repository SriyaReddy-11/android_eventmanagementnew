package com.eventmanagement;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.eventmanagement.Adapters.UserRVAdapter;
import com.eventmanagement.Entities.Event;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class UserHomeActivity extends AppCompatActivity {
    DrawerLayout drawerLayout;
    NavigationView navigationView;

    RecyclerView eventsRecyclerView;
    UserRVAdapter rvAdapter;
    ArrayList<Event> events;
    DatabaseReference databaseReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_home);
        Toolbar toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawerLayout=findViewById(R.id.drawe_layout);
        navigationView=findViewById(R.id.nv); ActionBarDrawerToggle toggle=new
                ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.open,R.string.close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        eventsRecyclerView = findViewById(R.id.eventsrecyclerView);
        eventsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        rvAdapter = new UserRVAdapter(new UserRVAdapter.ClickListener() {
            @Override
            public void onClick(int position) {
                Intent i =new Intent(UserHomeActivity.this,EventDetailsActivity.class);
                i.putExtra("event",events.get(position));
                startActivity(i);
            }


        });
        rvAdapter.setItems(new ArrayList<>());

        eventsRecyclerView.setAdapter(rvAdapter);


        FirebaseDatabase db = FirebaseDatabase.getInstance();
        databaseReference = db.getReference(Event.class.getSimpleName());
        fetchItems();


        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.upcommingevents:

                        Intent intent =new Intent(UserHomeActivity.this,EventsListActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        startActivity(intent);

                        break;
                    case R.id.myevents:
                        Intent intent1 =new Intent(UserHomeActivity.this,MyEventsActivity.class);
                        intent1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        startActivity(intent1);

                        break;
                    case R.id.profile:
                        Intent p =new Intent(UserHomeActivity.this,CustomerProfileActivity.class);
                        p.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        startActivity(p);

                        break;

                    case R.id.logout:
                        Intent logout =new Intent(UserHomeActivity.this,CustomerLoginActivity.class);
//                        logout.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        startActivity(logout);
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
    public void onBackPressed() {
        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }else {
            super.onBackPressed();
        }
    }
}