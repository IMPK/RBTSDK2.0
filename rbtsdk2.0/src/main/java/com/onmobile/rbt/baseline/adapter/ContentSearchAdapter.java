package com.onmobile.rbt.baseline.adapter;

import android.view.View;
import android.view.ViewGroup;

import com.onmobile.baseline.http.api_action.dtos.RingBackToneDTO;
import com.onmobile.baseline.http.api_action.dtos.search.CategoricalSearchItemDTO;
import com.onmobile.rbt.baseline.R;
import com.onmobile.rbt.baseline.adapter.base.SimpleRecyclerAdapter;
import com.onmobile.rbt.baseline.holder.RootViewHolder;
import com.onmobile.rbt.baseline.listener.OnItemClickListener;
import com.onmobile.rbt.baseline.util.FunkyAnnotation;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


/**
 * Created by Shahbaz Akhtar on 15/11/2018.
 *
 * @author Shahbaz Akhtar
 */
public class ContentSearchAdapter extends SimpleRecyclerAdapter<RootViewHolder<CategoricalSearchItemDTO>, CategoricalSearchItemDTO> {

    public static final String TYPE_SONG = "type:song";
    public static final String TYPE_ARTIST = "type:artist";
    public static final String TYPE_ALBUM = "type:album";

    private FragmentManager mFragmentManager;
    private String mSearchQuery;

    public ContentSearchAdapter(FragmentManager fragmentManager, @NonNull List<CategoricalSearchItemDTO> list, OnItemClickListener<CategoricalSearchItemDTO> listener) {
        super(list, listener);
        mFragmentManager = fragmentManager;
    }

    public void setSearchQuery(String mSearchQuery) {
        this.mSearchQuery = mSearchQuery;
    }

    @Override
    protected RootViewHolder<CategoricalSearchItemDTO> onSimpleCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(getInflater().inflate(R.layout.layout_search_content_item, parent, false));
    }

    @Override
    protected void onSimpleBindViewHolder(@NonNull RootViewHolder<CategoricalSearchItemDTO> holder, int position) {
        holder.bind(getList().get(position), position);
    }

    private class ViewHolder extends RootViewHolder<CategoricalSearchItemDTO> {

        private AppCompatTextView tvTitle, tvMore;
        //private FrameLayout fragmentContainer;
        private RecyclerView recyclerView;
        private HorizontalMusicAdapter adapter;
        private CategoricalSearchItemDTO data;

        private OnItemClickListener itemClickListener = (OnItemClickListener<RingBackToneDTO>) (view, data, position, sharedElements) -> {
            if (getListener() != null)
                getListener().onItemClick(view, this.data, position);
        };

        protected ViewHolder(View view) {
            super(view);
        }

        @Override
        protected void initViews(View view) {
            tvTitle = view.findViewById(R.id.tv_title_search_content_item);
            tvMore = view.findViewById(R.id.tv_more_search_content_item);
            //fragmentContainer = view.findViewById(R.id.layout_fragment_container);
            recyclerView = view.findViewById(R.id.rv_horizontal_search_item);
        }

        @Override
        protected void initComponents() {

        }

        @Override
        protected void bindViews() {

        }

        @Override
        public void bind(CategoricalSearchItemDTO data, int position) {

            this.data = data;
            tvTitle.setText(getCategoryTitle(data.getType()));
            tvMore.setOnClickListener(v -> {
                if (getListener() != null)
                    getListener().onItemClick(v, data, position);
            });

            /*Removed fragment attachment from recycler view holder. Doesn't work well*/
            /*ListItem listItem = new ListItem(null, data.getItems());
            FragmentHorizontalMusic fragment = FragmentHorizontalMusic.newInstance(FunkyAnnotation.HORIZONTAL_MUSIC_CONTENT_TYPE_SEARCH,
                    listItem, mSearchQuery, getCategoryContentItemType(data.getType()), true, true);

            // Delete old fragment
            int containerId = fragmentContainer.getId();// Get container id
            Fragment oldFragment = mFragmentManager.findFragmentById(containerId);
            if (oldFragment != null) {
                mFragmentManager.beginTransaction().remove(oldFragment).commitAllowingStateLoss();
            }

            int newContainerId = (position + 1) + (int) (Math.random() * AppConstant.SMALL_NUMBER_TO_GENERATE_RANDOM_ID);
            fragmentContainer.setId(newContainerId);// Set container id

            // Add new fragment
            mFragmentManager.beginTransaction().replace(newContainerId, fragment).commitAllowingStateLoss();*/

            /*Instead of fragment support*/
            adapter = new HorizontalMusicAdapter(mFragmentManager, null, data.getItems(), false, true, itemClickListener);
            adapter.setType(data.getType());
            setupRecycler();
        }

        private void setupRecycler() {
            LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setHasFixedSize(false);
            recyclerView.setItemAnimator(null);
            recyclerView.setAdapter(adapter);
        }
    }

    /**
     * Get the title/label to show
     *
     * @param type Type passed from activity
     * @return Title/label
     */
    public String getCategoryTitle(String type) {
        switch (type) {
            case TYPE_ARTIST:
                return getContext().getString(R.string.search_category_artist_title);
            case TYPE_ALBUM:
                return getContext().getString(R.string.search_category_album_title);
            default:
                return getContext().getString(R.string.search_category_songs_title);
        }
    }

    /**
     * Get the Item type
     *
     * @param type Type passed from activity
     * @return Id
     */
    private int getCategoryContentItemType(String type) {
        switch (type) {
            case TYPE_ARTIST:
                return FunkyAnnotation.SEARCH_CONTENT_TYPE_ARTIST;
            case TYPE_ALBUM:
                return FunkyAnnotation.SEARCH_CONTENT_TYPE_ALBUM;
            default:
                return FunkyAnnotation.SEARCH_CONTENT_TYPE_SONG;
        }
    }

}
