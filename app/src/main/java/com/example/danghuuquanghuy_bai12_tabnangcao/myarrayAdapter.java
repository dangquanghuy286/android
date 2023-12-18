package com.example.danghuuquanghuy_bai12_tabnangcao;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class myarrayAdapter extends ArrayAdapter<Item> {
    Activity context = null;
    ArrayList<Item> myArray = null;
    int LayoutId;
    public myarrayAdapter(Activity context, int LayoutId,ArrayList<Item>arr) {
        super(context, LayoutId,arr);
        // TODO Auto-generated constructor stub
        this.context = context;
        this.LayoutId = LayoutId;
        this.myArray = arr;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        LayoutInflater inflater = context.getLayoutInflater();
        convertView = inflater.inflate(LayoutId, null);
        Item myItem = myArray.get(position);
        TextView tieude = convertView.findViewById(R.id.txttieude);
        tieude.setText(myItem.getTieude());
        TextView maso = convertView.findViewById(R.id.txtmaso);
        maso.setText(myItem.getMaso());
        ImageButton btnlike = convertView.findViewById(R.id.btn_Like);
        //Xử lý hiển thị cho ImageButton btnlike
        if (myItem.getThich()==1) {
            btnlike.setImageResource(R.drawable.heart);
        } else {
            btnlike.setImageResource(R.drawable.timtrang);
        }
// Xử lý sự kiện khi click vào ImageButton btnlike
// Cập nhật trạng thái thích vào CSDL; Thiết lập hình ảnh ImageButton cho phù hợp
        btnlike.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                int thich = myItem.getThich();
                ContentValues values = new ContentValues();
                if (thich == 0) {
                    btnlike.setImageResource(R.drawable.heart);
                    thich = 1;
                } else {
                    btnlike.setImageResource(R.drawable.timtrang);
                    thich = 0;
                }
                //Cập nhật lại trạng thái thích cho mảng
                myItem.setThich(thich);
                values.put("YEUTHICH", thich);
                //Cập nhật vào cơ sở dữ liệu
                MainActivity.database.update("ArirangSongList", values,
                        "MABH=?", new String[]{myItem.getMaso()});
            }
        });
//Xử lý sự kiện khi Click vào mỗi dòng tiều đề bài hát trên Listview
//Chuyển Textview tieude và maso sang màu đỏ
//Khai báo Intent, Bundle,lấy maso truyền qua SubActivity
        tieude.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                tieude.setTextColor(Color.RED);
                maso.setTextColor(Color.RED);
                Intent intent1 = new Intent(context, SUBACTIVITY.class);
                Bundle bundle1 = new Bundle();
                bundle1.putString("maso", myItem.getMaso());
                intent1.putExtra("package", bundle1);
                context.startActivity(intent1);
            }
        });
        return convertView;
    }
}
