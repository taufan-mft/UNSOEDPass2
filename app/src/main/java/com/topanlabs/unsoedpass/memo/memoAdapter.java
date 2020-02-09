package com.topanlabs.unsoedpass.memo;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
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
import com.topanlabs.unsoedpass.memoDetail;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static android.graphics.Color.parseColor;

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
   public void setKelas(List<memoent> words){
        dataList = words;
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(memoViewHolder holder, int position) {
        memoent current = dataList.get(position);
        holder.txtNamatkul.setText(current.getNamakul());
        holder.txtJam.setText(current.getJam());
        holder.txtRuangan.setText(current.getRuangan());
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
                harini = "Minggu,";
                break;
            case Calendar.MONDAY:
                harini = "Senin,";
                break;
            case Calendar.TUESDAY:
                harini = "Selasa,";
                break;
            case Calendar.WEDNESDAY:
                harini = "Rabu,";
                break;
            case Calendar.THURSDAY:
                harini = "Kamis,";
                break;
            case Calendar.FRIDAY:
                harini = "Jumat,";
                break;
            case Calendar.SATURDAY:
                harini = "Sabtu,";
                break;

        }
        holder.txthari.setText(harini);
        holder.txtTanggal.setText(current.getTanggal());
        String holid;
        if (current.getCatatan().length()>18){
            holid = current.getCatatan().substring(0,17);
            holid = holid + "...";
        } else {
            holid = current.getCatatan();
        }
        holder.txtdesc.setText(holid);
        holder.txtJenis.setText(current.getJenis());
        if (current.getJenis().equals("Kuis")) {
            Log.d("zafarin","ohyeah");
            holder.cview.setCardBackgroundColor(parseColor("#DF2C2C"));
            holder.txtNamatkul.setTextColor(parseColor("#FFFFFF"));
            holder.txtJam.setTextColor(parseColor("#FFFFFF"));
            holder.txtRuangan.setTextColor(parseColor("#FFFFFF"));
            holder.txtTanggal.setTextColor(parseColor("#FFFFFF"));
            holder.txtdesc.setTextColor(parseColor("#FFFFFF"));
            holder.txtJenis.setTextColor(parseColor("#FFFFFF"));
            holder.txthari.setTextColor(parseColor("#FFFFFF"));
        }
    final String buatl = harini;
    holder.cview.setOnClickListener((View v)-> {
        Intent i = new Intent(context, memoDetail.class);
        i.putExtra("matkul", current.getNamakul());
        i.putExtra("jam", current.getJam());
        i.putExtra("ruangan", current.getRuangan());
        i.putExtra("tanggal",buatl +" "+current.getTanggal());
        i.putExtra("catatan", current.getCatatan());
        i.putExtra("jenis", current.getJenis());
        i.putExtra("id", Integer.toString(current.getIdmemo()));
        Log.d("zhafarin", Integer.toString(current.getIdmemo()));
        context.startActivity(i);

    });


    }

    @Override
    public int getItemCount() {
        return (dataList != null) ? dataList.size() : 0;
    }

    public class memoViewHolder extends RecyclerView.ViewHolder{
        private TextView txtNamatkul, txtJam, txtRuangan, txtTanggal, txtJenis, txtdesc, txthari;
        private CardView cview;


        public memoViewHolder(View itemView) {
            super(itemView);
            cview = itemView.findViewById(R.id.card);
            txtNamatkul = (TextView) itemView.findViewById(R.id.txt_namatkul);
            txtJam = (TextView) itemView.findViewById(R.id.txt_jam);
            txtRuangan = (TextView) itemView.findViewById(R.id.txt_ruang);
            txtTanggal = itemView.findViewById(R.id.txt_tanggal);
            txtJenis= itemView.findViewById(R.id.txt_jenis);
            txtdesc= itemView.findViewById(R.id.txt_shortdesc);
            txthari = itemView.findViewById(R.id.txt_hari);
        }
    }
}
