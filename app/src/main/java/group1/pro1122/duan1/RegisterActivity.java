package group1.pro1122.duan1;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import android.view.View;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import group1.pro1122.duan1.daos.UserDAO;
import group1.pro1122.duan1.models.User;

public class RegisterActivity extends AppCompatActivity {

    String TAG = "zzzzzzzzzzz";

    TextInputLayout tilTaiKhoan, tilMatKhau, tilXNMK, tilSDT;
    TextInputEditText edtTaiKhoan, edtMatKhau, edtXNMK, edtSDT;
    MaterialButton btnDangKy;
    Spinner spinnerVaiTro;

    int vaiTro = 0; // Mặc định là Người dùng
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);

        //Ánh xạ
        spinnerVaiTro = findViewById(R.id.spinnerVaiTro);
        tilTaiKhoan = findViewById(R.id.tilTaiKhoan);
        tilMatKhau = findViewById(R.id.tilMatKhau);
        tilXNMK = findViewById(R.id.tilXNMK);
        tilSDT = findViewById(R.id.tilSDT);
        edtTaiKhoan = findViewById(R.id.edtTaiKhoan);
        edtMatKhau = findViewById(R.id.edtMatKhau);
        edtXNMK = findViewById(R.id.edtXNMK);
        edtSDT = findViewById(R.id.edtSDT);
        btnDangKy = findViewById(R.id.btnDangKy);


        // Tạo mảng các vai trò
        String[] vaiTroList = {"Người dùng", "Chủ trọ"};

        // Tạo ArrayAdapter cho Spinner
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, vaiTroList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerVaiTro.setAdapter(adapter);

        // Thiết lập sự kiện chọn vai trò
        spinnerVaiTro.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                vaiTro = position;  // 0 là Người dùng, 1 là Chủ trọ
                Toast.makeText(RegisterActivity.this, "Vai trò: " + vaiTroList[position], Toast.LENGTH_SHORT).show();
                Log.d(TAG, "onItemSelected: Vai trò: "+vaiTro);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                vaiTro = 0;  // Mặc định là Người dùng
            }
        });

        //Nút đăng ký
        btnDangKy.setOnClickListener(v -> {
            registerUser();
        });
    }

    private void registerUser() {
        // Lấy dữ liệu từ các trường nhập
        String username = edtTaiKhoan.getText().toString().trim();
        String password = edtMatKhau.getText().toString().trim();
        String confirmPassword = edtXNMK.getText().toString().trim();
        String sdt = edtSDT.getText().toString().trim();
        // Gọi DAO để lưu dữ liệu
        UserDAO userDAO = new UserDAO(this);

        // Kiểm tra dữ liệu đầu vào
        // Reset lỗi
        tilTaiKhoan.setError(null);
        tilMatKhau.setError(null);
        tilXNMK.setError(null);
        tilSDT.setError(null);

        // Kiểm tra dữ liệu đầu vào
        boolean hasError = false;

        // Kiểm tra tên tài khoản ít nhất 3 kí tự
        if (username.isEmpty() || username.length() < 3) {
            tilTaiKhoan.setError("Tên tài khoản phải có ít nhất 3 kí tự");
            hasError = true;
        }

        // Kiểm tra mật khẩu ít nhất 5 kí tự
        if (password.isEmpty() || password.length() < 5) {
            tilMatKhau.setError("Mật khẩu phải có ít nhất 5 kí tự");
            hasError = true;
        }

        // Kiểm tra mật khẩu xác nhận khớp
        if (!password.equals(confirmPassword)) {
            tilXNMK.setError("Mật khẩu không khớp");
            hasError = true;
        }

        // Kiểm tra số điện thoại phải có số 0 ở đầu
        if (sdt.isEmpty() || !sdt.startsWith("0")) {
            tilSDT.setError("Số điện thoại phải bắt đầu bằng số 0");
            hasError = true;
        }

        if(sdt.length() != 10){
            tilSDT.setError("Số điện thoại phải có 10 số");
            hasError = true;
        }

        //Kiểm tra nếu tài khoản đã tồn tại
        if (userDAO.isUsernameExist(username)) {
            tilTaiKhoan.setError("Tên tài khoản đã tồn tại");
            hasError = true;
        }

        // Nếu có lỗi, dừng lại
        if (hasError) {
            return;
        }

        // Định dạng ngày
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        // Lấy ngày hiện tại
        String currentDate = dateFormat.format(Calendar.getInstance().getTime());

        // Khởi tạo đối tượng User
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        user.setVaiTro(vaiTro);
        user.setSdt(sdt);
        user.setTrangThai(1); // 1 là trạng thái hoạt động
        user.setNgayTao(currentDate); // Ngày tạo, bạn có thể lấy ngày hiện tại


        boolean isRegistered = userDAO.registerUser(user);

        if (isRegistered) {
            Toast.makeText(this, "Đăng ký thành công!", Toast.LENGTH_SHORT).show();
            // Đợi 3 giây rồi chuyển sang màn hình đăng nhập
            new android.os.Handler().postDelayed(() -> {
                // Chuyển sang màn hình đăng nhập
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
                finish(); // Đóng màn hình đăng ký
            }, 3000);
        } else {
            Toast.makeText(this, "Đăng ký thất bại. Vui lòng thử lại.", Toast.LENGTH_SHORT).show();
        }
    }
}