package com.topanlabs.unsoedpass;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;


public class beritaAdapter extends RecyclerView.Adapter<beritaAdapter.BeritaViewHolder> {

    private Context context;
    private List<beritaModel> dataList;
    private String namane;

    //public MahasiswaAdapter(ArrayList<matkul> dataList) {
    //  this.dataList = dataList;
    //}
    //private final LayoutInflater mInflater;
    //beritaAdapter(Context context) { mInflater = LayoutInflater.from(context); }
    public beritaAdapter(List<beritaModel> dataList, Context context) {
        this.dataList = dataList;
        this.context = context;
    }
    @Override
    public BeritaViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.row_berita, parent, false);
        return new BeritaViewHolder(view);
    }
    void setBerita(List<beritaModel> words){
        dataList = words;
        notifyDataSetChanged();
    }
    void setBerita(List<beritaModel> words, String namane2){
        dataList = words;
        namane = namane2;
        notifyDataSetChanged();
    }
    @Override
    public void onBindViewHolder(BeritaViewHolder holder, int position) {
        beritaModel current = dataList.get(position);
        holder.txtHeadline.setText(current.getHeadline());
        holder.txtKonten.setText(current.getKonten());
        holder.txtAuthor.setText(current.getAuthor());
        if (current.getAuthor().equals("Offline")){
            Glide.with(context).load(R.drawable.blackof).fitCenter().into(holder.imgHead);
        } else {
            Glide.with(context).load(current.getCover()).fitCenter().into(holder.imgHead);
        }
        holder.card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Clicked element "+current.getHeadline(), Snackbar.LENGTH_LONG).show();
                Intent i = new Intent(context, beritaView.class);
                if (namane!=null){
                    i.putExtra("judul", namane);
                }
                i.putExtra("url", current.getUrl());
                Log.d("tadigita", current.getUrl());
                context.startActivity(i);
            }
        });




    }

    @Override
    public int getItemCount() {
        return (dataList != null) ? dataList.size() : 0;
    }

    public class BeritaViewHolder extends RecyclerView.ViewHolder{
        private TextView txtHeadline, txtKonten, txtAuthor;
        private ImageView imgHead;
        private CardView card;

        public BeritaViewHolder(View itemView) {
            super(itemView);
            txtHeadline = (TextView) itemView.findViewById(R.id.headline);
            txtKonten = (TextView) itemView.findViewById(R.id.konten);
            txtAuthor = (TextView) itemView.findViewById(R.id.authore);
            imgHead = itemView.findViewById(R.id.imageView6);
            card = itemView.findViewById(R.id.card);
        }
    }
}
