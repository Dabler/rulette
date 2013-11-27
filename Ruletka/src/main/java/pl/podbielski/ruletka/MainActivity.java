package pl.podbielski.ruletka;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.os.Build;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Date;
import java.util.Random;
import java.util.Vector;

public class MainActivity extends ActionBarActivity implements SensorEventListener {
    private SensorManager sensorManager;






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_UI);
    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case R.id.action_settings:
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void GetAccelerometerData()
    {

    }
    Random r=new Random();
    private MediaPlayer player;
    boolean isLoaded = false;
    long TimeNew;
    long TimeOld;
    long delay;
    //do kręcenia bębenkiem
    int Zmin = 0;
    int Zmax = 0;
    float ZAxis;
    boolean ZIsFlipped = false;
    Vector<Pair<Float, Long>> Zvector = new Vector<Pair<Float, Long>>();

    //do spustu
    int Ymin = 0;
    int Ymax = 0;
    float YAxis;
    boolean YIsFlipped = false;
    Vector<Pair<Float, Long>> Yvector = new Vector<Pair<Float, Long>>();

    private final static int SIZE = 10;

    @Override
    public synchronized void onSensorChanged(SensorEvent sensorEvent)
    {
        Log.d("tag","data changed" );
        if(sensorEvent.sensor.getType() == Sensor.TYPE_ACCELEROMETER)
        {


            synchronized (this) {
                switch (sensorEvent.sensor.getType()) {

                    case Sensor.TYPE_ACCELEROMETER:
                        TimeNew = sensorEvent.timestamp;
                        delay = (long)((TimeNew - TimeOld)/1000000);
                        TimeOld = TimeNew;
                        Log.d("Test", delay + " ms");
                        break;
                }
            }
            //ISFLIPPED
            ZAxis = sensorEvent.values[2];          //zcztywanie z osi z
            Pair<Float, Long> ZActualPair = Pair.create(ZAxis, new Date().getTime());
            if(Zvector.size()>9)
            {
                Zvector.removeElementAt(0);
            }
            Zvector.add(ZActualPair);

            for(int i = 0; i<Zvector.size(); i++)
            {
                if(Zvector.get(i).first < Zvector.get(Zmin).first)
                    Zmin = i;
                if(Zvector.get(i).first > Zvector.get(Zmax).first)
                    Zmax = i;
            }
            if(Math.abs(Zvector.get(Zmin).second - Zvector.get(Zmax).second) < 600)     //warunek sprawdzajacy
            {                                                                       //ZIsFlipped jest true gdy czas flipa jest mniejszy niz 600ms i gdy amplituda > |6|
                if(Zvector.get(Zmax).first>6 && Zvector.get(Zmin).first<-6)
                {
                    ZIsFlipped = true;
                    Log.d("Z", ""+ZIsFlipped);
                }
                else
                {
                    ZIsFlipped = false;
                    Log.d("Z", ""+ZIsFlipped);
                }
            }

            //PULLTRIGGER
            YAxis = sensorEvent.values[1];          //zcztywanie z osi y
            Pair<Float, Long> YActualPair = Pair.create(YAxis, new Date().getTime());
            if(Yvector.size()>9)
            {
                Yvector.removeElementAt(0);
            }
            Yvector.add(YActualPair);

            for(int i = 0; i<Yvector.size(); i++)
            {
                if(Yvector.get(i).first < Yvector.get(Ymin).first)
                    Ymin = i;
                if(Yvector.get(i).first > Yvector.get(Ymax).first)
                    Ymax = i;
            }

            if(Math.abs(Yvector.get(Ymin).second - Yvector.get(Ymax).second) < 400)     //warunek sprawdzajacy
            {                                                                           //YIsFlipped jest true gdy czas flipa jest mniejszy niz 600ms i gdy amplituda > |6|
                if(Yvector.get(Ymax).first>6 && Yvector.get(Ymin).first<2)
                {
                    YIsFlipped = true;
                    Log.d("Y", ""+YIsFlipped);
                }
                else
                {
                    YIsFlipped = false;
                    Log.d("Y", ""+YIsFlipped);
                }
            }


//           Log.d("isLoaded", "isLoaded przed załadowaniem:" + isLoaded);
//           if(ZIsFlipped && !isLoaded )
//           {
//               player = MediaPlayer.create(this, R.raw.guncocking);
//               player.start();
//               Log.d("pif", "załadowano");
//               isLoaded = true;
//               ZIsFlipped = false;
//           }
//          //  long timesDifference = timeBeforeLoad - (new Date().getTime());
//          //  Log.d("pif", "timesDifference"+timesDifference);
//            //android.os.SystemClock.sleep(200);
//            if(YIsFlipped && isLoaded)
//            {
//                int losowa = r.nextInt()%2;
//                if(losowa == 0)
//                {
//                player = MediaPlayer.create(this, R.raw.shot);
//                player.start();
//                Log.d("pif", "wystrzel");
//                isLoaded = false;
//                Log.d("isLoaded", "isLoaded traf: "+isLoaded);
//                }
//                if(losowa == 1)
//                {
//                    player = MediaPlayer.create(this, R.raw.guntrigger);
//                    player.start();
//                    Log.d("pif", "pudło");
//                    isLoaded = false;
//                    Log.d("isLoaded", "isLoaded pudło: "+isLoaded);
//                }
//                //strzela za duzo razy
//                //moze cos z czasem wykombinowac
//                //???
//                YIsFlipped = false;
//                //Pair<Float, Long> zerowaPara = Pair.create(0f, 0l);
//                Zvector.clear();
//                Yvector.clear();
//            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }


}
