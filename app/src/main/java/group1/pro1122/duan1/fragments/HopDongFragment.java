package group1.pro1122.duan1.fragments;

import android.app.AlertDialog;
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
import group1.pro1122.duan1.adapters.HopDongAdapter;
import group1.pro1122.duan1.adapters.HopDongNguoiThueAdapter;
import group1.pro1122.duan1.daos.HopDongDAO;
import group1.pro1122.duan1.daos.PhiDichVuDAO;
import group1.pro1122.duan1.daos.PhongDAO;
import group1.pro1122.duan1.daos.ThongBaoDAO;
import group1.pro1122.duan1.models.HopDong;

public class HopDongFragment extends Fragment {
    private RecyclerView rcvHopDong;
    private FloatingActionButton fab;
    private HopDongNguoiThueAdapter adapter;
    private ArrayList<HopDong> listHopDong;
    private HopDongDAO hopDongDAO;
    private TextView tvHopDongThongBao;
    private PhongDAO phongDAO;
    private PhiDichVuDAO phiDichVuDAO;
    private ThongBaoDAO thongBaoDAO;
    private SearchView searchView;
    private ImageView btnFilter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_hop_dong, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        rcvHopDong = view.findViewById(R.id.rcvHopDong);
        tvHopDongThongBao = view.findViewById(R.id.tvHopDongThongBao);
        phongDAO = new PhongDAO(getContext());
        phiDichVuDAO = new PhiDichVuDAO(getContext());
        rcvHopDong.setLayoutManager(new LinearLayoutManager(getActivity()));
        searchView = view.findViewById(R.id.searchView);
        btnFilter = view.findViewById(R.id.btnFilter);

        //Lấy user id
        SharedPreferences pref = getContext().getSharedPreferences("user", getContext().MODE_PRIVATE);
        int nguoiThue = pref.getInt("userID", -1);

        //Khởi tạo dữ liệu
        hopDongDAO = new HopDongDAO(getContext());
        listHopDong = hopDongDAO.readByIDNguoiThue(nguoiThue);

        //Cập nhật trạng của hợp đồng nếu hợp đồng hết hạn:
        hopDongDAO.capNhatTrangThaiHopDongHetHan();

        //Khởi tọa adapter
        if(listHopDong != null){
            adapter = new HopDongNguoiThueAdapter(getContext(), listHopDong);
            rcvHopDong.setAdapter(adapter);
            tvHopDongThongBao.setVisibility(View.GONE);
            rcvHopDong.setVisibility(View.VISIBLE);
        } else{
            rcvHopDong.setVisibility(View.GONE);
            tvHopDongThongBao.setVisibility(View.VISIBLE);
        }

        // Tìm kiếm theo ID hợp đồng
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchHopDongById(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                searchHopDongById(newText);
                return true;
            }
        });

        // Lọc theo trạng thái hợp đồng
        btnFilter.setOnClickListener(v -> showFilterDialog());
    }

    // Tìm kiếm theo ID hợp đồng
    private void searchHopDongById(String query) {
        ArrayList<HopDong> filteredList = new ArrayList<>();
        for (HopDong hopDong : listHopDong) {
            if (String.valueOf(hopDong.getHopDongId()).contains(query)) {
                filteredList.add(hopDong);
            }
        }
        updateRecyclerView(filteredList);
    }

    // Hiển thị dialog lọc trạng thái hợp đồng
    private void showFilterDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_filter_hopdong, null);
        builder.setView(dialogView);

        // Ánh xạ
        Spinner spinnerTrangThai = dialogView.findViewById(R.id.spinnerTrangThaiHopDong);
        Button btnApDung = dialogView.findViewById(R.id.btnApDung);

        AlertDialog alertDialog = builder.create();

        btnApDung.setOnClickListener(v -> {
            int trangThai = spinnerTrangThai.getSelectedItemPosition() - 1; // -1 là tất cả, 0 là đã kết thúc, 1 là đang hiệu lực, 2 là chưa ký kết
            filterHopDongByTrangThai(trangThai);
            alertDialog.dismiss();
        });

        alertDialog.show();
    }

    // Lọc danh sách hợp đồng theo trạng thái
    private void filterHopDongByTrangThai(int trangThai) {
        ArrayList<HopDong> filteredList = new ArrayList<>();
        for (HopDong hopDong : listHopDong) {
            if (trangThai == -1 || hopDong.getTrangThaiHopDong() == trangThai) {
                filteredList.add(hopDong);
            }
        }
        updateRecyclerView(filteredList);
    }

    // Cập nhật RecyclerView
    private void updateRecyclerView(ArrayList<HopDong> filteredList) {
        if (filteredList.isEmpty()) {
            rcvHopDong.setVisibility(View.GONE);
            tvHopDongThongBao.setVisibility(View.VISIBLE);
        } else {
            rcvHopDong.setVisibility(View.VISIBLE);
            tvHopDongThongBao.setVisibility(View.GONE);
        }
        adapter = new HopDongNguoiThueAdapter(getContext(), filteredList);
        rcvHopDong.setAdapter(adapter);
    }
}