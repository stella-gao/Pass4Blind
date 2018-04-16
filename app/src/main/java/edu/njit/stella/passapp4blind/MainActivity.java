package edu.njit.stella.passapp4blind;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Path;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationManager;
import android.speech.tts.TextToSpeech;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderApi;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;


import android.app.Activity;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;

import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;

import edu.njit.stella.passapp4blind.MessageID;
import edu.njit.stella.passapp4blind.SensorUtil;

import static java.lang.Math.abs;


public class MainActivity extends AppCompatActivity  implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        com.google.android.gms.location.LocationListener,
        AnimationListener {


    private Button ourButton;
    private TextView ourMessage;
    private TextView ourCompass;
    private TextView ourDegree;
    private int numTimesClicked = 0; //Initialize the counter and set to default zero
    TextToSpeech t1, t2, t3;



    private TextView ourPos, newPos;
    private LocationManager locationManager;
    private String provider;

    private SensorManager mSensorManager;

    ////////////////////////////////////////////
    private static final String TAG2 = "LocationActivity";
    private static final long INTERVAL = 1000 * 90; //90 seconds
    private static final long FASTEST_INTERVAL = 1000 * 45;// 45 seconds
    //Button btnFusedLocation;
    //TextView tvLocation;
    LocationRequest mLocationRequest;
    GoogleApiClient mGoogleApiClient;
    Location mCurrentLocation;
    String mLastUpdateTime;
    private FusedLocationProviderApi fusedLocationProviderApi = LocationServices.FusedLocationApi;


    private SensorUtil sensorUtil;
    public static boolean isSensorSuccess;
    private static String direction = null;

    Timer t = new Timer();




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG2, "onCreate ...............................");
        setContentView(R.layout.activity_main);  // setContentView(R.layout.activity_compass);
        sensorUtil = new SensorUtil(this, mHandler);


        ourButton = (Button) findViewById(R.id.button);
        ourMessage = (TextView) findViewById(R.id.textView);
        ourCompass = (TextView) findViewById(R.id.compass);
        ourDegree = (TextView) findViewById(R.id.degree);
        ////////////////////////////

        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);

        t2 = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status != TextToSpeech.ERROR) {
                    t2.setLanguage(Locale.US);
                }
            }
        });




///////////////////////////////////////////////////////////////////////

        //show error dialog if GoolglePlayServices not available
        if (!isGooglePlayServicesAvailable()) {
            finish();
        }
        createLocationRequest();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();





        View.OnClickListener ourOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                numTimesClicked = numTimesClicked + 1;


                /*
                String result = "The button got tapped " + numTimesClicked + " time";
                if (numTimesClicked != 1) { //boolean expression so that when button is pressed 1 time the message will reflect grammatically
                    result +="s..."; //When button is tapped more than once, an "s" will be at the end of the word "times"
                }*/


                switch (numTimesClicked) {
                    case 0:

                      /*  t1.speak("You are facing " +
                                        ourCompass.getText().toString() + "...................." +
                                        "Please click on the screen, " +
                                        "once for East, " +
                                        "twice for West, " +
                                        "three times for South " +
                                        "and four times for North!" +
                                        "If you have selected, then shake the phone!",
                                TextToSpeech.QUEUE_FLUSH, null);*/

                        break;
                    case 1:
                        ourMessage.setText("East");
                        t1.speak("east", TextToSpeech.QUEUE_FLUSH, null);
                        direction = "East";
                        /*if (isSensorSuccess) {
                            direction = "East";
                            t1.speak("You have shaked.", TextToSpeech.QUEUE_FLUSH, null);
                            Log.d("shake", "You have shaked.");
                            Toast.makeText(MainActivity.this, direction+" "+ourCompass.getText().toString(), Toast.LENGTH_LONG).show();
                            while (ourCompass.getText().toString() != direction) {
                                t1.speak("Turn Left!", TextToSpeech.QUEUE_FLUSH, null);
                            }
                        }*/

                        break;
                    case 2:
                        ourMessage.setText("West");
                        t1.speak("west", TextToSpeech.QUEUE_FLUSH, null);
                        direction = "West";/*
                        if (isSensorSuccess) {
                            direction = "West";
                            t1.speak("You have shaked.", TextToSpeech.QUEUE_FLUSH, null);
                            Log.d("shake", "You have shaked.");

                        }*/
                        break;
                    case 3:
                        ourMessage.setText("South");
                        t1.speak("south", TextToSpeech.QUEUE_FLUSH, null);
                        direction = "South";/*
                        if (isSensorSuccess) {
                            direction = "South";
                            t1.speak("You have shaked.", TextToSpeech.QUEUE_FLUSH, null);
                            Log.d("shake", "You have shaked.");

                        }*/
                        break;
                    case 4:
                        ourMessage.setText("North");
                        t1.speak("north", TextToSpeech.QUEUE_FLUSH, null);
                        direction = "North";/*
                        if (isSensorSuccess) {
                            direction = "North";
                            t1.speak("You have shaked.", TextToSpeech.QUEUE_FLUSH, null);
                            Log.d("shake", "You have shaked.");

                        }*/
                        break;
                    default:
                        ourMessage.setText("Error! Tap again!");
                        t1.speak("Error! Tap again!", TextToSpeech.QUEUE_FLUSH, null);
                        numTimesClicked = 0;
                }
                //ourMessage.setText(result);
            }

        };
        ourButton.setOnClickListener(ourOnClickListener);

        t1 = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status != TextToSpeech.ERROR) {
                    t1.setLanguage(Locale.US);
                }
            }
        });
    }




