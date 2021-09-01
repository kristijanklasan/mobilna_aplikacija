package android.unipu.theater.adapter;

import android.content.Context;
import android.content.Intent;
import android.unipu.theater.R;
import android.unipu.theater.model.TheaterModel;
import android.unipu.theater.user.ShowDetail;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class UserPerformanceAdapter extends RecyclerView.Adapter<UserPerformanceAdapter.ViewHolder> implements Filterable {

    private LayoutInflater layoutInflater;
    ArrayList<TheaterModel> naslov;
    private ImageView imageView;
    private CustomFilter filter;
    ArrayList<TheaterModel> filterList;

    public UserPerformanceAdapter(Context context, ArrayList<TheaterModel> naslov){
        this.layoutInflater = LayoutInflater.from(context);
        this.naslov = naslov;
        this.filterList = naslov;
    }

    @NonNull
    @Override
    public UserPerformanceAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = layoutInflater.inflate(R.layout.cardview ,viewGroup,false);
        return new UserPerformanceAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final UserPerformanceAdapter.ViewHolder holder, int position) {
        holder.textNaslov.setText(naslov.get(position).getNaziv());
        Picasso.get().load(naslov.get(position).getSlika1()).
                placeholder(R.drawable.pozadina).fit().into(imageView);

        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(v.getContext(), ShowDetail.class);
                i.putExtra("id", naslov.get(holder.getAdapterPosition()).getId());
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                v.getContext().startActivity(i);
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
    public int getItemCount() {
        return naslov.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView textNaslov;
        ConstraintLayout layout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            setIsRecyclable(false);
            layout = itemView.findViewById(R.id.layout);
            textNaslov = itemView.findViewById(R.id.naslovPredstava);
            imageView = itemView.findViewById(R.id.pozadinaImage);
        }
    }

    @Override
    public Filter getFilter() {
        if(filter==null)
            filter=new CustomFilter(filterList,this);
        return filter;
    }
}
