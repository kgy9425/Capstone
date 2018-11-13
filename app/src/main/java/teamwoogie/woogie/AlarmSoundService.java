package teamwoogie.woogie;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.widget.Toast;

public class AlarmSoundService extends Service {
    public AlarmSoundService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    public int onStartCommand(Intent intent, int flags, int startId){
        Toast.makeText(this,"알람이울립니다",Toast.LENGTH_SHORT).show();
        return START_NOT_STICKY;
    }
}
