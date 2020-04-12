package com.yvo.genrateurdattestationdplacement;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class AdapterMotifList extends RecyclerView.Adapter<AdapterMotifList.ViewHolder> {

    private String[] motifs;
    private String[] motifs_long;
    private String[] motifs_court;
    private ArrayList<String> lstChk= new ArrayList<>();

    private LayoutInflater mInflater;
    private Context context;

    AdapterMotifList(Context context, String[] motifs, String[] motifs_long, String[] motifs_court) {
        try {
            this.mInflater = LayoutInflater.from(context);
            this.context = context;
            this.motifs = motifs;
            this.motifs_long = motifs_long;
            this.motifs_court = motifs_court;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        CheckBox cbSelect;
        ImageView infoButton;

        ViewHolder(View v) {
            super(v);
            this.cbSelect = (CheckBox) v.findViewById(R.id.cbselect);
            this.infoButton = (ImageView) v.findViewById(R.id.infoButton);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.cvmotifs, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        holder.cbSelect.setText(motifs[position]);
        holder.cbSelect.setOnClickListener(v -> {
            String motif = motifs_court[position];

            if (!lstChk.contains(motif))
            {
                lstChk.add(motif);
            }
            else
            {
                lstChk.remove(motif);
            }

        });
        holder.infoButton.setOnClickListener(v -> new AlertDialog.Builder(context)
                .setTitle("Description motif")
                .setMessage(motifs_long[position])
        .show());
    }

    @Override
    public int getItemCount() {
        return motifs.length;
    }

    String getCheckedAsString()
    {
        return lstChk.toString().replace("[", "").replace("]", "").replace(", ", "-");
    }

    ArrayList<String> getChecked()
    {
        return lstChk;
    }
}
