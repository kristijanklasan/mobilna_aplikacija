package android.unipu.theater.fragmenti.user;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import android.unipu.theater.R;
import android.unipu.theater.model.TheaterModel;
import android.unipu.theater.retrofit.ITheater;
import android.unipu.theater.retrofit.RetrofitClient;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.google.android.material.chip.Chip;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ShowDetailFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;
    private ImageView imageLink;
    private ITheater iTheater;
    private TheaterModel theaterModel;
    private TextView opisDetalji,glumciText, datumText, dramaturgijaText,
                    redateljiText, kostimografijaText, scenografijaText,glazbaText,
                    koreografijaText, podrskaText ;
    private Chip kategorijaChip;

    public ShowDetailFragment() {
        // Required empty public constructor
    }

    public static ShowDetailFragment newInstance(String param1, String param2) {
        ShowDetailFragment fragment = new ShowDetailFragment();
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

        View view = inflater.inflate(R.layout.fragment_show_detail, container, false);
        initView(view);
        int id = getID(view);
        getDetail(id);
        return view;
    }

    private int getID(View view){
        int id = 0;
        try{ Bundle bundle = this.getArguments();
            if(bundle != null){
                id = bundle.getInt("id",0);
                return id;
            }
        }catch(Exception e){ e.printStackTrace(); }
        return id;
    }
    private void initView(View view){

        theaterModel = new TheaterModel();
        imageLink = (ImageView) view.findViewById(R.id.imageLink);
        imageLink.setImageResource(R.drawable.link);
        opisDetalji = (TextView) view.findViewById(R.id.opisDetalji);
        kategorijaChip = (Chip) view.findViewById(R.id.kategorijaDetaljiChip);
        glumciText = (TextView) view.findViewById(R.id.glumciDetalji);
        datumText = (TextView) view.findViewById(R.id.datumDetalji);
        redateljiText = (TextView) view.findViewById(R.id.redateljDetalji);
        dramaturgijaText = (TextView) view.findViewById(R.id.dramaturgijaDetalji);
        kostimografijaText = (TextView) view.findViewById(R.id.kostimografijaDetalji);
        scenografijaText = (TextView) view.findViewById(R.id.scenografijaDetalji);
        glazbaText = (TextView) view.findViewById(R.id.glazbaDetalji);
        koreografijaText = (TextView) view.findViewById(R.id.koreografijaDetalji);
        podrskaText = (TextView) view.findViewById(R.id.podrskaDetalji);
    }

    private void setText(){
        opisDetalji.setText(theaterModel.getOpis());
        kategorijaChip.setText(theaterModel.getKategorija());

        String s = theaterModel.getGlumci();
        s = s.replace(",",",\n \n");
        glumciText.setText(s);
        datumText.setText(theaterModel.getDatumPremijere());
        redateljiText.setText(theaterModel.getRedatelj());
        dramaturgijaText.setText(theaterModel.getDramaturgija());
        kostimografijaText.setText(theaterModel.getKostimografija());
        scenografijaText.setText(theaterModel.getScenografija());
        glazbaText.setText(theaterModel.getGlazba());
        koreografijaText.setText(theaterModel.getKoreografija());
        podrskaText.setText(theaterModel.getTehnickaPodrska());
    }

    private void getDetail(int id){
        Retrofit retrofitClient = RetrofitClient.getInstance2();
        iTheater = retrofitClient.create(ITheater.class);

        Call<List<TheaterModel>> call = iTheater.getPredstavaDetalji(id);
        call.enqueue(new Callback<List<TheaterModel>>(){
            @Override
            public void onResponse(Call<List<TheaterModel>> call, Response<List<TheaterModel>> response){
                List<TheaterModel> list = response.body();

                if(response.isSuccessful()){
                    for(int i=0; i<list.size(); i++){
                        theaterModel.setId(list.get(i).getId());
                        theaterModel.setKategorija(list.get(i).getKategorija());
                        theaterModel.setNaziv(list.get(i).getNaziv());
                        theaterModel.setOpis(list.get(i).getOpis());
                        theaterModel.setDatumPremijere(list.get(i).getDatumPremijere());
                        theaterModel.setGlumci(list.get(i).getGlumci());
                        theaterModel.setRedatelj(list.get(i).getRedatelj());
                        theaterModel.setDramaturgija(list.get(i).getDramaturgija());
                        theaterModel.setKostimografija(list.get(i).getKostimografija());
                        theaterModel.setScenografija(list.get(i).getScenografija());
                        theaterModel.setGlazba(list.get(i).getGlazba());
                        theaterModel.setKoreografija(list.get(i).getKoreografija());
                        theaterModel.setTehnickaPodrska(list.get(i).getTehnickaPodrska());
                        theaterModel.setSlika1(list.get(i).getSlika1());
                        theaterModel.setSlika2(list.get(i).getSlika2());
                        setText();
                    }
                }
            }

            @Override
            public void onFailure(Call<List<TheaterModel>> call, Throwable t){
                t.printStackTrace();
                t.getMessage();
            }
        });
    }
}
