package android.unipu.theater.adapter;

import android.content.Context;
import android.os.Bundle;
import android.unipu.theater.R;
import android.unipu.theater.fragmenti.administrator.TicketDetail;
import android.unipu.theater.model.ReservationModel;
import android.unipu.theater.retrofit.IReservation;
import android.unipu.theater.retrofit.RetrofitClient;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import com.chauthai.swipereveallayout.SwipeRevealLayout;
import com.chauthai.swipereveallayout.ViewBinderHelper;

import java.util.ArrayList;
import java.util.Locale;

import cn.pedant.SweetAlert.SweetAlertDialog;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class SwipeAdapter extends RecyclerView.Adapter<SwipeAdapter.SwipeViewHolder> {

    private Context context;
    private IReservation iReservation;
    private ArrayList<ReservationModel> reservationModels;
    private ViewBinderHelper viewBinderHelper = new ViewBinderHelper();

    public SwipeAdapter(Context context, ArrayList<ReservationModel> reservationModels){
        this.context = context;
        this.reservationModels = reservationModels;
    }

    @NonNull
    @Override
    public SwipeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_swipe,parent, false);
        return new SwipeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final SwipeViewHolder holder, final int position) {
        Retrofit retrofit = RetrofitClient.getInstance2();
        iReservation = retrofit.create(IReservation.class);

        Animation animation = AnimationUtils.loadAnimation(holder.itemView.getContext(),R.anim.item_anim_fall_right);
        holder.itemView.startAnimation(animation);
        viewBinderHelper.setOpenOnlyOne(true);
        viewBinderHelper.bind(holder.swipeRevealLayout, String.valueOf(reservationModels.get(position).getNaziv()));
        viewBinderHelper.closeLayout(String.valueOf(reservationModels.get(position).getNaziv()));

        String korisnik = reservationModels.get(position)
                .getIme()+" "+reservationModels.get(position).getPrezime();
        String ukupnoString = reservationModels.get(position).getUkupno()+" kn";
        String datum = reservationModels.get(position).getDatum_dodavanja()+"h";

        holder.naslov.setText(korisnik);
        holder.kolicina.setText(String.valueOf(reservationModels.get(position).getKolicina()));
        holder.datum.setText(datum);
        holder.ukupno.setText(String.format(Locale.ENGLISH,"%.2s %s",ukupnoString,"kn"));

        String potvrdaString = reservationModels.get(position).getPotvrda();
        String da = "da";
        if(potvrdaString.equals(da)){
            holder.potvrda.setChecked(true);
            holder.potvrda.setText("Da");
        }else{
            holder.potvrda.setChecked(false);
            holder.potvrda.setText("Ne");
        }

        holder.potvrda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(holder.potvrda.isChecked()){
                    holder.potvrda.setText("Da");
                    updateConfirmation(reservationModels.get(position).getId(),"da");
                }else {
                    holder.potvrda.setText("Ne");
                    updateConfirmation(reservationModels.get(position).getId(),"ne");
                }
            }
        });

        holder.linearDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SweetAlertDialog dialog = new SweetAlertDialog(holder.itemView.getContext(),SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("Želite li izbrisati kartu?")
                        .setContentText("Jednom izbrisanu kartu nije moguće vratiti!")
                        .setConfirmText("Izbriši")
                        .setCancelText("Odustani")
                        .setCancelClickListener(null)
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                deleteTicket(holder.itemView,reservationModels.get(position).getId(),
                                        position,reservationModels.get(position).getKolicina());
                                sweetAlertDialog.dismissWithAnimation();
                            }
                        });
                dialog.show();
            }
        });

        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TicketDetail detail = new TicketDetail();
                Bundle bundle = new Bundle();
                bundle.putInt("id",reservationModels.get(position).getId());
                bundle.putInt("id_ponuda",reservationModels.get(position).getId_ponuda());
                AppCompatActivity activity = (AppCompatActivity) v.getContext();
                detail.setArguments(bundle);
                activity.getSupportFragmentManager().beginTransaction()
                        .setCustomAnimations(R.anim.slide_out_right,R.anim.slide_out_left)
                        .replace(R.id.fragmentReservation,detail).commit();
            }
        });
    }

    @Override
    public int getItemCount() {
        return reservationModels.size();
    }

    private void updateConfirmation(int id, String potvrda){
        Call<ResponseBody> call = iReservation.updateConfirmation(id,potvrda);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) { }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                t.getMessage();
                t.printStackTrace();
            }
        });
    }

    private void deleteTicket(final View v, int id, final int position, int kolicina){
        Call<ResponseBody> call = iReservation.deleteReservation(id,kolicina);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.isSuccessful()){
                    reservationModels.remove(position);
                    notifyItemRemoved(position);
                    notifyItemRangeChanged(position, reservationModels.size());
                    SweetAlertDialog dialog = new SweetAlertDialog(v.getContext(),SweetAlertDialog.SUCCESS_TYPE)
                            .setTitleText("Karta je izbrisana!")
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                    sweetAlertDialog.dismissWithAnimation();
                                }
                            });
                    dialog.show();
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                t.getMessage();
                t.printStackTrace();
            }
        });
    }

    class SwipeViewHolder extends RecyclerView.ViewHolder{
        private TextView naslov,kolicina, datum, ukupno;
        private SwipeRevealLayout swipeRevealLayout;
        private LinearLayout linearLayout, linearDelete;
        private CheckBox potvrda;

        public SwipeViewHolder(@NonNull final View itemView){
            super(itemView);

            naslov = itemView.findViewById(R.id.naslovText);
            kolicina = itemView.findViewById(R.id.kolicinaText);
            datum = itemView.findViewById(R.id.datumText);
            ukupno = itemView.findViewById(R.id.cijenaText);
            potvrda = itemView.findViewById(R.id.checkBoxPotvrda);
            swipeRevealLayout = itemView.findViewById(R.id.swipeLayout);
            linearLayout = itemView.findViewById(R.id.linearSwipe);
            linearDelete = itemView.findViewById(R.id.linearSwipe2);
        }
    }
}
