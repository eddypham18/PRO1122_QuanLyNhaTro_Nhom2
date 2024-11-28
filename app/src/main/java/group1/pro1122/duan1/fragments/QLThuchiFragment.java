package group1.pro1122.duan1.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

import group1.pro1122.duan1.R;
import group1.pro1122.duan1.adapters.ThuChiAdapter;
import group1.pro1122.duan1.daos.PhongDAO;
import group1.pro1122.duan1.daos.ThanhToanDAO;
import group1.pro1122.duan1.daos.ThuChiDAO;
import group1.pro1122.duan1.models.ChiPhi;
import group1.pro1122.duan1.models.Phong;
import group1.pro1122.duan1.models.ThanhToan;


public class QLThuchiFragment extends Fragment {
    private RecyclerView rcvThuChi;
    private ThuChiAdapter thuChiAdapter;
    private ArrayList<ChiPhi> chiPhiList;
    private ThuChiDAO thuChiDAO;
    private SearchView searchView;
    private ImageView btnFilter;
    private FloatingActionButton fab;
    private ThanhToanDAO thanhToanDAO;
    TextView tvThu, tvChi, tvLoiNhuan;
    SharedPreferences pref;
    int userId;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_q_l_thuchi, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Ánh xạ các thành phần
        rcvThuChi = view.findViewById(R.id.rcvThuChi);
        searchView = view.findViewById(R.id.searchView);
        btnFilter = view.findViewById(R.id.btnFilter);
        fab = view.findViewById(R.id.fab);
        tvThu = view.findViewById(R.id.tvThu);
        tvChi = view.findViewById(R.id.tvChi);
        tvLoiNhuan = view.findViewById(R.id.tvLoiNhuan);

        thuChiDAO = new ThuChiDAO(getContext());

        pref = getContext().getSharedPreferences("user", Context.MODE_PRIVATE);
        userId = pref.getInt("userID", -1);
        // Lấy dữ liệu từ cơ sở dữ liệu
        chiPhiList = thuChiDAO.getChiPhiByChuSoHuu(userId);
        thuChiAdapter = new ThuChiAdapter(getContext(), chiPhiList, this);

        // Thiết lập RecyclerView
        rcvThuChi.setLayoutManager(new LinearLayoutManager(getContext()));
        rcvThuChi.setAdapter(thuChiAdapter);

        //Tính tổng thu, chi, lợi nhuận
        thanhToanDAO = new ThanhToanDAO(getContext());
        ArrayList<ThanhToan> thanhToanList = thanhToanDAO.getThanhToanByChuTroAndTrangThaiIsTrue(userId);
       tinhTongThuChi(chiPhiList, thanhToanList);

        // Xử lý sự kiện tìm kiếm
        setupSearchFunction();

        fab.setOnClickListener(v -> {
            showAddChiPhiDialog();
        });


