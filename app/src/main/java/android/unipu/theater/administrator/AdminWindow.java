package android.unipu.theater.administrator;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.unipu.theater.R;
import android.unipu.theater.model.UserModel;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.navigation.NavigationView;

public class AdminWindow extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout adminDrawerLayout;
    private UserModel userModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_window);

        userModel = new UserModel();

        Intent intent = getIntent();
        String id = intent.getStringExtra("id");
        if(id != null)
            userModel.setKljuc(id);

        Toolbar adminToolbar = findViewById(R.id.adminToolbar);
        setSupportActionBar(adminToolbar);

        adminDrawerLayout = findViewById(R.id.adminDrawer_layout);
        NavigationView navigation = findViewById(R.id.adminNavigation_view);
        navigation.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,
                adminDrawerLayout, adminToolbar,
                R.string.navigation_open, R.string.navigation_close);

        adminDrawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        setListener();
    }

    private void setListener(){
        CardView cardPretrazivanje = (CardView) findViewById(R.id.cardPretrazivanjeAdmin);
        cardPretrazivanje.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminWindow.this, PerformancesActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_out_left,R.anim.slide_out_right);
                finish();
            }
        });

        CardView cardRezervacija = (CardView) findViewById(R.id.cardRezervacijaAdmin);
        cardRezervacija.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminWindow.this, ReservationActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_out_left,R.anim.slide_out_right);
                finish();
            }
        });

        CardView cardPredstava = (CardView) findViewById(R.id.cardPredstavaAdmin);
        cardPredstava.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminWindow.this, OfferActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_out_left,R.anim.slide_out_right);
                finish();
            }
        });

        CardView cardProfil = (CardView) findViewById(R.id.cardProfilAdmin);
        cardProfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminWindow.this,AdminProfile.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_out_left,R.anim.slide_out_right);
                finish();
            }
        });

        CardView cardCijene = (CardView) findViewById(R.id.cardCijenaAdmin);
        cardCijene.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminWindow.this, PricesActivtiy.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_out_left,R.anim.slide_out_right);
                finish();
            }
        });

        CardView cardPitanja = (CardView) findViewById(R.id.cardPitanjeAdmin);
        cardPitanja.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminWindow.this, QuestionActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_out_left,R.anim.slide_out_right);
                finish();
            }
        });
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        adminDrawerLayout.closeDrawer(GravityCompat.START);

        switch(menuItem.getItemId()){
            case R.id.pregledPonuda:{
                Intent intent = new Intent(AdminWindow.this, PerformancesActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_out_left,R.anim.slide_out_right);
                finish();
            }break;

            case R.id.rezerviraneKarte:{
                Intent intent = new Intent(AdminWindow.this, ReservationActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_out_left,R.anim.slide_out_right);
                finish();
            }break;

            case R.id.novaPonuda:{
                Intent intent = new Intent(AdminWindow.this, OfferActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_out_left,R.anim.slide_out_right);
                finish();
            }break;

            case R.id.postavkeProfila:{
                Intent intent = new Intent(AdminWindow.this,AdminProfile.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_out_left,R.anim.slide_out_right);
                finish();
            }break;

            case R.id.upravljanjeCijenama:{
                Intent intent = new Intent(AdminWindow.this, PricesActivtiy.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_out_left,R.anim.slide_out_right);
                finish();
            }break;

            case R.id.upravljanjeUpitima:{
                Intent intent = new Intent(AdminWindow.this, QuestionActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_out_left,R.anim.slide_out_right);
                finish();
            }break;

            case R.id.adminOdjava:{
                Intent intent = new Intent(AdminWindow.this, AdministratorLogin.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }break;
        }
        return false;
    }
}
