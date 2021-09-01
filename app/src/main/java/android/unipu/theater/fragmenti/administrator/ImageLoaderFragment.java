package android.unipu.theater.fragmenti.administrator;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.unipu.theater.R;
import android.unipu.theater.retrofit.ITheater;
import android.unipu.theater.retrofit.RetrofitClient;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import android.widget.EditText;
import android.widget.ImageButton;

import cn.pedant.SweetAlert.SweetAlertDialog;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ImageLoaderFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private ITheater iTheater;
    private ImageButton insert;
    private String mParam1;
    private String mParam2;
    private EditText urlEdit,nazivEdit;

    public ImageLoaderFragment() {
        // Required empty public constructor
    }

    public static ImageLoaderFragment newInstance(String param1, String param2) {
        ImageLoaderFragment fragment = new ImageLoaderFragment();
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
        View view = inflater.
                inflate(R.layout.fragment_image_loader, container, false);
        initView(view);
        return view;
    }

    private void initView(View view){
        urlEdit = (EditText) view.findViewById(R.id.urlEdit);
        nazivEdit = (EditText) view.findViewById(R.id.nazivEdit);
        insert = (ImageButton) view.findViewById(R.id.insertImageButton);
        insert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkEdit();
            }
        });
    }

    private void checkEdit(){
        final String naziv = nazivEdit.getText().toString();
        final String slika = urlEdit.getText().toString();
        if(!naziv.isEmpty() && !slika.isEmpty()){
            new SweetAlertDialog(getContext(), SweetAlertDialog.WARNING_TYPE)
                    .setTitleText("Želite spremiti sliku?")
                    .setConfirmText("Spremi!")
                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sDialog) {
                            insertImage(naziv,slika);
                            sDialog.dismissWithAnimation();
                        }
                    })
                    .setCancelButton("Odustani",null)
                    .show();
        }
    }

    private void insertImage(String naziv, String slika){
        Retrofit retrofit = RetrofitClient.getInstance2();
        iTheater = retrofit.create(ITheater.class);

        Call<ResponseBody> call = iTheater.insertImage(naziv,slika);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.isSuccessful()){
                  new SweetAlertDialog(getContext(),SweetAlertDialog.SUCCESS_TYPE)
                          .setContentText("Uspješno spremljeno!")
                          .setConfirmButton("U redu",null)
                          .show();
                  nazivEdit.getText().clear();
                  urlEdit.getText().clear();
                }else{
                    new SweetAlertDialog(getContext(),SweetAlertDialog.ERROR_TYPE)
                            .setContentText("Nije spremljeno!")
                            .setConfirmButton("U redu",null)
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
