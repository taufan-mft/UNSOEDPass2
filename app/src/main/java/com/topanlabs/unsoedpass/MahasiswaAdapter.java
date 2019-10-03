package com.topanlabs.unsoedpass;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


public class MahasiswaAdapter extends RecyclerView.Adapter<MahasiswaAdapter.MahasiswaViewHolder> {


    private List<matkuldb> dataList;

    //public MahasiswaAdapter(ArrayList<matkul> dataList) {
      //  this.dataList = dataList;
    //}
    private final LayoutInflater mInflater;
    MahasiswaAdapter(Context context) { mInflater = LayoutInflater.from(context); }
    @Override
    public MahasiswaViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.row_matkul, parent, false);
        return new MahasiswaViewHolder(view);
    }
    void setWords(List<matkuldb> words){
        dataList = words;
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(MahasiswaViewHolder holder, int position) {
        matkuldb current = dataList.get(position);
        holder.txtNama.setText(current.getNamakul());
        holder.txtNpm.setText(current.getRuangan());
        holder.txtNoHp.setText(current.getHari());
        holder.txtJam.setText(current.getJam().replaceAll("..", "$0:"));
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
