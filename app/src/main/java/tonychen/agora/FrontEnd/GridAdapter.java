package tonychen.agora.FrontEnd;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import tonychen.agora.BackEnd.Post;
import tonychen.agora.R;

/**
 * Created by tonyc on 6/28/2015.
 */
public class GridAdapter extends BaseAdapter {
    private Context mContext;
    private List<Post> postList;

    public GridAdapter(Context c, List<Post> posts) {
        mContext = c;
        postList = posts;
    }

    public int getCount() {
        return postList.size();
    }
    public Object getItem(int position) {
        return null;
    }
    public long getItemId(int position) {
        return 0;
    }

    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        View gridView = convertView;
        ViewHolder holder;

       if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            gridView = inflater.inflate(R.layout.grid_single, parent, false);

            holder = new ViewHolder(gridView);

            gridView.setTag(holder);
       } else {
           holder = (ViewHolder) gridView.getTag();
       }

        holder.title.setText(postList.get(position).title);
        holder.title.setTag(postList.get(position).objectId);
        holder.title.setBackgroundColor(0);
        holder.title.setGravity(Gravity.BOTTOM);

        holder.thumbnail.setImageBitmap(postList.get(position).thumbnail);
        holder.thumbnail.setTag(postList.get(position).objectId);
        holder.thumbnail.setMaxHeight(500);
        holder.thumbnail.setMaxWidth(500);
        holder.thumbnail.setScaleType(ImageView.ScaleType.CENTER_CROP);

        return gridView;
    }
}
