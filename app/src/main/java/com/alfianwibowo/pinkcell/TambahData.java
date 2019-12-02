package com.alfianwibowo.pinkcell;

import android.Manifest;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Locale;
import java.util.Objects;

import pub.devrel.easypermissions.EasyPermissions;

public class TambahData extends AppCompatActivity {

    public static final int RESULT_LOAD_IMAGE = 1;
    public static final int TAKE_PICTURE = 2;

    ImageView imageView;
    EditText edmerk;
    EditText edtype;
    EditText edram;
    EditText edinternal;
    EditText edkelengkapan;
    EditText edjaringan;
    EditText eddeskripsi;
    EditText edhargajual;
    EditText edhargajualmaks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tambah_data);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        imageView = findViewById(R.id.imgview);
        edmerk = findViewById(R.id.edmerk);
        edtype = findViewById(R.id.edtype);
        edram = findViewById(R.id.edram);
        edinternal = findViewById(R.id.edinternal);
        edkelengkapan = findViewById(R.id.edkelengkapan);
        edjaringan = findViewById(R.id.edjaringan);
        eddeskripsi = findViewById(R.id.eddeskripsi);
        edhargajual = findViewById(R.id.edhargajual);
        edhargajualmaks = findViewById(R.id.edhargajualmaks);

        Button bpfg = findViewById(R.id.bpicturefromgallery);
        bpfg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] galleryPermissions = {Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE};
                if (EasyPermissions.hasPermissions(getApplicationContext(), galleryPermissions)) {
                    pickImageFromGallery();
                } else {
                    EasyPermissions.requestPermissions(TambahData.this, "Access for storage",
                            101, galleryPermissions);
                    pickImageFromGallery();
                }
            }
        });

        Button bpfp = findViewById(R.id.bpicturefromphoto);
        bpfp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] galleryPermissions = {Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE};
                if (EasyPermissions.hasPermissions(getApplicationContext(), galleryPermissions)) {
                    pickImageFromPhoto();
                } else {
                    EasyPermissions.requestPermissions(TambahData.this, "Access for storage",
                            101, galleryPermissions);
                    pickImageFromPhoto();
                }
            }
        });
    }

    void pickImageFromGallery() {
        Intent intent = new Intent(
                Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, RESULT_LOAD_IMAGE);
    }

    void pickImageFromPhoto() {

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
        File output = new File(dir, "temp.jpg");

        Uri imageUri = FileProvider.getUriForFile(
                getApplicationContext(), "com.alfianwibowo.pinkcell.provider",
                output);

        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(intent, TAKE_PICTURE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            String[] filepathcolumn = { MediaStore.Images.Media.DATA };

            assert selectedImage != null;
            Cursor cursor = getContentResolver().query(selectedImage,
                    filepathcolumn, null, null, null);
            assert cursor != null;
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filepathcolumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();

            Bitmap bitmap = new BitmapDrawable(getApplicationContext().getResources(),
                    BitmapFactory.decodeFile(picturePath)).getBitmap();
            int nh = (int) ( bitmap.getHeight() * (1024.0 / bitmap.getWidth()));
            Bitmap scaled = Bitmap.createScaledBitmap(bitmap, 1024, nh, true);
            imageView.setImageBitmap(scaled);
        }

        if(requestCode == TAKE_PICTURE && resultCode == RESULT_OK) {
            //Bitmap bitmap1 = (Bitmap) Objects.requireNonNull(data.getExtras()).get("data");
            File sdCard = Environment.getExternalStorageDirectory();
            File imgFile = new File(sdCard.getAbsolutePath() + "/DCIM/temp.jpg");
            Bitmap bitmap1 = BitmapFactory.decodeFile(imgFile.getAbsolutePath());

            Bitmap bitmap = new BitmapDrawable(getApplicationContext().getResources(),
                    bitmap1).getBitmap();
            int nh = (int) ( bitmap.getHeight() * (1024.0 / bitmap.getWidth()));
            Bitmap scaled = Bitmap.createScaledBitmap(bitmap, 1024, nh, true);
            imageView.setImageBitmap(scaled);
        }
    }

    public void tambahDataHP(View view) {

        DBManager dbManager = new DBManager(this);
        ContentValues values = new ContentValues();

        if(TextUtils.isEmpty(edmerk.getText().toString().trim())) {
            if(TextUtils.isEmpty(edtype.getText().toString().trim())) {
                Snackbar.make(view, "Merk dan Type harus diisi", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
            else{
                Snackbar.make(view, "Merk harus diisi", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        } else if(TextUtils.isEmpty(edtype.getText().toString().trim())){
            Snackbar.make(view, "Type harus diisi", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
        } else {
            values.put(DBManager.merk, edmerk.getText().toString());
            values.put(DBManager.type, edtype.getText().toString());
            values.put(DBManager.ram, edram.getText().toString());
            values.put(DBManager.internal, edinternal.getText().toString());
            values.put(DBManager.kelengkapan, edkelengkapan.getText().toString());
            values.put(DBManager.jaringan, edjaringan.getText().toString());
            values.put(DBManager.deskripsi, eddeskripsi.getText().toString());
            values.put(DBManager.harga_jual, edhargajual.getText().toString());
            values.put(DBManager.harga_jual_maks, edhargajualmaks.getText().toString());
            dbManager.Insert(values);

            Snackbar.make(view, "Berhasil Tambah Data", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();

            String[] galleryPermissions = {Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE};

            if (EasyPermissions.hasPermissions(getApplicationContext(), galleryPermissions)) {
                BitmapDrawable draw = (BitmapDrawable) imageView.getDrawable();

                if(draw != null) {
                    Bitmap bitmap = draw.getBitmap();
                    FileOutputStream outStream = null;
                    File sdCard = Environment.getExternalStorageDirectory();
                    File dir = new File(sdCard.getAbsolutePath() + "/pinkcell/img");
                    boolean isDirectoryCreated = dir.exists();
                    if (!isDirectoryCreated) {
                        isDirectoryCreated = dir.mkdirs();
                    }
                    if(isDirectoryCreated) {
                        String smerk = edmerk.getText().toString();
                        String stype = edtype.getText().toString();
                        String fileName = String.format(Locale.getDefault(),
                                smerk + " " + stype + ".jpg", System.currentTimeMillis());
                        File outFile = new File(dir, fileName);
                        try {
                            outStream = new FileOutputStream(outFile);
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outStream);
                        try {
                            Objects.requireNonNull(outStream).flush();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        try {
                            Objects.requireNonNull(outStream).close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }

            } else {
                EasyPermissions.requestPermissions((Activity) getApplicationContext(), "Access for storage",
                        101, galleryPermissions);
            }
        }

    }

    public void clearData(View view) {
        imageView.setImageDrawable(null);
        edmerk.setText(null);
        edtype.setText(null);
        edram.setText(null);
        edinternal.setText(null);
        edkelengkapan.setText(null);
        edjaringan.setText(null);
        eddeskripsi.setText(null);
        edhargajual.setText(null);
        edhargajualmaks.setText(null);
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(getApplicationContext(), MainActivity.class));
        TambahData.this.finish();
    }
}
