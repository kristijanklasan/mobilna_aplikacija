package android.unipu.theater.administrator;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.unipu.theater.R;
import android.unipu.theater.fragmenti.administrator.ImageGalleryFragment;
import android.unipu.theater.fragmenti.administrator.ImageLoaderFragment;
import android.view.View;

import com.google.android.material.floatingactionbutton.FloatingActionButton;


public class ImageGallery extends AppCompatActivity {

    private FloatingActionButton fabAdd;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_gallery);

        initView();
        setElements();
    }

    private void initView(){
        toolbar = (Toolbar) findViewById(R.id.toolbarGallery);
        fabAdd = (FloatingActionButton) findViewById(R.id.fabAddImage);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ImageGalleryFragment fragment = new ImageGalleryFragment();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fragmentImageGallery, fragment);
        ft.commit();
    }

    private void setElements(){
        fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImageLoaderFragment fragment = new ImageLoaderFragment();
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.fragmentImageGallery, fragment);
                ft.commit();
            }
        });

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ImageGallery.this, OfferActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
