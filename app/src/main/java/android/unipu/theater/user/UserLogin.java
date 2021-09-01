package android.unipu.theater.user;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.unipu.theater.R;
import android.unipu.theater.ShareMethods;
import android.unipu.theater.administrator.AdministratorLogin;
import android.unipu.theater.retrofit.IKorisnik;
import android.unipu.theater.retrofit.RetrofitClient;
import android.unipu.theater.model.UserModel;
import android.view.View;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class UserLogin extends AppCompatActivity {

    private Button btnLogin;
    private EditText emailText;
    private EditText passwordText;
    private TextView registrationLink;
    private IKorisnik iLogin;
    private FloatingActionButton adminButton;
    private ShareMethods shareMethods;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_login);

        initView();
        setListener();
    }

    private void initView(){
        shareMethods = new ShareMethods();
        btnLogin = (Button) findViewById(R.id.login);
        emailText = (EditText) findViewById(R.id.email);
        passwordText = (EditText) findViewById(R.id.password);
        registrationLink = (TextView) findViewById(R.id.registrationLink);
        adminButton = (FloatingActionButton) findViewById(R.id.adminButton);

        String stil = "<font color='#466ff1'>Registracija</font>";
        registrationLink.setText(Html.fromHtml(stil), TextView.BufferType.SPANNABLE);
    }

    private void setListener(){

        Animation anim = android.view.animation.
                AnimationUtils.loadAnimation(adminButton.getContext(),  R.anim.shake_login_fab);
        anim.setDuration(200L);

        registrationLink.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserLogin.this, UserRegistration.class);
                startActivity(intent);
                finish();
            }
        });

        adminButton.startAnimation(anim);
        adminButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(UserLogin.this, AdministratorLogin.class);
                startActivity(i);
                finish();
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                checkArray(emailText.getText().toString(),
                        passwordText.getText().toString());
            }
        });
    }

    private void checkArray(String email, String password){

        if(TextUtils.isEmpty(email)){
            emailText.setError("Unesite e-mail adresu!");
            emailText.setBackgroundResource(R.drawable.edit_text_red_border);
        }
        else emailText.setBackgroundResource(R.drawable.textfield);

        if(TextUtils.isEmpty(password)){
            passwordText.setError("Unesite lozinku!");
            passwordText.setBackgroundResource(R.drawable.edit_text_red_border);
        }
        else passwordText.setBackgroundResource(R.drawable.textfield);

        if(!(TextUtils.isEmpty(email))){
            if(!(TextUtils.isEmpty(password)) && shareMethods.validateEmail(getApplicationContext(),emailText))
                loginUser(email,password);
        }
    }

    private void loginUser(String email, String lozinka){

        Retrofit retrofitClient = RetrofitClient.getInstance2();

        // Metoda web servisa prema IKorisnik sučelju
        iLogin = retrofitClient.create(IKorisnik.class);

        // Asinkrono povezivanje web servisa s parametrima metode
        Call<List<UserModel>> call = iLogin.getUser(email,lozinka);

        // Implementacija događaja
        call.enqueue(new Callback<List<UserModel>>() {

            // U slučaju da je poziv uspješan
            @Override
            public void onResponse(Call<List<UserModel>> call, Response<List<UserModel>> response) {

                if(response.isSuccessful()) {
                    // Podaci korisnika postoje u bazi podataka
                    Intent intent = new Intent(UserLogin.this, MainWindow.class);
                    intent.putExtra("email", emailText.getText().toString());
                    startActivity(intent);
                    finish();
                }
                // Nema podataka o korisniku u bazi podataka, te se ispisuje poruka o pogrešci
                else if(response.code() == 400){
                    Toast.makeText(getApplicationContext(),
                            "Pogrešna e-mail adresa ili lozinka!",Toast.LENGTH_SHORT).show();
                }
            }

            // U slučaju da poziv nije uspješan
            @Override
            public void onFailure(Call<List<UserModel>> call, Throwable t){ ;
                Toast.makeText(getApplicationContext(), "Pogreška, poziv nije uspješan!", Toast.LENGTH_SHORT).show();
                t.printStackTrace();
                t.getMessage();
            }
        });
    }
}
