package group1.pro1122.duan1.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import group1.pro1122.duan1.daos.ThongBaoDAO;
import group1.pro1122.duan1.models.ThongBao;
import group1.pro1122.duan1.R;
public class ThongBaoAdapter extends RecyclerView.Adapter<ThongBaoAdapter.ViewHolder>{
    private List<ThongBao> thongBaoList;
    private Context context;

    public ThongBaoAdapter(Context context, List<ThongBao> thongBaoList) {
        this.context = context;
        this.thongBaoList = thongBaoList;
    }

    @Override
    public ThongBaoAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_thongbao, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ThongBaoAdapter.ViewHolder holder, int position) {
        ThongBao thongBao = thongBaoList.get(position);

        // Chuyển đổi loaiThongBao từ int sang String nếu cần thiết
        String loaiThongBaoText = getLoaiThongBaoText(thongBao.getLoaiThongBao());
        holder.tvLoaiThongBao.setText(loaiThongBaoText);

        holder.tvNoiDung.setText(thongBao.getNoiDung());

        // Xử lý sự kiện xóa
        holder.btnXoa.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("Xác nhận xóa thông báo")
                    .setMessage("Bạn có chắc chắn muốn xóa thông báo này không?")
                    .setPositiveButton("Có", (dialog, which) -> {
                        // Xử lý xóa thông báo
                        ThongBaoDAO thongBaoDAO = new ThongBaoDAO(context);
                        boolean check = thongBaoDAO.deleteThongBao(thongBao.getThongBaoId());
                        if (check){
                            thongBaoList.remove(position);
                            notifyDataSetChanged();
                        }
                    })
                    .setNegativeButton("Không", (dialog, which) -> dialog.dismiss())
                    .show();
        });
    }

    @Override
    public int getItemCount() {
        return thongBaoList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvLoaiThongBao;
        TextView tvNoiDung;
        ImageView btnXoa;

        public ViewHolder(View itemView) {
            super(itemView);
            tvLoaiThongBao = itemView.findViewById(R.id.tvLoaiThongBao);
            tvNoiDung = itemView.findViewById(R.id.tvNoiDung);
            btnXoa = itemView.findViewById(R.id.btnXoaTB);
        }
    }

    // Phương thức chuyển đổi loaiThongBao từ int sang String
    private String getLoaiThongBaoText(int loaiThongBao) {
        switch (loaiThongBao) {
            case 0:
                return "Yêu cầu thuê phòng";
            case 1:
                return "Thông báo thanh toán";
            case 2:
                return "Thông báo hợp đồng";
            case 3:
                return "Phản hồi hệ thống";
            default:
                return "Thông báo khác";
        }
    }
}
