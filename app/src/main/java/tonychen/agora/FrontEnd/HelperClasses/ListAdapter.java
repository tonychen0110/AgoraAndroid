package tonychen.agora.FrontEnd.HelperClasses;

import android.content.Context;
import android.media.Image;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

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
        holder.thumbnail.setImageBitmap(list.get(position).thumbnail);

        return rowView;
    }
}
