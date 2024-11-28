package group1.pro1122.duan1.adapters;
import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import group1.pro1122.duan1.R;
import group1.pro1122.duan1.daos.DiaDiemDAO;
import group1.pro1122.duan1.daos.HopDongDAO;
import group1.pro1122.duan1.daos.PhiDichVuDAO;
import group1.pro1122.duan1.daos.PhongDAO;
import group1.pro1122.duan1.daos.ThongBaoDAO;
import group1.pro1122.duan1.daos.UserDAO;
import group1.pro1122.duan1.models.HopDong;
import group1.pro1122.duan1.models.PhiDichVu;
import group1.pro1122.duan1.models.Phong;
import group1.pro1122.duan1.models.User;

public class HopDongAdapter extends RecyclerView.Adapter<HopDongAdapter.ViewHolder>{
    private final Context context;
    private ArrayList<HopDong> listHopDong;
    private UserDAO userDAO;
    private PhongDAO phongDAO;
    private DiaDiemDAO diaDiemDAO;
    private PhiDichVuDAO phiDichVuDAO;
    private HopDongDAO hopDongDAO;
    private ThongBaoDAO thongBaoDAO;

    // Constructor
    public HopDongAdapter(Context context, ArrayList<HopDong> listHopDong) {
        this.context = context;
        this.listHopDong = listHopDong;
        this.userDAO = new UserDAO(context);
        this.phongDAO = new PhongDAO(context);
        this.diaDiemDAO = new DiaDiemDAO(context);
        this.phiDichVuDAO = new PhiDichVuDAO(context);
        this.hopDongDAO = new HopDongDAO(context);
        this.thongBaoDAO = new ThongBaoDAO(context);
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
        Phong phong = phongDAO.getPhongByPhongID(hopDong.getPhongId());
        holder.tvHopDongId.setText("Hợp đồng ID số " + hopDong.getHopDongId());
        holder.tvPhongId.setText("Phòng ID: " + hopDong.getPhongId());
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

        // Bấm giữ để sửa hợp đồng
        holder.itemView.setOnLongClickListener(v -> {
            if (hopDong.getTrangThaiHopDong() == 2) {
                showDialogEditHopDong(hopDong); // Hiển thị dialog sửa hợp đồng
            } else {
                showDialogThongBao("Thông báo", "Chỉ có thể sửa hợp đồng chưa kí kết!");
            }
            return true;
        });

        //Hủy hợp đồng
        holder.btnHuyHopDong.setOnClickListener(v -> {
            androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(context);
            builder.setTitle("Xác nhận")
                    .setMessage("Bạn có chắc chắn muốn hủy hợp đồng này?")
                    .setPositiveButton("OK", (dialog, which) -> {
                        if(hopDong.getTrangThaiHopDong() == 2 || hopDong.getTrangThaiHopDong() == 1){
                            boolean result = hopDongDAO.huyHopDong(hopDong.getHopDongId());
                            if(result){
                                hopDong.setTrangThaiHopDong(0);

                                showDialogThongBao("Thông báo", "Hủy hợp đồng thành công!");
                                //Gửi thông báo!
                                thongBaoDAO = new ThongBaoDAO(context);
                                String noiDung = "Chủ trọ ID: "+hopDong.getChuTro()+" đã hủy Hợp đồng với ID là"+ hopDong.getHopDongId()+" của phòng " + phong.getSoPhong();
                                thongBaoDAO.sendThongBao(hopDong.getUserId(), (int) hopDong.getHopDongId(), 2, noiDung); // 2 là loại thông báo về hợp đồng

                                //Set lại trạng thái của phòng nếu phòng đó đã được ký kết - đã thuê thì đổi thành trống
                                Phong phong1 = phongDAO.getPhongByPhongID(hopDong.getPhongId());
                                if(phong1.getTrangThai() == 1){
                                    phong1.setTrangThai(0);
                                    phongDAO.updateTrangThaiPhong(hopDong.getPhongId(), 0);
                                }
                                notifyDataSetChanged();
                            } else{
                                showDialogThongBao("Lỗi", "Hủy hợp đồng thất bại!");
                            }
                        } else{
                            showDialogThongBao("Thông báo", "Chỉ có thể hủy hợp đồng chưa kí kết!");
                        }
                    })
                    .setNegativeButton("Không", (dialog, which) -> dialog.dismiss());
            androidx.appcompat.app.AlertDialog alertDialog = builder.create();
            alertDialog.show();
        });
    }

    @Override
    public int getItemCount() {
        return listHopDong.size();
    }

    public void refreshList(ArrayList<HopDong> newList) {
        this.listHopDong = newList;
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvHopDongId, tvPhongId, tvPhong,tvNguoiThue, tvNguoiThueID, tvTrangThai;
        Button btnXemChiTiet, btnHuyHopDong;

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
            btnHuyHopDong = itemView.findViewById(R.id.btnHuyHopDong);
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
        } else {
            tvBenBKyTen.setText("Bên B đã ký \n" + nguoiThue.getHoTen());
        }

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

    public void showDialogThongBao(String title, String noiDung){
        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(context);
        builder.setTitle(title)
                .setMessage(noiDung)
                .setPositiveButton("OK", (dialog, which) -> dialog.dismiss());
        androidx.appcompat.app.AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void showDialogEditHopDong(HopDong hopDong) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_hopdong, null);
        builder.setView(view);

