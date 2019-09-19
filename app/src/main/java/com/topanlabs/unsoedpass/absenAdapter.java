package com.topanlabs.unsoedpass;

import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import java.util.ArrayList;
public class absenAdapter extends RecyclerView.Adapter<absenAdapter.absenViewHolder>  {

    private ArrayList<absen> dataList;

    public absenAdapter(ArrayList<absen> dataList) {
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
        holder.txtNama.setText(dataList.get(position).getNamatkul());
        holder.txtAbsen.setText(dataList.get(position).getAbsen());
        holder.txtTotal.setText(dataList.get(position).getTotpertemuan());

    }

    @Override
    public int getItemCount() {
        return (dataList != null) ? dataList.size() : 0;
    }

    public class absenViewHolder extends RecyclerView.ViewHolder{
        private TextView txtNama, txtAbsen, txtTotal, txtJam;

        public absenViewHolder(View itemView) {
            super(itemView);
            txtNama = (TextView) itemView.findViewById(R.id.txt_namatkul);
            txtAbsen = (TextView) itemView.findViewById(R.id.txt_absen);
            txtTotal = (TextView) itemView.findViewById(R.id.txt_total);
            //txtJam = (TextView) itemView.findViewById(R.id.txt_jam);
        }
    }
}
