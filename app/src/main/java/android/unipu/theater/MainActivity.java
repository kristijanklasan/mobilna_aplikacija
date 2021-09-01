package android.unipu.theater;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.unipu.theater.user.UserLogin;

import yanzhikai.textpath.SyncTextPathView;
import yanzhikai.textpath.painter.ArrowPainter;

public class MainActivity extends AppCompatActivity {

    private SyncTextPathView textTitle,textContinue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textTitle = findViewById(R.id.textTitle);
        textTitle.setPathPainter(new ArrowPainter());
        textTitle.startAnimation(0,1);

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                textContinue = findViewById(R.id.textTitle2);
                textContinue.setPathPainter(new ArrowPainter());
                textContinue.setFillColor(true);
                textContinue.startAnimation(0,1);
            }
        }, 5200);

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(MainActivity.this, UserLogin.class);
                startActivity(intent);
            }
        },9000);
    }
}
