package android.unipu.theater.fragmenti.administrator;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.unipu.theater.administrator.AdminWindow;
import android.unipu.theater.administrator.ImageGallery;
import android.unipu.theater.model.TheaterModel;
import android.unipu.theater.retrofit.ITheater;
import android.unipu.theater.retrofit.RetrofitClient;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.unipu.theater.R;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static android.app.Activity.RESULT_OK;

public class TheaterPlay extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private EditText nazivPredstaveEdit, opisEdit, redateljEdit, glumciEdit,
                    dramaturgijaEdit, kostimografijaEdit, scenografijaEdit,
                    glazbaEdit, koreografijaEdit, tehnickaPodrskaEdit;
    private TextView textDate;
    private Spinner kategorijaSpinner;
    private ImageButton dateButton;
    private DatePickerDialog.OnDateSetListener mDatePicker;
    private Button spremiPredstavuBtn,btnSlike;
    private TheaterModel theaterModel;
    private ImageView imageView1, imageView2;
    private ITheater iTheater;
    private String mParam1,mParam2,formatDate;

    public TheaterPlay() {
        // Required empty public constructor
    }

    public static TheaterPlay newInstance(String param1, String param2) {
        TheaterPlay fragment = new TheaterPlay();
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

        View view = inflater.inflate(R.layout.fragment_theater_play, container, false);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE|
                WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        theaterModel = new TheaterModel();
        initViewFragment(view);

        return view;
    }

    private void initViewFragment(View view){

        imageView1 = (ImageView) view.findViewById(R.id.imageTheater1);
        imageView2 = (ImageView) view.findViewById(R.id.imageTheater2);
        nazivPredstaveEdit = (EditText) view.findViewById(R.id.nazivPredstaveEdit);
        opisEdit = (EditText) view.findViewById(R.id.opisEdit);
        redateljEdit = (EditText) view.findViewById(R.id.redateljEdit);
        glumciEdit = (EditText) view.findViewById(R.id.glumciEdit);
        dramaturgijaEdit = (EditText) view.findViewById(R.id.dramaturgijaEdit);
        kostimografijaEdit = (EditText) view.findViewById(R.id.kostimografijaEdit);
        scenografijaEdit = (EditText) view.findViewById(R.id.scenografijaEdit);
        glazbaEdit = (EditText) view.findViewById(R.id.glazbaEdit);
        koreografijaEdit = (EditText) view.findViewById(R.id.koreografijaEdit);
        tehnickaPodrskaEdit = (EditText) view.findViewById(R.id.tehnickaPodrskaEdit);
        kategorijaSpinner = (Spinner) view.findViewById(R.id.kategorijaSpinner);
        textDate = (TextView) view.findViewById(R.id.textDatum);
        spremiPredstavuBtn = (Button) view.findViewById(R.id.predstavaButton);

        Toolbar toolbar = view.findViewById(R.id.toolbarTheaterNew);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getContext(), AdminWindow.class);
                startActivity(i);
            }
        });

        dateButton = (ImageButton) view.findViewById(R.id.imageDatum);
        dateButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(getActivity(),
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
                formatDate = simple.format(calendar.getTime());
                textDate.setText(formatDate);

            }
        };

        spremiPredstavuBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                try {
                    checkField(nazivPredstaveEdit.getText().toString(), textDate.getText().toString(),
                            opisEdit.getText().toString(),glumciEdit.getText().toString(), redateljEdit.getText().toString(),
                            dramaturgijaEdit.getText().toString(), kostimografijaEdit.getText().toString(),
                            scenografijaEdit.getText().toString(),glazbaEdit.getText().toString(),
                            koreografijaEdit.getText().toString(),tehnickaPodrskaEdit.getText().toString());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        imageView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ImageGallery.class);
                startActivityForResult(intent,1);
            }
        });

        imageView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),ImageGallery.class);
                startActivityForResult(intent, 2);
            }
        });
        spinnerCategory();
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if(resultCode == RESULT_OK) {
                String imageString1 = data.getStringExtra("slika");
                theaterModel.setSlika1(imageString1);
                Picasso.get().load(imageString1).placeholder(R.drawable.pozadina).fit().into(imageView1);
            }
        }else if(requestCode == 2){
            if(resultCode == RESULT_OK){
                String imageString2 = data.getStringExtra("slika");
                theaterModel.setSlika2(imageString2);
                Picasso.get().load(imageString2).placeholder(R.drawable.pozadina).fit().into(imageView2);
            }
        }
    }

    private void spinnerCategory(){
        ArrayAdapter<TheaterModel.kategorija> adapter = new ArrayAdapter<TheaterModel.kategorija>(getContext(),
                android.R.layout.simple_spinner_item, TheaterModel.kategorija.values());

        adapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
        kategorijaSpinner.setAdapter(adapter);
        kategorijaSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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
    }

    private void checkField(String naziv, String datum, String opis, String redatelj, String glumci, String drama, String kostimografija,
                           String scenografija, String glazba, String koreografija, String podrska) throws IOException {

        int counter = 0;
        List<Uri> list = new ArrayList<>();
        list = theaterModel.getList();

        if(TextUtils.isEmpty(naziv)){
            nazivPredstaveEdit.setError("Unesite naziv predstave!");
        }else counter++;

        if(textDate.getText().toString().equals("")){
            textDate.setError("Odaberite datum!");
        }else counter++;

        if(theaterModel.getSlika1() == null || theaterModel.getSlika2() == null){
            Snackbar.make(getActivity().findViewById(R.id.content), "Slike nisu odabrane!", Snackbar.LENGTH_SHORT).show();
        }else counter++;

        if(TextUtils.isEmpty(opis)){
            opisEdit.setError("Unesite opis!");
        } else counter++;

        if(TextUtils.isEmpty(glumci)){
            glumciEdit.setError("Unesite glumce!");
        } else counter++;

        if(TextUtils.isEmpty(redatelj)){
            redateljEdit.setError("Unesite redatelja!");
        }else counter++;

        if(TextUtils.isEmpty(drama)){
            dramaturgijaEdit.setError("Unesite dramaturga!");
        }else counter++;

        if(TextUtils.isEmpty(kostimografija)){
            kostimografijaEdit.setError("Unesite kostimografa");
        }else counter++;

        if(TextUtils.isEmpty(scenografija)){
            scenografijaEdit.setError("Unesite scenografa");
        }else counter++;

        if(TextUtils.isEmpty(glazba)){
            glazbaEdit.setError("Unesite glazbenog izvođača");
        }else counter++;

        if(TextUtils.isEmpty(koreografija)){
            koreografijaEdit.setError("Unesite koreografa!");
        }else counter++;

        if(TextUtils.isEmpty(podrska)){
            tehnickaPodrskaEdit.setError("Unesite tehničku podršku");
        }else counter++;


        if(counter == 12){
            String kategorija = theaterModel.getKategorija();

            uploadPlay(naziv, kategorija, datum, opis, glumci, redatelj,drama,
                    kostimografija, scenografija, glazba, koreografija, podrska,
                    theaterModel.getSlika1(), theaterModel.getSlika2());
        }

    }

    private void uploadPlay(String naziv, String kategorija, String datum, String opis,String glumci,
                            String redatelj, String drama, String kostimografija, String scenografija,
                            String glazba, String koreografija, String podrska, String slika1, String slika2) throws IOException {

        Retrofit retrofitClient = RetrofitClient.getInstance2();
        iTheater = retrofitClient.create(ITheater.class);

        Call<TheaterModel> call = iTheater.uploadImage(naziv, kategorija, datum, opis, glumci,
                                                    redatelj, drama, kostimografija, scenografija,
                                                    glazba, koreografija, podrska, slika1, slika2);
        call.enqueue(new Callback<TheaterModel>() {
            @Override
            public void onResponse(Call<TheaterModel> call, Response<TheaterModel> response) {
                if (response.isSuccessful()) {
                    TheaterModel theater = response.body();
                    Snackbar snackbar = Snackbar.make(getActivity().findViewById(R.id.content), theater.getMessage(), Snackbar.LENGTH_SHORT);
                    View snackbar_view = snackbar.getView();

                    TextView snackbar_text = (TextView) snackbar_view.findViewById(R.id.snackbar_text);
                    snackbar_text.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.ic_save_black_24dp,0);
                    snackbar_text.setGravity(Gravity.CENTER);
                    snackbar.show();
                }
            }

            @Override
            public void onFailure(Call<TheaterModel> call, Throwable t) {
                t.printStackTrace();
                t.getMessage();
            }
        });
    }
}
