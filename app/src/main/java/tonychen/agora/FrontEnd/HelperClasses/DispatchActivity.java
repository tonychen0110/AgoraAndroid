package tonychen.agora.FrontEnd.HelperClasses;

import android.app.Activity;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import com.parse.ui.ParseLoginDispatchActivity;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import tonychen.agora.FrontEnd.MainActivity;

/**
 * Created by tonyc on 7/14/2015.
 */
public class DispatchActivity extends ParseLoginDispatchActivity {

    @Override
    protected Class<?> getTargetClass() {
        return MainActivity.class;
    }
}
