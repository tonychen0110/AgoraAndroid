package tonychen.agora.FrontEnd;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.Parse;

import java.util.List;

import tonychen.agora.BackEnd.ParseInterface;
import tonychen.agora.BackEnd.Post;
import tonychen.agora.R;


/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {

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

        String parameter = getArguments().getString("parameter");
        List<Post> listPosts = ParseInterface.getPostsFromParse(parameter, 0);

        GridView gridview = (GridView) viewer.findViewById(R.id.gridview);
        gridview.setAdapter(new ImageAdapter( getActivity(), listPosts ));

        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                Toast.makeText(getActivity(), "" + position,
                        Toast.LENGTH_SHORT).show();
            }
        });

        return viewer;
    }
}
