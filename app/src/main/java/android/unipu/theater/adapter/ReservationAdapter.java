package android.unipu.theater.adapter;

import android.content.Context;
import android.os.Bundle;
import android.unipu.theater.R;
import android.unipu.theater.fragmenti.user.UserReservationDetail;
import android.unipu.theater.model.OfferModel;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ReservationAdapter extends RecyclerView.Adapter<ReservationAdapter.ViewHolder> {

    private LayoutInflater layoutInflater;
    private List<OfferModel> offerModelList;

    public ReservationAdapter(Context context, List<OfferModel> offerModelList){
        this.layoutInflater = LayoutInflater.from(context);
        this.offerModelList = offerModelList;
    }

    @NonNull
    @Override
    public ReservationAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.reservation_cardview, parent, false);
        return new ReservationAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReservationAdapter.ViewHolder holder, final int position) {
        Animation animation = AnimationUtils.
                loadAnimation(holder.itemView.getContext(), R.anim.item_anim_fall_right);
        holder.itemView.startAnimation(animation);
        holder.naziv.setText(offerModelList.get(position).getNaziv());
        holder.kategorija.setText(offerModelList.get(position).getKategorija());
        String mDatum = offerModelList.get(position).getDatum_prikazivanja()+" "+
                offerModelList.get(position).getVrijeme_prikazivanja()+"h";
        holder.datum.setText(mDatum);
        holder.kolicina.setText(String.valueOf(offerModelList.get(position).getKolicina()));
        holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserReservationDetail reservationDetail = new UserReservationDetail();
                Bundle bundle = new Bundle();
                bundle.putInt("id",offerModelList.get(position).getId());
                AppCompatActivity activity = (AppCompatActivity) v.getContext();
                reservationDetail.setArguments(bundle);
                activity.getSupportFragmentManager().beginTransaction()
                        .setCustomAnimations(R.anim.slide_out_right,R.anim.slide_out_left)
                        .replace(R.id.contentReservation,reservationDetail).commit();
            }
        });
    }

    @Override
    public int getItemCount() {
        return offerModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView naziv, kategorija, datum, kolicina;
        RelativeLayout relativeLayout;

        public ViewHolder(@NonNull View itemView){
            super(itemView);

            relativeLayout = (RelativeLayout) itemView.findViewById(R.id.relativeReservation);
            naziv = (TextView) itemView.findViewById(R.id.nazivTicketsAdapter);
            kategorija = (TextView) itemView.findViewById(R.id.kategorijaTicketsAdapter);
            datum = (TextView) itemView.findViewById(R.id.datumTicketsAdapter);
            kolicina = (TextView) itemView.findViewById(R.id.kolicinaTicketsAdapter);
        }
    }
}
