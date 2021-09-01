package android.unipu.theater.administrator;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.unipu.theater.R;
import android.unipu.theater.fragmenti.administrator.NewUserFragment;
import android.unipu.theater.fragmenti.administrator.ProfileDetail;
import android.unipu.theater.model.UserModel;
import android.view.View;

import com.google.android.material.tabs.TabLayout;

public class AdminProfile extends AppCompatActivity {

    private Toolbar toolbar;
    private UserModel userModel;
    private TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_profile);

        initView();
    }

    private void initView(){
        userModel = new UserModel();
        tabLayout = (TabLayout) findViewById(R.id.tabAdmin);
        toolbar = (Toolbar) findViewById(R.id.toolbarAdminProfile);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        setElements();
    }

    private void setElements(){
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(AdminProfile.this, AdminWindow.class);
                startActivity(i);
                finish();
            }
        });

        tabLayout.getTabAt(0).select();
        setTabText(0);
        tabLayout.getTabAt(0).setIcon(R.drawable.ic_account_circle_black_24dp);
        tabLayout.getTabAt(0).getIcon().setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_IN);
        tabLayout.getTabAt(1).setIcon(R.drawable.ticket);
        tabLayout.getTabAt(1).getIcon().setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_IN);
        tabLayout.setOnTabSelectedListener(new TabLayout.BaseOnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position =  tab.getPosition();
                setTabText(position);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) { }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {int position =  tab.getPosition();
                setTabText(position);
            }
        });
    }

    private void setTabText(int position){
        Bundle bundle = new Bundle();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

        int id = Integer.parseInt(userModel.getKljuc());

        if(position == 0){
            bundle.putInt("id", id);
            ProfileDetail profileDetail = new ProfileDetail();
            profileDetail.setArguments(bundle);
            ft.replace(R.id.frameAdminProfile, profileDetail).commit();
        }

        else if(position == 1){
            NewUserFragment userFragment = new NewUserFragment();
            ft.replace(R.id.frameAdminProfile, userFragment).commit();
        }
    }
}
