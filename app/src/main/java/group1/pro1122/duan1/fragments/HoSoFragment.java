package group1.pro1122.duan1.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import com.google.android.material.button.MaterialButton;

import group1.pro1122.duan1.R;
import group1.pro1122.duan1.daos.UserDAO;
import group1.pro1122.duan1.models.User;

public class HoSoFragment extends Fragment {

    private TextView tvUserID, tvVaiTro, tvHoTen, tvNgaySinh, tvEmail, tvSDT, tvCCCD;
    private MaterialButton btnCapNhat;
    private UserDAO userDAO;
    private User user;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ho_so, container, false);

        //Ánh xạ
        tvUserID = view.findViewById(R.id.tvUserID);
        tvVaiTro = view.findViewById(R.id.tvVaiTro);
        tvHoTen = view.findViewById(R.id.tvHoTen);
        tvNgaySinh = view.findViewById(R.id.tvNgaySinh);
        tvEmail = view.findViewById(R.id.tvEmail);
        tvSDT = view.findViewById(R.id.tvSDT);
        tvCCCD = view.findViewById(R.id.tvCCCD);
        btnCapNhat = view.findViewById(R.id.btnCapNhat);

        //Lấy thông tin user
        SharedPreferences pref = getContext().getSharedPreferences("user", Context.MODE_PRIVATE);
        int userID = pref.getInt("userID", -1);
        userDAO = new UserDAO(getContext());
        user = userDAO.getUserByUserID(userID);

        //Hiển thị thông tin user
        if(user == null){
            tvUserID.setText("ID: Chưa xác định");
            tvVaiTro.setText("Vai trò: Chưa xác định");
            tvHoTen.setText("Họ tên: Chưa xác định");
            tvNgaySinh.setText("Ngày sinh: Chưa xác định");
            tvEmail.setText("Email: Chưa xác định");
            tvSDT.setText("Số điện thoại: Chưa xác định");
            tvCCCD.setText("CCCD: Chưa xác định");
            btnCapNhat.setOnClickListener(v -> {
                Toast.makeText(getContext(), "Vui lòng đăng nhập để cập nhật thông tin", Toast.LENGTH_SHORT).show();
            });
        } else {
            tvUserID.setText("ID: " + user.getUserId());
            if(user.getVaiTro() == 0){
                tvVaiTro.setText("Vai trò: Người dùng");
            } else if (user.getVaiTro() == 1) {
                tvVaiTro.setText("Vai trò: Chủ trọ");
            } else {
                tvVaiTro.setText("Vai trò: Admin");
            }
            tvHoTen.setText("Họ tên: " + user.getHoTen());
            tvNgaySinh.setText("Ngày sinh: " + user.getNgaySinh());
            tvEmail.setText("Email: " + user.getEmail());
            tvSDT.setText("Số điện thoại: " + user.getSdt());
            tvCCCD.setText("CCCD: " + user.getCccd());

            //Nút cập nhật
            btnCapNhat.setOnClickListener(v -> showDialogUpdate());
        }

        return view;
    }

    public void showDialogUpdate() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_update_user, null);
        builder.setView(view);

        EditText edtHoTen = view.findViewById(R.id.edtHoTen);
        EditText edtNgaySinh = view.findViewById(R.id.edtNgaySinh);
        EditText edtEmail = view.findViewById(R.id.edtEmail);
        EditText edtSDT = view.findViewById(R.id.edtSDT);
        EditText edtCCCD = view.findViewById(R.id.edtCCCD);
        MaterialButton btnUpdate = view.findViewById(R.id.btnUpdate);

        AlertDialog alertDialog = builder.create();

        // Đặt các dữ liệu vào trường nhập
        edtHoTen.setText(user.getHoTen());
        edtNgaySinh.setText(user.getNgaySinh());
        edtEmail.setText(user.getEmail());
        edtSDT.setText(user.getSdt());
        edtCCCD.setText(user.getCccd());

        btnUpdate.setOnClickListener(v -> {
            String hoTen = edtHoTen.getText().toString().trim();
            String ngaySinh = edtNgaySinh.getText().toString().trim();
            String email = edtEmail.getText().toString().trim();
            String sdt = edtSDT.getText().toString().trim();
            String cccd = edtCCCD.getText().toString().trim();

            // Regex cho email và ngày sinh
            String emailRegex = "^[\\w\\.-]+@[\\w\\.-]+\\.\\w{2,}$";
            String ngaySinhRegex = "^\\d{2}[/-]\\d{2}[/-]\\d{4}$";

            // Kiểm tra dữ liệu hợp lệ
            if (hoTen.isEmpty() || ngaySinh.isEmpty() || email.isEmpty() || sdt.isEmpty() || cccd.isEmpty()) {
                Toast.makeText(getContext(), "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
            } else if (!email.matches(emailRegex)) {
                Toast.makeText(getContext(), "Email không hợp lệ", Toast.LENGTH_SHORT).show();
            } else if (!ngaySinh.matches(ngaySinhRegex)) {
                Toast.makeText(getContext(), "Ngày sinh không hợp lệ. Định dạng dd/MM/yyyy hoặc dd-MM-yyyy", Toast.LENGTH_SHORT).show();
            } else if (sdt.length() != 10) {
                Toast.makeText(getContext(), "Số điện thoại không hợp lệ", Toast.LENGTH_SHORT).show();
            } else if (cccd.length() != 12) {
                Toast.makeText(getContext(), "CCCD không hợp lệ", Toast.LENGTH_SHORT).show();
            } else {
                user.setHoTen(hoTen);
                user.setNgaySinh(ngaySinh);
                user.setEmail(email);
                user.setSdt(sdt);
                user.setCccd(cccd);

                if (userDAO.updateUserInfo(user)) {
                    Toast.makeText(getContext(), "Cập nhật thông tin thành công", Toast.LENGTH_SHORT).show();
                    tvUserID.setText("ID: " + user.getUserId());
                    if(user.getVaiTro() == 0){
                        tvVaiTro.setText("Vai trò: Người dùng");
                    } else if (user.getVaiTro() == 1) {
                        tvVaiTro.setText("Vai trò: Chủ trọ");
                    } else {
                        tvVaiTro.setText("Vai trò: Admin");
                    }
                    tvHoTen.setText("Họ tên: " + user.getHoTen());
                    tvNgaySinh.setText("Ngày sinh: " + user.getNgaySinh());
                    tvEmail.setText("Email: " + user.getEmail());
                    tvSDT.setText("Số điện thoại: " + user.getSdt());
                    tvCCCD.setText("CCCD: " + user.getCccd());
                    alertDialog.dismiss();
                } else {
                    Toast.makeText(getContext(), "Cập nhật thông tin thất bại", Toast.LENGTH_SHORT).show();
                }
            }
        });
        alertDialog.show();
    }

}
