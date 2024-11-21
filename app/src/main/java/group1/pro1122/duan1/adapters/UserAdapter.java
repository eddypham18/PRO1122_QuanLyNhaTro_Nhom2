package group1.pro1122.duan1.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import group1.pro1122.duan1.R;
import group1.pro1122.duan1.models.User;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {

    private final Context context;
    private ArrayList<User> userList;
    private OnChangeStatusClickListener onChangeStatusClickListener;

    public UserAdapter(Context context, ArrayList<User> userList) {
        this.context = context;
        this.userList = userList;
    }

    public interface OnChangeStatusClickListener {
        void onChangeStatusClick(User user);
    }

    public void setOnChangeStatusClickListener(OnChangeStatusClickListener listener) {
        this.onChangeStatusClickListener = listener;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_user, parent, false);
        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        User user = userList.get(position);
        holder.tvUsername.setText("Username: " + user.getUsername());
        holder.tvUserID.setText("ID: " + user.getUserId());
        if(user.getVaiTro() == 0){
            holder.tvVaiTro.setText("Vai trò: Người dùng");
        } else if (user.getVaiTro() == 1) {
            holder.tvVaiTro.setText("Vai trò: Chủ trọ");
        } else {
            holder.tvVaiTro.setText("Vai trò: Admin");
        }
        holder.tvNgaySinh.setText("Ngày sinh: " + user.getNgaySinh());
        holder.tvSDT.setText("Số điện thoại: " + user.getSdt());
        holder.tvCCCD.setText("CCCD: " + user.getCccd());
        holder.tvHoTen.setText("Họ tên: " + user.getHoTen());
        holder.tvEmail.setText("Email: " + user.getEmail());
        holder.tvTrangThai.setText("Trạng thái: " + (user.getTrangThai() == 1 ? "Hoạt động" : "Khóa"));

        // Đổi tên nút dựa trên trạng thái hiện tại
        holder.btnChangeStatus.setText(user.getTrangThai() == 1 ? "Khóa" : "Mở khóa");
        holder.btnChangeStatus.setBackgroundColor(user.getTrangThai() == 1 ? context.getResources().getColor(R.color.red) : context.getResources().getColor(R.color.green));

        holder.btnChangeStatus.setOnClickListener(v -> {
            if (onChangeStatusClickListener != null) {
                onChangeStatusClickListener.onChangeStatusClick(user);
            }
        });
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    static class UserViewHolder extends RecyclerView.ViewHolder {
        TextView tvUsername, tvUserID, tvVaiTro, tvHoTen, tvNgaySinh,tvSDT, tvCCCD, tvEmail, tvTrangThai;
        Button btnChangeStatus;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            tvUsername = itemView.findViewById(R.id.tvUsername);
            tvUserID = itemView.findViewById(R.id.tvUserID);
            tvVaiTro = itemView.findViewById(R.id.tvVaiTro);
            tvNgaySinh = itemView.findViewById(R.id.tvNgaySinh);
            tvSDT = itemView.findViewById(R.id.tvSDT);
            tvCCCD = itemView.findViewById(R.id.tvCCCD);
            tvHoTen = itemView.findViewById(R.id.tvHoTen);
            tvEmail = itemView.findViewById(R.id.tvEmail);
            tvTrangThai = itemView.findViewById(R.id.tvTrangThai);
            btnChangeStatus = itemView.findViewById(R.id.btnChangeStatus);
        }
    }

    public void updateList(ArrayList<User> newList) {
        this.userList = newList;
        notifyDataSetChanged();
    }

}
