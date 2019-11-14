package fr.istic.mob.starld;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.Process;


public class ServiceStar extends Service {

    @Override
    public void onCreate(){
        HandlerThread threadServiceStar = new HandlerThread("ThreadStarService", Process.THREAD_PRIORITY_BACKGROUND);
        threadServiceStar.start();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId){

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
