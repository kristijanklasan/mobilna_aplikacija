package android.unipu.theater.fragmenti.user;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.unipu.theater.R;
import android.unipu.theater.model.OfferModel;
import android.unipu.theater.model.PricesModel;
import android.unipu.theater.model.ReservationModel;
import android.unipu.theater.model.UserModel;
import android.unipu.theater.retrofit.IKorisnik;
import android.unipu.theater.retrofit.IPrices;
import android.unipu.theater.retrofit.IReservation;
import android.unipu.theater.retrofit.RetrofitClient;
import android.unipu.theater.user.ReservationOverview;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class BookingSeat extends Fragment {

    private Button btnRezerviraj;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private LinearLayout ticketLayout;
    private UserModel userModel;
    private PricesModel pricesModel;
    private IPrices iPrices;
    private ReservationModel reservationModel;
    private OfferModel offerModel;
    private IReservation iReservation;
    private TextView naziv,datum,vrijeme,cijena,ulaznice,korisnik,stavka,kolicina,iznos,cijenaIzracun
            ,konacnaCijena, dan, kolicinaDan, cijenaDan, ukupnoDan, premijera, kolicinaPremijera,
            cijenaPremijera, ukupnoPremijera;
    private RelativeLayout relativeRezerviraj;
    private IKorisnik iKorisnik;
    public Context context;
    private String mParam1;
    private String mParam2;

    public BookingSeat() {
        // Required empty public constructor
    }

    public static BookingSeat newInstance(String param1, String param2) {
        BookingSeat fragment = new BookingSeat();
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
        View view = inflater.inflate(R.layout.fragment_booking_seat, container, false);
        initView(view);
        getData(view);
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    private void getData(View view){
        try{ Bundle bundle = getArguments();
            if(bundle!=null){
                ArrayList<String> dataList = bundle.getStringArrayList("podaci");
                ArrayList<Integer> redList = getArguments().getIntegerArrayList("red");
                ArrayList<Integer> sjedaloList = getArguments().getIntegerArrayList("sjedalo");
                ArrayList<Integer> oznakaList = getArguments().getIntegerArrayList("oznaka");
                cijena.setText(dataList.get(1)+" kn");
                reservationModel.setRedLista(redList);
                reservationModel.setSjedaloLista(sjedaloList);
                reservationModel.setOznakaLista(oznakaList);
                reservationModel.setCijena(Float.parseFloat(dataList.get(1)));
                reservationModel.setId_raspored(Integer.parseInt(dataList.get(0)));
                getMainData(reservationModel.getId_raspored());
                numberList(redList,sjedaloList,oznakaList,view);
            }
        }catch(Exception e){ e.printStackTrace(); }
    }

    private void initView(View view){
        offerModel = new OfferModel();
        pricesModel = new PricesModel();
        reservationModel = new ReservationModel();
        userModel = new UserModel();
        naziv = (TextView) view.findViewById(R.id.nazivTickets);
        datum = (TextView) view.findViewById(R.id.datumFinal);
        vrijeme = (TextView) view.findViewById(R.id.vrijemeFinal);
        cijena = (TextView) view.findViewById(R.id.cijenaFinal);
        ulaznice = (TextView) view.findViewById(R.id.ulazniceFinal);
        korisnik = (TextView) view.findViewById(R.id.korisnikFinal);
        stavka = (TextView) view.findViewById(R.id.stavkaFinal);
        kolicina = (TextView) view.findViewById(R.id.kolicinaFinal);
        iznos = (TextView)view.findViewById(R.id.iznosFinal);
        cijenaIzracun = (TextView) view.findViewById(R.id.cijena);
        konacnaCijena = (TextView) view.findViewById(R.id.iznosFullFinal);
        ticketLayout = (LinearLayout) view.findViewById(R.id.ticketLayout);
        relativeRezerviraj = (RelativeLayout) view.findViewById(R.id.relativeRezerviraj);
        dan = (TextView) view.findViewById(R.id.stavkaDanFinal);
        kolicinaDan = (TextView) view.findViewById(R.id.kolicinaDanFinal);
        cijenaDan = (TextView) view.findViewById(R.id.cijenaDan);
        ukupnoDan = (TextView) view.findViewById(R.id.iznosDanFinal);
        premijera = (TextView) view.findViewById(R.id.stavkaPremijeraFinal);
        kolicinaPremijera = (TextView) view.findViewById(R.id.kolicinaPremijeraFinal);
        cijenaPremijera = (TextView) view.findViewById(R.id.cijenaPremijeraFinal);
        ukupnoPremijera = (TextView) view.findViewById(R.id.iznosPremijeraFinal);
    }

    private String getCurrentTime(){
        String date = null;
        try { date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Calendar.getInstance().getTime());
            reservationModel.setDatum_dodavanja(date);
            return date;
        }catch(Exception e){ e.printStackTrace(); }
        return date;
    }

    private void getMainData(int id){
        Retrofit retrofit = RetrofitClient.getInstance2();
        iReservation = retrofit.create(IReservation.class);
        final String premijeraString = "Da";
        Call<List<OfferModel>> call = iReservation.getAllData(id);
        call.enqueue(new Callback<List<OfferModel>>() {
            @Override
            public void onResponse(Call<List<OfferModel>> call, Response<List<OfferModel>> response) {
                if(response.isSuccessful()){
                    List<OfferModel> list = response.body();
                    for(int i=0; i<list.size(); i++){
                        offerModel.setNaziv(list.get(i).getNaziv());
                        offerModel.setDatum_prikazivanja(list.get(i).getDatum_prikazivanja());
                        offerModel.setVrijeme_prikazivanja(list.get(i).getVrijeme_prikazivanja());
                        offerModel.setTrenutno_ulaznica(list.get(i).getTrenutno_ulaznica());
                        offerModel.setPremijera(list.get(i).getPremijera());
                    }
                    if(premijeraString.equals(offerModel.getPremijera())){
                        offerModel.setZauzetost(true);
                    }else offerModel.setZauzetost(false);
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

    // Dohvat podataka o korisniku
    private void getName(){
        Retrofit retrofit =RetrofitClient.getInstance2();
        iKorisnik = retrofit.create(IKorisnik.class);

        Call<List<UserModel>> call = iKorisnik.getProfileData(Integer.parseInt(userModel.getKljuc()));
        call.enqueue(new Callback<List<UserModel>>() {
            @Override
            public void onResponse(Call<List<UserModel>> call, Response<List<UserModel>> response) {
                if(response.isSuccessful()){
                    List<UserModel> list = response.body();
                    for(int i=0; i<list.size(); i++){
                        userModel.setIme(list.get(i).getIme());
                        userModel.setPrezime(list.get(i).getPrezime());
                    }
                    String user = userModel.getIme()+" "+userModel.getPrezime();
                    korisnik.setText(user);
                }
            }

            @Override
            public void onFailure(Call<List<UserModel>> call, Throwable t) {
                t.getMessage();
                t.printStackTrace();
            }
        });
    }

    //Dohvat cijena: premijere i naknada za dan rezerviranja - izracun cijene
    private void getPrices(){
        Retrofit retrofit = RetrofitClient.getInstance2();
        iPrices = retrofit.create(IPrices.class);

        Call<List<PricesModel>> call = iPrices.getPricesData();
        call.enqueue(new Callback<List<PricesModel>>() {
            @Override
            public void onResponse(Call<List<PricesModel>> call, Response<List<PricesModel>> response) {
                if(response.isSuccessful()){
                    List<PricesModel> list = response.body();
                    for (int i = 0; i<list.size(); i++){
                        pricesModel.setId(list.get(i).getId());
                        pricesModel.setPremijera(list.get(i).getPremijera());
                        pricesModel.setDan(list.get(i).getDan());
                    }
                    cijenaDan.setText(String.format("%.2f",pricesModel.getDan()));
                    cijenaPremijera.setText(String.format("%.2f",pricesModel.getPremijera()));
                    calculatePrice();
                }
            }
            @Override
            public void onFailure(Call<List<PricesModel>> call, Throwable t) {
                t.getMessage();
                t.printStackTrace();
            }
        });
    }

    private void setData(){
        relativeRezerviraj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmDialog();
            }
        });
        naziv.setText(offerModel.getNaziv());
        datum.setText(offerModel.getDatum_prikazivanja());
        vrijeme.setText(offerModel.getVrijeme_prikazivanja()+" h");
        cijenaIzracun.setText(String.format("%.2f",reservationModel.getCijena()));
        getName();
        getPrices();
    }

    private void numberList(ArrayList<Integer> listRed,ArrayList<Integer> listSjedalo, ArrayList<Integer> listOznaka, View view){

        reservationModel.setKolicina(listRed.size());
        ulaznice.setText(String.valueOf(reservationModel.getKolicina()));
        kolicina.setText(String.valueOf(reservationModel.getKolicina()));

        TextView[] textViews = new TextView[listRed.size()];
        CardView[] cardViews = new CardView[listRed.size()];
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT
        );
        params.setMargins(0, 0, 0, 20);

        for(int i=0; i<listRed.size(); i++){
            cardViews[i] = new CardView(getContext());
            cardViews[i].setId(i+1);
            cardViews[i].setCardBackgroundColor(Color.WHITE);
            cardViews[i].setMaxCardElevation(30);
            cardViews[i].setCardElevation(20);
            cardViews[i].setRadius(15);
            cardViews[i].setPadding(0,25,0,25);
            cardViews[i].setLayoutParams(params);

            textViews[i] = new TextView(getContext());
            textViews[i].setId(i+1);
            textViews[i].setText("Red: "+listRed.get(i)+" |  "+"Sjedalo: "+listSjedalo.get(i));
            textViews[i].setTextColor(Color.GRAY);
            textViews[i].setGravity(Gravity.CENTER);
            textViews[i].setTextSize(16);
            textViews[i].setPadding(5,0,5,20);
            textViews[i].setCompoundDrawablesWithIntrinsicBounds(R.
                    drawable.ic_airline_seat_recline_extra_black_24dp,0,0,0);
            cardViews[i].addView(textViews[i]);
            ticketLayout.addView(cardViews[i],i);
        }
    }

    private void confirmDialog(){
        String karta = "";
        String natpis = "";

        if(reservationModel.getKolicina()>1){
            karta = "Karte su spremljene!";
            natpis = "Želite li rezervirati karte?";
        }else {
            karta = "Karta je spremljena!";
            natpis = "Želite li rezervirati kartu?";
        }

        final String finalKarta = karta;
        new SweetAlertDialog(getContext(), SweetAlertDialog.WARNING_TYPE)
                .setTitleText("Jeste li sigurni?")
                .setContentText(natpis)
                .setCancelText("Odustani!")
                .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        sweetAlertDialog.cancel();
                    }
                }).setConfirmText("Spremi!").setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sDialog) {
                sDialog.setTitleText("Spremljeno!")
                        .setContentText(finalKarta)
                        .setConfirmText("U redu")
                        .setConfirmClickListener(null)
                        .showCancelButton(false)
                        .changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
                sDialog.dismissWithAnimation();

                getCurrentTime();
                reservationModel.setId_korisnik(Integer.parseInt(userModel.getKljuc()));
                saveReservation(reservationModel.getId_korisnik(),pricesModel.getId(),reservationModel.getId_raspored(),
                        reservationModel.getKolicina(),reservationModel.getRed(),reservationModel.getSjedalo(),
                        reservationModel.getOznaka(),reservationModel.getCijena(),reservationModel.getUkupno(),
                        offerModel.getTrenutno_ulaznica(), reservationModel.getDatum_dodavanja());
            }
        }).show();
    }

    private void calculatePrice(){
        float kolicinaProracun = Float.parseFloat(kolicina.getText().toString());
        float cijenaProracun = (reservationModel.getCijena());
        float konacna = kolicinaProracun*cijenaProracun;

        //Provjera iznosa za premijeru predstava i izracun cijene
        float iznosPremijera = 0;
        if(offerModel.getZauzetost()) {
            kolicinaPremijera.setText(String.valueOf(reservationModel.getKolicina()));
            iznosPremijera = pricesModel.getPremijera() * reservationModel.getKolicina();
        }else kolicinaPremijera.setText("0");
        float ukupno = konacna + iznosPremijera;

        //Provjera iznosa za dan kupnje i izracun cijene
        String date = getCurrentTime();
        float izracunDan = 0;
        boolean flag = compareDates(offerModel.getDatum_prikazivanja(),date);
        if(flag){
            kolicinaDan.setText(String.valueOf(reservationModel.getKolicina()));
            izracunDan = reservationModel.calculatePrice
                    (pricesModel.getDan(), reservationModel.getKolicina());
        }else kolicinaDan.setText("0");
        float ukupnaCijena = ukupno + izracunDan;

        reservationModel.setCijena(cijenaProracun);
        reservationModel.setUkupno(konacna);

        ukupnoPremijera.setText(String.format("%.2f",iznosPremijera));
        ukupnoDan.setText(String.format("%.2f",izracunDan));
        iznos.setText(String.format("%.2f",konacna));
        konacnaCijena.setText(String.format("%.2f",ukupnaCijena)+" kn");
        reservationModel.setUkupno(ukupnaCijena);
    }

    private boolean compareDates(String dateDatabase,String current){
        try { DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = dateFormat.parse(current);
            SimpleDateFormat simpleDateFormat
                    = new SimpleDateFormat("dd.MM.yyyy");
            String newDate = simpleDateFormat.format(date);
            if (dateDatabase.equals(newDate)) return true;
            else return false;
        }catch(Exception e){ e.printStackTrace(); }
        return false;
    }

    private void saveReservation(int id_korisnik, final int id_cijena, final int id_raspored,final int kolicina,
                                 final int red, final int sjedalo, final int oznaka, float cijena, float ukupno,
                                 int trenutno_ulaznica, String datum_dodavanja){

        Retrofit retrofit = RetrofitClient.getInstance2();
        iReservation = retrofit.create(IReservation.class);
        String potvrda = "ne";
        int ulaznice = trenutno_ulaznica - kolicina;
        Call<ResponseBody> call = iReservation.setReservation(id_korisnik,id_cijena, id_raspored,kolicina,
                cijena, ukupno, potvrda,ulaznice, datum_dodavanja);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.isSuccessful()){
                    String  number = null;
                    try { number = new String(response.body().bytes());
                    } catch (IOException e) { e.printStackTrace(); }
                    reservationModel.setId(Integer.parseInt(number));
                    saveSeat(reservationModel.getId(),kolicina);
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                t.getMessage();
                t.printStackTrace();
            }
        });
    }

    private void saveSeat(int id_raspored,int kolicina){
        Retrofit retrofit = RetrofitClient.getInstance2();
        iReservation = retrofit.create(IReservation.class);

        for(int i=0; i<kolicina; i++) {
            reservationModel.setRed(reservationModel.getRedLista().get(i));
            reservationModel.setSjedalo(reservationModel.getSjedaloLista().get(i));
            reservationModel.setOznaka(reservationModel.getOznakaLista().get(i));

            Call<ResponseBody> call = iReservation.setSeat(id_raspored, reservationModel.getRed(),
                    reservationModel.getSjedalo(), reservationModel.getOznaka());
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    Intent intent = new Intent(getContext(), ReservationOverview.class);
                    startActivity(intent);
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    t.printStackTrace();
                    t.getMessage();
                }
            });
        }
    }
}
