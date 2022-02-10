package com.example.sqliteexample;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Schedules extends Activity {
    private RecyclerView mRv_list;
    private FloatingActionButton mBtn_write;
    private ArrayList<DiarItem> mDiarItems;
    private DBHelper mDBHelper;
    private CustomAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedules);

        setInit();
    }

    private void setInit()
    {
        mDBHelper = new DBHelper(this);
        mRv_list = findViewById(R.id.rv_list);
        mBtn_write = findViewById(R.id.btn_write);
        mDiarItems = new ArrayList<>();

        // load recent DB
        loadRecentDB();

        mBtn_write.setOnClickListener(new View.OnClickListener() {
            @Override

            // 팝업 창 띄우기
            public void onClick(View v) {
                Dialog dialog = new Dialog(Schedules.this, android.R.style.Theme_Material_Light_Dialog);
                dialog.setContentView(R.layout.dialog_edit);

                EditText et_title = dialog.findViewById(R.id.et_title);
                EditText et_content = dialog.findViewById(R.id.et_content);
                Button btn_ok = dialog.findViewById(R.id.btn_ok);

                btn_ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        // Insert Database
                        String currentTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()); // 현재 시간 연월일시분초 받아오기
                        mDBHelper.InsertList(et_title.getText().toString(), et_content.getText().toString(), currentTime);

                        // Insert UI
                        DiarItem item = new DiarItem();

                        item.setTitle(et_title.getText().toString());
                        item.setContent(et_content.getText().toString());
                        item.setWriteDate(currentTime);

                        mAdapter.addItem(item);
                        mRv_list.smoothScrollToPosition(0);
                        dialog.dismiss();
                        Toast.makeText(Schedules.this, "목록 추가 완료", Toast.LENGTH_SHORT).show();
                    }
                });

                dialog.show(); // 다이어로그 띄움
            }
        });
    }

    private void loadRecentDB() {
        // 저장되어있던 DB 중 가져온다
        mDiarItems = mDBHelper.getDiarItem();
        if(mAdapter == null) {
            mAdapter = new CustomAdapter(mDiarItems, this);
            mRv_list.setHasFixedSize(true);
            mRv_list.setAdapter(mAdapter);
        }
    }
}