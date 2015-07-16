package tonychen.agora.FrontEnd;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.Gallery;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.parse.ParseUser;

import org.json.JSONException;
import org.w3c.dom.Text;

import java.text.DecimalFormat;

import tonychen.agora.BackEnd.ParseInterface;
import tonychen.agora.BackEnd.Post;
import tonychen.agora.R;

public class PostViewActivity extends ActionBarActivity {
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_view);

        //Setting status bar color to PrimaryColorDark
        this.getWindow().setStatusBarColor(Color.parseColor("#303F9F"));

        // Initializing Toolbar and setting it as the actionbar
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Setting back button to previous activity
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setTitle("Back");

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        Bundle bundle = getIntent().getExtras();
        String objectId = bundle.getString("objectId");

        Post post = ParseInterface.getPostFromParseIndividual(objectId);

        GraphRequest graphRequest = new GraphRequest(AccessToken.getCurrentAccessToken(), "/" + post.createdBy.get("facebookId"), null, HttpMethod.GET, new GraphRequest.Callback() {
            @Override
            public void onCompleted(GraphResponse graphResponse) {
                try {
                    TextView poster_name = (TextView) findViewById(R.id.poster_name);
                    poster_name.setText(graphResponse.getJSONObject().getString("name"));
                } catch (JSONException e) {

                }
            }
        });
        graphRequest.executeAsync();

        //Setting various views with relevant information
        LinearLayout secondaryImagesGallery = (LinearLayout) findViewById(R.id.secondaryImagesGallery);
        TextView price = (TextView) findViewById(R.id.post_price);
        TextView title = (TextView) findViewById(R.id.post_title);
        TextView desc = (TextView) findViewById(R.id.post_desc);
        ImageView headerImage = (ImageView) findViewById(R.id.header_image);

        DecimalFormat decimal = new DecimalFormat("#.00");

        //Header image setting
        headerImage.setImageBitmap(post.headerPhoto);
        headerImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Picture clicked!", Toast.LENGTH_SHORT).show();
            }
        });

        title.setText(post.title);
        price.setText("$" + decimal.format(post.price));
        desc.setText(post.itemDesc);
        for (Bitmap bitmap: post.secondaryPictures) {
            ImageView imageView = new ImageView(getApplicationContext());
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            layoutParams.setMargins(10,10,10,10);

            imageView.setLayoutParams(layoutParams);
            imageView.setImageBitmap(bitmap);
            imageView.setTag(bitmap);
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Toast.makeText(getApplicationContext(), (String)v.getTag(), Toast.LENGTH_SHORT).show();
                }
            });
            secondaryImagesGallery.addView(imageView);
        }

        //Initializing message button and setting onClick listener
        FloatingActionButton floatingActionButton = (FloatingActionButton) findViewById(R.id.post_fab);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Message FAB Clicked!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        getMenuInflater().inflate(R.menu.post_view_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.share:
                Toast.makeText(getApplicationContext(), "Share Clicked!", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
