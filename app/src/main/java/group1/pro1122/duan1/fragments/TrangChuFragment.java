package group1.pro1122.duan1.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.Image;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
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
import android.widget.Toast;

import java.util.ArrayList;

import group1.pro1122.duan1.R;
import group1.pro1122.duan1.adapters.ThanhPhoSpinnerAdapter;
import group1.pro1122.duan1.adapters.TrangChuAdapter;
import group1.pro1122.duan1.daos.DiaDiemDAO;
import group1.pro1122.duan1.daos.PhongDAO;
import group1.pro1122.duan1.daos.ThongBaoDAO;
import group1.pro1122.duan1.daos.UserDAO;
import group1.pro1122.duan1.models.DiaDiem;
import group1.pro1122.duan1.models.Phong;
import group1.pro1122.duan1.models.User;


public class TrangChuFragment extends Fragment {

    RecyclerView rcvTrangChu;
    TrangChuAdapter trangChuAdapter;
    PhongDAO phongDAO;
    DiaDiemDAO diaDiemDAO;
    ArrayList<Phong> listPhong;
    ArrayList<DiaDiem> listThanhPho;
    ArrayList<Phong> filteredList;
    SearchView searchViewTrangChu;
    ImageView btnFilterTrangChu;
    ThanhPhoSpinnerAdapter diaDiemAdapter;
    UserDAO userDAO;

    int vaiTro;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_trang_chu, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //Ánh xạ và khai báo
        rcvTrangChu = view.findViewById(R.id.rcvTrangChu);
        phongDAO = new PhongDAO(getContext());
        diaDiemDAO = new DiaDiemDAO(getContext());
        searchViewTrangChu = view.findViewById(R.id.searchViewTrangChu);
        btnFilterTrangChu = view.findViewById(R.id.btnFilterTrangChu);

        //Lấy danh sách các bài đăng từ database
        listPhong = phongDAO.getPhongDaDuyetVaTrong();
        listThanhPho = diaDiemDAO.read();
        filteredList = new ArrayList<>(listPhong);

        // Kiểm tra nếu không có dữ liệu
        if (listPhong.isEmpty()) {
            Toast.makeText(getContext(), "Không có bài đăng nào.", Toast.LENGTH_SHORT).show();
            return;
        }

        //Khởi tạo adapter
        trangChuAdapter = new TrangChuAdapter(getContext(), filteredList);
        rcvTrangChu.setLayoutManager(new LinearLayoutManager(getContext()));
        rcvTrangChu.setAdapter(trangChuAdapter);

        // Thiết lập tính năng tìm kiếm và lọc
        setupSearch();
        setupFilter();

