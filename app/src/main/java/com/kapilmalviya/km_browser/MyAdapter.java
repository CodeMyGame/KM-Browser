package com.kapilmalviya.km_browser;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.Collections;
import java.util.List;

/**
 * Created by Kapil Malviya on 12/15/2015.
 */
public class MyAdapter extends RecyclerView.Adapter<MyAdapter.viewHolder> {

    private final LayoutInflater inflater;
    List<Information> data = Collections.emptyList();
    Context context;
    int position;

    public MyAdapter(Context c, List<Information> data) {
        this.context = c;
        inflater = LayoutInflater.from(c);
        this.data = data;

    }

    @Override
    public viewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = inflater.inflate(R.layout.custom_row, parent, false);
        viewHolder holder = new viewHolder(v);  //avoiding to initilize using find view by id every time
        return holder;
    }

    @Override
    public void onBindViewHolder(viewHolder holder, int position) {
        this.position = position;//here we fill the data item
        Information current = data.get(position);
        holder.tv.setText(current.title);


    }

    @Override
    public int getItemCount() {

        return data.size();
    }


    class viewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tv;

        public viewHolder(View itemView) {

            super(itemView);
            tv = (TextView) itemView.findViewById(R.id.textViewList);
        }

        @Override
        public void onClick(View v) {

        }
    }
}
