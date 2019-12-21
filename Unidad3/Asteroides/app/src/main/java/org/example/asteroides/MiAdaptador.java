package org.example.asteroides;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MiAdaptador extends RecyclerView.Adapter<MiAdaptador.ViewHolder> {
    protected View.OnClickListener onClickListener;
    private LayoutInflater inflador;
    private List<String> lista;

    public MiAdaptador(Context context, List<String> lista) {
        inflador = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.lista = lista;
    }


    @Override
    public ViewHolder onCreateViewHolder( ViewGroup parent, int viewType) {
        View v = inflador.inflate(R.layout.elemento_lista, parent, false);
        v.setOnClickListener(onClickListener);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder( ViewHolder holder, int position) {
        holder.titulo.setText(lista.get(position));
        switch (Math.round((float) Math.random()*3)){
            case 0: holder.icon.setImageResource(R.drawable.ic_asteroide1);
                break;
            case 1: holder.icon.setImageResource(R.drawable.ic_asteroide2);
                break;
            default: holder.icon.setImageResource(R.drawable.ic_asteroide3);
                break;
        }

    }

    @Override
    public int getItemCount() {
        return lista.size();
    }




    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView titulo, subtitutlo;
        public ImageView icon;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            titulo = itemView.findViewById(R.id.titulo);
            subtitutlo = itemView.findViewById(R.id.subtitulo);
            icon = itemView.findViewById(R.id.icono);
        }
    }

    public void setOnClickListener(View.OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }
}
