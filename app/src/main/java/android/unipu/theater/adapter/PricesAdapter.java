package android.unipu.theater.adapter;

import android.unipu.theater.R;
import android.unipu.theater.model.PricesModel;
import android.unipu.theater.retrofit.IPrices;
import android.unipu.theater.retrofit.RetrofitClient;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import cn.pedant.SweetAlert.SweetAlertDialog;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class PricesAdapter extends RecyclerView.Adapter<PricesAdapter.ViewHolder> {

    private IPrices iPrices;
    private List<PricesModel> priceList;

    public PricesAdapter (List<PricesModel> priceList){
       this.priceList = priceList;
    }

    @NonNull
    @Override
    public PricesAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.prices_cardview ,viewGroup,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        final PricesModel pricesModel = priceList.get(position);
        String kn = " kn";

        try { String date = changeDate(pricesModel.getDatum());
            holder.datum.setText(date);}
        catch (Exception e) { e.printStackTrace(); }

        if(pricesModel.getCijeneDjeca()%1==0){
            holder.karte_djeca.setText(String.valueOf((Math.round(pricesModel.getCijeneDjeca())+kn)));
        }else holder.karte_djeca.setText(String.valueOf(pricesModel.getCijeneDjeca())+kn);

        if(pricesModel.getCijene_odrasli()%1==0) {
            holder.karte_odrasli.setText(String.valueOf(Math.round(pricesModel.getCijene_odrasli()) + kn));
        }
        else holder.karte_odrasli.setText(String.valueOf(pricesModel.getCijene_odrasli())+kn);

        if(pricesModel.getCijene_studenti()%1==0) {
            holder.karte_studenti.setText(String.valueOf(Math.round(pricesModel.getCijene_studenti())+kn));
        }else holder.karte_studenti.setText(String.valueOf(pricesModel.getCijene_studenti())+kn);

        if(pricesModel.getCijene_umirovljenici()%1==0) {
            holder.karte_umirovljenici.setText(String.valueOf(Math.round(pricesModel.getCijene_umirovljenici())+kn));
        }else holder.karte_umirovljenici.setText(String.valueOf(pricesModel.getCijene_umirovljenici())+kn);

        if(pricesModel.getDan()%1==0){
            holder.cijeneDan.setText(String.valueOf(Math.round(pricesModel.getDan())+kn));
        }else holder.cijeneDan.setText(String.valueOf(pricesModel.getDan())+kn);

        if(pricesModel.getPremijera()%1==0){
            holder.cijenePremijera.setText(String.valueOf(Math.round(pricesModel.getPremijera())+kn));
        }else holder.cijenePremijera.setText(String.valueOf(pricesModel.getPremijera())+kn);

        boolean isExpanded = pricesModel.isExpanded();
        holder.linearLayoutPrices2.setVisibility(isExpanded ? View.VISIBLE : View.GONE);

        holder.deleteButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                new SweetAlertDialog(holder.itemView.getContext(), SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("Jeste li sigurni?")
                        .setContentText("Jednom izbrisana cijena je zauvijek izbrisana!")
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
                                .setContentText("Cijena je izbrisana!")
                                .setConfirmText("U redu")
                                .setConfirmClickListener(null)
                                .showCancelButton(false)
                                .changeAlertType(SweetAlertDialog.SUCCESS_TYPE);

                        deletePrice(pricesModel.getId());
                        pricesModel.setExpanded(!pricesModel.isExpanded());
                        priceList.remove(position);
                        notifyItemRemoved(position);
                        notifyDataSetChanged();
                    }
                }).show();
            }
        });
    }

    public String changeDate(String date){
        String dateString = null;
        try { TimeZone zone = TimeZone.getTimeZone("UTC");
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
            SimpleDateFormat newFormat = new SimpleDateFormat("E, dd.MM.yyyy HH:mm");
            simpleDateFormat.setTimeZone(zone);
            Date newDate = simpleDateFormat.parse(date);
            dateString = newFormat.format(newDate);
            return dateString;
        }catch(Exception e){ e.printStackTrace(); }
        return dateString;
    }

    public void deletePrice(int id){
        Retrofit retrofitClient = RetrofitClient.getInstance2();
        iPrices = retrofitClient.create(IPrices.class);

        Call<ResponseBody> call = iPrices.deletePrices(id);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) { }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                t.printStackTrace();
                t.getMessage();
            }
        });
    }


    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getItemCount() { return priceList.size(); }

    public class ViewHolder extends RecyclerView.ViewHolder{

        Button deleteButton;
        TextView karte_djeca, karte_odrasli, karte_studenti, karte_umirovljenici,datum, cijeneDan, cijenePremijera;
        LinearLayout linearLayoutPrices1,linearLayoutDatum;
        LinearLayout linearLayoutPrices2;

        public ViewHolder(@NonNull final View itemView){
            super(itemView);

            datum = itemView.findViewById(R.id.textDatum);
            karte_djeca = itemView.findViewById(R.id.cijena1);
            karte_odrasli = itemView.findViewById(R.id.cijena2);
            karte_studenti = itemView.findViewById(R.id.cijena3);
            karte_umirovljenici = itemView.findViewById(R.id.cijena4);
            cijeneDan = itemView.findViewById(R.id.cijena5);
            cijenePremijera = itemView.findViewById(R.id.cijena6);
            linearLayoutPrices1 = itemView.findViewById(R.id.linearPrices);
            linearLayoutPrices2 = itemView.findViewById(R.id.linearLayoutPrices);
            linearLayoutDatum = itemView.findViewById(R.id.linearLayoutDatum);
            deleteButton = itemView.findViewById(R.id.deleteButton);

            linearLayoutDatum.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){
                    PricesModel pricesModel = priceList.get(getAdapterPosition());
                    pricesModel.setExpanded(!pricesModel.isExpanded());
                    notifyItemChanged(getAdapterPosition());
                }
            });
        }
    }
}