/*
        if(isSensorSuccess) {
            Toast.makeText(this, "You have shaked.", Toast.LENGTH_LONG).show();
            t1.speak("You have shaked.", TextToSpeech.QUEUE_FLUSH, null);
            Log.d("shake", "You have shaked.");
        } else{
            Log.d("shake", "Nothing.");
        }


    }*/
/*
    public void turningEast(String direction) {
        facing = ourCompass.getText().toString();
        switch(facing) {
            case "East":
                break;
            case "West":
                break;
            case "South":
                break;
            case "North":
                break;
        }


    }
    */



    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(INTERVAL);
        mLocationRequest.setFastestInterval(FASTEST_INTERVAL);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.


        int id = item.getItemId();

        switch (id) {
            case R.id.action_settings:
                //Toast toastMessage = Toast.makeText(this, "Text value is now " + ourMessage.getText(), Toast.LENGTH_LONG); //*gets* the value of the message and displays it on the screen
                Toast toastMessage = Toast.makeText(this, "You can set the geocode of intersection.", Toast.LENGTH_LONG);
                toastMessage.show();
                numTimesClicked = 0; //reset the counter

                updateUI();//////////////////////////////////

                TableLayout settingForm = (TableLayout) getLayoutInflater().inflate(R.layout.setting, null);

                AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);
                alertDialog.setTitle("Geocode Setting");

                alertDialog.setView(settingForm);



                ourPos= (TextView) settingForm.findViewById(R.id.pos);
                newPos= (TextView) settingForm.findViewById(R.id.newpos);

                final Location loc1 = new Location("");
                loc1.setLatitude(mCurrentLocation.getLatitude());
                loc1.setLongitude(mCurrentLocation.getLongitude());

                Location loc0 = new Location("");
                loc0.setLatitude(40.744720);
                loc0.setLongitude(-74.179824);

                final double distanceInMeters0 = loc1.distanceTo(loc0);


                String lat = String.valueOf(mCurrentLocation.getLatitude());
                String lng = String.valueOf(mCurrentLocation.getLongitude());
                ourPos.setText("\nCurrent Latitude: " + lat + "\n" +
                        "Current Longitude: " + lng + "\n" +
                        "Accuracy: " + mCurrentLocation.getAccuracy()+ "\n" +
                        "Distance to intersection by default is: "  + distanceInMeters0 + " meters."
                        );
