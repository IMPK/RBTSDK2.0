package com.onmobile.rbt.baseline.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;
import com.onmobile.rbt.baseline.R;
import com.onmobile.rbt.baseline.fragment.FragmentGiftRbtChild;
import com.onmobile.rbt.baseline.model.ContactModelDTO;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.RecyclerView;


public class GiftRbtChildAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private List<FragmentGiftRbtChild.GiftChildInfo> mGiftChildList;
    private LayoutInflater mInflater;
    private IDeleteChildListener mDeleteChildListener;

    @Override
    public long getItemId(int position) {
        return position;
    }

    public GiftRbtChildAdapter(Context context, List<FragmentGiftRbtChild.GiftChildInfo> giftChildList, IDeleteChildListener deleteChildListener) {
        mContext = context;
        mGiftChildList = giftChildList;
        mDeleteChildListener = deleteChildListener;
        mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView;
        RecyclerView.ViewHolder viewHolder = null;
        itemView = mInflater.inflate(R.layout.gift_child_contacts_row, parent, false);
        viewHolder = new ContentViewHolder(itemView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        getContentView((ContentViewHolder) holder, position);
    }


    private void getContentView(final ContentViewHolder viewHolder, final int position) {
        final FragmentGiftRbtChild.GiftChildInfo giftChildInfo = getItem(position);
        ContactModelDTO contactModelDTO = giftChildInfo.getContactModelDTO();
        if (contactModelDTO != null) {
            Glide.with(mContext)
                    .load(contactModelDTO.getPhotoURI())
                    .transform(new RoundTransform(mContext))
                    .placeholder(R.drawable.ic_contct_selectn_icon)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .error(R.drawable.ic_contct_selectn_icon)
                    .dontAnimate()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(viewHolder.contactImgeURI);

            viewHolder.contactNumber.setText(contactModelDTO.getMobileNumber());
            viewHolder.contactName.setText(contactModelDTO.getName());
            viewHolder.contactNumber.setTextColor(mContext.getResources().getColor(R.color.recomendation_text_color));
            viewHolder.contactName.setTextColor(mContext.getResources().getColor(R.color.recomendation_bold_color));
            if(giftChildInfo.getChildInfo().getStatus().equals("accepted")){
                viewHolder.giftStatus.setText(mContext.getString(R.string.gift_status_accepted));
                viewHolder.giftStatusCircle.setBackgroundResource(R.drawable.gift_circle_accepted);
                viewHolder.giftStatus.setVisibility(View.VISIBLE);
            }
            else if(giftChildInfo.getChildInfo().getStatus().equals("pending")){
                viewHolder.giftStatus.setText(mContext.getString(R.string.gift_status_pending));
                viewHolder.giftStatusCircle.setBackgroundResource(R.drawable.gift_circle_pending);
                viewHolder.giftStatus.setVisibility(View.VISIBLE);
            }
            else{
                viewHolder.giftStatus.setVisibility(View.INVISIBLE);
            }
            viewHolder.mGiftDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mDeleteChildListener.deleteChild(position);
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
        return mGiftChildList.size();
    }

    public FragmentGiftRbtChild.GiftChildInfo getItem(int i) {
        return mGiftChildList.get(i);
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public class ContentViewHolder extends RecyclerView.ViewHolder {
        ImageView contactImgeURI;
        RelativeLayout mRootRelativelayout;
        TextView contactName;
        TextView contactNumber;
        TextView giftStatus;
        View giftStatusCircle;
        AppCompatImageView mGiftDelete;

        ContentViewHolder(View convertView) {
            super(convertView);
            contactImgeURI = convertView.findViewById(R.id.contact_pic);
            contactNumber = convertView.findViewById(R.id.contact_num);
            contactName = convertView.findViewById(R.id.contact_name);
            mRootRelativelayout = convertView.findViewById(R.id.root_layout);
            giftStatus = convertView.findViewById(R.id.gift_status);
            giftStatusCircle = convertView.findViewById(R.id.gift_status_circle);
            mGiftDelete = convertView.findViewById(R.id.gift_child_delete);
        }


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

    public interface IDeleteChildListener{
        void deleteChild(int position);
    }
}

