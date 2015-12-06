package tonychen.agora.FrontEnd;

import android.app.AlertDialog;
import android.app.DialogFragment;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.Parse;

import org.w3c.dom.Text;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import tonychen.agora.BackEnd.ParseInterface;
import tonychen.agora.BackEnd.Post;
import tonychen.agora.FrontEnd.Dialogs.HeaderPhotoSourceDialog;
import tonychen.agora.FrontEnd.Dialogs.SecondaryPhotoSourceDialog;
import tonychen.agora.R;

public class AddPostActivity extends ActionBarActivity implements HeaderPhotoSourceDialog.NoticeDialogListener, SecondaryPhotoSourceDialog.NoticeDialogListener {
    private Toolbar toolbar;
    private Post post;
    private Spinner spinner;
    private ImageView addSecondaryPhotosImage;
    private LinearLayout addSecondaryImagesGallery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_post);
        Bundle bundle = getIntent().getExtras();
        String objectId = bundle.getString("objectId");

        if (objectId.equals("NEW")) {
            post = new Post();
            post.secondaryPictures = new ArrayList<>();
        } else {
            post = ParseInterface.getPostFromParseIndividual(objectId);
        }

        setUpHeader();
        setUpTextFields();
        setUpActionBar();
        setUpCategoriesSpinner();
        setUpButton();
        setUpSecondaryImagesGallery();
    }

    private boolean isEmpty(EditText editText) {
        //Returns true if there is nothing in the edittext, fail otherwise
        return editText.getText().toString().trim().length() == 0;
    }

    private void setUpTextFields() {
        if (post.title != null && post.itemDesc != null) {
            EditText title = (EditText) findViewById(R.id.add_title);
            EditText price = (EditText) findViewById(R.id.add_price);
            EditText desc = (EditText) findViewById(R.id.add_description);

            title.setText(post.title);
            desc.setText(post.itemDesc);

            DecimalFormat df = new DecimalFormat("#.00");
            price.setText(df.format(post.price));
        }
    }

    private void setUpHeader() {
        ImageView header = (ImageView) findViewById(R.id.add_header_image);
        if (post.headerPhoto != null) {
            header.setScaleType(ImageView.ScaleType.FIT_XY);
            header.setImageBitmap(post.headerPhoto);
        }
        header.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog("HEADER");
            }
        });
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
                if (isEmpty(title)) {
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
                    ((TextView) spinner.getSelectedView()).setError("No Category Selected");
                    incomplete = true;
                }

                post.secondaryPictures = new ArrayList<Bitmap>();

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

        if (post.category != null) {
            int spinnerPos = categoriesAdapter.getPosition(post.category);
            spinner.setSelection(spinnerPos);
        }
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

    public void showDialog(String context) {
        if (context.equals("HEADER")) {
            DialogFragment dialog = new HeaderPhotoSourceDialog();
            dialog.show(getFragmentManager(), "HeaderPhotoSourceDialog");
        } else if (context.equals("SECONDARY")) {
            DialogFragment dialog = new SecondaryPhotoSourceDialog();
            dialog.show(getFragmentManager(), "SecondaryPhotoSourceDialog");
        }
    }

    @Override
    public void onDialogPositiveClick(DialogFragment dialog) {

    }

    @Override
    public void onDialogNegativeClick(DialogFragment dialog) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        ImageView headerImage = (ImageView) findViewById(R.id.add_header_image);

        if (resultCode == RESULT_OK) {
            if (requestCode == getResources().getInteger(R.integer.REQUEST_CAMERA_HEADER)) {
                Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                headerImage.setImageBitmap(bitmap);
                post.headerPhoto = bitmap;

            } else if (requestCode == getResources().getInteger(R.integer.SELECT_FILE_HEADER)) {
                String selectedImagePath = getImagePath(data.getData());
                Bitmap bitmap = BitmapFactory.decodeFile(selectedImagePath);
                headerImage.setImageBitmap(bitmap);
                post.headerPhoto = bitmap;

            } else if (requestCode == getResources().getInteger(R.integer.REQUEST_CAMERA_SECONDARY)) {
                Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                post.secondaryPictures.add(bitmap);

                ImageView imageView = new ImageView(getApplicationContext());
                imageView.setLayoutParams(setUpLayoutParams());

                int imageHeight = imageView.getHeight();
                int imageWidth = imageView.getWidth();
                int ratio = imageWidth/imageHeight;

                imageView.setImageBitmap(Bitmap.createScaledBitmap(bitmap, 500, (500/ratio), false));

                imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //Remove image view if clicked on
                    }
                });

                addSecondaryImagesGallery.removeView(addSecondaryPhotosImage);
                addSecondaryImagesGallery.addView(imageView);
                addSecondaryImagesGallery.addView(addSecondaryPhotosImage);


            } else if (requestCode == getResources().getInteger(R.integer.SELECT_FILE_SECONDARY)) {
                String selectedImagePath = getImagePath(data.getData());

                //Create fullsized bitmap for storing in post object
                Bitmap bitmap = BitmapFactory.decodeFile(selectedImagePath);
                post.secondaryPictures.add(bitmap);

                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inSampleSize = 4;
                //Create reduced sized bitmap for display
                bitmap = BitmapFactory.decodeFile(selectedImagePath, options);

                ImageView imageView = new ImageView(getApplicationContext());
                imageView.setLayoutParams(setUpLayoutParams());
                imageView.setImageBitmap(Bitmap.createScaledBitmap(bitmap, 500, 500, false));

                imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //Remove image view if clicked on
                    }
                });

                addSecondaryImagesGallery.removeView(addSecondaryPhotosImage);
                addSecondaryImagesGallery.addView(imageView);
                addSecondaryImagesGallery.addView(addSecondaryPhotosImage);
            }
        }
    }

    private String getImagePath(Uri selectedImageUri) {
        String[] projection = { MediaStore.MediaColumns.DATA };
        Cursor cursor = managedQuery(selectedImageUri, projection, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
        cursor.moveToFirst();

        return cursor.getString(column_index);

    }

    private void setUpSecondaryImagesGallery() {
        addSecondaryImagesGallery = (LinearLayout) findViewById(R.id.add_secondary_images_gallery);

        addSecondaryPhotosImage = new ImageView(getApplicationContext());
        addSecondaryPhotosImage.setLayoutParams(setUpLayoutParams());
        addSecondaryPhotosImage.setImageResource(R.drawable.add_camera);

        addSecondaryPhotosImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog("SECONDARY");
            }
        });

        addSecondaryImagesGallery.addView(addSecondaryPhotosImage);

    }

    private LinearLayout.LayoutParams setUpLayoutParams() {
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(20,20,20,20);

        return layoutParams;
    }
}
