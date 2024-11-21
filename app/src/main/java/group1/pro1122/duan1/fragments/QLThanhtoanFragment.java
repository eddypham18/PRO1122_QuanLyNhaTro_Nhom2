package group1.pro1122.duan1.fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

import group1.pro1122.duan1.R;
import group1.pro1122.duan1.adapters.ThanhToanAdapter;
import group1.pro1122.duan1.daos.HopDongDAO;
import group1.pro1122.duan1.daos.PhiDichVuDAO;
import group1.pro1122.duan1.daos.PhongDAO;
import group1.pro1122.duan1.daos.ThanhToanDAO;
import group1.pro1122.duan1.daos.ThongBaoDAO;
import group1.pro1122.duan1.models.HopDong;
import group1.pro1122.duan1.models.PhiDichVu;
import group1.pro1122.duan1.models.Phong;
import group1.pro1122.duan1.models.ThanhToan;


public class QLThanhtoanFragment extends Fragment {

    SearchView searchView;
    ImageView btnFilter;
    RecyclerView rcvThanhToan;
    TextView tvThanhToan;
    FloatingActionButton fab;
    ThanhToanAdapter thanhToanAdapter;
    ArrayList<ThanhToan> listThanhToan;
    ThanhToanDAO thanhToanDAO;
    HopDongDAO hopDongDAO;
    PhiDichVuDAO phiDichVuDAO;
    ThongBaoDAO thongBaoDAO;
    PhongDAO phongDAO;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_q_l_thanhtoan, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        searchView = view.findViewById(R.id.searchView);
        btnFilter = view.findViewById(R.id.btnFilter);
        rcvThanhToan = view.findViewById(R.id.rcvThanhToan);
        tvThanhToan = view.findViewById(R.id.tvThanhToan);
        fab = view.findViewById(R.id.fab);
        hopDongDAO = new HopDongDAO(getContext());
        phiDichVuDAO = new PhiDichVuDAO(getContext());
        thongBaoDAO = new ThongBaoDAO(getContext());
        phongDAO = new PhongDAO(getContext());

        SharedPreferences pref = getContext().getSharedPreferences("user", Context.MODE_PRIVATE);
        int userID = pref.getInt("userID", -1);

        // Lấy danh sách thanh toán từ CSDL của Chủ trọ
        thanhToanDAO = new ThanhToanDAO(getContext());
        listThanhToan = thanhToanDAO.getThanhToanByChuTro(userID);

