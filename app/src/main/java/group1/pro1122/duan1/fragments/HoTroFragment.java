package group1.pro1122.duan1.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

import group1.pro1122.duan1.R;
import group1.pro1122.duan1.daos.HoTroDAO;
import group1.pro1122.duan1.models.HoTro;


public class HoTroFragment extends Fragment {

    private TextInputEditText edtTieuDe, edtNoiDung;
    private Button btnGui;
    private HoTroDAO hoTroDAO;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_ho_tro, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Ánh xạ các thành phần
        edtTieuDe = view.findViewById(R.id.edtTieuDe);
        edtNoiDung = view.findViewById(R.id.edtNoiDung);
        btnGui = view.findViewById(R.id.btnGui);

        hoTroDAO = new HoTroDAO(getContext());

        // Xử lý sự kiện nút gửi
        btnGui.setOnClickListener(v -> handleSendRequest());
    }

    private void handleSendRequest() {
        String tieuDe = edtTieuDe.getText().toString().trim();
        String noiDung = edtNoiDung.getText().toString().trim();

        if (tieuDe.isEmpty()) {
            edtTieuDe.setError("Vui lòng nhập tiêu đề!");
            return;
        }
        if (noiDung.isEmpty()) {
            edtNoiDung.setError("Vui lòng nhập nội dung!");
            return;
        }

        // Lấy thông tin userId từ SharedPreferences
        SharedPreferences pref = getContext().getSharedPreferences("user", Context.MODE_PRIVATE);
        int userId = pref.getInt("userID", -1);

        if (userId == -1) {
            Toast.makeText(getContext(), "Không tìm thấy thông tin người dùng!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Tạo đối tượng HoTro
        String ngayTao = String.valueOf(System.currentTimeMillis());
        HoTro hoTro = new HoTro(0, userId, tieuDe, noiDung, 0, ngayTao, null);

        // Lưu vào cơ sở dữ liệu
        if (hoTroDAO.addHoTroRequest(hoTro)) {
            showDialogThongBao("Thông báo","Đã gửi yêu cầu đến admin hệ thống. Vui lòng đợi phản hồi.");
            edtTieuDe.setText("");
            edtNoiDung.setText("");
        } else {
            showDialogThongBao("Lỗi", "Gửi yêu cầu thất bại! Vui lòng thử lại.");
        }
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