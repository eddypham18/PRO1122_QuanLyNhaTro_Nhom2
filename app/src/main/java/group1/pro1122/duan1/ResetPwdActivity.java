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
import com.google.android.material.textfield.TextInputLayout;

import group1.pro1122.duan1.daos.UserDAO;

public class ResetPwdActivity extends AppCompatActivity {
    TextInputLayout tilPIN, tilMatKhau, tilXNMK;
    TextInputEditText edtPIN, edtMatKhau, edtXNMK;
    MaterialButton btnDoiMK;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_reset_pwd);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        //Ánh xạ
        tilPIN = findViewById(R.id.tilPIN);
        tilMatKhau = findViewById(R.id.tilMatKhau);
        tilXNMK = findViewById(R.id.tilXNMK);
        edtPIN = findViewById(R.id.edtPIN);
        edtMatKhau = findViewById(R.id.edtMatKhau);
        edtXNMK = findViewById(R.id.edtXNMK);
        btnDoiMK = findViewById(R.id.btnDoiMK);

        btnDoiMK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String pin = edtPIN.getText().toString();
                String matKhau = edtMatKhau.getText().toString();
                String xacNhanMK = edtXNMK.getText().toString();
                boolean hasError = false;

                tilPIN.setError(null);
                tilMatKhau.setError(null);
                tilXNMK.setError(null);

                //Kiểm tra dữ liệu
                if (pin.isEmpty() || matKhau.isEmpty() || xacNhanMK.isEmpty()) {
                    tilPIN.setError("Vui lòng nhập mã PIN");
                    tilMatKhau.setError("Vui lòng nhập mật khẩu");
                    tilXNMK.setError("Vui lòng xác nhận mật khẩu");
                    hasError = true;
                }


                // Kiểm tra mật khẩu ít nhất 5 kí tự
                if (matKhau.isEmpty() || matKhau.length() < 5) {
                    tilMatKhau.setError("Mật khẩu phải có ít nhất 5 kí tự");
                    hasError = true;
                }

                //Kiểm tra mật khẩu
                if (!matKhau.equals(xacNhanMK)) {
                    tilXNMK.setError("Mật khẩu không khớp");
                    hasError = true;
                }

                if(!pin.equals("1111")){
                    tilPIN.setError("Mã PIN không đúng");
                    hasError = true;
                }

                if(hasError){
                   return;
                }

                //Xử lý đổi mật khẩu
                Intent i = getIntent();
                String username = i.getStringExtra("username");
                //Gọi DAO để đổi mật khẩu
                UserDAO userDAO = new UserDAO(ResetPwdActivity.this);
                boolean check = userDAO.updatePassword(username, matKhau);

                if(check) {
                    Toast.makeText(ResetPwdActivity.this, "Đổi mật khẩu thành công!", Toast.LENGTH_SHORT).show();
                    new android.os.Handler().postDelayed(() -> {
                        Intent intent = new Intent(ResetPwdActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    }, 3000);
                }
            }
        });


    }
}