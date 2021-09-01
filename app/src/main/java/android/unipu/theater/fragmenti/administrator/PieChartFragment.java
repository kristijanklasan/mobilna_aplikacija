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

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.ViewPortHandler;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class PieChartFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;
    private OfferModel offerModel;
    private IReservation iReservation;
    private ReservationModel reservationModel;
    private PieChart pieChart;
    private Switch switchBarChart;

    public PieChartFragment() {
        // Required empty public constructor
    }

    public static PieChartFragment newInstance(String param1, String param2) {
        PieChartFragment fragment = new PieChartFragment();
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
                .inflate(R.layout.fragment_pie_chart, container, false);
        initView(view);
        return view;
    }

    private void initView(View view){
        offerModel = new OfferModel();
        reservationModel = new ReservationModel();
        pieChart = (PieChart) view.findViewById(R.id.pieChart);
        switchBarChart = (Switch) view.findViewById(R.id.switchBarChart);
        getID();
        setElements();
    }

    private void setElements(){
        switchBarChart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(switchBarChart.isChecked()){
                    Bundle bundle = new Bundle();
                    bundle.putInt("id",offerModel.getId());
                    StatisticsFragment fragment = new StatisticsFragment();
                    fragment.setArguments(bundle);
                    getFragmentManager().beginTransaction().replace(R.id.fragmentReservation,fragment).commit();
                }
            }
        });
    }

    private void getID(){
        try{ Bundle bundle = getArguments();
            if(bundle!=null){
                int id = bundle.getInt("id");
                offerModel.setId(id);
                getStatisticsData(id);
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    private void getStatisticsData(int id){
        Retrofit retrofit = RetrofitClient.getInstance2();
        iReservation = retrofit.create(IReservation.class);

        final List<String> lista = new ArrayList<>();
        final List<Integer> listaKolicina = new ArrayList<>();
        Call<List<ReservationModel>> call = iReservation.getStatisticsData(id);
        call.enqueue(new Callback<List<ReservationModel>>() {
            @Override
            public void onResponse(Call<List<ReservationModel>> call, Response<List<ReservationModel>> response) {
                if(response.isSuccessful()){
                    List<ReservationModel> list = response.body();
                    for(int i=0; i<list.size(); i++){
                        reservationModel.setKolicina(list.get(i).getKolicina());
                        reservationModel.setUlaznice(list.get(i).getUlaznice());
                        reservationModel.setPotvrda(list.get(i).getPotvrda());
                        reservationModel.setMaxUlaznica(list.get(i).getMaxUlaznice());
                        lista.add(reservationModel.getPotvrda());
                        listaKolicina.add(reservationModel.getKolicina());
                    }
                    calculate(lista,listaKolicina,list.size());
                }
            }
            @Override
            public void onFailure(Call<List<ReservationModel>> call, Throwable t) {
                t.getMessage();
                t.printStackTrace();
            }
        });
    }

    private void calculate(List<String> list,List<Integer> kolicina, int size){
        String potvrdaString = "da";
        int poz = 0;
        int neg = 0;
        for(int i=0; i<size; i++){
            if(potvrdaString.equals(list.get(i))) poz += kolicina.get(i);
            else neg += kolicina.get(i);
        }
        createGraph(reservationModel.getUlaznice(),poz,neg);
    }

    private void createGraph(int razlika, int poz, int neg){
        List pieData = new ArrayList<>();
        ArrayList label = new ArrayList();

        pieData.add(new Entry(razlika, Color.BLUE));
        pieData.add(new Entry(poz, Color.GREEN));
        pieData.add(new Entry(neg, Color.RED));
        PieDataSet dataSet = new PieDataSet(pieData, "Rezervirana sjedala");

        label.add("Slobodna mjesta");
        label.add("Plaćeno");
        label.add("Neplaćeno");
        PieData data = new PieData(label, dataSet);
        data.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
                return String.valueOf((int) value);
            }
        });
        data.setValueTextSize(9f);
        dataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        pieChart.animateXY(5000, 5000);

        PieDataSet pieDataSet = new PieDataSet(pieData, "Odnos plaćenih i neplaćenih sjedala");
        pieDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        pieDataSet.setValueTextColor(Color.WHITE);
        pieDataSet.setValueTextSize(12f);
        data.setValueTextColor(Color.WHITE);
        pieChart.getLegend().setTextColor(Color.WHITE);
        pieChart.setData(data);
    }
}
