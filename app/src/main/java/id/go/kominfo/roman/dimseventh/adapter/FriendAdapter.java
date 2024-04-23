package id.go.kominfo.roman.dimseventh.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import id.go.kominfo.roman.dimseventh.model.Friend;

public class FriendAdapter extends BaseAdapter {
    private final List<Friend> mList;
    private final Context mContext;

    public FriendAdapter(List<Friend> mList, Context mContext) {
        this.mList = mList;
        this.mContext = mContext;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView==null)
            convertView = LayoutInflater.from(mContext)
                    .inflate(android.R.layout.simple_list_item_1, parent, false);

        TextView tvName = convertView.findViewById(android.R.id.text1);
        tvName.setText(mList.get(position).getName());

        return convertView;
    }
}
