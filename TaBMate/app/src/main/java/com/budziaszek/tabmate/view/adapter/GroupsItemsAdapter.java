package com.budziaszek.tabmate.view.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.budziaszek.tabmate.R;
import com.budziaszek.tabmate.firestoreData.Group;
import com.budziaszek.tabmate.view.listener.GroupsClickListener;

import java.util.ArrayList;
import java.util.List;

public class GroupsItemsAdapter extends RecyclerView.Adapter<GroupsItemsAdapter.MyViewHolder> {

    private GroupsClickListener groupsClickListener;
    private List<Group> groupsList;
    private int selectedItem;
    private View itemView;
    private ArrayList<RelativeLayout> layoutList = new ArrayList<>();

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView groupName;
        private RelativeLayout groupItemLayout;

        private MyViewHolder(View view) {
            super(view);

            groupItemLayout = view.findViewById(R.id.group_item_layout);
            groupItemLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //TODO use items view design change
                    // All items
                    /*for(RelativeLayout layout : layoutList) {
                        layout.setBackgroundColor(itemView.getResources()
                                .getColor(R.color.colorPrimarySemi, itemView.getContext().getTheme()));
                    }
                    // Selected item
                    groupItemLayout.setBackgroundColor(itemView.getResources()
                            .getColor(R.color.colorPrimary, itemView.getContext().getTheme()));*/
                    groupsClickListener.onItemClicked(getAdapterPosition());
                }
            });
            groupItemLayout.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    groupsClickListener.onItemLongClicked(getAdapterPosition());
                    return true;
                }
            });
            if(selectedItem == getAdapterPosition())
                groupItemLayout.callOnClick();

            groupName = view.findViewById(R.id.group_name);
        }
    }


    public GroupsItemsAdapter(List<Group> groupsList, GroupsClickListener groupsClickListener, int selectedItem) {
        this.groupsList = groupsList;
        this.groupsClickListener = groupsClickListener;
        this.selectedItem = selectedItem;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_group, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Group group = groupsList.get(position);
        holder.groupName.setText(group.getName());
        layoutList.add(holder.groupItemLayout);
    }

    @Override
    public int getItemCount() {
        return groupsList.size();
    }

    public void update(List<Group> data) {
        groupsList.clear();
        groupsList.addAll(data);
        notifyDataSetChanged();
    }
}