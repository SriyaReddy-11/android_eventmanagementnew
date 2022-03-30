package com.eventmanagement;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

public class EventSuccessActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_success);

        final int ScreenDisplay = 2000;
        Thread t1=new Thread(){
            int wait1=0;
            public void run(){
                try{
                    while(wait1<=ScreenDisplay )
                    {
                        sleep(100);
                        wait1+=100;
                    }
                }
                catch(Exception e){
                    e.printStackTrace();
                }
                finally{
                    Intent intentg= new Intent(EventSuccessActivity.this, OrganiserHomeActivity.class);
                    startActivity(intentg);
                    finish();

                }
            }
        };
        t1.start();
    }
}