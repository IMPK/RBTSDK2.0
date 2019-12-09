package com.onmobile.rbt.baseline.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;
import com.onmobile.rbt.baseline.R;
import com.onmobile.rbt.baseline.adapter.base.SimpleRecyclerAdapter;
import com.onmobile.rbt.baseline.holder.RootViewHolder;
import com.onmobile.rbt.baseline.listener.OnItemClickListener;
import com.onmobile.rbt.baseline.model.ContactModelDTO;
import com.onmobile.rbt.baseline.util.Util;
import com.onmobile.rbt.baseline.widget.RecyclerViewFastScroller;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatCheckBox;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.core.util.Pair;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


public class ContactsRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements RecyclerViewFastScroller.BubbleTextGetter {

    private Context context;
    private ArrayList<ContactModelDTO> mContactsArrayList;
    private ArrayList<ContactModelDTO> mOriginalContactsArrayList;
    private LayoutInflater inflater;
    public int selectedIndex = -1;

    private int VIEW_TYPE_ALREADY_SET = 0;

    private AlreadySetAdapter mAlreadySetAdapter;
    private List<AdapterData> mAlreadySetData;

    @Override
    public long getItemId(int position) {
        return position != 0 ? 1 : VIEW_TYPE_ALREADY_SET;
    }


