package com.example.appunti.main;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appunti.data.Nota;
import com.example.appunti.R;
import com.example.appunti.listener.IonClickRecyclerListener;

import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder> {

    private IonClickRecyclerListener ionClickRecyclerListener;

    private List<Nota> notes;
    private Context context;

    public RecyclerViewAdapter(List<Nota> notes, Context context) {
        this.notes = notes;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_row, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        Nota nota = notes.get(position);
        //if(nota.getTitle() != null)
        if(!nota.getTitle().trim().isEmpty())
            holder.title.setText(nota.getTitle());
        else
            holder.title.setText("(Senza titolo)");
        //if(nota.getNote() != null)
        if(!nota.getText().trim().isEmpty())
            holder.text.setText(nota.getText());
        else
            holder.text.setText("(Senza testo)");
        holder.date.setText((nota.getDate()));
        holder.touch_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ionClickRecyclerListener.onClick(nota);
            }
        });

    }

    public void setNewNotes(List<Nota> notes){
        this.notes = notes;
    }

    public void setOnClickRecyclerListener(IonClickRecyclerListener ionClickRecyclerListener){
        this.ionClickRecyclerListener = ionClickRecyclerListener;
    }

    @Override
    public int getItemCount() {
        return notes.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        private TextView title;
        private TextView text;
        private TextView date;
        private RelativeLayout touch_layout;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            text = itemView.findViewById(R.id.text);
            date = itemView.findViewById(R.id.date);
            touch_layout = itemView.findViewById(R.id.touchLayout);
        }
    }
}
