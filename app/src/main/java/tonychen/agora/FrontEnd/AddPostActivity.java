package tonychen.agora.FrontEnd;

import android.app.AlertDialog;
import android.app.DialogFragment;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import tonychen.agora.BackEnd.ParseInterface;
import tonychen.agora.BackEnd.Post;
import tonychen.agora.FrontEnd.Dialogs.PhotoSourceDialog;
import tonychen.agora.R;

public class AddPostActivity extends ActionBarActivity implements PhotoSourceDialog.NoticeDialogListener{
    private Toolbar toolbar;
    private Post post;
    private Spinner spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_post);
        ImageView header = (ImageView) findViewById(R.id.add_header_image);
        header.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog();
            }
        });

        post = new Post();
        setUpActionBar();
        setUpCategoriesSpinner();
        setUpButton();
    }

    private boolean isEmpty(EditText editText) {
        //Returns true if there is nothing in the edittext, fail otherwise
        return editText.getText().toString().trim().length() == 0;
    }

    private void setUpButton() {
        Button button = (Button) findViewById(R.id.add_post_button);

        button.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean incomplete = false;
                EditText title = (EditText) findViewById(R.id.add_title);
                EditText price = (EditText) findViewById(R.id.add_price);
                EditText desc = (EditText) findViewById(R.id.add_description);
                ImageView header = (ImageView) findViewById(R.id.add_header_image);

                //Incomplete post checking
                if(isEmpty(title)) {
                    title.setError("No Title Entered");
                    incomplete = true;
                } else {
                    post.title = title.getText().toString();
                }

                if (isEmpty(price)) {
                    price.setError("No Price Entered");
                    incomplete = true;
                } else {
                    post.price = Double.parseDouble(price.getText().toString());
                }

                if (isEmpty(desc)) {
                    desc.setError("No Description Entered");
                    incomplete = true;
                } else {
                    post.itemDesc = desc.getText().toString();
                }

                if (post.category.equals("Select One") || post.category.equals("")) { //Invalid selection for category
                    ((TextView)spinner.getSelectedView()).setError("No Category Selected");
                    incomplete = true;
                }

                if (!incomplete) { //All necessary fields have been filled
                    //Save post to Parse backend
                    ParseInterface.saveToParse(post);
                    //Return to main page after post
                    Intent i = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(i);
                }
            }
        });
    }

    private void setUpCategoriesSpinner() {
        spinner = (Spinner) findViewById(R.id.add_category_spinner);
        // Create an ArrayAdapter using the string array and a default spinner layout
        List<String> categories = new ArrayList<>();
        categories.add("Select One");
        categories.addAll(Arrays.asList(getResources().getStringArray(R.array.categories)));
        ArrayAdapter<String> categoriesAdapter = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, categories);
        // Specify the layout to use when the list of choices appears
        categoriesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(categoriesAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                post.category = (String) parent.getItemAtPosition(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                //No category has been selected

            }
        });
    }

    private void setUpActionBar() {

        //Setting status bar color to PrimaryColorDark
        this.getWindow().setStatusBarColor(Color.parseColor("#303F9F"));

        // Initializing Toolbar and setting it as the actionbar
        toolbar = (Toolbar) findViewById(R.id.add_toolbar);
        setSupportActionBar(toolbar);

        //Setting back button to previous activity
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setTitle(R.string.add_post);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    public void showDialog() {
        DialogFragment dialog = new PhotoSourceDialog();
        dialog.show(getFragmentManager(), "PhotoSourceDialog");
    }

    @Override
    public void onDialogPositiveClick(DialogFragment dialog) {

    }

    @Override
    public void onDialogNegativeClick(DialogFragment dialog) {

    }
}
