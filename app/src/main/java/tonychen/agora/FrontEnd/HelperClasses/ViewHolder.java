package tonychen.agora.FrontEnd.HelperClasses;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import tonychen.agora.R;

/**
 * Created by tonyc on 6/30/2015.
 */
public class ViewHolder {
    ImageView thumbnail;
    TextView text;

    ViewHolder(View row) {
        this.thumbnail = (ImageView)row.findViewById(R.id.grid_image);
        this.text = (TextView)row.findViewById(R.id.grid_title_price);
    }
}
