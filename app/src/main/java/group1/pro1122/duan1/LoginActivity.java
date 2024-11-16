package group1.pro1122.duan1;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;

import group1.pro1122.duan1.daos.UserDAO;
import group1.pro1122.duan1.models.User;

public class LoginActivity extends AppCompatActivity {

    TextInputLayout tilTaiKhoan, tilMatKhau;
    TextInputEditText edtTaiKhoan, edtMatKhau;
    CheckBox chkGhiNho;
    MaterialButton btnDangNhap, btnHuy;
    TextView tvQuenMatKhau, tvTaoTaiKhoan;
    String taiKhoan, matKhau;
    SharedPreferences pref;
    ArrayList<User> list;
    int vaiTro = -1;
    int userID = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login2);

        // Ánh xạ các phần tử
        tilTaiKhoan = findViewById(R.id.tilTaiKhoan);
        tilMatKhau = findViewById(R.id.tilMatKhau);
        edtTaiKhoan = findViewById(R.id.edtTaiKhoan);
        edtMatKhau = findViewById(R.id.edtMatKhau);
        btnDangNhap = findViewById(R.id.btnDangNhap);
        chkGhiNho = findViewById(R.id.chkGhiNho);
        tvQuenMatKhau = findViewById(R.id.tvQuenMatKhau);
        tvTaoTaiKhoan = findViewById(R.id.tvTaoTaiKhoan);

        // Truy vấn CSDL
        UserDAO userDAO = new UserDAO(LoginActivity.this);
        list = userDAO.read();

        // Kiểm tra trạng thái ghi nhớ tài khoản
        checkRemember();

        btnDangNhap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String taiKhoanEDT = edtTaiKhoan.getText().toString();
                String matKhauEDT = edtMatKhau.getText().toString();
                boolean ghiNho = chkGhiNho.isChecked(); // Kiểm tra nếu người dùng chọn ghi nhớ tài khoản
                boolean check = false;

                // Kiểm tra đăng nhập
                if(!taiKhoanEDT.equals("") && !matKhauEDT.equals("")){
                    for (User user : list) {
                        if(user.getUsername().equals(taiKhoanEDT) && user.getPassword().equals(matKhauEDT)){
                            check = true;
                            userID = user.getUserId();
                            vaiTro = user.getVaiTro();
                            break;
                        }
                    }

                    if(check){
                        Toast.makeText(LoginActivity.this, "Đăng nhập thành công!", Toast.LENGTH_SHORT).show();
                        remember(userID, taiKhoanEDT, matKhauEDT, vaiTro, ghiNho);
                        new android.os.Handler().postDelayed(() -> {
                            Intent i = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(i);
                            finish();
                        }, 3000);
                    } else{
                        Toast.makeText(LoginActivity.this, "Đăng nhập thất bại!", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(LoginActivity.this, "Tài khoản hoặc mật khẩu không chính xác!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Nút đăng ký
        tvTaoTaiKhoan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            }
        });

        // Nút quên mật khẩu (chưa xử lý)
        tvQuenMatKhau.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, ForgotPwdActivity.class));
                finish();
            }
        });

    }

    // Kiểm tra nếu có ghi nhớ tài khoản
    public void checkRemember(){
        pref = getSharedPreferences("user", MODE_PRIVATE);
        taiKhoan = pref.getString("tk", "");
        matKhau = pref.getString("mk", "");
        boolean chkGhiNho1 = pref.getBoolean("ghinho", false);
        chkGhiNho.setChecked(chkGhiNho1);

        // Nếu ghi nhớ tài khoản được chọn, điền thông tin vào các trường
        if(chkGhiNho.isChecked()){
            edtTaiKhoan.setText(taiKhoan);
            edtMatKhau.setText(matKhau);
        } else {
            // Nếu không ghi nhớ, khởi động lại ứng dụng với chế độ Guest
            edtTaiKhoan.setText(""); // Để trống tài khoản
            edtMatKhau.setText(""); // Để trống mật khẩu
        }
    }

    // Lưu thông tin đăng nhập vào SharedPreferences
    public void remember(int userID, String tk, String mk, int vaiTro, boolean chkGhiNho){
        pref = getSharedPreferences("user", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putInt("userID", userID);
        editor.putString("tk", tk);
        editor.putString("mk", mk);
        editor.putInt("vaiTro", vaiTro);
        editor.putBoolean("ghinho", chkGhiNho);
        editor.apply();
    }

}
