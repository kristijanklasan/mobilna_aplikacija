package android.unipu.theater.fragmenti.user;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.unipu.theater.R;
import android.unipu.theater.adapter.FavoritesAdapter;
import android.unipu.theater.model.FavoritesModel;
import android.unipu.theater.model.UserModel;
import android.unipu.theater.retrofit.IFavorites;
import android.unipu.theater.retrofit.RetrofitClient;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class FavoritesOverview extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;
    private RecyclerView recyclerView;
    private IFavorites iFavorites;
    private UserModel userModel;
    private FavoritesModel favoritesModel;
    private FavoritesAdapter adapter;
    private List<FavoritesModel> favoritesModelArrayList;
    private ImageView datumImage;
    private TextView datumIzbor;
    private DatePickerDialog.OnDateSetListener mDatePicker;
    private LinearLayout linearIzbor, linearRezultat;
    private Switch switchIzbor;

    public FavoritesOverview() {
        // Required empty public constructor
    }

    public static FavoritesOverview newInstance(String param1, String param2) {
        FavoritesOverview fragment = new FavoritesOverview();
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
                inflate(R.layout.fragment_favorites_overview, container, false);

        initView(view);
        getFavorites();
        return view;
    }

    private void initView(View view){
        userModel = new UserModel();
        favoritesModel = new FavoritesModel();
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerMojIzbor);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        datumImage = (ImageView) view.findViewById(R.id.datumImageIzbor);
        datumIzbor = (TextView) view.findViewById(R.id.datumIzbor);
        switchIzbor = (Switch) view.findViewById(R.id.switchIzbor);
        linearIzbor = (LinearLayout) view.findViewById(R.id.linearIzbor);
        linearRezultat = (LinearLayout) view.findViewById(R.id.linearRezultat);
        Retrofit retrofitClient = RetrofitClient.getInstance2();
        iFavorites = retrofitClient.create(IFavorites.class);
        initWidget(view);
    }

    private void initWidget(View view){
        switchIzbor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(switchIzbor.isChecked()) {
                    switchIzbor.setText(R.string.text_on);
                    linearIzbor.setVisibility(v.VISIBLE);
                }
                else {
                    switchIzbor.setText(R.string.text_off);
                    linearIzbor.setVisibility(v.GONE);
                    linearRezultat.setVisibility(v.GONE);
                    getFavorites();
                }
            }
        });

        datumImage.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(getContext(), AlertDialog.THEME_HOLO_LIGHT, mDatePicker, year,month,day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        mDatePicker = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                Calendar calendar = Calendar.getInstance();
                calendar.set(year, month, dayOfMonth);
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                String datum = simpleDateFormat.format(calendar.getTime());
                datumIzbor.setText(datum);
                getDesiredFavorites(datum);
            }
        };
    }

    private void getDesiredFavorites(String date){
        favoritesModelArrayList = new ArrayList<>();
        linearRezultat.setVisibility(View.GONE);
        int id_korisnik = Integer.parseInt(userModel.getKljuc());

        Call<List<FavoritesModel>> call = iFavorites.getDesiredData(id_korisnik, date);
        call.enqueue(new Callback<List<FavoritesModel>>() {
            @Override
            public void onResponse(Call<List<FavoritesModel>> call, Response<List<FavoritesModel>> response) {
                if(response.isSuccessful()){
                    List<FavoritesModel> list = response.body();

                    if(list.size() == 0)
                        linearRezultat.setVisibility(View.VISIBLE);

                    for(int i=0; i<list.size(); i++){
                        favoritesModel = new FavoritesModel();
                        favoritesModel.setNaziv(list.get(i).getNaziv());
                        favoritesModel.setDatum(list.get(i).getDatum());
                        favoritesModel.setId_predstava(list.get(i).getId_predstava());
                        favoritesModel.setSlika1(list.get(i).getSlika1());
                        favoritesModel.setId_predstava(list.get(i).getId_predstava());
                        favoritesModel.setKategorija(list.get(i).getKategorija());

                        favoritesModelArrayList.add(favoritesModel);
                    }
                    try{
                        adapter = new FavoritesAdapter(getContext(), favoritesModelArrayList);
                        recyclerView.setAdapter(adapter);
                        recyclerView.scheduleLayoutAnimation();
                    }catch(Exception e){ e.printStackTrace(); }
                }
            }

            @Override
            public void onFailure(Call<List<FavoritesModel>> call, Throwable t) {
                t.getMessage();
                t.printStackTrace();
            }
        });

    }
    private void getFavorites(){

        linearRezultat.setVisibility(View.GONE);
        favoritesModelArrayList = new ArrayList<>();

        int id_korisnik = Integer.parseInt(userModel.getKljuc());
        Call<List<FavoritesModel>> call = iFavorites.getData(id_korisnik);
        call.enqueue(new Callback<List<FavoritesModel>>() {
            @Override
            public void onResponse(Call<List<FavoritesModel>> call, Response<List<FavoritesModel>> response) {
                if(response.isSuccessful()){
                    List<FavoritesModel> list = response.body();

                    if(list.size() == 0)
                        linearRezultat.setVisibility(View.VISIBLE);

                    if(list != null){
                        for(int i=0; i<list.size(); i++){
                            favoritesModel = new FavoritesModel();
                            favoritesModel.setNaziv(list.get(i).getNaziv());
                            favoritesModel.setDatum(list.get(i).getDatum());
                            favoritesModel.setId_predstava(list.get(i).getId_predstava());
                            favoritesModel.setSlika1(list.get(i).getSlika1());
                            favoritesModel.setId_predstava(list.get(i).getId_predstava());
                            favoritesModel.setKategorija(list.get(i).getKategorija());
                            favoritesModelArrayList.add(favoritesModel);
                        }
                        try{
                            adapter = new FavoritesAdapter(getContext(), favoritesModelArrayList);
                            recyclerView.setAdapter(adapter);
                            recyclerView.scheduleLayoutAnimation();
                        }catch(Exception e){ e.printStackTrace(); }
                    }
                }
            }

            @Override
            public void onFailure(Call<List<FavoritesModel>> call, Throwable t) {
                t.getMessage();
                t.printStackTrace();
            }
        });

    }
}
