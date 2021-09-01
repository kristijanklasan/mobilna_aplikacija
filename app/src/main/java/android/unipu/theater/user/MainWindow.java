package android.unipu.theater.user;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.unipu.theater.MainActivity;
import android.unipu.theater.R;
import android.unipu.theater.model.UserModel;
import android.unipu.theater.retrofit.IKorisnik;
import android.unipu.theater.retrofit.RetrofitClient;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.navigation.NavigationView;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MainWindow extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawerLayout;
    private IKorisnik iUser;
    private UserModel userModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_window);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle drawerToggle = new ActionBarDrawerToggle(this,
                drawerLayout, toolbar, R.string.navigation_open,R.string.navigation_close);
        drawerLayout.addDrawerListener(drawerToggle);
        drawerToggle.syncState();

        userModel = new UserModel();
        setListener();
        getIntentData();
    }

    private void getIntentData(){
        try { Intent intent = getIntent();
            String email = intent.getStringExtra("email");
            getId(email);
        }catch(Exception e){ e.printStackTrace(); }
    }

    private void getId(String email){
        Retrofit retrofitClient = RetrofitClient.getInstance2();
        iUser = retrofitClient.create(IKorisnik.class);

        Call<List<UserModel>> call = iUser.getEmail(email);
        call.enqueue(new Callback<List<UserModel>>() {
            @Override
            public void onResponse(Call<List<UserModel>> call, Response<List<UserModel>> response) {
                List<UserModel> list = response.body();

                for(int i = 0; i<list.size(); i++){
                    userModel.setKljuc(list.get(i).getId());
                }
            }
            @Override
            public void onFailure(Call<List<UserModel>> call, Throwable t) {
                t.getMessage();
            }
        });
    }

    private void setListener(){
        CardView cardPretrazivanje = (CardView) findViewById(R.id.cardPretrazivanjeUser);
        cardPretrazivanje.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainWindow.this, UserPerformances.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_out_left, R.anim.slide_out_right);
                finish();
            }
        });

        CardView cardRezervacija = (CardView) findViewById(R.id.cardRezervacijaUser);
        cardRezervacija.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainWindow.this, UserReservation.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_out_left,R.anim.slide_out_right);
                finish();
            }
        });

        CardView cardPregledRezervacija = (CardView) findViewById(R.id.cardPregledKartiUser);
        cardPregledRezervacija.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainWindow.this, ReservationOverview.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_out_left,R.anim.slide_out_right);
                finish();
            }
        });

        CardView cardProfil = (CardView) findViewById(R.id.cardRacunUser);
        cardProfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainWindow.this, ProfileActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_out_left,R.anim.slide_out_right);
                finish();
            }
        });

        CardView cardCijene = (CardView) findViewById(R.id.cardCijeneUser);
        cardCijene.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainWindow.this, UserPrices.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_out_left,R.anim.slide_out_right);
                finish();
            }
        });
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

        drawerLayout.closeDrawer(GravityCompat.START);

        switch(menuItem.getItemId()){
            case R.id.pregledRezervacija:{
                Intent intent = new Intent(MainWindow.this, ReservationOverview.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_out_left,R.anim.slide_out_right);
                finish();

            }break;

            case R.id.popisPredstava:{
                Intent intent = new Intent(MainWindow.this, UserPerformances.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_out_left, R.anim.slide_out_right);
                finish();

            }break;

            case R.id.rezervacijaUlaznica:{
                Intent intent = new Intent(MainWindow.this, UserReservation.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_out_left,R.anim.slide_out_right);
                finish();

            }break;

            case R.id.korisnickiRacun:{
                Intent intent = new Intent(MainWindow.this, ProfileActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_out_left,R.anim.slide_out_right);
                finish();
            }break;

            case R.id.cijeneUlaznica:{
                Intent intent = new Intent(MainWindow.this, UserPrices.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_out_left,R.anim.slide_out_right);
                finish();
            }break;

            case R.id.najcescaPitanja:{
                Intent intent = new Intent(MainWindow.this, UserQuestionActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_out_left,R.anim.slide_out_right);
                finish();
            }break;

            case R.id.informacijeAplikacija:{
                Intent intent = new Intent(MainWindow.this, AboutApplication.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_out_left,R.anim.slide_out_right);
                finish();
            }break;

            case R.id.odjava:{
                Intent intent = new Intent(MainWindow.this, UserLogin.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_out_left,R.anim.slide_out_right);
                finish();
            }break;
        }
        return false;
    }
}
