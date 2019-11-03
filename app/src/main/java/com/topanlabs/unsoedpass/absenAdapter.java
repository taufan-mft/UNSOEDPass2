package com.topanlabs.unsoedpass;

import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;

public class absenAdapter extends RecyclerView.Adapter<absenAdapter.absenViewHolder>  {

    //private ArrayList<absen> dataList;
    private List<absendb> dataList;

    public absenAdapter(List<absendb> dataList) {
        this.dataList = dataList;
    }

    @Override
    public absenViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.row_absen, parent, false);
        return new absenViewHolder(view);
    }

    @Override
    public void onBindViewHolder(absenViewHolder holder, int position) {
        String p1 = "Pertemuan ke - " + (position + 1);
        holder.txtNama.setText(p1);
        holder.txtAbsen.setText(dataList.get(position).getHari());
        holder.txtTotal.setText(dataList.get(position).getTanggal());
        holder.txtHadir.setText(dataList.get(position).getKehadiran());

    }

    @Override
    public int getItemCount() {
        return (dataList != null) ? dataList.size() : 0;
    }

    public class absenViewHolder extends RecyclerView.ViewHolder{
        private TextView txtNama, txtAbsen, txtTotal, txtHadir;

        public absenViewHolder(View itemView) {
            super(itemView);
            txtNama = (TextView) itemView.findViewById(R.id.txt_namatkul);
            txtAbsen = (TextView) itemView.findViewById(R.id.txt_hari);
            txtTotal = (TextView) itemView.findViewById(R.id.txt_tanggal);
            txtHadir = (TextView) itemView.findViewById(R.id.txt_hadir);
            //txtJam = (TextView) itemView.findViewById(R.id.txt_jam);
        }
    }
}