    public ContactsRecyclerAdapter(Context mContext, ArrayList<ContactModelDTO> list, ArrayList<AdapterData> alreadySetDataList) {
        this.context = mContext;
        this.mContactsArrayList = list;
        mAlreadySetData = alreadySetDataList;
        //setHasStableIds(false);
        inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView;
        RecyclerView.ViewHolder viewHolder = null;
        if (viewType != VIEW_TYPE_ALREADY_SET) {
            itemView = inflater.inflate(R.layout.contacts_row, parent, false);
            viewHolder = new ContentViewHolder(itemView);
        } else {
            itemView = inflater.inflate(R.layout.contacts_row_rec, parent, false);
            viewHolder = new ContentViewHolderAlreadySet(itemView);
        }

        //viewHolder.setIsRecyclable(false);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (position > 0) {
            getContentView((ContentViewHolder) holder, position);
        } else {
            ContentViewHolderAlreadySet alreadySetHolder = (ContentViewHolderAlreadySet) holder;
            if (mAlreadySetData != null && mAlreadySetData.size() > 0) {
                alreadySetHolder.recyclerView.setVisibility(View.VISIBLE);
                mAlreadySetAdapter = new AlreadySetAdapter(mAlreadySetData, new OnItemClickListener<AdapterData>() {
                    @SafeVarargs
                    @Override
                    public final void onItemClick(View view, AdapterData data, int position, Pair<View, String>... sharedElements) {
                        mAlreadySetData.get(position).setSelected(!data.isSelected());
                        alreadySetHolder.recyclerView.post(() -> mAlreadySetAdapter.notifyItemChanged(position));
                    }
                });
                alreadySetHolder.recyclerView.setHasFixedSize(true);
                alreadySetHolder.recyclerView.setItemAnimator(null);
                alreadySetHolder.recyclerView.setItemViewCacheSize(10);
                alreadySetHolder.recyclerView.setLayoutManager(new LinearLayoutManager(context));
                alreadySetHolder.recyclerView.setAdapter(mAlreadySetAdapter);
            } else {
                alreadySetHolder.recyclerView.setVisibility(View.GONE);
            }
        }
    }


    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position, @NonNull List<Object> payloads) {
        if (!payloads.isEmpty()) {
            boolean isChecked = (boolean) payloads.get(0);
            ContentViewHolder holder = (ContentViewHolder) viewHolder;
            if (isChecked) {
                holder.radioButton.setChecked(true);
            } else {
                holder.radioButton.setChecked(false);
            }
        } else {
            super.onBindViewHolder(viewHolder, position, payloads);
        }
    }

    private void getContentView(final ContentViewHolder viewHolder, final int position) {
        final ContactModelDTO contactModelDTO = getItem(position);
        if (contactModelDTO != null) {

            /*if (isPresentInSetList(contactModelDTO.getMobileNumber())) {
                viewHolder.mRootRelativelayout.setVisibility(View.GONE);
                return;
            }*/

            Glide.with(context)
                    .load(contactModelDTO.getPhotoURI())
                    .transform(new RoundTransform(context))
                    .placeholder(R.drawable.ic_contct_selectn_icon)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .error(R.drawable.ic_contct_selectn_icon)
                    .dontAnimate()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(viewHolder.contactImgeURI);

            viewHolder.contactNumber.setText(contactModelDTO.getMobileNumber());
            viewHolder.contactName.setText(contactModelDTO.getName());
            viewHolder.contactNumber.setTextColor(context.getResources().getColor(R.color.recomendation_text_color));
            viewHolder.contactName.setTextColor(context.getResources().getColor(R.color.recomendation_bold_color));
            if (position == selectedIndex) {
                viewHolder.radioButton.setChecked(true);
            } else {
                viewHolder.radioButton.setChecked(false);
            }

            viewHolder.mRootRelativelayout.setOnClickListener(view -> {
                if (!viewHolder.radioButton.isChecked()) {
                    notifyItemChanged(selectedIndex, false);
                    selectedIndex = position;
                    viewHolder.radioButton.setChecked(true);
                }
            });
        }
    }


    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public int getItemCount() {
        return mContactsArrayList.size() + 1;
    }

    public ContactModelDTO getItem(int i) {
        return mContactsArrayList.get(i - 1);
    }

    @Override
    public int getItemViewType(int position) {

        return position;
    }

    @Override
    public String getTextToShowInBubble(int pos) {
        if (pos == 0) {
            return null;
        }
        return Character.toString(mContactsArrayList.get(pos - 1).getName().charAt(0));

    }

    public class ContentViewHolder extends RecyclerView.ViewHolder {
        ImageView contactImgeURI;
        RelativeLayout mRootRelativelayout;
        TextView contactName;
        TextView contactNumber;
        RadioButton radioButton;

        ContentViewHolder(View convertView) {
            super(convertView);
            contactImgeURI = convertView.findViewById(R.id.contact_pic);
            contactNumber = convertView.findViewById(R.id.contact_num);
            contactName = convertView.findViewById(R.id.contact_name);
            mRootRelativelayout = convertView.findViewById(R.id.root_layout);
            radioButton = convertView.findViewById(R.id.check_contact);
        }
    }

    public class ContentViewHolderAlreadySet extends RecyclerView.ViewHolder {
        RecyclerView recyclerView;

        ContentViewHolderAlreadySet(View convertView) {
            super(convertView);
            recyclerView = convertView.findViewById(R.id.recyclerView_already_set);
        }
    }

    public String getSelectedContact() {
        if (selectedIndex == -1) {
            return null;
        }
        return mContactsArrayList.get(selectedIndex - 1).getMobileNumber();
    }

    public ContactModelDTO getSelectedContactItem() {
        if (selectedIndex == -1) {
            return null;
        }
        return mContactsArrayList.get(selectedIndex - 1);
    }

    public static class AdapterData {
        String contact;
        boolean selected;

        public AdapterData(String contact, boolean selected) {
            this.contact = contact;
            this.selected = selected;
        }

        public String getContact() {
            return contact;
        }

        public void setContact(String contact) {
            this.contact = contact;
        }

        public boolean isSelected() {
            return selected;
        }

        public void setSelected(boolean selected) {
            this.selected = selected;
        }
    }

    private class AlreadySetAdapter extends SimpleRecyclerAdapter<RootViewHolder<AdapterData>, AdapterData> {

        AlreadySetAdapter(@NonNull List<AdapterData> list, OnItemClickListener<AdapterData> listener) {
            super(list, listener);
        }

        @Override
        protected AlreadySetAdapter.ViewHolder onSimpleCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new AlreadySetAdapter.ViewHolder(getInflater().inflate(R.layout.contacts_already_set_row, parent, false));
        }

        @Override
        protected void onSimpleBindViewHolder(@NonNull RootViewHolder<AdapterData> holder, int position) {
            holder.bind(getList().get(position), position);
        }

        private class ViewHolder extends RootViewHolder<AdapterData> implements View.OnClickListener {

            private AppCompatImageView contactImageURI;
            private RelativeLayout rootRelativeLayout;
            private TextView contactName;
            private TextView contactNumber;
            private AppCompatCheckBox checkBox;

            private ContactModelDTO contactModelDTO;
            private boolean isCheckedInList;

            protected ViewHolder(View view) {
                super(view);
            }

            @Override
            protected void initViews(View view) {
                contactImageURI = view.findViewById(R.id.contact_pic);
                contactNumber = view.findViewById(R.id.contact_num);
                contactName = view.findViewById(R.id.contact_name);
                rootRelativeLayout = view.findViewById(R.id.root_layout);
                checkBox = view.findViewById(R.id.check_contact);

                contactNumber.setTextColor(context.getResources().getColor(R.color.recomendation_text_color));
                contactName.setTextColor(context.getResources().getColor(R.color.recomendation_bold_color));
            }

            @Override
            protected void initComponents() {

            }

            @Override
            protected void bindViews() {
                rootRelativeLayout.setOnClickListener(this);
            }

            @Override
            public void bind(AdapterData data, int position) {
                if (!isCheckedInList) {
                    isCheckedInList = true;
                    if (getContactsArrayListOriginal() != null && !TextUtils.isEmpty(data.getContact())) {
                        String phoneNumberToCompare = Util.filterNumber(data.getContact());
                        for (ContactModelDTO contactModelDTO : getContactsArrayListOriginal()) {
                            if (contactModelDTO != null && !TextUtils.isEmpty(contactModelDTO.getMobileNumber())) {
                                if (contactModelDTO.getMobileNumber().contains(phoneNumberToCompare)) {
                                    this.contactModelDTO = contactModelDTO;
                                    break;
                                }
                            }
                        }
                    }
                }

                String name = data.getContact();
                String number = data.getContact();
                String uri = "";
                if (contactModelDTO != null) {
                    name = contactModelDTO.getName();
                    number = contactModelDTO.getMobileNumber();
                    uri = contactModelDTO.getPhotoURI();
                }

                Glide.with(context)
                        .load(uri)
                        .transform(new RoundTransform(context))
                        .placeholder(R.drawable.ic_contct_selectn_icon)
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .error(R.drawable.ic_contct_selectn_icon)
                        .dontAnimate()
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(contactImageURI);
                contactNumber.setText(number);
                contactName.setText(name);

                checkBox.setChecked(data.isSelected());
            }

            @Override
            public void onClick(View v) {
                if (getListener() != null)
                    getListener().onItemClick(v, getList().get(getAdapterPosition()), getAdapterPosition());
            }
        }
    }

    public List<AdapterData> getAlreadySetData() {
        return mAlreadySetData;
    }

    private ArrayList<ContactModelDTO> getContactsArrayListOriginal() {
        if (mOriginalContactsArrayList == null)
            return mContactsArrayList;
        return mOriginalContactsArrayList;
    }

    public void setContactsArrayListOriginal(ArrayList<ContactModelDTO> mContactsArrayListOriginal) {
        this.mOriginalContactsArrayList = mContactsArrayListOriginal;
    }

    public static class RoundTransform extends BitmapTransformation {
        RoundTransform(Context context) {
            super(context);
        }

        @Override
        protected Bitmap transform(BitmapPool pool, Bitmap toTransform, int outWidth, int outHeight) {
            return circleCrop(pool, toTransform);
        }

        private static Bitmap circleCrop(BitmapPool pool, Bitmap source) {
            if (source == null) return null;

            int size = Math.min(source.getWidth(), source.getHeight());
            int x = (source.getWidth() - size) / 2;
            int y = (source.getHeight() - size) / 2;

            Bitmap squared = Bitmap.createBitmap(source, x, y, size, size);

            Bitmap result = pool.get(size, size, Bitmap.Config.ARGB_8888);
            if (result == null) {
                result = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888);
            }

            Canvas canvas = new Canvas(result);
            Paint paint = new Paint();
            paint.setShader(new BitmapShader(squared, BitmapShader.TileMode.CLAMP, BitmapShader.TileMode.CLAMP));
            paint.setAntiAlias(true);
            float r = size / 2f;
            canvas.drawCircle(r, r, r, paint);
            return result;
        }

        @Override
        public String getId() {
            return getClass().getName();
        }
    }
}

