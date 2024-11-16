package group1.pro1122.duan1;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;

import group1.pro1122.duan1.daos.UserDAO;
import group1.pro1122.duan1.models.User;

public class ForgotPwdActivity extends AppCompatActivity {
    TextInputEditText edtTaiKhoan, edtSDT;
    MaterialButton btnGuiYC;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_forgot_pwd);

        //Ánh xạ
        edtTaiKhoan = findViewById(R.id.edtTaiKhoan);
        edtSDT = findViewById(R.id.edtSDT);
        btnGuiYC = findViewById(R.id.btnGuiYC);

        //Xử lý sự kiện
        btnGuiYC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String taiKhoan = edtTaiKhoan.getText().toString();
                String sdt = edtSDT.getText().toString();

                //Kiểm tra dữ liệu
                if (taiKhoan.isEmpty() || sdt.isEmpty()) {
                    Toast.makeText(ForgotPwdActivity.this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                    return;
                }

                //Kiểm tra tài khoản và số điện thoại
                UserDAO userDAO = new UserDAO(ForgotPwdActivity.this);
                ArrayList<User> list = userDAO.read();
                boolean flag = false;
                for (User user : list) {
                    if (user.getUsername().equals(taiKhoan) && user.getSdt().equals(sdt)) {
                        flag = true;
                        break;
                    }
                }

                if(flag) {
                    Toast.makeText(ForgotPwdActivity.this, "Yêu cầu của bạn đã được gửi. Vui lòng kiểm tra tin nhắn!", Toast.LENGTH_SHORT).show();
                    // Đợi 3 giây
                    new android.os.Handler().postDelayed(() -> {
                        Intent intent = new Intent(ForgotPwdActivity.this, ResetPwdActivity.class);
                        intent.putExtra("username", taiKhoan);
                        startActivity(intent);
                        finish();
                    }, 5000);
                } else {
                    Toast.makeText(ForgotPwdActivity.this, "Tài khoản hoặc số điện thoại không đúng", Toast.LENGTH_SHORT).show();
                    return;
                }

                //Gửi yêu cầu
            }
        });
    }
}