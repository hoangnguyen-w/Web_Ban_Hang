package com.example.web_ban_hang.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.web_ban_hang.Interface.IImageClickListenner;
import com.example.web_ban_hang.R;
import com.example.web_ban_hang.model.EventBus.TinhTongEvent;
import com.example.web_ban_hang.model.GioHang;
import com.example.web_ban_hang.utils.Utils;

import org.greenrobot.eventbus.EventBus;

import java.text.DecimalFormat;
import java.util.List;

public class GioHangAdapter extends RecyclerView.Adapter<GioHangAdapter.MyViewHolder> {
    Context context;
    List<GioHang> gioHangList;

    public GioHangAdapter(Context context, List<GioHang> gioHangList) {
        this.context = context;
        this.gioHangList = gioHangList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_giohang, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        GioHang giohang = gioHangList.get(position);
        holder.item_giohang_tensp.setText(giohang.getTensp());
        holder.item_giohang_soluong.setText(giohang.getSoluong() + "");
        Glide.with(context).load(giohang.getHinhsp()).into(holder.item_giohang_image);
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        holder.item_giohang_gia.setText("Giá: " + decimalFormat.format(giohang.getGiasp()));
        long gia = giohang.getSoluong() * giohang.getGiasp();
        holder.item_giohang_giasp2.setText(decimalFormat.format(gia));
        holder.setListenner(new IImageClickListenner() {
            @Override
            public void onImageClick(View view, int adapterPosition, int i) {
                Log.d("TAG", "onImageClick" + adapterPosition + "..." + i);
                if(i == 1){
                    if(gioHangList.get(adapterPosition).getSoluong() > 1){
                        int soluongmoi = gioHangList.get(adapterPosition).getSoluong() - 1;
                        gioHangList.get(adapterPosition).setSoluong(soluongmoi);

                        holder.item_giohang_soluong.setText(gioHangList.get(adapterPosition).getSoluong()+ "");
                        long gia = gioHangList.get(adapterPosition).getSoluong() * gioHangList.get(adapterPosition).getGiasp();
                        holder.item_giohang_giasp2.setText(decimalFormat.format(gia));
                        EventBus.getDefault().postSticky(new TinhTongEvent());
                    }else if(gioHangList.get(adapterPosition).getSoluong() == 1){
                        AlertDialog.Builder builder = new AlertDialog.Builder(view.getRootView().getContext());
                        builder.setTitle("Thông báo");
                        builder.setMessage("Bạn cớ muốn xóa sản phẩm khỏi giỏ hàng");
                        builder.setPositiveButton("Đồng ý", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Utils.manggiohang.remove(adapterPosition);
                                notifyDataSetChanged();
                                EventBus.getDefault().postSticky(new TinhTongEvent());
                            }
                        });
                        builder.setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        });
                        builder.show();
                    }
                }else if(i == 2){
                    if(gioHangList.get(adapterPosition).getSoluong() < 11){
                        int soluongmoi = gioHangList.get(adapterPosition).getSoluong() + 1;
                        gioHangList.get(adapterPosition).setSoluong(soluongmoi);
                    }
                }
                holder.item_giohang_soluong.setText(gioHangList.get(adapterPosition).getSoluong()+ " ");
                long gia = gioHangList.get(adapterPosition).getSoluong() * gioHangList.get(adapterPosition).getGiasp();
                holder.item_giohang_giasp2.setText(decimalFormat.format(gia));
                EventBus.getDefault().postSticky(new TinhTongEvent());
            }
        });
    }

    @Override
    public int getItemCount() {
        return gioHangList.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView item_giohang_image, imgtru, imgcong;
        TextView item_giohang_tensp, item_giohang_gia, item_giohang_soluong, item_giohang_giasp2;
        IImageClickListenner listenner;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            item_giohang_image = (ImageView) itemView.findViewById(R.id.item_giohang_img);
            item_giohang_tensp = (TextView) itemView.findViewById(R.id.item_giohang_tensp);
            item_giohang_gia = (TextView) itemView.findViewById(R.id.item_giohang_price);
            item_giohang_soluong = (TextView) itemView.findViewById(R.id.item_giohang_quatity);
            item_giohang_giasp2 = (TextView) itemView.findViewById(R.id.item_giohang_price2);
            imgcong = (ImageView) itemView.findViewById(R.id.item_giohang_cong);
            imgtru = (ImageView) itemView.findViewById(R.id.item_giohang_tru);

            //Even Click
            imgcong.setOnClickListener(this);
            imgtru.setOnClickListener(this);
        }

        public void setListenner(IImageClickListenner listenner) {
            this.listenner = listenner;
        }

        @Override
        public void onClick(View view) {
            if(view == imgtru){
                listenner.onImageClick(view, getAdapterPosition(), 1);
                //1 tru
            }else if(view == imgcong){
                listenner.onImageClick(view, getAdapterPosition(), 2);
                //2 cong
            }
        }
    }
}
