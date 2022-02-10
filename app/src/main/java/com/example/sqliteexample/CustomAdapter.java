package com.example.sqliteexample;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.ViewHelper>
{
    private ArrayList<DiarItem> mDiarItems;
    private Context mContext;
    private DBHelper mDBHelper;

    public CustomAdapter(ArrayList<DiarItem> mDiarItems, Context mContext) {
        this.mDiarItems = mDiarItems;
        this.mContext = mContext;
        mDBHelper = new DBHelper(mContext);
    }

    @NonNull
    @Override
    public ViewHelper onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        // item_list 뷰의 연결
        View holder = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list, parent, false);
        return new ViewHelper(holder);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHelper holder, int position)
    {
        holder.tv_title.setText(mDiarItems.get(position).getTitle());
        holder.tv_content.setText(mDiarItems.get(position).getContent());
        holder.tv_writeDate.setText(mDiarItems.get(position).getWriteDate());
    }

    @Override
    public int getItemCount()
    {
        return mDiarItems.size();
    }

    public class ViewHelper extends RecyclerView.ViewHolder { // item_list

        private TextView tv_title;
        private TextView tv_content;
        private TextView tv_writeDate;

        public ViewHelper(@NonNull View itemView) {
            super(itemView);

            tv_title = itemView.findViewById(R.id.tv_title);
            tv_content = itemView.findViewById(R.id.tv_content);
            tv_writeDate = itemView.findViewById(R.id.tv_date);

            // 하나의 뷰에 대한 아이템 뷰
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int curPos = getAdapterPosition(); // 현재 리스트 클릭한 아이템 위치
                    DiarItem diarItem = mDiarItems.get(curPos);

                    String[] strChoiceItems = {"수정", "삭제"};
                    AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                    builder.setTitle("원하는 작업을 선택해 주세요.");
                    builder.setItems(strChoiceItems, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int position) {
                            if (position == 0) {
                                // 수정
                                // 팝업 창 띄우기
                                Dialog dialog = new Dialog(mContext, android.R.style.Theme_Material_Light_Dialog);
                                dialog.setContentView(R.layout.dialog_edit);

                                EditText et_title = dialog.findViewById(R.id.et_title);
                                EditText et_content = dialog.findViewById(R.id.et_content);
                                Button btn_ok = dialog.findViewById(R.id.btn_ok);

                                et_title.setText(diarItem.getTitle());
                                et_content.setText(diarItem.getContent());

                                et_title.setSelection(et_title.getText().length()); // 커서를 글자의 마지막으로 이동

                                btn_ok.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {

                                        // Update Database
                                        String title = et_title.getText().toString();
                                        String content = et_content.getText().toString();
                                        String currentTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()); // 현재 시간 연월일시분초 받아오기
                                        String beforeTime = diarItem.getWriteDate();

                                        mDBHelper.UpdateList(title, content, currentTime, beforeTime);

                                        // Update UI
                                        diarItem.setTitle(title);
                                        diarItem.setContent(content);
                                        diarItem.setWriteDate(currentTime);
                                        notifyItemChanged(curPos, diarItem);
                                        dialog.dismiss();
                                        Toast.makeText(mContext, "목록 수정 완료", Toast.LENGTH_SHORT).show();
                                    }
                                });

                                dialog.show(); // 다이어로그 띄움
                            } else if (position == 1) {
                                // 삭제
                                // delete table
                                String beforeTime = diarItem.getWriteDate();
                                mDBHelper.DeleteList(beforeTime);

                                // delete UI
                                mDiarItems.remove(curPos);
                                notifyItemRemoved(curPos);
                                Toast.makeText(mContext, "목록 제거 완료", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                    builder.show();
                }
            });
        }
    }
    public void addItem(DiarItem _item) {
        mDiarItems.add(0, _item);
        notifyItemInserted(0);
    }
}
