package tonychen.agora.BackEnd;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.parse.GetCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by tonyc on 6/22/2015.
 */

public class ParseInterface {
    public static void saveToParse(Post post) {
        ParseObject parsePost = new ParseObject("Posts");

        parsePost.put("title", post.title);
        parsePost.put("description", post.itemDesc);
        parsePost.put("category", post.category);
        parsePost.put("price", post.price);

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        post.headerPhoto.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] data = stream.toByteArray();

        ParseFile file = new ParseFile("file", data);
        file.saveInBackground();
        parsePost.put("picture", file);

        parsePost.saveInBackground();
    }

    public static void updateParsePost(Post post) {
        ParseQuery query = new ParseQuery("Posts");
        ParseObject parsePost;
        try {
             parsePost = query.get(post.objectId);
        } catch (ParseException e) {
            return;
        }

        parsePost.put("title", post.title);
        parsePost.put("description", post.itemDesc);
        parsePost.put("category", post.category);
        parsePost.put("price", post.price);

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        post.headerPhoto.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] data = stream.toByteArray();

        ParseFile file = new ParseFile("file", data);
        file.saveInBackground();
        parsePost.put("picture", file);

        parsePost.saveInBackground();
    }

    public static Post getPostFromParseIndividual(String objectId) {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Posts");
        query.include("createdBy");

        Post post = new Post();

        try {
            ParseObject parseObject = query.get(objectId);

            // object found
            post.objectId = parseObject.getObjectId();
            post.title = parseObject.getString("title");
            post.itemDesc = parseObject.getString("description");
            post.category = parseObject.getString("category");
            post.price = parseObject.getDouble("price");
            post.createdBy = parseObject.getParseUser("createdBy");

            byte[] bytes = parseObject.getParseFile("picture").getData();
            BitmapFactory.Options headerOptions = new BitmapFactory.Options();
            headerOptions.inSampleSize = 4;
            post.headerPhoto = BitmapFactory.decodeByteArray(bytes, 0,bytes.length, headerOptions );

            ArrayList<ParseFile> pictures = (ArrayList) parseObject.get("pictures");
            post.PFPhotos = pictures;
            ArrayList<Bitmap> secondaryPicturesThumbnails = new ArrayList<>();
            ArrayList<Bitmap> secondaryPicturesFull = new ArrayList<>();
            for (ParseFile pfPictures: pictures) {
                bytes = pfPictures.getData();
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inSampleSize = 7;
                secondaryPicturesThumbnails.add(BitmapFactory.decodeByteArray(bytes, 0, bytes.length, options));
                options.inSampleSize = 3;
                secondaryPicturesFull.add(BitmapFactory.decodeByteArray(bytes, 0, bytes.length, options));
            }
            post.secondaryPicturesThumbnails = secondaryPicturesThumbnails;
            post.secondaryPictures = secondaryPicturesFull;

        } catch (ParseException e) {
            //Something wrong happened
        }

        return post;
    }

    public static List<Post> getPostsFromParse(String parameter, int skip/*, final Vector vec*/) {
        List<String> browseKeys = Arrays.asList("objectId", "title", "category", "price", "thumbnail");
        List<Post> retrievedPosts = new ArrayList<>();

        ParseQuery<ParseObject> query = ParseQuery.getQuery("Posts");
        if (parameter.equals("RECENTS")) { //Gettings most recent posts
            query.setSkip(skip);
            query.setLimit(20);
            query.include("createdBy");
            query.selectKeys(browseKeys);
            query.orderByDescending("createdAt");
        } else if (parameter.equals("USER")) { //Getting a user's posts
            query.whereEqualTo("createdBy", ParseUser.getCurrentUser());
            query.selectKeys(browseKeys);
            query.include("createdBy");
        } else { //Getting a category's posts
            query.setSkip(skip);
            query.setLimit(20);
            query.include("createdBy");
            query.selectKeys(browseKeys);
            query.whereEqualTo("category", parameter);

        }

        try {
            List<ParseObject> results = query.find();

            for (ParseObject parseObject: results) {
                Post post = new Post();

                post.objectId = parseObject.getObjectId();
                post.title = parseObject.getString("title");
                post.category = parseObject.getString("category");
                post.price = parseObject.getDouble("price");

                byte[] bytes = parseObject.getParseFile("thumbnail").getData();
                post.thumbnail = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);

                retrievedPosts.add(post);

            }
        } catch (ParseException e) {
            //Something went wrong!
        }

        return retrievedPosts;
    }

    public static void deleteFromParse(String objectId) {
        ParseObject post = new ParseObject(objectId);

        post.deleteInBackground();
    }

    public static void getImage(ParseFile image, final Post post, final String type) {
        image.getDataInBackground(new GetDataCallback() {
            @Override
            public void done(byte[] bytes, ParseException e) {
                if (type.equals("THUMBNAIL")) {
                    post.thumbnail = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                } else if (type.equals("HEADER")) {
                    post.headerPhoto = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                } else {
                    post.photos.add(BitmapFactory.decodeByteArray(bytes, 0, bytes.length));
                }
            }
        });
    }

    public static void logout () {
        ParseUser.logOut();
    }
}
