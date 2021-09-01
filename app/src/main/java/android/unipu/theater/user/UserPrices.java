package android.unipu.theater.user;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.unipu.theater.R;
import android.unipu.theater.model.PricesModel;
import android.unipu.theater.retrofit.IPrices;
import android.unipu.theater.retrofit.RetrofitClient;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class UserPrices extends AppCompatActivity {

    private TextView karteDjeca, karteStudenti, karteOdrasli, karteUmirovljenici,datumKarte,
                        premijera, danRezervacije;
    private IPrices iPrices;
    private PricesModel pricesModel;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_prices);

        initView();
        getPricesData();
    }

    private void initView(){

        karteDjeca = (TextView) findViewById(R.id.karteDjecaUser);
        karteOdrasli = (TextView) findViewById(R.id.karteOdrasliUser);
        karteStudenti = (TextView) findViewById(R.id.karteStudentiUser);
        karteUmirovljenici = (TextView) findViewById(R.id.karteUmirovljeniciUser);
        datumKarte = (TextView) findViewById(R.id.datumCijeneUser);
        premijera = (TextView) findViewById(R.id.premijeraCijeneUser);
        danRezervacije = (TextView) findViewById(R.id.danCijeneUser);
        pricesModel = new PricesModel();
        Retrofit retrofitClient = RetrofitClient.getInstance2();
        iPrices = retrofitClient.create(IPrices.class);

        toolbar = findViewById(R.id.toolbarUserPrices);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setWidget();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE
                | WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
    }

    private void setWidget(){
        toolbar.setNavigationOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(UserPrices.this, MainWindow.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void getPricesData(){
        Call<List<PricesModel>> call = iPrices.getPricesData();
        call.enqueue(new Callback<List<PricesModel>>() {
            @Override
            public void onResponse(Call<List<PricesModel>> call, Response<List<PricesModel>> response) {
                List<PricesModel> list = response.body();

                if(response.isSuccessful()){
                    for(int i=0; i<list.size(); i++) {
                        pricesModel.setDatum(list.get(i).getDatum());
                        pricesModel.setCijene_djeca(list.get(i).getCijeneDjeca());
                        pricesModel.setCijene_odrasli(list.get(i).getCijene_odrasli());
                        pricesModel.setCijene_studenti(list.get(i).getCijene_studenti());
                        pricesModel.setCijene_umirovljenici(list.get(i).getCijene_umirovljenici());
                        pricesModel.setDan(list.get(i).getDan());
                        pricesModel.setPremijera(list.get(i).getPremijera());
                        try { setClearText(); }
                        catch (ParseException e) { e.printStackTrace(); }
                    }
                }else Toast.makeText(getApplicationContext(),
                        "Nije moguće dohvatiti cijene!",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<List<PricesModel>> call, Throwable t) {
                t.getMessage();
                t.printStackTrace();
            }
        });
    }

    private void setClearText() throws ParseException {

        String kn = " kn";
        if(pricesModel.getCijeneDjeca()%1==0){
            karteDjeca.setText(String.valueOf((Math.round(pricesModel.getCijeneDjeca())+kn)));
        }else karteDjeca.setText(String.valueOf(pricesModel.getCijeneDjeca())+kn);

        if(pricesModel.getCijene_odrasli()%1==0) {
           karteOdrasli.setText(String.valueOf(Math.round(pricesModel.getCijene_odrasli()) + kn));
        }
        else karteOdrasli.setText(String.valueOf(pricesModel.getCijene_odrasli())+kn);

        if(pricesModel.getCijene_studenti()%1==0) {
            karteStudenti.setText(String.valueOf(Math.round(pricesModel.getCijene_studenti())+kn));
        }else karteStudenti.setText(String.valueOf(pricesModel.getCijene_studenti())+kn);

        if(pricesModel.getCijene_umirovljenici()%1==0) {
            karteUmirovljenici.setText(String.valueOf(Math.round(pricesModel.getCijene_umirovljenici())+kn));
        }else karteUmirovljenici.setText(String.valueOf(pricesModel.getCijene_umirovljenici())+kn);

        if(pricesModel.getDan()%1==0) {
            danRezervacije.setText(String.valueOf(Math.round(pricesModel.getDan())+kn));
        }else danRezervacije.setText(String.valueOf(pricesModel.getDan())+kn);

        if(pricesModel.getPremijera()%1==0) {
            premijera.setText(String.valueOf(Math.round(pricesModel.getPremijera())+kn));
        }else premijera.setText(String.valueOf(pricesModel.getPremijera())+kn);

        datumKarte.setText(convertDateToString(pricesModel.getDatum()));
    }

    public String convertDateToString(String date){
        String dateString = null;
        try { TimeZone zone = TimeZone.getTimeZone("UTC");
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
            SimpleDateFormat newFormat = new SimpleDateFormat("dd.MM.yyyy");
            simpleDateFormat.setTimeZone(zone);
            Date newDate = simpleDateFormat.parse(date);
            dateString = newFormat.format(newDate);
            return dateString;
        }catch(Exception e){ e.printStackTrace(); }
        return dateString;
    }
}
