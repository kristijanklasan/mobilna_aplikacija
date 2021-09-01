package android.unipu.theater.adapter;

import android.content.Context;
import android.content.Intent;
import android.unipu.theater.R;
import android.unipu.theater.administrator.PerformanceDetail;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.squareup.picasso.Picasso;

import java.util.List;

public class PerformanceAdapter extends RecyclerView.Adapter<PerformanceAdapter.ViewHolder> {

    private LayoutInflater layoutInflater;
    private List<String> naslov;
    private List<String> slike;
    private List<Integer> id;
    private ImageView imageView;

    public PerformanceAdapter(Context context, List<String> slike, List<String> naslov, List<Integer> id){
        this.layoutInflater = LayoutInflater.from(context);
        this.slike = slike;
        this.naslov = naslov;
        this.id = id;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = layoutInflater.inflate(R.layout.cardview ,viewGroup,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String imageUrl = slike.get(position);
        Picasso.get().load(imageUrl).placeholder(R.drawable.pozadina).fit().into(imageView);

        String title = naslov.get(position);
        holder.textNaslov.setText(title);
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

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(v.getContext(), PerformanceDetail.class);
                    i.putExtra("id",id.get(getAdapterPosition()));
                    v.getContext().startActivity(i);
                }
            });

            textNaslov = itemView.findViewById(R.id.naslovPredstava);
            imageView = itemView.findViewById(R.id.pozadinaImage);
        }
    }
}
