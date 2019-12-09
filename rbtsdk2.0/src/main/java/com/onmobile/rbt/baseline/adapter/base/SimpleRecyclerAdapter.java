package com.onmobile.rbt.baseline.adapter.base;

import android.content.Context;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.onmobile.rbt.baseline.holder.RootViewHolder;
import com.onmobile.rbt.baseline.listener.OnItemClickListener;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by Shahbaz Akhtar on 09/11/2018.
 *
 * @author Shahbaz Akhtar
 */
public abstract class SimpleRecyclerAdapter<T extends RootViewHolder, D> extends RecyclerView.Adapter<T> {

    public interface AdapterInternalCallback<T, D> {
        void reloadData();
    }

    protected abstract T onSimpleCreateViewHolder(@NonNull ViewGroup parent, int viewType);

    protected abstract void onSimpleBindViewHolder(@NonNull final T holder, final int position);

    public void register() { }

    public void unregister() { }

    private Context mContext;
    private LayoutInflater mInflater;

    private List<D> mList;
    private OnItemClickListener<D> mListener;

    public SimpleRecyclerAdapter(@NonNull List<D> list) {
        this.mList = list;
    }

    public SimpleRecyclerAdapter(@NonNull List<D> list, OnItemClickListener<D> listener) {
        this.mList = list;
        this.mListener = listener;
    }

    @NonNull
    @Override
    public T onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (mInflater == null) {
            mContext = parent.getContext();
            mInflater = LayoutInflater.from(parent.getContext());
        }
        return onSimpleCreateViewHolder(parent, viewType);
    }

    @Override
    public void onBindViewHolder(@NonNull final T holder, final int position) {
        onSimpleBindViewHolder(holder, position);
    }

    @Override
    public int getItemCount() {
        return mList == null ? 0 : mList.size();
    }

    public Context getContext() {
        return mContext;
    }

    public LayoutInflater getInflater() {
        return mInflater;
    }

    public List<D> getList() {
        return mList;
    }

    public void setListener(OnItemClickListener<D> listener) {
        this.mListener = listener;
    }

    public OnItemClickListener<D> getListener() {
        return mListener;
    }
}
