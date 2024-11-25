package group1.pro1122.duan1.adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import group1.pro1122.duan1.R;
import group1.pro1122.duan1.daos.PhongDAO;
import group1.pro1122.duan1.daos.ThuChiDAO;
import group1.pro1122.duan1.fragments.QLThuchiFragment;
import group1.pro1122.duan1.models.ChiPhi;
import group1.pro1122.duan1.models.Phong;

public class ThuChiAdapter extends RecyclerView.Adapter<ThuChiAdapter.ViewHolder> {

    private final Context context;
    private ArrayList<ChiPhi> chiPhiList;
    PhongDAO phongDAO;

    QLThuchiFragment fragment;

    public ThuChiAdapter(Context context, ArrayList<ChiPhi> chiPhiList, QLThuchiFragment fragment) {
        this.context = context;
        this.chiPhiList = chiPhiList;
        this.phongDAO = new PhongDAO(context);
        this.fragment = fragment;
    }

    @NonNull
    @Override
    public ThuChiAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_chiphi, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ThuChiAdapter.ViewHolder holder, int position) {
        ChiPhi chiPhi = chiPhiList.get(position);
        holder.tvTenChiPhi.setText("Tên chi phí: "+chiPhi.getTenChiPhi());
        holder.tvSoTienChi.setText("Số tiền chi: "+chiPhi.getSoTienChi());
        holder.tvNgayPhatSinh.setText("Ngày phát sinh: "+formatDate(chiPhi.getNgayPhatSinh()));

        Phong phong = phongDAO.getPhongByPhongID(chiPhi.getPhongId());
        holder.tvPhongLienQuan.setText("Phòng liên quan: "+phong.getSoPhong() + " - ID: "+phong.getPhongId());

        holder.btnEdit.setOnClickListener(v -> {
            showEditChiPhiDialog(chiPhi, position);
        });

        holder.btnDelete.setOnClickListener(v -> {
            showDeleteConfirmationDialog(chiPhi, position);
        });
    }

    @Override
    public int getItemCount() {
        return chiPhiList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvTenChiPhi, tvSoTienChi, tvNgayPhatSinh, tvPhongLienQuan;
        ImageView btnEdit, btnDelete;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTenChiPhi = itemView.findViewById(R.id.tvTenChiPhi);
            tvSoTienChi = itemView.findViewById(R.id.tvSoTienChi);
            tvNgayPhatSinh = itemView.findViewById(R.id.tvNgayPhatSinh);
            tvPhongLienQuan = itemView.findViewById(R.id.tvPhongLienQuan);
            btnEdit = itemView.findViewById(R.id.btnEdit);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }

    private void showDeleteConfirmationDialog(ChiPhi chiPhi, int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Xác nhận xóa")
                .setMessage("Bạn có chắc muốn xóa chi phí này?")
                .setPositiveButton("Xóa", (dialog, which) -> {
                    // Xóa chi phí khỏi cơ sở dữ liệu
                    ThuChiDAO thuChiDAO = new ThuChiDAO(context);
                    if (thuChiDAO.deleteChiPhi(chiPhi.getChiPhiId())) {
                        chiPhiList.remove(position);
                        notifyItemRemoved(position);
                        fragment.updateThuChi();
                    }
                })
                .setNegativeButton("Hủy", (dialog, which) -> dialog.dismiss())
                .create()
                .show();
    }

    private void showEditChiPhiDialog(ChiPhi chiPhi, int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater inflater = LayoutInflater.from(context);
        View dialogView = inflater.inflate(R.layout.dialog_edit_chiphi, null);
        builder.setView(dialogView);

        // Ánh xạ các thành phần trong dialog
        TextView tvTitle = dialogView.findViewById(R.id.tvTitle);
        tvTitle.setText("Chỉnh sửa Chi Phí");
        Spinner spinnerPhong = dialogView.findViewById(R.id.spinnerPhong);
        TextView edtTenChiPhi = dialogView.findViewById(R.id.edtTenChiPhi);
        TextView edtSoTienChi = dialogView.findViewById(R.id.edtSoTienChi);
        Button btnLuu = dialogView.findViewById(R.id.btnLuu);

        // Hiển thị thông tin chi phí hiện tại
        edtTenChiPhi.setText(chiPhi.getTenChiPhi());
        edtSoTienChi.setText(String.valueOf(chiPhi.getSoTienChi()));

        // Lấy danh sách phòng
        SharedPreferences pref = context.getSharedPreferences("user", Context.MODE_PRIVATE);
        int chuSoHuu = pref.getInt("userID", -1);
        ArrayList<Phong> phongList = phongDAO.getPhongByChuSoHuu(chuSoHuu);
        ArrayAdapter<String> phongAdapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item);
        phongAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        phongList.forEach(phong -> phongAdapter.add(phong.getSoPhong() + " - ID: " + phong.getPhongId()));
        spinnerPhong.setAdapter(phongAdapter);

        // Đặt phòng hiện tại
        for (int i = 0; i < phongAdapter.getCount(); i++) {
            if (phongAdapter.getItem(i).contains("ID: " + chiPhi.getPhongId())) {
                spinnerPhong.setSelection(i);
                break;
            }
        }

        AlertDialog alertDialog = builder.create();

        btnLuu.setOnClickListener(v -> {
            String tenChiPhi = edtTenChiPhi.getText().toString().trim();
            String soTienChiStr = edtSoTienChi.getText().toString().trim();
            String selectedPhong = (String) spinnerPhong.getSelectedItem();

            if (tenChiPhi.isEmpty() || soTienChiStr.isEmpty() || selectedPhong == null) {
                edtTenChiPhi.setError("Vui lòng nhập tên chi phí!");
                edtSoTienChi.setError("Vui lòng nhập số tiền chi!");
                return;
            }

            int soTienChi = Integer.parseInt(soTienChiStr);
            int phongId = Integer.parseInt(selectedPhong.split(" - ID: ")[1]);

            // Cập nhật thông tin chi phí
            chiPhi.setTenChiPhi(tenChiPhi);
            chiPhi.setSoTienChi(soTienChi);
            chiPhi.setPhongId(phongId);

            // Cập nhật vào cơ sở dữ liệu
            ThuChiDAO thuChiDAO = new ThuChiDAO(context);
            if (thuChiDAO.updateChiPhi(chiPhi)) {
                chiPhiList.set(position, chiPhi);
                notifyItemChanged(position);
                fragment.updateThuChi();
                alertDialog.dismiss();
            } else {
                edtTenChiPhi.setError("Cập nhật thất bại!");
            }
        });

        alertDialog.show();
    }


    private String formatDate(String timestampString) {
        try {
            long timestamp = Long.parseLong(timestampString);
            Date date = new Date(timestamp);
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", java.util.Locale.getDefault());
            return dateFormat.format(date);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return "Ngày không hợp lệ";
        }
    }


}
