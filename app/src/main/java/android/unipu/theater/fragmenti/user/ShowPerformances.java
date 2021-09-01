package android.unipu.theater.fragmenti.user;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.unipu.theater.R;
import android.unipu.theater.adapter.SwipeAdapter;
import android.unipu.theater.adapter.UserPerformanceAdapter;
import android.unipu.theater.model.TheaterModel;
import android.unipu.theater.retrofit.ITheater;
import android.unipu.theater.retrofit.RetrofitClient;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ShowPerformances extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;
    private TextView textPredstave;
    private RecyclerView recyclerPerformances;
    private UserPerformanceAdapter adapter;
    private ArrayList<TheaterModel> naslov;
    private ITheater iTheater;
    private TheaterModel theaterModel;
    private SwipeAdapter swipeAdapter;

    public ShowPerformances() {
        // Required empty public constructor
    }

    public static ShowPerformances newInstance(String param1, String param2) {
        ShowPerformances fragment = new ShowPerformances();
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
        View view = inflater.inflate(R.layout.fragment_show_performances, container, false);
        initView(view);
        getData();

        getPerformances(theaterModel.getKategorija());
        return view;
    }

    private void initView(View view) {
        theaterModel = new TheaterModel();
        textPredstave = (TextView) view.findViewById(R.id.textUserPredstave);
        recyclerPerformances = view.findViewById(R.id.recyclerUserPerformances);
        recyclerPerformances.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    private String getData(){
        String kategorija = "";
        try{ kategorija = getArguments().getString("kategorija","");
            theaterModel.setKategorija(kategorija);
            return kategorija;
        }catch(Exception e){e.printStackTrace();}
        return kategorija;
    }

    private void getPerformances(String kategorija){

        Retrofit retrofitClient = RetrofitClient.getInstance2();
        iTheater = retrofitClient.create(ITheater.class);
        naslov = new ArrayList<>();

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
                        theaterModel = new TheaterModel();
                        theaterModel.setNaziv(predstave.get(i).getNaziv());
                        theaterModel.setSlika1(predstave.get(i).getSlika1());
                        theaterModel.setId(predstave.get(i).getId());
                        naslov.add(theaterModel);
                    }
                    try {
                        adapter = new UserPerformanceAdapter(getContext(), naslov);
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
