package android.unipu.theater.administrator;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.unipu.theater.R;
import android.unipu.theater.ShareMethods;
import android.unipu.theater.user.UserLogin;
import android.unipu.theater.user.UserRegistration;
import android.unipu.theater.model.UserModel;
import android.unipu.theater.retrofit.IAdministrator;
import android.unipu.theater.retrofit.RetrofitClient;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
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

public class AdministratorLogin extends AppCompatActivity {

    private FloatingActionButton fabPlus, fabLogin, fabRegister;
    Animation fabOpen, fabClose, rotateForward, rotateBack;
    private TextView registrationText, loginText;
    private EditText emailEdit, passwordEdit;
    private Button loginButton;
    private ShareMethods shareMethods;
    boolean isOpen = false;
    private IAdministrator iAdmin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_administrator_login);

        initView();
        setView();
    }

    private void initView(){
        shareMethods = new ShareMethods();
        passwordEdit = (EditText) findViewById(R.id.passwordAdmin);
        emailEdit = (EditText) findViewById(R.id.emailAdmin);
        loginButton = (Button) findViewById(R.id.adminLoginButton);
        registrationText = (TextView) findViewById(R.id.registrationFabText);
        loginText = (TextView) findViewById(R.id.loginFabText);
        fabPlus = (FloatingActionButton) findViewById(R.id.fabPlus);
        fabLogin = (FloatingActionButton) findViewById(R.id.fabLogin);
        fabRegister = (FloatingActionButton) findViewById(R.id.fabRegister);

        fabOpen = AnimationUtils.loadAnimation(this, R.anim.fab_open);
        fabClose = AnimationUtils.loadAnimation(this, R.anim.fab_close);
        rotateForward = AnimationUtils.loadAnimation(this, R.anim.rotate_fab);
        rotateBack = AnimationUtils.loadAnimation(this, R.anim.rotate_back_fab);
    }

    private void setView(){
        fabPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startAnimation();
            }
        });
        fabLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(AdministratorLogin.this, UserLogin.class);
                startActivity(i);
            }
        });
        fabRegister.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent i = new Intent(AdministratorLogin.this, UserRegistration.class);
                startActivity(i);
            }
        });
        loginButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                checkField(emailEdit.getText().toString(),
                        passwordEdit.getText().toString());
            }
        });
    }

    private void checkField(String email, String password){

        if(TextUtils.isEmpty(email)){
            emailEdit.setError("Unesite e-mail adresu!");
            emailEdit.setBackgroundResource(R.drawable.edit_text_red_border);
        }
        else emailEdit.setBackgroundResource(R.drawable.textfield);


        if(TextUtils.isEmpty(password)){
            passwordEdit.setError("Unesite lozinku!");
            passwordEdit.setBackgroundResource(R.drawable.edit_text_red_border);
        }
        else passwordEdit.setBackgroundResource(R.drawable.textfield);

        if(!(TextUtils.isEmpty(email))){
            if(!(TextUtils.isEmpty(password)) && shareMethods.validateEmail(getApplicationContext(),emailEdit)){
                loginAdministrator(email,password);
            }
        }
    }

    private void startAnimation(){

        if(isOpen){
            fabPlus.startAnimation(rotateForward);
            fabLogin.startAnimation(fabClose);
            fabRegister.startAnimation(fabClose);
            fabLogin.setClickable(false);
            fabRegister.setClickable(false);
            registrationText.setVisibility(View.GONE);
            loginText.setVisibility(View.GONE);
            isOpen = false;
        }

        else{
            fabPlus.startAnimation(rotateBack);
            fabLogin.startAnimation(fabOpen);
            fabRegister.startAnimation(fabOpen);
            fabLogin.setClickable(true);
            fabRegister.setClickable(true);
            registrationText.setVisibility(View.VISIBLE);
            loginText.setVisibility(View.VISIBLE);
            isOpen = true;
        }
    }

    private void loginAdministrator(String email, String password){

        Retrofit retrofitClient = RetrofitClient.getInstance2();
        iAdmin = retrofitClient.create(IAdministrator.class);

        Call<List<UserModel>> call = iAdmin.getAdmin(email,password);
        call.enqueue(new Callback<List<UserModel>> (){
            @Override
            public void onResponse(Call<List<UserModel>> call, Response<List<UserModel>> response) {
                List<UserModel> list = response.body();
                if(response.isSuccessful()) {
                    Intent intent = new Intent(AdministratorLogin.this, AdminWindow.class);
                    intent.putExtra("id", list.get(0).getId());
                    startActivity(intent);
                }
            }

            @Override
            public void onFailure(Call<List<UserModel>> call, Throwable t){ ;
                Toast.makeText(getApplicationContext(), "Pogrešna e-mail adresa ili lozinka!", Toast.LENGTH_SHORT).show();
                t.getMessage();
            }
        });
    }
}
