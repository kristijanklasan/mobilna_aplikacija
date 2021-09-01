package android.unipu.theater.user;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.unipu.theater.R;
import android.unipu.theater.fragmenti.user.BuyTickets;
import android.unipu.theater.fragmenti.user.TermsReservation;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

public class UserReservation extends AppCompatActivity {

    private Toolbar userReservation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_reservation);

        initView();
    }

    private void initView(){
        userReservation = (Toolbar) findViewById(R.id.toolbarUserReservation);
        setSupportActionBar(userReservation);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE |
                WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        Bundle bundle = new Bundle();
        BuyTickets buy = new BuyTickets();
        ArrayList<String> list = getDataTicket();

        if(list.size() != 0){
            bundle.putStringArrayList("karte",getDataTicket());
            buy.setArguments(bundle);
        }
        getSupportFragmentManager().beginTransaction().
                replace(R.id.containerReservation,buy).commit();

        BottomNavigationView bottomNavigationView = findViewById(R.id.navigation_reservation);
        bottomNavigationView.setOnNavigationItemSelectedListener(navListener);
        setElements();
    }

    private void setElements(){
        userReservation.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserReservation.this, MainWindow.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private ArrayList<String> getDataTicket(){
        Bundle bundle = getIntent().getExtras();
        ArrayList<String> list = new ArrayList<>();
        if(bundle != null) {
            list = bundle.getStringArrayList("ulaznice");
            return list;
        }
        return list;
    }

    public boolean onOptionsItemSelected(MenuItem item){
        Intent i = new Intent(getApplicationContext(), UserReservation.class);
        startActivityForResult(i, 0);
        finish();
        return true;
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem){
            List<Fragment> fragments = getSupportFragmentManager().getFragments();
            for(Fragment fragment : fragments) {
                switch (menuItem.getItemId()) {
                    case R.id.list1: {
                        if (!(fragment instanceof BuyTickets)) {
                            fragment = new BuyTickets();
                        }
                    }break;

                    case R.id.list2: {
                        if(!(fragment instanceof TermsReservation)){
                            fragment = new TermsReservation();
                        }
                    }break;
                }

                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.containerReservation,fragment).commit();
            }
            return true;
        }
    };
}
