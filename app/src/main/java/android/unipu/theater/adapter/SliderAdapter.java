package android.unipu.theater.adapter;
import android.content.Context;
import android.unipu.theater.R;
import android.unipu.theater.model.ImageModel;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.smarteist.autoimageslider.SliderViewAdapter;

import java.util.ArrayList;
import java.util.List;

public class SliderAdapter extends SliderViewAdapter<SliderAdapter.SliderAdapterViewHolder>  {

    private final List<ImageModel> imageSlider;

    public SliderAdapter(Context context, ArrayList<ImageModel> dataSlider){
        this.imageSlider = dataSlider;
    }

    @Override
    public SliderAdapter.SliderAdapterViewHolder onCreateViewHolder(ViewGroup parent) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.image_slider_layout,null);
        return new SliderAdapterViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(SliderAdapterViewHolder viewHolder, final int position) {
        final ImageModel sliderItem = imageSlider.get(position);
        Glide.with(viewHolder.itemView).load(sliderItem.getImageUrl())
                .fitCenter().into(viewHolder.imageViewPoster);
    }

    @Override
    public int getCount() {
        return imageSlider.size();
    }

    static class SliderAdapterViewHolder extends SliderViewAdapter.ViewHolder{

        View itemView;
        ImageView imageViewPoster;

        public SliderAdapterViewHolder(View itemView){
            super(itemView);
            imageViewPoster = itemView.findViewById(R.id.updateImageAdmin);
            this.itemView = itemView;
        }
    }
}
