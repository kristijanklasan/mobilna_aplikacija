package android.unipu.theater.user;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.widget.CompoundButtonCompat;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.ColorStateList;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.unipu.theater.R;
import android.unipu.theater.model.OfferModel;
import android.unipu.theater.model.PricesModel;
import android.unipu.theater.model.ReservationModel;
import android.unipu.theater.retrofit.IReservation;
import android.unipu.theater.retrofit.RetrofitClient;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.material.appbar.CollapsingToolbarLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class BookingTickets extends AppCompatActivity {

    private LinearLayout linearLayout1, linearLayout2,linearLayout3,
            linearLayout4,linearLayout5,linearLayout6,linearLayout7,linearLayout8,
            linearLayout9,linearLayout10;
    private TextView datumPredstave,vrijemePredstave,ulaznicePredstava;
    private Button btnPrihvatiSjedala;
    private int num;
    private CheckBox[] checkBoxes;
    private ImageView imageRotate;
    private ImageView[] imageViews;
    private Spinner karteSpinner;
    private Toolbar bookingToolbar;
    private IReservation iReservation;
    private ReservationModel reservationModel;
    private OfferModel offerModel;
    private PricesModel pricesModel;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private int count;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_tickets);
        initView();
        getData();
        checkSeat();
    }

    private void initView(){
        bookingToolbar = (Toolbar) findViewById(R.id.toolbarBooking);
        offerModel = new OfferModel();
        pricesModel = new PricesModel();
        reservationModel = new ReservationModel();

        setSupportActionBar(bookingToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        ActionBar actionBar=getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsingLayout);
        imageRotate = (ImageView) findViewById(R.id.imageRotate);
        karteSpinner = (Spinner) findViewById(R.id.karteSpinner);
        datumPredstave = (TextView) findViewById(R.id.datumBooking);
        vrijemePredstave = (TextView) findViewById(R.id.vrijemeBooking);
        ulaznicePredstava = (TextView) findViewById(R.id.ulazniceBooking);
        linearLayout1 = (LinearLayout) findViewById(R.id.layoutRed1);
        linearLayout2 = (LinearLayout) findViewById(R.id.layoutRed2);
        linearLayout3 = (LinearLayout) findViewById(R.id.layoutRed3);
        linearLayout4 = (LinearLayout) findViewById(R.id.layoutRed4);
        linearLayout5 = (LinearLayout) findViewById(R.id.layoutRed5);
        linearLayout6 = (LinearLayout) findViewById(R.id.layoutRed6);
        linearLayout7 = (LinearLayout) findViewById(R.id.layoutRed7);
        linearLayout8 = (LinearLayout) findViewById(R.id.layoutRed8);
        linearLayout9 = (LinearLayout) findViewById(R.id.layoutRed9);
        linearLayout10 = (LinearLayout) findViewById(R.id.layoutRed10);
        btnPrihvatiSjedala = (Button)  findViewById(R.id.potvrdiBooking);
        setElements();
        countCheckBox();
        setSpinner();
    }

    private void getData(){
        try{ Intent intent = getIntent();
            ArrayList<String> list = new ArrayList<>();
            list = intent.getStringArrayListExtra("podaci");
            int id = Integer.parseInt(list.get(0));
            float price = Float.parseFloat(list.get(1));
            offerModel.setId(id);
            pricesModel.setCijene_odrasli(price);
            reservationModel.setId_raspored(id);
            getDataDatabase(id);
        }catch(Exception e){ e.printStackTrace(); }
    }

    private void getDataDatabase(int id){
        Retrofit retrofit = RetrofitClient.getInstance2();
        iReservation = retrofit.create(IReservation.class);

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
                    }
                    collapsingToolbarLayout.setTitle(offerModel.getNaziv());
                    datumPredstave.setText(offerModel.getDatum_prikazivanja());
                    vrijemePredstave.setText(offerModel.getVrijeme_prikazivanja()+" h");
                    ulaznicePredstava.setText(String.valueOf(offerModel.getTrenutno_ulaznica()));
                }
            }

            @Override
            public void onFailure(Call<List<OfferModel>> call, Throwable t) {
                t.getMessage();
                t.printStackTrace();
            }
        });

    }

    private void setElements(){
        createCheckBox();
        collapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.app_bar_expanded);
        collapsingToolbarLayout.setCollapsedTitleTextAppearance(R.style.app_bar_collapsed);
        bookingToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BookingTickets.this,UserReservation.class);
                startActivity(intent);
                finish();
            }
        });

        imageRotate.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SourceLockedOrientationActivity")
            @Override
            public void onClick(View v) {
                final int orientation = BookingTickets.this.getResources().getConfiguration().orientation;

                switch(orientation){
                    case Configuration.ORIENTATION_PORTRAIT: {
                        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                    }break;

                    case Configuration.ORIENTATION_LANDSCAPE:{
                        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                    }break;
                }
            }
        });

        btnPrihvatiSjedala.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<String> dataList = new ArrayList<>();
                dataList.add(String.valueOf(offerModel.getId()));
                dataList.add(String.valueOf(pricesModel.getCijene_odrasli()));
                reserveSeat();
                ArrayList<Integer> redList = reservationModel.getRedLista();
                ArrayList<Integer> sjedaloList = reservationModel.getSjedaloLista();
                ArrayList<Integer> oznakaList = reservationModel.getOznakaLista();

                int mCount = countCheckBox();
                if(mCount != 0){
                   Intent intent = new Intent(BookingTickets.this, ReserveTickets.class);
                   intent.putStringArrayListExtra("podaci",dataList);
                   intent.putIntegerArrayListExtra("red",redList);
                   intent.putIntegerArrayListExtra("sjedalo", sjedaloList);
                   intent.putIntegerArrayListExtra("oznaka",oznakaList);
                   startActivity(intent);
                   finish();
                }
                else{
                    SweetAlertDialog dialog = new SweetAlertDialog(BookingTickets.this, SweetAlertDialog.ERROR_TYPE);
                    dialog.setTitleText("Pogreška!");
                    dialog.setContentText("Morate odabrati sjedalo!");
                    dialog.setCancelable(true);
                    dialog.show();
                }

            }
        });

        CheckBox[] checkBoxes = new CheckBox[120];
        for(int i=0; i<120; i++) {
            checkBoxes[i] = (CheckBox) findViewById(1 + i);
            checkBoxes[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    count = countCheckBox();
                    if(count<num) setClickable(false);
                    else setClickable(true);
                }
            });
        }
    }

    private void checkSeat(){
        Retrofit retrofit = RetrofitClient.getInstance2();
        iReservation = retrofit.create(IReservation.class);

        checkBoxes = new CheckBox[121];
        Call<List<ReservationModel>> call = iReservation.checkAvailability(reservationModel.getId_raspored());
        call.enqueue(new Callback<List<ReservationModel>>() {
            @Override
            public void onResponse(Call<List<ReservationModel>> call, Response<List<ReservationModel>> response) {
                if(response.isSuccessful()){
                    List<ReservationModel> list = response.body();

                    for(int i=0; i<list.size(); i++){
                        reservationModel.setOznaka(list.get(i).getOznaka());
                        int oznaka = reservationModel.getOznaka();
                        checkBoxes[oznaka] = (CheckBox) findViewById(oznaka);
                        checkBoxes[oznaka].setTextColor(Color.RED);
                        checkBoxes[oznaka].setEnabled(false);
                        checkBoxes[oznaka].setClickable(false);
                        CompoundButtonCompat.setButtonTintList(checkBoxes[oznaka], ColorStateList.valueOf(Color.RED));
                    }
                }
            }

            @Override
            public void onFailure(Call<List<ReservationModel>> call, Throwable t) {
                t.getMessage();
                t.printStackTrace();
            }
        });
    }

    private void setSpinner(){
        String [] number = {"1","2","3","4"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(),
                android.R.layout.simple_spinner_item, number);
        adapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
        karteSpinner.setAdapter(adapter);
        for(int i=0; i<karteSpinner.getCount(); i++){
            if(karteSpinner.getItemAtPosition(i).toString().equals(number[1])){
                karteSpinner.setSelection(i);
                break;
            }
        }

        karteSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                TextView selectedText = (TextView) parent.getChildAt(0);
                if (selectedText != null) {
                    selectedText.setGravity(Gravity.CENTER);
                    selectedText.setTextColor(Color.WHITE);
                }

                if(parent.getItemAtPosition(position).equals("")){ }
                else{
                    String item = parent.getItemAtPosition(position).toString();
                    num = Integer.parseInt(item);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });
    }

    private void setClickable(boolean flag){
        CheckBox[] checkBoxes = new CheckBox[120];
        if(flag) {
            for (int i = 0; i < 120; i++) {
                checkBoxes[i] = (CheckBox) findViewById(1 + i);
                if(!(checkBoxes[i].isChecked())) {
                    checkBoxes[i].setEnabled(false);
                }
            }
        }

        else{
            for (int i = 0; i < 120; i++) {
                checkBoxes[i] = (CheckBox) findViewById(1 + i);
                checkBoxes[i].setEnabled(true);
            }
        }
    }

    private void createCheckBox(){
        CheckBox[] checkBoxes = new CheckBox[12];
        LinearLayout.LayoutParams imageparam = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT,1f);

        for (int i = 0; i < 12; i++) {
            checkBoxes[i] = new CheckBox(this);
            checkBoxes[i].setText(String.format(Locale.getDefault(), "%d", i + 1));
            checkBoxes[i].setGravity(Gravity.TOP);
            checkBoxes[i].setButtonDrawable(R.drawable.checkbox);
            checkBoxes[i].setTextColor(Color.WHITE);
            checkBoxes[i].setId(1+i);
            checkBoxes[i].setTag(1);
            checkBoxes[i].setPadding(20,0,20,0);
            checkBoxes[i].setLayoutParams(imageparam);
            linearLayout1.addView(checkBoxes[i], i);
        }


        for (int i = 0; i < 12; i++) {
            checkBoxes[i] = new CheckBox(this);
            checkBoxes[i].setText(String.format(Locale.getDefault(), "%d", i+1));
            checkBoxes[i].setButtonDrawable(R.drawable.checkbox);
            checkBoxes[i].setTextColor(Color.WHITE);
            checkBoxes[i].setId(i+12+1);
            checkBoxes[i].setTag(2);
            checkBoxes[i].setPadding(20,0,20,0);
            checkBoxes[i].setLayoutParams(imageparam);
            linearLayout2.addView(checkBoxes[i], i);
        }


        for (int i = 0; i < 12; i++) {
            checkBoxes[i] = new CheckBox(this);
            checkBoxes[i].setText(String.format(Locale.getDefault(), "%d", i+1));
            checkBoxes[i].setButtonDrawable(R.drawable.checkbox);
            checkBoxes[i].setTextColor(Color.WHITE);
            checkBoxes[i].setId(i+24+1);
            checkBoxes[i].setTag(3);
            checkBoxes[i].setTextSize(14);
            checkBoxes[i].setPadding(20,0,20,0);
            checkBoxes[i].setLayoutParams(imageparam);
            linearLayout3.addView(checkBoxes[i], i);
        }

        for (int i = 0; i < 12; i++) {
            checkBoxes[i] = new CheckBox(this);
            checkBoxes[i].setText(String.format(Locale.getDefault(), "%d", i+1));
            checkBoxes[i].setId(i+36+1);
            checkBoxes[i].setTag(4);
            checkBoxes[i].setButtonDrawable(R.drawable.checkbox);
            checkBoxes[i].setTextColor(Color.WHITE);
            checkBoxes[i].setPadding(20,0,20,0);
            checkBoxes[i].setLayoutParams(imageparam);
            linearLayout4.addView(checkBoxes[i], i);
        }

        for (int i = 0; i < 12; i++) {
            checkBoxes[i] = new CheckBox(this);
            checkBoxes[i].setText(String.format(Locale.getDefault(), "%d", i+1));
            checkBoxes[i].setId(i+48+1);
            checkBoxes[i].setTag(5);
            checkBoxes[i].setButtonDrawable(R.drawable.checkbox);
            checkBoxes[i].setTextColor(Color.WHITE);
            checkBoxes[i].setPadding(20,0,20,0);
            checkBoxes[i].setLayoutParams(imageparam);
            linearLayout5.addView(checkBoxes[i], i);
        }

        for (int i = 0; i < 12; i++) {
            checkBoxes[i] = new CheckBox(this);
            checkBoxes[i].setText(String.format(Locale.getDefault(), "%d", i+1));
            checkBoxes[i].setId(i+60+1);
            checkBoxes[i].setTag(6);
            checkBoxes[i].setButtonDrawable(R.drawable.checkbox);
            checkBoxes[i].setTextColor(Color.WHITE);
            checkBoxes[i].setPadding(20,0,20,0);
            checkBoxes[i].setLayoutParams(imageparam);
            linearLayout6.addView(checkBoxes[i], i);
        }

        for (int i = 0; i < 12; i++) {
            checkBoxes[i] = new CheckBox(this);
            checkBoxes[i].setText(String.format(Locale.getDefault(), "%d", i+1));
            checkBoxes[i].setId(i+72+1);
            checkBoxes[i].setTag(7);
            checkBoxes[i].setPadding(20,0,20,0);
            checkBoxes[i].setButtonDrawable(R.drawable.checkbox);
            checkBoxes[i].setTextColor(Color.WHITE);
            checkBoxes[i].setLayoutParams(imageparam);
            linearLayout7.addView(checkBoxes[i], i);
        }

        for (int i = 0; i < 12; i++) {
            checkBoxes[i] = new CheckBox(this);
            checkBoxes[i].setText(String.format(Locale.getDefault(), "%d", i+1));
            checkBoxes[i].setButtonDrawable(R.drawable.checkbox);
            checkBoxes[i].setTextColor(Color.WHITE);
            checkBoxes[i].setId(i+84+1);
            checkBoxes[i].setTag(8);
            checkBoxes[i].setPadding(20,0,20,0);
            checkBoxes[i].setLayoutParams(imageparam);
            linearLayout8.addView(checkBoxes[i], i);
        }

        for (int i = 0; i < 12; i++) {
            checkBoxes[i] = new CheckBox(this);
            checkBoxes[i].setText(String.format(Locale.getDefault(), "%d", i+1));
            checkBoxes[i].setButtonDrawable(R.drawable.checkbox);
            checkBoxes[i].setTextColor(Color.WHITE);
            checkBoxes[i].setId(i+97);
            checkBoxes[i].setTag(9);
            checkBoxes[i].setPadding(20,0,20,0);
            checkBoxes[i].setLayoutParams(imageparam);
            linearLayout9.addView(checkBoxes[i], i);
        }

        for (int i = 0; i < 12; i++) {
            checkBoxes[i] = new CheckBox(this);
            checkBoxes[i].setText(String.format(Locale.getDefault(), "%d", i+1));
            checkBoxes[i].setButtonDrawable(R.drawable.checkbox);
            checkBoxes[i].setTextColor(Color.WHITE);
            checkBoxes[i].setId(i+109);
            checkBoxes[i].setTag(10);
            checkBoxes[i].setPadding(20,0,20,0);
            checkBoxes[i].setLayoutParams(imageparam);
            linearLayout10.addView(checkBoxes[i], i);
        }
    }

    private int countCheckBox(){
        int counter = 0;
        CheckBox[] checkBoxes = new CheckBox[121];
        for (int i = 0; i < 120; i++) {
            checkBoxes[i] = (CheckBox) findViewById(1+i);
            if(checkBoxes[i].isChecked()){
                counter++;
            }
        }
        return counter;
    }

    private void reserveSeat(){
        ArrayList<Integer> listSjedalo = new ArrayList<>();
        ArrayList<Integer> listRed = new ArrayList<>();
        ArrayList<Integer> listOznaka = new ArrayList<>();
        CheckBox[] checkBoxes = new CheckBox[120];

        for(int k=0; k<120; k++){
            checkBoxes[k] = (CheckBox) findViewById(1+k);
            if(checkBoxes[k].isChecked()){
                String redString = checkBoxes[k].getTag().toString();
                int red = Integer.parseInt(redString);
                int sjedalo = Integer.parseInt(checkBoxes[k].getText().toString());
                int oznaka = checkBoxes[k].getId();
                listRed.add(red);
                listSjedalo.add(sjedalo);
                listOznaka.add(oznaka);
                reservationModel.setRedLista(listRed);
                reservationModel.setSjedaloLista(listSjedalo);
                reservationModel.setOznakaLista(listOznaka);
            }
        }
    }
}
