package com.alfianwibowo.pinkcell;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements SearchView.OnQueryTextListener{

    DBManager dbManager;
    boolean b;
    SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        snackbarHapus();

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), TambahData.class));
                MainActivity.this.finish();
            }
        });

        dbManager = new DBManager(this);
        loadTable();

    }

    ArrayList<AdapterItems> listnewsData = new ArrayList<>();
    MyCustomAdapter myadapter;

    private void loadTable() {
        listnewsData.clear();
        String[] projection = {DBManager.id, DBManager.merk, DBManager.type, DBManager.ram,
                DBManager.internal, DBManager.kelengkapan, DBManager.jaringan, DBManager.deskripsi,
                DBManager.harga_jual, DBManager.harga_jual_maks};
        String sortorder = DBManager.id + " DESC";
        Cursor cursor = dbManager.Query(projection, null, null, sortorder);
        if (cursor.moveToFirst()) {
            do {
                listnewsData.add(new AdapterItems(cursor.getString(cursor.getColumnIndex(DBManager.id)),
                        cursor.getString(cursor.getColumnIndex(DBManager.merk)),
                        cursor.getString(cursor.getColumnIndex(DBManager.type)),
                        cursor.getString(cursor.getColumnIndex(DBManager.ram)),
                        cursor.getString(cursor.getColumnIndex(DBManager.internal)),
                        cursor.getString(cursor.getColumnIndex(DBManager.kelengkapan)),
                        cursor.getString(cursor.getColumnIndex(DBManager.jaringan)),
                        cursor.getString(cursor.getColumnIndex(DBManager.deskripsi)),
                        cursor.getString(cursor.getColumnIndex(DBManager.harga_jual)),
                        cursor.getString(cursor.getColumnIndex(DBManager.harga_jual_maks))));
            } while (cursor.moveToNext());
        }

        myadapter = new MyCustomAdapter(listnewsData);
        ListView lsNews = findViewById(R.id.lvtampil);
        lsNews.setAdapter(myadapter);
        TextView tvKosong = findViewById(R.id.tvkosong);

        if(myadapter.isEmpty()) {
            lsNews.setVisibility(View.GONE);
            tvKosong.setVisibility(View.VISIBLE);
        } else {
            lsNews.setVisibility(View.VISIBLE);
            tvKosong.setVisibility(View.GONE);
        }
    }

    private class MyCustomAdapter extends BaseAdapter {
        ArrayList<AdapterItems> listnewsDataAdpater ;

        MyCustomAdapter(ArrayList<AdapterItems> listnewsDataAdpater) {
            this.listnewsDataAdpater=listnewsDataAdpater;
        }

        @Override
        public int getCount() {
            return listnewsDataAdpater.size();
        }

        @Override
        public String getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater mInflater = getLayoutInflater();
            View myView = mInflater.inflate(R.layout.layout_ticket,null);
            final AdapterItems s = listnewsDataAdpater.get(position);

            final TextView tvmerk = myView.findViewById(R.id.tvmerk);
            tvmerk.setText(s.getMerk());

            TextView tvtype = myView.findViewById(R.id.tvtype);
            tvtype.setText(s.getType());

            if(!s.getHarga_jual().isEmpty()){
                TextView tvhargajual = myView.findViewById(R.id.tvhargajual);
                String rp = "Rp. " + s.getHarga_jual();
                tvhargajual.setText(rp);
            }

            ImageView imageView = myView.findViewById(R.id.ivimage);
            imageView.setVisibility(View.VISIBLE);
            File imgFile = new File(Environment.getExternalStorageDirectory().
                    getAbsolutePath() + "/pinkcell/img/" + s.getMerk() + " " +
                    s.getType() + ".jpg");
            if(imgFile.isFile()){
                Bitmap bitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                imageView.setImageBitmap(bitmap);
            } else {
                imageView.setVisibility(View.GONE);
            }

            final ListView listView = findViewById(R.id.lvtampil);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    String idlist = listnewsData.get(position).getId();

                    Intent intent = new Intent(getApplicationContext(), TampilData.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("idlist", idlist);
                    intent.putExtras(bundle);
                    startActivity(intent);
                    MainActivity.this.finish();
                }
            });

            return myView;
        }
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {

        listnewsData.clear();

        String like = " like ? or ";
        String selection = DBManager.merk + like + DBManager.type + like + DBManager.ram + like +
                DBManager.internal + like + DBManager.kelengkapan + like + DBManager.jaringan +
                like + DBManager.deskripsi + like + DBManager.harga_jual + like +
                DBManager.harga_jual_maks + " like ?";

        String args = "%" + newText + "%";
        String[] selectionargs = {args,args,args,args,args,args,args,args,args};
        String sortorder = DBManager.id + " DESC";
        Cursor cursor = dbManager.Query(null, selection, selectionargs, sortorder);
        if (cursor.moveToFirst()) {
            do {
                listnewsData.add(new AdapterItems(cursor.getString(cursor.getColumnIndex(DBManager.id)),
                        cursor.getString(cursor.getColumnIndex(DBManager.merk)),
                        cursor.getString(cursor.getColumnIndex(DBManager.type)),
                        cursor.getString(cursor.getColumnIndex(DBManager.ram)),
                        cursor.getString(cursor.getColumnIndex(DBManager.internal)),
                        cursor.getString(cursor.getColumnIndex(DBManager.kelengkapan)),
                        cursor.getString(cursor.getColumnIndex(DBManager.jaringan)),
                        cursor.getString(cursor.getColumnIndex(DBManager.deskripsi)),
                        cursor.getString(cursor.getColumnIndex(DBManager.harga_jual)),
                        cursor.getString(cursor.getColumnIndex(DBManager.harga_jual_maks))));
            } while (cursor.moveToNext());
        }

        myadapter = new MyCustomAdapter(listnewsData);
        ListView lsNews = findViewById(R.id.lvtampil);
        lsNews.setAdapter(myadapter);

        return false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        MenuItem menuItem = menu.findItem(R.id.search);
        searchView = (SearchView) menuItem.getActionView();
        searchView.setQueryHint("Cari...");
        searchView.setOnQueryTextListener(this);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_about:
                startActivity(new Intent(getApplicationContext(), About.class));
                MainActivity.this.finish();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void snackbarHapus(){
        b = false;
        Bundle bundle = getIntent().getExtras();
        if (bundle != null){
            b = bundle.getBoolean("hapus");
            if (b) {
                Snackbar.make(findViewById(R.id.cLayout), "Data Berhasil Dihapus",
                        Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        }

    }

}