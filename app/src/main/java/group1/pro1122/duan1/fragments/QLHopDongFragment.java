package group1.pro1122.duan1.fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import group1.pro1122.duan1.R;
import group1.pro1122.duan1.adapters.HopDongAdapter;
import group1.pro1122.duan1.daos.HopDongDAO;
import group1.pro1122.duan1.daos.PhiDichVuDAO;
import group1.pro1122.duan1.daos.PhongDAO;
import group1.pro1122.duan1.daos.ThongBaoDAO;
import group1.pro1122.duan1.models.HopDong;
import group1.pro1122.duan1.models.PhiDichVu;
import group1.pro1122.duan1.models.Phong;
import group1.pro1122.duan1.models.ThongBao;


public class QLHopDongFragment extends Fragment {

    String TAG = "zzzzzzzzzzzz";

    private RecyclerView rcvHopDong;
    private FloatingActionButton fab;
    private HopDongAdapter adapter;
    private ArrayList<HopDong> listHopDong;
    private HopDongDAO hopDongDAO;
    private TextView tvHopDongThongBao;
    private PhongDAO phongDAO;
    private PhiDichVuDAO phiDichVuDAO;
    private ThongBaoDAO thongBaoDAO;
    private SearchView searchView;
    private ImageView btnFilter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_q_l_hop_dong, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //Ánh xạ và khai báo
        rcvHopDong = view.findViewById(R.id.rcvHopDong);
        fab = view.findViewById(R.id.fab);
        tvHopDongThongBao = view.findViewById(R.id.tvHopDongThongBao);
        phongDAO = new PhongDAO(getContext());
        phiDichVuDAO = new PhiDichVuDAO(getContext());
        rcvHopDong.setLayoutManager(new LinearLayoutManager(getActivity()));
        searchView = view.findViewById(R.id.searchView);
        btnFilter = view.findViewById(R.id.btnFilter);
        adapter = new HopDongAdapter(getContext(), listHopDong);
        thongBaoDAO = new ThongBaoDAO(getContext());
        refreshList();

        // Nhận dữ liệu từ Bundle
        Bundle bundle = getArguments();
        if (bundle != null) {
            int nguoiDungID = bundle.getInt("NguoiDungID", -1);
            int phongID = bundle.getInt("PhongID", -1);

            // Kiểm tra và mở dialog nếu có dữ liệu hợp lệ
            if (nguoiDungID != -1 && phongID != -1) {
                showAddHopDongDialog(nguoiDungID, phongID);
            }
        }

