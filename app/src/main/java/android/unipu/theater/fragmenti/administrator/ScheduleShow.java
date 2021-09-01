package android.unipu.theater.fragmenti.administrator;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.unipu.theater.R;
import android.unipu.theater.administrator.AdminWindow;
import android.unipu.theater.model.OfferModel;
import android.unipu.theater.model.TheaterModel;
import android.unipu.theater.model.UserModel;
import android.unipu.theater.retrofit.IAdministrator;
import android.unipu.theater.retrofit.IOffer;
import android.unipu.theater.retrofit.ITheater;
import android.unipu.theater.retrofit.RetrofitClient;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;

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

public class ScheduleShow extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private TextView textTrenutnoUlaznica,textVrijeme,textDatum,textSifra,textKorisnik,textSifraKorisnik,
                        textDatumPonuda,textDvorana,textGreska;
    private EditText editUlaznice;
    private ImageView imageDatum, imageVrijeme;
    private CheckBox checkBoxPremijera;
    private Spinner spinnerPredstava;
    private OfferModel offerModel;
    private TheaterModel theaterModel;
    private ITheater iTheater;
    private Switch switchPonuda;
    private IAdministrator iAdministrator;
    private CardView cardDetaljiPonuda;
    private Button btnSpremi;
    private UserModel userModel;
    private IOffer iOffer;
    private Toolbar toolbar;
    private static long timeLimit = 7200000;

    private String mParam1;
    private String mParam2;

    public ScheduleShow() {
        // Required empty public constructor
    }

    public static ScheduleShow newInstance(String param1, String param2) {
        ScheduleShow fragment = new ScheduleShow();
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

        View view = inflater.inflate(R.layout.fragment_schedule_show, container, false);
        theaterModel = new TheaterModel();
        initView(view);
        setListener(view);
        return view;
    }

    private void initView(View view){
        userModel = new UserModel();
        offerModel = new OfferModel();
        textDatum = (TextView) view.findViewById(R.id.textDatum);
        textVrijeme = (TextView) view.findViewById(R.id.textVrijeme);
        textTrenutnoUlaznica = (TextView) view.findViewById(R.id.textTrenutnoUlaznica);
        editUlaznice = (EditText) view.findViewById(R.id.editMaxUlaznica);
        imageDatum = (ImageView) view.findViewById(R.id.imageDatum);
        imageVrijeme = (ImageView) view.findViewById(R.id.imageVrijeme);
        checkBoxPremijera = (CheckBox) view.findViewById(R.id.checkBoxPremijera);
        spinnerPredstava = (Spinner)view.findViewById(R.id.spinnerPredstava);
        switchPonuda = (Switch) view.findViewById(R.id.switchPonuda);
        textSifra = (TextView) view.findViewById(R.id.textSifra);
        textSifraKorisnik = (TextView) view.findViewById(R.id.textSifraKorisnik);
        textDatumPonuda = (TextView) view.findViewById(R.id.textDatumPonuda);
        textKorisnik = (TextView) view.findViewById(R.id.textKorisnik);
        cardDetaljiPonuda = (CardView) view.findViewById(R.id.cardDetaljiPonuda);
        btnSpremi = (Button) view.findViewById(R.id.buttonSpremiPonudu);
        textDvorana = (TextView) view.findViewById(R.id.textDvorana);
        textGreska = (TextView) view.findViewById(R.id.textGreska);
        toolbar = view.findViewById(R.id.toolbarShowSchedule);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        getActivity().setTitle("Unos ponude");
    }

    private void setListener(final View view){

        imageDatum.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        Calendar calendar = Calendar.getInstance();
                        calendar.set(year,month,dayOfMonth);

                        SimpleDateFormat simple = new SimpleDateFormat("dd.MM.yyyy");
                        String formatDate = simple.format(calendar.getTime());
                        textDatum.setText(formatDate);
                    }
                },year,month,day);
                dialog.show();
            }
        });

        imageVrijeme.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Calendar currentTime = Calendar.getInstance();
                int hour = currentTime.get(Calendar.HOUR_OF_DAY);
                int minute = currentTime.get(Calendar.MINUTE);
                TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        textVrijeme.setText(hourOfDay+ ":"+minute);
                    }
                },hour,minute,true);
                timePickerDialog.setTitle("Odaberite vrijeme");
                timePickerDialog.show();
            }
        });

        checkBoxPremijera.setText("Ne");
        checkBoxPremijera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkBoxPremijera.isChecked()){
                    checkBoxPremijera.setText("Da");
                }else checkBoxPremijera.setText("Ne");
            }
        });

        editUlaznice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textTrenutnoUlaznica.setText(editUlaznice.getText().toString());
            }
        });

        switchPonuda.setText("Isključeno");
        switchPonuda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(switchPonuda.isChecked()){
                    switchPonuda.setText("Uključeno");
                    cardDetaljiPonuda.setVisibility(View.VISIBLE);
                }else {
                    switchPonuda.setText("Isključeno");
                    cardDetaljiPonuda.setVisibility(View.GONE);
                }
            }
        });

        btnSpremi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkShow(textDatum.getText().toString(),textVrijeme.getText().toString());
            }
        });

        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_keyboard_arrow_left_black_24dp));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getContext(), AdminWindow.class);
                startActivity(i);
            }
        });

        SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy");
        Date date = new Date();
        textDatumPonuda.setText(formatter.format(date));

        try { getShow();
            getUserData();
        }catch(Exception e){
            e.printStackTrace(); }
    }

    private void checkField(String ulaznice){
        offerModel.setId_korisnik(Integer.parseInt(textSifraKorisnik.getText().toString()));
        offerModel.setId_predstava(Integer.parseInt(textSifra.getText().toString()));
        offerModel.setDatum_prikazivanja(textDatum.getText().toString());
        offerModel.setVrijeme_prikazivanja(textVrijeme.getText().toString());
        offerModel.setDvorana(textDvorana.getText().toString());
        offerModel.setPremijera(checkBoxPremijera.getText().toString());

        if(TextUtils.isEmpty(ulaznice)){
            editUlaznice.setError("Unesite ulaznice");
        }
        else {
            editUlaznice.setError(null);
            offerModel.setMax_ulaznica(Integer.parseInt(editUlaznice.getText().toString()));
            offerModel.setTrenutno_ulaznica(Integer.parseInt(textTrenutnoUlaznica.getText().toString()));
        }

        SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy");
        Date date = new Date();
        String datum_unosa = formatter.format(date);
        offerModel.setDatum_unosa(datum_unosa);

        if(!offerModel.getZauzetost()) {
            if ((offerModel.getId_korisnik() != 0) && (offerModel.getId_predstava() != 0) &&
                    (!TextUtils.isEmpty(ulaznice)) && (offerModel.getTrenutno_ulaznica() != 0) &&
                    (!offerModel.getDatum_prikazivanja().isEmpty()) && (!offerModel.getVrijeme_prikazivanja().isEmpty())
                    && (!offerModel.getDvorana().isEmpty())) {

                new SweetAlertDialog(getContext(), SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("Jeste li sigurni?")
                        .setContentText("Želite li spremiti ponudu?")
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
                                .setContentText("Ponuda je spremljena!")
                                .setConfirmText("U redu")
                                .setConfirmClickListener(null)
                                .showCancelButton(false)
                                .changeAlertType(SweetAlertDialog.SUCCESS_TYPE);

                        setOffer(offerModel.getId_korisnik(), offerModel.getId_predstava(), offerModel.getDatum_unosa(),
                                offerModel.getDatum_prikazivanja(), offerModel.getVrijeme_prikazivanja(),
                                offerModel.getMax_ulaznica(), offerModel.getTrenutno_ulaznica(), offerModel.getDvorana(), offerModel.getPremijera());
                    }
                }).show();
            }
        }
    }

    private void setSpinner(){

        String [] arrayPredstava = new String[theaterModel.getNazivPredstave().size()];
        arrayPredstava = theaterModel.getNazivPredstave().toArray(arrayPredstava);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(),
                android.R.layout.simple_spinner_item, arrayPredstava);

        adapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
        spinnerPredstava.setAdapter(adapter);
        spinnerPredstava.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ((TextView) parent.getChildAt(0)).setTextColor(Color.WHITE);
                if(parent.getItemAtPosition(position).equals("")){ }
                else{
                    String item = parent.getItemAtPosition(position).toString();
                    getShowID(item);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });
    }

    private void getShow(){
        Retrofit retrofitClient = RetrofitClient.getInstance2();
        iTheater = retrofitClient.create(ITheater.class);

        final ArrayList<String> tempList = new ArrayList<>();
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
                t.getMessage();
                t.printStackTrace();
            }
        });
    }

    private void getShowID(String naziv){
        Retrofit retrofitClient = RetrofitClient.getInstance2();
        iTheater = retrofitClient.create(ITheater.class);

        Call<List<TheaterModel>> call = iTheater.dohvatIdPredstave(naziv);
        call.enqueue(new Callback<List<TheaterModel>>() {
            @Override
            public void onResponse(Call<List<TheaterModel>> call, Response<List<TheaterModel>> response) {
                if(response.isSuccessful()){
                    List<TheaterModel> list = response.body();
                    for(int i=0; i<list.size(); i++){
                        theaterModel.setId(list.get(i).getId());
                        textSifra.setText(String.valueOf(list.get(i).getId()));
                    }
                }
            }

            @Override
            public void onFailure(Call<List<TheaterModel>> call, Throwable t) {
                t.getMessage();
                t.printStackTrace();
            }
        });
    }

    private void getUserData(){
        Retrofit retrofitClient = RetrofitClient.getInstance2();
        iAdministrator = retrofitClient.create(IAdministrator.class);

        final int id_admin = Integer.parseInt(userModel.getKljuc());
        Call<List<UserModel>> call = iAdministrator.getAdminData(id_admin);
        call.enqueue(new Callback<List<UserModel>>() {
            @Override
            public void onResponse(Call<List<UserModel>> call, Response<List<UserModel>> response) {
                if (response.isSuccessful()) {
                    List<UserModel> list = response.body();
                    for (int i = 0; i < list.size(); i++) {
                        userModel.setIme(list.get(i).getIme());
                        userModel.setPrezime(list.get(i).getPrezime());
                        textKorisnik.setText(userModel.getIme() + " " + userModel.getPrezime());
                        textSifraKorisnik.setText(String.valueOf(id_admin));
                    }
                }
            }

            @Override
            public void onFailure(Call<List<UserModel>> call, Throwable t) {
                t.getMessage();
                t.printStackTrace();
            }
        });
    }

    private long convertTime(String time1,String time2) {
        try { SimpleDateFormat format = new SimpleDateFormat("HH:mm");
            Date date1 = format.parse(time1);
            Date date2 = format.parse(time2);
            long difference = date2.getTime() - date1.getTime();
            return difference;
        }catch(Exception e){ e.printStackTrace(); }
        return 0;
    }

    private boolean checkDate(String date1,String date2){
        try{ Date dateCurrent = new SimpleDateFormat("dd.MM.yyyy").parse(date1);
            Date dateDatabase = new SimpleDateFormat("dd.MM.yyyy").parse(date2);
            if(dateCurrent.equals(dateDatabase)){
                return true;
            }
            else return false;
        }catch(Exception e){e.printStackTrace();}
       return false;
    }

    // Provjerava dostupnost dvorane.
    private void checkShow(final String datum, final String vrijeme){
        Retrofit retrofitClient = RetrofitClient.getInstance2();
        iOffer = retrofitClient.create(IOffer.class);

        Call<List<OfferModel>> call = iOffer.getPrikazivanje(datum);
        call.enqueue(new Callback<List<OfferModel>>() {
            @Override
            public void onResponse(Call<List<OfferModel>> call, Response<List<OfferModel>> response) {
                if(response.isSuccessful()){
                    List<OfferModel> list = response.body();
                    boolean flag = false;
                    for(int i=0; i<list.size(); i++){
                        offerModel.setDatum_prikazivanja(list.get(i).getDatum_prikazivanja());
                        long time = convertTime(vrijeme,list.get(i).getVrijeme_prikazivanja());
                        boolean result = checkDate(datum,offerModel.getDatum_prikazivanja());
                        if(result && (time < timeLimit) && (time >= 0)) {
                            flag = true;
                        }
                    }
                    if(!flag){
                        textGreska.setVisibility(View.GONE);
                        offerModel.setZauzetost(false);
                    }else{
                        textGreska.setVisibility(View.VISIBLE);
                        offerModel.setZauzetost(true);
                    }
                    checkField(editUlaznice.getText().toString());
                }
            }
            @Override
            public void onFailure(Call<List<OfferModel>> call, Throwable t) {
                t.printStackTrace();
                t.getMessage();
            }
        });
    }

    private void setOffer(int id_korisnik, int id_predstava, String datum_unosa, String datum_prikazivanja,
                          String vrijeme__prikazivanja,int max_ulaznica, int trenutno_ulaznica, String dvorana, String premijera){

        String datum = changeDateFormat(datum_prikazivanja)+" "+vrijeme__prikazivanja+":00";
        Retrofit retrofitClient = RetrofitClient.getInstance2();
        iOffer = retrofitClient.create(IOffer.class);

        Call<ResponseBody> call = iOffer.sendOffer(id_korisnik,id_predstava,datum_unosa,datum,datum_prikazivanja,
                vrijeme__prikazivanja,max_ulaznica,trenutno_ulaznica,dvorana,premijera);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {}

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                t.getMessage();
                t.printStackTrace();
            }
        });
    }

    private String changeDateFormat(String datumStaro){
        String novi_datum = "";
        try { SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd.MM.yyyy");
            Date dateStaro = simpleDateFormat.parse(datumStaro);
            simpleDateFormat.applyPattern("YYYY-MM-dd");
            novi_datum = simpleDateFormat.format(dateStaro);
            return  novi_datum;

        }catch (Exception e){
            e.printStackTrace();
        }
        return novi_datum;
    }

    private Date getTimeStamp(String date){
        Date dateNew = null;
        try { SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm");
            dateNew = simpleDateFormat.parse(date);
            return dateNew;
        }catch(Exception e){
            e.printStackTrace();
        }
        return dateNew;
    }
}
