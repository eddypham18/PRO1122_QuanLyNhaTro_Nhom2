package group1.pro1122.duan1.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.Manifest;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.SearchView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import group1.pro1122.duan1.R;
import group1.pro1122.duan1.adapters.PhongAdapter;
import group1.pro1122.duan1.adapters.ThanhPhoSpinnerAdapter;
import group1.pro1122.duan1.daos.DiaDiemDAO;
import group1.pro1122.duan1.daos.PhongDAO;
import group1.pro1122.duan1.daos.UserDAO;
import group1.pro1122.duan1.models.DiaDiem;
import group1.pro1122.duan1.models.Phong;
import group1.pro1122.duan1.models.User;

public class QLPhongFragment extends Fragment {

    private static final int REQUEST_READ_EXTERNAL_STORAGE = 101;
    private static final int REQUEST_IMAGE_PICK = 100;

    SearchView searchView;
    RecyclerView rcvQLPhong;
    FloatingActionButton fab;
    ArrayList<Phong> listPhong = new ArrayList<>();
    PhongDAO phongDAO;

    private ImageView imgPhong, btnFilter;
    private byte[] imgPhongData;
    int userID;
    ArrayList<DiaDiem> listThanhPho;
    DiaDiemDAO diaDiemDAO;
    int idThanhPho = 0;
    ThanhPhoSpinnerAdapter thanhPhoSpinnerAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_quan_ly_phong, container, false);

        // Ánh xạ và khai báo
        searchView = view.findViewById(R.id.searchView);
        btnFilter = view.findViewById(R.id.btnFilter);
        rcvQLPhong = view.findViewById(R.id.rcvQLPhong);
        fab = view.findViewById(R.id.fab);
        phongDAO = new PhongDAO(getContext());
        diaDiemDAO = new DiaDiemDAO(getContext());

        // Xin quyền đọc bộ nhớ ngoài
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_READ_EXTERNAL_STORAGE);
        }

        // Lấy User_ID từ SharedPreferences
        SharedPreferences pref = getContext().getSharedPreferences("user", Context.MODE_PRIVATE);
        userID = pref.getInt("userID", 0);
        UserDAO userDAO = new UserDAO(getContext());
        User user = userDAO.getUserByUserID(userID);

        // Lấy dữ liệu từ cơ sở dữ liệu
        listPhong = phongDAO.read(userID);
        listThanhPho = diaDiemDAO.read();

        // Tạo adapter
        PhongAdapter adapter = new PhongAdapter(getContext(), listPhong);
        adapter.setOnItemClickListener(this::showPhongDetailDialog);
        // Thêm long-click listener để hiển thị hộp thoại cập nhật
        adapter.setOnItemLongClickListener(this::showUpdatePhongDialog);
        rcvQLPhong.setLayoutManager(new LinearLayoutManager(getContext()));
        rcvQLPhong.setAdapter(adapter);

        //Floating Action button
        fab.setOnClickListener(v -> {
            if(!user.getHoTen().equals("") && !user.getSdt().equals("") && !user.getCccd().equals("") && !user.getEmail().equals("") && !user.getNgaySinh().equals("")){
                showAddPhongDialog();
            } else {
                Toast.makeText(getContext(), "Vui lòng cập nhật thông tin cá nhân trước khi sử dụng chức năng!", Toast.LENGTH_SHORT).show();
            }
        });

        //Nút lọc
        btnFilter.setOnClickListener(v -> {
            showFilterDialog();
        });

        // Thiết lập tìm kiếm tên phòng
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchPhongByName(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                searchPhongByName(newText);
                return true;
            }
        });

        return view;
    }

    // Hàm tìm kiếm theo tên phòng
    private void searchPhongByName(String query) {
        ArrayList<Phong> filteredList = new ArrayList<>();
        for (Phong phong : listPhong) {
            if (phong.getSoPhong().toLowerCase().contains(query.toLowerCase())) {
                filteredList.add(phong);
            }
        }
        PhongAdapter adapter = new PhongAdapter(getContext(), filteredList);
        adapter.setOnItemClickListener(this::showPhongDetailDialog);
        adapter.setOnItemLongClickListener(this::showUpdatePhongDialog);
        rcvQLPhong.setAdapter(adapter);
    }

    // Hàm hiển thị dialog lọc
    private void showFilterDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_filter_phong, null);
        builder.setView(dialogView);

        // Các thành phần trong dialog
        Spinner spinnerTrangThaiPheDuyet = dialogView.findViewById(R.id.spinnerTrangThaiPheDuyet);
        Spinner spinnerTrangThaiThue = dialogView.findViewById(R.id.spinnerTrangThaiThue);
        Spinner spinnerDiaDiem = dialogView.findViewById(R.id.spinnerDiaDiem);
        EditText edtGiaThueMin = dialogView.findViewById(R.id.edtGiaThueMin);
        EditText edtGiaThueMax = dialogView.findViewById(R.id.edtGiaThueMax);
        EditText edtDienTichMin = dialogView.findViewById(R.id.edtDienTichMin);
        EditText edtDienTichMax = dialogView.findViewById(R.id.edtDienTichMax);
        Button btnApDung = dialogView.findViewById(R.id.btnApDung);

        // Thêm mục tất cả (0) vào danh sách địa điểm -> 0 tức là không chọn địa điểm
        addTatCaOptionToDiaDiemList();

        // Adapter cho Spinner Thành phố
        ThanhPhoSpinnerAdapter diaDiemAdapter = new ThanhPhoSpinnerAdapter(getContext(), listThanhPho);
        spinnerDiaDiem.setAdapter(diaDiemAdapter);

        AlertDialog alertDialog = builder.create();

        // Xử lý nút Áp dụng
        btnApDung.setOnClickListener(v -> {
            // Lấy các giá trị lọc từ người dùng
            int trangThaiPheDuyet = spinnerTrangThaiPheDuyet.getSelectedItemPosition(); // 0: Tất cả, 1: Đã duyệt, 2: Chưa duyệt
            int trangThaiThue = spinnerTrangThaiThue.getSelectedItemPosition(); // 0: Tất cả, 1: Đã thuê, 2: Trống
            int diaDiemID = listThanhPho.get(spinnerDiaDiem.getSelectedItemPosition()).getDiaDiemId();
            String giaThueMinStr = edtGiaThueMin.getText().toString().trim();
            String giaThueMaxStr = edtGiaThueMax.getText().toString().trim();
            String dienTichMinStr = edtDienTichMin.getText().toString().trim();
            String dienTichMaxStr = edtDienTichMax.getText().toString().trim();

            // Chuyển đổi giá trị lọc về kiểu số
            Integer giaThueMin = giaThueMinStr.isEmpty() ? null : Integer.parseInt(giaThueMinStr);
            Integer giaThueMax = giaThueMaxStr.isEmpty() ? null : Integer.parseInt(giaThueMaxStr);
            Integer dienTichMin = dienTichMinStr.isEmpty() ? null : Integer.parseInt(dienTichMinStr);
            Integer dienTichMax = dienTichMaxStr.isEmpty() ? null : Integer.parseInt(dienTichMaxStr);

            // Gọi hàm lọc danh sách phòng
            filterPhongList(trangThaiPheDuyet, trangThaiThue, diaDiemID, giaThueMin, giaThueMax, dienTichMin, dienTichMax);

            alertDialog.dismiss();
        });

        alertDialog.show();
    }


    //xử lý lọc
    private void filterPhongList(int trangThaiPheDuyet, int trangThaiThue, int diaDiemID,
                                 Integer giaThueMin, Integer giaThueMax, Integer dienTichMin, Integer dienTichMax) {
        // Lấy danh sách phòng gốc từ database
        ArrayList<Phong> allPhong = phongDAO.read(userID);
        ArrayList<Phong> filteredPhong = new ArrayList<>();

        for (Phong phong : allPhong) {
            // Lọc theo trạng thái phê duyệt nếu được chọn
            if (trangThaiPheDuyet == 1 && phong.getTrangThaiPheduyet() != 1) continue;
            if (trangThaiPheDuyet == 2 && phong.getTrangThaiPheduyet() != 0) continue;

            // Lọc theo trạng thái thuê nếu được chọn
            if (trangThaiThue == 1 && phong.getTrangThai() != 1) continue;
            if (trangThaiThue == 2 && phong.getTrangThai() != 0) continue;

            // Lọc theo địa điểm nếu được chọn
            if (diaDiemID != 0 && phong.getDiaDiemID() != diaDiemID) continue;

            // Lọc theo giá thuê nếu người dùng nhập giá trị
            if (giaThueMin != null && phong.getGiaThue() < giaThueMin) continue;
            if (giaThueMax != null && phong.getGiaThue() > giaThueMax) continue;

            // Lọc theo diện tích nếu người dùng nhập giá trị
            if (dienTichMin != null && phong.getDienTich() < dienTichMin) continue;
            if (dienTichMax != null && phong.getDienTich() > dienTichMax) continue;

            // Nếu thỏa mãn tất cả điều kiện lọc (có giá trị) thì thêm vào danh sách kết quả
            filteredPhong.add(phong);
        }

        // Cập nhật adapter với danh sách phòng đã được lọc
        PhongAdapter adapter = new PhongAdapter(getContext(), filteredPhong);
        adapter.setOnItemClickListener(this::showPhongDetailDialog);
        adapter.setOnItemLongClickListener(this::showUpdatePhongDialog);
        rcvQLPhong.setAdapter(adapter);
    }

    // Kiểm tra và chỉ thêm mục "Tất cả" một lần vào listThanhPho
    private void addTatCaOptionToDiaDiemList() {
        // Kiểm tra xem đã có mục "Tất cả" trong danh sách chưa
        boolean hasTatCaOption = false;
        for (DiaDiem diaDiem : listThanhPho) {
            if (diaDiem.getDiaDiemId() == 0) {
                hasTatCaOption = true;
                break;
            }
        }

        // Nếu chưa có, thêm "Tất cả" vào danh sách
        if (!hasTatCaOption) {
            DiaDiem diaDiemTatCa = new DiaDiem(0, "Tất cả");
            listThanhPho.add(0, diaDiemTatCa);
        }
    }


    // Hàm để hiển thị dialog cập nhật phòng khi người dùng nhấn giữ vào item
    private void showUpdatePhongDialog(Phong phong) {
        // Kiểm tra trạng thái của phòng
        if (phong.getTrangThai() == 1) {
            Toast.makeText(getContext(), "Không thể cập nhật vì phòng đang được thuê!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (phong.getTrangThaiPheduyet() == 1) {
            Toast.makeText(getContext(), "Không thể cập nhật vì phòng đã được phê duyệt!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Nếu các điều kiện trên không thỏa mãn, tiếp tục hiển thị dialog cập nhật
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_phong, null);
        builder.setView(dialogView);

        EditText edtSoPhong = dialogView.findViewById(R.id.edtSoPhong);
        EditText edtDienTich = dialogView.findViewById(R.id.edtDienTich);
        EditText edtGiaThue = dialogView.findViewById(R.id.edtGiaThue);
        EditText edtMoTa = dialogView.findViewById(R.id.edtMoTa);
        imgPhong = dialogView.findViewById(R.id.imgPhong);
        Button btnChonAnh = dialogView.findViewById(R.id.btnChonAnh);
        Spinner spinnerTP = dialogView.findViewById(R.id.spinnerTP);
        Button btnCapNhat = dialogView.findViewById(R.id.btnTao);
        btnCapNhat.setText("Cập nhật");
        AlertDialog alertDialog = builder.create();

        // Đặt dữ liệu hiện tại của phòng vào các trường nhập liệu
        edtSoPhong.setText(phong.getSoPhong());
        edtDienTich.setText(String.valueOf(phong.getDienTich()));
        edtGiaThue.setText(String.valueOf(phong.getGiaThue()));
        edtMoTa.setText(phong.getMoTa());
        imgPhong.setImageBitmap(BitmapFactory.decodeByteArray(phong.getAnhPhong(), 0, phong.getAnhPhong().length));

        // Đặt giá trị hiện tại của ảnh nếu không chọn ảnh mới
        imgPhongData = phong.getAnhPhong();

        // Đặt spinner cho Thành phố
        thanhPhoSpinnerAdapter = new ThanhPhoSpinnerAdapter(getContext(), listThanhPho);
        spinnerTP.setAdapter(thanhPhoSpinnerAdapter);
        for (int i = 0; i < listThanhPho.size(); i++) {
            if (listThanhPho.get(i).getDiaDiemId() == phong.getDiaDiemID()) {
                spinnerTP.setSelection(i);
                break;
            }
        }

        spinnerTP.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                idThanhPho = listThanhPho.get(position).getDiaDiemId();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        // Xử lý chọn ảnh
        btnChonAnh.setOnClickListener(v -> openGallery());

        // Xử lý cập nhật phòng
        btnCapNhat.setOnClickListener(v -> {
            String soPhong = edtSoPhong.getText().toString().trim();
            String dienTichStr = edtDienTich.getText().toString().trim();
            String giaThueStr = edtGiaThue.getText().toString().trim();
            String moTa = edtMoTa.getText().toString().trim();

            if (soPhong.isEmpty() || dienTichStr.isEmpty() || giaThueStr.isEmpty() || moTa.isEmpty()) {
                Toast.makeText(getContext(), "Vui lòng nhập đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
                return;
            }

            int dienTich = Integer.parseInt(dienTichStr);
            int giaThue = Integer.parseInt(giaThueStr);

            // Cập nhật thông tin phòng vào cơ sở dữ liệu
            phong.setSoPhong(soPhong);
            phong.setDienTich(dienTich);
            phong.setGiaThue(giaThue);
            phong.setMoTa(moTa);
            phong.setDiaDiemID(idThanhPho);

            if (imgPhongData != null) {
                phong.setAnhPhong(imgPhongData);
            }

            boolean isUpdated = phongDAO.update(phong);
            if (isUpdated) {
                listPhong = phongDAO.read(userID);
                rcvQLPhong.setAdapter(new PhongAdapter(getContext(), listPhong));
                Toast.makeText(getContext(), "Cập nhật phòng thành công!", Toast.LENGTH_SHORT).show();
                alertDialog.dismiss();
            } else {
                Toast.makeText(getContext(), "Cập nhật phòng thất bại!", Toast.LENGTH_SHORT).show();
            }
        });


        alertDialog.show();
    }

    private void showAddPhongDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_phong, null);
        builder.setView(dialogView);

        EditText edtSoPhong = dialogView.findViewById(R.id.edtSoPhong);
        EditText edtDienTich = dialogView.findViewById(R.id.edtDienTich);
        EditText edtGiaThue = dialogView.findViewById(R.id.edtGiaThue);
        EditText edtMoTa = dialogView.findViewById(R.id.edtMoTa);
        imgPhong = dialogView.findViewById(R.id.imgPhong);
        Button btnChonAnh = dialogView.findViewById(R.id.btnChonAnh);
        Spinner spinnerTP = dialogView.findViewById(R.id.spinnerTP);
        Button btnTao = dialogView.findViewById(R.id.btnTao);

        AlertDialog alertDialog = builder.create();

        // Xử lý spinner
        thanhPhoSpinnerAdapter = new ThanhPhoSpinnerAdapter(getContext(), listThanhPho);
        spinnerTP.setAdapter(thanhPhoSpinnerAdapter);
        spinnerTP.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                idThanhPho = listThanhPho.get(position).getDiaDiemId();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        // Xử lý chọn ảnh
        btnChonAnh.setOnClickListener(v -> openGallery());

        // Xử lý tạo phòng
        btnTao.setOnClickListener(v -> {
            String soPhong = edtSoPhong.getText().toString().trim();
            String dienTichStr = edtDienTich.getText().toString().trim();
            String giaThueStr = edtGiaThue.getText().toString().trim();
            String moTa = edtMoTa.getText().toString().trim();

            if (soPhong.isEmpty() || dienTichStr.isEmpty() || giaThueStr.isEmpty() || moTa.isEmpty() || imgPhongData == null) {
                Toast.makeText(getContext(), "Vui lòng nhập đầy đủ thông tin và chọn ảnh!", Toast.LENGTH_SHORT).show();
                return;
            }

            int dienTich = Integer.parseInt(dienTichStr);
            int giaThue = Integer.parseInt(giaThueStr);

            // Lưu thông tin phòng vào cơ sở dữ liệu
            boolean isAdded = phongDAO.create(userID, soPhong, dienTich, giaThue, moTa, imgPhongData, idThanhPho);
            if (isAdded) {
                listPhong = phongDAO.read(userID);
                rcvQLPhong.setAdapter(new PhongAdapter(getContext(), listPhong));
                Toast.makeText(getContext(), "Thêm phòng thành công!", Toast.LENGTH_SHORT).show();
                alertDialog.dismiss();
            } else {
                Toast.makeText(getContext(), "Thêm phòng thất bại!", Toast.LENGTH_SHORT).show();
            }
        });

        alertDialog.show();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_PICK && resultCode == Activity.RESULT_OK && data != null) {
            Uri selectedImageUri = data.getData();
            if (selectedImageUri != null) {
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), selectedImageUri);
                    imgPhong.setImageBitmap(bitmap);

                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                    imgPhongData = stream.toByteArray();
                    stream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(getContext(), "Không thể hiển thị ảnh", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private void showPhongDetailDialog(Phong phong) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_chitietphong, null);
        builder.setView(dialogView);

        ImageView imgPhong = dialogView.findViewById(R.id.imgPhong);
        TextView tvSoPhong = dialogView.findViewById(R.id.tvSoPhong);
        TextView tvDienTich = dialogView.findViewById(R.id.tvDienTich);
        TextView tvTrangThaiThue = dialogView.findViewById(R.id.tvTrangThaiThue);
        TextView tvGiaThue = dialogView.findViewById(R.id.tvGiaThue);
        TextView tvMoTa = dialogView.findViewById(R.id.tvMoTa);
        TextView tvDiaDiem = dialogView.findViewById(R.id.tvDiaDiem);
        TextView tvNgayTao = dialogView.findViewById(R.id.tvNgayTao);

        // Hiển thị ảnh từ byte[]
        Bitmap bitmap = BitmapFactory.decodeByteArray(phong.getAnhPhong(), 0, phong.getAnhPhong().length);
        imgPhong.setImageBitmap(bitmap);

        tvSoPhong.setText("Số phòng: " + phong.getSoPhong());
        tvDienTich.setText("Diện tích: " + phong.getDienTich() + " m²");
        tvTrangThaiThue.setText("Trạng thái thuê: " + (phong.getTrangThai() == 1 ? "Đã thuê" : "Trống"));
        tvGiaThue.setText("Giá thuê: " + phong.getGiaThue() + " VND");
        for (DiaDiem diaDiem : listThanhPho) {
            if (phong.getDiaDiemID() == diaDiem.getDiaDiemId()) {
                tvDiaDiem.setText("Địa điểm: " +diaDiem.getThanhPho());
                break;
            }
        }
        // Hiển thị ngày tạo sau khi chuyển đổi
        String formattedDate = convertTimestampToDate(phong.getNgayTao());
        tvNgayTao.setText("Ngày tạo: " + formattedDate);
        tvMoTa.setText("Mô tả: " + phong.getMoTa());

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, REQUEST_IMAGE_PICK);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_READ_EXTERNAL_STORAGE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openGallery();
            } else {
                Toast.makeText(getContext(), "Vui lòng cấp quyền để chọn ảnh", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private String convertTimestampToDate(String timestamp) {
        try {
            long timeMillis = Long.parseLong(timestamp);
            Date date = new Date(timeMillis);
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.getDefault());
            return sdf.format(date);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return "Invalid date";
        }
    }
}
