package android.unipu.theater.fragmenti.administrator;

import android.os.Bundle;
import androidx.fragment.app.Fragment;

import android.unipu.theater.R;
import android.unipu.theater.ShareMethods;
import android.unipu.theater.model.OfferModel;
import android.unipu.theater.model.UserModel;
import android.unipu.theater.retrofit.IAdministrator;
import android.unipu.theater.retrofit.RetrofitClient;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ProfileDetail extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private OfferModel offerModel;
    private String mParam1;
    private String mParam2;
    private IAdministrator iAdmin;
    private UserModel userModel;
    private TextView sifra;
    private Switch switchUpdate;
    private EditText ime,prezime, broj, email, loznika, ponoviteLozinku;
    private CheckBox checkBox,checkBoxEmail;
    private LinearLayout linearLayout;
    private ShareMethods shareMethods;
    private Button btnUpdate;

    public ProfileDetail() {
        // Required empty public constructor
    }

    public static ProfileDetail newInstance(String param1, String param2) {
        ProfileDetail fragment = new ProfileDetail();
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
        View view = inflater
                .inflate(R.layout.fragment_profile_detail, container, false);
        initView(view);
        getKey();
        return view;
    }

    private void initView(View view){
        userModel = new UserModel();
        offerModel = new OfferModel();
        shareMethods = new ShareMethods();
        ime = (EditText) view.findViewById(R.id.editImeAdmin);
        prezime = (EditText) view.findViewById(R.id.editPrezimeAdmin);
        broj = (EditText) view.findViewById(R.id.editBrojAdmin);
        email = (EditText) view.findViewById(R.id.editEmailAdmin);
        ponoviteLozinku = (EditText) view.findViewById(R.id.editNovaLozinkaAdmin);
        loznika = (EditText) view.findViewById(R.id.editLozinkaAdmin);
        sifra = (TextView) view.findViewById(R.id.textSifraAdmin);
        switchUpdate = (Switch) view.findViewById(R.id.switchUpdateAdmin);
        checkBox = (CheckBox) view.findViewById(R.id.checkBoxUpdateAdmin);
        linearLayout = (LinearLayout) view.findViewById(R.id.linearLayoutAdmin);
        btnUpdate = (Button) view.findViewById(R.id.btnUpdateAdmin);
        checkBoxEmail = (CheckBox) view.findViewById(R.id.checkBoxEmailAdmin);
        Retrofit retrofit = RetrofitClient.getInstance2();
        iAdmin = retrofit.create(IAdministrator.class);
        setElements();
    }

    private void getKey(){
        try { Bundle bundle = getArguments();
            if(bundle != null){
                int id = bundle.getInt("id");
                getAdminData(id);
            }
        }catch (Exception e){ e.printStackTrace(); }
    }

    private void setElements(){
        editableText(false);
        switchUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(switchUpdate.isChecked()){
                    editableText(true);
                    btnUpdate.setVisibility(View.VISIBLE);
                    switchUpdate.setText(R.string.da);
                }else{
                    editableText(false);
                    btnUpdate.setVisibility(View.GONE);
                    switchUpdate.setText(R.string.ne);
                }
            }
        });

        checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkBox.isChecked()) {
                    linearLayout.setVisibility(View.VISIBLE);
                    checkBox.setText(R.string.text_on);
                }
                else {
                    linearLayout.setVisibility(View.GONE);
                    checkBox.setText(R.string.text_off);
                }
            }
        });

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new SweetAlertDialog(getContext(), SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("Jeste li sigurni?")
                        .setContentText("Želite li ažurirati podatke?")
                        .setConfirmText("Ažuriraj!")
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

    private void getAdminData(int id){
        Call<List<UserModel>> call = iAdmin.getAdminData(id);
        call.enqueue(new Callback<List<UserModel>>() {
            @Override
            public void onResponse(Call<List<UserModel>> call, Response<List<UserModel>> response) {
                if(response.isSuccessful()){
                    List<UserModel> list = response.body();
                    for(int i=0; i<list.size(); i++){
                        userModel.setIme(list.get(i).getIme());
                        userModel.setPrezime(list.get(i).getPrezime());
                        userModel.setEmail(list.get(i).getEmail());
                        userModel.setBroj(list.get(i).getBroj());
                    }
                    ime.setText(userModel.getIme());
                    prezime.setText(userModel.getPrezime());
                    broj.setText(String.valueOf(userModel.getBroj()));
                    email.setText(userModel.getEmail());
                    sifra.setText(userModel.getKljuc());
                }
            }
            @Override
            public void onFailure(Call<List<UserModel>> call, Throwable t) {
                t.getMessage();
                t.printStackTrace();
            }
        });
    }

    private void editableText(Boolean flag){
        if(flag.equals(true)){
            ime.setFocusableInTouchMode(true);
            ime.setSingleLine(false);
            prezime.setFocusableInTouchMode(true);;
            prezime.setSingleLine(false);
            broj.setFocusableInTouchMode(true);
            broj.setSingleLine(false);
            email.setFocusableInTouchMode(true);
            email.setSingleLine(false);
            ponoviteLozinku.setSingleLine(false);
            ponoviteLozinku.setFocusableInTouchMode(true);
            loznika.setFocusableInTouchMode(true);
            loznika.setSingleLine(false);
        }
        else if(flag.equals(false)){
            ime.setFocusable(false);
            prezime.setFocusable(false);
            broj.setFocusable(false);
            email.setFocusable(false);
            ponoviteLozinku.setFocusable(false);
            loznika.setFocusable(false);
        }
    }

    private void checkData(){
        String imeString = ime.getText().toString();
        String prezimeString = prezime.getText().toString();
        int brojInt = Integer.parseInt(broj.getText().toString());
        String novaLozinka = loznika.getText().toString();
        String potvrdaLozinke = ponoviteLozinku.getText().toString();
        int id = Integer.parseInt(userModel.getKljuc());
        boolean potvrda = false;
        String emailString = "";

        if(checkBoxEmail.isChecked()) {
            emailString = userModel.getEmail();
            potvrda = false;
        }
        else {
            emailString = email.getText().toString();
            potvrda = true;
        }

        boolean emailFlag = shareMethods.validateEmail(getContext(),email);

        if(checkBox.isChecked()){
            if(novaLozinka.equals(potvrdaLozinke) && emailFlag){
                updateData(1, potvrda, id,imeString,prezimeString,
                        brojInt,emailString,novaLozinka);
            }else{
                new SweetAlertDialog(getContext(),SweetAlertDialog.ERROR_TYPE)
                        .setTitleText("Lozinke nisu jednake!")
                        .show();
            }
        }else {
            if(!imeString.isEmpty() && !prezimeString.isEmpty() && emailFlag){
                try{ updateData(2, potvrda, id, imeString, prezimeString,
                            brojInt, emailString, novaLozinka);
                }catch (Exception e){ e.printStackTrace(); };
            }
        }
    }

    // Azuriranje administratorskih podataka
    private void updateData(int oznaka, boolean potvrda, int id, String ime,String prezime,
                                 int broj, String email, String lozinka){
        Call<ResponseBody> call = null;
        if(oznaka == 1) call = iAdmin.updateAdminAllData(potvrda, id,ime,prezime,broj,email,lozinka);
        else if (oznaka == 2) call = iAdmin.updateAdminData(potvrda, id, ime, prezime, broj, email);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.isSuccessful()){
                    new SweetAlertDialog(getContext(),SweetAlertDialog.SUCCESS_TYPE)
                            .setTitleText("Uspješno promijenjeno!")
                            .show();
                }
                else if(response.code() == 400){
                    Toast.makeText(getContext(),"Ažuriranje nije uspjelo!",Toast.LENGTH_SHORT).show();
                    new SweetAlertDialog(getContext(),SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("Provjerite e-mail adresu!")
                            .setConfirmText("U redu")
                            .show();
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
