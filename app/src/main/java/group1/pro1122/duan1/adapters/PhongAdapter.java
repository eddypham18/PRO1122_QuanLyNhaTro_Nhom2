package group1.pro1122.duan1.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import group1.pro1122.duan1.R;
import group1.pro1122.duan1.daos.DiaDiemDAO;
import group1.pro1122.duan1.daos.PhongDAO;
import group1.pro1122.duan1.models.DiaDiem;
import group1.pro1122.duan1.models.Phong;
public class PhongAdapter extends RecyclerView.Adapter<PhongAdapter.PhongViewHolder>{
    private Context context;
    private ArrayList<Phong> listPhong;

    private OnItemClickListener onItemClickListener;
    ArrayList<DiaDiem> listDiaDiem;
    DiaDiemDAO diaDiemDAO;
    PhongDAO phongDAO;
    public PhongAdapter(Context context, ArrayList<Phong> listPhong) {
        this.context = context;
        this.listPhong = listPhong;
    }

    // Interface để xử lý sự kiện click
    public interface OnItemClickListener {
        void onItemClick(Phong phong);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    //Interface long click
    public interface OnItemLongClickListener {
        void onItemLongClick(Phong phong);
    }

    private OnItemLongClickListener onItemLongClickListener;

    public void setOnItemLongClickListener(OnItemLongClickListener listener) {
        this.onItemLongClickListener = listener;
    }

    @NonNull
    @Override
    public PhongViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_phong, parent, false);
        return new PhongViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PhongViewHolder holder, int position) {
        Phong phong = listPhong.get(position);

        holder.tvSoPhong.setText("Phòng: " + phong.getSoPhong());
        holder.tvDienTich.setText("Diện tích: " + phong.getDienTich() + " m²");
        holder.tvTrangThai.setText("Trạng thái thuê: " + (phong.getTrangThai() == 1 ? "Đã thuê" : "Trống"));
        holder.tvGiaThue.setText("Giá thuê: " + phong.getGiaThue() + " VND");
        diaDiemDAO = new DiaDiemDAO(context);
        listDiaDiem = diaDiemDAO.read();
        for (DiaDiem diaDiem : listDiaDiem) {
            if (phong.getDiaDiemID() == diaDiem.getDiaDiemId()) {
                holder.tvDiaDiem.setText("Địa điểm: " +diaDiem.getThanhPho());
                break;
            }
        }
        holder.tvTrangThaiHopDong.setText(phong.getTrangThaiPheduyet() == 1 ? "Đã duyệt" : "Chưa duyệt");
        holder.tvTrangThaiHopDong.setTextColor(context.getResources().getColor(
                phong.getTrangThaiPheduyet() == 1 ? android.R.color.holo_green_dark : android.R.color.holo_red_dark
        ));


        //Xự kiện click
        holder.itemView.setOnClickListener(v -> {
            if (onItemClickListener != null) {
                onItemClickListener.onItemClick(phong);
            }
        });

        // Sự kiện long-click
        holder.itemView.setOnLongClickListener(v -> {
            if (onItemLongClickListener != null) {
                onItemLongClickListener.onItemLongClick(phong);
            }
            return true;
        });

        //Nút xóa
        holder.btnDelete.setOnClickListener(v -> {
            if (phong.getTrangThaiPheduyet() == 1) {
                Toast.makeText(context, "Không thể xóa phòng đã được phê duyệt!", Toast.LENGTH_SHORT).show();
            } else if (phong.getTrangThai() == 1) {
                Toast.makeText(context, "Không thể xóa phòng đang được thuê!", Toast.LENGTH_SHORT).show();
            } else {
                // Hiển thị hộp thoại xác nhận xóa
                new AlertDialog.Builder(context)
                        .setTitle("Xác nhận xóa")
                        .setMessage("Bạn có chắc chắn muốn xóa phòng này không?")
                        .setPositiveButton("Có", (dialog, which) -> {
                            // Xác nhận xóa phòng
                            phongDAO = new PhongDAO(context);
                            boolean isDeleted = phongDAO.delete(phong.getPhongId());
                            if (isDeleted) {
                                listPhong.remove(position); // Xóa khỏi danh sách hiện tại
                                notifyItemRemoved(position); // Cập nhật RecyclerView
                                Toast.makeText(context, "Xóa phòng thành công!", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(context, "Xóa phòng thất bại!", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .setNegativeButton("Không", (dialog, which) -> dialog.dismiss()) // Đóng hộp thoại nếu chọn Không
                        .show();
            }
        });

    }

    @Override
    public int getItemCount() {
        return listPhong.size();
    }

    public static class PhongViewHolder extends RecyclerView.ViewHolder {
        TextView tvSoPhong, tvDienTich, tvTrangThai, tvGiaThue, tvTrangThaiHopDong, tvDiaDiem;
        ImageView imgPhong, btnDelete;
        public PhongViewHolder(@NonNull View itemView) {
            super(itemView);

            tvSoPhong = itemView.findViewById(R.id.tvSoPhong);
            tvDienTich = itemView.findViewById(R.id.tvDienTich);
            tvTrangThai = itemView.findViewById(R.id.tvTrangThai);
            tvGiaThue = itemView.findViewById(R.id.tvGiaThue);
            tvDiaDiem = itemView.findViewById(R.id.tvDiaDiem);
            tvTrangThaiHopDong = itemView.findViewById(R.id.tvTrangThaiHopDong);
            btnDelete = itemView.findViewById(R.id.btnDelete);

        }
    }


}