        // Xử lý sự kiện lọc
        btnFilter.setOnClickListener(v -> showFilterDialog());
    }

    public void tinhTongThuChi(ArrayList<ChiPhi> chiPhiList, ArrayList<ThanhToan> thanhToanList) {
        int tongThu = 0;
        int tongChi = 0;
        int loiNhuan;

        //Tính thu
        for (ThanhToan thanhToan : thanhToanList) {
            tongThu += thanhToan.getTongTien();
        }

        //Tính chi phí
        for (ChiPhi chiPhi : chiPhiList) {
            if (chiPhi.getSoTienChi() > 0) {
                tongChi += chiPhi.getSoTienChi();
            } else {
                tongThu += chiPhi.getSoTienChi();
            }
        }
        loiNhuan = tongThu - tongChi;
        tvThu.setText("Thu: " + tongThu + " VND");
        tvChi.setText("Chi: " + tongChi + " VND");
        tvLoiNhuan.setText("Lợi: " + loiNhuan + " VND");
    }

    private void setupSearchFunction() {
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchByName(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                searchByName(newText);
                return true;
            }
        });
    }

    private void searchByName(String query) {
        ArrayList<ChiPhi> filteredList = new ArrayList<>();
        for (ChiPhi chiPhi : chiPhiList) {
            if (chiPhi.getTenChiPhi().toLowerCase().contains(query.toLowerCase())) {
                filteredList.add(chiPhi);
            }
        }
        thuChiAdapter = new ThuChiAdapter(getContext(), filteredList, this);
        rcvThuChi.setAdapter(thuChiAdapter);
    }


    private void showFilterDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_filter_chiphi, null);
        builder.setView(dialogView);

        Spinner spinnerPhong = dialogView.findViewById(R.id.spinnerPhong);
        Spinner spinnerNgay = dialogView.findViewById(R.id.spinnerNgay);
        Button btnApDung = dialogView.findViewById(R.id.btnApDung);

        // Lấy danh sách phòng từ DAO
        ArrayList<Phong> phongList = new PhongDAO(getContext()).getPhongByChuSoHuu(userId);
        ArrayAdapter<String> phongAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item);
        phongAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        phongList.forEach(phong -> phongAdapter.add(phong.getSoPhong() + " - ID: " + phong.getPhongId()));
        spinnerPhong.setAdapter(phongAdapter);

        AlertDialog alertDialog = builder.create();

        btnApDung.setOnClickListener(v -> {
            String selectedPhong = (String) spinnerPhong.getSelectedItem();
            String selectedNgay = spinnerNgay.getSelectedItem().toString();

            int phongId = Integer.parseInt(selectedPhong.split(" - ID: ")[1]);

            filterChiPhi(phongId, selectedNgay);
            alertDialog.dismiss();
        });

        alertDialog.show();
    }

    private void filterChiPhi(int phongId, String selectedNgay) {
        ArrayList<ChiPhi> filteredList = new ArrayList<>();
        for (ChiPhi chiPhi : chiPhiList) {
            if (chiPhi.getPhongId() == phongId || phongId == 0) {
                filteredList.add(chiPhi);
            }
        }

        if (selectedNgay.equals("Ngày tăng dần")) {
            filteredList.sort((o1, o2) -> Long.compare(
                    Long.parseLong(o1.getNgayPhatSinh()), Long.parseLong(o2.getNgayPhatSinh())));
        } else if (selectedNgay.equals("Ngày giảm dần")) {
            filteredList.sort((o1, o2) -> Long.compare(
                    Long.parseLong(o2.getNgayPhatSinh()), Long.parseLong(o1.getNgayPhatSinh())));
        }

        thuChiAdapter = new ThuChiAdapter(getContext(), filteredList, this);
        rcvThuChi.setAdapter(thuChiAdapter);
    }



    private void showAddChiPhiDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_add_chiphi, null);
        builder.setView(dialogView);

        // Ánh xạ các thành phần trong dialog
        TextView tvTitle = dialogView.findViewById(R.id.tvTitle);
        tvTitle.setText("Thêm Chi Phí");
        Spinner spinnerPhong = dialogView.findViewById(R.id.spinnerPhong);
        TextView edtTenChiPhi = dialogView.findViewById(R.id.edtTenChiPhi);
        TextView edtSoTienChi = dialogView.findViewById(R.id.edtSoTienChi);
        Button btnLuu = dialogView.findViewById(R.id.btnLuu);

        // Lấy danh sách phòng
        ArrayList<Phong> phongList = new PhongDAO(getContext()).getPhongByChuSoHuu(userId);
        ArrayAdapter<String> phongAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item);
        phongAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        phongList.forEach(phong -> phongAdapter.add(phong.getSoPhong() + " - ID: " + phong.getPhongId()));
        spinnerPhong.setAdapter(phongAdapter);

        AlertDialog alertDialog = builder.create();

        // Xử lý sự kiện nút Lưu
        btnLuu.setOnClickListener(v -> {
            String tenChiPhi = edtTenChiPhi.getText().toString().trim();
            String soTienChiStr = edtSoTienChi.getText().toString().trim();
            String selectedPhong = (String) spinnerPhong.getSelectedItem();

            if (tenChiPhi.isEmpty() || soTienChiStr.isEmpty() || selectedPhong == null) {
                // Kiểm tra không được để trống
                edtTenChiPhi.setError("Vui lòng nhập tên chi phí!");
                edtSoTienChi.setError("Vui lòng nhập số tiền chi!");
                return;
            }

            int soTienChi = Integer.parseInt(soTienChiStr);
            int phongId = Integer.parseInt(selectedPhong.split(" - ID: ")[1]);

            // Tạo đối tượng ChiPhi
            ChiPhi chiPhi = new ChiPhi(
                    0,
                    phongId,
                    tenChiPhi,
                    soTienChi,
                    String.valueOf(System.currentTimeMillis()) // Lấy thời gian hiện tại
            );

            // Lưu vào cơ sở dữ liệu
            boolean isSuccess = thuChiDAO.addChiPhi(chiPhi);
            if (isSuccess) {
                chiPhiList.add(chiPhi);
                thuChiAdapter.notifyDataSetChanged();
                Toast.makeText(getContext(), "Thêm thành công!", Toast.LENGTH_SHORT).show();
                updateThuChi();
                alertDialog.dismiss();
            } else {
                Toast.makeText(getContext(), "Thêm chi phí thất bại!", Toast.LENGTH_SHORT).show();
            }
        });

        alertDialog.show();
    }

    public void updateThuChi() {
        ArrayList<ThanhToan> thanhToanList = thanhToanDAO.getThanhToanByChuTroAndTrangThaiIsTrue(userId);
        tinhTongThuChi(chiPhiList, thanhToanList);
    }

}