        //Khởi tọa adapter
        if(listHopDong != null){
            adapter = new HopDongAdapter(getContext(), listHopDong);
            rcvHopDong.setAdapter(adapter);
            tvHopDongThongBao.setVisibility(View.GONE);
            rcvHopDong.setVisibility(View.VISIBLE);


            //Kiểm tra xem có hợp đồng nào đến hạn thanh toán tiền phòng không - sau đó gửi thông báo để chủ trọ có thể biết
            for(HopDong hopDong : listHopDong){
                if(hopDong.getTrangThaiHopDong() == 1){
                    try {
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                        Date ngayBatDau = sdf.parse(hopDong.getNgayTaoHopDong());
                        Date ngayHienTai = new Date();

                        Log.d(TAG, "onViewCreated: ngayBatDau của HĐ "+ hopDong.getHopDongId()+ " = " +ngayBatDau);
                        Log.d(TAG, "onViewCreated: ngayHienTai của HĐ"+ hopDong.getHopDongId()+ " = "+ngayHienTai);

                        // Lấy ngày gửi thông báo cuối hoặc ngày bắt đầu (nếu chưa có ngày gửi thông báo)
                        Date ngayGuiThongBaoCuoi = thongBaoDAO.getNgayGuiThongBaoCuoi(hopDong.getHopDongId()) != null
                                ? new Date(Long.parseLong(thongBaoDAO.getNgayGuiThongBaoCuoi(hopDong.getHopDongId())))
                                : ngayBatDau;

                        Log.d(TAG, "onViewCreated: ngayGuiThongBaoCuoi "+ hopDong.getHopDongId()+ " = "+ngayGuiThongBaoCuoi.toString());
                        Calendar calendar = Calendar.getInstance();
                        calendar.setTime(ngayGuiThongBaoCuoi);
                        calendar.add(Calendar.DAY_OF_MONTH, 30); // Thay đổi ngày để test ở đây nhé AE

                        if (!ngayHienTai.before(calendar.getTime())) {
                            // Gửi thông báo cho chủ trọ
                            int chuTro = hopDong.getChuTro();
                            String noiDung = "Hợp đồng ID số " + hopDong.getHopDongId()
                                    + " cần thanh toán tiền phòng. Vui lòng kiểm tra!";
                            boolean check = thongBaoDAO.sendThongBao(chuTro, hopDong.getHopDongId(), 1, noiDung); // Loại 1: thông báo thanh toán

                            if (check) {
                                Log.d("ThongBao", "Gửi thông báo thanh toán thành công");
                            } else {
                                Log.d("ThongBao", "Gửi thông báo thanh toán thất bại");
                            }
                        }


                        // Kiểm tra xem hợp đồng nào đã hết hạn chưa
                        Date ngayKetThuc = sdf.parse(hopDong.getNgayKetThuc());
                        Log.d(TAG, "onViewCreated: ngayKetThuc của HĐ " + hopDong.getHopDongId() + " = " + ngayKetThuc);
                        // Nếu ngày hiện tại bằng hoặc sau ngày kết thúc hợp đồng
                        if (!ngayHienTai.before(ngayKetThuc)) {
                            if (hopDong.getTrangThaiHopDong() != 0) { // Trạng thái 0 là đã hết hạn
                                hopDongDAO.capNhatTrangThaiHopDongHetHan(hopDong.getHopDongId());

                                // Tạo nội dung thông báo
                                String noiDungHopDong = "Hợp đồng số " + hopDong.getHopDongId()
                                        + " của phòng " + hopDong.getPhongId()
                                        + " đã hết hạn. Vui lòng kiểm tra!";

                                // Gửi thông báo cho chủ trọ
                                thongBaoDAO.sendThongBao(hopDong.getChuTro(), hopDong.getHopDongId(), 2, noiDungHopDong);

                                // Gửi thông báo cho người thuê
                                thongBaoDAO.sendThongBao(hopDong.getUserId(), hopDong.getHopDongId(), 2, noiDungHopDong);

                                Log.d(TAG, "onViewCreated: Đã cập nhật trạng thái và gửi thông báo cho HĐ " + hopDong.getHopDongId());
                            }
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }


        } else{
            rcvHopDong.setVisibility(View.GONE);
            tvHopDongThongBao.setVisibility(View.VISIBLE);
        }

        // Tìm kiếm theo ID hợp đồng
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchHopDongById(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                searchHopDongById(newText);
                return true;
            }
        });

        // Lọc theo trạng thái hợp đồng
        btnFilter.setOnClickListener(v -> showFilterDialog());

        fab.setOnClickListener(v -> {
            showAddHopDongDialog(-1, -1);
        });
    }

    // Tìm kiếm theo ID hợp đồng
    private void searchHopDongById(String query) {
        ArrayList<HopDong> filteredList = new ArrayList<>();
        for (HopDong hopDong : listHopDong) {
            if (String.valueOf(hopDong.getHopDongId()).contains(query)) {
                filteredList.add(hopDong);
            }
        }
        updateRecyclerView(filteredList);
    }

