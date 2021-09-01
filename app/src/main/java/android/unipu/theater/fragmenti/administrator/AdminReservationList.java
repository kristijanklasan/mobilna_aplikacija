package android.unipu.theater.fragmenti.administrator;

import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.unipu.theater.R;
import android.unipu.theater.adapter.SwipeAdapter;
import android.unipu.theater.model.OfferModel;
import android.unipu.theater.model.ReservationModel;
import android.unipu.theater.retrofit.IReservation;
import android.unipu.theater.retrofit.RetrofitClient;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class AdminReservationList extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;
    private RadioGroup radioGroup;
    private ReservationModel reservationModel;
    private RecyclerView recyclerReservation;
    private SwipeAdapter adapter;
    private ArrayList<ReservationModel> arrayReservation;
    private IReservation iReservation;
    private TextView kolicina;
    private ImageView imageVisible;
    private CardView cardPopup;
    private OfferModel offerModel;

    public AdminReservationList() {
        // Required empty public constructor
    }

    public static AdminReservationList newInstance(String param1, String param2) {
        AdminReservationList fragment = new AdminReservationList();
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
                inflate(R.layout.fragment_admin_reservation_list, container, false);

        initView(view);
        getSentId();
        setElement();
        return view;
    }

    private void initView(View view){
        reservationModel = new ReservationModel();
        offerModel = new OfferModel();
        arrayReservation = new ArrayList<>();
        recyclerReservation = (RecyclerView) view.findViewById(R.id.recyclerTicketList);
        recyclerReservation.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerReservation.addItemDecoration(new DividerItemDecoration(getContext(),
                LinearLayoutManager.VERTICAL));
        kolicina = (TextView) view.findViewById(R.id.kolicinaAdminReservation);
        radioGroup = (RadioGroup) view.findViewById(R.id.radioGroupAdminTickets);
        imageVisible = (ImageView) view.findViewById(R.id.adminReservationImage);
        cardPopup = (CardView) view.findViewById(R.id.adminCardPopup);
    }

    private void setElement(){
        imageVisible.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(cardPopup.getVisibility() == View.VISIBLE){
                    imageVisible.setImageDrawable(getResources().getDrawable(R.drawable.ic_keyboard_arrow_up_black_24dp));
                    cardPopup.setVisibility(View.GONE);
                }else {
                    imageVisible.setImageDrawable(getResources().getDrawable(R.drawable.ic_keyboard_arrow_down_black_24dp));
                    cardPopup.setVisibility(View.VISIBLE);
                }
            }
        });

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                View radio = radioGroup.findViewById(checkedId);
                int index = radioGroup.indexOfChild(radio);
                String tekst = null;
                if(index==1) tekst = "da";
                else tekst = "ne";
                filterConfirmation(offerModel.getId(),index, tekst);
            }
        });
    }

    private void getSentId(){
        try{ Bundle bundle = getArguments();
            if(bundle!=null){
                int id = bundle.getInt("id");
                offerModel.setId(id);
                getAllTickets(id);
            }
        }catch(Exception e){ e.printStackTrace(); }
    }

    public String convertDateToString(String date){
        String dateString = null;
        try { TimeZone zone = TimeZone.getTimeZone("UTC");
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
            SimpleDateFormat newFormat = new SimpleDateFormat("dd.MM.yyyy");
            simpleDateFormat.setTimeZone(zone);
            Date newDate = simpleDateFormat.parse(date);
            dateString = newFormat.format(newDate);
            return dateString;
        }catch(Exception e){ e.printStackTrace(); }
        return dateString;
    }

    private void getAllTickets(int id){
        if(adapter != null)
            clearAdapter();

        Retrofit retrofit = RetrofitClient.getInstance2();
        iReservation = retrofit.create(IReservation.class);

        Call<List<ReservationModel>> call = iReservation.getReservationData(id);
        call.enqueue(new Callback<List<ReservationModel>>() {
            @Override
            public void onResponse(Call<List<ReservationModel>> call, Response<List<ReservationModel>> response) {
                if(response.isSuccessful()){
                    List<ReservationModel> list = response.body();

                    for(int i=0; i<list.size(); i++){
                        reservationModel.setId(list.get(i).getId());
                        reservationModel.setKolicina(list.get(i).getKolicina());
                        reservationModel.setUkupno(list.get(i).getUkupno());
                        reservationModel.setIme(list.get(i).getIme());
                        reservationModel.setPrezime(list.get(i).getPrezime());
                        reservationModel.setPotvrda(list.get(i).getPotvrda());
                        String datum = convertDateToString(list.get(i).getDatum_dodavanja());
                        reservationModel.setDatum_dodavanja(datum);
                        arrayReservation.add(new ReservationModel(reservationModel.getIme(),
                                reservationModel.getPrezime(),reservationModel.getKolicina(),
                                reservationModel.getUkupno(),reservationModel.getDatum_dodavanja(),
                                reservationModel.getPotvrda(),reservationModel.getId(),
                                offerModel.getId()));
                    }

                    adapter = new SwipeAdapter(getContext(),arrayReservation);
                    recyclerReservation.setAdapter(adapter);
                    kolicina.setText(String.valueOf(list.size()));
                }
            }

            @Override
            public void onFailure(Call<List<ReservationModel>> call, Throwable t) {
                t.getMessage();
                t.printStackTrace();
            }
        });
    }

    private void filterConfirmation(int id, int index, String potvrda){
        Retrofit retrofit = RetrofitClient.getInstance2();
        iReservation = retrofit.create(IReservation.class);

        if(adapter != null)
            clearAdapter();
        if(index == 0) getAllTickets(id);
        else {
            Call<List<ReservationModel>> call = iReservation.getConfirmationData(id, potvrda);
            call.enqueue(new Callback<List<ReservationModel>>() {
                @Override
                public void onResponse(Call<List<ReservationModel>> call, Response<List<ReservationModel>> response) {
                    if (response.isSuccessful()) {
                        List<ReservationModel> list = response.body();
                        for (int i = 0; i < list.size(); i++) {
                            reservationModel.setId(list.get(i).getId());
                            reservationModel.setKolicina(list.get(i).getKolicina());
                            reservationModel.setUkupno(list.get(i).getUkupno());
                            reservationModel.setIme(list.get(i).getIme());
                            reservationModel.setPrezime(list.get(i).getPrezime());
                            reservationModel.setPotvrda(list.get(i).getPotvrda());
                            String datum = convertDateToString(list.get(i).getDatum_dodavanja());
                            reservationModel.setDatum_dodavanja(datum);
                            arrayReservation.add(new ReservationModel(reservationModel.getIme(),
                                    reservationModel.getPrezime(), reservationModel.getKolicina(),
                                    reservationModel.getUkupno(), reservationModel.getDatum_dodavanja(),
                                    reservationModel.getPotvrda(), reservationModel.getId(),
                                    offerModel.getId()));
                        }
                        adapter = new SwipeAdapter(getContext(), arrayReservation);
                        recyclerReservation.setAdapter(adapter);
                        kolicina.setText(String.valueOf(list.size()));
                    }
                }

                @Override
                public void onFailure(Call<List<ReservationModel>> call, Throwable t) {
                    t.getMessage();
                    t.printStackTrace();
                }
            });
        }
    }

    private void clearAdapter(){
        arrayReservation.clear();
        adapter.notifyDataSetChanged();
    }
}
