package group1.pro1122.duan1.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import group1.pro1122.duan1.R;
import group1.pro1122.duan1.daos.DiaDiemDAO;
import group1.pro1122.duan1.models.DiaDiem;
import group1.pro1122.duan1.models.Phong;

public class BaiDangAdapter extends RecyclerView.Adapter<BaiDangAdapter.BaiDangViewHolder> {

    private final Context context;
    private final ArrayList<Phong> listPhong;
    private OnApproveClickListener onApproveClickListener;
    private OnDeleteClickListener onDeleteClickListener;

    private DiaDiemDAO diaDiemDAO;
    private ArrayList<DiaDiem> listDiaDiem;

    public BaiDangAdapter(Context context, ArrayList<Phong> listPhong) {
        this.context = context;
        this.listPhong = listPhong;
    }

    // Interface cho sự kiện duyệt
    public interface OnApproveClickListener {
        void onApproveClick(Phong phong);
    }

    // Interface cho sự kiện xóa
    public interface OnDeleteClickListener {
        void onDeleteClick(Phong phong);
    }

    // Hàm thiết lập listener cho sự kiện duyệt
    public void setOnApproveClickListener(OnApproveClickListener listener) {
        this.onApproveClickListener = listener;
    }

    // Hàm thiết lập listener cho sự kiện xóa
    public void setOnDeleteClickListener(OnDeleteClickListener listener) {
        this.onDeleteClickListener = listener;
    }

    @NonNull
    @Override
    public BaiDangViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_ql_baidang, parent, false);
        return new BaiDangViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BaiDangViewHolder holder, int position) {
        Phong phong = listPhong.get(position);

        // Đặt dữ liệu cho các thành phần của item
        holder.tvSoPhong.setText("Phòng: " + phong.getSoPhong());
        holder.tvDienTich.setText("Diện tích: " + phong.getDienTich() + " m²");
        holder.tvTrangThaiThue.setText("Trạng thái thuê: " + (phong.getTrangThai() == 1 ? "Đã thuê" : "Trống"));
        holder.tvGiaThue.setText("Giá thuê: " + phong.getGiaThue() + " VND");
        diaDiemDAO = new DiaDiemDAO(context);
        listDiaDiem = diaDiemDAO.read();
        for (DiaDiem diaDiem : listDiaDiem) {
            if (phong.getDiaDiemID() == diaDiem.getDiaDiemId()) {
                holder.tvDiaDiem.setText("Địa điểm: " +diaDiem.getThanhPho());
                break;
            }
        }
        holder.tvNgayTao.setText("Ngày tạo: " + convertTimestampToDate(phong.getNgayTao()));
        holder.tvTrangThaiPheDuyet.setText((phong.getTrangThaiPheduyet() == 1 ? "Đã duyệt" : "Chưa duyệt"));
        holder.tvTrangThaiPheDuyet.setTextColor(context.getResources().getColor(
                phong.getTrangThaiPheduyet() == 1 ? android.R.color.holo_green_dark : android.R.color.holo_red_dark
        ));
        holder.tvMoTa.setText("Mô tả: " + phong.getMoTa());

        // Hiển thị ảnh từ byte[]
        Bitmap bitmap = BitmapFactory.decodeByteArray(phong.getAnhPhong(), 0, phong.getAnhPhong().length);
        holder.imgPhong.setImageBitmap(bitmap);

        // Thiết lập sự kiện cho nút "Duyệt"
        holder.btnApprove.setOnClickListener(v -> {
            if (onApproveClickListener != null) {
                onApproveClickListener.onApproveClick(phong);
            }
        });

        // Thiết lập sự kiện cho nút "Hủy"
        holder.btnDelete.setOnClickListener(v -> {
            // Kiểm tra nếu phòng đang thuê (trangThai == 1)
            if (phong.getTrangThai() == 1) {
                // Hiển thị thông báo không cho phép xóa
                Toast.makeText(context, "Không thể hủy bài đăng vì phòng đang được thuê!", Toast.LENGTH_SHORT).show();
            } else {
                // Hiển thị dialog xác nhận nếu phòng không đang thuê
                new AlertDialog.Builder(context)
                        .setTitle("Xác nhận")
                        .setMessage("Bạn có chắc chắn muốn hủy bài đăng này không?")
                        .setPositiveButton("Đồng ý", (dialog, which) -> {
                            if (onDeleteClickListener != null) {
                                onDeleteClickListener.onDeleteClick(phong);
                            }
                        })
                        .setNegativeButton("Hủy", (dialog, which) -> dialog.dismiss())
                        .show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return listPhong.size();
    }

    // ViewHolder cho BaiDangAdapter
    public static class BaiDangViewHolder extends RecyclerView.ViewHolder {

        ImageView imgPhong;
        TextView tvSoPhong, tvDienTich, tvTrangThaiThue, tvGiaThue, tvDiaDiem, tvNgayTao, tvTrangThaiPheDuyet, tvMoTa;
        MaterialButton btnApprove, btnDelete;

        public BaiDangViewHolder(@NonNull View itemView) {
            super(itemView);

            imgPhong = itemView.findViewById(R.id.imgPhong);
            tvSoPhong = itemView.findViewById(R.id.tvSoPhong);
            tvDienTich = itemView.findViewById(R.id.tvDienTich);
            tvTrangThaiThue = itemView.findViewById(R.id.tvTrangThaiThueBaiDang);
            tvGiaThue = itemView.findViewById(R.id.tvGiaThue);
            tvDiaDiem = itemView.findViewById(R.id.tvDiaDiem);
            tvNgayTao = itemView.findViewById(R.id.tvNgayTao);
            tvTrangThaiPheDuyet = itemView.findViewById(R.id.tvTrangThaiPheDuyetBD);
            tvMoTa = itemView.findViewById(R.id.tvMoTa);
            btnApprove = itemView.findViewById(R.id.btnDuyet);
            btnDelete = itemView.findViewById(R.id.btnHuy);
        }
    }

    private String convertTimestampToDate(String timestamp) {
        try {
            long timeMillis = Long.parseLong(timestamp);
            Date date = new Date(timeMillis);
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.getDefault());
            return sdf.format(date);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return "Invalid date";
        }
    }
}
