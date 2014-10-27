package net.rmj.android.ohfeedback;

import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

/**
 * Created by Ronaldo on 10/24/2014.
 */
public class DrawerOptionsAdapter extends RecyclerView.Adapter<DrawerOptionsAdapter.ViewHolder> {

    private String[] mDataset;
    private OnItemClickListener mListener;

    public interface OnItemClickListener {
        public void onClick(View view, int position);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public final TextView mTextView;

        public ViewHolder(TextView v) {
            super(v);
            mTextView = v;
        }
    }

    public DrawerOptionsAdapter(String[] dataset, OnItemClickListener listener) {
        mDataset = dataset;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater vi = LayoutInflater.from(parent.getContext());
        View v = vi.inflate(R.layout.drawer_list_item,parent,false);
        TextView tv = (TextView)v.findViewById(android.R.id.text1);
        return new ViewHolder(tv);

    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.mTextView.setText(mDataset[position]);
        holder.mTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onClick(view, position);

            }
        });
    }

    @Override
    public int getItemCount() { return mDataset.length; }
}
