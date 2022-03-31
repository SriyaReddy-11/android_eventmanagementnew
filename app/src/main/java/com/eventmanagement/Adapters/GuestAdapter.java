package com.eventmanagement.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.eventmanagement.Entities.Event;
import com.eventmanagement.Entities.EventStudent;
import com.eventmanagement.R;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class GuestAdapter extends RecyclerView.Adapter<GuestAdapter.GuestVH>{
    public ArrayList<EventStudent> list = new ArrayList<>();
    ClickListener clickListener;
    public GuestAdapter(ClickListener clickListener){
this.clickListener=clickListener;
    }

    public void setItems(ArrayList<EventStudent> events) {
        this.list=events;
    }

    public interface ClickListener {
        public void delete(int position);

    }

    public void removeItem(int position) {
        notifyItemRemoved(position);
        list.remove(position);
    }
    @NonNull
    @Override
    public GuestVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.eventitems, parent, false);
        return new GuestVH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GuestVH holder, int position) {
        holder.eventName.setText(list.get(position).getCustomerName());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class GuestVH extends RecyclerView.ViewHolder{
        public TextView eventName;
        public ImageView imageView;
        public ImageView imgedit;
        public GuestVH(@NonNull View itemView) {

            super(itemView);

            eventName = itemView.findViewById(R.id.name);
            imageView = itemView.findViewById(R.id.imgdelete);
            imgedit= itemView.findViewById(R.id.imgedit);

            imgedit.setVisibility(View.GONE);

            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    clickListener.delete(getAdapterPosition());
                }
            });

        }


    }
}
