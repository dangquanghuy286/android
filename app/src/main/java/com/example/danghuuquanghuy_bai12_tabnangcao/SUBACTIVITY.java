package com.example.danghuuquanghuy_bai12_tabnangcao;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class SUBACTIVITY extends AppCompatActivity {
    TextView txtmaso, txtbaihat, txtloibaihat, txttacgia;
    ImageButton btnthich,btnPlay,btnStop;
    MediaPlayer mymusic;
    int media[]={R.raw.vo,R.raw.noinaycoanh,R.raw.dotoc,R.raw.makingmyway,R.raw.ngumotminh,R.raw.anhlacuaem,
    R.raw.taivisao,R.raw.chimsau,R.raw.vetinh,R.raw.haytraochoanh,R.raw.anhnangcuaanh,
    R.raw.tan,R.raw.khongphaigu,R.raw.cua,R.raw.lamgimaphaihot,R.raw.mottrieulike,
    R.raw.duanhauditron,R.raw.emkhongsaichungtasai,R.raw.causeilopyou,R.raw.anhnhaodauthe};

    int trangthai = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subactivity);
        ImageView imageView = findViewById(R.id.img_play);
        btnPlay=findViewById(R.id.btn_play);
        btnStop=findViewById(R.id.btn_pause);
        btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        btnStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });



        // Đặt ảnh từ tệp drawable (đây là một ví dụ, bạn cần thay đổi đường dẫn ảnh của mình)
        imageView.setImageResource(R.drawable.yeah);

        txtmaso = findViewById(R.id.txtMaBaiHat);
        txtbaihat = findViewById(R.id.txtTenBaiHat);
        txtloibaihat = findViewById(R.id.txtLoibaihat);
        txttacgia = findViewById(R.id.txtTacGia);
        btnthich = findViewById(R.id.btn_yeuthich);

        // Nhận Intent từ myarrayAdapter, lấy dữ liệu khỏi Bundle
        Intent callerIntent1 = getIntent();
        Bundle backagecaller1 = callerIntent1.getBundleExtra("package");
        String maso = backagecaller1.getString("maso");

        // Truy vấn dữ liệu từ maso nhận được; Hiển thị dữ liệu Mã bài hát, Tên bài hát, Lời bài
        // hát, Tác giả, Trạng thái Thích lên SubActivity
        Cursor c = MainActivity.database.rawQuery("SELECT * FROM ArirangSongList\n" +
                "WHERE MABH LIKE'" + maso + "'", null);

        if (c.moveToFirst()) {
            txtmaso.setText(maso);
            txtbaihat.setText(c.getString(1));
            txtloibaihat.setText(c.getString(3)); // Adjust index based on your database schema
            txttacgia.setText(c.getString(2));  // Adjust index based on your database schema
            trangthai = c.getInt(4);

            if (trangthai == 0) {
                btnthich.setImageResource(R.drawable.timtrang);
            } else {
                btnthich.setImageResource(R.drawable.heart);
            }
        }

        c.close();

        // Xử lý sự kiện khi click vào Button btnthich
        // Cập nhật dữ liệu vào CSDL, thay đổi trạng thái hiển thị cho Button
        btnthich.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                ContentValues values = new ContentValues();
                if (trangthai == 0) {
                    trangthai = 1;
                    btnthich.setImageResource(R.drawable.heart);
                } else {
                    trangthai = 0;
                    btnthich.setImageResource(R.drawable.timtrang);
                }
                values.put("YEUTHICH", trangthai);
                MainActivity.database.update("ArirangSongList", values,
                        "MABH=?", new String[]{txtmaso.getText().toString()});
            }
        });
    }
}