        //Set adapter
        rcvThanhToan.setLayoutManager(new LinearLayoutManager(getContext()));
        thanhToanAdapter = new ThanhToanAdapter(getContext(), listThanhToan);
        rcvThanhToan.setAdapter(thanhToanAdapter);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchByRoomName(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                searchByRoomName(newText);
                return true;
            }
        });

        //Lọc
        btnFilter.setOnClickListener(v -> showFilterDialog());

        //Nút thêm
        fab.setOnClickListener(v -> {
            showAddThanhToanDialog();
        });
    }

    private void filterList(int trangThai, int sapXep) {
        ArrayList<ThanhToan> filteredList = new ArrayList<>(listThanhToan);

        // Lọc theo trạng thái
        if (trangThai == 1) { // Chưa thanh toán
            filteredList.removeIf(thanhToan -> thanhToan.getTrangThaiThanhToan() != 0);
        } else if (trangThai == 2) { // Đã thanh toán
            filteredList.removeIf(thanhToan -> thanhToan.getTrangThaiThanhToan() != 1);
        }

        // Sắp xếp theo ngày
        if (sapXep == 1) { // Tăng dần
            filteredList.sort((o1, o2) -> Long.compare(
                    Long.parseLong(o1.getNgayTao()), Long.parseLong(o2.getNgayTao())));
        } else if (sapXep == 2) { // Giảm dần
            filteredList.sort((o1, o2) -> Long.compare(
                    Long.parseLong(o2.getNgayTao()), Long.parseLong(o1.getNgayTao())));
        }

        // Cập nhật adapter
        thanhToanAdapter = new ThanhToanAdapter(getContext(), filteredList);
        rcvThanhToan.setAdapter(thanhToanAdapter);
    }


    private void showFilterDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_filter_thanhtoan, null);
        builder.setView(dialogView);

        Spinner spinnerTrangThai = dialogView.findViewById(R.id.spinnerTrangThai);
        Spinner spinnerSapXep = dialogView.findViewById(R.id.spinnerSapXep);
        Button btnApDung = dialogView.findViewById(R.id.btnApDung);

        AlertDialog alertDialog = builder.create();

        btnApDung.setOnClickListener(v -> {
            int trangThai = spinnerTrangThai.getSelectedItemPosition(); // 0: Tất cả, 1: Chưa thanh toán, 2: Đã thanh toán
            int sapXep = spinnerSapXep.getSelectedItemPosition(); // 0: Không, 1: Tăng dần, 2: Giảm dần

            filterList(trangThai, sapXep);
            alertDialog.dismiss();
        });

        alertDialog.show();
    }

    private void searchByRoomName(String query) {
        ArrayList<ThanhToan> filteredList = new ArrayList<>();
        for (ThanhToan thanhToan : listThanhToan) {
            // Lấy thông tin phòng từ HopDong và Phong
            HopDong hopDong = hopDongDAO.getHopDongByHopDongID(thanhToan.getHopDongId());
            Phong phong = phongDAO.getPhongByPhongID(hopDong.getPhongId());

            // Kiểm tra nếu tên phòng chứa chuỗi query
            if (phong.getSoPhong().toLowerCase().contains(query.toLowerCase())) {
                filteredList.add(thanhToan);
            }
        }

        // Cập nhật adapter
        thanhToanAdapter = new ThanhToanAdapter(getContext(), filteredList);
        rcvThanhToan.setAdapter(thanhToanAdapter);
    }

    public void showAddThanhToanDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_thanhtoan, null);
        builder.setView(view);

        TextView tvTitle = view.findViewById(R.id.tvTitle);
        EditText edtHopDongID = view.findViewById(R.id.edtHopDongID);
        EditText edtSoDien = view.findViewById(R.id.edtSoDien);
        EditText edtSoNuoc = view.findViewById(R.id.edtSoNuoc);
        Button btnTaoPhieu = view.findViewById(R.id.btnTaoPhieu);

        AlertDialog dialog = builder.create();

        btnTaoPhieu.setOnClickListener(v -> {
            // Lấy dữ liệu từ dialog
            int hopDongID = -1;
            int soDien = 0;
            int soNuoc = 0;

            try {
                hopDongID = Integer.parseInt(edtHopDongID.getText().toString());
                soDien = Integer.parseInt(edtSoDien.getText().toString());
                soNuoc = Integer.parseInt(edtSoNuoc.getText().toString());
            } catch (NumberFormatException e) {
                Toast.makeText(getContext(), "Vui lòng nhập đúng dữ liệu!", Toast.LENGTH_SHORT).show();
                return;
            }

            if (hopDongID == -1 || soDien <= 0 || soNuoc <= 0) {
                Toast.makeText(getContext(), "Vui lòng nhập lại!", Toast.LENGTH_SHORT).show();
                return;
            }

            // Kiểm tra hợp đồng có tồn tại hay không
            HopDong hopDong = hopDongDAO.getHopDongByHopDongID(hopDongID);
            if (hopDong == null) {
                Toast.makeText(getContext(), "Không tìm thấy hợp đồng!", Toast.LENGTH_SHORT).show();
                return;
            }

            if(hopDong.getTrangThaiHopDong() != 1){
                Toast.makeText(getContext(), "Hợp đồng không hợp lệ! Hợp đồng đã hết hạn hoặc chưa kí kết", Toast.LENGTH_SHORT).show();
                return;
            }
            Phong phong = phongDAO.getPhongByPhongID(hopDong.getPhongId());
            if(phong.getTrangThai() == 0){
                Toast.makeText(getContext(), "Phòng đang trống!", Toast.LENGTH_SHORT).show();
                return;
            }

            // Tính tổng tiền dựa trên đơn giá từ bảng PhiDichVu
            int phiDichVuID = hopDong.getPhiDichVuId();
            int tongTien = 0;

            if (phiDichVuID > 0) {
                PhiDichVu phiDichVu = phiDichVuDAO.getPhiDichVuByID(phiDichVuID);
                if (phiDichVu != null) {
                    tongTien = (phiDichVu.getDonGiaDien() * soDien)
                            + (phiDichVu.getDonGiaNuoc() * soNuoc)
                            + phiDichVu.getVeSinh()
                            + phiDichVu.getInternet()
                            + (phiDichVu.getThangMay() != null ? phiDichVu.getThangMay() : 0)
                            + (phiDichVu.getGuiXe() != null ? phiDichVu.getGuiXe() : 0)
                            + phong.getGiaThue();
                }
            }


            ThanhToan thanhToan = new ThanhToan(0, hopDongID, String.valueOf(System.currentTimeMillis()), soDien, soNuoc, tongTien, 0);

            // Lưu vào CSDL
            boolean isAdded = thanhToanDAO.addThanhToan(thanhToan);
            if (isAdded) {
                showDialogThongBao("Thông báo", "Tạo phiếu thanh toán thành công! Đã gửi thông báo đến khách thuê.");
                listThanhToan.add(thanhToan);
                thanhToanAdapter.notifyDataSetChanged();

                //Gửi thông báo đến khách thuê
                int khachThue = hopDong.getUserId();
                int loaiThongBao = 1; //Tức là thông báo về phiếu thanh toán
                String noiDung = "Chủ trọ đã tạo phiếu thanh toán cho hợp đồng số " + hopDongID+". Vui lòng kiểm tra phiếu thanh toán !";
                thongBaoDAO.sendThongBao(khachThue, hopDongID, loaiThongBao, noiDung);
            } else {
                showDialogThongBao("Lỗi", "Tạo phiếu thanh toán thất bại!");
            }

            dialog.dismiss();
        });
        dialog.show();
    }

    public void showDialogThongBao(String title, String noiDung){
        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(getContext());
        builder.setTitle(title)
                .setMessage(noiDung)
                .setPositiveButton("OK", (dialog, which) -> dialog.dismiss());
        androidx.appcompat.app.AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}