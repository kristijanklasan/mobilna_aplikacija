package android.unipu.theater.user;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.unipu.theater.R;
import android.unipu.theater.adapter.UserPerformanceAdapter;
import android.unipu.theater.model.TheaterModel;
import android.unipu.theater.retrofit.ITheater;
import android.unipu.theater.retrofit.RetrofitClient;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class UserPerformances extends AppCompatActivity {

    private TabLayout tabLayout;
    private ITheater iTheater;
    private TheaterModel theaterModel;
    private UserPerformanceAdapter adapter;
    private RecyclerView recyclerPerformances;
    private SearchView searchView;
    private TabLayout tabsUser;
    private RelativeLayout relativeLayoutDetail;
    private TextView textPredstave, searchText;
    private ArrayList<TheaterModel> naslov;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_performances);
        initView();
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

                    if (!predstave.isEmpty()) textPredstave.setVisibility(View.GONE);
                    else textPredstave.setVisibility(View.VISIBLE);
                    for (int i = 0; i < predstave.size(); i++) {
                        theaterModel = new TheaterModel();
                        theaterModel.setNaziv(predstave.get(i).getNaziv());
                        theaterModel.setSlika1(predstave.get(i).getSlika1());
                        theaterModel.setId(predstave.get(i).getId());
                        naslov.add(theaterModel);
                    }
                    try {
                        adapter = new UserPerformanceAdapter(getApplicationContext(), naslov);
                        recyclerPerformances.setAdapter(adapter);
                        recyclerPerformances.scheduleLayoutAnimation();
                    } catch (Exception e) { e.printStackTrace(); }
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu){

        getMenuInflater().inflate(R.menu.search_menu, menu);
        MenuItem menuItem = menu.findItem(R.id.searchPerformance);

        searchView = (SearchView) menuItem.getActionView();
        searchView.setQueryHint("Pretraži predstave");
        searchView.addOnAttachStateChangeListener(new View.OnAttachStateChangeListener() {
            @Override
            public void onViewDetachedFromWindow(View arg0) {
                tabsUser.setVisibility(View.VISIBLE);
                searchText.setVisibility(View.GONE);
                recyclerPerformances.removeAllViews();
                adapter.notifyDataSetChanged();
                sendCategory("Drama");
            }

            @Override
            public void onViewAttachedToWindow(View arg0) {
                searchText.setVisibility(View.VISIBLE);
                searchView.setBackgroundResource(R.drawable.search_view_background);
                relativeLayoutDetail.setVisibility(View.GONE);
            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if(adapter.getItemCount() == 0){
                    relativeLayoutDetail.setVisibility(View.VISIBLE);
                }else relativeLayoutDetail.setVisibility(View.GONE);

                adapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                adapter.getFilter().filter(query);
                if(query.length()==0){
                    searchText.setVisibility(View.VISIBLE);
                    relativeLayoutDetail.setVisibility(View.GONE);
                }else{
                    searchText.setVisibility(View.GONE);
                    if(query.length() < 3)
                        relativeLayoutDetail.setVisibility(View.GONE);
                }
                return false;
            }
        });
        searchView.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getNameShow();
                tabsUser.setVisibility(View.GONE);
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        return true;
    }

    public void getNameShow(){
        Retrofit retrofitClient = RetrofitClient.getInstance2();
        iTheater = retrofitClient.create(ITheater.class);
        naslov = new ArrayList<>();
        Call<List<TheaterModel>> call = iTheater.dohvatPredstava();
        call.enqueue(new Callback<List<TheaterModel>>() {
            @Override
            public void onResponse(Call<List<TheaterModel>> call, Response<List<TheaterModel>> response) {
                List<TheaterModel> list = response.body();
                if(response.isSuccessful()){

                    textPredstave.setVisibility(View.GONE);
                    for(int i=0; i<list.size(); i++){
                        theaterModel = new TheaterModel();
                        theaterModel.setId(list.get(i).getId());
                        theaterModel.setNaziv(list.get(i).getNaziv());
                        theaterModel.setSlika1(list.get(i).getSlika1());
                        naslov.add(theaterModel);
                    }
                    try {
                        adapter = new UserPerformanceAdapter(getApplicationContext(),naslov);
                        recyclerPerformances.setAdapter(adapter);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                else textPredstave.setVisibility(View.VISIBLE);
            }

            @Override
            public void onFailure(Call<List<TheaterModel>> call, Throwable t) {
                t.getMessage();
                t.printStackTrace();
                textPredstave.setVisibility(View.VISIBLE);
            }
        });
    }

    private void initView() {

        Toolbar toolbar = findViewById(R.id.toolbarUserPerformance);
        setSupportActionBar(toolbar);
        theaterModel = new TheaterModel();
        tabLayout = (TabLayout) findViewById(R.id.tabsUser);
        searchText = (TextView) findViewById(R.id.searchText);
        relativeLayoutDetail = (RelativeLayout) findViewById(R.id.relativeLayoutDetail);
        tabLayout.addTab(tabLayout.newTab().setText("Drama"));
        tabLayout.addTab(tabLayout.newTab().setText("Komedija"));
        tabLayout.addTab(tabLayout.newTab().setText("Tragikomedija"));
        tabLayout.addTab(tabLayout.newTab().setText("Vodvilj"));
        tabLayout.addTab(tabLayout.newTab().setText("Glazba"));
        tabLayout.addTab(tabLayout.newTab().setText("Melodrama"));
        tabLayout.addTab(tabLayout.newTab().setText("Balet"));
        tabLayout.addTab(tabLayout.newTab().setText("Parodija"));
        tabLayout.addTab(tabLayout.newTab().setText("Opera"));
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        tabsUser = (TabLayout) findViewById(R.id.tabsUser);

        textPredstave = (TextView) findViewById(R.id.textUserPredstave);
        recyclerPerformances = findViewById(R.id.recyclerUserPerformances);
        recyclerPerformances.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        recyclerPerformances = findViewById(R.id.recyclerUserPerformances);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(UserPerformances.this, MainWindow.class);
                tabsUser.setVisibility(View.VISIBLE);
                startActivity(i);
                finish();
            }
        });

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE|
                WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        tabLayout.setOnTabSelectedListener(new TabLayout.BaseOnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                String position = (String) tab.getText();
                sendCategory(position);
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) { }

            @Override
            public void onTabReselected(TabLayout.Tab tab) { }
        });
       sendCategory("Drama");
    }

    private void sendCategory(String kategorija){
        getPerformances(kategorija);
    }
}
