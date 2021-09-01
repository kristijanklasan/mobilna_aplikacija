package android.unipu.theater.user;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.unipu.theater.R;
import android.unipu.theater.ShareMethods;
import android.unipu.theater.retrofit.IKorisnik;
import android.unipu.theater.retrofit.RetrofitClient;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class UserRegistration extends AppCompatActivity {

    private EditText nameText;
    private EditText lastNameText;
    private EditText emailText;
    private EditText passwordText;
    private EditText phoneText;
    private Button btnRegistration;
    private IKorisnik iKorisnik;
    private TextView loginLink;
    private ShareMethods shareMethods;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_registration);

        shareMethods = new ShareMethods();
        nameText = (EditText) findViewById(R.id.nameRegistration);
        lastNameText = (EditText) findViewById(R.id.lastNameRegistration);
        emailText = (EditText) findViewById(R.id.emailRegistration);
        phoneText = (EditText) findViewById(R.id.phoneRegistration);
        passwordText = (EditText) findViewById(R.id.passwordRegistration);
        loginLink = (TextView) findViewById(R.id.loginLink);

        loginLink = (TextView) findViewById(R.id.loginLink);
        String stil = "<font color='#fc0a3a'>Prijava</font>";
        loginLink.setText(Html.fromHtml(stil), TextView.BufferType.SPANNABLE);

        loginLink.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserRegistration.this, UserLogin.class);
                startActivity(intent);
                finish();
            }
        });

        btnRegistration = (Button) findViewById(R.id.registration);
        btnRegistration.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                checkArray(nameText.getText().toString(),
                        lastNameText.getText().toString(),
                        phoneText.getText().toString(),
                        emailText.getText().toString(),
                        passwordText.getText().toString());
            }
        });

    }

    private void checkArray(String ime, String prezime, String telefon, String email, String lozinka){
        if (TextUtils.isEmpty(ime)) {
            nameText.setError("Polje ime mora biti popunjeno!");
            nameText.setBackgroundResource(R.drawable.edit_text_red_border);
        }
        else{
            nameText.setBackgroundResource(R.drawable.textfield);
        }

        if (TextUtils.isEmpty(prezime)) {
            lastNameText.setError("Polje prezime mora biti popunjeno!");
            lastNameText.setBackgroundResource(R.drawable.edit_text_red_border);
        }
        else{
            lastNameText.setBackgroundResource(R.drawable.textfield);
        }

        if (TextUtils.isEmpty(telefon)) {
            phoneText.setError("Unesite broj telefona!");
            phoneText.setBackgroundResource(R.drawable.edit_text_red_border);
        }
        else{
            phoneText.setBackgroundResource(R.drawable.textfield);
        }

        if(TextUtils.isEmpty(email)){
            emailText.setError("Unesite e-mail adresu!");
            emailText.setBackgroundResource(R.drawable.edit_text_red_border);
        }

        else{
            emailText.setBackgroundResource(R.drawable.textfield);
        }

        if(TextUtils.isEmpty(lozinka)){
            passwordText.setError("Unesite lozinku!");
            passwordText.setBackgroundResource(R.drawable.edit_text_red_border);
        }
        else{
            passwordText.setBackgroundResource(R.drawable.textfield);
        }

        if(!(TextUtils.isEmpty(lozinka)) && !(TextUtils.isEmpty(email)) &&
                !(TextUtils.isEmpty(telefon)) && !(TextUtils.isEmpty(prezime) &&
                shareMethods.validateEmail(getApplicationContext(),emailText))

                && !(TextUtils.isEmpty(ime)))
                registerUser(ime, prezime, telefon, email, lozinka);
    }

    private void registerUser(String ime, String prezime, String telefon, String email, String lozinka) {

        Retrofit retrofitClient = RetrofitClient.getInstance2();
        iKorisnik = retrofitClient.create(IKorisnik.class);

        Call<Object> call = iKorisnik.registerUser(ime, prezime, telefon, email, lozinka);
        call.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                if(response.isSuccessful()){
                    String responseBody = response.body().toString();
                    if(responseBody.equals("true")) {

                        new SweetAlertDialog(UserRegistration.this, SweetAlertDialog.SUCCESS_TYPE)
                                .setTitleText("Uspješna registracija!")
                                .setConfirmText("Nastavi..")
                                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                                        Intent intent = new Intent(UserRegistration.this, MainWindow.class);
                                        intent.putExtra("email", emailText.getText().toString());
                                        startActivity(intent);
                                        finish();
                                    }
                                }).show();
                    }
                }
                else if(response.code() == 404) {
                    Toast.makeText(getApplicationContext(),
                            "Korisnik već postoji!", Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(getApplicationContext(),
                            "Poziv nije uspješan!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                t.printStackTrace();
                t.getMessage();
            }
        });
    }


}
