package com.example.covid_check_program;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.BeaconConsumer;
import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.BeaconParser;
import org.altbeacon.beacon.MonitorNotifier;
import org.altbeacon.beacon.RangeNotifier;
import org.altbeacon.beacon.Region;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static java.lang.Thread.sleep;

public class BeaconBackgroundService extends Service implements BeaconConsumer {
    public static final String CHANNEL_ID = "BeaconBackgroundServiceChannel";

    protected static final String TAG1 = "::MonitoringActivity::";
    protected static final String TAG2 = "::RangingActivity::";
    private BeaconManager beaconManager;

    //for HTTP request
    private final String BASEURL = "https://gpbl.lemondouble.com";
    private JsonPlaceHolderApi jsonPlaceHolderApi;

    private Userdata userdata = new Userdata();

    private void createPost(){
        Call<Userdata> call = jsonPlaceHolderApi.createPost(userdata);

        call.enqueue(new Callback<Userdata>() {
            @Override
            public void onResponse(Call<Userdata> call, Response<Userdata> response) {
                if (!response.isSuccessful()){
                    Log.e("connection problem : ", "error code : " + response.code());
                    return;
                }

                Userdata responseUserdata = response.body();
                Log.d("connected : ", response.body().toString());

            }

            @Override
            public void onFailure(Call<Userdata> call, Throwable t) {
                Log.e("connect failed.",t.getMessage());
            }
        });
    }

    @Override
    public void onBeaconServiceConnect(){
        beaconManager.removeAllRangeNotifiers();

        beaconManager.addMonitorNotifier(new MonitorNotifier() {
            @Override
            public void didEnterRegion(Region region) {
                Log.d(TAG1, "::: we find at least 1 beacon:::" );
            }

            @Override
            public void didExitRegion(Region region) {
                Log.d(TAG1, ":::we cannot find beacons:::");
            }

            @Override
            public void didDetermineStateForRegion(int state, Region region) {
                if(state == 0){
                    Log.d(TAG1, "::: WE CAN SEE BEACON. state : "+state + " ::: ");
                }else{
                    Log.d(TAG2, "::: WE CANNOT SEE BEACON. state : " + state + ":::");
                }
            }
        });

        beaconManager.addRangeNotifier(new RangeNotifier() {
            @Override
            public void didRangeBeaconsInRegion(Collection<Beacon> beacons, Region region) {
                List<Beacon> list = (List<Beacon>)beacons;

                if(beacons.size() > 0){
                    SharedPreferences userPreferencesGetter = getSharedPreferences("userdata",MODE_PRIVATE);

                    userdata.setUserName(userPreferencesGetter.getString("userName",""));
                    userdata.setUserPhone(userPreferencesGetter.getString("userPhone",""));
                    userdata.setUserEmail(userPreferencesGetter.getString("userEmail",""));

                    userdata.setUUID(beacons.iterator().next().getId1().toString());
                    userdata.setMajor(beacons.iterator().next().getId2().toString());
                    userdata.setMinor(beacons.iterator().next().getId3().toString());

                    Timestamp timeStamp = new Timestamp(System.currentTimeMillis());
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.US);

                    userdata.setTimeData(simpleDateFormat.format(timeStamp));

                    createPost();

                    try{
                        sleep(5000);
                    }catch(InterruptedException e){
                        e.printStackTrace();
                    }

                    // Log.d(TAG2, ":::::This :: U U I D :: of beacon   :  "+ beacons.iterator().next().getId1().toString() + ":::::");
                    // Log.d(TAG2, ":::::This ::M a j o r:: of beacon   :  "+ beacons.iterator().next().getId2().toString() + ":::::");
                    // Log.d(TAG2, ":::::This ::M i n o r:: of beacon   :  "+ beacons.iterator().next().getId3().toString() + ":::::");
                }
            }
        });
        try {
            beaconManager.startRangingBeaconsInRegion(new Region("C2:02:DD:00:13:DD", null, null, null));
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onCreate(){
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId){
        createNotificationChannel();
        Intent notificationIntent = new Intent(this,activeBluetooth.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);

        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("Covid-check-program")
                .setContentText("automatic check program running in background...")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentIntent(pendingIntent)
                .build();
        startForeground(1,notification);

        //running data


        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){}

        beaconManager = BeaconManager.getInstanceForApplication(this);
        beaconManager.getBeaconParsers().add(new BeaconParser().setBeaconLayout("m:2-3=0215,i:4-19,i:20-21,i:22-23,p:24-24,d:25-25"));

        //binding BeaconService to Android Activity or service
        beaconManager.bind(this);

        //for http request
        Retrofit retrofit = new Retrofit.Builder().baseUrl(BASEURL).addConverterFactory(GsonConverterFactory.create()).build();

        jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi.class);

        return START_STICKY;
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel serviceChannel = new NotificationChannel(
                    CHANNEL_ID,
                    "BeaconBackgroundServiceChannel",
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(serviceChannel);
        }
    }
}