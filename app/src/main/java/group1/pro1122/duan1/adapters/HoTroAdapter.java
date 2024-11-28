package group1.pro1122.duan1.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
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
import group1.pro1122.duan1.daos.HoTroDAO;
import group1.pro1122.duan1.daos.ThongBaoDAO;
import group1.pro1122.duan1.models.HoTro;

public class HoTroAdapter extends RecyclerView.Adapter<HoTroAdapter.HoTroViewHolder> {

    private Context context;
    private ArrayList<HoTro> hoTroList;

    private HoTroDAO hoTroDAO;


    public HoTroAdapter(Context context, ArrayList<HoTro> hoTroList) {
        this.context = context;
        this.hoTroList = hoTroList;
        this.hoTroDAO = new HoTroDAO(context);
    }

    @NonNull
    @Override
    public HoTroViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_hotro, parent, false);
        return new HoTroViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HoTroViewHolder holder, int position) {
        HoTro hoTro = hoTroList.get(position);

        holder.tvTieuDe.setText("Tiêu đề: " + hoTro.getTieuDe());
        holder.tvNoiDung.setText("Nội dung: " + hoTro.getNoiDung());
        holder.tvTrangThaiXuLy.setText(hoTro.getTrangThai() == 1 ? "Đã xử lý" : "Chưa xử lý");
        holder.tvNgayTaoDon.setText("Ngày tạo: " + formatDate(hoTro.getNgayTao()));
        holder.tvNgayXuLy.setText("Ngày xử lý: " + (hoTro.getTrangThai() == 1 ? formatDate(hoTro.getNgayXuLy()) : "Chưa xử lý"));

        // Xử lý sự kiện Trả lời
        holder.btnTraLoi.setOnClickListener(v -> {
            showTraLoiDialog(position, hoTro);
        });

        // Xử lý sự kiện Xóa
        holder.btnXoa.setOnClickListener(v -> {
            deleteHoTro(position, hoTro);
        });
    }

    @Override
    public int getItemCount() {
        return hoTroList.size();
    }

    public static class HoTroViewHolder extends RecyclerView.ViewHolder {
        TextView tvTieuDe, tvNoiDung, tvTrangThaiXuLy, tvNgayTaoDon, tvNgayXuLy;
        MaterialButton btnTraLoi, btnXoa;

        public HoTroViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTieuDe = itemView.findViewById(R.id.tvTieuDe);
            tvNoiDung = itemView.findViewById(R.id.tvNoiDung);
            tvTrangThaiXuLy = itemView.findViewById(R.id.tvTrangThaiXuLy);
            tvNgayTaoDon = itemView.findViewById(R.id.tvNgayTaoDon);
            tvNgayXuLy = itemView.findViewById(R.id.tvNgayXuLy);
            btnTraLoi = itemView.findViewById(R.id.btnTraLoi);
            btnXoa = itemView.findViewById(R.id.btnXoa);
        }
    }

    public void deleteHoTro(int position, HoTro hoTro){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Xóa yêu cầu")
                .setMessage("Bạn có chắc chắn muốn xóa yêu cầu này?")
                .setPositiveButton("Xóa", (dialog, which) -> {
                    if (hoTroDAO.deleteHoTroRequest(hoTro.getHoTroId())) {
                        hoTroList.remove(position);
                        notifyItemRemoved(position);
                        Toast.makeText(context, "Đã xóa yêu cầu!", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(context, "Lỗi khi xóa yêu cầu!", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Hủy", (dialog, which) -> dialog.dismiss())
                .create()
                .show();
    }

    private void showTraLoiDialog(int position, HoTro hoTro) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater inflater = LayoutInflater.from(context);
        View dialogView = inflater.inflate(R.layout.dialog_traloi_yeucauhotro, null);
        builder.setView(dialogView);

        EditText edtTraLoi = dialogView.findViewById(R.id.edtTraLoi);
        Button btnGui = dialogView.findViewById(R.id.btnGui);

        AlertDialog dialog = builder.create();

        btnGui.setOnClickListener(v -> {
            String noiDung = edtTraLoi.getText().toString().trim();

            if(noiDung.isEmpty()){
                Toast.makeText(context, "Vui lòng nhập nội dung trả lời!", Toast.LENGTH_SHORT).show();
                return;
            }

            // Cập nhật trạng thái xử lý và ngày xử lý
            String ngayXuLy = String.valueOf(System.currentTimeMillis());
            if (hoTroDAO.updateHoTroStatus(hoTro.getHoTroId(), 1, ngayXuLy)) {
                hoTro.setTrangThai(1);
                hoTro.setNgayXuLy(ngayXuLy);
                notifyItemChanged(position);

                sendNotificationToUser(hoTro.getUserId(), noiDung);

                showDialogThongBao("Thông báo", "Trả lời đã được gửi!");
                dialog.dismiss();
            } else {
                showDialogThongBao("Lỗi", "Lỗi khi xử lý yêu cầu!");
            }
        });

        dialog.show();
    }

    private void sendNotificationToUser(int userId, String noiDungTraLoi) {
        ThongBaoDAO thongBaoDAO = new ThongBaoDAO(context);
        boolean isSent = thongBaoDAO.sendThongBao(userId, -1, 3,noiDungTraLoi);

        if (isSent) {
            Toast.makeText(context, "Thông báo đã được gửi đến người dùng!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Lỗi khi gửi thông báo!", Toast.LENGTH_SHORT).show();
        }
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

    public void showDialogThongBao(String title, String noiDung){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title)
                .setMessage(noiDung)
                .setPositiveButton("OK", (dialog, which) -> dialog.dismiss());
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

}
