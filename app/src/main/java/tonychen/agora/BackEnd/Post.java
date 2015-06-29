package tonychen.agora.BackEnd;

import android.graphics.Bitmap;
import com.parse.Parse;
import com.parse.ParseFile;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tonyc on 6/22/2015.
 */
public class Post {
    public String title;
    public String itemDesc;
    public String category;
    public String objectId;
    public String[] tags;
    public double price;

    public ParseUser createdBy;
    public String creatorFacebookId;

    public Bitmap headerPhoto;
    public Bitmap thumbnail;
    public List<Bitmap> photos;

    public ParseFile PFheaderPhoto;
    public ParseFile PFthumbnail;
    public ArrayList<ParseFile> PFPhotos;

    public boolean ready;

}
