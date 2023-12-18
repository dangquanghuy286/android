package com.example.danghuuquanghuy_bai12_tabnangcao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.Toast;


import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    String DB_PATH_SUFFIX = "/databases/";
    public static SQLiteDatabase database=null;
    public static String DATABASE_NAME="arirang.sqlite";
    EditText edttim;
    ListView lv1,lv2,lv3;
    ArrayList<Item> list1, list2, list3;
    myarrayAdapter myarray1, myarray2, myarray3;
    TabHost tab;
    ImageButton btnxoa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        processCopy(); //Copy CSDL arirang.sqlite
        //Mở cở sở dữ liệu đã copy. Lưu vào biến database
        database = openOrCreateDatabase("arirang.sqlite", MODE_PRIVATE, null);
        addControl(); //Hàm thêm các Controls
        addTim(); //Xử lý công việc tìm kiếm
        addEvents(); //Xử lý sự kiện khi chuyển Tab và các sự kiện khác
    }

    //Hàm khai báo và Add các Controls vào giao diện
    //Trên 3 Tab chúng ta có 3 ListView ứng với 3 Danh sách dữ liệu (Dữ liệu tìm kiếm, Danh
    //sách bài hát gốc, Danh sách bài hát yêu thích) và Adapter riêng
    private void addControl() {
        // TODO Auto-generated method stub
        btnxoa =(ImageButton) findViewById(R.id.btnxoa);
        tab=(TabHost)findViewById(R.id.mytab);
        tab.setup();
        TabHost.TabSpec tab1=tab.newTabSpec("t1");
        tab1.setContent(R.id.tab1);
        tab1.setIndicator("",getResources().getDrawable(R.drawable.search));
        tab.addTab(tab1);
        TabHost.TabSpec tab2=tab.newTabSpec("t2");
        tab2.setContent(R.id.tab2);
        tab2.setIndicator("",getResources().getDrawable(R.drawable.list1));
        tab.addTab(tab2);
        TabHost.TabSpec tab3=tab.newTabSpec("t3");
        tab3.setContent(R.id.tab3);
        tab3.setIndicator("",getResources().getDrawable(R.drawable.like));
        tab.addTab(tab3);
        edttim = (EditText) findViewById(R.id.edttim);
        lv1 = (ListView) findViewById(R.id.lv1);
        lv2 =(ListView) findViewById(R.id.lv2);
        lv3 = (ListView) findViewById(R.id.lv3);
        list1 = new ArrayList<Item>();
        list2 = new ArrayList<Item>();
        list3 = new ArrayList<Item>();
        myarray1 = new myarrayAdapter(MainActivity.this,
                R.layout.layout1,
                list1);
        myarray2 = new myarrayAdapter(MainActivity.this,
                R.layout.layout1,
                list2);
        myarray3 = new myarrayAdapter(MainActivity.this,
                R.layout.layout1,
                list3);
        lv1.setAdapter(myarray1);
        lv2.setAdapter(myarray2);
        lv3.setAdapter(myarray3);
    }
    //Xử lý sự kiện khi chuyển qua lại giữa các Tab Danh sách và Yêu Thích
    private void addEvents() {
        // TODO Auto-generated method stub
        tab.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String tabId) {
                // TODO Auto-generated method stub
                if (tabId.equalsIgnoreCase("t2")) {
                    addDanhsach();
                }
                if (tabId.equalsIgnoreCase("t3")) {
                    addYeuthich();
                }
            }
        });
        // Sự kiện khi Click vào Button xóa trên Tab Tìm kiếm
        btnxoa.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                edttim.setText("");
            }
        });
    }
    //Hàm thêm bài hát vào Listview trên Tab Yêu thích
    private void addYeuthich() {
        // TODO Auto-generated method stub
        myarray3.clear();
        Cursor c = database.rawQuery("SELECT * FROM ArirangSongList WHERE YEUTHICH = 1", null);
        c.moveToFirst();
        while(c.isAfterLast()==false)
        {
            list3.add(new Item(c.getString(0),c.getString(1),c.getInt(4)));
            c.moveToNext();
        }
        c.close();
        myarray3.notifyDataSetChanged();

    }
    // Hàm thêm bài hát vào Listview trên Tab Danh sách bài hát
    private void addDanhsach() {
        // TODO Auto-generated method stub
        myarray2.clear();
        Cursor c = database.rawQuery("SELECT * FROM ArirangSongList", null);
        c.moveToFirst();
        while(c.isAfterLast()==false)
        {
            list2.add(new Item(c.getString(0),c.getString(1),c.getInt(4)));
            c.moveToNext();
        }
        c.close();
        myarray2.notifyDataSetChanged();
    }
    //Hàm xử lý tìm kiếm bài hát theo Tiêu đề và Mã số
    private void addTim() {
        // TODO Auto-generated method stub
        //Sự kiện khi Thay đổi Text trong Edittext edttim
        edttim.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int
                    count) {
                getdata();
            }
            private void getdata() {
                // TODO Auto-generated method stub
                String dulieunhap =edttim.getText().toString();
                myarray1.clear();
                if (!edttim.getText().toString().equals("")) {
                    Cursor c = database.rawQuery("SELECT * FROM ArirangSongList WHERE TENBAIHAT LIKE '"+"%"+dulieunhap+"%"+"' OR MABH LIKE '"+"%"+dulieunhap+"%"+"'", null);
                    c.moveToFirst();
                    while(c.isAfterLast()==false)
                    {
                        list1.add(new Item(c.getString(0),c.getString(1),c.getInt(4)));
                        c.moveToNext();
                    }
                    c.close();
                }
                myarray1.notifyDataSetChanged();
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }
    //Hàm xử lý Copy CS dữ liệu từ thư mục assets vào hệ thống thư mục cài đặt
    private void processCopy() {
        //private app
        File dbFile = getDatabasePath(DATABASE_NAME);
        if (!dbFile.exists())
        {
            try{CopyDataBaseFromAsset();
                Toast.makeText(this, "Copying sucess from Assets folder", Toast.LENGTH_LONG).show();
            }
            catch (Exception e){
                Toast.makeText(this, e.toString(), Toast.LENGTH_LONG).show();
            }
        }
    }
    private String getDatabasePath() {
        return getApplicationInfo().dataDir + DB_PATH_SUFFIX+ DATABASE_NAME;
    }
    public void CopyDataBaseFromAsset() {
        // TODO Auto-generated method stub
        try {
            InputStream myInput;
            myInput = getAssets().open(DATABASE_NAME);
            // Path to the just created empty db
            String outFileName = getDatabasePath();
            //if the path doesn't exist first, create it
            File f = new File(getApplicationInfo().dataDir + DB_PATH_SUFFIX);
            if (!f.exists())
                f.mkdir();
            //Open the empty db as the output stream
            OutputStream myOutput = new FileOutputStream(outFileName);
            //transfer bytes from the inputfile to the outputfile
            byte[] buffer = new byte[1024];
            int length;
            while ((length = myInput.read(buffer)) > 0)
            {
                myOutput.write(buffer, 0, length);
            }
            //Close the streams
            myOutput.flush();
            myOutput.close();
            myInput.close();
        } catch (IOException e) {
            //TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}