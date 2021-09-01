package android.unipu.theater.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.unipu.theater.R;
import android.unipu.theater.administrator.PerformanceDetail;
import android.unipu.theater.model.ImageModel;
import android.unipu.theater.retrofit.ITheater;
import android.unipu.theater.retrofit.RetrofitClient;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import cn.pedant.SweetAlert.SweetAlertDialog;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ImagePickerAdapter extends RecyclerView.Adapter<ImagePickerAdapter.ViewHolder> {

    private LayoutInflater layoutInflater;
    ArrayList<ImageModel> data;
    private ITheater iTheater;
    private ImageView imageView;
    private Activity activity;

    public ImagePickerAdapter(Activity activity, Context context, ArrayList<ImageModel> data){
        this.layoutInflater = LayoutInflater.from(context);
        this.data = data;
        this.activity = activity;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = layoutInflater.inflate(R.layout.item_gallery ,viewGroup,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ImagePickerAdapter.ViewHolder holder, final int position) {
        String imageUrl = data.get(position).getSlika();
        Picasso.get().load(imageUrl).placeholder(R.drawable.pozadina).fit().into(imageView);

        String title = data.get(position).getNaziv();
        holder.textNaslov.setText(title);

        holder.linearDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteShow(data.get(position).getId(),holder.itemView.getContext(),position);
                notifyItemChanged(position);
            }
        });

        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(v.getContext(), PerformanceDetail.class);
                i.putExtra("slika",data.get(position).getSlika());
                activity.setResult(Activity.RESULT_OK,i);
                activity.finish();
            }
        });
    }

    private void deleteShow(int id, final Context context, final int position){
        Retrofit retrofit = RetrofitClient.getInstance2();
        iTheater = retrofit.create(ITheater.class);

        Call<ResponseBody> call = iTheater.deleteImages(id);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.isSuccessful()){
                    new SweetAlertDialog(context, SweetAlertDialog.SUCCESS_TYPE)
                            .setContentText("Slika je izbrisana!")
                            .setConfirmButton("U redu",null)
                            .show();
                    data.remove(position);
                    notifyItemRemoved(position);
                }else {
                    new SweetAlertDialog(context, SweetAlertDialog.ERROR_TYPE)
                            .setContentText("Slika nije izbrisana!")
                            .setConfirmButton("U redu",null)
                            .show();
                }
            }

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
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView textNaslov;
        ImageView imageDelete;
        LinearLayout linearLayout,linearDelete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(v.getContext(), PerformanceDetail.class);
                    i.putExtra("slika",data.get(getAdapterPosition()).getSlika());
                    activity.setResult(Activity.RESULT_OK,i);
                    activity.finish();
                }
            });

            linearLayout = itemView.findViewById(R.id.linearSwipeGallery);
            linearDelete = itemView.findViewById(R.id.deleteShowImage);
            textNaslov = itemView.findViewById(R.id.naslovPredstavaGallery);
            imageView = itemView.findViewById(R.id.pozadinaImageGallery);
        }
    }
}
