package alarma.example.com.alarmaph;

import android.app.Application;
import android.content.Intent;

/**
 * Created by joshua on 2/16/2018.
 */

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        startService(new Intent(this, MyService.class));
    }
}
