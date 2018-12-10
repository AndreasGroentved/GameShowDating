package dating.innovative.gameshowdating.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import dating.innovative.gameshowdating.R;
import dating.innovative.gameshowdating.data.WebSocketHandler;
import dating.innovative.gameshowdating.util.BaseActivity;
import dating.innovative.gameshowdating.util.PreferenceManagerClass;
import dating.innovative.gameshowdating.util.SQLiteHelper;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import org.jetbrains.annotations.NotNull;

import java.io.*;

/*  Andreas Jeppesen - ajepp09@student.sdu.dk
    Emil Jensen - emije14@student.sdu.dk
    Nicolai Jensen - nije214@student.sdu.dk */

public class ImageSettingActivity extends BaseActivity {

    ImageView previewImageView;
    Button uploadImageButton;
    Button snapImageButton;
    Button saveImageButton;
    Uri uri;
    SQLiteHelper dbHelper;
    public static String photoPath;
    boolean fromAlbum = false;


    @NotNull
    @Override
    public Toolbar getToolBar() {
        return findViewById(R.id.toolbar_image_settings);
    }

    @Override
    public int getLayout() {
        return R.layout.activity_image_setting;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_setting);

        previewImageView = (ImageView) findViewById(R.id.imageSettingImagePreview);
        uploadImageButton = (Button) findViewById(R.id.imageSettingUploadButton);
        snapImageButton = (Button) findViewById(R.id.imageSettingTakeImageButton);
        saveImageButton = (Button) findViewById(R.id.imageSettingSaveButton);
        dbHelper = SQLiteHelper.getSqLiteHelperInstance(getApplicationContext());

        previewImageView.setVisibility(View.INVISIBLE);

        uploadImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent uploadImage = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(uploadImage, 0);
            }
        });

        snapImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent snapPictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (snapPictureIntent.resolveActivity(getPackageManager()) != null) {
                    File photoFile = null;
                    try {
                        photoFile = createImageFile();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    if (photoFile != null) {
                        uri = FileProvider.getUriForFile(getApplicationContext(), "dating.innovative.gameshowdating", photoFile);
                        snapPictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                        startActivityForResult(snapPictureIntent, 1);
                    }
                }
            }
        });

        saveImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PreferenceManagerClass.setPreferenceProfilePictureUpdated(getApplicationContext(), uri.toString());
                Intent backToCustomization = new Intent(getApplicationContext(), CustomizeProfileActivity.class);
                startActivity(backToCustomization);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            previewImageView.setVisibility(View.VISIBLE);
            if (requestCode == 0) {
                try {
                    uri = data.getData();
                    fromAlbum = true; //for data persistence
                    InputStream imageStream = getContentResolver().openInputStream(uri);
                    Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    selectedImage.compress(Bitmap.CompressFormat.PNG, 100, stream);
                    byte[] byteArray = stream.toByteArray();
                    System.out.println("her her her");
                    System.out.println(uri.toString());
                    WebSocketHandler.getInstance().updateProfilePicture(byteArray, new Function1<Boolean, Unit>() {
                        @Override
                        public Unit invoke(Boolean aBoolean) {
                            return null;
                        }
                    });
                    previewImageView.setImageBitmap(selectedImage);

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
            if (requestCode == 1) {
                try {
                    InputStream imageStream = getContentResolver().openInputStream(uri);
                    Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);

                    ExifInterface ei = new ExifInterface(photoPath);
                    int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                            ExifInterface.ORIENTATION_UNDEFINED);

                    Bitmap rotatedBitmap = null;
                    switch (orientation) {
                        case ExifInterface.ORIENTATION_ROTATE_90:
                            rotatedBitmap = rotateImage(selectedImage, 90);
                            break;

                        case ExifInterface.ORIENTATION_ROTATE_180:
                            rotatedBitmap = rotateImage(selectedImage, 180);
                            break;

                        case ExifInterface.ORIENTATION_ROTATE_270:
                            rotatedBitmap = rotateImage(selectedImage, 270);
                            break;

                        case ExifInterface.ORIENTATION_NORMAL:
                        default:
                            rotatedBitmap = selectedImage;
                    }

                    fromAlbum = false; // data persistence
                    previewImageView.setImageBitmap(rotatedBitmap);


                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    selectedImage.compress(Bitmap.CompressFormat.PNG, 100, stream);
                    byte[] byteArray = stream.toByteArray();
                    WebSocketHandler.getInstance().updateProfilePicture(byteArray, new Function1<Boolean, Unit>() {
                        @Override
                        public Unit invoke(Boolean aBoolean) {
                            return null;
                        }
                    });

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

    }

    private File createImageFile() throws IOException {
        String imageName = "profilePictureImage_" + PreferenceManagerClass.getUsername(getApplicationContext());
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(imageName, ".jpg", storageDir);
        photoPath = image.getAbsolutePath();
        return image;
    }

    public static Bitmap rotateImage(Bitmap source, float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(),
                matrix, true);
    }

}
