package android.unipu.theater.administrator;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.unipu.theater.R;
import android.unipu.theater.fragmenti.administrator.ScheduleShow;
import android.unipu.theater.fragmenti.administrator.TheaterPlay;
import android.view.MenuItem;
import android.view.WindowManager;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.List;

public class OfferActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offer);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE|
                WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        getSupportFragmentManager().beginTransaction().replace(R.id.containerUnos,new TheaterPlay()).commit();
        BottomNavigationView bottomView = findViewById(R.id.navigation_unos);
        bottomView.setOnNavigationItemSelectedListener(navListener);
    }

    public boolean onOptionsItemSelected(MenuItem item){
        Intent i = new Intent(getApplicationContext(), OfferActivity.class);
        startActivityForResult(i, 0);
        return true;
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem){
            List<Fragment> fragments = getSupportFragmentManager().getFragments();
            for(Fragment fragment : fragments) {
                switch (menuItem.getItemId()) {
                    case R.id.list1: {
                        if(!(fragment instanceof  TheaterPlay))
                            fragment = new TheaterPlay();
                    }
                    break;
                    case R.id.list2: {
                        if(!(fragment instanceof  ScheduleShow))
                            fragment = new ScheduleShow();
                    }
                    break;
                }
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.containerUnos,fragment).commit();
            }
            return true;
        }
    };
}
