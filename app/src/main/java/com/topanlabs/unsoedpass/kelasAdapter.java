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

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
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
        SimpleDateFormat formatterTanggal = new SimpleDateFormat("dd-MM-yyyy");
        Date waktu1 = new Date();
        try {
            waktu1 = formatterTanggal.parse(current.getTanggal());
        } catch (Exception e) {
            //
        }
        Calendar c = Calendar.getInstance();
        c.setTime(waktu1);
        int day = c.get(Calendar.DAY_OF_WEEK);
        String harini = "o";
        switch (day) {
            case Calendar.SUNDAY:
                harini = "MINGGU";
                break;
            case Calendar.MONDAY:
                harini = "SENIN";
                break;
            case Calendar.TUESDAY:
                harini = "SELASA";
                break;
            case Calendar.WEDNESDAY:
                harini = "RABU";
                break;
            case Calendar.THURSDAY:
                harini = "KAMIS";
                break;
            case Calendar.FRIDAY:
                harini = "JUMAT";
                break;
            case Calendar.SATURDAY:
                harini = "SABTU";
                break;

        }
        holder.txtHari.setText(harini);



    }

    @Override
    public int getItemCount() {
        return (dataList != null) ? dataList.size() : 0;
    }

    public class kelasViewHolder extends RecyclerView.ViewHolder{
        private TextView txtNamatkul, txtJam, txtRuangan, txtTanggal, txtHari;


        public kelasViewHolder(View itemView) {
            super(itemView);
            txtNamatkul = (TextView) itemView.findViewById(R.id.txt_namatkul);
            txtJam = (TextView) itemView.findViewById(R.id.txt_jam);
            txtRuangan = (TextView) itemView.findViewById(R.id.txt_ruangan);
            txtTanggal = itemView.findViewById(R.id.txt_tanggal);
        txtHari = itemView.findViewById(R.id.txt_hari);
        }
    }
}
