package android.unipu.theater.fragmenti.administrator;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.unipu.theater.adapter.PerformanceAdapter;
import android.unipu.theater.adapter.SwipeAdapter;
import android.unipu.theater.model.TheaterModel;
import android.unipu.theater.retrofit.ITheater;
import android.unipu.theater.retrofit.RetrofitClient;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.unipu.theater.R;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class PerformancesFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private TextView textKategorija,textPredstave;
    private RecyclerView recyclerPerformances;
    private PerformanceAdapter adapter;
    private String mParam1;
    private String mParam2;
    private List<String> slike, naslov;
    private ArrayList<Integer> id;
    private ITheater iTheater;
    private SwipeAdapter swipeAdapter;

    public PerformancesFragment() {
        // Required empty public constructor
    }

    public static PerformancesFragment newInstance(String param1, String param2) {
        PerformancesFragment fragment = new PerformancesFragment();
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

        View view = inflater.inflate(R.layout.fragment_performances, container, false);

        textPredstave = (TextView) view.findViewById(R.id.textPredstave);
        textKategorija = (TextView) view.findViewById(R.id.kategorijaFragment);
        recyclerPerformances = (RecyclerView) view.findViewById(R.id.recyclerPerformances);
        recyclerPerformances.setLayoutManager(new LinearLayoutManager(getContext()));

        String kategorija = getArguments().getString("kategorija","defaultValue");
        textKategorija.setText("Kategorija: "+ kategorija);

        Retrofit retrofitClient = RetrofitClient.getInstance2();
        iTheater = retrofitClient.create(ITheater.class);
        getPerformances(kategorija);

        return view;
    }

    private void getPerformances(String kategorija){
        slike = new ArrayList<>();
        naslov = new ArrayList<>();
        id = new ArrayList<>();

        Call<List<TheaterModel>> call = iTheater.getPredstava(kategorija);
        call.enqueue(new Callback<List<TheaterModel>>() {
            @Override
            public void onResponse(Call<List<TheaterModel>> call, Response<List<TheaterModel>> response) {

                if(response.isSuccessful()) {
                    List<TheaterModel> predstave = response.body();

                    if (!predstave.isEmpty())
                        textPredstave.setVisibility(View.GONE);
                    else
                        textPredstave.setVisibility(View.VISIBLE);

                    for (int i = 0; i < predstave.size(); i++) {
                        naslov.add(predstave.get(i).getNaziv());
                        slike.add(predstave.get(i).getSlika1());
                        id.add(predstave.get(i).getId());
                    }
                    try {
                        adapter = new PerformanceAdapter(getContext(), slike, naslov, id);
                        recyclerPerformances.setAdapter(adapter);
                        recyclerPerformances.scheduleLayoutAnimation();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<List<TheaterModel>> call, Throwable t) {
                t.printStackTrace();
                t.getMessage();
                textPredstave.setVisibility(TextView.VISIBLE);
            }
        });
    }
}
