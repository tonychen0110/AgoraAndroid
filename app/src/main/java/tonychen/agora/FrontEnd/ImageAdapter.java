package tonychen.agora.FrontEnd;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import java.util.List;

import tonychen.agora.BackEnd.Post;

/**
 * Created by tonyc on 6/28/2015.
 */
public class ImageAdapter extends BaseAdapter {
    private Context mContext;
    private List<Post> postList;

    public ImageAdapter(Context c, List<Post> posts) {
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
        ImageView imageView;
        if (convertView == null) {
            // if it's not recycled, initialize some attributes
            imageView = new ImageView(mContext);
            imageView.setLayoutParams(new GridView.LayoutParams(500, 500));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setPadding(2, 2, 2, 2);
        } else {
            imageView = (ImageView) convertView;
        }

        imageView.setImageBitmap(postList.get(position).thumbnail);
        return imageView;
    }
}
