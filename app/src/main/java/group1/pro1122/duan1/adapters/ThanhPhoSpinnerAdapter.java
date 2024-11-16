package group1.pro1122.duan1.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import group1.pro1122.duan1.R;
import group1.pro1122.duan1.models.DiaDiem;

public class ThanhPhoSpinnerAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<DiaDiem> list;

    public ThanhPhoSpinnerAdapter(Context context, ArrayList<DiaDiem> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = ((Activity) context).getLayoutInflater();
        convertView = inflater.inflate(R.layout.spinner_thanhpho, parent, false);

        //Mapping
        TextView tvThanhPho = convertView.findViewById(R.id.tvThanhPho);

        //Set data
        tvThanhPho.setText(list.get(position).getThanhPho());

        return convertView;
    }
}
