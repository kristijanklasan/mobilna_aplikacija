package android.unipu.theater.administrator;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.unipu.theater.R;
import android.unipu.theater.fragmenti.administrator.AdminReservationList;
import android.unipu.theater.fragmenti.administrator.OfferDetails;
import android.unipu.theater.fragmenti.administrator.StatisticsFragment;
import android.unipu.theater.model.OfferModel;
import android.unipu.theater.retrofit.IOffer;
import android.unipu.theater.retrofit.RetrofitClient;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ReservationDetail extends AppCompatActivity {

    private OfferModel offerModel;
    private TextView nameReservation;
    private IOffer iOffer;
    private TabLayout tabOffer;
    private Toolbar toolbarReservation;
    private FloatingActionButton fabDelete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservation_detail);
        initView();
    }

    private void initView(){
        offerModel = new OfferModel();
        Retrofit retrofitClient = RetrofitClient.getInstance2();
        iOffer = retrofitClient.create(IOffer.class);
        nameReservation = (TextView) findViewById(R.id.naslovReservation);
        toolbarReservation = (Toolbar) findViewById(R.id.toolbarReservationDetail);
        fabDelete = (FloatingActionButton) findViewById(R.id.fabDeleteReservation);
        tabOffer = (TabLayout) findViewById(R.id.tabOffer);
        tabOffer.getTabAt(0).select();
        getKey();
        setElements();
        getAllData();
    }

    private void setTabText(int position){
        Bundle bundle = new Bundle();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

        if(position == 0){
            bundle.putInt("id", offerModel.getId());
            OfferDetails offerDetails = new OfferDetails();
            offerDetails.setArguments(bundle);
            ft.replace(R.id.fragmentReservation, offerDetails).commit();
        }

        else if(position == 1){
            bundle.putInt("id", offerModel.getId());
            AdminReservationList adminReservationList = new AdminReservationList();
            adminReservationList.setArguments(bundle);
            ft.replace(R.id.fragmentReservation,adminReservationList).commit();
        }

        else if(position == 2){
            bundle.putInt("id",offerModel.getId());
            StatisticsFragment frag = new StatisticsFragment();
            frag.setArguments(bundle);
            ft.replace(R.id.fragmentReservation,frag).commit();
        }
    }

    private void setElements(){
        setTabText(0);

        setSupportActionBar(toolbarReservation);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        tabOffer.getTabAt(0).setIcon(R.drawable.ic_assignment_black_24dp);
        tabOffer.getTabAt(0).getIcon().setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_IN);
        tabOffer.getTabAt(1).setIcon(R.drawable.ticket);
        tabOffer.getTabAt(1).getIcon().setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_IN);
        tabOffer.getTabAt(2).setIcon(R.drawable.ic_insert_chart_black_24dp);
        tabOffer.getTabAt(2).getIcon().setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_IN);
        tabOffer.setOnTabSelectedListener(new TabLayout.BaseOnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position =  tab.getPosition();
                setTabText(position);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) { }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {int position =  tab.getPosition();
                setTabText(position);
            }
        });

        toolbarReservation.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               Intent intent = new Intent(ReservationDetail.this,ReservationActivity.class);
               startActivity(intent);
               finish();
            }
        });

        fabDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new SweetAlertDialog(ReservationDetail.this, SweetAlertDialog.CUSTOM_IMAGE_TYPE)
                        .setTitleText("Jeste li sigurni?")
                        .setCustomImage(R.drawable.ic_delete_forever_black_24dp)
                        .setContentText("Jednom izbrisana ponuda je zauvijek izbrisana!")
                        .setCancelText("Odustani!")
                        .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                sweetAlertDialog.cancel();
                            }
                        }).setConfirmText("IZBRIŠI!").setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        sDialog.setTitleText("Izbrisano!")
                                .setContentText("Ponuda je izbrisana!")
                                .setConfirmText("U redu")
                                .setConfirmClickListener(null)
                                .showCancelButton(false)
                                .changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
                        deleteOffer(offerModel.getId());

                    }
                }).show();
            }
        });
    }

    private void getKey(){
        try{ Intent intent = getIntent();
            offerModel.setId(intent.getIntExtra("ponuda",0));
        }catch(Exception e){ e.printStackTrace(); }
    }

    private void getAllData(){
        Call<List<OfferModel>> call = iOffer.getDataFromID(offerModel.getId());
        call.enqueue(new Callback<List<OfferModel>>() {
            @Override
            public void onResponse(Call<List<OfferModel>> call, Response<List<OfferModel>> response) {
                if(response.isSuccessful()){
                    List<OfferModel> list = response.body();
                    for(int i=0; i<list.size(); i++){
                        offerModel.setNaziv(list.get(i).getNaziv());
                    }nameReservation.setText(offerModel.getNaziv());
                }
            }

            @Override
            public void onFailure(Call<List<OfferModel>> call, Throwable t) {
                t.printStackTrace();
                t.getMessage();
            }
        });
    }

    private void deleteOffer(int id){
        Call<ResponseBody> call = iOffer.deleteOffer(id);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.isSuccessful()){
                    Intent intent = new Intent(ReservationDetail.this,ReservationActivity.class);
                    startActivity(intent);
                    finish();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                t.printStackTrace();
                t.getMessage();
            }
        });
    }
}
