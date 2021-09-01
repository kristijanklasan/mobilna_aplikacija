package android.unipu.theater.administrator;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.unipu.theater.R;
import android.unipu.theater.adapter.OfferAdapter;
import android.unipu.theater.model.OfferModel;
import android.unipu.theater.model.TheaterModel;
import android.unipu.theater.retrofit.IOffer;
import android.unipu.theater.retrofit.ITheater;
import android.unipu.theater.retrofit.RetrofitClient;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ReservationActivity extends AppCompatActivity {

    private IOffer iOffer;
    private ITheater iTheater;
    private ArrayList<OfferModel> arrayOffer;
    private Toolbar toolbar;
    private Spinner spinnerPonuda;
    private OfferModel offerModel;
    private TextView textPrikaz;
    ListView listOffer;
    private TheaterModel theaterModel;
    private static OfferAdapter adapter;
    private RadioGroup radioGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservation);

        initView();
    }

    private void initView(){
        offerModel = new OfferModel();
        theaterModel = new TheaterModel();
        offerModel.setUvjet(0);
        offerModel.setPozicija(0);
        listOffer = (ListView) findViewById(R.id.listOffer);
        toolbar = (Toolbar) findViewById(R.id.toolbarOfferDetail);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        textPrikaz = (TextView) findViewById(R.id.prikazPonuda);
        spinnerPonuda = (Spinner) findViewById(R.id.spinnerPonuda);
        radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        arrayOffer = new ArrayList<>();
        setView();

        if(getApplicationContext() !=null)
            getShow();
    }

    private void setView(){
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ReservationActivity.this, AdminWindow.class);
                startActivity(i);
            }
        });

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                View radio = radioGroup.findViewById(checkedId);
                int index = radioGroup.indexOfChild(radio);
                offerModel.setUvjet(index);
                getData(offerModel.getNaziv(),offerModel.getPozicija(),index);
            }
        });
    }


    private void setSpinner(){
        String [] arrayPredstava = new String[theaterModel.getNazivPredstave().size()];
        arrayPredstava = theaterModel.getNazivPredstave().toArray(arrayPredstava);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(),
                android.R.layout.simple_spinner_item, arrayPredstava);

        adapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
        spinnerPonuda.setAdapter(adapter);
        spinnerPonuda.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ((TextView) parent.getChildAt(0)).setTextColor(Color.WHITE);

                if(parent.getItemAtPosition(position).equals("")){ }
                else{
                    String item = parent.getItemAtPosition(position).toString();
                    textPrikaz.setText(item);
                    offerModel.setNaziv(item);
                    offerModel.setPozicija(position);
                    getData(item,position,offerModel.getUvjet());
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });
    }

    private String getCurrentDateToString(){
        DateFormat date = new SimpleDateFormat("dd.MM.yyyy");
        Calendar calendar = Calendar.getInstance();
        String dateText = date.format(calendar.getTime());
        return dateText;
    }

    private Date switchStringDateFormat(String date) {
        Date dateFormat = null;
        try { SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd.MM.yyyy");
            dateFormat = simpleDateFormat.parse(date);
            return dateFormat;
        }catch (Exception e){ e.printStackTrace(); }
        return dateFormat;
    }

    private boolean checkDate(int num, Date current, Date database){
        switch(num){
            case 1: {
                if(current.before(database) || current.equals(database))
                    return true;
                else return false;
            }
            case 2: {
                if(current.after(database)) return true;
                else return false;
            }
        }
        return true;
    }

    private void getData(String naziv, int broj, final int uvjet){
        arrayOffer.clear();
        Retrofit retrofitClient = RetrofitClient.getInstance2();
        iOffer = retrofitClient.create(IOffer.class);
        String date = getCurrentDateToString();
        final Date dateCurrent = switchStringDateFormat(date);

        Call<List<OfferModel>> call = null;
        if(broj == 0) call = iOffer.getAllDataForAdapter();
        else call = iOffer.getDataForAdapter(naziv);

        call.enqueue(new Callback<List<OfferModel>>() {
            @Override
            public void onResponse(Call<List<OfferModel>> call, Response<List<OfferModel>> response) {
                if(response.isSuccessful()){
                    List<OfferModel> list = response.body();

                    if(list.size() == 0){
                        Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content),
                                "Trenutno nema ponuda za prikazati!", Snackbar.LENGTH_SHORT);
                        View snackbar_view = snackbar.getView();
                        TextView snackbar_text = (TextView) snackbar_view.findViewById(R.id.snackbar_text);
                        snackbar_text.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.ic_close_black_24dp,0);
                        snackbar_text.setGravity(Gravity.CENTER);
                        snackbar.show();
                    }

                    for(int i=0; i<list.size(); i++) {
                        Date dateDatabase = switchStringDateFormat(list.get(i).getDatum_prikazivanja());
                        if (checkDate(uvjet, dateCurrent, dateDatabase)) {
                            offerModel.setNaziv(list.get(i).getNaziv());
                            offerModel.setKategorija(list.get(i).getKategorija());
                            offerModel.setId(list.get(i).getId());
                            offerModel.setDatum_prikazivanja(list.get(i).getDatum_prikazivanja());
                            offerModel.setVrijeme_prikazivanja(list.get(i).getVrijeme_prikazivanja());
                            arrayOffer.add(new OfferModel(offerModel.getNaziv(), offerModel.getKategorija(),
                                    offerModel.getDatum_prikazivanja(), offerModel.getVrijeme_prikazivanja(), offerModel.getId()));
                        }
                    }

                    adapter = new OfferAdapter(arrayOffer,getApplicationContext());
                    listOffer.setAdapter(adapter);
                    listOffer.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            OfferModel offerModel = arrayOffer.get(position);
                            Intent intent = new Intent(ReservationActivity.this,ReservationDetail.class);
                            intent.putExtra("ponuda",offerModel.getId());
                            startActivity(intent);
                        }
                    });
                }
            }

            @Override
            public void onFailure(Call<List<OfferModel>> call, Throwable t) {
                t.getMessage();
                t.printStackTrace();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();
        return super.onOptionsItemSelected(item);
    }

    private void getShow(){
        final ArrayList<String> tempList = new ArrayList<>();
        theaterModel.setNaziv("Sve ponude");
        tempList.add(theaterModel.getNaziv());

        Retrofit retrofitClient = RetrofitClient.getInstance2();
        iTheater = retrofitClient.create(ITheater.class);

        Call<List<TheaterModel>> call = iTheater.dohvatPredstava();
        call.enqueue(new Callback<List<TheaterModel>>() {
            @Override
            public void onResponse(Call<List<TheaterModel>> call, Response<List<TheaterModel>> response) {
                if(response.isSuccessful()){
                    List<TheaterModel> list = response.body();
                    for(int i=0; i<list.size(); i++){
                        theaterModel.setNaziv(list.get(i).getNaziv());
                        tempList.add(theaterModel.getNaziv());
                    }
                    theaterModel.setNazivPredstave(tempList);
                    setSpinner();
                }
            }

            @Override
            public void onFailure(Call<List<TheaterModel>> call, Throwable t) {
                t.printStackTrace();
                t.getMessage();
            }
        });
    }
}
