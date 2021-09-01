package android.unipu.theater.fragmenti.administrator;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.unipu.theater.R;
import android.unipu.theater.ShareMethods;
import android.unipu.theater.retrofit.IAdministrator;
import android.unipu.theater.retrofit.RetrofitClient;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import cn.pedant.SweetAlert.SweetAlertDialog;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class NewUserFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private EditText ime,prezime,broj, email, lozinka, novaLozinka;
    private Button btnDodaj;
    private IAdministrator iAdministrator;
    private ShareMethods shareMethods;
    private String mParam1;
    private String mParam2;

    public NewUserFragment() {
        // Required empty public constructor
    }

    public static NewUserFragment newInstance(String param1, String param2) {
        NewUserFragment fragment = new NewUserFragment();
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
        View view = inflater.inflate(R.layout.fragment_new_user, container, false);

        initView(view);
        return view;
    }

    private void initView(View view){
        shareMethods = new ShareMethods();
        ime = (EditText) view.findViewById(R.id.imeNewAdmin);
        prezime = (EditText) view.findViewById(R.id.prezimeNewAdmin);
        broj = (EditText) view.findViewById(R.id.brojNewAdmin);
        email = (EditText) view.findViewById(R.id.emailNewAdmin);
        lozinka = (EditText) view.findViewById(R.id.lozinkaNewAdmin);
        novaLozinka = (EditText) view.findViewById(R.id.potvrdaLozinkeNewAdmin);
        btnDodaj = (Button) view.findViewById(R.id.btnDodajAdmin);
        setElements();
    }

    private void setElements(){
        btnDodaj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new SweetAlertDialog(getContext(), SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("Jeste li sigurni?")
                        .setContentText("Želite li dodati novog administratora?")
                        .setConfirmText("Dodaj!")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                checkData(ime.getText().toString(),prezime.getText().toString(),
                                        broj.getText().toString(),email.getText().toString(),
                                        lozinka.getText().toString(),novaLozinka.getText().toString());
                                sDialog.dismissWithAnimation();
                            }
                        })
                        .setCancelButton("Odustani",null)
                        .show();
            }
        });
    }

    private void checkData(String imeString, String prezimeString, String brojString, String emailString,
                           String lozinkaString, String novaLozinkaString){
        if(TextUtils.isEmpty(imeString))
            ime.setError("Unesite ime!");
        else ime.setBackgroundResource(R.drawable.normal_edittext);

        if(TextUtils.isEmpty(prezimeString))
            prezime.setError("Unesite prezime!");
        else prezime.setBackgroundResource(R.drawable.normal_edittext);

        if(TextUtils.isEmpty(brojString))
            broj.setError("Unesite broj telefona!");
        else broj.setBackgroundResource(R.drawable.normal_edittext);

        if(TextUtils.isEmpty(emailString)) email.setError("Unesite e-mail adresu!");
        else email.setBackgroundResource(R.drawable.normal_edittext);

        if(TextUtils.isEmpty(lozinkaString))
            lozinka.setError("Unesite lozinku!");
        else lozinka.setBackgroundResource(R.drawable.normal_edittext);

        if(TextUtils.isEmpty(novaLozinkaString))
            novaLozinka.setError("Unesite potvrdu lozinke!");
        else novaLozinka.setBackgroundResource(R.drawable.normal_edittext);

        boolean checkEmail = shareMethods.validateEmail(getContext(),email);

        if(!TextUtils.isEmpty(imeString) && !TextUtils.isEmpty(prezimeString) && !TextUtils.isEmpty(brojString)
        && checkEmail && !TextUtils.isEmpty(lozinkaString) && !TextUtils.isEmpty(novaLozinkaString)){
            if(lozinkaString.equals(novaLozinkaString)){
                int broj = Integer.parseInt(brojString);
                insertAdmin(imeString,prezimeString,broj,emailString,lozinkaString);
            }else Toast.makeText(getContext(),"Lozinke nisu jednake!",Toast.LENGTH_LONG).show();
        }
    }

    private void insertAdmin(String ime,String prezime, int broj, String email, String lozinka){
        Retrofit retrofit = RetrofitClient.getInstance2();
        iAdministrator = retrofit.create(IAdministrator.class);

        Call<ResponseBody> call = iAdministrator.insertAdmin(ime,prezime,broj,email,lozinka);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.isSuccessful()){
                    new SweetAlertDialog(getContext(),SweetAlertDialog.SUCCESS_TYPE)
                            .setContentText("Podaci su spremljeni!")
                            .setConfirmText("U redu")
                            .show();
                }else if(response.code() == 400){
                    new SweetAlertDialog(getContext(),SweetAlertDialog.ERROR_TYPE)
                            .setContentText("E-mail adresa postoji!")
                            .setConfirmText("U redu")
                            .show();
                    Toast.makeText(getContext(),"Došlo je do pogreške!",
                            Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                t.getMessage();
                t.printStackTrace();
            }
        });


    }
}
