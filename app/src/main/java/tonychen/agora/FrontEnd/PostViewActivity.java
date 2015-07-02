package tonychen.agora.FrontEnd;

import android.graphics.Color;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

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
        toolbar.setTitle("Back");
        //Setting back button to previous activity
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        Bundle bundle = getIntent().getExtras();
        String objectId = bundle.getString("objectId");

        Post post = ParseInterface.getPostFromParseIndividual(objectId);

        TextView price = (TextView) findViewById(R.id.post_price);
        TextView title = (TextView) findViewById(R.id.post_title);
        ImageView headerImage = (ImageView) findViewById(R.id.header_image);

        headerImage.setImageBitmap(post.headerPhoto);
        title.setText(post.title);
        price.setText("" + post.price);

        headerImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Picture clicked!", Toast.LENGTH_SHORT).show();
            }
        });
    }


}
