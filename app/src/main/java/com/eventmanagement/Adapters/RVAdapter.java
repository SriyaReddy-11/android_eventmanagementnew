package com.eventmanagement.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.eventmanagement.Entities.Event;
import com.eventmanagement.R;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class RVAdapter extends RecyclerView.Adapter<RVAdapter.EventVH> {

    public ArrayList<Event> list = new ArrayList<>();
    ClickListener clickListener;

    public interface ClickListener {
        public void onClick(int position);

        public void delete(int position);

        public void detailsPage(int position);



    }

    public void removeItem(int position) {
        notifyItemRemoved(position);
        list.remove(position);
    }

    public RVAdapter(ClickListener clickListener) {
        this.clickListener = clickListener;
    }

    public void setItems(ArrayList<Event> events) {
        this.list=events;
    }

    @NonNull
    @Override
    public EventVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.eventitems, parent, false);
        return new EventVH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EventVH holder, int position) {
        holder.eventName.setText(list.get(position).getEventName());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    public class EventVH extends RecyclerView.ViewHolder {
        public TextView eventName;
        public ImageView imageView;
        public ImageView imgedit;

        public EventVH(@NonNull View itemView) {
            super(itemView);
            eventName = itemView.findViewById(R.id.name);
            imageView = itemView.findViewById(R.id.imgdelete);
            imgedit= itemView.findViewById(R.id.imgedit);
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    clickListener.delete(getAdapterPosition());
                }
            });
            imgedit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    clickListener.onClick(getAdapterPosition());
                }
            });

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    clickListener.detailsPage(getAdapterPosition());
                }
            });



        }
    }
}
