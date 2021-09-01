package android.unipu.theater.fragmenti.user;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import android.unipu.theater.R;
import android.unipu.theater.adapter.TicketsAdapter;
import android.unipu.theater.model.OfferModel;
import android.unipu.theater.retrofit.IOffer;
import android.unipu.theater.retrofit.RetrofitClient;
import android.unipu.theater.user.UserReservation;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.android.material.chip.Chip;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class PerformanceTickets extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static TicketsAdapter adapter;
    private ArrayList<OfferModel> arrayOffer;
    private ListView listTickets;
    private OfferModel offerModel;
    private IOffer iOffer;
    private Chip sveChip,danasChip,sutraChip,mjesecChip;

    private String mParam1;
    private String mParam2;

    public PerformanceTickets() {
        // Required empty public constructor
    }

    public static PerformanceTickets newInstance(String param1, String param2) {
        PerformanceTickets fragment = new PerformanceTickets();
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
        View view = inflater.inflate(R.layout.fragment_performance_tickets, container, false);
        initView(view);
        return view;
    }

    private void initView(View view){
        offerModel = new OfferModel();
        offerModel.setId(getArgumentId());
        arrayOffer = new ArrayList<>();
        listTickets = (ListView) view.findViewById(R.id.listTickets);
        sveChip = (Chip) view.findViewById(R.id.chipSve);
        danasChip = (Chip) view.findViewById(R.id.chipDanas);
        sutraChip = (Chip) view.findViewById(R.id.chipSutra);
        mjesecChip = (Chip) view.findViewById(R.id.chipMjesec);
        setElements();
    }

    private int getArgumentId(){
        try{ int id = getArguments().getInt("id",0);
            setDataTickets(id,1);
            return id;
        }catch(Exception e){ e.printStackTrace(); }
        return 0;
    }

    private void setElements(){
        sveChip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(sveChip.isChecked()) {
                    arrayOffer.clear();
                    setDataTickets(offerModel.getId(), 1);
                }
            }
        });

        danasChip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(danasChip.isChecked()) {
                    arrayOffer.clear();
                    setDataTickets(offerModel.getId(),2);
                }
            }
        });

        sutraChip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(sutraChip.isChecked()){
                    arrayOffer.clear();
                    setDataTickets(offerModel.getId(),3);
                }
            }
        });

        mjesecChip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mjesecChip.isChecked()){
                    arrayOffer.clear();
                    setDataTickets(offerModel.getId(),4);
                }
            }
        });
    }

    private boolean checkDate(Date database, int broj){
        Date currentDate = getCurrentDate();
        switch(broj){
            case 1: {
                return true;
            }
            case 2:{
                if(currentDate.equals(database)) return true;
                else return false;
            }
            case 3:{
                Date newDate = addOneDay(currentDate);
                if(database.equals(newDate)) return true;
                else return false;
            }
            case 4: {
                if(checkMonth(database)) return true;
                else return false;
            }
        }
        return false;
    }

    private Date getCurrentDate(){
        Date date = null;
        try{ DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
            Calendar calendar = Calendar.getInstance();
            String strDate  = dateFormat.format(calendar.getTime());
            date = new SimpleDateFormat("dd.MM.yyyy").parse(strDate);
            return date;
        }catch(Exception e){ e.printStackTrace(); }
        return date;
    }

    private Date convertStringToDate(String database){
        Date date = null;
        try{ Date newDate =
                    new SimpleDateFormat("dd.MM.yyyy").parse(database);
            return newDate;
        }catch(Exception e){ e.printStackTrace(); }
        return date;
    }

    private Date addOneDay(Date date){
        Date newDate = null;
        try { DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.DATE, 1);
            String strDate  = dateFormat.format(calendar.getTime());
            newDate = new SimpleDateFormat("dd.MM.yyyy").parse(strDate);
            return newDate;
        }catch(Exception e){ e.printStackTrace(); }
        return newDate;
    }

    private boolean checkMonth(Date date){
        boolean flag = false;
        Calendar calendar1 = Calendar.getInstance();
        Calendar calendar2 = Calendar.getInstance();

        calendar1.setTime(date);
        calendar2.setTime(new Date());

        if(calendar1.get(Calendar.YEAR) == calendar2.get(Calendar.YEAR)){
            if(calendar1.get(Calendar.MONTH) == calendar2.get(Calendar.MONTH)){
                flag = true;
            }
        }
        return flag;
    }

    private void setDataTickets(int id, final int broj){
        Retrofit retrofit = RetrofitClient.getInstance2();
        iOffer = retrofit.create(IOffer.class);

        Call<List<OfferModel>> call = iOffer.getUlaznice(id);
        call.enqueue(new Callback<List<OfferModel>>() {
            @Override
            public void onResponse(Call<List<OfferModel>> call, Response<List<OfferModel>> response) {
                if(response.isSuccessful()){
                    List<OfferModel> list = response.body();
                    for(int i=0; i<list.size(); i++){
                        listTickets.setVisibility(View.VISIBLE);

                        if(checkDate(convertStringToDate(list.get(i).getDatum_prikazivanja()),broj)) {
                            offerModel.setNaziv(list.get(i).getNaziv());
                            offerModel.setDatum_prikazivanja(list.get(i).getDatum_prikazivanja());
                            offerModel.setVrijeme_prikazivanja(list.get(i).getVrijeme_prikazivanja());
                            arrayOffer.add(new OfferModel(
                                    offerModel.getDatum_prikazivanja(),
                                    offerModel.getVrijeme_prikazivanja()));
                        }
                    }
                    final String naziv = offerModel.getNaziv();
                    if(arrayOffer.isEmpty()){
                        listTickets.setVisibility(View.GONE);
                    }
                    adapter = new TicketsAdapter(arrayOffer,getContext());
                    listTickets.setAdapter(adapter);
                    listTickets.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            final ArrayList<String> sendList = new ArrayList<>();
                            OfferModel offerModel = arrayOffer.get(position);
                            sendList.add(naziv);
                            sendList.add(offerModel.getDatum_prikazivanja());
                            sendList.add(offerModel.getVrijeme_prikazivanja());
                            Intent intent = new Intent(getContext(),UserReservation.class);
                            intent.putStringArrayListExtra("ulaznice",sendList);
                            startActivity(intent);
                        }
                    });
                }
            }

            @Override
            public void onFailure(Call<List<OfferModel>> call, Throwable t) {
                t.getMessage();
                t.printStackTrace();
            }
        });
    }
}
