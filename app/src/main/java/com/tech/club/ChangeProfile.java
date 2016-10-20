package com.tech.club;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.Toolbar;
import android.transition.TransitionInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.pkmmte.view.CircularImageView;
import com.soundcloud.android.crop.Crop;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class ChangeProfile extends AppCompatActivity {


    private static final int PICK_IMAGE = 1;
    static boolean profile_uploaded = false;
    FloatingActionButton bt_next2;
    Uri selectedImage;
    private int GET_FROM_GALLERY = 3;
    private Bitmap bitmap;
    private int MEDIA_TYPE_IMAGE = 1;
    private Uri fileUri;
    private ProgressDialog pdialog;
    private CircularImageView user_img;
    private EditText user_tagline;
    private Button bt_change_tag;
    private String user_old_tag;
    private boolean click = true;
    private String updated_tag;
    private RelativeLayout mainContent, color_top, color_bottom;
    private String myprofile;
    private Context context;

    public static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image

        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) > reqHeight
                    && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }

    private static int exifToDegrees(int exifOrientation) {
        if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_90) {
            return 90;
        } else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_180) {
            return 180;
        } else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_270) {
            return 270;
        }
        return 0;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().setSharedElementEnterTransition(TransitionInflater.from(this).inflateTransition(R.transition.shared_element_transition));
        }


        setContentView(R.layout.activity_change_profile);

        Toolbar toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        pdialog = new ProgressDialog(this);
        pdialog.setCancelable(false);
        pdialog.setMessage("Updating data..");

        context = ChangeProfile.this;


        user_tagline = (EditText) findViewById(R.id.et_tag);
        user_tagline.setEnabled(false);
        bt_change_tag = (Button) findViewById(R.id.bt_tag_change);
        color_bottom = (RelativeLayout) findViewById(R.id.layout_top);
        color_top = (RelativeLayout) findViewById(R.id.layout_bottom);

        bt_next2 = (FloatingActionButton) findViewById(R.id.upload_ques_fab);
      /*  bt_upload2 = (Button) findViewById(R.id.bt_upload1);*/
        user_img = (CircularImageView) findViewById(R.id.change_pic);
        mainContent = (RelativeLayout) findViewById(R.id.main_content);

        Toast.makeText(ChangeProfile.this, myprofile, Toast.LENGTH_SHORT);


        bt_change_tag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (click) {
                    click = false;
                    bt_change_tag.setBackgroundResource(R.drawable.ic_save_white_24dp);
                    user_tagline.setEnabled(true);

                } else {
                    click = true;
                    updated_tag = user_tagline.getText().toString();
                    pdialog.show();

                    final ParseQuery<ParseUser> query = ParseUser.getQuery();
                    query.whereEqualTo("username", ParseUser.getCurrentUser().getUsername());
                    query.getFirstInBackground(new GetCallback<ParseUser>() {
                        @Override
                        public void done(ParseUser parseUser, ParseException e) {
                            if (e == null) {
                                parseUser.put("tagline", updated_tag);
                                parseUser.saveInBackground(new SaveCallback() {
                                    @Override
                                    public void done(ParseException e) {
                                        if (e == null) {
                                            user_tagline.setText(updated_tag);
                                            pdialog.dismiss();
                                        } else {

                                        }
                                    }
                                });

                            } else {
                                pdialog.dismiss();
                                Snackbar.make(mainContent, e.getMessage(), Snackbar.LENGTH_SHORT).show();
                            }


                        }
                    });


                    user_tagline.setEnabled(false);
                    bt_change_tag.setBackgroundResource(R.drawable.ic_mode_edit_white_24dp);

                }

            }
        });


        Intent intent = getIntent();
        if (intent != null) {
            myprofile = intent.getStringExtra("profileurl");
            Picasso.with(context).load(myprofile).placeholder(R.drawable.user1).error(R.drawable.user1).into(new Target() {
                @Override
                public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                    user_img.setImageBitmap(bitmap);

                    Palette palette = Palette.from(bitmap).generate();
                    Palette.Swatch swatch = palette.getVibrantSwatch();
                    Palette.Swatch swatch1 = palette.getDarkVibrantSwatch();

                    if (swatch != null && swatch1 != null) {
                        color_top.setBackgroundColor(swatch.getRgb());
                        color_bottom.setBackgroundColor(swatch1.getRgb());
                    }
                    else {

                        swatch = palette.getMutedSwatch();
                        swatch1 = palette.getDarkMutedSwatch();
                        if (swatch != null && swatch1 != null) {
                            color_top.setBackgroundColor(swatch.getRgb());
                            color_bottom.setBackgroundColor(swatch1.getRgb());
                        }
                        else{


                        }
                    }
                }

                @Override
                public void onBitmapFailed(Drawable errorDrawable) {

                }

                @Override
                public void onPrepareLoad(Drawable placeHolderDrawable) {

                }
            });


            user_old_tag = intent.getStringExtra("tagline");
            user_tagline.setText(user_old_tag);
        }


        user_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                /*Intent intent = new Intent();
                intent.setType("image*//*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);*/
                /*startActivityForResult(new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI), GET_FROM_GALLERY);*/

                fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);
                Crop.pickImage(ChangeProfile.this);


            }
        });


        bt_next2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (profile_uploaded) {
                    pdialog.show();
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                    final byte[] image = stream.toByteArray();

                    ParseFile file = null;
                    file = new ParseFile("profile.png", image);

                    final ParseFile finalFile = file;
                    file.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {


                            if (e == null) {

                                ParseQuery<ParseObject> query = ParseQuery.getQuery("Profile");
                                query.whereEqualTo("user", ParseUser.getCurrentUser().getUsername());
                                query.getFirstInBackground(new GetCallback<ParseObject>() {
                                    @Override
                                    public void done(ParseObject parseObject, ParseException e) {
                                        if (e == null) {
                                            parseObject.put("image", finalFile);
                                            parseObject.saveInBackground(new SaveCallback() {
                                                @Override
                                                public void done(ParseException e) {
                                                    if (e == null) {
                                                        try {
                                                            File Dir = new File(Environment.getExternalStoragePublicDirectory(
                                                                    Environment.DIRECTORY_PICTURES), "Clasick");

                                                            if (Dir.isDirectory()) {
                                                                String[] other = Dir.list();
                                                                for (int i = 0; i < other.length; i++)
                                                                    new File(Dir, other[i]).delete();

                                                            }
                                                        } catch (Exception ex) {
                                                            ex.printStackTrace();

                                                        }

                                                        pdialog.dismiss();
                                                        Intent i = new Intent(ChangeProfile.this, Profile.class);
                                                        i.putExtra("user", ParseUser.getCurrentUser().getUsername());
                                                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                        overridePendingTransition(R.anim.right_in, R.anim.right_out);
                                                        startActivity(i);
                                                        finish();
                                                    } else {
                                                        pdialog.dismiss();
                                                        Toast.makeText(ChangeProfile.this, "Try again", Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            });

                                        } else {
                                            pdialog.dismiss();

                                        }
                                    }
                                });


                            } else {
                                pdialog.dismiss();
                                //error in saving
                            }
                        }
                    });
                } else {
                    Toast.makeText(ChangeProfile.this, "Select an Image", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

    private Uri getOutputMediaFileUri(int type) {
        return Uri.fromFile(getOutputMediaFile(type));
    }

    private File getOutputMediaFile(int type) {
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "Clasick");


        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {

                return null;
            }
        }
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File mediaFile;
        if (type == MEDIA_TYPE_IMAGE) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                    "IMG_" + timeStamp + ".jpg");
        } else {
            return null;
        }

        return mediaFile;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {


       /* super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GET_FROM_GALLERY && resultCode == Activity.RESULT_OK) {
             selectedImage = data.getData();
            String[] filePathColumn ={MediaStore.Images.Media.DATA};
            Cursor cursor=getContentResolver().query(selectedImage,filePathColumn,null,null,null);
            cursor.moveToFirst();
            int columnIndex =cursor.getColumnIndex(filePathColumn[0]);
            String picturePath =cursor.getString(columnIndex);
            cursor.close();
            user_img.setImageBitmap(BitmapFactory.decodeFile(picturePath));
        }*/


        if (requestCode == Crop.REQUEST_PICK && resultCode == RESULT_OK) {
            beginCrop(data.getData());
        } else if (requestCode == Crop.REQUEST_CROP) {
            handleCrop(resultCode, data);
        }
    }

    private void beginCrop(Uri source) {
        // Uri destination = Uri.fromFile(new File(getCacheDir(), "cropped"));
        Uri destination = fileUri;
        Crop.of(source, destination).asSquare().start(this);

    }

    private void handleCrop(int resultCode, Intent result) {
        if (resultCode == RESULT_OK) {
            /*ChangeProfile.setImageURI(Crop.getOutput(result));*/


            Uri output = Crop.getOutput(result);
            String filepath = output.getPath();

            bitmap = decodeSampledBitmapFromFile(filepath, 200, 200);
            bitmap = getRotatedImage(filepath, bitmap);
            user_img.setImageBitmap(bitmap);
            profile_uploaded = true;

        } else if (resultCode == Crop.RESULT_ERROR) {
            Toast.makeText(this, Crop.getError(result).getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    public Bitmap decodeSampledBitmapFromFile(String filePath,
                                              int reqWidth, int reqHeight) {

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(filePath, options);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_change_profile, menu);
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


        if (id == android.R.id.home) {
            super.onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {

        super.onBackPressed();
    }

    public Bitmap getRotatedImage(String path, Bitmap bmp) {

        ExifInterface exif = null;
        try {
            exif = new ExifInterface(path);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        int rotation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
        int rotationInDegrees = exifToDegrees(rotation);

        Matrix matrix = new Matrix();
        if (rotation != 0f) {
            matrix.preRotate(rotationInDegrees);
        }


        Bitmap rotatedBitmap = Bitmap.createBitmap(bmp, 0, 0, bmp.getWidth(),
                bmp.getHeight(), matrix, true);


        return rotatedBitmap;

    }


}
