package com.topanlabs.unsoedpass.memo;

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
import com.topanlabs.unsoedpass.R;
import com.topanlabs.unsoedpass.kelaspenggantidb.kelaspengganti;
import com.topanlabs.unsoedpass.memo.memoent;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class memoAdapter extends RecyclerView.Adapter<memoAdapter.memoViewHolder> {

    private Context context;
    private List<memoent> dataList;

    //public MahasiswaAdapter(ArrayList<matkul> dataList) {
    //  this.dataList = dataList;
    //}
    //private final LayoutInflater mInflater;
    //beritaAdapter(Context context) { mInflater = LayoutInflater.from(context); }
    public memoAdapter(List<memoent> dataList, Context context) {
        this.dataList = dataList;
        this.context = context;
    }
    @Override
    public memoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.row_memo, parent, false);
        return new memoViewHolder(view);
    }
    void setKelas(List<memoent> words){
        dataList = words;
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(memoViewHolder holder, int position) {
        memoent current = dataList.get(position);
        holder.txtNamatkul.setText(current.getNamakul());
        holder.txtJam.setText(current.getJam());
        holder.txtRuangan.setText(current.getRuangan());
        holder.txtTanggal.setText(current.getTanggal());
        holder.txtdesc.setText(current.getCatatan());
        holder.txtJenis.setText(current.getJenis());




    }

    @Override
    public int getItemCount() {
        return (dataList != null) ? dataList.size() : 0;
    }

    public class memoViewHolder extends RecyclerView.ViewHolder{
        private TextView txtNamatkul, txtJam, txtRuangan, txtTanggal, txtJenis, txtdesc;


        public memoViewHolder(View itemView) {
            super(itemView);
            txtNamatkul = (TextView) itemView.findViewById(R.id.txt_namatkul);
            txtJam = (TextView) itemView.findViewById(R.id.txt_jam);
            txtRuangan = (TextView) itemView.findViewById(R.id.txt_ruang);
            txtTanggal = itemView.findViewById(R.id.txt_tanggal);
            txtJenis= itemView.findViewById(R.id.txt_jenis);
            txtdesc= itemView.findViewById(R.id.txt_shortdesc);
        }
    }
}
