package tonychen.agora.FrontEnd;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.parse.ParseUser;

import org.json.JSONException;
import org.json.JSONObject;

import tonychen.agora.BackEnd.ParseInterface;
import tonychen.agora.R;


public class MainActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private NavigationView navigationView;
    private DrawerLayout drawerLayout;
    private ImageButton FAB;

    private final String tag = "mainActivityFragment";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FragmentManager fragmentManager = getFragmentManager();

        //Setting status bar color to PrimaryColorDark
        this.getWindow().setStatusBarColor(getResources().getColor(R.color.PrimaryDarkColor));
        if (fragmentManager.findFragmentByTag(tag) == null) {
            GridFragment gridFragment = GridFragment.newInstance("RECENTS");
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.add(R.id.frame, gridFragment, tag);
            fragmentTransaction.commit();

            setUpFAB();
        }

        setUpToolbar();
        setUpHeader();
        nameGraphRequest();
        setUpNavBar();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        getMenuInflater().inflate(R.menu.menu_main, menu);

        MenuItem searchItem = menu.findItem(R.id.search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);

        searchView.clearFocus();
        searchView.setQueryHint("Search");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                Intent i = new Intent(getApplicationContext(), SearchResultActivity.class);
                i.putExtra("query", query);
                startActivityForResult(i, getResources().getInteger(R.integer.SEARCH));

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        // When using the support library, the setOnActionExpandListener() method is
        // static and accepts the MenuItem object as an argument
        MenuItemCompat.setOnActionExpandListener(searchItem, new MenuItemCompat.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                // Do something when collapsed
                return true;  // Return true to collapse action view
            }

            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                // Do something when expanded
                return true;  // Return true to expand action view
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void setUpNavBar() {

        //Initializing NavigationView
        navigationView = (NavigationView) findViewById(R.id.navigation_view);

        //Setting Navigation View Item Selected Listener to handle the item click of the navigation menu
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {

            // This method will trigger on item Click of navigation menu
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                //Checking if the item is in checked state or not, if not make it in checked state
                if (menuItem.isChecked()) menuItem.setChecked(false);
                else menuItem.setChecked(true);

                //Closing drawer on item click
                drawerLayout.closeDrawers();

                GridFragment gridFragment;

                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                //Check to see which item was being clicked and perform appropriate action
                switch (menuItem.getItemId()) {
                    case R.id.browse:
                        gridFragment = GridFragment.newInstance("RECENTS");
                        fragmentTransaction.replace(R.id.frame, gridFragment, tag);
                        toolbar.setTitle("Agora");
                        break;
                    case R.id.education:
                        gridFragment = GridFragment.newInstance(getResources().getString(R.string.education));
                        fragmentTransaction.replace(R.id.frame, gridFragment, tag);
                        toolbar.setTitle(getResources().getString(R.string.education));
                        break;
                    case R.id.fashion:
                        gridFragment = GridFragment.newInstance(getResources().getString(R.string.fashion));
                        fragmentTransaction.replace(R.id.frame, gridFragment, tag);
                        toolbar.setTitle(getResources().getString(R.string.fashion));
                        break;
                    case R.id.home:
                        gridFragment = GridFragment.newInstance(getResources().getString(R.string.home));
                        fragmentTransaction.replace(R.id.frame, gridFragment, tag);
                        toolbar.setTitle(getResources().getString(R.string.home));
                        break;
                    case R.id.tech:
                        gridFragment = GridFragment.newInstance(getResources().getString(R.string.tech));
                        fragmentTransaction.replace(R.id.frame, gridFragment, tag);
                        toolbar.setTitle(getResources().getString(R.string.tech));
                        break;
                    case R.id.misc:
                        gridFragment = GridFragment.newInstance(getResources().getString(R.string.misc));
                        fragmentTransaction.replace(R.id.frame, gridFragment, tag);
                        toolbar.setTitle(getResources().getString(R.string.misc));
                        break;
                    case R.id.myposts:
                        Intent i = new Intent(getApplicationContext(), MyPostsActivity.class);
                        startActivity(i);
                        break;
                    case R.id.settings:
                        Toast.makeText(getApplicationContext(), "Settings", Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        Toast.makeText(getApplicationContext(), "Somethings Wrong", Toast.LENGTH_SHORT).show();
                        toolbar.setTitle("Agora");
                        break;
                }
                fragmentTransaction.commit();
                return true;
            }
        });

        setUpDrawerAndActionBarToggle();
    }

    private void setUpFAB() {
        //Initializing floating action button & setting click listener
        FAB = (ImageButton) findViewById(R.id.imageButton);
        FAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), AddPostActivity.class);
                i.putExtra("objectId", "NEW");
                startActivity(i);
            }
        });
    }

    private void nameGraphRequest() {
        //User's name is set here
        GraphRequest profileRequest = GraphRequest.newMeRequest(AccessToken.getCurrentAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
            @Override
            public void onCompleted(JSONObject jsonObject, GraphResponse graphResponse) {
                try {
                    TextView headerUsername = (TextView) findViewById(R.id.username);
                    headerUsername.setText(jsonObject.getString("name"));
                    ParseUser user = ParseUser.getCurrentUser();
                    user.put("facebookId", jsonObject.getString("id"));
                    user.saveInBackground();
                } catch (JSONException e) {}
            }
        });
        profileRequest.executeAsync();
    }

    private void setUpHeader() {
        //Initializing the header
        RelativeLayout header = (RelativeLayout) findViewById(R.id.header);

        //Setting click listener to handle tap on header
        header.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ParseInterface.logout();
                Intent i = new Intent(getApplicationContext(), com.parse.ui.ParseLoginActivity.class);
                startActivity(i);
            }
        });

    }

    private void setUpToolbar() {
        // Initializing Toolbar and setting it as the actionbar
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    private void setUpDrawerAndActionBarToggle() {
        // Initializing Drawer Layout and ActionBarToggle
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer);
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.drawer_open, R.string.drawer_close){

            @Override
            public void onDrawerClosed(View drawerView) {
                // Code here will be triggered once the drawer closes as we dont want anything to happen so we leave this blank
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                // Code here will be triggered once the drawer open as we dont want anything to happen so we leave this blank

                super.onDrawerOpened(drawerView);
            }
        };

        //Setting the actionbarToggle to drawer layout
        drawerLayout.setDrawerListener(actionBarDrawerToggle);

        //calling sync state is necessay or else your hamburger icon wont show up
        actionBarDrawerToggle.syncState();
    }
}