package android.unipu.theater.user;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.unipu.theater.R;
import android.unipu.theater.ShareMethods;
import android.unipu.theater.adapter.SliderAdapter;
import android.unipu.theater.fragmenti.user.PerformanceTickets;
import android.unipu.theater.fragmenti.user.ShowDetailFragment;
import android.unipu.theater.model.FavoritesModel;
import android.unipu.theater.model.ImageModel;
import android.unipu.theater.model.TheaterModel;
import android.unipu.theater.model.UserModel;
import android.unipu.theater.retrofit.IFavorites;
import android.unipu.theater.retrofit.ITheater;
import android.unipu.theater.retrofit.RetrofitClient;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;
import com.smarteist.autoimageslider.SliderView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import cn.pedant.SweetAlert.SweetAlertDialog;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ShowDetail extends AppCompatActivity {

    private TabLayout tabMenu;
    private SliderView sliderSlike;
    private ImageView imageFavorites,imageShare;
    private ITheater iTheater;
    private IFavorites iFavorites;
    private TheaterModel theaterModel;
    private Toolbar toolbar;
    private FavoritesModel favoritesModel;
    private ShareMethods shareMethods;
    private UserModel userModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_detail);

        initView();
        int id = getID();
        getData(id);
    }

     private void initView(){
        tabMenu = (TabLayout) findViewById(R.id.tabMenu);
        sliderSlike = (SliderView) findViewById(R.id.sliderSlike);
        tabMenu.getTabAt(0).select();
        toolbar = findViewById(R.id.toolbarDetailActivity);
        imageFavorites = (ImageView) findViewById(R.id.toolbarFavorites);
        imageShare = (ImageView) findViewById(R.id.toolbarShare);
        favoritesModel = new FavoritesModel();
        shareMethods = new ShareMethods();
        userModel = new UserModel();
        theaterModel = new TheaterModel();
        Retrofit retrofitClient = RetrofitClient.getInstance2();
        iFavorites = retrofitClient.create(IFavorites.class);
     }

     private int getID(){
        try{ Intent intent = getIntent();
            int id = intent.getIntExtra("id",0);
            return id;
        }catch(Exception e){ e.printStackTrace(); }
        return 0;
     }

     private void setTabText(int position){
         Bundle bundle = new Bundle();
         FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

         if(position == 0){
             bundle.putInt("id", theaterModel.getId());
             ShowDetailFragment showDetail = new ShowDetailFragment();
             showDetail.setArguments(bundle);
             ft.replace(R.id.fragmentShowPerformances, showDetail).commit();
         }

         else if(position == 1){
             bundle.putInt("id",theaterModel.getId());
             PerformanceTickets tickets = new PerformanceTickets();
             tickets.setArguments(bundle);
             ft.replace(R.id.fragmentShowPerformances,tickets).commit();
         }
     }

     private void setBookmarks(){
        if(favoritesModel.getDostupnost()){
            imageFavorites.setBackgroundResource(R.drawable.ic_favorite_black_24dp);
        }else imageFavorites.setBackgroundResource(R.drawable.ic_favorite_border_black_24dp);
     }

     private void setData(){
        ArrayList<ImageModel> sliderImage = new ArrayList<>();

        tabMenu.getTabAt(0).setIcon(R.drawable.ic_details_black_24dp);
        tabMenu.getTabAt(0).getIcon().setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_IN);
        tabMenu.getTabAt(1).setIcon(R.drawable.ic_date_range_black_24dp);
        tabMenu.getTabAt(1).getIcon().setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_IN);

        sliderImage.add(new ImageModel(theaterModel.getSlika1()));
        sliderImage.add(new ImageModel(theaterModel.getSlika2()));
        SliderAdapter adapter = new SliderAdapter(getApplicationContext(),sliderImage);
        sliderSlike.setAutoCycleDirection(SliderView.LAYOUT_DIRECTION_LTR);
        sliderSlike.setSliderAdapter(adapter);
        sliderSlike.setScrollTimeInSec(5);
        sliderSlike.setAutoCycle(true);
        sliderSlike.startAutoCycle();

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(theaterModel.getNaziv());

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 Intent i = new Intent(ShowDetail.this, UserPerformances.class);
                 startActivity(i);
                 finish();
             }
         });

        imageFavorites.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(favoritesModel.getDostupnost()) {
                    imageFavorites.setBackgroundResource(R.drawable.ic_favorite_black_24dp);
                    deleteFavorites();
                    setNegativeSnackbar(v);
                }else {
                    imageFavorites.setBackgroundResource(R.drawable.ic_favorite_border_black_24dp);
                    insertFavorites();
                    setPozitiveSnackbar(v);
                }
            }
        });

        imageShare.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                try { Intent i = new Intent(Intent.ACTION_SEND);
                    i.setType("text/plain");
                    i.putExtra(Intent.EXTRA_SUBJECT, "Premijera predstave");
                    i.putExtra(Intent.EXTRA_TEXT, theaterModel.getNaziv() + " dostupno u TheaterGOLD aplikaciji!");
                    startActivity(Intent.createChooser(i, "Podijeli s prijateljima!"));
                    finish();
                }catch(Exception e){
                    Toast.makeText(getApplicationContext(),"Nije moguće podijeliti sadržaj!",Toast.LENGTH_SHORT).show();
                }
            }
        });

        setTabText(0);
        tabMenu.setOnTabSelectedListener(new TabLayout.BaseOnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position =  tab.getPosition();
                setTabText(position);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) { }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                int position =  tab.getPosition();
                setTabText(position);
            }
         });
     }

     private void setPozitiveSnackbar(View view) {
         Snackbar snackbar = Snackbar.make(view, "Predstava je spremljena u favorite!", Snackbar.LENGTH_LONG)
                 .setAction("OTVORI", new View.OnClickListener() {
                     @Override
                     public void onClick(View v) {
                         Intent intent = new Intent(ShowDetail.this, ReservationOverview.class);
                         startActivity(intent);
                         finish();
                     }
                 });
         snackbar.setActionTextColor(getResources().getColor(R.color.bluelight));
         snackbar.show();
     }

     private void setNegativeSnackbar(View view){
        Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content),
                "Uklonjeno iz favorita!",Snackbar.LENGTH_SHORT);
         View snackbarLayout = snackbar.getView();
         TextView textView = (TextView)snackbarLayout.findViewById(com.google.android.material.R.id.snackbar_text);
         textView.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_delete_black_24dp, 0);
         snackbar.show();
     }

     private void getData(int id){

         Retrofit retrofitClient = RetrofitClient.getInstance2();
         iTheater = retrofitClient.create(ITheater.class);

         Call<List<TheaterModel>> call = iTheater.getPredstavaDetalji(id);
         call.enqueue(new Callback<List<TheaterModel>> (){
             @Override
             public void onResponse(Call<List<TheaterModel>> call, Response<List<TheaterModel>> response){
                List<TheaterModel> list = response.body();
                if(response.isSuccessful()){
                    for(int i=0; i<list.size(); i++){
                        theaterModel.setId(list.get(i).getId());
                        theaterModel.setNaziv(list.get(i).getNaziv());
                        theaterModel.setSlika1(list.get(i).getSlika1());
                        theaterModel.setSlika2(list.get(i).getSlika2());
                        setData();
                        checkAvailability();
                    }
                }
             }
             @Override
             public void onFailure(Call<List<TheaterModel>> call, Throwable t){
                t.getMessage();
                t.printStackTrace();
             }
         });
     }

     private void checkAvailability(){

        Retrofit retrofitClient = RetrofitClient.getInstance2();
        iFavorites = retrofitClient.create(IFavorites.class);

        int id_korisnik = Integer.parseInt(userModel.getKljuc());
        Call<List<FavoritesModel>> call = iFavorites.checkAvailability(id_korisnik,theaterModel.getId());
        call.enqueue(new Callback<List<FavoritesModel>>(){
            @Override
            public void onResponse(Call<List<FavoritesModel>> call, Response<List<FavoritesModel>> response){
                if(response.isSuccessful()){
                    List<FavoritesModel> list = response.body();
                    if(list != null) {
                        for (int i = 0; i < list.size(); i++) {
                            favoritesModel.setId(list.get(i).getId());
                            favoritesModel.setDatum(list.get(i).getDatum());
                            favoritesModel.setId_korisnik(list.get(i).getId_korisnik());
                            favoritesModel.setId_predstava(list.get(i).getId_predstava());
                        }
                        favoritesModel.setDostupnost(true);
                        setBookmarks();
                    }else {
                        favoritesModel.setDostupnost(false);
                        setBookmarks();
                    }
                } else setErrorMessage();
            }

            @Override
            public void onFailure(Call<List<FavoritesModel>> call, Throwable t) {
                t.getMessage();
                t.printStackTrace();
            }
        });
     }

     private void insertFavorites(){
        String date = getDate();
        int id_korisnik = Integer.parseInt(userModel.getKljuc());

        Call<ResponseBody> call = iFavorites.insertFavorites(date,id_korisnik,theaterModel.getId());
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.isSuccessful()) checkAvailability();
                else setErrorMessage();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                t.printStackTrace();
                t.getMessage();
            }
        });
     }

     private void deleteFavorites(){
        Retrofit retrofitClient = RetrofitClient.getInstance2();
        iFavorites = retrofitClient.create(IFavorites.class);

        Call<ResponseBody> call = iFavorites.deleteFavorites(favoritesModel.getId());
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.isSuccessful()) checkAvailability();
                else setErrorMessage();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                t.printStackTrace();
                t.getMessage();
            }
        });
     }

    private String getDate() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date datum = new Date();
        favoritesModel.setDatum(dateFormat.format(datum));
        return dateFormat.format(datum);
    }

    private void setErrorMessage(){
        new SweetAlertDialog(getApplicationContext(),SweetAlertDialog.ERROR_TYPE).
                setTitleText("Greška!").
                setContentText("Dogodila se pogreška!").show();
    }
}

