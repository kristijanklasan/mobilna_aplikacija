package android.unipu.theater.adapter;

import android.content.Context;
import android.content.Intent;
import android.unipu.theater.R;
import android.unipu.theater.model.FavoritesModel;
import android.unipu.theater.user.ShowDetail;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import java.util.List;

public class FavoritesAdapter extends RecyclerView.Adapter<FavoritesAdapter.ViewHolder> {

    private LayoutInflater layoutInflater;
    private List<FavoritesModel> favoritesModelList;
    private ImageView imageView;

    public FavoritesAdapter(Context context, List<FavoritesModel> favoritesModelList){
        this.layoutInflater = LayoutInflater.from(context);
        this.favoritesModelList = favoritesModelList;
    }

    @NonNull
    @Override
    public FavoritesAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.cardview_favorites,parent, false);
        return new FavoritesAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final FavoritesAdapter.ViewHolder holder, final int position) {
        Animation animation = AnimationUtils.
                loadAnimation(holder.itemView.getContext(), R.anim.item_anim_fall_down);
        holder.itemView.startAnimation(animation);
        holder.textNaslov.setText(favoritesModelList.get(position).getNaziv());
        holder.textKategorija.setText(favoritesModelList.get(position).getKategorija());
        imageView.setImageDrawable (null);
        Picasso.get().load(favoritesModelList.get(position).getSlika1()).fit().into(imageView);

        holder.constraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(v.getContext(), ShowDetail.class);
                i.putExtra("id", favoritesModelList.get(holder.getAdapterPosition()).getId_predstava());
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                v.getContext().startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return favoritesModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView textNaslov, textKategorija;
        ConstraintLayout constraintLayout;
        public ViewHolder (@NonNull View itemView){
            super(itemView);

            constraintLayout = (ConstraintLayout) itemView.findViewById(R.id.layoutFavorites);
            textNaslov = itemView.findViewById(R.id.naslovPredstavaFavorites);
            textKategorija = itemView.findViewById(R.id.textKategorija);
            imageView = itemView.findViewById(R.id.pozadinaImageFavorites);
        }
    }
}
