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
public class TrangChuAdapter extends RecyclerView.Adapter<TrangChuAdapter.TrangChuViewHolder> {
    private Context context;
    private ArrayList<Phong> listPhong;
    private TrangChuAdapter.OnClickListener onClickListener;

    private DiaDiemDAO diaDiemDAO;
    private ArrayList<DiaDiem> listDiaDiem;

    public TrangChuAdapter(Context context, ArrayList<Phong> listPhong) {
        this.context = context;
        this.listPhong = listPhong;
    }

    public interface OnClickListener {
        void onClick(Phong phong);
    }

    public void setOnClickListener(TrangChuAdapter.OnClickListener listener) {
        this.onClickListener = listener;
    }


    @NonNull
    @Override
    public TrangChuAdapter.TrangChuViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_phong_trangchu, parent, false);
        return new TrangChuAdapter.TrangChuViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TrangChuAdapter.TrangChuViewHolder holder, int position) {
        Phong phong = listPhong.get(position);

        holder.tvSoPhong.setText("Phòng: " + phong.getSoPhong());
        holder.tvDienTich.setText("Diện tích: " + phong.getDienTich() + " m²");
        holder.tvGiaThue.setText("Giá thuê: " + phong.getGiaThue() + " VND");
        diaDiemDAO = new DiaDiemDAO(context);
        listDiaDiem = diaDiemDAO.read();
        for (DiaDiem diaDiem : listDiaDiem) {
            if (phong.getDiaDiemID() == diaDiem.getDiaDiemId()) {
                holder.tvDiaDiem.setText("Địa điểm: " +diaDiem.getThanhPho());
                break;
            }
        }
        holder.tvNgayTao.setText("Ngày đăng: " + convertTimestampToDate(phong.getNgayTao()));
        holder.tvMoTa.setText("Mô tả: " + phong.getMoTa());

        // Hiển thị ảnh từ byte[]
        Bitmap bitmap = BitmapFactory.decodeByteArray(phong.getAnhPhong(), 0, phong.getAnhPhong().length);
        holder.imgPhong.setImageBitmap(bitmap);

        // Thiết lập sự kiện cho nút "Duyệt"
        holder.btnThue.setOnClickListener(v -> {
            if (onClickListener != null) {
                onClickListener.onClick(phong);
            }
        });

    }

    @Override
    public int getItemCount() {
        return listPhong.size();
    }

    // ViewHolder cho BaiDangAdapter
    public static class TrangChuViewHolder extends RecyclerView.ViewHolder {

        ImageView imgPhong;
        TextView tvSoPhong, tvDienTich, tvGiaThue, tvDiaDiem, tvNgayTao, tvMoTa;
        MaterialButton btnThue;

        public TrangChuViewHolder(@NonNull View itemView) {
            super(itemView);

            imgPhong = itemView.findViewById(R.id.imgPhong);
            tvSoPhong = itemView.findViewById(R.id.tvSoPhong);
            tvDienTich = itemView.findViewById(R.id.tvDienTich);
            tvGiaThue = itemView.findViewById(R.id.tvGiaThue);
            tvDiaDiem = itemView.findViewById(R.id.tvDiaDiem);
            tvNgayTao = itemView.findViewById(R.id.tvNgayDang);
            tvMoTa = itemView.findViewById(R.id.tvMoTa);
            btnThue = itemView.findViewById(R.id.btnThue);
        }
    }

    private String convertTimestampToDate(String timestamp) {
        try {
            long timeMillis = Long.parseLong(timestamp);
            Date date = new Date(timeMillis);
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
            return sdf.format(date);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return "Invalid date";
        }
    }
}
