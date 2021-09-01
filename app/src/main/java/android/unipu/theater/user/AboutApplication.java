package android.unipu.theater.user;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.unipu.theater.BuildConfig;
import android.unipu.theater.R;
import android.view.View;
import android.widget.TextView;

public class AboutApplication extends AppCompatActivity {

    private TextView deviceText, sdkText, numberText, versionText, connectionText;
    private Boolean wifiConnected,mobileConnected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_application);

        initView();
    }

    private void initView(){

        deviceText = findViewById(R.id.deviceText);
        sdkText = findViewById(R.id.sdkVersion);
        numberText = findViewById(R.id.brojUredaja);
        versionText = findViewById(R.id.verzijaOS);
        connectionText = findViewById(R.id.povezivanjeTekst);

        deviceText.setText(getDeviceName());
        String version = android.os.Build.VERSION.RELEASE;
        versionText.setText(version);

        int versionSdk = android.os.Build.VERSION.SDK_INT;
        sdkText.setText(String.valueOf(versionSdk));
        checkNetworkConnection();

        int versionCode = BuildConfig.VERSION_CODE;
        numberText.setText(String.valueOf(versionCode));

        Toolbar toolbar = findViewById(R.id.toolbarAbout);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AboutApplication.this, MainWindow.class);
                startActivity(intent);
                finish();
            }
        });
    }

    public static String getDeviceName() {
        String manufacturer = Build.MANUFACTURER;
        String model = Build.MODEL;
        if (model.startsWith(manufacturer)) {
            return model;
        }
        String device = manufacturer + " " + model;
        return device;
    }


    private void checkNetworkConnection(){

        ConnectivityManager manager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();

        if(networkInfo!=null && networkInfo.isConnected()){
            wifiConnected = networkInfo.getType() == ConnectivityManager.TYPE_WIFI;
            mobileConnected = networkInfo.getType() == ConnectivityManager.TYPE_MOBILE;

            if(wifiConnected)
                connectionText.setText("Wireless");
            else if(mobileConnected)
                connectionText.setText("Mobilni podaci");
        }
        else{
            connectionText.setText("Nije povezano!");
        }
    }
}
