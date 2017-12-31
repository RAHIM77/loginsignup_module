package com.example.stjais.carpoolkarle;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

public class WelcomeScreen extends AppCompatActivity {
    ImageView mylogo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_screen);
        mylogo=(ImageView)findViewById(R.id.logo);
        Animation myanim= AnimationUtils.loadAnimation(this,R.anim.mytrans);
        mylogo.setAnimation(myanim);
        Thread myThread=new Thread(){
            @Override
            public void run() {
                super.run();
                try {

                            sleep(5000);
                    Intent intent=new Intent(WelcomeScreen.this,GuideActivity.class);
                     startActivity(intent);
                    overridePendingTransition(R.anim.mytrans,R.anim.fi);
                    finish();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        myThread.start();
    }
}
