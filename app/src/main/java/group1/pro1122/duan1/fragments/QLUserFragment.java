package group1.pro1122.duan1.fragments;

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
import android.widget.ImageView;
import android.widget.Toast;
import java.util.ArrayList;
import group1.pro1122.duan1.R;
import group1.pro1122.duan1.adapters.UserAdapter;
import group1.pro1122.duan1.daos.UserDAO;
import group1.pro1122.duan1.models.User;

public class QLUserFragment extends Fragment {

    private RecyclerView recyclerView;
    private UserAdapter adapter;
    private UserDAO userDAO;
    private ArrayList<User> userList;
    private SearchView searchViewUser;
    private ImageView btnFilterUser;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_q_l_user, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.rcvQLUser);
        userDAO = new UserDAO(getContext());
        searchViewUser = view.findViewById(R.id.searchViewUser);
        btnFilterUser =  view.findViewById(R.id.btnFilterUser);

        // Lấy danh sách người dùng
        userList = userDAO.read();

        // Khởi tạo Adapter và thiết lập cho RecyclerView
        adapter = new UserAdapter(getContext(), userList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);

        // Xử lý sự kiện thay đổi trạng thái người dùng
        setupAdapterListeners();


        // Tìm kiếm người dùng theo tên
        searchViewUser.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchUserByName(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                searchUserByName(newText);
                return true;
            }
        });

        // Hiển thị dialog lọc khi bấm vào nút lọc
        btnFilterUser.setOnClickListener(v -> showFilterDialog());
    }

    // Hàm tìm kiếm người dùng theo tên
    private void searchUserByName(String query) {
        ArrayList<User> filteredList = new ArrayList<>();
        for (User user : userList) {
            if (user.getUsername().toLowerCase().contains(query.toLowerCase())) {
                filteredList.add(user);
            }
        }
        adapter.updateList(filteredList);
    }
    // Hàm hiển thị dialog lọc
    private void showFilterDialog() {
        // Tạo dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Lọc theo trạng thái");

        String[] statusOptions = {"Tất cả", "Hoạt động", "Khóa"};
        builder.setItems(statusOptions, (dialog, which) -> {
            switch (which) {
                case 0: // Tất cả
                    adapter.updateList(userList);
                    break;
                case 1: // Hoạt động
                    filterUsersByStatus(1);
                    break;
                case 2: // Khóa
                    filterUsersByStatus(0);
                    break;
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void filterUsersByStatus(int status) {
        ArrayList<User> filteredList = new ArrayList<>();
        for (User user : userList) {
            if (user.getTrangThai() == status) {
                filteredList.add(user);
            }
        }
        adapter.updateList(filteredList);
    }



    private void setupAdapterListeners() {
        adapter.setOnChangeStatusClickListener(user -> {
            int newStatus = (user.getTrangThai() == 1) ? 0 : 1; // Đổi trạng thái: 1 (hoạt động) <-> 0 (khóa)
            if (userDAO.updateTrangThai(user.getUserId(), newStatus)) {
                user.setTrangThai(newStatus); // Cập nhật trạng thái trong danh sách
                adapter.notifyDataSetChanged();
                Toast.makeText(getContext(), "Đã " + (newStatus == 1 ? "mở khóa" : "khóa") + " người dùng", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getContext(), "Lỗi khi thay đổi trạng thái", Toast.LENGTH_SHORT).show();
            }
        });
    }
}

