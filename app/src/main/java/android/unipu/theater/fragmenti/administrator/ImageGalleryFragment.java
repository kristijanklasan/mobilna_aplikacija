package android.unipu.theater.fragmenti.administrator;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.unipu.theater.R;
import android.unipu.theater.adapter.ImagePickerAdapter;
import android.unipu.theater.model.ImageModel;
import android.unipu.theater.retrofit.ITheater;
import android.unipu.theater.retrofit.RetrofitClient;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ImageGalleryFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private ImagePickerAdapter adapter;
    private ArrayList<ImageModel> data;
    private ITheater iTheater;
    private RecyclerView recyclerImages;
    private String mParam1;
    private String mParam2;
    private ImageModel imageModel;

    public ImageGalleryFragment() {
        // Required empty public constructor
    }


    public static ImageGalleryFragment newInstance(String param1, String param2) {
        ImageGalleryFragment fragment = new ImageGalleryFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.
                inflate(R.layout.fragment_image_gallery, container, false);

        initView(view);
        getImages();
        return view;
    }

    private void initView(View view){
        imageModel = new ImageModel();
        recyclerImages = (RecyclerView) view.findViewById(R.id.recyclerImageGallery);
        recyclerImages.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    private void getImages(){
        Retrofit retrofit = RetrofitClient.getInstance2();
        iTheater = retrofit.create(ITheater.class);

        data = new ArrayList<>();
        Call<List<ImageModel>> call = iTheater.getImages();
        call.enqueue(new Callback<List<ImageModel>>() {
            @Override
            public void onResponse(Call<List<ImageModel>> call, Response<List<ImageModel>> response) {
                if(response.isSuccessful()){
                    List<ImageModel> list = response.body();

                    for(int i=0; i<list.size(); i++){
                        imageModel = new ImageModel();
                        imageModel.setId(list.get(i).getId());
                        imageModel.setNaziv(list.get(i).getNaziv());
                        imageModel.setSlika(list.get(i).getSlika());
                        data.add(imageModel);
                    }
                    try {
                        adapter = new ImagePickerAdapter(getActivity(),getContext(),data);
                        recyclerImages.setAdapter(adapter);
                        recyclerImages.scheduleLayoutAnimation();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<List<ImageModel>> call, Throwable t) {
                t.printStackTrace();
                t.getMessage();
            }
        });
    }
}
