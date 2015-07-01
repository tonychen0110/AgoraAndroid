package tonychen.agora.FrontEnd;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import tonychen.agora.R;


public class MainActivity extends ActionBarActivity {

    private Toolbar toolbar;
    private NavigationView navigationView;
    private DrawerLayout drawerLayout;
    private ImageButton FAB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MainActivityFragment mainActivityFragment = MainActivityFragment.newInstance("RECENTS");

        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.frame, mainActivityFragment);
        fragmentTransaction.commit();

        // Initializing Toolbar and setting it as the actionbar
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Initializing floating action button & setting click listener
        FAB = (ImageButton) findViewById(R.id.imageButton);
        FAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(MainActivity.this,"Hello World",Toast.LENGTH_SHORT).show();

            }
        });
        //Initializing the header
        RelativeLayout header = (RelativeLayout) findViewById(R.id.header);

        //Setting click listener to handle tap on header
        header.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Header Selected", Toast.LENGTH_SHORT).show();
            }
        });

        //TODO Set user's name here
        TextView headerUsername = (TextView) findViewById(R.id.username);
        headerUsername.setText("Tony Chen");

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

                MainActivityFragment mainActivityFragment;

                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                //Check to see which item was being clicked and perform appropriate action
                switch (menuItem.getItemId()) {
                    case R.id.browse:
                        mainActivityFragment = MainActivityFragment.newInstance("RECENTS");
                        fragmentTransaction.replace(R.id.frame, mainActivityFragment);
                        fragmentTransaction.commit();

                        toolbar.setTitle("Agora");
                        break;
                    case R.id.education:
                        mainActivityFragment = MainActivityFragment.newInstance(getResources().getString(R.string.education));
                        fragmentTransaction.replace(R.id.frame, mainActivityFragment);
                        fragmentTransaction.commit();

                        toolbar.setTitle(getResources().getString(R.string.education));
                        break;
                    case R.id.fashion:
                        mainActivityFragment = MainActivityFragment.newInstance(getResources().getString(R.string.fashion));
                        fragmentTransaction.replace(R.id.frame, mainActivityFragment);
                        fragmentTransaction.commit();

                        toolbar.setTitle(getResources().getString(R.string.fashion));
                        break;
                    case R.id.home:
                        mainActivityFragment = MainActivityFragment.newInstance(getResources().getString(R.string.home));
                        fragmentTransaction.replace(R.id.frame, mainActivityFragment);
                        fragmentTransaction.commit();

                        toolbar.setTitle(getResources().getString(R.string.home));
                        break;
                    case R.id.tech:
                        mainActivityFragment = MainActivityFragment.newInstance(getResources().getString(R.string.tech));
                        fragmentTransaction.replace(R.id.frame, mainActivityFragment);
                        fragmentTransaction.commit();

                        toolbar.setTitle(getResources().getString(R.string.tech));
                        break;
                    case R.id.misc:
                        mainActivityFragment = MainActivityFragment.newInstance(getResources().getString(R.string.misc));
                        fragmentTransaction.replace(R.id.frame, mainActivityFragment);
                        fragmentTransaction.commit();

                        toolbar.setTitle(getResources().getString(R.string.misc));
                        break;
                    default:
                        Toast.makeText(getApplicationContext(), "Somethings Wrong", Toast.LENGTH_SHORT).show();
                        toolbar.setTitle("Agora");
                        break;
                }
                return true;
            }
        });

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
