package cn.edu.uzz.activity.book;

import android.app.Application;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.mob.MobSDK;

/**
 * Created by 10616 on 2017/11/25.
 */

public class DemoApplication extends Application {
    public static RequestQueue queues;
    @Override
    public void onCreate() {
        super.onCreate();
        queues= Volley.newRequestQueue(getApplicationContext());
		MobSDK.init(this);
    }
    public static RequestQueue getHttpQueues(){
        return queues;

    }

}
