package tonychen.agora;

import android.app.Activity;
import android.app.Application;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import com.facebook.FacebookSdk;
import com.parse.Parse;
import com.parse.ParseFacebookUtils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by tonyc on 6/29/2015.
 */
public class AgoraApplication extends Application {
    public void onCreate() {
        Parse.initialize(this, "U7URHkl93QVktNmfR9D1ZoZiQoib1fXZgMKIySXl", "447pKmFDlBBmYDLXUbn6lTFuXeRa0NUUjPghtM4Q");
        ParseFacebookUtils.initialize(getApplicationContext());
        FacebookSdk.sdkInitialize(getApplicationContext());
    }
}
