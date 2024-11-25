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
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

import group1.pro1122.duan1.R;
import group1.pro1122.duan1.adapters.ThanhToanAdapter;
import group1.pro1122.duan1.adapters.XemThanhToanAdapter;
import group1.pro1122.duan1.daos.HopDongDAO;
import group1.pro1122.duan1.daos.PhiDichVuDAO;
import group1.pro1122.duan1.daos.PhongDAO;
import group1.pro1122.duan1.daos.ThanhToanDAO;
import group1.pro1122.duan1.daos.ThongBaoDAO;
import group1.pro1122.duan1.models.HopDong;
import group1.pro1122.duan1.models.Phong;
import group1.pro1122.duan1.models.ThanhToan;


public class XemThanhToanFragment extends Fragment {

    SearchView searchView;
    ImageView btnFilter;
    RecyclerView rcvThanhToan;
    TextView tvThanhToan;
    FloatingActionButton fab;
    XemThanhToanAdapter xemThanhToanAdapter;
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
        return inflater.inflate(R.layout.fragment_xem_thanh_toan, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        searchView = view.findViewById(R.id.searchView);
        btnFilter = view.findViewById(R.id.btnFilter);
        rcvThanhToan = view.findViewById(R.id.rcvThanhToan);
        tvThanhToan = view.findViewById(R.id.tvThanhToan);
        hopDongDAO = new HopDongDAO(getContext());
        phiDichVuDAO = new PhiDichVuDAO(getContext());
        thongBaoDAO = new ThongBaoDAO(getContext());
        phongDAO = new PhongDAO(getContext());

        SharedPreferences pref = getContext().getSharedPreferences("user", Context.MODE_PRIVATE);
        int userID = pref.getInt("userID", -1);

        // Lấy danh sách thanh toán từ CSDL của Người thuê
        thanhToanDAO = new ThanhToanDAO(getContext());
        listThanhToan = thanhToanDAO.getThanhToanByNguoiThue(userID);

        //Set adapter
        rcvThanhToan.setLayoutManager(new LinearLayoutManager(getContext()));
        xemThanhToanAdapter = new XemThanhToanAdapter(getContext(), listThanhToan);
        rcvThanhToan.setAdapter(xemThanhToanAdapter);


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
        xemThanhToanAdapter = new XemThanhToanAdapter(getContext(), filteredList);
        rcvThanhToan.setAdapter(xemThanhToanAdapter);
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
        xemThanhToanAdapter = new XemThanhToanAdapter(getContext(), filteredList);
        rcvThanhToan.setAdapter(xemThanhToanAdapter);
    }
}