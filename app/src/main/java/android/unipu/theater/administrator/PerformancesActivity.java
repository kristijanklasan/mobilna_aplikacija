package android.unipu.theater.administrator;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.unipu.theater.R;
import android.unipu.theater.fragmenti.administrator.PerformancesFragment;
import android.view.View;

import com.google.android.material.tabs.TabLayout;

public class PerformancesActivity extends AppCompatActivity {

    private String kategorija;
    private TabLayout tabLayout;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_performances);

        initView();
    }
    private void initView() {
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.addTab(tabLayout.newTab().setText("Drama"));
        tabLayout.addTab(tabLayout.newTab().setText("Komedija"));
        tabLayout.addTab(tabLayout.newTab().setText("Tragikomedija"));
        tabLayout.addTab(tabLayout.newTab().setText("Vodvilj"));
        tabLayout.addTab(tabLayout.newTab().setText("Glazba"));
        tabLayout.addTab(tabLayout.newTab().setText("Melodrama"));
        tabLayout.addTab(tabLayout.newTab().setText("Balet"));
        tabLayout.addTab(tabLayout.newTab().setText("Parodija"));
        tabLayout.addTab(tabLayout.newTab().setText("Opera"));
        toolbar = (Toolbar) findViewById(R.id.toolbarPerformance);
        setView();
    }

    private void setView(){
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PerformancesActivity.this,AdminWindow.class);
                startActivity(intent);
                finish();
            }
        });

        tabLayout.setOnTabSelectedListener(new TabLayout.BaseOnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                String position = (String) tab.getText();
                sendCategory(position);
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) { }

            @Override
            public void onTabReselected(TabLayout.Tab tab) { }
        });

        Bundle bundle = new Bundle();
        bundle.putString("kategorija", "Drama");
        PerformancesFragment frag = new PerformancesFragment();
        frag.setArguments(bundle);

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fragmentPerformances, frag);
        ft.commit();
    }

    private void sendCategory(String kategorija){
        Bundle bundle = new Bundle();
        bundle.putString("kategorija", kategorija);
        PerformancesFragment fragment = new PerformancesFragment();
        fragment.setArguments(bundle);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fragmentPerformances, fragment);
        ft.commit();
    }
}