        onClickThuePhong();
    }

    private void setupSearch() {
        searchViewTrangChu.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
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
        filteredList.clear();
        for (Phong phong : listPhong) {
            if (phong.getSoPhong().toLowerCase().contains(query.toLowerCase())) {
                filteredList.add(phong);
            }
        }
        trangChuAdapter.notifyDataSetChanged();
    }

    private void setupFilter() {
        btnFilterTrangChu.setOnClickListener(v -> showFilterDialog());
    }

    private void showFilterDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_filter_phong_trangchu, null);
        builder.setView(dialogView);

        Spinner spinnerDiaDiem = dialogView.findViewById(R.id.spinnerDiaDiem);
        EditText edtGiaThueMin = dialogView.findViewById(R.id.edtGiaThueMin);
        EditText edtGiaThueMax = dialogView.findViewById(R.id.edtGiaThueMax);
        EditText edtDienTichMin = dialogView.findViewById(R.id.edtDienTichMin);
        EditText edtDienTichMax = dialogView.findViewById(R.id.edtDienTichMax);
        Button btnApDung = dialogView.findViewById(R.id.btnApDung);

        // Thêm mục tất cả vào danh sách địa điểm
        addTatCaOptionToDiaDiemList();
        diaDiemAdapter = new ThanhPhoSpinnerAdapter(getContext(), listThanhPho);
        spinnerDiaDiem.setAdapter(diaDiemAdapter);

        AlertDialog alertDialog = builder.create();
        btnApDung.setOnClickListener(v -> {
            int diaDiemID = listThanhPho.get(spinnerDiaDiem.getSelectedItemPosition()).getDiaDiemId();
            String giaThueMinStr = edtGiaThueMin.getText().toString().trim();
            String giaThueMaxStr = edtGiaThueMax.getText().toString().trim();
            String dienTichMinStr = edtDienTichMin.getText().toString().trim();
            String dienTichMaxStr = edtDienTichMax.getText().toString().trim();

            Integer giaThueMin = giaThueMinStr.isEmpty() ? null : Integer.parseInt(giaThueMinStr);
            Integer giaThueMax = giaThueMaxStr.isEmpty() ? null : Integer.parseInt(giaThueMaxStr);
            Integer dienTichMin = dienTichMinStr.isEmpty() ? null : Integer.parseInt(dienTichMinStr);
            Integer dienTichMax = dienTichMaxStr.isEmpty() ? null : Integer.parseInt(dienTichMaxStr);

            applyFilter(diaDiemID, giaThueMin, giaThueMax, dienTichMin, dienTichMax);
            alertDialog.dismiss();
        });

        alertDialog.show();
    }

    private void applyFilter(int diaDiemID,
                             Integer giaThueMin, Integer giaThueMax, Integer dienTichMin, Integer dienTichMax) {
        filteredList.clear();
        for (Phong phong : listPhong) {
            if (diaDiemID != 0 && phong.getDiaDiemID() != diaDiemID) continue;
            if (giaThueMin != null && phong.getGiaThue() < giaThueMin) continue;
            if (giaThueMax != null && phong.getGiaThue() > giaThueMax) continue;
            if (dienTichMin != null && phong.getDienTich() < dienTichMin) continue;
            if (dienTichMax != null && phong.getDienTich() > dienTichMax) continue;

            filteredList.add(phong);
        }
        trangChuAdapter.notifyDataSetChanged();
    }

    private void addTatCaOptionToDiaDiemList() {
        boolean hasTatCaOption = false;
        for (DiaDiem diaDiem : listThanhPho) {
            if (diaDiem.getDiaDiemId() == 0) {
                hasTatCaOption = true;
                break;
            }
        }
        if (!hasTatCaOption) {
            DiaDiem diaDiemTatCa = new DiaDiem(0, "Tất cả");
            listThanhPho.add(0, diaDiemTatCa);
        }
    }

    //Chức năng thuê phòng
    public void onClickThuePhong(){
        trangChuAdapter.setOnClickListener(phong -> {
            // Lấy User_ID từ SharedPreferences
            SharedPreferences pref = getContext().getSharedPreferences("user", Context.MODE_PRIVATE);
            vaiTro = pref.getInt("vaiTro", -1);
            if(vaiTro == 0){
                //Xử lý chức năng thuê phòng
                userDAO = new UserDAO(getContext());
                User user = userDAO.getUserByUserID(pref.getInt("userID", -1));
                if(user.getHoTen() != null && user.getNgaySinh() != null && user.getEmail() != null && user.getSdt() != null&& user.getCccd() != null){
                    // Gửi thông báo
                    ThongBaoDAO thongBaoDAO = new ThongBaoDAO(getContext());
                    int chuSoHuu = phong.getChuSoHuu();
                    int hopDongID = 0;  // Chưa có hợp đồng
                    int loaiThongBao = 0;  // Loại thông báo: 0 - Yêu cầu thuê phòng
                    String noiDung = "Người dùng ID số "+user.getUserId()+", Họ tên: " + user.getHoTen()+", SĐT: "+user.getSdt() + " đã gửi yêu cầu thuê phòng " + phong.getSoPhong()+", Phòng ID: "  + phong.getPhongId();
                    boolean check = thongBaoDAO.sendThongBao(chuSoHuu, hopDongID, loaiThongBao, noiDung);
                    if(check){
                        showDialogThongBao("Thông báo", "Đã gửi yêu cầu thuê phòng thành công!");
                    } else{
                        showDialogThongBao("Lỗi", "Gửi yêu cầu thuê phòng thất bại!");
                    }
                } else {
                    showDialogThongBao("Thông báo", "Vui lòng nhập đầy đủ thông tin cá nhân trước khi thuê phòng!");
                }
            } else if (vaiTro == 1 || vaiTro == 2) {
                showDialogThongBao("Thông báo", "Chỉ người dùng mới có thể thuê phòng!");
            } else {
                showDialogThongBao("Thông báo", "Vui lòng đăng nhập để có thể thuê phòng!");
            }
        });
    }

    public void showDialogThongBao(String title, String noiDung){
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle(title)
                .setMessage(noiDung)
                .setPositiveButton("OK", (dialog, which) -> dialog.dismiss());
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}