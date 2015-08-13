package tonychen.agora.BackEnd;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

import com.parse.Parse;
import com.parse.ParseFile;
import com.parse.ParseUser;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by tonyc on 6/22/2015.
 */
public class Post implements Serializable{
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
    public ArrayList<Bitmap> photos;
    public ArrayList<Bitmap> secondaryPictures;
    public ArrayList<Bitmap> secondaryPicturesThumbnails;

    public ParseFile PFheaderPhoto;
    public ParseFile PFthumbnail;
    public ArrayList<ParseFile> PFPhotos;
}