/*
                if(distanceInMeters0 <= 50.0){
                    t2.speak("You are facing " +
                                    ourCompass.getText().toString() + "...................." +
                                    "Please click on the screen, " +
                                    "once for East, " +
                                    "twice for West, " +
                                    "three times for South " +
                                    "and four times for North!" +
                                    "If you have selected, then shake the phone!",
                            TextToSpeech.QUEUE_FLUSH, null);
                }
*/

                final EditText[] editTexts = new EditText[2];
                editTexts[0] = (EditText) settingForm.findViewById(R.id.lat);
                editTexts[1] = (EditText) settingForm.findViewById(R.id.lng);



                alertDialog.setIcon(R.drawable.maps);

                alertDialog.setPositiveButton("Set Geocode",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                //EditText et = (EditText)settingForm.findViewById(R.id.GoalChooser);
                                //EditText elat = (EditText) settingForm.findViewById(R.id.lat);
                                double nlat = Double.parseDouble(editTexts[0].getText().toString());
                                double nlng = Double.parseDouble(editTexts[1].getText().toString());

                                Location loc2 = new Location("");
                                loc2.setLatitude(nlat);
                                loc2.setLongitude(nlng);

                                final double distanceInMeters = loc1.distanceTo(loc2);

                                Toast.makeText(getApplicationContext(),"Distance to the new intersection is "+
                                        Double.toString(distanceInMeters)+" meters", Toast.LENGTH_SHORT).show();


                                //newPos.setText("Distance to the new intersection is "+ Double.toString(distanceInMeters)+" meters");

                                //globalVariable = nlat;
                                Log.i("INFO",Double.toString(nlat));
                                Log.i("INFO",Double.toString(nlng));

                                dialog.dismiss();
                            }
                        });
                alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });



