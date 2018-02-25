package com.delivery.factory.deliveryfactory.Adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.signature.StringSignature;
import com.delivery.factory.deliveryfactory.ModelPOJO.MenuListModel;
import com.delivery.factory.deliveryfactory.R;

import java.util.Collections;
import java.util.List;


/**
 * Created by vimoxpc on 25-Feb-18.
 */
public class MenuListAdapter extends RecyclerView.Adapter<MenuListAdapter.View_Holder> {

    List<MenuListModel> list = Collections.emptyList();
    Context  context;

    public MenuListAdapter(List<MenuListModel> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public View_Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        //Inflate the layout, initialize the View Holder
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_recyclerview, parent, false);
        View_Holder holder = new View_Holder(v);
        return holder;

    }

    @Override
    public void onBindViewHolder(View_Holder holder, int position) {

        //Use the provided View Holder on the onCreateViewHolder method to populate the current row on the RecyclerView
        holder.title.setText(list.get(position).getName());
        holder.description.setText(list.get(position).getDesciption());

        Glide.with(context)
                .load(list.get(position).getImageID())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .dontAnimate()
                .skipMemoryCache(true)
                .signature(new StringSignature(String.valueOf(System.currentTimeMillis())))
                .error(R.drawable.ic_menu)
                .into(holder.imageView);

        //animate(holder);

    }

    @Override
    public int getItemCount() {
        //returns the number of elements the RecyclerView will display
        return list.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    // Insert a new item to the RecyclerView on a predefined position
    public void insert(int position, MenuListModel data) {
        list.add(position, data);
        notifyItemInserted(position);
    }

    // Remove a RecyclerView item containing a specified MenuListModel object
    public void remove(MenuListModel data) {
        int position = list.indexOf(data);
        list.remove(position);
        notifyItemRemoved(position);
    }

    public class View_Holder extends RecyclerView.ViewHolder {

        CardView cv;
        TextView title;
        TextView description;
        ImageView imageView;

        View_Holder(View itemView) {
            super(itemView);
            cv = (CardView) itemView.findViewById(R.id.cardView);
            title = (TextView) itemView.findViewById(R.id.title);
            description = (TextView) itemView.findViewById(R.id.description);
            imageView = (ImageView) itemView.findViewById(R.id.imageView);
        }
    }

}
