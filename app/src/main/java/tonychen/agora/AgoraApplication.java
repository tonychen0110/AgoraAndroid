package tonychen.agora;

import android.app.Application;

import com.facebook.FacebookSdk;
import com.parse.Parse;

/**
 * Created by tonyc on 6/29/2015.
 */
public class AgoraApplication extends Application {
    public void onCreate() {
        Parse.initialize(this, "U7URHkl93QVktNmfR9D1ZoZiQoib1fXZgMKIySXl", "447pKmFDlBBmYDLXUbn6lTFuXeRa0NUUjPghtM4Q");
        FacebookSdk.sdkInitialize(getApplicationContext());
    }
}