/*
                alertDialog.setPositiveButton("YES",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                String latStr = elat.getText().toString();
                                String lngStr = elng.getText().toString();
                                //if (latStr.equals(null)) {
                                    Toast.makeText(getApplicationContext(),
                                            latStr, Toast.LENGTH_SHORT).show();
                                //}

                                //if (lngStr.equals(null)) {
                                    Toast.makeText(getApplicationContext(),
                                            lngStr, Toast.LENGTH_SHORT).show();
                                //}
                            }
                        });

                alertDialog.setNegativeButton("NO",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });*/

                alertDialog.show();

                return true;

            //similarly write for other actions

            default:
                return super.onOptionsItemSelected(item);
        }


    }

    private boolean isGooglePlayServicesAvailable() {
        int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (ConnectionResult.SUCCESS == status) {
            return true;
        } else {
            GooglePlayServicesUtil.getErrorDialog(status, this, 0).show();
            return false;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        Log.d(TAG2, "onStart fired ..............");
        mGoogleApiClient.connect();
    }

    @Override
    protected void onPause() {

        if(t1 !=null){
            t1.stop();
            t1.shutdown();
        }
        mSensorManager.unregisterListener(mSensorEventListener);
        super.onPause();
        if(mGoogleApiClient.isConnected())
            stopLocationUpdates();

        if(sensorUtil != null){
            sensorUtil.unRegeditListener();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mSensorManager.registerListener(mSensorEventListener, mSensorManager
                        .getDefaultSensor(Sensor.TYPE_ORIENTATION),
                SensorManager.SENSOR_DELAY_NORMAL);

        if (mGoogleApiClient.isConnected()) {
            startLocationUpdates();
            Log.d(TAG2, "Location update resumed .....................");
        }

        if(sensorUtil != null){
            sensorUtil.regeditListener();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();

        Log.d(TAG2, "onStop fired ..............");
        mGoogleApiClient.disconnect();
        Log.d(TAG2, "isConnected ...............: " + mGoogleApiClient.isConnected());
    }


    protected void onDestroy(){
        super.onDestroy();
        t.cancel();
    }


    @Override
    public void onLocationChanged(Location location) {
        Log.d(TAG2, "Firing onLocationChanged..............................................");
        mCurrentLocation = location;
        mLastUpdateTime = DateFormat.getTimeInstance().format(new Date());
        updateUI();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Log.d(TAG2, "onConnected - isConnected ...............: " + mGoogleApiClient.isConnected());
        startLocationUpdates();
    }

    private void startLocationUpdates() {
        try {
            PendingResult<Status> pendingResult = LocationServices.FusedLocationApi.requestLocationUpdates(
                    mGoogleApiClient, mLocationRequest, this);
        }catch (SecurityException e) {
            Log.e("PERMISSION_EXCEPTION","PERMISSION_NOT_GRANTED");
        }
        Log.d(TAG2, "Location update started ..............: ");
    }



    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d(TAG2, "Connection failed: " + connectionResult.toString());
    }



    private void updateUI() {
        Log.d(TAG2, "UI update initiated .............");

        if (null != mCurrentLocation) {
            String lat = String.valueOf(mCurrentLocation.getLatitude());
            String lng = String.valueOf(mCurrentLocation.getLongitude());
          /*  Toast.makeText(this, "At Time: " + mLastUpdateTime + "\n" +
                    "Latitude: " + lat + "\n" +
                    "Longitude: " + lng + "\n" +
                    "Accuracy: " + mCurrentLocation.getAccuracy() + "\n" +
                    "Provider: " + mCurrentLocation.getProvider(), Toast.LENGTH_LONG).show(); */

            final Location l1 = new Location("");
            l1.setLatitude(mCurrentLocation.getLatitude());
            l1.setLongitude(mCurrentLocation.getLongitude());

            final Location l2 = new Location("");
            l2.setLatitude(40.744720);
            l2.setLongitude(-74.179824);

            final double distanceInMeters0 = l1.distanceTo(l2);


            if(distanceInMeters0 <= 35.0){ // 35 meters
                t2.speak("You are around the intersection, facing " +
                                ourCompass.getText().toString() + "...................." +
                                "Please click on the screen, " +
                                "once for East, " +
                                "twice for West, " +
                                "three times for South " +
                                "and four times for North!" +
                                "If you have selected, then shake the phone!",
                        TextToSpeech.QUEUE_FLUSH, null);
            }



            Log.d(TAG2, "location is ok ...............");
        } else {
            Log.d(TAG2, "location is null ...............");
        }
    }


    protected void stopLocationUpdates() {
        LocationServices.FusedLocationApi.removeLocationUpdates(
                mGoogleApiClient, this);
        Log.d(TAG2, "Location update stopped .......................");
    }

    private final SensorEventListener mSensorEventListener = new SensorEventListener()
    {

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy)
        {

        }

        @Override
        public void onSensorChanged(SensorEvent event)
        {

            if (event.sensor.getType() == Sensor.TYPE_ORIENTATION)
            {

                float x_data = event.values[SensorManager.DATA_X]; ////X-->Z

                if ((x_data > 0 && x_data <= 22.5) || x_data > 337.5)
                {
                    ourCompass.setText(" North ");
                    ourDegree.setText(String.valueOf(x_data));
                    //t1.speak("You are facing North " , TextToSpeech.QUEUE_FLUSH, null);
                } else if (x_data > 22.5 && x_data <= 67.5)
                {
                    ourCompass.setText(" NorthEast " );
                    ourDegree.setText(String.valueOf(x_data));
                    //t1.speak("You are facing NorthEast " , TextToSpeech.QUEUE_FLUSH, null);
                } else if (x_data > 67.5 && x_data <= 112.5)
                {
                    ourCompass.setText(" East " );
                    ourDegree.setText(String.valueOf(x_data));
                    //t1.speak("You are facing East " , TextToSpeech.QUEUE_FLUSH, null);
                } else if (x_data > 112.5 && x_data <= 157.5)
                {
                    ourCompass.setText(" SouthEast " );
                    ourDegree.setText(String.valueOf(x_data));
                    //t1.speak("You are facing SouthEast " , TextToSpeech.QUEUE_FLUSH, null);
                } else if (x_data > 157.5 && x_data <= 202.5)
                {
                    ourCompass.setText(" South " );
                    ourDegree.setText(String.valueOf(x_data));
                    //t1.speak("You are facing South " , TextToSpeech.QUEUE_FLUSH, null);
                } else if (x_data > 202.5 && x_data <= 247.5)
                {
                    ourCompass.setText(" SouthWest " );
                    ourDegree.setText(String.valueOf(x_data));
                    //t1.speak("You are facing SouthWest " , TextToSpeech.QUEUE_FLUSH, null);
                } else if (x_data > 247.5 && x_data <= 292.5)
                {
                    ourCompass.setText(" West ");
                    ourDegree.setText(String.valueOf(x_data));
                    //t1.speak("You are facing West " , TextToSpeech.QUEUE_FLUSH, null);
                } else if (x_data > 292.5 && x_data <= 337.5)
                {
                    ourCompass.setText(" NorthWest " );
                    ourDegree.setText(String.valueOf(x_data));
                    //t1.speak("You are facing NorthWest ", TextToSpeech.QUEUE_FLUSH, null);
                }
                //t1.speak("You are facing " + ourCompass.getText().toString(), TextToSpeech.QUEUE_FLUSH, null);
            }
        }
    };


    private Handler mHandler = new Handler(){

        @Override
        public void handleMessage(Message msg) {

            super.handleMessage(msg);
            switch (msg.what) {
                case MessageID.MESSAGE_SENSOR:
                    isSensorSuccess = true;

                    //isSensorSuccess = false;
                    numTimesClicked=0;


                    //playRockStart();
                    turnLeft();
                    break;
                case MessageID.MESSAGE_SENSOR_STARTROCK_PLAYOVER:
                    //isSensorSuccess = true;
                    isSensorSuccess = false;

                    //playRockOver();
                    break;
                //case 2222:
                    //changeRockLayout();
                    //break;
                default:
                    break;
            }
        }

    };

    private MediaPlayer mp_start;
    private void playRockStart(){
        try {
            if(mp_start == null){
                mp_start = MediaPlayer.create(this, R.raw.turnleft);
            }
            if(!mp_start.isPlaying()){


                while (ourCompass.getText().toString() != direction) {

                    Toast.makeText(this, "Facing " + ourCompass.getText().toString() + "\n" +
                        "Direction: " + direction, Toast.LENGTH_LONG).show();
                    TextView textView = (TextView) findViewById(R.id.textView);
                    textView.setText("Turn Left!");
                    mp_start.start();
                }

                mp_start.setOnCompletionListener(new OnCompletionListener() {

                    @Override
                    public void onCompletion(MediaPlayer mp) {

                        mp.release();
                        mp_start = null;
                        //isCanBack = true;
                        mHandler.sendEmptyMessage(MessageID.MESSAGE_SENSOR_STARTROCK_PLAYOVER);
                        Log.i("webview","rock start completion");
                    }
                });
                addVibrator(200);
            }
        } catch (Exception e) {

        }
    }


    private void playRockOver(){
        try {
            if(mp_start == null){
                mp_start = MediaPlayer.create(this, R.raw.shake);
            }
            if(!mp_start.isPlaying()){
                mp_start.start();
                mp_start.setOnCompletionListener(new OnCompletionListener() {

                    @Override
                    public void onCompletion(MediaPlayer mp) {

                        mp.release();
                        mp_start = null;
                        //isCanBack = true;
                        //mHandler.sendEmptyMessage(MessageID.MESSAGE_SENSOR_STARTROCK_PLAYOVER);
                        Log.i("webview","rock start completion");
                    }
                });
                //addVibrator(200);
            }

        } catch (Exception e) {

        }

    }

    private void turnLeft(){
        //final Double obj=new Double(0.0);

        final Double[] obj = new Double[1];
        obj[0] = new Double(0.0);




        switch(direction){ // According to GITC Lock St and Central Ave Intersection.
            case "East": obj[0]=130.0; break;
            case "South": obj[0]=220.0; break;
            case "West": obj[0]=310.0; break;
            case "North": obj[0]=40.0; break;
            default: break;

        }



        Toast.makeText(this, "Facing " + ourCompass.getText().toString() + ourDegree.getText().toString()+ "\n" +
                "Direction: " + direction, Toast.LENGTH_LONG).show();
        TextView textView = (TextView) findViewById(R.id.textView);

        //Double ret = obj-Double.valueOf(ourDegree.getText().toString());
        //double interval = 45.0;

        //Double x = new Double(ret);

      /*
            if (abs(obj-Double.valueOf(ourDegree.getText().toString())) < 30) {  // degree interval 60
                t2.speak("Go straight", TextToSpeech.QUEUE_FLUSH, null);
                textView.setText("Go straight");
                //break;
            } else if (obj-Double.valueOf(ourDegree.getText().toString()) < 0) {
                t2.speak("Turn left" + (int) (obj - Double.valueOf(ourDegree.getText().toString())) + "degree, and then go straight", TextToSpeech.QUEUE_FLUSH, null);
                textView.setText("Left " + abs(ret));
             /*   int remainder = (int) (obj - Double.parseDouble(ourDegree.getText().toString()));
                while(abs(remainder)-interval>22.5) {
                    t2.speak("Turn left" + interval + "degree, and then go straight", TextToSpeech.QUEUE_FLUSH, null);
                    textView.setText("Left " + interval);
                    remainder-=interval;
                }
            } else {
                t2.speak("Turn right" + (int) (obj-Double.valueOf(ourDegree.getText().toString())) + "degree, and then go straight", TextToSpeech.QUEUE_FLUSH, null);
                textView.setText("Right " + ret);
            }
    */

        /*
        if (abs(obj-Double.valueOf(ourDegree.getText().toString())) < 30) {  // degree interval 60
            t2.speak("Go straight", TextToSpeech.QUEUE_FLUSH, null);
            textView.setText("Go straight");
            //break;
        } else if (obj-Double.valueOf(ourDegree.getText().toString()) < 0) {
            t2.speak("Please Turn left", TextToSpeech.QUEUE_FLUSH, null);
            textView.setText("Left " + abs(ret));

        } else {
            t2.speak("Please Turn right", TextToSpeech.QUEUE_FLUSH, null);
            textView.setText("Right " + ret);
        }*/




        t.schedule(new TimerTask()
        {
            @Override
            public void run()
            {
                System.out.println("Timer is working.");
                //new Thread(rcThread).start();
                if((abs(obj[0]-Double.valueOf(ourDegree.getText().toString()))> 30)) {
                    t2.speak("Keep turning", TextToSpeech.QUEUE_FLUSH, null);
                }
                else{
                    t2.speak("Go straight", TextToSpeech.QUEUE_FLUSH, null);
                }

                /* if (obj[0]-Double.valueOf(ourDegree.getText().toString()) < 0) {
                    textView.setText("Left " + abs(obj[0]-Double.valueOf(ourDegree.getText().toString())));
                }
                else{
                    textView.setText("Right " + (obj[0]-Double.valueOf(ourDegree.getText().toString())));
                }*/
            }
        }, 1000, 5000); // initial delay, interval time

       /* while(abs(obj-Double.valueOf(ourDegree.getText().toString()))> 30){
            t2.speak("Keep turning", TextToSpeech.QUEUE_FLUSH, null);
            if (obj-Double.valueOf(ourDegree.getText().toString()) < 0) {
                textView.setText("Left " + abs(obj-Double.valueOf(ourDegree.getText().toString())));
            }
            else{
                textView.setText("Right " + (obj-Double.valueOf(ourDegree.getText().toString())));
            }

        }*/

        //t2.speak("Go straight", TextToSpeech.QUEUE_FLUSH, null);
        //textView.setText("Go straight");




        mHandler.sendEmptyMessage(MessageID.MESSAGE_SENSOR_STARTROCK_PLAYOVER);

    }




    private void addVibrator(long ms){
        try {
            Vibrator vibrator = (Vibrator)getSystemService(VIBRATOR_SERVICE);
            vibrator.vibrate(ms);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onAnimationStart(Animation animation) {

    }

    @Override
    public void onAnimationEnd(Animation animation) {

    }

    @Override
    public void onAnimationRepeat(Animation animation) {

    }
}