    // Hiển thị dialog lọc trạng thái hợp đồng
    private void showFilterDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_filter_hopdong, null);
        builder.setView(dialogView);

        // Ánh xạ
        Spinner spinnerTrangThai = dialogView.findViewById(R.id.spinnerTrangThaiHopDong);
        Button btnApDung = dialogView.findViewById(R.id.btnApDung);

        AlertDialog alertDialog = builder.create();

        btnApDung.setOnClickListener(v -> {
            int trangThai = spinnerTrangThai.getSelectedItemPosition() - 1; // -1 là tất cả, 0 là đã kết thúc, 1 là đang hiệu lực, 2 là chưa ký kết
            filterHopDongByTrangThai(trangThai);
            alertDialog.dismiss();
        });

        alertDialog.show();
    }

    // Lọc danh sách hợp đồng theo trạng thái
    private void filterHopDongByTrangThai(int trangThai) {
        ArrayList<HopDong> filteredList = new ArrayList<>();
        for (HopDong hopDong : listHopDong) {
            if (trangThai == -1 || hopDong.getTrangThaiHopDong() == trangThai) {
                filteredList.add(hopDong);
            }
        }
        updateRecyclerView(filteredList);
    }

    // Cập nhật RecyclerView
    private void updateRecyclerView(ArrayList<HopDong> filteredList) {
        if (filteredList.isEmpty()) {
            rcvHopDong.setVisibility(View.GONE);
            tvHopDongThongBao.setVisibility(View.VISIBLE);
        } else {
            rcvHopDong.setVisibility(View.VISIBLE);
            tvHopDongThongBao.setVisibility(View.GONE);
        }
        adapter = new HopDongAdapter(getContext(), filteredList);
        rcvHopDong.setAdapter(adapter);
    }

    public void showAddHopDongDialog(int nguoiDungID, int phongID){
        Context context = getContext();
        if (context == null) {
            Log.e("QLHopDongFragment", "Context is null, cannot show dialog");
            return;
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_hopdong, null);
        builder.setView(view);

        //Ánh xạ
        EditText edtPhongId = view.findViewById(R.id.edtPhongId);
        EditText edtNguoiThue = view.findViewById(R.id.edtNguoiThue);
        EditText edtDonGiaDien = view.findViewById(R.id.edtDonGiaDien);
        EditText edtDonGiaNuoc = view.findViewById(R.id.edtDonGiaNuoc);
        EditText edtVeSinh = view.findViewById(R.id.edtVeSinh);
        EditText edtGuiXe = view.findViewById(R.id.edtGuiXe);
        EditText edtInternet = view.findViewById(R.id.edtInternet);
        EditText edtThangMay = view.findViewById(R.id.edtThangMay);
        Button btnTao = view.findViewById(R.id.btnTao);

        // Gán giá trị nhận được vào các trường
        if(nguoiDungID != -1 && phongID != -1){
            edtPhongId.setText(String.valueOf(phongID));
            edtNguoiThue.setText(String.valueOf(nguoiDungID));
        }


        AlertDialog alertDialog = builder.create();

        btnTao.setOnClickListener(v -> {
            String phongId = edtPhongId.getText().toString().trim();
            String nguoiThue = edtNguoiThue.getText().toString().trim();
            String donGiaDien = edtDonGiaDien.getText().toString().trim();
            String donGiaNuoc = edtDonGiaNuoc.getText().toString().trim();
            String veSinh = edtVeSinh.getText().toString().trim();
            String guiXe = edtGuiXe.getText().toString().trim();
            String internet = edtInternet.getText().toString().trim();
            String thangMay = edtThangMay.getText().toString().trim();

            if(phongId.isEmpty() || nguoiThue.isEmpty() || donGiaDien.isEmpty() || donGiaNuoc.isEmpty() || veSinh.isEmpty() || guiXe.isEmpty() || internet.isEmpty() || thangMay.isEmpty()){
                Toast.makeText(getContext(), "Không để trống!", Toast.LENGTH_SHORT).show();
                return;
            }

            int phongIdInt = Integer.parseInt(phongId);
            // Kiểm tra trạng thái hợp đồng hiện tại của phòng
            HopDong hopDongDangHieuLuc = hopDongDAO.getHopDongDangHieuLucByPhongID(phongIdInt);
            if (hopDongDangHieuLuc != null) {
                Toast.makeText(getContext(),
                        "Phòng này đang có hợp đồng hiệu lực. Không thể tạo hợp đồng mới!",
                        Toast.LENGTH_SHORT).show();
                return;
            }

            Phong phong = phongDAO.getPhongByPhongID(Integer.parseInt(phongId));
            if(phong == null){
                Toast.makeText(getContext(), "Phòng không tồn tại!", Toast.LENGTH_SHORT).show();
                return;
            }

            if(phong.getTrangThai() == 1){
                Toast.makeText(getContext(), "Phòng "+phong.getSoPhong()+" đã được thuê. Vui lòng chọn phòng khác!", Toast.LENGTH_SHORT).show();
                return;
            }

            if(phong.getTrangThaiPheduyet() == 0){
                Toast.makeText(getContext(), "Phòng "+phong.getSoPhong()+" chưa được phê duyệt", Toast.LENGTH_SHORT).show();
            }

            // Tạo đối tượng PhiDichVu và chèn vào cơ sở dữ liệu
            PhiDichVu phiDichVu = new PhiDichVu(0, Integer.parseInt(donGiaDien),Integer.parseInt( donGiaNuoc),Integer.parseInt(veSinh), Integer.parseInt(guiXe), Integer.parseInt(internet), Integer.parseInt(thangMay));
            long phiDichVuId = phiDichVuDAO.createPhiDichVu(phiDichVu);
            if(phiDichVuId == -1){
                Toast.makeText(getContext(), "Tạo phí dịch vụ thất bại", Toast.LENGTH_SHORT).show();
                return;
            }

            // Lấy ngày hiện tại
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            String ngayBatDau = sdf.format(new Date());

            // Tính ngày kết thúc hợp đồng (1 năm sau)
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.YEAR, 1); //Thay đổi thời gian của hợp đồng để test ở đây nhé AE
            String ngayKetThuc = sdf.format(calendar.getTime());

            // Tạo đối tượng HopDong và chèn vào cơ sở dữ liệu
            SharedPreferences pref = getContext().getSharedPreferences("user", getContext().MODE_PRIVATE);
            int chuTro = pref.getInt("userID", -1);
            HopDong hopDong = new HopDong(0, Integer.parseInt(phongId), Integer.parseInt(nguoiThue), chuTro, (int) phiDichVuId, ngayBatDau, ngayKetThuc, 2); //Trạng thại hợp đồng 2 - chờ ký kết
            boolean result = hopDongDAO.sendHopDongToNguoiThue(hopDong);
            if(result){
                alertDialog.dismiss();
                showDialogThongBao("Thông báo", "Tạo hợp đồng thành công! Đã gửi yêu cầu ký kết hợp đồng đến người thuê. Vui lòng đợi người thuê ký");
                //Gửi thông báo!
                thongBaoDAO = new ThongBaoDAO(getContext());
                String noiDung = "Chủ trọ ID số "+chuTro+" đã gửi yêu cầu ký kết hợp đồng phòng " + phong.getSoPhong() +" cho bạn. Vui lòng kiểm tra và ký kết hợp đồng!";
                thongBaoDAO.sendThongBao(Integer.parseInt(nguoiThue), (int) hopDong.getHopDongId(), 2, noiDung); // 2 là loại thông báo về hợp đồng
                refreshList();
            } else{
                showDialogThongBao("Lỗi", "Tạo hợp đồng thất bại!");
            }

        });

        alertDialog.show();
    }

    public void refreshList(){
        //Lấy user id
        SharedPreferences pref = getContext().getSharedPreferences("user", getContext().MODE_PRIVATE);
        int userID = pref.getInt("userID", -1);

        //Khởi tạo dữ liệu
        hopDongDAO = new HopDongDAO(getContext());
        listHopDong = hopDongDAO.readByIDChuTro(userID);
        adapter.refreshList(listHopDong);
    }

    public void showDialogThongBao(String title, String noiDung){
        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(getContext());
        builder.setTitle(title)
                .setMessage(noiDung)
                .setPositiveButton("OK", (dialog, which) -> dialog.dismiss());
        androidx.appcompat.app.AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}