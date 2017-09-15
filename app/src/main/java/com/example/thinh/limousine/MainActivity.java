package com.example.thinh.limousine;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import limousine.ChooseSeatFragment;
import limousine.RouteListFragment;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportFragmentManager().beginTransaction().add(R.id.main, new RouteListFragment()).commit();
    }

    public void replaceFragment(ChooseSeatFragment chooseSeatFragment) {
        getSupportFragmentManager().beginTransaction().replace(R.id.main, chooseSeatFragment).commit();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

}
