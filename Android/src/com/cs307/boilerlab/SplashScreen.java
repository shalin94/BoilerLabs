package com.cs307.boilerlab;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
 
public class SplashScreen extends Activity {
   /** Called when the activity is first created. */
   @Override
   public void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      overridePendingTransition(R.anim.slidein, R.anim.slideout);
      getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
      getActionBar().hide();
      setContentView(R.layout.splash);
      Thread splashThread = new Thread() {
         @Override
         public void run() {
            try {
               int waited = 0;
               while (waited < 3000) {
                  sleep(100);
                  waited += 100;
               }
            } catch (InterruptedException e) {
               // do nothing
            } finally {
               finish();
               Intent i = new Intent(SplashScreen.this,MainActivity.class);
               startActivity(i);
            }
         }
      };
      splashThread.start();
   }
   @Override
   protected void onStart() {
       // TODO Auto-generated method stub
       super.onStart();
       System.out.println("----main activity---onStart---");
       overridePendingTransition(R.anim.slidein, R.anim.slideout);
      }
}
