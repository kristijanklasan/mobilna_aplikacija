package android.unipu.theater.adapter;

import android.content.Context;
import android.unipu.theater.R;
import android.unipu.theater.model.OfferModel;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class TicketsAdapter extends ArrayAdapter<OfferModel> {

    private ArrayList<OfferModel> arrayList;
    Context context;

    public TicketsAdapter(ArrayList<OfferModel> data, Context context){
        super(context, R.layout.row_tickets,data);
        this.arrayList = data;
        this.context = context;
    }

    private static class ViewHolder{
        TextView datum;
        TextView vrijeme;
        TextView danas;
    }

    private int lastPosition = -1;

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        final OfferModel offerModel = getItem(position);
        final ViewHolder viewHolder;

        if(convertView == null){
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_tickets,parent, false);
            viewHolder.datum = (TextView) convertView.findViewById(R.id.datumUlaznica);
            viewHolder.vrijeme = (TextView) convertView.findViewById(R.id.vrijemeUlaznica);
            viewHolder.danas = (TextView) convertView.findViewById(R.id.danasUlaznice);

            Date current = getCurrentDate();
            Date database = stringToDate(arrayList.get(position).getDatum_prikazivanja());
            if(current.equals(database)){
                viewHolder.danas.setVisibility(View.VISIBLE);
                viewHolder.danas.startAnimation(AnimationUtils.loadAnimation(getContext(),R.anim.anim_listview));
            }
            convertView.setTag(viewHolder);
        }
        else
            viewHolder = (TicketsAdapter.ViewHolder) convertView.getTag();

        lastPosition = position;
        viewHolder.datum.setText(offerModel.getDatum_prikazivanja());
        viewHolder.vrijeme.setText(offerModel.getVrijeme_prikazivanja()+"h");
        viewHolder.datum.startAnimation(AnimationUtils.loadAnimation(getContext(),R.anim.anim_listview));
        viewHolder.vrijeme.startAnimation(AnimationUtils.loadAnimation(getContext(),R.anim.anim_listview));
        return convertView;
    }

    private Date stringToDate(String date){
        Date dateFormat = null;
        try { dateFormat = new SimpleDateFormat("dd.MM.yyyy").parse(date);
        }catch(Exception e){ e.printStackTrace(); }
        return dateFormat;
    }

    private Date getCurrentDate(){
        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault());
        String format = simpleDateFormat.format(c);
        Date date = stringToDate(format);
        return date;
    }
}