        // Ánh xạ các view
        TextView tvTitle = view.findViewById(R.id.tvTitle);
        tvTitle.setText("Cập nhật hợp đồng");
        EditText edtPhongId = view.findViewById(R.id.edtPhongId);
        EditText edtNguoiThue = view.findViewById(R.id.edtNguoiThue);
        EditText edtDonGiaDien = view.findViewById(R.id.edtDonGiaDien);
        EditText edtDonGiaNuoc = view.findViewById(R.id.edtDonGiaNuoc);
        EditText edtVeSinh = view.findViewById(R.id.edtVeSinh);
        EditText edtGuiXe = view.findViewById(R.id.edtGuiXe);
        EditText edtInternet = view.findViewById(R.id.edtInternet);
        EditText edtThangMay = view.findViewById(R.id.edtThangMay);
        Button btnTao = view.findViewById(R.id.btnTao);
        btnTao.setText("CẬP NHẬT");

        // Đổ dữ liệu hiện tại vào các trường
        PhiDichVu phiDichVu = phiDichVuDAO.getPhiDichVuByID(hopDong.getPhiDichVuId());
        edtPhongId.setText(String.valueOf(hopDong.getPhongId()));
        edtNguoiThue.setText(String.valueOf(hopDong.getUserId()));
        edtDonGiaDien.setText(String.valueOf(phiDichVu.getDonGiaNuoc()));
        edtDonGiaNuoc.setText(String.valueOf(phiDichVu.getDonGiaNuoc()));
        edtVeSinh.setText(String.valueOf(phiDichVu.getVeSinh()));
        edtGuiXe.setText(String.valueOf(phiDichVu.getGuiXe()));
        edtInternet.setText(String.valueOf(phiDichVu.getInternet()));
        edtThangMay.setText(String.valueOf(phiDichVu.getThangMay()));

        AlertDialog dialog = builder.create();

        btnTao.setOnClickListener(v -> {
            String phongId = edtPhongId.getText().toString().trim();
            String nguoiThue = edtNguoiThue.getText().toString().trim();
            String donGiaDien = edtDonGiaDien.getText().toString().trim();
            String donGiaNuoc = edtDonGiaNuoc.getText().toString().trim();
            String veSinh = edtVeSinh.getText().toString().trim();
            String guiXe = edtGuiXe.getText().toString().trim();
            String internet = edtInternet.getText().toString().trim();
            String thangMay = edtThangMay.getText().toString().trim();

            if(phongId.isEmpty() || nguoiThue.isEmpty() || donGiaDien.isEmpty() || donGiaNuoc.isEmpty() || veSinh.isEmpty() || guiXe.isEmpty() || internet.isEmpty() || thangMay.isEmpty()){
                Toast.makeText(context, "Không để trống!", Toast.LENGTH_SHORT).show();
                return;
            }

            Phong phong = phongDAO.getPhongByPhongID(Integer.parseInt(phongId));
            if(phong == null){
                Toast.makeText(context, "Phòng không tồn tại!", Toast.LENGTH_SHORT).show();
                return;
            }

            if(phong.getTrangThai() == 1){
                Toast.makeText(context, "Phòng "+phong.getSoPhong()+" đã được thuê. Vui lòng chọn phòng khác!", Toast.LENGTH_SHORT).show();
                return;
            }

            if(phong.getTrangThaiPheduyet() == 0){
                Toast.makeText(context, "Phòng "+phong.getSoPhong()+" chưa được phê duyệt", Toast.LENGTH_SHORT).show();
            }

            // Tạo đối tượng PhiDichVu và chèn vào cơ sở dữ liệu
            PhiDichVu phiDichVuMoi = new PhiDichVu(0, Integer.parseInt(donGiaDien),Integer.parseInt( donGiaNuoc),Integer.parseInt(veSinh), Integer.parseInt(guiXe), Integer.parseInt(internet), Integer.parseInt(thangMay));
            long phiDichVuId = phiDichVuDAO.createPhiDichVu(phiDichVuMoi);
            if(phiDichVuId == -1){
                Toast.makeText(context, "Tạo phí dịch vụ thất bại", Toast.LENGTH_SHORT).show();
                return;
            }

            // Lấy ngày hiện tại
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            String ngayBatDau = sdf.format(new Date());

            // Tính ngày kết thúc hợp đồng (1 năm sau)
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.YEAR, 1);
            String ngayKetThuc = sdf.format(calendar.getTime());

            // Tạo đối tượng HopDong và chèn vào cơ sở dữ liệu
            SharedPreferences pref = context.getSharedPreferences("user", context.MODE_PRIVATE);
            int chuTro = pref.getInt("userID", -1);
            HopDong hopDongMoi = new HopDong(0, Integer.parseInt(phongId), Integer.parseInt(nguoiThue), chuTro, (int) phiDichVuId, ngayBatDau, ngayKetThuc, 2); //Trạng thại hợp đồng 2 - chưa kí kết
            boolean result = hopDongDAO.updateHopDong(hopDongMoi);
            if(result){
                dialog.dismiss();
                showDialogThongBao("Thông báo", "Cập nhật hợp đồng thành công! Đã gửi yêu cầu ký kết hợp đồng đến người thuê. Vui lòng đợi người thuê ký");
                //Gửi thông báo!
                thongBaoDAO = new ThongBaoDAO(context);
                String noiDung = "Chủ trọ ID: "+chuTro+" đã cập nhật lại Hợp đồng với ID là "+ hopDong.getHopDongId()+" của phòng " + phong.getSoPhong() +". Vui lòng kiểm tra và ký kết hợp đồng!";
                thongBaoDAO.sendThongBao(Integer.parseInt(nguoiThue), (int) hopDong.getHopDongId(), 2, noiDung); // 2 là loại thông báo về hợp đồng
            } else{
                showDialogThongBao("Lỗi", "Tạo hợp đồng thất bại!");
            }
        });


        dialog.show();
    }
}
