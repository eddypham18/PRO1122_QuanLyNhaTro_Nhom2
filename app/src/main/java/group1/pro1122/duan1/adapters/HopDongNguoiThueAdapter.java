package group1.pro1122.duan1.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import group1.pro1122.duan1.R;
import group1.pro1122.duan1.daos.DiaDiemDAO;
import group1.pro1122.duan1.daos.HopDongDAO;
import group1.pro1122.duan1.daos.PhiDichVuDAO;
import group1.pro1122.duan1.daos.PhongDAO;
import group1.pro1122.duan1.daos.UserDAO;
import group1.pro1122.duan1.models.HopDong;
import group1.pro1122.duan1.models.PhiDichVu;
import group1.pro1122.duan1.models.Phong;
import group1.pro1122.duan1.models.User;

public class HopDongNguoiThueAdapter extends RecyclerView.Adapter<HopDongNguoiThueAdapter.ViewHolder>{
    private final Context context;
    private ArrayList<HopDong> listHopDong;
    private UserDAO userDAO;
    private PhongDAO phongDAO;
    private DiaDiemDAO diaDiemDAO;
    private PhiDichVuDAO phiDichVuDAO;
    private HopDongDAO hopDongDAO;

    public HopDongNguoiThueAdapter(Context context, ArrayList<HopDong> listHopDong) {
        this.context = context;
        this.listHopDong = listHopDong;
        this.userDAO = new UserDAO(context);
        this.phongDAO = new PhongDAO(context);
        this.diaDiemDAO = new DiaDiemDAO(context);
        this.phiDichVuDAO = new PhiDichVuDAO(context);
        this.hopDongDAO = new HopDongDAO(context);
    }


