package com.onmobile.rbt.baseline.bottomsheet.action;

import android.content.Context;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.onmobile.rbt.baseline.R;

import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by Shahbaz Akhtar on 30-11-2018.
 */

public class BSLARecyclerViewAdapter extends RecyclerView.Adapter<BSLARecyclerViewAdapter.RootViewHolder> {

    private Context mContext;
    private List<BSLAModel> list;

    private RecyclerClickListener mRecyclerClickListener;

    BSLARecyclerViewAdapter(Context context, List<BSLAModel> list) {
        this.mContext = context;
        this.list = list;
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public void onBindViewHolder(RootViewHolder holder, int position) {
        ListViewHolder listViewHolder = (ListViewHolder) holder;
        listViewHolder.setupLayout(position);
    }

    @Override
    public RootViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        return new ListViewHolder(LayoutInflater.from(mContext).inflate(R.layout.recyclerview_bottom_sheet_list_item, viewGroup, false));
    }

    public class RootViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public RootViewHolder(View v) {
            super(v);
            v.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (getAdapterPosition() >= 0)
                mRecyclerClickListener.onItemClick(getAdapterPosition(), view);
        }
    }

    public class ListViewHolder extends RootViewHolder {
        private TextView tvLabel;

        ListViewHolder(View v) {
            super(v);
            tvLabel = v.findViewById(R.id.tv_bsl_item);
        }

        void setupLayout(int position) {
            tvLabel.setText(list.get(position).getLabel());
            if (list.get(position).getIcon() != null) {
                //list.get(position).getIcon().setBounds(0, 0, 48, 48);
                tvLabel.setCompoundDrawablesWithIntrinsicBounds(list.get(position).getIcon(), null, null, null);
                tvLabel.setCompoundDrawablePadding(16);
            }
        }
    }

    public interface RecyclerClickListener {
        void onItemClick(int position, View v);
    }

    public void setOnItemClickListener(RecyclerClickListener recyclerClickListener) {
        this.mRecyclerClickListener = recyclerClickListener;
    }

}
