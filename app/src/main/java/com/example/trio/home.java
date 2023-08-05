    package com.example.trio;

    import android.os.Bundle;
    import android.view.MenuItem;

    import androidx.annotation.NonNull;
    import androidx.appcompat.app.AppCompatActivity;

    import com.example.trio.Storage.Storage;
    import com.example.trio.bloodDonor.BloodFragment;
    import com.example.trio.databinding.ActivityHomeBinding;
    import com.google.android.material.bottomnavigation.BottomNavigationView;
    import com.google.android.material.navigation.NavigationBarView;

    public class home extends AppCompatActivity {

        ActivityHomeBinding binding;
        BottomNavigationView bottomNavigationView;
        Trio_Home home=new Trio_Home();
        admin_Request re=new admin_Request();
        BloodFragment blood=new BloodFragment();
        UserFragment user=new UserFragment();
    //    committeFragment com=new committeFragment();
        newPost post=new newPost();
        Storage store=new Storage();
        private static int HOME_TRIO_ID= R.id.home_trio;
        private static int REQUEST_TRIO_ID= R.id.request;
        private static int BLOOD_TRIO_ID = R.id.blood_trio;
        private static int NEW_POST_ID=R.id.post;
    //    private static int  COMMITEE_ID=R.id.commitee;
        private static int USER_TRIO_ID = R.id.user_trio;
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            binding = ActivityHomeBinding.inflate(getLayoutInflater());
            setContentView(binding.getRoot());
            getSupportFragmentManager().beginTransaction().replace(R.id.container,home).commit();
            bottomNavigationView=findViewById(R.id.bottom_navigation);
            int role=getIntent().getIntExtra("role1",0);
            int role2=getIntent().getIntExtra("role2",0);
            if(role!=0 || role2!=0){
                bottomNavigationView.getMenu().findItem(R.id.request).setVisible(true);
                bottomNavigationView.getMenu().findItem(R.id.post).setVisible(true);
            }
            else{
                bottomNavigationView.getMenu().findItem(R.id.request).setVisible(false);
                bottomNavigationView.getMenu().findItem(R.id.post).setVisible(false);
            }
            if(role!=0){
                store.saveRole(role);
            }
            bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    if (item.getItemId() == BLOOD_TRIO_ID) {
                        getSupportFragmentManager().beginTransaction().replace(R.id.container, blood).commit();
                        return true;
                    }
                    if(item.getItemId()==REQUEST_TRIO_ID){
                        getSupportFragmentManager().beginTransaction().replace(R.id.container,re).commit();
                        return true;
                    }
                    if(item.getItemId()==NEW_POST_ID){
                        getSupportFragmentManager().beginTransaction().replace(R.id.container,post).commit();
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