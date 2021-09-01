package android.unipu.theater.fragmenti.administrator;

import android.graphics.Color;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.unipu.theater.R;
import android.unipu.theater.model.ReservationModel;
import android.unipu.theater.retrofit.IReservation;
import android.unipu.theater.retrofit.RetrofitClient;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Switch;
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

public class TicketDetail extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private TextView kupac, datumPrikaza, vrijeme, potvrda, sifra, datumDodavanja, kolicina, ukupno, status;
    private Switch switchPotvrda;
    private Button deleteButton;
    private IReservation iReservation;
    private ReservationModel reservationModel;
    private AdminReservationList adminReservationList;
    private LinearLayout adminTicketLayout;
    private String mParam1;
    private String mParam2;

    public TicketDetail() {
        // Required empty public constructor
    }

    public static TicketDetail newInstance(String param1, String param2) {
        TicketDetail fragment = new TicketDetail();
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
        View view = inflater.inflate(R.layout.fragment_ticket_detail, container, false);

        initView(view);
        getSentId();
        return view;
    }

    private void getSentId(){
        try{ Bundle bundle = getArguments();
            if(bundle!=null){
                int id = bundle.getInt("id");
                int id_ponuda = bundle.getInt("id_ponuda");
                reservationModel.setId_ponuda(id_ponuda);
                reservationModel.setId(id);
                getTicketDetail(id);
            }
        }catch(Exception e){ e.printStackTrace(); }

    }

    private void initView(View view){
        Retrofit retrofit = RetrofitClient.getInstance2();
        iReservation = retrofit.create(IReservation.class);
        reservationModel = new ReservationModel();
        adminReservationList = new AdminReservationList();
        kupac = (TextView) view.findViewById(R.id.kupacTicketDetail);
        datumPrikaza = (TextView) view.findViewById(R.id.prikazTicketDetail);
        vrijeme = (TextView) view.findViewById(R.id.vrijemeTicketDetail);
        datumDodavanja = (TextView) view.findViewById(R.id.datumTicketDetail);
        kolicina = (TextView) view.findViewById(R.id.brojTicketDetail);
        ukupno = (TextView) view.findViewById(R.id.ukupnoTicketDetail);
        potvrda = (TextView) view.findViewById(R.id.potvrdaTicketDetail);
        sifra = (TextView) view.findViewById(R.id.sifraTicketDetail);
        status = (TextView) view.findViewById(R.id.statusTicketDetail);
        switchPotvrda = (Switch) view.findViewById(R.id.switchTicketDetail);
        adminTicketLayout = (LinearLayout) view.findViewById(R.id.adminTicketLayout);
        deleteButton = (Button) view.findViewById(R.id.adminDeleteTicket);
    }

    private void getTicketDetail(int id){
        final ArrayList<Integer> red = new ArrayList<>();
        final ArrayList<Integer> sjedalo = new ArrayList<>();

        Call<List<ReservationModel>> call = iReservation.getTicketDetail(id);
        call.enqueue(new Callback<List<ReservationModel>>() {
            @Override
            public void onResponse(Call<List<ReservationModel>> call, Response<List<ReservationModel>> response) {
                if(response.isSuccessful()){
                    List<ReservationModel> list = response.body();

                    for(int i=0; i<list.size(); i++){
                        reservationModel.setDatum_dodavanja(list.get(i).getDatum_dodavanja());
                        reservationModel.setDatum_prikazivanja(list.get(i).getDatum_prikazivanja());
                        reservationModel.setVrijeme_prikazivanja(list.get(i).getVrijeme_prikazivanja());
                        reservationModel.setKolicina(list.get(i).getKolicina());
                        reservationModel.setUkupno(list.get(i).getUkupno());
                        reservationModel.setPotvrda(list.get(i).getPotvrda());
                        reservationModel.setIme(list.get(i).getIme());
                        reservationModel.setPrezime(list.get(i).getPrezime());
                        reservationModel.setRed(list.get(i).getRed());
                        reservationModel.setSjedalo(list.get(i).getSjedalo());
                        red.add(reservationModel.getRed());
                        sjedalo.add(reservationModel.getSjedalo());
                    }
                    reservationModel.setRedLista(red);
                    reservationModel.setSjedaloLista(sjedalo);
                    setText();
                }
            }

            @Override
            public void onFailure(Call<List<ReservationModel>> call, Throwable t) {
                t.getMessage();
                t.printStackTrace();
            }
        });
    }

    private void setText(){
        checkStatus();
        String korisnikString = reservationModel.getIme()+" "+reservationModel.getPrezime();
        String vrijemeString = reservationModel.getVrijeme_prikazivanja()+"h";
        String convertDate = adminReservationList.convertDateToString(reservationModel.getDatum_dodavanja());

        kupac.setText(korisnikString);
        datumPrikaza.setText(reservationModel.getDatum_prikazivanja());
        vrijeme.setText(vrijemeString);
        datumDodavanja.setText(convertDate);
        kolicina.setText(String.valueOf(reservationModel.getKolicina()));
        ukupno.setText(String.format(Locale.ENGLISH,"%.2f %s",reservationModel.getUkupno(),"kn"));
        potvrda.setText(reservationModel.getPotvrda());
        sifra.setText(String.valueOf(reservationModel.getId()));

        TextView[] textViews = new TextView[reservationModel.getRedLista().size()];
        CardView[] cardViews = new CardView[reservationModel.getSjedaloLista().size()];
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT
        );
        params.setMargins(0, 0, 0, 20);

        switchPotvrda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(switchPotvrda.isChecked()){
                    updateConfirmation(reservationModel.getId(),"da");
                    status.setText("Plaćeno");
                    switchPotvrda.setText("Da");
                }
                else{
                    updateConfirmation(reservationModel.getId(),"ne");
                    status.setText("Nije plaćeno");
                    switchPotvrda.setText("Ne");
                }
            }
        });

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteTicket(reservationModel.getId(),reservationModel.getKolicina());
            }
        });

        for(int i=0; i<reservationModel.getSjedaloLista().size(); i++){
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
            String podaci = "Red: "+reservationModel.getRedLista()
                    .get(i)+" | "+"Sjedalo: "+reservationModel.getSjedaloLista().get(i);
            textViews[i].setText(podaci);
            textViews[i].setTextColor(Color.GRAY);
            textViews[i].setGravity(Gravity.CENTER);
            textViews[i].setTextSize(14);
            textViews[i].setPadding(2,0,2,20);
            textViews[i].setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_airline_seat_recline_extra_black_24dp,0,0,0);
            cardViews[i].addView(textViews[i]);
            adminTicketLayout.addView(cardViews[i],i);
        }
    }


    private void updateConfirmation(int id, String status){
        Call<ResponseBody> call = iReservation.updateConfirmation(id,status);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {}

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                t.getMessage();
                t.printStackTrace();
            }
        });
    }

    private void deleteTicket(int id, int kolicina){
        Call<ResponseBody> call = iReservation.deleteReservation(id, kolicina);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.isSuccessful()){
                    SweetAlertDialog dialog = new SweetAlertDialog(getContext(),SweetAlertDialog.SUCCESS_TYPE)
                            .setTitleText("Karta je izbrisana!")
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                    sweetAlertDialog.dismissWithAnimation();
                                }
                            });
                    dialog.show();
                    AdminReservationList adminReservationList = new AdminReservationList();
                    FragmentManager fm = getFragmentManager();
                    Bundle bundle = new Bundle();
                    bundle.putInt("id",reservationModel.getId_ponuda());
                    adminReservationList.setArguments(bundle);
                    fm.beginTransaction()
                            .setCustomAnimations(R.anim.slide_out_right,R.anim.slide_out_left)
                            .replace(R.id.fragmentReservation,adminReservationList).commit();
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                t.getMessage();
                t.printStackTrace();
            }
        });
    }

    private void checkStatus(){
        String checkedConfirmation = "da";
        if(reservationModel.getPotvrda().equals(checkedConfirmation)){
            switchPotvrda.setChecked(true);
            switchPotvrda.setText("Da");
            status.setText("Plaćeno");
        }else {
            switchPotvrda.setChecked(false);
            switchPotvrda.setText("Ne");
            status.setText("Nije plaćeno");
        }
    }
}
