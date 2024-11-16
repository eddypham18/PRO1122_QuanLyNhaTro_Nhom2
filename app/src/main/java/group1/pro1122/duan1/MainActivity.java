package group1.pro1122.duan1;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import androidx.fragment.app.FragmentManager;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;

import group1.pro1122.duan1.fragments.DoiMatKhauFragment;
import group1.pro1122.duan1.fragments.HoSoFragment;
import group1.pro1122.duan1.fragments.HoTroFragment;
import group1.pro1122.duan1.fragments.QLBaidangFragment;
import group1.pro1122.duan1.fragments.QLHopDongFragment;
import group1.pro1122.duan1.fragments.QLHotroFragment;
import group1.pro1122.duan1.fragments.QLThanhtoanFragment;
import group1.pro1122.duan1.fragments.QLThuchiFragment;
import group1.pro1122.duan1.fragments.QLUserFragment;
import group1.pro1122.duan1.fragments.QuanLyPhongFragment;
import group1.pro1122.duan1.fragments.ThongBaoFragment;
import group1.pro1122.duan1.fragments.TimKiemFragment;
import group1.pro1122.duan1.fragments.TrangChuFragment;


public class MainActivity extends AppCompatActivity {
    String TAG = "zzzzzzzzzzzzzz";
    private DrawerLayout drawer;
    private Toolbar toolBar;
    private View headerView;
    private NavigationView nvView;
    private TextView txtUser;
    FragmentManager fragmentManager;
    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Ánh xạ
        drawer = findViewById(R.id.drawer_layout);
        toolBar = findViewById(R.id.toolBar);
        nvView = findViewById(R.id.nvView);
        bottomNavigationView = findViewById(R.id.bottom_navigation);

        //Set toolbar
        toolBar.setTitleTextColor(getResources().getColor(android.R.color.white));
        setSupportActionBar(toolBar);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.baseline_menu_24);
        getSupportActionBar().setTitle("Trang chủ");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Đọc thông tin người dùng từ SharedPreferences
        SharedPreferences pref = getSharedPreferences("user", MODE_PRIVATE);
        boolean ghiNho = pref.getBoolean("ghinho", false);
        String username = pref.getString("tk", "Guest");
        int vaiTro = pref.getInt("vaiTro", -1);  // Mặc định là -1 nếu không có vai trò

        //Hiển thị tên người dùng
        headerView = nvView.getHeaderView(0);
        txtUser = headerView.findViewById(R.id.txtUser);
        txtUser.setText("Xin chào " + username + "!");

        //Hiện thị chức năng theo vai trò của người dùng
        // 0: người dùng, người thuê
        // 1: Chủ trọ, người cho thuê phòng
        // 2: Admin hệ thống
        if(vaiTro == 2){
            nvView.getMenu().findItem(R.id.nav_Admin).setVisible(true);
            nvView.getMenu().findItem(R.id.nav_login_prompt).setVisible(false);
            nvView.getMenu().findItem(R.id.nav_login).setVisible(false);
            nvView.getMenu().findItem(R.id.nav_User).setVisible(true);
            nvView.getMenu().findItem(R.id.nav_HoTro).setVisible(false);
            nvView.getMenu().findItem(R.id.nav_DoiMK).setVisible(false);
        } else if(vaiTro == 1){
            nvView.getMenu().findItem(R.id.nav_QL).setVisible(true);
            nvView.getMenu().findItem(R.id.nav_User).setVisible(true);
            nvView.getMenu().findItem(R.id.nav_login_prompt).setVisible(false);
            nvView.getMenu().findItem(R.id.nav_login).setVisible(false);
        } else if(vaiTro == 0){
            nvView.getMenu().findItem(R.id.nav_User).setVisible(true);
            nvView.getMenu().findItem(R.id.nav_login_prompt).setVisible(false);
            nvView.getMenu().findItem(R.id.nav_login).setVisible(false);
        } else{
            nvView.getMenu().findItem(R.id.nav_login_prompt).setVisible(true);
            nvView.getMenu().findItem(R.id.nav_login).setVisible(true);
        }

        //Nếu người dùng không chọn ghi nhớ, đặt tài khoản về Guest
        if (!ghiNho) {
            SharedPreferences.Editor editor = pref.edit();
            editor.putString("tk", "Guest");
            editor.putInt("vaiTro", -1);
            editor.apply();
        }


        //Navigation drawer
        fragmentManager = getSupportFragmentManager();
        //Home screen
        nvView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if(item.getItemId() == R.id.nav_QLPhong){
                    setTitleAndFragment("Quản lý phòng trọ", new QuanLyPhongFragment());
                } else if(item.getItemId() == R.id.nav_QLHopDong){
                    setTitleAndFragment("Quản lý hợp đồng", new QLHopDongFragment());
                } else if(item.getItemId() == R.id.nav_QLThanhToan){
                    setTitleAndFragment("Quản lý thanh toán", new QLThanhtoanFragment());
                } else if(item.getItemId() == R.id.nav_QLThuChi){
                    setTitleAndFragment("Quản lý thu chi", new QLThuchiFragment());
                } else if(item.getItemId() == R.id.nav_HoTro){
                    setTitleAndFragment("Hỗ trợ", new HoTroFragment());
                } else if (item.getItemId() == R.id.nav_DoiMK) {
                    setTitleAndFragment("Đổi mật khẩu", new DoiMatKhauFragment());
                } else if (item.getItemId() == R.id.nav_QLUser) {
                    setTitleAndFragment("Quản lý người dùng", new QLUserFragment());
                } else if(item.getItemId() == R.id.nav_QLBaidang){
                    setTitleAndFragment("Quản lý bài đăng", new QLBaidangFragment());
                } else if (item.getItemId() == R.id.nav_QLHotro){
                    setTitleAndFragment("Quản lý hỗ trợ", new QLHotroFragment());
                } else if(item.getItemId() == R.id.nav_DangXuat){
                    // Xóa dữ liệu trong SharedPreferences khi người dùng đăng xuất
                    SharedPreferences pref = getSharedPreferences("user", MODE_PRIVATE);
                    SharedPreferences.Editor editor = pref.edit();
                    editor.clear();  // Xóa tất cả dữ liệu trong SharedPreferences
                    editor.apply();  // Áp dụng thay đổi

                    // Chuyển hướng về màn hình đăng nhập
                    Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();  // Đóng MainActivity để người dùng không quay lại trang này
                } else if(item.getItemId() == R.id.nav_login){
                    Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                }
                drawer.closeDrawers(); //when clicked then close
                return true;
            }
        });

        // Bottom Navigation Item Selection
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if(item.getItemId() == R.id.nav_home) {
                    setTitleAndFragment("Trang chủ", new TrangChuFragment());
                } else if(item.getItemId() == R.id.nav_search) {
                    setTitleAndFragment("Tìm kiếm", new TimKiemFragment());
                } else if(item.getItemId() == R.id.nav_notifications){
                    setTitleAndFragment("Thông báo", new ThongBaoFragment());
                } else if(item.getItemId() == R.id.nav_profile){
                    setTitleAndFragment("Hồ sơ", new HoSoFragment());
                }
                return true;
            }
        });
        // Set the default selected item for BottomNavigationView
        bottomNavigationView.setSelectedItemId(R.id.nav_home);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            drawer.openDrawer(GravityCompat.START);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    public void setTitleAndFragment(String title, Fragment fragment) {
        getSupportActionBar().setTitle(title);
        fragmentManager
                .beginTransaction()
                .replace(R.id.frame_layout, fragment)
                .commit();
    }



}