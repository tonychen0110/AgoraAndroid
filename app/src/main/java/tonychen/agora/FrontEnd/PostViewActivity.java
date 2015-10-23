package tonychen.agora.FrontEnd;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.graphics.drawable.shapes.Shape;
import android.os.AsyncTask;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Display;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.DecelerateInterpolator;
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
import com.parse.ParseException;
import com.parse.ParseFile;
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

        Bundle bundle = getIntent().getExtras();
        String objectId = bundle.getString("objectId");

        GetPostTask task = new GetPostTask();
        task.execute(new String[] {objectId});

        setUpActionBar();
        setUpFab();

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

    private class GetPostTask extends AsyncTask<String, Void, Post> {
        @Override
        protected Post doInBackground(String... params) {
            Post post = null;
            for(String objectId: params) {
                 post = ParseInterface.getPostFromParseIndividual(objectId);
            }
            return post;
        }

        @Override
        protected void onPostExecute(Post post) {
            super.onPostExecute(post);
            setUpPosterInfo(post);
            setUpTextAndHeader(post);
            setUpSecondaryImages(post);
        }
    }

    private void zoomImage(final View v) {

        //Getting the view
        final ImageView zoomedImage = (ImageView) findViewById(R.id.zoomed_image);

        //Getting the image
        zoomedImage.setImageBitmap((Bitmap)v.getTag());
        zoomedImage.setVisibility(View.VISIBLE);

        //Zoom back down when clicked on
        zoomedImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                v.setAlpha(1f);
                zoomedImage.setVisibility(View.GONE);
                System.gc();
            }
        });
        System.gc();
    }

    private void setUpTextAndHeader(Post post) {
        //Finding the views
        TextView category = (TextView) findViewById(R.id.category_text);
        TextView price = (TextView) findViewById(R.id.post_price);
        TextView title = (TextView) findViewById(R.id.post_title);
        TextView desc = (TextView) findViewById(R.id.post_desc);
        ImageView headerImage = (ImageView) findViewById(R.id.header_image);

        //Ensure 2 decimal places for price
        DecimalFormat decimal = new DecimalFormat("#.00");

        //Setting the text
        category.setText(post.category);
        title.setText(post.title);
        price.setText("$" + decimal.format(post.price));
        desc.setText(post.itemDesc);

        //Setting the header image
        headerImage.setImageBitmap(post.headerPhoto);

        setUpCategoryDot(post.category);
    }

    private void setUpActionBar() {

        //Setting status bar color to PrimaryColorDark
        this.getWindow().setStatusBarColor(getResources().getColor(R.color.PrimaryDarkColor));

        // Initializing Toolbar and setting it as the actionbar
        toolbar = (Toolbar) findViewById(R.id.post_toolbar);
        setSupportActionBar(toolbar);

        //Setting back button to previous activity
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setTitle("");

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void setUpSecondaryImages(Post post) {
        //Setting various views with relevant information
        LinearLayout secondaryImagesGallery = (LinearLayout) findViewById(R.id.secondaryImagesGallery);

        int count = 0;
        for (Bitmap bitmap: post.secondaryPicturesThumbnails) {
            ImageView imageView = new ImageView(getApplicationContext());
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            layoutParams.setMargins(20,20,20,20);
            imageView.setLayoutParams(layoutParams);

            imageView.setImageBitmap(bitmap);

            imageView.setTag(post.secondaryPictures.get(count));
            count++;

            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    zoomImage(v);
                }
            });
            secondaryImagesGallery.addView(imageView);
        }
    }

    private void setUpFab() {
        //Initializing message button and setting onClick listener
        FloatingActionButton floatingActionButton = (FloatingActionButton) findViewById(R.id.post_fab);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Message FAB Clicked!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setUpPosterInfo(Post post) {
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
    }

    private void setUpCategoryDot(String category) {
        View cate_dot = findViewById(R.id.category_circle);

        //Instantiating the dot drawable
        ShapeDrawable cate_dot_drawable = new ShapeDrawable(new OvalShape());

        //Setting up the dot's size
        cate_dot_drawable.setIntrinsicHeight(4);
        cate_dot_drawable.setIntrinsicWidth(4);

        //Setting category dot color
        switch (category) {
            case ("Education"):
                cate_dot_drawable.getPaint().setColor(getResources().getColor(R.color.eduColor));
                break;
            case ("Fashion"):
                cate_dot_drawable.getPaint().setColor(getResources().getColor(R.color.fashColor));
                break;
            case ("Home"):
                cate_dot_drawable.getPaint().setColor(getResources().getColor(R.color.homeColor));
                break;
            case ("Tech"):
                cate_dot_drawable.getPaint().setColor(getResources().getColor(R.color.techColor));
                break;
            case ("Misc"):
                cate_dot_drawable.getPaint().setColor(getResources().getColor(R.color.miscColor));
                break;
        }

        //Adding the dot
        Drawable[] d = {cate_dot_drawable};
        LayerDrawable layerDrawable = new LayerDrawable(d);
        cate_dot.setBackground(layerDrawable);
    }
}
