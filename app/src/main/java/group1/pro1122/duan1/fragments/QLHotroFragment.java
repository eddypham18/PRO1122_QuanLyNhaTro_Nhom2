package group1.pro1122.duan1.fragments;

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
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;

import group1.pro1122.duan1.R;
import group1.pro1122.duan1.adapters.HoTroAdapter;
import group1.pro1122.duan1.daos.HoTroDAO;
import group1.pro1122.duan1.models.HoTro;


public class QLHotroFragment extends Fragment {
    private RecyclerView recyclerView;
    private HoTroAdapter adapter;
    private HoTroDAO hoTroDAO;
    private ArrayList<HoTro> hoTroList;
    private ArrayList<HoTro> filteredList;
    private Spinner spinnerTrangThai;
    private SearchView searchView;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_q_l_hotro, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.recyclerView);
        spinnerTrangThai = view.findViewById(R.id.spinnerTrangThai);
        searchView = view.findViewById(R.id.searchView);
        hoTroDAO = new HoTroDAO(getContext());

        hoTroList = hoTroDAO.getAllHoTroRequests();
        filteredList = new ArrayList<>(hoTroList);

        adapter = new HoTroAdapter(getContext(), filteredList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);

        setupSpinnerFilter();
        setupSearchFilter();
    }

    private void setupSpinnerFilter() {
        spinnerTrangThai.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                applyFilters();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }

    private void setupSearchFilter() {
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                applyFilters();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                applyFilters();
                return true;
            }
        });
    }

    private void applyFilters() {
        filteredList.clear();
        int trangThai = spinnerTrangThai.getSelectedItemPosition(); // 0: All, 1: Đã xử lý, 2: Chưa xử lý
        String searchText = searchView.getQuery().toString().toLowerCase();

        for (HoTro hoTro : hoTroList) {
            boolean matchesStatus = (trangThai == 0) ||
                    (trangThai == 1 && hoTro.getTrangThai() == 1) ||
                    (trangThai == 2 && hoTro.getTrangThai() == 0);
            boolean matchesSearch = hoTro.getTieuDe().toLowerCase().contains(searchText) ||
                    hoTro.getNoiDung().toLowerCase().contains(searchText);

            if (matchesStatus && matchesSearch) {
                filteredList.add(hoTro);
            }
        }
        adapter.notifyDataSetChanged();
    }
}