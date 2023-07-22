package com.example.trio;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MenuItem;

import com.example.trio.bloodDonor.BloodFragment;
import com.example.trio.databinding.ActivityHomeBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class home extends AppCompatActivity {

    ActivityHomeBinding binding;
    BottomNavigationView bottomNavigationView;
    Trio_Home home=new Trio_Home();
    BloodFragment blood=new BloodFragment();
    UserFragment user=new UserFragment();
    private static int HOME_TRIO_ID= R.id.home_trio;
    private static int BLOOD_TRIO_ID = R.id.blood_trio;
    private static int USER_TRIO_ID = R.id.user_trio;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportFragmentManager().beginTransaction().replace(R.id.container,home).commit();
        bottomNavigationView=findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == BLOOD_TRIO_ID) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.container, blood).commit();
                    return true;
                }
                if (item.getItemId() == USER_TRIO_ID) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.container, user).commit();
                    return true;
                }
                else{
                    getSupportFragmentManager().beginTransaction().replace(R.id.container,home).commit();
                    return true;
                }
            }
        });
    }
}