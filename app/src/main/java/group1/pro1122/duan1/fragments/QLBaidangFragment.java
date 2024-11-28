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
import group1.pro1122.duan1.adapters.BaiDangAdapter;
import group1.pro1122.duan1.adapters.ThanhPhoSpinnerAdapter;
import group1.pro1122.duan1.daos.DiaDiemDAO;
import group1.pro1122.duan1.daos.PhongDAO;
import group1.pro1122.duan1.models.DiaDiem;
import group1.pro1122.duan1.models.Phong;

public class QLBaidangFragment extends Fragment {

    private RecyclerView recyclerView;
    private BaiDangAdapter adapter;
    private PhongDAO phongDAO;
    private ArrayList<Phong> listPhong;
    private ArrayList<DiaDiem> listThanhPho;
    private ArrayList<Phong> filteredList;
    private SearchView searchView;
    private ImageView btnFilter;
    private DiaDiemDAO diaDiemDAO;
    private ThanhPhoSpinnerAdapter diaDiemAdapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_q_l_baidang, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Khởi tạo các thành phần
        btnFilter = view.findViewById(R.id.btnFilterBD);
        searchView = view.findViewById(R.id.searchViewBD);
        recyclerView = view.findViewById(R.id.rcvQLBaiDang);
        phongDAO = new PhongDAO(getContext());
        diaDiemDAO = new DiaDiemDAO(getContext());

        // Lấy danh sách các bài đăng từ database
        listPhong = phongDAO.readAll();
        listThanhPho = diaDiemDAO.read();
        filteredList = new ArrayList<>(listPhong);

        // Kiểm tra nếu không có dữ liệu
        if (listPhong.isEmpty()) {
            Toast.makeText(getContext(), "Không có bài đăng nào.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Khởi tạo Adapter
        adapter = new BaiDangAdapter(getContext(), filteredList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);

        // Thiết lập tính năng tìm kiếm và lọc
        setupSearch();
        setupFilter();

        // Xử lý các sự kiện duyệt/hủy bài đăng từ Adapter
        setupAdapterListeners();
    }

    private void setupSearch() {
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
        filteredList.clear();
        for (Phong phong : listPhong) {
            if (phong.getSoPhong().toLowerCase().contains(query.toLowerCase())) {
                filteredList.add(phong);
            }
        }
        adapter.notifyDataSetChanged();
    }

    private void setupFilter() {
        btnFilter.setOnClickListener(v -> showFilterDialog());
    }

    private void showFilterDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_filter_phong, null);
        builder.setView(dialogView);

        Spinner spinnerTrangThaiPheDuyet = dialogView.findViewById(R.id.spinnerTrangThaiPheDuyet);
        Spinner spinnerTrangThaiThue = dialogView.findViewById(R.id.spinnerTrangThaiThue);
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
            int trangThaiPheDuyet = spinnerTrangThaiPheDuyet.getSelectedItemPosition();
            int trangThaiThue = spinnerTrangThaiThue.getSelectedItemPosition();
            int diaDiemID = listThanhPho.get(spinnerDiaDiem.getSelectedItemPosition()).getDiaDiemId();
            String giaThueMinStr = edtGiaThueMin.getText().toString().trim();
            String giaThueMaxStr = edtGiaThueMax.getText().toString().trim();
            String dienTichMinStr = edtDienTichMin.getText().toString().trim();
            String dienTichMaxStr = edtDienTichMax.getText().toString().trim();

            Integer giaThueMin = giaThueMinStr.isEmpty() ? null : Integer.parseInt(giaThueMinStr);
            Integer giaThueMax = giaThueMaxStr.isEmpty() ? null : Integer.parseInt(giaThueMaxStr);
            Integer dienTichMin = dienTichMinStr.isEmpty() ? null : Integer.parseInt(dienTichMinStr);
            Integer dienTichMax = dienTichMaxStr.isEmpty() ? null : Integer.parseInt(dienTichMaxStr);

            applyFilter(trangThaiPheDuyet, trangThaiThue, diaDiemID, giaThueMin, giaThueMax, dienTichMin, dienTichMax);
            alertDialog.dismiss();
        });

        alertDialog.show();
    }

    private void applyFilter(int trangThaiPheDuyet, int trangThaiThue, int diaDiemID,
                             Integer giaThueMin, Integer giaThueMax, Integer dienTichMin, Integer dienTichMax) {
        filteredList.clear();
        for (Phong phong : listPhong) {
            if ((trangThaiPheDuyet == 1 && phong.getTrangThaiPheduyet() != 1) ||
                    (trangThaiPheDuyet == 2 && phong.getTrangThaiPheduyet() != 0)) {
                continue;
            }
            if ((trangThaiThue == 1 && phong.getTrangThai() != 1) ||
                    (trangThaiThue == 2 && phong.getTrangThai() != 0)) {
                continue;
            }
            if (diaDiemID != 0 && phong.getDiaDiemID() != diaDiemID) continue;
            if (giaThueMin != null && phong.getGiaThue() < giaThueMin) continue;
            if (giaThueMax != null && phong.getGiaThue() > giaThueMax) continue;
            if (dienTichMin != null && phong.getDienTich() < dienTichMin) continue;
            if (dienTichMax != null && phong.getDienTich() > dienTichMax) continue;

            filteredList.add(phong);
        }
        adapter.notifyDataSetChanged();
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

    private void setupAdapterListeners() {
        adapter.setOnApproveClickListener(phong -> {
            phong.setTrangThaiPheduyet(1);
            SharedPreferences pref = getContext().getSharedPreferences("user", Context.MODE_PRIVATE);
            int pheDuyetBoi = pref.getInt("userID", -1);
            if (phongDAO.updateTrangThaiPheDuyet(phong.getPhongId(), pheDuyetBoi)) {
                Toast.makeText(getContext(), "Bài đăng đã được duyệt", Toast.LENGTH_SHORT).show();
                adapter.notifyDataSetChanged();
            } else {
                Toast.makeText(getContext(), "Lỗi khi duyệt bài đăng", Toast.LENGTH_SHORT).show();
            }
        });

        adapter.setOnDeleteClickListener(phong -> {
            if (phong.getTrangThai() == 1) {
                Toast.makeText(getContext(), "Không thể hủy vì phòng đang được thuê", Toast.LENGTH_SHORT).show();
            } else {
                SharedPreferences pref = getContext().getSharedPreferences("user", Context.MODE_PRIVATE);
                int pheDuyetBoi = pref.getInt("userID", -1);
                if (phongDAO.huyDuyet(phong.getPhongId(), pheDuyetBoi)) {
                    phong.setTrangThaiPheduyet(0);
                    Toast.makeText(getContext(), "Bài đăng đã bị hủy", Toast.LENGTH_SHORT).show();
                    adapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(getContext(), "Lỗi khi hủy bài đăng", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
