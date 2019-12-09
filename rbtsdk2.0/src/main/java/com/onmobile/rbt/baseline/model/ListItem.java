package com.onmobile.rbt.baseline.model;

import android.text.TextUtils;

import com.onmobile.baseline.http.api_action.dtos.RingBackToneDTO;
import com.onmobile.baseline.http.retrofit_io.APIRequestParameters;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;

/**
 * Created by Shahbaz Akhtar on 19/09/2018.
 *
 * @author Shahbaz Akhtar
 */
public class ListItem implements Serializable {

    public static final int LIMIT_PER_PAGER_LIST = 4;

    private String title;
    private Object parent;
    private List<RingBackToneDTO> bulkItems;
    private List<RingBackToneDTO> items;

    public ListItem(Object parent) {
        this.parent = parent != null ? parent : new RingBackToneDTO();
        this.items = new ArrayList<>();
        this.bulkItems = new ArrayList<>();
        try {
            if (parent instanceof RingBackToneDTO) {
                this.bulkItems.addAll(((RingBackToneDTO) this.parent).getItems());
                this.items.addAll(((RingBackToneDTO) this.parent).getItems());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ListItem(Object parent, List<RingBackToneDTO> items) {
        this.parent = parent != null ? parent : new RingBackToneDTO();
        this.items = new ArrayList<>(items);
        this.bulkItems = new ArrayList<>(items);
    }

    public Object getParent() {
        return parent;
    }

    public void setParent(@NonNull RingBackToneDTO parent) {
        this.parent = parent;
    }

    public List<RingBackToneDTO> getItems() {
        if (items == null)
            items = new ArrayList<>();
        return items;
    }

    public void setItems(List<RingBackToneDTO> items) {
        this.items = items;
    }

    public void addItem(RingBackToneDTO item) {
        items.add(item);
    }

    public int itemCount() {
        return items == null ? 0 : items.size();
    }

    public List<RingBackToneDTO> getBulkItems() {
        return bulkItems.size() > items.size() ? bulkItems : items;
    }

    public void setBulkItems(List<RingBackToneDTO> bulkItems) {
        this.bulkItems = bulkItems;
    }

    public void addBulkItem(RingBackToneDTO item) {
        bulkItems.add(item);
    }

    public int bulkItemCount() {
        return bulkItems == null ? 0 : bulkItems.size();
    }

    /**
     * Search an item in bulkItem and return the position of that item from bulkList
     *
     * @param defaultPosition //Pass a default position. If item didn't available then method will return the same position
     * @param item            //Pass the item to compare
     * @return bulkPosition
     */
    public int getBulkPosition(int defaultPosition, RingBackToneDTO item) {
        int bulkPosition = defaultPosition;
        if (item != null && bulkItems != null) {
            int counter = 0;
            for (RingBackToneDTO bulkItem : bulkItems) {
                if (item.equals(bulkItem)) {
                    bulkPosition = counter;
                    break;
                }
                counter++;
            }
        }
        return bulkPosition;
    }

    /**
     * Return the biggest list item, either bulkItems or items.
     *
     * @return List<RingBackToneDTO>
     */
    public List<RingBackToneDTO> getBigItemList() {
        return (bulkItems.size() > items.size()) ? bulkItems : items;
    }

    /**
     * Create a filtered list and add correspondence type of item into that
     *
     * @param type Item type need to be added into the list
     * @return List<RingBackToneDTO> filtered items
     */
    public List<RingBackToneDTO> getFilteredItems(APIRequestParameters.EMode type) {
        List<RingBackToneDTO> filteredItems = new ArrayList<>();
        for (RingBackToneDTO item : items) {
            if (item != null && !TextUtils.isEmpty(item.getType()) && item.getType().equals(type.value()))
                filteredItems.add(item);
        }
        return filteredItems;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
