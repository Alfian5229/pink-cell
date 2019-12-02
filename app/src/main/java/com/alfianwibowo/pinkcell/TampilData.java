package com.alfianwibowo.pinkcell;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;

import pub.devrel.easypermissions.EasyPermissions;

public class TampilData extends AppCompatActivity {

    DBManager dbManager;
    ImageView imageView;
    String id;
    File fileImage;

    TextView tvmerk;
    TextView tvtype;
    TextView tvram;
    TextView tvinternal;
    TextView tvkelengkapan;
    TextView tvjaringan;
    TextView tvdeskripsi;
    TextView tvhargajual;
    TextView tvhargajualmaks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tampil_data);

        tvmerk = findViewById(R.id.tvtmerk);
        tvtype = findViewById(R.id.tvttype);
        tvram = findViewById(R.id.tvtram);
        tvinternal = findViewById(R.id.tvtinternal);
        tvkelengkapan = findViewById(R.id.tvtkelengkapan);
        tvjaringan = findViewById(R.id.tvtjaringan);
        tvdeskripsi = findViewById(R.id.tvtdeskripsi);
        tvhargajual = findViewById(R.id.tvthargajual);
        tvhargajualmaks = findViewById(R.id.tvthargajualmaks);

        dbManager = new DBManager(this);
        imageView = findViewById(R.id.ivtampilgambar);
        fileImage = tampilData();

        imageView.setClickable(true);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] galleryPermissions = {Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE};
                if (EasyPermissions.hasPermissions(getApplicationContext(), galleryPermissions)) {
                    openImage();
                } else {
                    EasyPermissions.requestPermissions(TampilData.this,
                            "Access for storage", 101, galleryPermissions);
                    openImage();
                }
            }
        });
    }

    private void openImage() {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        Uri imageUri = FileProvider.getUriForFile(getApplicationContext(),
                "com.alfianwibowo.pinkcell.provider", fileImage);
        intent.setDataAndType(imageUri, "image/*");
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        startActivity(intent);
    }

    private File tampilData() {
        Bundle bundle = getIntent().getExtras();
        assert bundle != null;
        id = bundle.getString("idlist");

        String[] projection = {"id", "merk", "type", "ram", "internal", "kelengkapan",
                "jaringan", "deskripsi", "harga_jual", "harga_jual_maks"};
        String selection = "id=?";
        String[] selectionargs = {id};
        Cursor cursor = dbManager.Query(projection, selection, selectionargs, null);
        cursor.moveToFirst();

        String smerk = cursor.getString(cursor.getColumnIndex(projection[1]));
        tvmerk.setText(smerk);
        String stype = cursor.getString(cursor.getColumnIndex(projection[2]));
        tvtype.setText(stype);
        tvram.setText(cursor.getString(cursor.getColumnIndex(projection[3])));
        tvinternal.setText(cursor.getString(cursor.getColumnIndex(projection[4])));
        tvkelengkapan.setText(cursor.getString(cursor.getColumnIndex(projection[5])));
        tvjaringan.setText(cursor.getString(cursor.getColumnIndex(projection[6])));
        tvdeskripsi.setText(cursor.getString(cursor.getColumnIndex(projection[7])));
        tvhargajual.setText(cursor.getString(cursor.getColumnIndex(projection[8])));
        tvhargajualmaks.setText(cursor.getString(cursor.getColumnIndex(projection[9])));

        File imgFile = new File(Environment.getExternalStorageDirectory().
                getAbsolutePath() + "/pinkcell/img/" + smerk + " " + stype + ".jpg");
        if(imgFile.isFile()){
            Bitmap bitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
            imageView.setImageBitmap(bitmap);
        }
        return imgFile;
    }

    public void hapusData(View view) {
        new AlertDialog.Builder(this)
                .setMessage("apakah Anda Yakin?")
                .setCancelable(true)
                .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        String selection = "id=?";
                        String[] selectionArgs = {id};
                        int temp;
                        temp = dbManager.Delete(selection, selectionArgs);
                        if(temp != 0) {
                            if(fileImage.isFile()){
                                boolean b = fileImage.delete();
                                if(!b){
                                    return;
                                }
                            }
                            Bundle bundle = new Bundle();
                            bundle.putBoolean("hapus", true);
                            startActivity(new Intent(getApplicationContext(),
                                    MainActivity.class).putExtras(bundle));
                            TampilData.this.finish();
                        }
                    }
                })
                .setNegativeButton("Tidak", null)
                .show();
    }

    public void updateData(View view) {
        Intent intent = new Intent(getApplicationContext(), UpdateData.class);
        Bundle bundle = new Bundle();
        bundle.putString("id", id);
        bundle.putString("merk", tvmerk.getText().toString());
        bundle.putString("type", tvtype.getText().toString());
        bundle.putString("ram", tvram.getText().toString());
        bundle.putString("internal", tvinternal.getText().toString());
        bundle.putString("kelengkapan", tvkelengkapan.getText().toString());
        bundle.putString("jaringan", tvjaringan.getText().toString());
        bundle.putString("deskripsi", tvdeskripsi.getText().toString());
        bundle.putString("hargajual", tvhargajual.getText().toString());
        bundle.putString("hargajualmaks", tvhargajualmaks.getText().toString());
        intent.putExtras(bundle);
        startActivity(intent);
        TampilData.this.finish();
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(getApplicationContext(), MainActivity.class));
        TampilData.this.finish();
    }

}
