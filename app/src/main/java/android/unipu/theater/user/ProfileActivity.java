package android.unipu.theater.user;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.unipu.theater.R;
import android.unipu.theater.ShareMethods;
import android.unipu.theater.model.PricesModel;
import android.unipu.theater.model.UserModel;
import android.unipu.theater.retrofit.IKorisnik;
import android.unipu.theater.retrofit.RetrofitClient;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ProfileActivity extends AppCompatActivity {

    private EditText imeEdit, prezimeEdit,racunEdit,
            emailEdit, telefonEdit, lozinkaEdit, ponovljenaLozinkaEdit;

    private LinearLayout layoutLozinka1, layoutLozinka2, layoutOdluka, layoutPotvrda;
    private CheckBox checkBoxProfile,checkBoxEmail;
    private IKorisnik iKorisnik;
    private FloatingActionButton fabPregled, fabUpdate;
    private Switch switchProfile;
    private TextView textPregled, textUpdate;
    private Button btnProfile;
    private PricesModel pricesModel;
    private UserModel userModel;
    private Toolbar toolbar;
    private ShareMethods shareMethods;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        initView();
        initViewElements();
        getProfileData();
    }

    private void setFabColor(Boolean flag){

        if(flag.equals(true)){
            fabPregled.setBackgroundTintList(fabPregled.getResources().getColorStateList(R.color.green));
            textPregled.setTextColor(textPregled.getResources().getColor(R.color.white));

            fabUpdate.setBackgroundTintList(fabUpdate.getResources().getColorStateList(R.color.gray_color));
            textUpdate.setTextColor(textUpdate.getResources().getColor(R.color.gray_tint));
        }

        else{
            fabPregled.setBackgroundTintList(fabPregled.getResources().getColorStateList(R.color.gray_color));
            textPregled.setTextColor(textPregled.getResources().getColor(R.color.gray_tint));

            fabUpdate.setBackgroundTintList(fabUpdate.getResources().getColorStateList(R.color.green));
            textUpdate.setTextColor(textUpdate.getResources().getColor(R.color.white));
        }
    }

    private void initView(){
        pricesModel = new PricesModel();
        userModel = new UserModel();
        shareMethods = new ShareMethods();
        layoutPotvrda = (LinearLayout) findViewById(R.id.layoutPotvrda);
        switchProfile = (Switch) findViewById(R.id.switchProfile);
        toolbar = (Toolbar) findViewById(R.id.toolbarUserProfile);
        imeEdit = (EditText) findViewById(R.id.imeEdit);
        prezimeEdit = (EditText) findViewById(R.id.prezimeEdit);
        telefonEdit = (EditText) findViewById(R.id.telefonEdit);
        emailEdit = (EditText) findViewById(R.id.emailEdit);
        racunEdit = (EditText) findViewById(R.id.racunEdit);
        lozinkaEdit = (EditText) findViewById(R.id.lozinkaEdit);
        ponovljenaLozinkaEdit = (EditText) findViewById(R.id.ponovljenaLozinkaEdit);
        fabPregled = (FloatingActionButton) findViewById(R.id.fabPregled);
        fabUpdate = (FloatingActionButton) findViewById(R.id.fabUpdate);
        textPregled = (TextView) findViewById(R.id.textPregled);
        textUpdate = (TextView) findViewById(R.id.textUpdate);
        checkBoxProfile = (CheckBox) findViewById(R.id.checkBoxProfile);
        checkBoxEmail = (CheckBox) findViewById(R.id.checkBoxEmailUser);
        layoutLozinka1 = (LinearLayout) findViewById(R.id.layoutLozinka1);
        layoutLozinka2 = (LinearLayout) findViewById(R.id.layoutLozinka2);
        layoutOdluka = (LinearLayout) findViewById(R.id.layoutOdluka);
        btnProfile = (Button) findViewById(R.id.btnProfile);

        Retrofit retrofitClient = RetrofitClient.getInstance2();
        iKorisnik = retrofitClient.create(IKorisnik.class);
        setFabColor(true);
    }

    private void initViewElements() {
        editText(false);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileActivity.this, MainWindow.class);
                startActivity(intent);
                finish();
            }
        });

        switchProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean flag = false;
                if (switchProfile.isChecked()) {
                    flag = true;
                    editText(flag);
                    setFabColor(false);
                    layoutPotvrda.setVisibility(View.VISIBLE);
                    layoutOdluka.setVisibility(View.VISIBLE);
                    btnProfile.setVisibility(View.VISIBLE);
                    checkBoxProfile.setChecked(false);
                    checkBoxProfile.setText(R.string.text_off);
                    setVisibility(false);
                } else {
                    flag = false;
                    editText(flag);
                    setFabColor(true);
                    layoutPotvrda.setVisibility(View.GONE);
                    layoutOdluka.setVisibility(View.GONE);
                    btnProfile.setVisibility(View.GONE);
                    setVisibility(false);
                }
            }
        });

        checkBoxEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkBoxEmail.isChecked()) {
                    checkBoxEmail.setText(R.string.text_on);
                    userModel.setPotvrda(true);

                } else {
                    checkBoxEmail.setText(R.string.text_off);
                    userModel.setPotvrda(false);
                }
            }
        });

        checkBoxProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkBoxProfile.isChecked()) {
                    checkBoxProfile.setText(R.string.text_on);
                    userModel.setOdgovor(true);
                    setVisibility(true);
                } else {
                    checkBoxProfile.setText(R.string.text_off);
                    userModel.setOdgovor(false);
                    setVisibility(false);
                }
            }
        });

        btnProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new SweetAlertDialog(ProfileActivity.this, SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("Želite li spremiti?")
                        .setContentText("Podaci će se promijeniti!")
                        .setConfirmText("Update")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                checkData();
                                sDialog.dismissWithAnimation();
                            }
                        })
                        .setCancelButton("Odustani",null)
                        .show();
            }
        });
    }

    private void checkData(){
        String ime = imeEdit.getText().toString();
        String prezime = prezimeEdit.getText().toString();
        String broj = telefonEdit.getText().toString();

        String novaLozinka = lozinkaEdit.getText().toString();
        String potvrdaLozinke = ponovljenaLozinkaEdit.getText().toString();
        int id = Integer.parseInt(userModel.getKljuc());
        boolean potvrda = false;
        String emailString = "";

        if(checkBoxEmail.isChecked()) {
            emailString = userModel.getEmail();
            potvrda = true;
        }
        else {
            emailString = emailEdit.getText().toString();
            potvrda = false;
        }
        boolean emailFlag = shareMethods.validateEmail(this, emailEdit);

        if(checkBoxProfile.isChecked()){
            if(novaLozinka.equals(potvrdaLozinke) && emailFlag){
                updateData(1, potvrda, id,ime,prezime,
                        broj, emailString, novaLozinka);
            }else{
                new SweetAlertDialog(this,SweetAlertDialog.ERROR_TYPE)
                        .setTitleText("Lozinke nisu jednake!")
                        .setConfirmText("U redu")
                        .show();
            }
        }else {
            if(!ime.isEmpty() && !prezime.isEmpty() && emailFlag){
                try{ updateData(2, potvrda, id, ime, prezime,
                        broj, emailString, novaLozinka);
                }catch (Exception e){ e.printStackTrace(); };
            }
        }
    }

    // Azuriranje korisnickih podataka
    private void updateData(int oznaka, boolean potvrda, int id, String ime, String prezime,
                            String broj, String email, String lozinka){
        Call<ResponseBody> call = null;
        if(oznaka == 1) call = iKorisnik.updateProfil2(id,potvrda,ime,prezime,broj,email,lozinka);
        else if (oznaka == 2) call = iKorisnik.updateProfil1(id,potvrda,ime,prezime,broj,email);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.isSuccessful()) succesfulDialog();
                else if(response.code() == 400){
                    Toast.makeText(getApplicationContext(),"Ažuriranje nije uspjelo!",Toast.LENGTH_SHORT).show();
                    failureDialog();
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                t.getMessage();
                t.printStackTrace();
            }
        });
    }

    private void setVisibility(Boolean flag){
        if(flag.equals(true)){
            layoutLozinka1.setVisibility(View.VISIBLE);
            layoutLozinka2.setVisibility(View.VISIBLE);
        }else {
            layoutLozinka1.setVisibility(View.GONE);
            layoutLozinka2.setVisibility(View.GONE);
        }
    }

    private void editText(Boolean flag){
        if(flag.equals(true)){
            imeEdit.setFocusableInTouchMode(true);
            imeEdit.setSingleLine(false);
            prezimeEdit.setFocusableInTouchMode(true);
            prezimeEdit.setSingleLine(false);
            telefonEdit.setFocusableInTouchMode(true);
            telefonEdit.setSingleLine(false);
            emailEdit.setFocusableInTouchMode(true);
            emailEdit.setSingleLine(false);
            racunEdit.setFocusableInTouchMode(true);
            racunEdit.setSingleLine(true);
        }
        else if(flag.equals(false)){
            imeEdit.setFocusable(false);
            prezimeEdit.setFocusable(false);
            telefonEdit.setFocusable(false);
            emailEdit.setFocusable(false);
        }
    }

    public void getProfileData(){
        Call<List<UserModel>> call = iKorisnik.getProfileData(Integer.parseInt(userModel.getKljuc()));
        call.enqueue(new Callback<List<UserModel>>() {
            @Override
            public void onResponse(Call<List<UserModel>> call, Response<List<UserModel>> response) {
                List<UserModel> list = response.body();
                if(response.isSuccessful()){
                    for(int i=0; i<list.size(); i++){
                        userModel.setIme(list.get(i).getIme());
                        userModel.setPrezime(list.get(i).getPrezime());
                        userModel.setEmail(list.get(i).getEmail());
                        userModel.setTelefon(list.get(i).getTelefon());
                        racunEdit.setText(list.get(i).getId());
                    }
                    imeEdit.setText(userModel.getIme());
                    prezimeEdit.setText(userModel.getPrezime());
                    emailEdit.setText(userModel.getEmail());
                    telefonEdit.setText(userModel.getTelefon());
                }
            }
            @Override
            public void onFailure(Call<List<UserModel>> call, Throwable t) {
                t.getMessage();
                t.printStackTrace();
            }
        });
    }

    private void succesfulDialog(){
        new SweetAlertDialog(ProfileActivity.this,SweetAlertDialog.SUCCESS_TYPE)
                .setTitleText("Uspješno!").setContentText("Podaci su se promijeniti!")
                .setConfirmText("U redu")
                .showCancelButton(false)
                .setConfirmClickListener(null).show();
    }

    private void failureDialog(){
        new SweetAlertDialog(ProfileActivity.this,SweetAlertDialog.ERROR_TYPE)
                .setTitleText("Neuspješno!").setContentText("Provjerite e-mail adresu!")
                .setConfirmText("U redu")
                .showCancelButton(false)
                .setConfirmClickListener(null).show();
    }
}
