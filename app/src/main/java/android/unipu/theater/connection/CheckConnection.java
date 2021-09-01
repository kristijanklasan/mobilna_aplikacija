package android.unipu.theater.connection;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class CheckConnection {

    private boolean connection = false;
    private Context context;
    private ConnectivityManager connectivityManager;

    public CheckConnection(Context context, ConnectivityManager connectivityManager){
        this.context = context;
        this.connectivityManager = connectivityManager;
        checkNetworkConnection(connectivityManager);

    }

    public boolean checkNetworkConnection(ConnectivityManager connectivityManager){
        if(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState()== NetworkInfo.State.CONNECTED ||
        connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState()==NetworkInfo.State.CONNECTED){
            connection = true;
        }
        else {
            connection = false;
            alertInternet(connectivityManager);
        }
        return connection;
    }

    private void alertInternet(final ConnectivityManager connectivityManager){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Niste povezani s Internetom");
        builder.setMessage("Provjerite povezanost s Internetom!");
        builder.setPositiveButton("Ponovno pokreni", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                checkNetworkConnection(connectivityManager);
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
