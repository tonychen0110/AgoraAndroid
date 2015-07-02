package tonychen.agora.FrontEnd;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import java.util.List;

import tonychen.agora.BackEnd.ParseInterface;
import tonychen.agora.BackEnd.Post;
import tonychen.agora.FrontEnd.HelperClasses.GridAdapter;
import tonychen.agora.R;


/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {
    private SwipeRefreshLayout swipeRefreshLayout;
    private List<Post> listPosts;
    private String parameter;
    private GridView gridview;

    public static MainActivityFragment newInstance(String parameter) {
        MainActivityFragment f = new MainActivityFragment();
        Bundle args = new Bundle();
        args.putString("parameter", parameter);
        f.setArguments(args);

        return f;
    }

    public MainActivityFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View viewer =  inflater.inflate(R.layout.fragment_main, container, false);

        parameter = getArguments().getString("parameter");
        gridview = (GridView) viewer.findViewById(R.id.gridview);
        listPosts = ParseInterface.getPostsFromParse(parameter, 0);

        swipeRefreshLayout = (SwipeRefreshLayout) viewer.findViewById(R.id.swipeRefresh);
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

        gridview.setAdapter(new GridAdapter( getActivity(), listPosts ));
        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                Intent i = new Intent(getActivity(), PostViewActivity.class);
                i.putExtra("objectId", listPosts.get(position).objectId);
                startActivity(i);
            }
        });

        return viewer;
    }
}
