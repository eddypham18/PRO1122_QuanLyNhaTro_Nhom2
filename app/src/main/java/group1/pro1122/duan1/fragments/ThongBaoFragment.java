package group1.pro1122.duan1.fragments;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import group1.pro1122.duan1.MainActivity;
import group1.pro1122.duan1.R;
import group1.pro1122.duan1.adapters.ThongBaoAdapter;
import group1.pro1122.duan1.daos.ThongBaoDAO;
import group1.pro1122.duan1.models.ThongBao;


public class ThongBaoFragment extends Fragment {
    private RecyclerView recyclerView;
    private ThongBaoAdapter thongBaoAdapter;
    private ThongBaoDAO thongBaoDAO;
    TextView tvThongBao;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_thong_bao, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = view.findViewById(R.id.rcvThongBao);
        tvThongBao = view.findViewById(R.id.tvThongBao);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        //Lấy thông báo từ database
        SharedPreferences pref = getActivity().getSharedPreferences("user", getActivity().MODE_PRIVATE);
        int userID = pref.getInt("userID", 0);
        thongBaoDAO = new ThongBaoDAO(getContext());
        List<ThongBao> thongBaoList = thongBaoDAO.getAllThongBao(userID);

        // Kiểm tra danh sách thông báo
        if (thongBaoList == null || thongBaoList.isEmpty()) {
            tvThongBao.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        } else {
            tvThongBao.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);

            // Khởi tạo adapter
            thongBaoAdapter = new ThongBaoAdapter(getContext(), thongBaoList);
            recyclerView.setAdapter(thongBaoAdapter);
        }
    }

}