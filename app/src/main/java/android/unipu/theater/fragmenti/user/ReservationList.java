package android.unipu.theater.fragmenti.user;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.unipu.theater.R;
import android.unipu.theater.adapter.ReservationAdapter;
import android.unipu.theater.model.OfferModel;
import android.unipu.theater.model.ReservationModel;
import android.unipu.theater.model.UserModel;
import android.unipu.theater.retrofit.IReservation;
import android.unipu.theater.retrofit.RetrofitClient;
import android.unipu.theater.user.UserReservation;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

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

public class ReservationList extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private LinearLayout linearPopup;
    private RadioButton allRadio, activeRadio, lastRadio;
    private ImageView popup;
    private RadioGroup radioGroup;
    private TextView kolicina;
    private ReservationAdapter adapter;
    private RecyclerView recyclerView;
    private List<OfferModel> arrayOffer;
    private IReservation iReservation;
    private UserModel userModel;
    private ReservationModel reservationModel;
    private OfferModel offerModel;
    private ImageView newReservation;
    private ArrayList<String> showList;
    private Spinner spinnerList;
    private String mParam1;
    private String mParam2;

    public ReservationList() {
        // Required empty public constructor
    }

    public static ReservationList newInstance(String param1, String param2) {
        ReservationList fragment = new ReservationList();
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
        View view = inflater.inflate(R.layout.fragment_reservation_list, container, false);
        initView(view);
        setElements();
        getShowNames();
        offerModel.setUvjet(0);
        return view;
    }

    private void initView(View view){
        arrayOffer = new ArrayList<>();
        userModel = new UserModel();
        reservationModel = new ReservationModel();
        offerModel = new OfferModel();
        showList = new ArrayList<String>();
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerReservation);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        kolicina = (TextView) view.findViewById(R.id.kolicinaReservation);
        newReservation = (ImageView) view.findViewById(R.id.addReservation);
        spinnerList = (Spinner) view.findViewById(R.id.spinnerReservationList);
        allRadio = (RadioButton) view.findViewById(R.id.allRadio);
        activeRadio = (RadioButton) view.findViewById(R.id.activeRadio);
        lastRadio = (RadioButton) view.findViewById(R.id.lastRadio);
        radioGroup = (RadioGroup) view.findViewById(R.id.radioGroupReservation);
        popup = (ImageView) view.findViewById(R.id.popupImage);
        linearPopup = (LinearLayout) view.findViewById(R.id.linearPopup);
    }

    private void setElements(){
        Retrofit retrofit = RetrofitClient.getInstance2();
        iReservation = retrofit.create(IReservation.class);
        newReservation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), UserReservation.class);
                startActivity(intent);
            }
        });

        popup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(linearPopup.getVisibility() == View.VISIBLE){
                    popup.setImageDrawable(getResources().getDrawable(R.drawable.ic_keyboard_arrow_up_black_24dp));
                    linearPopup.setVisibility(View.GONE);
                }else {
                    popup.setImageDrawable(getResources().getDrawable(R.drawable.ic_keyboard_arrow_down_black_24dp));
                    linearPopup.setVisibility(View.VISIBLE);
                }
            }
        });

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                View radio = radioGroup.findViewById(checkedId);
                int index = radioGroup.indexOfChild(radio);
                offerModel.setUvjet(index);
                filterReservation(offerModel.getNaziv(),offerModel.getPozicija(),index);
            }
        });
    }

    private String getCurrentDateToString(){
        DateFormat date = new SimpleDateFormat("dd.MM.yyyy");
        Calendar calendar = Calendar.getInstance();
        String dateText = date.format(calendar.getTime());
        return dateText;
    }

    private Date switchStringDateFormat(String date) {
        Date dateFormat = null;
        try { SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd.MM.yyyy");
            dateFormat = simpleDateFormat.parse(date);
            return dateFormat;
        }catch (Exception e){ e.printStackTrace(); }
        return dateFormat;
    }

    private boolean checkDate(int num, Date current, Date database){
        switch(num){
            case 0: {
                if(current.before(database) || current.equals(database) || current.after(database))
                    return true;
                else return false;
            }
            case 1: {
                if(current.before(database) || current.equals(database))
                    return true;
                else return false;
            }
            case 2: {
                if(current.after(database)) return true;
                else return false;
            }
        }
        return true;
    }

    private void setSpinner(ArrayList<String> list){
        String [] arrayNaziv = new String[list.size()];
        arrayNaziv = list.toArray(arrayNaziv);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(),
                android.R.layout.simple_spinner_item, arrayNaziv);

        adapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
        spinnerList.setAdapter(adapter);
        spinnerList.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ((TextView) parent.getChildAt(0)).setTextColor(Color.BLACK);
                if(parent.getItemAtPosition(position).equals("")){ }
                else{
                    String item = parent.getItemAtPosition(position).toString();
                    offerModel.setNaziv(item);
                    offerModel.setPozicija(position);
                    filterReservation(offerModel.getNaziv(),position,offerModel.getUvjet());
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });
    }

    private void getShowNames(){
        clearAdapterData();
        int id = Integer.parseInt(userModel.getKljuc());
        showList.add("Sve predstave");

        Call<List<OfferModel>> call = iReservation.getNames(id);
        call.enqueue(new Callback<List<OfferModel>>() {
            @Override
            public void onResponse(Call<List<OfferModel>> call, Response<List<OfferModel>> response) {
                if(response.isSuccessful()){
                    List<OfferModel> list = response.body();
                    for(int i=0; i<list.size(); i++){
                        showList.add(list.get(i).getNaziv());
                    }
                    offerModel.setNaziv(showList.get(0));
                    setSpinner(showList);
                }
            }
            @Override
            public void onFailure(Call<List<OfferModel>> call, Throwable t) {
                t.getMessage();
                t.printStackTrace();
            }
        });
    }

    private void filterReservation(String naziv, int pozicija, final int uvjet){
        clearAdapterData();
        String date = getCurrentDateToString();
        final Date dateCurrent = switchStringDateFormat(date);
        int id = Integer.parseInt(userModel.getKljuc());

        Call<List<OfferModel>> call = null;
        if(pozicija == 0) {
            call = iReservation.getAllReservations(id);
        }
        else {
            call = iReservation.getFilterData(id, uvjet, naziv);
        }
        
        call.enqueue(new Callback<List<OfferModel>>() {
            @Override
            public void onResponse(Call<List<OfferModel>> call, Response<List<OfferModel>> response) {
                if(response.isSuccessful()){
                    List<OfferModel> list = response.body();
                    int counter = 0;
                    for(int i=0; i<list.size(); i++){
                        Date dateDatabase = switchStringDateFormat(list.get(i).getDatum_prikazivanja());
                        if (checkDate(uvjet, dateCurrent, dateDatabase)) {
                            offerModel.setId(list.get(i).getId());
                            offerModel.setNaziv(list.get(i).getNaziv());
                            offerModel.setKategorija(list.get(i).getKategorija());
                            offerModel.setDatum_prikazivanja(list.get(i).getDatum_prikazivanja());
                            offerModel.setVrijeme_prikazivanja(list.get(i).getVrijeme_prikazivanja());
                            offerModel.setKolicina(list.get(i).getKolicina());
                            arrayOffer.add(new OfferModel(offerModel.getNaziv(), offerModel.getKategorija(),
                                    offerModel.getDatum_prikazivanja(), offerModel.getVrijeme_prikazivanja(),
                                    offerModel.getId(), offerModel.getKolicina()));
                            counter++;
                        }
                    }
                    kolicina.setText(String.valueOf(counter));
                    adapter = new ReservationAdapter(getContext(),arrayOffer);
                    recyclerView.setAdapter(adapter);
                }
            }

            @Override
            public void onFailure(Call<List<OfferModel>> call, Throwable t) {
                t.getMessage();
                t.printStackTrace();
            }
        });
    }

    private void clearAdapterData(){
        if(arrayOffer != null && !arrayOffer.isEmpty()){
            int size = arrayOffer.size();
            arrayOffer.clear();
            adapter.notifyItemRangeRemoved(0,size);
        }
    }
}
