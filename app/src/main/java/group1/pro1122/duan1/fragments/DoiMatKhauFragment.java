package group1.pro1122.duan1.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.google.android.material.textfield.TextInputEditText;

import group1.pro1122.duan1.R;
import group1.pro1122.duan1.LoginActivity;
import group1.pro1122.duan1.daos.UserDAO;
import group1.pro1122.duan1.models.User;

public class DoiMatKhauFragment extends Fragment {

    private TextInputEditText edtCurrentPassword, edtNewPassword, edtConfirmPassword;
    private Button btnChangePassword;
    private UserDAO userDAO;
    private User currentUser;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_doi_mat_khau, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        edtCurrentPassword = view.findViewById(R.id.edtCurrentPassword);
        edtNewPassword = view.findViewById(R.id.edtNewPassword);
        edtConfirmPassword = view.findViewById(R.id.edtConfirmPassword);
        btnChangePassword = view.findViewById(R.id.btnChangePassword);

        userDAO = new UserDAO(getContext());

        // Lấy userID từ SharedPreferences
        int userID = getUserIDFromSharedPreferences();

        currentUser = userDAO.getUserByUserID(userID);

        btnChangePassword.setOnClickListener(v -> handleChangePassword());
    }

    private void handleChangePassword() {
        String currentPassword = edtCurrentPassword.getText().toString().trim();
        String newPassword = edtNewPassword.getText().toString().trim();
        String confirmPassword = edtConfirmPassword.getText().toString().trim();

        // Kiểm tra dữ liệu nhập vào
        if (TextUtils.isEmpty(currentPassword) || TextUtils.isEmpty(newPassword) || TextUtils.isEmpty(confirmPassword)) {
            Toast.makeText(getContext(), "Vui lòng nhập đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!currentPassword.equals(currentUser.getPassword())) {
            Toast.makeText(getContext(), "Mật khẩu hiện tại không đúng!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!newPassword.equals(confirmPassword)) {
            Toast.makeText(getContext(), "Mật khẩu mới và xác nhận không khớp!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (newPassword.length() < 6) {
            Toast.makeText(getContext(), "Mật khẩu mới phải có ít nhất 6 ký tự!", Toast.LENGTH_SHORT).show();
            return;
        }


        boolean isUpdated = userDAO.updatePassword(currentUser.getUsername(), newPassword);

        if (isUpdated) {
            showSuccessDialog();
        } else {
            showDialogThongBao("Thông báo", "Đổi mật khẩu thất bại! Vui lòng thử lại!");
        }
    }

    private void showSuccessDialog() {
        // Tạo dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_countdown, null);

        TextView tvCountdown = dialogView.findViewById(R.id.tvCountdown);
        builder.setView(dialogView);

        AlertDialog alertDialog = builder.create();
        alertDialog.show();

        new CountDownTimer(5000, 1000) {
            int secondsLeft = 5;

            @Override
            public void onTick(long millisUntilFinished) {
                tvCountdown.setText("Đổi mật khẩu thành công! Bạn sẽ được chuyển đến màn hình đăng nhập sau " + secondsLeft + " giây...");
                secondsLeft--;
            }

            @Override
            public void onFinish() {
                alertDialog.dismiss();
                navigateToLogin();
            }
        }.start();
    }

    private void navigateToLogin() {
        clearLoginInfo();
        Intent intent = new Intent(getContext(), LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        requireActivity().finish();
    }

    //Xóa thông tin đăng nhập trong SharedPreferences
    private void clearLoginInfo() {
        getContext().getSharedPreferences("user", getContext().MODE_PRIVATE)
                .edit()
                .clear()
                .apply();
    }

    public void showDialogThongBao(String title, String noiDung) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle(title)
                .setMessage(noiDung)
                .setPositiveButton("OK", (dialog, which) -> dialog.dismiss());
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private int getUserIDFromSharedPreferences() {
        return getContext().getSharedPreferences("user", getContext().MODE_PRIVATE).getInt("userID", -1);
    }
}
