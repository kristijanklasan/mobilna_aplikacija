package android.unipu.theater.fragmenti.user;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.unipu.theater.R;
import android.unipu.theater.model.OfferModel;
import android.unipu.theater.model.ReservationModel;
import android.unipu.theater.model.UserModel;
import android.unipu.theater.retrofit.IReservation;
import android.unipu.theater.retrofit.RetrofitClient;
import android.unipu.theater.user.ReservationOverview;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import cn.pedant.SweetAlert.SweetAlertDialog;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class UserReservationDetail extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private Toolbar mToolbar;
    private Button delete;
    private LinearLayout bookedTicketLayout;
    private IReservation iReservation;
    private UserModel userModel;
    private OfferModel offerModel;
    private ReservationModel reservationModel;
    private TextView naslov,kategorija, datum, vrijeme, kolicina, ukupno, potvrda,kupac;
    private String mParam1;
    private String mParam2;

    public UserReservationDetail() {
        // Required empty public constructor
    }

    public static UserReservationDetail newInstance(String param1, String param2) {
        UserReservationDetail fragment = new UserReservationDetail();
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
        View view = inflater.inflate(R.layout.fragment_user_reservation_detail, container, false);
        initView(view);
        setElements();
        getIntentId();
        return view;
    }

    private void initView(View view){
        userModel = new UserModel();
        offerModel = new OfferModel();
        reservationModel = new ReservationModel();
        Retrofit retrofit = RetrofitClient.getInstance2();
        iReservation = retrofit.create(IReservation.class);
        bookedTicketLayout = (LinearLayout) view.findViewById(R.id.bookedTicketLayout);
        mToolbar = (Toolbar) view.findViewById(R.id.toolbarUserTicketDetail);
        naslov = (TextView) view.findViewById(R.id.nameTicket);
        kategorija = (TextView) view.findViewById(R.id.kategorijaTicket);
        datum = (TextView) view.findViewById(R.id.datumTicket);
        vrijeme = (TextView) view.findViewById(R.id.vrijemeTicket);
        kolicina = (TextView) view.findViewById(R.id.kolicinaTicket);
        ukupno = (TextView) view.findViewById(R.id.ukupnoTicket);
        potvrda = (TextView) view.findViewById(R.id.potvrdaTicket);
        kupac = (TextView) view.findViewById(R.id.kupacTicket);
        delete = (Button) view.findViewById(R.id.deleteTicket);
    }

    private void setElements(){
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ReservationOverview.class);
                startActivity(intent);
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SweetAlertDialog dialog = new SweetAlertDialog(getContext(),SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("Izbriši rezervaciju?")
                        .setContentText("Jednom izbrisanu rezervaciju nije moguće vratiti!")
                        .setCustomImage(R.drawable.ticket)
                        .setConfirmText("Izbriši")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                deleteReservation(reservationModel.getId(),reservationModel.getKolicina());
                                sweetAlertDialog.dismissWithAnimation();
                            }
                        })
                        .setCancelButton("Odustani",null);
                dialog.show();
            }
        });
    }

    private void getIntentId(){
        try{ Bundle bundle = getArguments();
            if(bundle!=null){
                int id = bundle.getInt("id");
                int id_korisnik = Integer.parseInt(userModel.getKljuc());
                reservationModel.setId(id);
                setDetail(id,id_korisnik);
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    private void setDetail(int id_rezervacije, int id_korisnik){
        final ArrayList<Integer> sjedaloLista = new ArrayList<>();
        final ArrayList<Integer> redLista = new ArrayList<>();
        Call<List<ReservationModel>> call = iReservation.getReservationDetail(id_korisnik,id_rezervacije);
        call.enqueue(new Callback<List<ReservationModel>>() {
            @Override
            public void onResponse(Call<List<ReservationModel>> call, Response<List<ReservationModel>> response) {
                if(response.isSuccessful()){
                    List<ReservationModel> list = response.body();
                    for(int i=0; i<list.size(); i++){
                        reservationModel.setNaziv(list.get(i).getNaziv());
                        reservationModel.setKategorija(list.get(i).getKategorija());
                        reservationModel.setDatum_prikazivanja(list.get(i).getDatum_prikazivanja());
                        reservationModel.setVrijeme_prikazivanja(list.get(i).getVrijeme_prikazivanja());
                        reservationModel.setIme(list.get(i).getIme());
                        reservationModel.setPrezime(list.get(i).getPrezime());
                        reservationModel.setKolicina(list.get(i).getKolicina());
                        reservationModel.setUkupno(list.get(i).getUkupno());
                        reservationModel.setPotvrda(list.get(i).getPotvrda());
                        sjedaloLista.add(list.get(i).getSjedalo());
                        redLista.add(list.get(i).getRed());
                    }
                    reservationModel.setSjedaloLista(sjedaloLista);
                    reservationModel.setRedLista(redLista);
                    setDataText();
                }
            }

            @Override
            public void onFailure(Call<List<ReservationModel>> call, Throwable t) {
                t.getMessage();
                t.printStackTrace();
            }
        });
    }

    private void deleteReservation(int id, int kolicina){
        Call<ResponseBody> call = iReservation.deleteReservation(id, kolicina);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                SweetAlertDialog dialog = new SweetAlertDialog(getContext(),SweetAlertDialog.SUCCESS_TYPE)
                        .setTitleText("Izbrisano!")
                        .setContentText("Rezervacija je izbrisana!")
                        .setConfirmText("U redu")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                ReservationList reservationList = new ReservationList();
                                FragmentManager fm = getFragmentManager();
                                fm.beginTransaction()
                                        .replace(R.id.contentReservation,reservationList)
                                        .commit();
                                sweetAlertDialog.dismissWithAnimation();
                            }
                        });
                dialog.show();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                t.getMessage();
                t.printStackTrace();
            }
        });
    }

    private void setDataText(){
        naslov.setText(reservationModel.getNaziv());
        kategorija.setText(reservationModel.getKategorija());
        datum.setText(reservationModel.getDatum_prikazivanja());
        String vrijemeString = reservationModel.getVrijeme_prikazivanja()+"h";
        vrijeme.setText(vrijemeString);
        kolicina.setText(String.valueOf(reservationModel.getKolicina()));
        ukupno.setText(String.format(Locale.ENGLISH,"%.2f %s",reservationModel.getUkupno()," kn"));
        potvrda.setText(reservationModel.getPotvrda());
        String kupacString = reservationModel.getIme()+" "+reservationModel.getPrezime();
        kupac.setText(kupacString);

        int sjedalo = reservationModel.getSjedaloLista().size();
        int red = reservationModel.getRedLista().size();

        TextView[] textViews = new TextView[sjedalo];
        CardView[] cardViews = new CardView[red];
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT
        );
        params.setMargins(0, 0, 0, 20);

        for(int i=0; i<red; i++){
            cardViews[i] = new CardView(getContext());
            cardViews[i].setId(i+1);
            cardViews[i].setCardBackgroundColor(Color.WHITE);
            cardViews[i].setMaxCardElevation(30);
            cardViews[i].setCardElevation(20);
            cardViews[i].setRadius(15);
            cardViews[i].setPadding(0,25,0,25);
            cardViews[i].setLayoutParams(params);

            textViews[i] = new TextView(getContext());
            textViews[i].setId(i+1);
            String seat = "Red: "+reservationModel.getRedLista().get(i)+" | "+"Sjedalo: "
                    +reservationModel.getSjedaloLista().get(i);

            textViews[i].setText(seat);
            textViews[i].setTextColor(Color.GRAY);
            textViews[i].setGravity(Gravity.CENTER);
            textViews[i].setTextSize(16);
            textViews[i].setPadding(5,0,5,20);
            textViews[i].setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_airline_seat_recline_extra_black_24dp,0,0,0);
            cardViews[i].addView(textViews[i]);
            bookedTicketLayout.addView(cardViews[i],i);
        }
    }
}
