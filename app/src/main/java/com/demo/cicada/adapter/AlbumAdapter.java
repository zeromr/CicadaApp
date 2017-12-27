package com.demo.cicada.adapter;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.demo.cicada.R;
import com.demo.cicada.entity.music.AlbumInfo;

import java.util.List;

/**
 * 专辑适配器
 */

public class AlbumAdapter extends RecyclerView.Adapter<AlbumAdapter.ViewHolder> {
    private List<AlbumInfo> albumInfoList;
    private Context context;
    private AlbumAdapter.OnItemClickListener onItemClickListener;

    public AlbumAdapter(Context context, List<AlbumInfo> albumInfoList) {
        this.context = context;
        this.albumInfoList = albumInfoList;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        View swipeContent;
        LinearLayout contentLl;
        ImageView albumIv;
        TextView albumName;
        TextView count;

        private ViewHolder(View itemView) {
            super(itemView);
            this.swipeContent = itemView.findViewById(R.id.model_swipemenu_layout);
            this.contentLl = (LinearLayout) itemView.findViewById(R.id.model_music_item_ll);
            this.albumIv = (ImageView) itemView.findViewById(R.id.model_head_iv);
            this.albumName = (TextView) itemView.findViewById(R.id.model_item_name);
            this.count = (TextView) itemView.findViewById(R.id.model_music_count);
        }

    }

    @Override
    public AlbumAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.local_model_rv_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final AlbumAdapter.ViewHolder holder, final int position) {
        Log.d("aaaa", "onBindViewHolder: position = " + position);
        AlbumInfo album = albumInfoList.get(position);
        holder.albumIv.setImageResource(R.drawable.ic_album);
        holder.albumName.setText(album.getName());
        holder.count.setText(album.getCount() + "首 " + album.getSinger());
        holder.contentLl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickListener.onContentClick(holder.contentLl, position);
            }
        });

    }

    @Override
    public int getItemCount() {
        return albumInfoList.size();
    }

    /*public void update(List<AlbumInfo> albumInfoList) {
        this.albumInfoList.clear();
        this.albumInfoList.addAll(albumInfoList);
        notifyDataSetChanged();
    }*/

    public interface OnItemClickListener {
        void onDeleteMenuClick(View content, int position);

        void onContentClick(View content, int position);
    }

    public void setOnItemClickListener(AlbumAdapter.OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }
}
