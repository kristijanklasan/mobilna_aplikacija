package android.unipu.theater.administrator;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.unipu.theater.R;
import android.unipu.theater.adapter.SliderAdapter;
import android.unipu.theater.model.ImageModel;
import android.unipu.theater.model.TheaterModel;
import android.unipu.theater.retrofit.ITheater;
import android.unipu.theater.retrofit.RetrofitClient;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.chip.Chip;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.smarteist.autoimageslider.SliderView;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class PerformanceDetail extends AppCompatActivity {

    private String formatDatum;
    private ITheater iTheater;
    private TheaterModel theaterModel;
    private Button btnUpdate;
    private EditText naslovEdit, kategorijaEdit, datumEdit, opisEdit, redateljEdit,
            dramaturgijaEdit, glumciEdit, kostimografijaEdit, scenografijaEdit,
            glazbaEdit, koreografijaEdit, podrskaEdit;

    private Spinner spinerKategorija;
    private DatePickerDialog.OnDateSetListener mDatePicker;
    private TextView naslovText,datumText;
    private SliderView sliderView;
    private Switch switchUpdate;
    private ImageButton imageDatumUpdate;
    private ImageView image1, image2;
    private Chip chipKategorija;
    private LinearLayout linearKategorija,layoutChip;
    private FloatingActionButton fabDelete;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_performance_detail);

        initView();
        editableText(false);
    }

    private void initView(){
        theaterModel = new TheaterModel();
        toolbar = (Toolbar) findViewById(R.id.toolbarTheaterDetail);
        switchUpdate = (Switch) findViewById(R.id.switchUpdate);
        image1 = (ImageView) findViewById(R.id.imageTheater1);
        image2 = (ImageView) findViewById(R.id.imageTheater2);
        btnUpdate = (Button) findViewById(R.id.btnUpdatePredstava);
        imageDatumUpdate = (ImageButton) findViewById(R.id.imageDatumUpdate);
        naslovEdit = (EditText) findViewById(R.id.naslovEdit);
        spinerKategorija= (Spinner) findViewById(R.id.kategorijaSpinnerUpdate);
        datumText = (TextView) findViewById(R.id.datumPremijereText);
        opisEdit = (EditText) findViewById(R.id.opisEdit);
        redateljEdit = (EditText) findViewById(R.id.redateljEdit);
        dramaturgijaEdit = (EditText) findViewById(R.id.dramaturgijaEdit);
        glumciEdit = (EditText) findViewById(R.id.glumciEdit);
        kostimografijaEdit = (EditText) findViewById(R.id.kostimografijaEdit);
        scenografijaEdit = (EditText) findViewById(R.id.scenografijaEdit);
        glazbaEdit = (EditText) findViewById(R.id.glazbaEdit);
        koreografijaEdit = (EditText) findViewById(R.id.koreografijaEdit);
        podrskaEdit = (EditText) findViewById(R.id.podrskaEdit);
        linearKategorija = (LinearLayout) findViewById(R.id.kategorijaLayout);
        layoutChip = (LinearLayout) findViewById(R.id.layoutChip);
        sliderView = findViewById(R.id.slideImage);
        chipKategorija = findViewById(R.id.chipKategorija);
        fabDelete = findViewById(R.id.fabDeleteShow);
        image1.setVisibility(View.VISIBLE);
        image2.setVisibility(View.VISIBLE);

        ArrayAdapter<TheaterModel.kategorija> adapter = new ArrayAdapter<TheaterModel.kategorija>(PerformanceDetail.this,
                android.R.layout.simple_spinner_item, TheaterModel.kategorija.values());

        adapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
        spinerKategorija.setPrompt(theaterModel.getKategorija());
        spinerKategorija.setAdapter(adapter);

        elementListener();
        int showId = getShowId();
        getTheaterDetail(showId);
    }

    private void getTheaterDetail(int id){
        Retrofit retrofitClient = RetrofitClient.getInstance2();
        iTheater = retrofitClient.create(ITheater.class);

        Call<List<TheaterModel>> call = iTheater.getPredstavaDetalji(id);
        call.enqueue(new Callback<List<TheaterModel>>() {
            @Override
            public void onResponse(Call<List<TheaterModel>> call, Response<List<TheaterModel>> response) {
                List<TheaterModel> list = response.body();

                if(response.isSuccessful()){
                    for(int i=0; i<list.size(); i++){
                        theaterModel.setNaziv(list.get(i).getNaziv());
                        theaterModel.setKategorija(list.get(i).getKategorija());
                        theaterModel.setDatumPremijere(list.get(i).getDatumPremijere());
                        theaterModel.setOpis(list.get(i).getOpis());
                        theaterModel.setRedatelj(list.get(i).getRedatelj());
                        theaterModel.setDramaturgija(list.get(i).getDramaturgija());
                        theaterModel.setGlumci(list.get(i).getGlumci());
                        theaterModel.setKostimografija(list.get(i).getKostimografija());
                        theaterModel.setScenografija(list.get(i).getScenografija());
                        theaterModel.setGlazba(list.get(i).getGlazba());
                        theaterModel.setKoreografija(list.get(i).getKoreografija());
                        theaterModel.setTehnickaPodrska(list.get(i).getTehnickaPodrska());
                        theaterModel.setSlika1(list.get(i).getSlika1());
                        theaterModel.setSlika2(list.get(i).getSlika2());
                        Picasso.get().load(theaterModel.getSlika1()).into(image1);
                        Picasso.get().load(theaterModel.getSlika2()).into(image2);
                        setText();
                    }
                }
                else Toast.makeText(getApplicationContext(),
                        "Podaci predstave nisu pronađeni!",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<List<TheaterModel>> call, Throwable t) {
                t.getMessage();
                t.printStackTrace();
            }
        });
    }

    private void elementListener(){
        imageDatumUpdate.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(PerformanceDetail.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,mDatePicker, year,month,day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        mDatePicker = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                Calendar calendar = Calendar.getInstance();
                calendar.set(year,month,dayOfMonth);

                SimpleDateFormat simple = new SimpleDateFormat("dd.MM.yyyy");
                formatDatum = simple.format(calendar.getTime());
                datumText.setText(formatDatum);
            }
        };

        spinerKategorija.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ((TextView) parent.getChildAt(0)).setTextColor(Color.WHITE);
                if(parent.getItemAtPosition(position).equals("")){ }
                else{
                    String item = parent.getItemAtPosition(position).toString();
                    theaterModel.setKategorija(item);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });

        btnUpdate.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                try { getText(); }
                catch (IOException e) { e.printStackTrace(); }
            }
        });

        switchUpdate.setText("Ne");
        switchUpdate.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                if(switchUpdate.isChecked()) {
                    switchUpdate.setText("Da");
                    switchUpdate.getTextOn().toString();
                    naslovEdit.setVisibility(View.VISIBLE);
                    editableText(true);
                }
                else {
                    switchUpdate.setText("Ne");
                    switchUpdate.getTextOff().toString();
                    naslovEdit.setVisibility(View.GONE);
                    editableText(false);
                }
            }
        });

        image1.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(PerformanceDetail.this, ImageGallery.class);
                startActivityForResult(intent,1);
            }
        });

        image2.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(PerformanceDetail.this,ImageGallery.class);
                startActivityForResult(intent, 2);
            }
        });

        fabDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new SweetAlertDialog(PerformanceDetail.this, SweetAlertDialog.CUSTOM_IMAGE_TYPE)
                        .setTitleText("Jeste li sigurni?")
                        .setCustomImage(R.drawable.ic_delete_black_24dp)
                        .setContentText("Jednom izbrisana predstava je zauvijek izbrisana!")
                        .setCancelText("Odustani!")
                        .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                sweetAlertDialog.cancel();
                            }
                        }).setConfirmText("Izbriši!").setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        sDialog.setTitleText("Izbrisano!")
                                .setContentText("Ponuda je izbrisana!")
                                .setConfirmText("U redu")
                                .setConfirmClickListener(null)
                                .showCancelButton(false)
                                .changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
                        deleteShow(getShowId());
                    }
                }).show();
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if(resultCode == RESULT_OK) {
                String imageString1 = data.getStringExtra("slika");
                theaterModel.setSlika1(imageString1);
                Picasso.get().load(imageString1).placeholder(R.drawable.pozadina).fit().into(image1);
            }
        }else if(requestCode == 2){
            if(resultCode == RESULT_OK){
                String imageString2 = data.getStringExtra("slika");
                theaterModel.setSlika2(imageString2);
                Picasso.get().load(imageString2).placeholder(R.drawable.pozadina).fit().into(image2);
            }
        }
    }

    private void getText() throws IOException {
        updateShow(naslovEdit.getText().toString(),theaterModel.getKategorija(),datumText.getText().toString(),
                opisEdit.getText().toString(),glumciEdit.getText().toString(), redateljEdit.getText().toString(),
                dramaturgijaEdit.getText().toString(), kostimografijaEdit.getText().toString(), scenografijaEdit.getText().toString(),
                koreografijaEdit.getText().toString(), glazbaEdit.getText().toString(), podrskaEdit.getText().toString(),
                theaterModel.getSlika1(),theaterModel.getSlika2());
    }

    private int getShowId(){
        try{ Intent intent = getIntent();
            int id = intent.getIntExtra("id",0);
            return id; }
        catch(Exception e){ e.printStackTrace(); }
        return 0;
    }

    private void editableText(Boolean flag){

        if(flag.equals(true)){
            naslovEdit.setFocusableInTouchMode(true);
            naslovEdit.setSingleLine(false);

            opisEdit.setFocusableInTouchMode(true);;
            opisEdit.setSingleLine(false);

            redateljEdit.setFocusableInTouchMode(true);
            redateljEdit.setSingleLine(false);

            glumciEdit.setFocusableInTouchMode(true);
            glumciEdit.setMaxLines(5);

            dramaturgijaEdit.setFocusableInTouchMode(true);
            dramaturgijaEdit.setSingleLine(false);

            kostimografijaEdit.setFocusableInTouchMode(true);
            kostimografijaEdit.setSingleLine(false);

            scenografijaEdit.setFocusableInTouchMode(true);
            scenografijaEdit.setSingleLine(false);

            glazbaEdit.setFocusableInTouchMode(true);
            glazbaEdit.setSingleLine(false);

            koreografijaEdit.setFocusableInTouchMode(true);
            koreografijaEdit.setSingleLine(false);

            podrskaEdit.setFocusableInTouchMode(true);
            podrskaEdit.setSingleLine(false);

            spinerKategorija.setFocusableInTouchMode(true);
            spinerKategorija.setEnabled(true);
            spinerKategorija.setClickable(true);

            linearKategorija.setVisibility(View.VISIBLE);
            layoutChip.setVisibility(View.GONE);
            image1.setVisibility(View.VISIBLE);
            image2.setVisibility(View.VISIBLE);
            imageDatumUpdate.setVisibility(View.VISIBLE);
            btnUpdate.setVisibility(View.VISIBLE);
        }
        else if(flag.equals(false)){
            naslovEdit.setFocusable(false);
            opisEdit.setFocusable(false);
            redateljEdit.setFocusable(false);
            glumciEdit.setFocusable(false);
            dramaturgijaEdit.setFocusable(false);
            kostimografijaEdit.setFocusable(false);
            scenografijaEdit.setFocusable(false);
            glazbaEdit.setFocusable(false);
            koreografijaEdit.setFocusable(false);
            podrskaEdit.setFocusable(false);
            spinerKategorija.setFocusable(false);
            spinerKategorija.setFocusable(false);
            spinerKategorija.setEnabled(false);
            spinerKategorija.setClickable(false);
            image1.setVisibility(View.GONE);
            image2.setVisibility(View.GONE);
            imageDatumUpdate.setVisibility(View.GONE);
            btnUpdate.setVisibility(View.GONE);
            linearKategorija.setVisibility(View.GONE);
            layoutChip.setVisibility(View.VISIBLE);
        }
    }


    private void setText(){
        ArrayList<ImageModel> sliderImage = new ArrayList<>();

        naslovEdit.setText(theaterModel.getNaziv());
        chipKategorija.setText(theaterModel.getKategorija());
        datumText.setText(theaterModel.getDatumPremijere());
        opisEdit.setText(theaterModel.getOpis());
        redateljEdit.setText(theaterModel.getRedatelj());
        dramaturgijaEdit.setText(theaterModel.getDramaturgija());
        glumciEdit.setText(theaterModel.getGlumci());
        kostimografijaEdit.setText(theaterModel.getKostimografija());
        scenografijaEdit.setText(theaterModel.getScenografija());
        glazbaEdit.setText(theaterModel.getGlazba());
        koreografijaEdit.setText(theaterModel.getKoreografija());
        String s = theaterModel.getTehnickaPodrska();
        s = s.replace(",",",\n");
        podrskaEdit.setText(s);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle(theaterModel.getNaziv());

        sliderImage.add(new ImageModel(theaterModel.getSlika1()));
        sliderImage.add(new ImageModel(theaterModel.getSlika2()));

        SliderAdapter adapter = new SliderAdapter(this, sliderImage);
        sliderView.setAutoCycleDirection(SliderView.LAYOUT_DIRECTION_LTR);
        sliderView.setSliderAdapter(adapter);
        sliderView.setScrollTimeInSec(4);
        sliderView.setAutoCycle(true);
        sliderView.startAutoCycle();

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PerformanceDetail.this, PerformancesActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void updateShow(String naziv, String kategorija, String datum, String opis, String glumci,
                            String redatelj,String dramaturgija, String kostimografija,
                            String scenografija, String koreografija,String glazba, String podrska,
                            String slika1, String slika2){

        Retrofit retrofitClient = RetrofitClient.getInstance2();
        iTheater = retrofitClient.create(ITheater.class);
        int id = getShowId();
        Call<TheaterModel> call = iTheater.updateDetaljiPredstave(id, naziv, kategorija, datum, opis, glumci,
                redatelj,dramaturgija, kostimografija, scenografija,
                glazba, koreografija,podrska,slika1,slika2);
        call.enqueue(new Callback<TheaterModel>() {
            @Override
            public void onResponse(Call<TheaterModel> call, Response<TheaterModel> response) {
                if(response.isSuccessful()) {
                    TheaterModel responseBody = response.body();
                    Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content),
                            "" + responseBody.getMessage(), Snackbar.LENGTH_SHORT);
                    View snackbar_view = snackbar.getView();
                    TextView snackbar_text = (TextView) snackbar_view.findViewById(R.id.snackbar_text);
                    snackbar_text.setCompoundDrawablesWithIntrinsicBounds(0, 0,
                            R.drawable.ic_check_black_24dp, 0);
                    snackbar_text.setGravity(Gravity.CENTER);
                    snackbar.show();
                    getTheaterDetail(getShowId());
                }
            }

            @Override
            public void onFailure(Call<TheaterModel> call, Throwable t) {
                t.printStackTrace();
                t.getMessage();
            }
        });
    }

    private void deleteShow(int id){
        Retrofit retrofitClient = RetrofitClient.getInstance2();
        iTheater = retrofitClient.create(ITheater.class);

        Call<ResponseBody> call = iTheater.deleteShow(id);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.isSuccessful()){
                    new SweetAlertDialog(PerformanceDetail.this)
                            .setTitleText("Predstava je izbrisana!")
                            .show();
                    Intent intent = new Intent(PerformanceDetail.this,ReservationActivity.class);
                    startActivity(intent);
                    finish();
                }else if(response.code()==400){
                    Toast.makeText(getApplicationContext(),"Greška! Predstava nije izbrisana!",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                t.printStackTrace();
                t.getMessage();
            }
        });
    }
}
