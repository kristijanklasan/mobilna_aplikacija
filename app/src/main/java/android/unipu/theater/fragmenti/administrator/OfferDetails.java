package android.unipu.theater.fragmenti.administrator;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.unipu.theater.R;
import android.unipu.theater.model.OfferModel;
import android.unipu.theater.model.UserModel;
import android.unipu.theater.retrofit.IAdministrator;
import android.unipu.theater.retrofit.IOffer;
import android.unipu.theater.retrofit.RetrofitClient;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class OfferDetails extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private TextView kategorija,datum,vrijeme, ulaznice,dvorana,premijera,textZauzetost;
    private TextView sifra,ime,email,broj;
    private OfferModel offerModel;
    private UserModel userModel;
    private EditText maxUlaznica;
    private IOffer iOffer;
    private String mParam1;
    private String mParam2;
    private Switch switchUredi,switchPremijera;
    private ImageView imageDatum,imageVrijeme;
    private FloatingActionButton fabAdd,fabMinus;
    private RelativeLayout relativeUpdate;
    private LinearLayout relativeAdmin;
    private IAdministrator iAdministrator;
    private RadioButton radioSve, radioAktivan, radioProsli;
    private RadioGroup radioGroup;
    private static long timeLimit = 7200000;

    public OfferDetails() {
        // Required empty public constructor
    }

    public static OfferDetails newInstance(String param1, String param2) {
        OfferDetails fragment = new OfferDetails();
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
        View view = inflater.inflate(R.layout.fragment_offer_details, container, false);
        initView(view);
        getKey();
        return view;
    }

    private void initView(View view){
        offerModel = new OfferModel();
        userModel = new UserModel();
        textZauzetost = (TextView) view.findViewById(R.id.textZauzetost);
        kategorija = (TextView) view.findViewById(R.id.kategorijaReservation);
        datum = (TextView) view.findViewById(R.id.datumReservation);
        vrijeme = (TextView) view.findViewById(R.id.vrijemeReservation);
        maxUlaznica = (EditText) view.findViewById(R.id.editMaxUlaznicaReservation);
        ulaznice = (TextView) view.findViewById(R.id.ulazniceReservation);
        dvorana = (TextView) view.findViewById(R.id.dvoranaReservation);
        premijera = (TextView) view.findViewById(R.id.premijeraReservation);
        switchUredi = (Switch) view.findViewById(R.id.switchReservation);
        switchPremijera = (Switch) view.findViewById(R.id.switchPremijeraReservation);
        imageDatum = (ImageView) view.findViewById(R.id.imageDatumReservation);
        imageVrijeme = (ImageView) view.findViewById(R.id.imageVrijemeReservation);
        relativeUpdate = (RelativeLayout) view.findViewById(R.id.relativeUpdate);
        relativeAdmin = (LinearLayout) view.findViewById(R.id.relativeAdmin);
        fabAdd = (FloatingActionButton) view.findViewById(R.id.fabAdminAdd);
        fabMinus = (FloatingActionButton) view.findViewById(R.id.fabAdminMinus);
        sifra = (TextView) view.findViewById(R.id.idReservation);
        ime = (TextView) view.findViewById(R.id.imeReservation);
        email = (TextView) view.findViewById(R.id.emailReservation);
        broj = (TextView) view.findViewById(R.id.brojReservation);
        setElements();
    }

    private void setElements(){
        Retrofit retrofitClient = RetrofitClient.getInstance2();
        iOffer = retrofitClient.create(IOffer.class);
        maxUlaznica.setFocusable(false);

        switchUredi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(switchUredi.isChecked()){
                    imageDatum.setVisibility(View.VISIBLE);
                    imageVrijeme.setVisibility(View.VISIBLE);
                    datum.setGravity(Gravity.CENTER_HORIZONTAL);
                    switchPremijera.setVisibility(View.VISIBLE);
                    relativeUpdate.setVisibility(View.VISIBLE);
                    premijera.setGravity(Gravity.CENTER_HORIZONTAL);
                    vrijeme.setGravity(Gravity.CENTER_HORIZONTAL);
                    switchUredi.setText(R.string.text_on);
                    maxUlaznica.setFocusableInTouchMode(true);
                    maxUlaznica.setSingleLine(false);
                } else{
                    imageDatum.setVisibility(View.GONE);
                    imageVrijeme.setVisibility(View.GONE);
                    switchPremijera.setVisibility(View.GONE);
                    relativeUpdate.setVisibility(View.GONE);
                    datum.setGravity(View.TEXT_ALIGNMENT_TEXT_START);
                    vrijeme.setGravity(Gravity.START);
                    premijera.setGravity(View.TEXT_ALIGNMENT_TEXT_START);
                    switchUredi.setText(R.string.text_off);
                    maxUlaznica.setFocusable(false);
                }
            }
        });

        switchPremijera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(switchPremijera.isChecked())
                    premijera.setText("Da");
                else
                    premijera.setText("Ne");
            }
        });

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
                        datum.setText(formatDate);
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
                        vrijeme.setText(hourOfDay+ ":"+minute);
                    }
                },hour,minute,true);
                timePickerDialog.setTitle("Odaberite vrijeme");
                timePickerDialog.show();
            }
        });



        maxUlaznica.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    getTicketNumber();
                }
            }
        });

        relativeUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkShow(datum.getText().toString(),vrijeme.getText().toString());
            }
        });

        fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                relativeAdmin.setVisibility(View.VISIBLE);
                fabAdd.setVisibility(View.GONE);
            }
        });

        fabMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                relativeAdmin.setVisibility(View.GONE);
                fabAdd.setVisibility(View.VISIBLE);
            }
        });

    }

    private void getKey(){
        try{ Bundle bundle = this.getArguments();
            int id = bundle.getInt("id",0);
            offerModel.setId(id);
            setData(offerModel.getId());
            userModel.setId(String.valueOf(offerModel.getId()));
            setAdminData();
        }catch(Exception e){ e.printStackTrace(); }
    }

    private void getTicketNumber(){
        offerModel.setRazlika(offerModel.getMax_ulaznica() - offerModel.getTrenutno_ulaznica());
    }

    private void setData(int id){
        Call<List<OfferModel>> call = iOffer.getDataFromID(id);
        call.enqueue(new Callback<List<OfferModel>>() {
            @Override
            public void onResponse(Call<List<OfferModel>> call, Response<List<OfferModel>> response) {
                if(response.isSuccessful()){
                    List<OfferModel> list = response.body();
                    for(int i=0; i<list.size(); i++){
                        offerModel.setKategorija(list.get(i).getKategorija());
                        offerModel.setDatum_prikazivanja(list.get(i).getDatum_prikazivanja());
                        offerModel.setVrijeme_prikazivanja(list.get(i).getVrijeme_prikazivanja());
                        offerModel.setMax_ulaznica(list.get(i).getMax_ulaznica());
                        offerModel.setTrenutno_ulaznica(list.get(i).getTrenutno_ulaznica());
                        offerModel.setDvorana(list.get(i).getDvorana());
                        offerModel.setPremijera(list.get(i).getPremijera());
                    }
                    setText();
                }
            }

            @Override
            public void onFailure(Call<List<OfferModel>> call, Throwable t) {
                t.printStackTrace();
                t.getMessage();
            }
        });
    }

    private void getModelElements(){
        offerModel.setDatum_prikazivanja(datum.getText().toString());
        offerModel.setVrijeme_prikazivanja(vrijeme.getText().toString());
        offerModel.setMax_ulaznica(Integer.parseInt(maxUlaznica.getText().toString()));
        offerModel.setPremijera(premijera.getText().toString());
        offerModel.setTrenutno_ulaznica(Integer.parseInt(ulaznice.getText().toString()));

        if(!offerModel.getZauzetost()) {
            updateOffer();
        }
    }

    private void getTicket(){
        if(offerModel.getMax_ulaznica()>=offerModel.getRazlika()){
            offerModel.setTrenutno_ulaznica(offerModel.getMax_ulaznica() - offerModel.getRazlika());
            ulaznice.setText(String.valueOf(offerModel.getTrenutno_ulaznica()));
        }else {
            offerModel.setMax_ulaznica(offerModel.getRazlika());
            offerModel.setTrenutno_ulaznica(offerModel.getRazlika());
        }
    }

    private void updateOffer(){
        getTicket();
        String datum = changeDateFormat(offerModel.getDatum_prikazivanja())+" "+offerModel.getVrijeme_prikazivanja()+":00";
        Call<ResponseBody> call = iOffer.updateOffer(offerModel.getId(),datum,offerModel.getDatum_prikazivanja(),
                offerModel.getVrijeme_prikazivanja(),offerModel.getMax_ulaznica(),
                offerModel.getTrenutno_ulaznica(), offerModel.getPremijera());
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.isSuccessful()){
                    new SweetAlertDialog(getContext(),SweetAlertDialog.SUCCESS_TYPE)
                            .setTitleText("Uspješno!")
                            .setContentText("Ponuda je uspješno ažurirana!")
                            .setConfirmText("U redu")
                            .setConfirmClickListener(null)
                            .showCancelButton(false).show();
                }else{
                    new SweetAlertDialog(getContext(),SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("Nije uspješno!")
                            .setContentText("Ponuda je nije ažurirana!")
                            .setConfirmText("U redu")
                            .setConfirmClickListener(null)
                            .showCancelButton(false).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                t.printStackTrace();
                t.getMessage();
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

    private void checkShow(final String datum, final String vrijeme){
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
                        if(result && (time < timeLimit) && (time >= 0)) flag = true;
                    }
                    if(!flag){
                        textZauzetost.setVisibility(View.GONE);
                        offerModel.setZauzetost(false);
                    }else{
                        textZauzetost.setVisibility(View.VISIBLE);
                        offerModel.setZauzetost(true);
                    }
                    getModelElements();
                }
            }
            @Override
            public void onFailure(Call<List<OfferModel>> call, Throwable t) {
                t.printStackTrace();
                t.getMessage();
            }
        });
    }

    private void setAdminData(){
        Retrofit retrofitClient = RetrofitClient.getInstance2();
        iAdministrator = retrofitClient.create(IAdministrator.class);

        Call<List<UserModel>> call = iAdministrator.getAdminData(Integer.parseInt(userModel.getKljuc()));
        call.enqueue(new Callback<List<UserModel>>() {
            @Override
            public void onResponse(Call<List<UserModel>> call, Response<List<UserModel>> response) {
                if(response.isSuccessful()){
                    List<UserModel> list = response.body();
                    for(int i=0; i<list.size(); i++){
                        userModel.setIme(list.get(i).getIme());
                        userModel.setPrezime(list.get(i).getPrezime());
                        userModel.setEmail(list.get(i).getEmail());
                        userModel.setKljuc(list.get(i).getKljuc());
                        userModel.setBroj(list.get(i).getBroj());
                    }
                    setAdminText();
                }
            }

            @Override
            public void onFailure(Call<List<UserModel>> call, Throwable t) {
                t.printStackTrace();
                t.getMessage();
            }
        });
    }

    private void setText() {
        kategorija.setText(offerModel.getKategorija());
        datum.setText(offerModel.getDatum_prikazivanja());
        vrijeme.setText(offerModel.getVrijeme_prikazivanja());
        maxUlaznica.setText(String.valueOf(offerModel.getMax_ulaznica()));
        ulaznice.setText(String.valueOf(offerModel.getTrenutno_ulaznica()));
        dvorana.setText(offerModel.getDvorana());
        premijera.setText(offerModel.getPremijera());
    }

    private void setAdminText(){
        ime.setText(userModel.getIme()+" "+userModel.getPrezime());
        sifra.setText(userModel.getKljuc());
        email.setText(userModel.getEmail());
        broj.setText("0"+String.valueOf(userModel.getBroj()));
    }

    private String changeDateFormat(String datumStaro){
        String novi_datum = "";
        try { SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd.MM.yyyy");
            Date dateStaro = simpleDateFormat.parse(datumStaro);
            simpleDateFormat.applyPattern("YYYY-MM-dd");
            novi_datum = simpleDateFormat.format(dateStaro);
            return  novi_datum;
        }catch (Exception e){ e.printStackTrace(); }
        return novi_datum;
    }
}
