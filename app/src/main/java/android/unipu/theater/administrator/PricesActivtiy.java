package android.unipu.theater.administrator;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.unipu.theater.R;
import android.unipu.theater.adapter.PricesAdapter;
import android.unipu.theater.model.PricesModel;
import android.unipu.theater.retrofit.IPrices;
import android.unipu.theater.retrofit.RetrofitClient;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.material.chip.Chip;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class PricesActivtiy extends AppCompatActivity {

    private Button btnSpremiCijene;
    private TextView datumText,closeText;
    private EditText cijeneNajmanje, cijeneOdrasli, cijeneStudenti,
            cijeneUmirovljenici,cijenaPremijere, cijenaDana;
    private Chip deleteChip;
    private View popupView;
    private List<PricesModel> listPrices;
    private View view;
    private RecyclerView recyclerPrices;
    private IPrices iPrices;
    private DateTimeFormatter dateTime;
    private PricesModel pricesModel;
    private Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prices_activtiy);

        initView();
        initDialog();
        getData(2);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
       MenuInflater menuInflater = getMenuInflater();
       menuInflater.inflate(R.menu.action_bar_insert, menu);
       return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int menuItem = item.getItemId();
        switch (menuItem) {
            case R.id.insertIcon:{
                dialog.show();
            }return true;

            case R.id.dateSortASC:{
                getData(1);
            }break;

            case R.id.dateSortDESC:{
                getData(2);
            }break;
        }
        return super.onOptionsItemSelected(item);
    }

    // Dohvat datuma i vremena prilikom unosa
    private String getCurrentTime(){
        String date = null;
        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try { date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Calendar.getInstance().getTime());
            datumText.setText(formatter.format(date));
            pricesModel.setDatum(datumText.getText().toString());
            return date;
        }catch(Exception e){ e.printStackTrace(); }
        return date;
    }

    private void initDialog(){
        pricesModel = new PricesModel();
        dialog = new Dialog(this);
        dialog.setContentView(R.layout.layout_prices_popup);
        btnSpremiCijene = (Button) dialog.findViewById(R.id.btnSpremiCijene);
        datumText = (TextView) dialog.findViewById(R.id.datumCijene);
        cijeneNajmanje = (EditText) dialog.findViewById(R.id.cijenaNajmanje);
        cijeneOdrasli = (EditText) dialog.findViewById(R.id.cijenaOdrasli);
        cijeneStudenti = (EditText) dialog.findViewById(R.id.cijenaStudenti);
        cijeneUmirovljenici = (EditText) dialog.findViewById(R.id.cijenaUmirovljenici);
        cijenaDana = (EditText) dialog.findViewById(R.id.cijenaDana);
        cijenaPremijere = (EditText) dialog.findViewById(R.id.cijenaPremijera);
        popupView = (View) findViewById(R.id.popupWindowPrices);

        view = (View) findViewById(R.id.pricesView);
        closeText = (TextView) dialog.findViewById(R.id.closeText);
        closeText.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                dialog.dismiss();
            }
        });
        btnSpremiCijene.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkField(cijeneNajmanje.getText().toString(),
                        cijeneOdrasli.getText().toString(),
                        cijeneStudenti.getText().toString(),
                        cijeneUmirovljenici.getText().toString(),
                        cijenaPremijere.getText().toString(),
                        cijenaDana.getText().toString());
            }
        });
    }
    private void initView(){
        Toolbar toolbar = findViewById(R.id.toolbarPrices);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(PricesActivtiy.this, AdminWindow.class);
                startActivity(i);
            }
        });

        recyclerPrices = (RecyclerView) findViewById(R.id.recyclerViewPrices);
        recyclerPrices.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE|
                WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        Retrofit retrofitClient = RetrofitClient.getInstance2();
        iPrices = retrofitClient.create(IPrices.class);
    }


    private void checkField(String karta_djeca, String karta_odrasli, String karta_studenti,
                            String karta_umirovljenici,String cijena_dana, String cijena_premijera){
        if(TextUtils.isEmpty(karta_djeca)){
            cijeneNajmanje.setError("Unesite cijenu!");
            cijeneNajmanje.setBackgroundResource(R.drawable.normal_edittext);
        }
        else {
            pricesModel.setCijene_djeca(Float.parseFloat(karta_djeca));
            cijeneNajmanje.setBackgroundResource(R.color.bluedark);
        }

        if(TextUtils.isEmpty(karta_odrasli)){
            cijeneOdrasli.setError("Unesite cijenu!");
            cijeneOdrasli.setBackgroundResource(R.drawable.normal_edittext);
        }
        else{
            pricesModel.setCijene_odrasli(Float.parseFloat(karta_odrasli));
            cijeneOdrasli.setBackgroundResource(R.color.bluedark);
        }

        if(TextUtils.isEmpty(karta_studenti)){
            cijeneStudenti.setError("Unesite cijenu!");
            cijeneStudenti.setBackgroundResource(R.drawable.normal_edittext);
        }
        else{
            pricesModel.setCijene_studenti(Float.parseFloat(karta_studenti));
            cijeneStudenti.setBackgroundResource(R.color.bluedark);
        }

        if(TextUtils.isEmpty(karta_umirovljenici)){
            cijeneUmirovljenici.setError("Unesite cijenu!");
            cijeneUmirovljenici.setBackgroundResource(R.drawable.normal_edittext);
        }
        else {
            pricesModel.setCijene_umirovljenici(Float.parseFloat(karta_umirovljenici));
            cijeneUmirovljenici.setBackgroundResource(R.color.bluedark);
        }

        if(TextUtils.isEmpty(cijena_dana)){
            cijenaDana.setError("Unesite cijenu!");
            cijenaDana.setBackgroundResource(R.drawable.normal_edittext);
        }
        else {
            pricesModel.setDan(Float.parseFloat(cijena_dana));
            cijenaDana.setBackgroundResource(R.color.bluedark);
        }

        if(TextUtils.isEmpty(cijena_premijera)){
            cijenaPremijere.setError("Unesite cijenu!");
            cijenaPremijere.setBackgroundResource(R.drawable.normal_edittext);
        }
        else {
            pricesModel.setPremijera(Float.parseFloat(cijena_premijera));
            cijenaPremijere.setBackgroundResource(R.color.bluedark);
        }

        if(!(TextUtils.isEmpty(karta_djeca)) && !(TextUtils.isEmpty(karta_odrasli)) &&
                !(TextUtils.isEmpty(karta_studenti) && !(TextUtils.isEmpty(karta_umirovljenici)) &&
                        !(TextUtils.isEmpty(cijena_dana)) && !(TextUtils.isEmpty(cijena_premijera)))){
                insertPrices();
        }
    }

    private void insertPrices(){
        Call<ResponseBody> call = iPrices.insertPrices(getCurrentTime(), pricesModel.getCijeneDjeca(),
                                                        pricesModel.getCijene_odrasli(), pricesModel.getCijene_studenti(),
                                                        pricesModel.getCijene_umirovljenici(), pricesModel.getPremijera(),
                                                        pricesModel.getDan());
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                new SweetAlertDialog(dialog.getContext(),SweetAlertDialog.SUCCESS_TYPE).
                        setTitleText("Uspješno dodano").
                        setContentText("Dodana je nova cijena!").show();
                getData(1);
                dialog.dismiss();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                t.getMessage();
                t.printStackTrace();
            }
        });
    }


    public void getData(int broj){
        Call<List<PricesModel>> call = iPrices.getPrices(broj);
        call.enqueue(new Callback<List<PricesModel>>(){
            @Override
            public void onResponse(Call<List<PricesModel>> call, Response<List<PricesModel>> response){
                List<PricesModel> list = response.body();
                listPrices = new ArrayList<>();

                if(response.isSuccessful() && list != null){
                    for(int i=0; i<list.size(); i++){
                        pricesModel.setId(list.get(i).getId());
                        listPrices.add(new PricesModel(list.get(i).getId(),list.get(i).getDatum(),list.get(i).getCijeneDjeca(),
                                list.get(i).getCijene_odrasli(),list.get(i).getCijene_studenti(),list.get(i).getCijene_umirovljenici(),
                                list.get(i).getPremijera(),list.get(i).getDan()));

                        PricesAdapter adapter = new PricesAdapter(listPrices);
                        adapter.notifyDataSetChanged();
                        recyclerPrices.setAdapter(adapter);
                        clearEditText();
                    }
                }
            }

            @Override
            public void onFailure(Call<List<PricesModel>> call, Throwable t){
                t.getMessage();
                t.printStackTrace();
            }
        });
    }

    private void clearEditText(){
        datumText.setText("");
        cijeneNajmanje.getText().clear();
        cijeneOdrasli.getText().clear();
        cijeneStudenti.getText().clear();
        cijeneUmirovljenici.getText().clear();
        cijenaDana.getText().clear();
        cijenaPremijere.getText().clear();
    }

}
