package com.tech.club;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Typeface;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Environment;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.soundcloud.android.crop.Crop;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class UserImage extends AppCompatActivity {
    ImageView user_img;
    Button bt_upload, bt_next, bt_skip;
    private static final int PICK_IMAGE = 1;
    private int GET_FROM_GALLERY = 3;
    Uri selectedImage;
    private Bitmap bitmap;
    private int MEDIA_TYPE_IMAGE = 1;
    static boolean profile_uploaded = false;
    private Uri fileUri;
    private String profile_pic_uri;
    private Bitmap profile_pic_bmp;
    private ProgressDialog pdialog;
    private EditText tag;
    private EditText name;
    private RelativeLayout mainContent;
    private String user_string;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_image);

        Toolbar toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        tag = (EditText) findViewById(R.id.et_tagline);
        name = (EditText) findViewById(R.id.et_name);
        bt_next = (Button) findViewById(R.id.next);
        bt_upload = (Button) findViewById(R.id.bt_upload);
        user_img = (ImageView) findViewById(R.id.load_img);
        mainContent = (RelativeLayout) findViewById(R.id.main_content);

        user_string = ParseUser.getCurrentUser().getUsername();

        ParseFile file = null;



        pdialog = new ProgressDialog(this);
        pdialog.setCancelable(false);
        pdialog.setMessage("Finalizing data..");


        bt_upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Intent intent = new Intent();
                intent.setType("image*//*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);*/
                /*startActivityForResult(new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI), GET_FROM_GALLERY);*/
                fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);
                Crop.pickImage(UserImage.this);

            }
        });




        bt_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String tagdata = tag.getText().toString();
                final String namedata = name.getText().toString();

                if (!(tagdata.length() > 0) || !(namedata.length() > 0)) {
                    Snackbar.make(mainContent, "Parameters not filled.", Snackbar.LENGTH_SHORT).show();
                } else {
                    pdialog.show();
                    if (profile_uploaded) {
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

                                    final ParseObject testObject = new ParseObject("Profile");
                                    testObject.put("user", user_string);
                                    testObject.put("image", finalFile);
                                    testObject.saveInBackground(new SaveCallback() {
                                        @Override
                                        public void done(ParseException e) {
                                            if (e == null) {


                                                ParseQuery<ParseUser> query = ParseUser.getQuery();
                                                query.whereEqualTo("username", user_string);
                                                query.getFirstInBackground(new GetCallback<ParseUser>() {
                                                    @Override
                                                    public void done(ParseUser parseUser, ParseException e) {
                                                        if (e == null) {
                                                            parseUser.put("Name", namedata);
                                                            parseUser.put("tagline", tagdata);
                                                            parseUser.put("Profile", testObject);
                                                            parseUser.saveInBackground(new SaveCallback() {
                                                                @Override
                                                                public void done(ParseException e) {

                                                                }
                                                            });
                                                        } else {
                                                            Snackbar.make(mainContent, e.getMessage(), Snackbar.LENGTH_SHORT).show();
                                                        }

                                                    }

                                                });


                                                pdialog.dismiss();
                                                startActivity(new Intent(UserImage.this, MainActivity.class));
                                            } else {
                                                pdialog.dismiss();
                                                Toast.makeText(UserImage.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                            }

                                        }

                                    });

                                } else {
                                    pdialog.dismiss();
                                    Toast.makeText(UserImage.this, "Try again", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

                    } else {
                        final ParseObject testObject1 = new ParseObject("Profile");
                        testObject1.put("user", ParseUser.getCurrentUser().getUsername());
                        testObject1.saveInBackground(new SaveCallback() {
                            @Override
                            public void done(ParseException e) {
                                if (e == null) {
                                    ParseQuery<ParseUser> query = ParseUser.getQuery();
                                    query.whereEqualTo("username", user_string);
                                    query.getFirstInBackground(new GetCallback<ParseUser>() {
                                        @Override
                                        public void done(ParseUser parseUser, ParseException e) {
                                            if (e == null) {
                                                parseUser.put("Name", namedata);
                                                parseUser.put("tagline", tagdata);
                                                parseUser.put("Profile", testObject1);
                                                parseUser.saveInBackground(new SaveCallback() {
                                                    @Override
                                                    public void done(ParseException e) {

                                                    }
                                                });
                                            }
                                            else {
                                                Snackbar.make(mainContent, e.getMessage(), Snackbar.LENGTH_SHORT).show();
                                            }

                                        }

                                    });

                                    pdialog.dismiss();
                                    startActivity(new Intent(UserImage.this, MainActivity.class));
                                } else {
                                    pdialog.dismiss();
                                    Toast.makeText(UserImage.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

                    }
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
            user_img.setImageURI(Crop.getOutput(result));


            Uri output = Crop.getOutput(result);
            String filepath = output.getPath();
            bitmap = decodeSampledBitmapFromFile(filepath, 200, 200);
            bitmap = getRotatedImage(filepath, bitmap);
            user_img.setImageBitmap(bitmap);
          /*  bt_upload.setVisibility(View.INVISIBLE);*/

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

}



