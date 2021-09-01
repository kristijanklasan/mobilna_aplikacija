package android.unipu.theater.adapter;

import android.content.Context;
import android.unipu.theater.R;
import android.unipu.theater.model.OfferModel;
import android.unipu.theater.retrofit.IOffer;
import android.unipu.theater.retrofit.RetrofitClient;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import cn.pedant.SweetAlert.SweetAlertDialog;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class OfferAdapter extends ArrayAdapter<OfferModel>  {

    private ArrayList<OfferModel> arraySet;
    Context context;
    private IOffer iOffer;

    public OfferAdapter(ArrayList<OfferModel> data, Context context) {
        super(context, R.layout.row_offer_item,data);
        this.arraySet = data;
        this.context = context;
    }

    private static class ViewHolder{
        TextView textNaziv;
        TextView textKategorija;
        TextView textDatum;
        TextView textVrijeme;
        ImageView imageDelete;
        ImageView imageIcon;
    }

    private int lastPosition = -1;

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        final OfferModel offerModel = getItem(position);
        final ViewHolder viewHolder;

        if(convertView == null){
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_offer_item ,parent,false);
            viewHolder.textNaziv = (TextView) convertView.findViewById(R.id.naslovOffer);
            viewHolder.textKategorija = (TextView) convertView.findViewById(R.id.kategorijaOffer);
            viewHolder.textDatum = (TextView) convertView.findViewById(R.id.datumOffer);
            viewHolder.textVrijeme = (TextView) convertView.findViewById(R.id.vrijemeOffer);
            viewHolder.imageDelete = (ImageView) convertView.findViewById(R.id.rowDelete);
            viewHolder.imageIcon = (ImageView) convertView.findViewById(R.id.imageIcon);
            convertView.setTag(viewHolder);
        }
        else
            viewHolder = (ViewHolder) convertView.getTag();

        lastPosition = position;
        viewHolder.textNaziv.setText(offerModel.getNaziv());
        viewHolder.textKategorija.setText(offerModel.getKategorija());
        viewHolder.textDatum.setText(offerModel.getDatum_prikazivanja());
        viewHolder.textVrijeme.setText("Vrijeme: "+offerModel.getVrijeme_prikazivanja()+" h");
        viewHolder.imageDelete.setTag(position);

        viewHolder.imageIcon.startAnimation(AnimationUtils.loadAnimation(getContext(),R.anim.anim_listview));
        viewHolder.imageDelete.startAnimation(AnimationUtils.loadAnimation(getContext(),R.anim.anim_listview));
        viewHolder.textNaziv.startAnimation(AnimationUtils.loadAnimation(getContext(),R.anim.anim_listview));
        viewHolder.textKategorija.startAnimation(AnimationUtils.loadAnimation(getContext(),R.anim.anim_listview));
        viewHolder.textDatum.startAnimation(AnimationUtils.loadAnimation(getContext(),R.anim.anim_listview));
        viewHolder.textVrijeme.startAnimation(AnimationUtils.loadAnimation(getContext(),R.anim.anim_listview));

        viewHolder.imageDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new SweetAlertDialog(viewHolder.imageDelete.getContext(), SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("Jeste li sigurni?")
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
                        deleteOffer(offerModel);
                        arraySet.remove(offerModel);
                        notifyDataSetChanged();
                    }
                }).show();
            }
        });
        return convertView;
    }

    private void deleteOffer(OfferModel offerModel){
        Retrofit retrofitClient = RetrofitClient.getInstance2();
        iOffer = retrofitClient.create(IOffer.class);

        Call<ResponseBody> call = iOffer.deleteOffer(offerModel.getId());
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
}
