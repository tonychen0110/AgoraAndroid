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
    TextView title;
    TextView price;

    ViewHolder(View row, String parameter) {
        if (parameter.equals("GRID")) {
            this.thumbnail = (ImageView) row.findViewById(R.id.grid_image);
            this.title = (TextView) row.findViewById(R.id.grid_title);
            this.price = (TextView) row.findViewById(R.id.grid_price);
        } else if (parameter.equals("LIST")) {
            this.thumbnail = (ImageView) row.findViewById(R.id.my_post_thumbnail);
            this.title = (TextView) row.findViewById(R.id.my_post_title);
        }
    }
}
