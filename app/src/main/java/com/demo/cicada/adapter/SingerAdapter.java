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
import com.demo.cicada.entity.music.SingerInfo;

import java.util.List;

/**
 * 歌手适配器
 */

public class SingerAdapter extends RecyclerView.Adapter<SingerAdapter.ViewHolder> {

    private List<SingerInfo> singerInfoList;
    private Context context;
    //    private DBManager dbManager;
    private OnItemClickListener onItemClickListener;

    public SingerAdapter(Context context, List<SingerInfo> singerInfoList) {
        this.context = context;
        this.singerInfoList = singerInfoList;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        View swipeContent;
        LinearLayout contentLl;
        ImageView singerIv;
        TextView singelName;
        TextView count;

        private ViewHolder(View itemView) {
            super(itemView);
            this.swipeContent = itemView.findViewById(R.id.model_swipemenu_layout);
            this.contentLl = (LinearLayout) itemView.findViewById(R.id.model_music_item_ll);
            this.singerIv = (ImageView) itemView.findViewById(R.id.model_head_iv);
            this.singelName = (TextView) itemView.findViewById(R.id.model_item_name);
            this.count = (TextView) itemView.findViewById(R.id.model_music_count);
        }

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.local_model_rv_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        Log.d("aaaa", "onBindViewHolder: position = " + position);
        SingerInfo singer = singerInfoList.get(position);
        holder.singelName.setText(singer.getName());
        holder.singerIv.setImageResource(R.drawable.ic_singer);
        holder.count.setText(singer.getCount() + "首");
        holder.contentLl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickListener.onContentClick(holder.contentLl, position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return singerInfoList.size();
    }

    /*public void update(List<SingerInfo> singerInfoList) {
        this.singerInfoList.clear();
        this.singerInfoList.addAll(singerInfoList);
        notifyDataSetChanged();
    }*/

    public interface OnItemClickListener {
        void onDeleteMenuClick(View content, int position);

        void onContentClick(View content, int position);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }
}
