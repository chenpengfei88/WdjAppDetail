package com.fe.wdj.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.fe.wdj.R;

/**
 * Created by chenpengfei on 2016/10/27.
 */
public class MyAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Activity mActivity;
    private Object[][]mArray;
    private OnItemClickLitener mOnItemClickLitener;

    public MyAdapter(Activity activity, Object[][] array) {
        mActivity = activity;
        mArray = array;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ItemViewHolder(View.inflate(mActivity, R.layout.activity_list_item, null));
    }

    @Override
    public int getItemCount() {
        return mArray.length;
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        ItemViewHolder itemVh = (ItemViewHolder) holder;
        Object[] contentArray = mArray[position];
        itemVh.iconImageView.setImageResource((int) contentArray[0]);
        itemVh.appNameTextView.setText((String) contentArray[1]);
        if(mOnItemClickLitener != null) {
            itemVh.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnItemClickLitener.onItemClick(v, position);
                }
            });
        }
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {

        ImageView iconImageView;
        TextView appNameTextView;

        public ItemViewHolder(View view) {
            super(view);
            iconImageView = (ImageView) view.findViewById(R.id.imageview_icon);
            appNameTextView = (TextView) view.findViewById(R.id.textview_appname);
        }
    }

    public void setOnItemClickLitener(OnItemClickLitener mOnItemClickLitener) {
        this.mOnItemClickLitener = mOnItemClickLitener;
    }

    public interface OnItemClickLitener {
        void onItemClick(View view, int position);
    }


}
