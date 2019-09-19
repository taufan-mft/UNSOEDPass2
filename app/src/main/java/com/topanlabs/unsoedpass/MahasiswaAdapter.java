package com.topanlabs.unsoedpass;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;


public class MahasiswaAdapter extends RecyclerView.Adapter<MahasiswaAdapter.MahasiswaViewHolder> {


    private ArrayList<matkul> dataList;

    public MahasiswaAdapter(ArrayList<matkul> dataList) {
        this.dataList = dataList;
    }

    @Override
    public MahasiswaViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.row_matkul, parent, false);
        return new MahasiswaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MahasiswaViewHolder holder, int position) {
        holder.txtNama.setText(dataList.get(position).getNamatkul());
        holder.txtNpm.setText(dataList.get(position).getDosen());
        holder.txtNoHp.setText(dataList.get(position).getHari());
        holder.txtJam.setText(dataList.get(position).getJam());
    }

    @Override
    public int getItemCount() {
        return (dataList != null) ? dataList.size() : 0;
    }

    public class MahasiswaViewHolder extends RecyclerView.ViewHolder{
        private TextView txtNama, txtNpm, txtNoHp, txtJam;

        public MahasiswaViewHolder(View itemView) {
            super(itemView);
            txtNama = (TextView) itemView.findViewById(R.id.txt_namatkul);
            txtNpm = (TextView) itemView.findViewById(R.id.txt_dosen);
            txtNoHp = (TextView) itemView.findViewById(R.id.txt_hari);
            txtJam = (TextView) itemView.findViewById(R.id.txt_jam);
        }
    }
}
