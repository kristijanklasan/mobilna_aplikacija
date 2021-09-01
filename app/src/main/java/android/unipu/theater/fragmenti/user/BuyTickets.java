package android.unipu.theater.fragmenti.user;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.unipu.theater.R;
import android.unipu.theater.model.OfferModel;
import android.unipu.theater.model.PricesModel;
import android.unipu.theater.model.TheaterModel;
import android.unipu.theater.retrofit.IOffer;
import android.unipu.theater.retrofit.IReservation;
import android.unipu.theater.retrofit.RetrofitClient;
import android.unipu.theater.user.BookingTickets;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class BuyTickets extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private Spinner spinnerPredstava,spinnerDatum,spinnerVrijeme;
    private TextView naslov, kategorija,datum,vrijeme,ulaznice, dvorana, karte;
    private CheckBox djeca,studenti,odrasli,umirovljenici;
    private Button btnSjedala;
    private TheaterModel theaterModel;
    private OfferModel offerModel;
    private PricesModel pricesModel;
    private IReservation iReservation;
    private ArrayList<String> listData;
    private IOffer iOffer;
    private String [] podaci;
    private String mParam1;
    private String mParam2;

    public BuyTickets() {
        // Required empty public constructor
    }

    public static BuyTickets newInstance(String param1, String param2) {
        BuyTickets fragment = new BuyTickets();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_buy_tickets, container, false);
        initView(view);
        getTickets();
        return view;
    }

    private void initView(View view){
        theaterModel = new TheaterModel();
        offerModel = new OfferModel();
        pricesModel = new PricesModel();
        podaci = new String[3];
        listData = new ArrayList<>();
        btnSjedala = (Button) view.findViewById(R.id.sjedalaButton);
        spinnerPredstava = (Spinner) view.findViewById(R.id.spinnerPredstava);
        spinnerDatum = (Spinner) view.findViewById(R.id.spinnerDatum);
        spinnerVrijeme = (Spinner) view.findViewById(R.id.spinnerVrijeme);
        naslov = (TextView) view.findViewById(R.id.naslovTickets);
        kategorija = (TextView) view.findViewById(R.id.kategorijaTickets);
        datum = (TextView) view.findViewById(R.id.datumTickets);
        vrijeme = (TextView) view.findViewById(R.id.vrijemeTickets);
        ulaznice = (TextView) view.findViewById(R.id.ulazniceTickets);
        dvorana = (TextView) view.findViewById(R.id.dvoranaTickets);
        djeca = (CheckBox) view.findViewById(R.id.djecaBox);
        odrasli = (CheckBox) view.findViewById(R.id.odrasliBox);
        studenti = (CheckBox) view.findViewById(R.id.studentiBox);
        umirovljenici = (CheckBox) view.findViewById(R.id.umirovljeniciBox);
        karte  =(TextView) view.findViewById(R.id.karteTickets);
        getPrices();
        getShowName(view);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    private void setElements(){
        circleNumber(pricesModel.getCijene_odrasli());
        djeca.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                odrasli.setChecked(false);
                studenti.setChecked(false);
                umirovljenici.setChecked(false);
                circleNumber(pricesModel.getCijeneDjeca());
            }
        });

        odrasli.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                djeca.setChecked(false);
                studenti.setChecked(false);
                umirovljenici.setChecked(false);
                circleNumber(pricesModel.getCijene_odrasli());
            }
        });

        studenti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                odrasli.setChecked(false);
                djeca.setChecked(false);
                umirovljenici.setChecked(false);
                circleNumber(pricesModel.getCijene_studenti());
            }
        });

        umirovljenici.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                odrasli.setChecked(false);
                studenti.setChecked(false);
                djeca.setChecked(false);
                circleNumber(pricesModel.getCijene_umirovljenici());
            }
        });

        btnSjedala.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listData = new ArrayList<>();
                listData.add(String.valueOf(offerModel.getId()));
                listData.add(karte.getText().toString());

                if(checkFields()) {
                    Intent intent = new Intent(getContext(), BookingTickets.class);
                    intent.putStringArrayListExtra("podaci", listData);
                    startActivity(intent);
                }else Toast.makeText(getContext(), "Popunite sva polja za nastavak!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void circleNumber(float number){
        if(number%1 == 0){
            int newNum = (int) number;
            karte.setText(String.valueOf(newNum));
        }
    }

    private boolean checkFields(){
        if(naslov.getText().toString().isEmpty() || kategorija.getText().toString().isEmpty() ||
        datum.getText().toString().isEmpty() || vrijeme.getText().toString().isEmpty() ||
        ulaznice.getText().toString().isEmpty() || karte.getText().toString().isEmpty() || dvorana.getText().toString().isEmpty()){
            return false;
        }else return true;
    }
    private void getTickets(){
        Bundle bundle = this.getArguments();
        if(bundle!=null){
            ArrayList<String> list = bundle.getStringArrayList("karte");
            for(int i=0; i<list.size(); i++){
                podaci[i] = list.get(i);
            }
        }
    }

    private void setSpinner(View view){
        String [] arrayNaziv = new String[theaterModel.getNazivPredstave().size()];
        arrayNaziv = theaterModel.getNazivPredstave().toArray(arrayNaziv);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(view.getContext(),
                android.R.layout.simple_spinner_item, arrayNaziv);
        adapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
        spinnerPredstava.setAdapter(adapter);

        for(int i=0; i<spinnerPredstava.getCount(); i++){
            if(spinnerPredstava.getItemAtPosition(i).toString().equals(podaci[0])){
                spinnerPredstava.setSelection(i);
                break;
            }
        }
        spinnerPredstava.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ((TextView) parent.getChildAt(0)).setTextColor(Color.BLACK);
                if(parent.getItemAtPosition(position).equals("")){ }
                else{
                    String item = parent.getItemAtPosition(position).toString();
                    getDate(item);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });
    }

    private void setSpinnerDatum(final String naziv, List<String> listDatum){
        String [] arrayDatum = new String[listDatum.size()];
        for(int i=0; i<listDatum.size(); i++){
            arrayDatum[i] = listDatum.get(i);
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(),
                android.R.layout.simple_spinner_item, arrayDatum);
        adapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
        spinnerDatum.setAdapter(adapter);

        for(int i=0; i<spinnerDatum.getCount(); i++){
            if(spinnerDatum.getItemAtPosition(i).toString().equals(podaci[1])){
                spinnerDatum.setSelection(i);
                break;
            }
        }
        spinnerDatum.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ((TextView) parent.getChildAt(0)).setTextColor(Color.WHITE);

                if(parent.getItemAtPosition(position).equals("")){ }
                else{
                    String item = parent.getItemAtPosition(position).toString();
                    getTime(naziv,item);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });
    }

    private void setSpinnerVrijeme(ArrayList<String> listaVrijeme){
        String[] arrayVrijeme = new String[listaVrijeme.size()];

        for(int j = 0; j<listaVrijeme.size(); j++)
            arrayVrijeme[j] = listaVrijeme.get(j);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(),
                android.R.layout.simple_spinner_item,arrayVrijeme);
        adapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
        spinnerVrijeme.setAdapter(adapter);
        for(int i=0; i<spinnerVrijeme.getCount(); i++){
            if(spinnerVrijeme.getItemAtPosition(i).toString().equals(podaci[2])){
                spinnerVrijeme.setSelection(i);
                break;
            }
        }
        spinnerVrijeme.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id){
                ((TextView) parent.getChildAt(0)).setTextColor(Color.WHITE);
                if(parent.getItemAtPosition(position).equals("")){}
                else{
                    String item = parent.getItemAtPosition(position).toString();
                    getData(offerModel.getNaziv(),offerModel.getDatum_prikazivanja(),item);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });
    }

    private void getShowName(final View view){
        final ArrayList<String> showList = new ArrayList<>();
        Retrofit retrofit = RetrofitClient.getInstance2();
        iReservation = retrofit.create(IReservation.class);

        Call<List<TheaterModel>> call = iReservation.getShowName();
        call.enqueue(new Callback<List<TheaterModel>>() {
            @Override
            public void onResponse(Call<List<TheaterModel>> call, Response<List<TheaterModel>> response) {
                if(response.isSuccessful()){
                    List<TheaterModel> list = response.body();
                    for(int i=0; i<list.size(); i++){
                        theaterModel.setNaziv(list.get(i).getNaziv());
                        theaterModel.setId(list.get(i).getId());
                        showList.add(theaterModel.getNaziv());
                    }
                    theaterModel.setNazivPredstave(showList);
                    setSpinner(view);
                }
            }

            @Override
            public void onFailure(Call<List<TheaterModel>> call, Throwable t) {
                t.getMessage();
                t.printStackTrace();
            }
        });
    }

    private void getDate(final String naziv){
        final ArrayList<String> lista = new ArrayList<>();

        Retrofit retrofit = RetrofitClient.getInstance2();
        iReservation = retrofit.create(IReservation.class);

        Call<List<OfferModel>> call = iReservation.getDate(naziv);
        call.enqueue(new Callback<List<OfferModel>>() {
            @Override
            public void onResponse(Call<List<OfferModel>> call, Response<List<OfferModel>> response) {
                if(response.isSuccessful()){
                    List<OfferModel> list = response.body();

                    for(int i=0; i<list.size(); i++){
                        offerModel.setDatum_prikazivanja(list.get(i).getDatum_prikazivanja());
                        lista.add(offerModel.getDatum_prikazivanja());
                    }
                    setSpinnerDatum(naziv,lista);
                }
            }
            @Override
            public void onFailure(Call<List<OfferModel>> call, Throwable t) {
                t.getMessage();
                t.printStackTrace();
            }
        });
    }

    private void getTime(String naziv, String datum){
        final ArrayList<String> vrijeme = new ArrayList<>();
        offerModel.setNaziv(naziv);
        offerModel.setDatum_prikazivanja(datum);

        Retrofit retrofit = RetrofitClient.getInstance2();
        iReservation = retrofit.create(IReservation.class);

        Call<List<OfferModel>> call = iReservation.getTime(naziv, datum);
        call.enqueue(new Callback<List<OfferModel>>() {
            @Override
            public void onResponse(Call<List<OfferModel>> call, Response<List<OfferModel>> response) {
                if(response.isSuccessful()){
                    List<OfferModel> list = response.body();
                    for(int i=0; i<list.size(); i++){
                        offerModel.setVrijeme_prikazivanja(list.get(i).getVrijeme_prikazivanja());
                        vrijeme.add(offerModel.getVrijeme_prikazivanja());
                    }
                    setSpinnerVrijeme(vrijeme);
                }
            }

            @Override
            public void onFailure(Call<List<OfferModel>> call, Throwable t) {
                t.getMessage();
                t.printStackTrace();
            }
        });
    }

    private void getData(String naziv,String datum,String vrijeme){
        Retrofit retrofit = RetrofitClient.getInstance2();
        iReservation = retrofit.create(IReservation.class);

        Call<List<OfferModel>> call = iReservation.getData(naziv,datum,vrijeme);
        call.enqueue(new Callback<List<OfferModel>>() {
            @Override
            public void onResponse(Call<List<OfferModel>> call, Response<List<OfferModel>> response) {
                if(response.isSuccessful()){
                    List<OfferModel> list = response.body();
                    for(int i=0; i<list.size(); i++){
                        offerModel.setId(list.get(i).getId());
                        offerModel.setNaziv(list.get(i).getNaziv());
                        offerModel.setKategorija(list.get(i).getKategorija());
                        offerModel.setTrenutno_ulaznica(list.get(i).getTrenutno_ulaznica());
                        offerModel.setDatum_prikazivanja(list.get(i).getDatum_prikazivanja());
                        offerModel.setVrijeme_prikazivanja(list.get(i).getVrijeme_prikazivanja());
                        offerModel.setDvorana(list.get(i).getDvorana());
                    }
                    setData();
                }
            }

            @Override
            public void onFailure(Call<List<OfferModel>> call, Throwable t) {
                t.getMessage();
                t.printStackTrace();
            }
        });
    }

    private void getPrices(){
        Retrofit retrofit = RetrofitClient.getInstance2();
        iReservation = retrofit.create(IReservation.class);

        Call<List<PricesModel>> call = iReservation.getPrices();
        call.enqueue(new Callback<List<PricesModel>>() {
            @Override
            public void onResponse(Call<List<PricesModel>> call, Response<List<PricesModel>> response) {
                if(response.isSuccessful()){
                    List<PricesModel> list = response.body();
                    for(int i=0; i<list.size(); i++){
                        pricesModel.setId(list.get(i).getId());
                        pricesModel.setCijene_djeca(list.get(i).getCijeneDjeca());
                        pricesModel.setCijene_odrasli(list.get(i).getCijene_odrasli());
                        pricesModel.setCijene_studenti(list.get(i).getCijene_studenti());
                        pricesModel.setCijene_umirovljenici(list.get(i).getCijene_umirovljenici());
                    }
                    setElements();
                }
            }

            @Override
            public void onFailure(Call<List<PricesModel>> call, Throwable t) {
                t.printStackTrace();
                t.getMessage();
            }
        });
    }

    private void setData(){
        naslov.setText(offerModel.getNaziv());
        kategorija.setText(offerModel.getKategorija());
        datum.setText(offerModel.getDatum_prikazivanja());
        vrijeme.setText(offerModel.getVrijeme_prikazivanja()+"h");
        ulaznice.setText(String.valueOf(offerModel.getTrenutno_ulaznica()));
        dvorana.setText(offerModel.getDvorana());
    }
}
