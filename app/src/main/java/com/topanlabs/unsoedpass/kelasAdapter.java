package com.topanlabs.unsoedpass;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.snackbar.Snackbar;
import com.topanlabs.unsoedpass.kelaspenggantidb.kelaspengganti;

import java.util.List;

public class kelasAdapter extends RecyclerView.Adapter<kelasAdapter.kelasViewHolder> {

    private Context context;
    private List<kelaspengganti> dataList;

    //public MahasiswaAdapter(ArrayList<matkul> dataList) {
    //  this.dataList = dataList;
    //}
    //private final LayoutInflater mInflater;
    //beritaAdapter(Context context) { mInflater = LayoutInflater.from(context); }
    public kelasAdapter(List<kelaspengganti> dataList, Context context) {
        this.dataList = dataList;
        this.context = context;
    }
    @Override
    public kelasViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.row_kelaspeng, parent, false);
        return new kelasViewHolder(view);
    }
    void setKelas(List<kelaspengganti> words){
        dataList = words;
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(kelasViewHolder holder, int position) {
        kelaspengganti current = dataList.get(position);
        holder.txtNamatkul.setText(current.getNamakul());
        holder.txtJam.setText(current.getJam());
        holder. txtRuangan.setText(current.getRuangan());
        holder.txtTanggal.setText(current.getTanggal());





    }

    @Override
    public int getItemCount() {
        return (dataList != null) ? dataList.size() : 0;
    }

    public class kelasViewHolder extends RecyclerView.ViewHolder{
        private TextView txtNamatkul, txtJam, txtRuangan, txtTanggal;


        public kelasViewHolder(View itemView) {
            super(itemView);
            txtNamatkul = (TextView) itemView.findViewById(R.id.txt_namatkul);
            txtJam = (TextView) itemView.findViewById(R.id.txt_jam);
            txtRuangan = (TextView) itemView.findViewById(R.id.txt_ruangan);
            txtTanggal = itemView.findViewById(R.id.txt_tanggal);

        }
    }
}