    @NonNull
    @Override
    public HopDongNguoiThueAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_hopdong_nguoithue, parent, false);
        return new HopDongNguoiThueAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HopDongNguoiThueAdapter.ViewHolder holder, int position) {
        HopDong hopDong = listHopDong.get(position);

        Phong phong = phongDAO.getPhongByPhongID(hopDong.getPhongId());
        holder.tvHopDongId.setText("Hợp đồng ID số " + hopDong.getHopDongId());
        holder.tvPhongId.setText("Phòng ID: " + phong.getPhongId());
        holder.tvPhong.setText("Phòng: " + phong.getSoPhong());
        User user = userDAO.getUserByUserID(hopDong.getUserId());
        holder.tvNguoiThueID.setText("Người thuê ID: " + hopDong.getUserId());
        holder.tvNguoiThue.setText("Người thuê: " + user.getHoTen());


        if(hopDong.getTrangThaiHopDong() == 0){
            holder.tvTrangThai.setText("Đã kết thúc");
            holder.tvTrangThai.setTextColor(context.getResources().getColor(R.color.red));
        } else if (hopDong.getTrangThaiHopDong() == 1) {
            holder.tvTrangThai.setText("Đang hiệu lực");
            holder.tvTrangThai.setTextColor(context.getResources().getColor(R.color.green));
        } else{
            holder.tvTrangThai.setText("Chưa kí kết");
            holder.tvTrangThai.setTextColor(context.getResources().getColor(R.color.black));
        }

        holder.btnXemChiTiet.setOnClickListener(v -> {
            showDialogChiTietHopDong(hopDong);
        });
    }

    @Override
    public int getItemCount() {
        return listHopDong.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvHopDongId, tvPhongId, tvPhong,tvNguoiThue, tvNguoiThueID, tvTrangThai;
        Button btnXemChiTiet;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            // Ánh xạ các view từ layout item_hop_dong.xml
            tvHopDongId = itemView.findViewById(R.id.tvID);
            tvPhongId = itemView.findViewById(R.id.tvPhongID);
            tvPhong = itemView.findViewById(R.id.tvPhong);
            tvNguoiThue = itemView.findViewById(R.id.tvNguoiThue);
            tvNguoiThueID = itemView.findViewById(R.id.tvNguoiThueID);
            tvTrangThai = itemView.findViewById(R.id.tvTrangThaiHopDong);

            btnXemChiTiet = itemView.findViewById(R.id.btnXemChiTiet);
        }
    }

    public void showDialogChiTietHopDong(HopDong hopDong){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_hopdong_chitiet, null);
        builder.setView(view);

        //Ánh xạ
        TextView tvNgayThangNam = view.findViewById(R.id.tvNgayThangNam);
        TextView tvHomNayNgayThangNam = view.findViewById(R.id.tvHomNayNgayThangNam);
        TextView tvChuTro = view.findViewById(R.id.tvChuTro);
        TextView tvCCCD = view.findViewById(R.id.tvCCCD);
        TextView tvNgaySinh = view.findViewById(R.id.tvNgaySinh);
        TextView tvSDT = view.findViewById(R.id.tvSDT);
        TextView tvEmail = view.findViewById(R.id.tvEmail);
        TextView tvNguoiThue = view.findViewById(R.id.tvNguoiThue);
        TextView tvCCCDNguoiThue = view.findViewById(R.id.tvCCCDNguoiThue);
        TextView tvNgaySinhNguoiThue = view.findViewById(R.id.tvNgaySinhNguoiThue);
        TextView tvSDTNguoiThue = view.findViewById(R.id.tvSDTNguoiThue);
        TextView tvEmailNguoiThue = view.findViewById(R.id.tvEmailNguoiThue);
        TextView tvDiaChi = view.findViewById(R.id.tvDiaChi);
        TextView tvSoPhong = view.findViewById(R.id.tvSoPhong);
        TextView tvGiaThue = view.findViewById(R.id.tvGiaThue);
        TextView tvDienTich = view.findViewById(R.id.tvDienTich);
        TextView tvTienDien = view.findViewById(R.id.tvTienDien);
        TextView tvTienNuoc = view.findViewById(R.id.tvTienNuoc);
        TextView tvTienVeSinh = view.findViewById(R.id.tvTienVeSinh);
        TextView tvTienGuiXe = view.findViewById(R.id.tvTienGuiXe);
        TextView tvTienInternet = view.findViewById(R.id.tvTienInternet);
        TextView tvTienThangMay = view.findViewById(R.id.tvTienThangMay);
        TextView tvGiaTriDen = view.findViewById(R.id.tvGiaTriDen);
        TextView tvBenAKyTen = view.findViewById(R.id.tvBenAKyTen);
        TextView tvBenBKyTen = view.findViewById(R.id.tvBenBKyTen);
        MaterialButton btnKy = view.findViewById(R.id.btnKy);
        MaterialButton btnDong = view.findViewById(R.id.btnDong);


        //Tạo dialog
        AlertDialog dialog = builder.create();

        //Set text
        String ngayTaoHopDong = formatDate(hopDong.getNgayTaoHopDong());
        tvNgayThangNam.setText(formatDate(ngayTaoHopDong));
        tvHomNayNgayThangNam.setText("Hôm này, " +formatDate(ngayTaoHopDong));

        //Chủ trọ
        User chuTro = userDAO.getUserByUserID(hopDong.getChuTro());
        tvChuTro.setText("Chủ trọ: " + chuTro.getHoTen());
        tvCCCD.setText("CCCD: " + chuTro.getCccd());
        tvNgaySinh.setText("Sinh: " + chuTro.getNgaySinh());
        tvSDT.setText("SDT: " + chuTro.getSdt());
        tvEmail.setText("Email: " + chuTro.getEmail());

        //Người thuê
        User nguoiThue = userDAO.getUserByUserID(hopDong.getUserId());
        tvNguoiThue.setText("Người thuê: " + nguoiThue.getHoTen());
        tvCCCDNguoiThue.setText("CCCD: " + nguoiThue.getCccd());
        tvNgaySinhNguoiThue.setText("Sinh: " + nguoiThue.getNgaySinh());
        tvSDTNguoiThue.setText("SDT: " + nguoiThue.getSdt());
        tvEmailNguoiThue.setText("Email: " + nguoiThue.getEmail());

        //Phòng
        Phong phong = phongDAO.getPhongByPhongID(hopDong.getPhongId());
        tvDiaChi.setText("Địa chỉ: " + diaDiemDAO.getDiaDiemByDiaDiemID(phong.getDiaDiemID()).getThanhPho());
        tvSoPhong.setText("Số phòng: " + phong.getSoPhong());
        tvGiaThue.setText("Giá thuê: " + phong.getGiaThue()+" triệu/tháng");
        tvDienTich.setText("Diện tích: " + phong.getDienTich()+" m2");

        //Phí dịch vụ
        PhiDichVu phiDichVu = phiDichVuDAO.getPhiDichVuByID(hopDong.getPhiDichVuId());
        tvTienDien.setText("Tiền điện: " + phiDichVu.getDonGiaDien()+" đồng/kwh");
        tvTienNuoc.setText("Tiền nước: " + phiDichVu.getDonGiaNuoc()+" đồng/m3");
        tvTienVeSinh.setText("Tiền vệ sinh: " + phiDichVu.getVeSinh()+" đồng/tháng");
        tvTienGuiXe.setText("Tiền gửi xe: " + phiDichVu.getGuiXe()+" đồng/tháng");
        tvTienInternet.setText("Tiền internet: " + phiDichVu.getInternet()+" đồng/tháng");
        tvTienThangMay.setText("Tiền thang máy: " + phiDichVu.getThangMay()+" đồng/tháng");
        tvGiaTriDen.setText("Hợp đồng có giá trị kể từ ngày kí kết đến "+formatDate(hopDong.getNgayKetThuc()));

        //Ký tên
        tvBenAKyTen.setText("Bên A đã ký \n" + chuTro.getHoTen());
        if(hopDong.getTrangThaiHopDong() == 2){
            tvBenBKyTen.setText("Bên B chưa ký");
            btnKy.setVisibility(View.VISIBLE);
        } else {
            tvBenBKyTen.setText("Bên B đã ký \n" + nguoiThue.getHoTen());
            btnKy.setVisibility(View.INVISIBLE);
        }

        //Ký hợp đồng
        btnKy.setOnClickListener(v -> {

            boolean check = hopDongDAO.kyHopDong(hopDong.getHopDongId());
            if (check){
                hopDong.setTrangThaiHopDong(1);
                phongDAO.updateTrangThaiPhong(phong.getPhongId(), 1);
                tvBenBKyTen.setText("Bên B đã ký \n" + nguoiThue.getHoTen());
                btnKy.setVisibility(View.INVISIBLE);
                notifyDataSetChanged();
            }
        });

        //Đóng hợp đồng
        btnDong.setOnClickListener(v -> {
            dialog.dismiss();
        });

        dialog.show();
    }

    private String formatDate(String ngayTaoHopDong) {
        try {
            // Chuyển chuỗi gốc thành đối tượng Date
            SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date date = inputFormat.parse(ngayTaoHopDong);

            SimpleDateFormat outputFormat = new SimpleDateFormat("'ngày' dd 'tháng' MM 'năm' yyyy");
            return outputFormat.format(date);
        } catch (Exception e) {
            e.printStackTrace();
            return ngayTaoHopDong;
        }
    }
}
