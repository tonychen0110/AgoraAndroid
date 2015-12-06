package tonychen.agora.FrontEnd;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import java.util.ArrayList;
import java.util.List;

import tonychen.agora.BackEnd.ParseInterface;
import tonychen.agora.BackEnd.Post;
import tonychen.agora.FrontEnd.HelperClasses.GridAdapter;
import tonychen.agora.R;


/**
 * A placeholder fragment containing a simple view.
 */
public class GridFragment extends Fragment {
    private SwipeRefreshLayout swipeRefreshLayout;
    private List<Post> listPosts;
    private String parameter;
    private GridView gridview;

    public static GridFragment newInstance(String parameter) {
        GridFragment f = new GridFragment();
        Bundle args = new Bundle();
        args.putString("parameter", parameter);
        f.setArguments(args);

        return f;
    }

    public static GridFragment newInstance(String parameter, ArrayList<String> keywords) {
        GridFragment f = new GridFragment();
        Bundle args = new Bundle();
        args.putString("parameter", parameter);
        args.putStringArrayList("keywords", keywords);
        f.setArguments(args);

        return f;
    }

    public GridFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View viewer =  inflater.inflate(R.layout.fragment_grid, container, false);
        parameter = getArguments().getString("parameter");

        ProgressDialog dialog = new ProgressDialog(getActivity());
        dialog.setTitle("Loading");
        dialog.setMessage("Please Wait While Loading");
        dialog.show();

        gridview = (GridView) viewer.findViewById(R.id.gridview);
        gridview.setBackgroundColor(Color.LTGRAY);

        AsyncGetPostsTask task = new AsyncGetPostsTask();
        task.execute();

        swipeRefreshLayout = (SwipeRefreshLayout) viewer.findViewById(R.id.swipeRefresh);
        setUpSwipeToRefresh(viewer);

        dialog.dismiss();

        return viewer;
    }

    private class AsyncGetPostsTask extends AsyncTask<String, Void, Void> {
        @Override
        protected Void doInBackground(String... params) {
            if (parameter.equals("Search")) {
                List<String> keywords = getArguments().getStringArrayList("keywords");
                listPosts = ParseInterface.search(keywords);
            } else {
                listPosts = ParseInterface.getPostsFromParse(parameter, 0);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            setUpGrid();
        }

    }

    private void setUpSwipeToRefresh(View viewer) {
        //Setting up pull-down to refresh
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                listPosts.clear();
                listPosts.addAll(ParseInterface.getPostsFromParse(parameter, 0));
                gridview.setAdapter(new GridAdapter(getActivity(), listPosts));
                swipeRefreshLayout.setRefreshing(false);
            }
        });
        swipeRefreshLayout.setColorSchemeColors(Color.BLUE,
                Color.GREEN,
                Color.YELLOW,
                Color.RED);
    }

    private void setUpGrid() {
        //Setting up grid of posts
        gridview.setAdapter(new GridAdapter( getActivity(), listPosts ));
        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                Intent i = new Intent(getActivity(), PostViewActivity.class);
                i.putExtra("objectId", listPosts.get(position).objectId);
                startActivity(i);
            }
        });
    }
}
