package android.unipu.theater.user;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.unipu.theater.R;
import android.unipu.theater.fragmenti.user.BookingSeat;
import android.unipu.theater.fragmenti.user.BuyTickets;
import android.unipu.theater.fragmenti.user.TermsReservation;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

public class ReserveTickets extends AppCompatActivity {

    private Toolbar toolbar;
    private ArrayList<String> dataList;
    private ArrayList<Integer> redList,oznakaList,sjedaloList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reserve_tickets);
        initView();
    }

    private void initView(){
        toolbar = (Toolbar) findViewById(R.id.toolbarUserTickets);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE |
                WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        BookingSeat seat = new BookingSeat();
        getSupportFragmentManager().beginTransaction().
                replace(R.id.containerTickets,seat).commit();

        BottomNavigationView bottomNavigationView = findViewById(R.id.navigation_tickets);
        bottomNavigationView.setOnNavigationItemSelectedListener(navListener);

        setElements();
        sendDataToFragment();
    }

    private void setElements(){
        dataList = new ArrayList<>();
        redList = new ArrayList<>();
        sjedaloList = new ArrayList<>();
        oznakaList = new ArrayList<>();
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ReserveTickets.this, MainWindow.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void sendDataToFragment(){
        Intent intent = getIntent();
        dataList = intent.getStringArrayListExtra("podaci");
        redList = intent.getIntegerArrayListExtra("red");
        sjedaloList = intent.getIntegerArrayListExtra("sjedalo");
        oznakaList = intent.getIntegerArrayListExtra("oznaka");

        Bundle bundle = new Bundle();
        bundle.putStringArrayList("podaci", dataList);
        bundle.putIntegerArrayList("red",redList);
        bundle.putIntegerArrayList("sjedalo",sjedaloList);
        bundle.putIntegerArrayList("oznaka",oznakaList);
        BookingSeat booking = new BookingSeat();
        booking.setArguments(bundle);
        getSupportFragmentManager().beginTransaction().replace(R.id.containerTickets,booking).commit();
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
                        .replace(R.id.containerTickets,fragment).commit();
            }
            return true;
        }
    };
}
