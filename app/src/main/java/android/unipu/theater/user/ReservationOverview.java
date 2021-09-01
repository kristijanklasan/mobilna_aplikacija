package android.unipu.theater.user;

import android.content.Intent;
import android.os.Bundle;
import android.unipu.theater.R;
import android.unipu.theater.fragmenti.user.FavoritesOverview;
import android.unipu.theater.fragmenti.user.ReservationList;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.List;

public class ReservationOverview extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservation_overview);
        initView();
    }

    private void initView(){
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE|
                WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        getSupportFragmentManager().beginTransaction().replace(R.id.contentReservation,new FavoritesOverview()).commit();

        BottomNavigationView bottomView = findViewById(R.id.navigation_pregledRezervacija);
        bottomView.setOnNavigationItemSelectedListener(navListener);

        Toolbar toolbar = findViewById(R.id.toolbarReservation);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(ReservationOverview.this, MainWindow.class);
                startActivity(intent);
                finish();
            }
        });
    }

    public boolean onOptionsItemSelected(MenuItem item){
        Intent i = new Intent(getApplicationContext(), ReservationOverview.class);
        startActivityForResult(i, 0);
        return true;
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem){
            List<Fragment> fragments = getSupportFragmentManager().getFragments();
            for(Fragment fragment :fragments) {
                switch (menuItem.getItemId()) {
                    case R.id.pregledRezervacije: {
                        if (!(fragment instanceof ReservationList)) {
                            fragment = new ReservationList();
                        }
                    }break;

                    case R.id.mojIzbor: {
                        if (!(fragment instanceof FavoritesOverview)) {
                            fragment = new FavoritesOverview();
                        }
                    }break;
                }
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.contentReservation, fragment).commit();
            }
            return true;
        }
    };
}
