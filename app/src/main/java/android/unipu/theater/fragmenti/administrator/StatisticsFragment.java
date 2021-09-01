package android.unipu.theater.fragmenti.administrator;

import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.unipu.theater.R;
import android.unipu.theater.model.OfferModel;
import android.unipu.theater.model.ReservationModel;
import android.unipu.theater.retrofit.IReservation;
import android.unipu.theater.retrofit.RetrofitClient;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.ViewPortHandler;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;


public class StatisticsFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private OfferModel offerModel;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private IReservation iReservation;
    private ReservationModel reservationModel;
    private int c1,c2,c3,c4,c5,c6,c7,c8,c9,c10 = 0;
    private BarChart barChart;
    private Switch changeSwitch;

    public StatisticsFragment() {
        // Required empty public constructor
    }

    public static StatisticsFragment newInstance(String param1, String param2) {
        StatisticsFragment fragment = new StatisticsFragment();
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
        View view = inflater.inflate(R.layout.fragment_statistics, container, false);
        initView(view);
        getID();
        return view;
    }

    private void getID(){
        try{ Bundle bundle = getArguments();
            if(bundle!=null){
                int id = bundle.getInt("id");
                offerModel.setId(id);
                getAllReservations(offerModel.getId());
            }
        }catch(Exception e){ e.printStackTrace(); }
    }

    private void initView(View view){
        offerModel = new OfferModel();
        reservationModel = new ReservationModel();
        barChart = (BarChart) view.findViewById(R.id.barChart);
        changeSwitch = (Switch) view.findViewById(R.id.switchPromjena);
        setElements();
    }

    private void setElements(){
        changeSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(changeSwitch.isChecked()){
                    Bundle bundle = new Bundle();
                    bundle.putInt("id",offerModel.getId());
                    PieChartFragment fragment = new PieChartFragment();
                    fragment.setArguments(bundle);
                    getFragmentManager().beginTransaction().replace(R.id.fragmentReservation,fragment).commit();
                }
            }
        });
    }

    private void getAllReservations(int id){
        Retrofit retrofit = RetrofitClient.getInstance2();
        iReservation = retrofit.create(IReservation.class);

        final ArrayList<Integer> seatList = new ArrayList<>();
        Call<List<ReservationModel>> call = iReservation.ticketSeat(id);
        call.enqueue(new Callback<List<ReservationModel>>() {
            @Override
            public void onResponse(Call<List<ReservationModel>> call, Response<List<ReservationModel>> response) {
                if(response.isSuccessful()){
                    List<ReservationModel> list = response.body();
                    for(int i=0; i<list.size(); i++){
                        reservationModel.setOznaka(list.get(i).getOznaka());
                        seatList.add(reservationModel.getOznaka());
                    }
                    reservationModel.setOznakaLista(seatList);
                    setDataGraph();
                }
            }

            @Override
            public void onFailure(Call<List<ReservationModel>> call, Throwable t) {
                t.getMessage();
                t.printStackTrace();
            }
        });
    }

    private void setDataGraph(){
        for(int i=0; i<reservationModel.getOznakaLista().size(); i++){
            int position = reservationModel.getOznakaLista().get(i);
            if(position>=1 && position<=12) c1++;
            else if(position>12 && position<=24) c2++;
            else if(position>24 && position<=36) c3++;
            else if(position>36 && position<=48) c4++;
            else if(position>48 && position<=60) c5++;
            else if(position>60 && position<=72) c6++;
            else if(position>72 && position<=84) c7++;
            else if(position>84 && position<=96) c8++;
            else if(position>96 && position<=108) c9++;
            else if(position>108 && position<=120) c10++;
        }
        createGraph(c1,c2,c3,c4,c5,c6,c7,c8,c9,c10);
    }

    private void createGraph(int red1, int red2, int red3, int red4, int red5, int red6, int red7, int red8,
                             int red9, int red10){

        ArrayList<BarEntry> entry = new ArrayList<>();
        ArrayList<String> label = new ArrayList();

        entry.add(new BarEntry(red1,0));
        entry.add(new BarEntry(red2,1));
        entry.add(new BarEntry(red3,2));
        entry.add(new BarEntry(red4,3));
        entry.add(new BarEntry(red5,4));
        entry.add(new BarEntry(red6,5));
        entry.add(new BarEntry(red7,6));
        entry.add(new BarEntry(red8,7));
        entry.add(new BarEntry(red9,8));
        entry.add(new BarEntry(red10,9));

        label.add("Red 1");
        label.add("Red 2");
        label.add("Red 3");
        label.add("Red 4");
        label.add("Red 5");
        label.add("Red 6");
        label.add("Red 7");
        label.add("Red 8");
        label.add("Red 9");
        label.add("Red 10");

        BarDataSet bardataset = new BarDataSet(entry, "Broj rezerviranih sjedala");
        ValueFormatter vf = new ValueFormatter() {
            @Override
            public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
                return ""+(int)value;
            }
        };
        bardataset.setValueFormatter(vf);
        barChart.animateY(5000);
        barChart.setVisibleYRangeMaximum(12, YAxis.AxisDependency.LEFT);
        bardataset.setColors(ColorTemplate.COLORFUL_COLORS);
        bardataset.setValueTextColor(Color.WHITE);
        bardataset.setValueTextSize(12f);
        BarData data = new BarData(label,bardataset);
        data.setValueTextColor(Color.WHITE);
        barChart.getLegend().setTextColor(Color.WHITE);
        barChart.getXAxis().setTextColor(Color.WHITE);
        barChart.getAxisLeft().setTextColor(Color.WHITE);
        barChart.getAxisLeft().setAxisMinValue(0);
        barChart.getAxisLeft().setAxisMaxValue(12);
        barChart.setData(data);
    }

}
