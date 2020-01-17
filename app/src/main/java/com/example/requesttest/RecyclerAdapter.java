package com.example.requesttest;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder>{

    JSONArray produkti;

    public RecyclerAdapter(JSONArray produkti) {
        this.produkti = produkti;
    }

    private static final String TAG = "RecyclerAdapter";
    int count = 0;

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        Log.i(TAG, "onCreateViewHolder: " + count++);

        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.row_item, parent, false);

        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        JSONObject produkt;
        String title = null;
        String url = null;
        double cena_roda, cena_uni, cena_idea, cena_tempo, cena_maxi;

        try
        {
            produkt = produkti.getJSONObject(position);
            title = produkt.getString("name");
            url = produkt.getString("url");
            cena_roda = produkt.getDouble("cena_roda");
            cena_uni = produkt.getDouble("cena_uni");
            cena_idea = produkt.getDouble("cena_idea");
            cena_tempo = produkt.getDouble("cena_temp");
            cena_maxi = produkt.getDouble("cena_maxi");
            Log.i(TAG, "onBindViewHolder: " + produkt.toString());
        }
        catch (Exception e)
        {
            Toast toast = Toast.makeText(holder.imageView.getContext(), "Proverite internet konekciju", Toast.LENGTH_SHORT);
            toast.show();
        }


        holder.rowCountTextView.setText(String.valueOf(position));
        holder.textViewTitle.setText(title);
        Picasso.with(holder.imageView.getContext()).load(url).into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        if(produkti != null)
            return produkti.length();
        else
            return 100;
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        TextView textViewTitle, rowCountTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.imageViewThumbnail);
            textViewTitle = itemView.findViewById(R.id.textViewTitle);
            rowCountTextView = itemView.findViewById(R.id.rowCountTextView);
        }

    }

}
