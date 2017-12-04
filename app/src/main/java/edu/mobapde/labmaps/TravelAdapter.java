package edu.mobapde.labmaps;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by student on 04/12/2017.
 */

public class TravelAdapter extends RecyclerView.Adapter<TravelAdapter.TravelViewHolder>{

    private ArrayList<Travel> travelList;
    private OnItemClickListener onItemClickListener;

    View previousClickedView = null;

    public TravelAdapter(ArrayList<Travel> travelList, OnItemClickListener onItemClickListener){
        this.travelList = travelList;
        this.onItemClickListener = onItemClickListener;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public TravelViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_travel, parent, false);
        return new TravelViewHolder(v);
    }

    @Override
    public void onBindViewHolder(TravelViewHolder holder, int position) {
        Travel travel = travelList.get(position);
        holder.tvPlace.setText(travel.getPlace());

        holder.itemView.setTag(travel);
        holder.itemView.setTag(R.id.key_list_position, position);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickListener.onItemClick((Travel) v.getTag());
            }
        });
    }

    @Override
    public int getItemCount() {
        return travelList.size();
    }

    public class TravelViewHolder extends RecyclerView.ViewHolder{

        TextView tvPlace;

        public TravelViewHolder(View itemView) {
            super(itemView);
            tvPlace = itemView.findViewById(R.id.tv_place);
        }
    }

    public interface OnItemClickListener{
        public void onItemClick(Travel travel);
    }
}
