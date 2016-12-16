package omfarid.com.legendlocator;

import android.content.Context;

import com.orm.SugarApp;

/**
 * Created by farid on 12/15/2016.
 */

public class Application extends SugarApp {
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        //MultiDex.install(this);
    }
}
