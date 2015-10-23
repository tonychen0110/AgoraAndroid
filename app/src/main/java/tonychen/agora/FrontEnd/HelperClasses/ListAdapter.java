package tonychen.agora.FrontEnd.HelperClasses;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;

import java.util.List;

import tonychen.agora.BackEnd.Post;
import tonychen.agora.R;

/**
 * Created by tonyc on 8/14/2015.
 */
public class ListAdapter extends ArrayAdapter {
    private final Context context;
    private final List<Post> list;

    public ListAdapter(Context context, List<Post> list) {
        super(context, R.layout.my_post_cell_layout, list);
        this.context = context;
        this.list = list;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View rowView = convertView;
        ViewHolder holder;

        if (rowView == null) {
            LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            rowView = inflater.inflate(R.layout.my_post_cell_layout, parent, false);

            holder = new ViewHolder(rowView, "LIST");
            rowView.setTag(holder);
        } else {
            holder = (ViewHolder) rowView.getTag();
        }

        holder.title.setText(list.get(position).title);
        holder.title.setTextColor(Color.BLACK);
        holder.thumbnail.setImageBitmap(list.get(position).thumbnail);

        Bitmap bitmap = Bitmap.createBitmap(20,150, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        canvas.drawRect(0,0,5,200, cateColor(list.get(position).category));
        rowView.setBackgroundDrawable(new BitmapDrawable(bitmap));
        return rowView;
    }

    private Paint cateColor(String category) {
        Paint paint = new Paint();

        switch (category) {
            case "Education":
                paint.setARGB(255, 253, 208, 23);
                break;
            case "Fashion":
                paint.setARGB(255, 255, 102, 204);
                break;
            case "Home":
                paint.setARGB(255, 56, 142, 60);
                break;
            case "Tech":
                paint.setARGB(255, 0, 0, 255);
                break;
            case "Misc":
                paint.setARGB(255, 143, 0, 255);
                break;
        }

        return paint;
    }
}
