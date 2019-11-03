package com.topanlabs.unsoedpass;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

public class absenSelAdapter  extends RecyclerView.Adapter<absenSelAdapter.absenSelViewHolder> {

    private Context context;
    private List<matkuldb> dataList;

    //public MahasiswaAdapter(ArrayList<matkul> dataList) {
    //  this.dataList = dataList;
    //}
    //private final LayoutInflater mInflater;
    //beritaAdapter(Context context) { mInflater = LayoutInflater.from(context); }
    public absenSelAdapter(List<matkuldb> dataList, Context context) {
        this.dataList = dataList;
        this.context = context;
    }
    @Override
    public absenSelAdapter.absenSelViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.row_absenselect, parent, false);
        return new absenSelAdapter.absenSelViewHolder(view);
    }
    void setBerita(List<matkuldb> words){
        dataList = words;
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(absenSelAdapter.absenSelViewHolder holder, int position) {
        matkuldb current = dataList.get(position);
        holder.txtHeadline.setText(current.getNamakul());

        holder.card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(view.getContext(), cekAbsen.class);
                i.putExtra("Matkul",holder.txtHeadline.getText());
                view.getContext().startActivity(i);

            }
        });




    }

    @Override
    public int getItemCount() {
        return (dataList != null) ? dataList.size() : 0;
    }

    public class absenSelViewHolder extends RecyclerView.ViewHolder{
        private TextView txtHeadline;
        private CardView card;
        public absenSelViewHolder(View itemView) {
            super(itemView);
            txtHeadline = (TextView) itemView.findViewById(R.id.txt_namatkul);
            card = itemView.findViewById(R.id.card);

        }
    }
}