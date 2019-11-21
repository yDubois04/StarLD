package fr.istic.mob.starld;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.Process;
import android.widget.Toast;


public class ServiceStar extends Service {
    String url = "https://data.explore.star.fr/api/records/1.0/search/?dataset=tco-busmetro-horaires-gtfs-versions-td";
    Looper serviceLooper;
    ServiceStarHandler serviceStarHandler;

    @Override
    public void onCreate(){
        HandlerThread threadServiceStar = new HandlerThread("ThreadStarService", Process.THREAD_PRIORITY_BACKGROUND);
        threadServiceStar.start();
        serviceLooper = threadServiceStar.getLooper();
        serviceStarHandler = new ServiceStarHandler(serviceLooper);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId){
        Toast.makeText(this, "Service running", Toast.LENGTH_SHORT).show();
        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private final class ServiceStarHandler extends Handler{
        public ServiceStarHandler(Looper looper){
            super(looper);
        }

        @Override
        public void handleMessage(Message msg){
            //faire des trucs

            stopSelf(msg.arg1);
        }
    }
}
