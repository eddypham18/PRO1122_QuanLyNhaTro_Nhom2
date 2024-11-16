package group1.pro1122.duan1.adapters;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import group1.pro1122.duan1.R;
import group1.pro1122.duan1.models.HopDong;

public class HopDongAdapter extends RecyclerView.Adapter<HopDongAdapter.ViewHolder>{
    private Context context;
    private ArrayList<HopDong> listHopDong;

    // Constructor
    public HopDongAdapter(Context context, ArrayList<HopDong> listHopDong) {
        this.context = context;
        this.listHopDong = listHopDong;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_hopdong, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        HopDong hopDong = listHopDong.get(position);

        // Gán dữ liệu từ model HopDong vào các view trong ViewHolder
        holder.tvHopDongId.setText("Hợp đồng ID: " + hopDong.getHopDongId());
        holder.tvPhongId.setText("Phòng ID: " + hopDong.getPhongId());
        holder.tvToaNha.setText("Tòa nhà: " );
        holder.tvNguoiThue.setText("Người thuê: " + hopDong.getUserId());
        holder.tvTrangThai.setText(hopDong.getTrangThaiHopDong() == 1 ? "Đang hiệu lực" : "Đã kết thúc");
    }

    @Override
    public int getItemCount() {
        return listHopDong.size();
    }

    // Cập nhật danh sách và làm mới RecyclerView
    public void refreshList(ArrayList<HopDong> newList) {
        this.listHopDong = newList;
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvHopDongId, tvPhongId,tvNguoiThue, tvNgayTao, tvTrangThai, tvToaNha;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            // Ánh xạ các view từ layout item_hop_dong.xml
            tvHopDongId = itemView.findViewById(R.id.tvID);
            tvPhongId = itemView.findViewById(R.id.tvPhong);
            tvToaNha = itemView.findViewById(R.id.tvToaNha);
            tvNguoiThue = itemView.findViewById(R.id.tvNguoiThue);
            tvTrangThai = itemView.findViewById(R.id.tvTrangThaiHopDong);
        }
    }
}
