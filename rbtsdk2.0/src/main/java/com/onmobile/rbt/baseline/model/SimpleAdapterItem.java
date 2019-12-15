package com.onmobile.rbt.baseline.model;

import android.text.TextUtils;

import com.onmobile.rbt.baseline.http.api_action.dtos.RingBackToneDTO;
import com.onmobile.rbt.baseline.http.retrofit_io.APIRequestParameters;
import com.onmobile.rbt.baseline.util.FunkyAnnotation;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;

/**
 * Created by Shahbaz Akhtar on 19/09/2018.
 *
 * @author Shahbaz Akhtar
 */
public class SimpleAdapterItem implements Serializable {

    private String id;
    private String title;
    private Object parent;
    @FunkyAnnotation.SimpleAdapterItemTypes
    private int itemType;
    private List<Object> items;

    public SimpleAdapterItem(@FunkyAnnotation.SimpleAdapterItemTypes int itemType,
                             String id, String title, Object parent) {
        this.itemType = itemType;
        this.id = id;
        this.title = title;
        this.parent = parent != null ? parent : new RingBackToneDTO();
        this.items = new ArrayList<>();
    }

    public SimpleAdapterItem(@FunkyAnnotation.SimpleAdapterItemTypes int itemType,
                             String id, String title, Object parent, List<Object> items) {
        this.itemType = itemType;
        this.id = id;
        this.title = title;
        this.parent = parent != null ? parent : new RingBackToneDTO();
        this.items = items;
    }

    public Object getParent() {
        return parent;
    }

    public void setParent(@NonNull RingBackToneDTO parent) {
        this.parent = parent;
    }

    public List<Object> getItems() {
        if (items == null)
            items = new ArrayList<>();
        return items;
    }

    public void setItems(List<Object> items) {
        this.items = items;
    }

    public void addItem(RingBackToneDTO item) {
        items.add(item);
    }

    public int itemCount() {
        return items == null ? 0 : items.size();
    }

    public int getItemType() {
        return itemType;
    }

    public void setItemType(int itemType) {
        this.itemType = itemType;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
