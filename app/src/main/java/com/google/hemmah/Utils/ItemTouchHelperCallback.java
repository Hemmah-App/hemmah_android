package com.google.hemmah.Utils;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.google.hemmah.presentation.common.common.AppAdapter;

public class ItemTouchHelperCallback extends ItemTouchHelper.SimpleCallback {
    AppAdapter mAppAdapter;
    public ItemTouchHelperCallback(AppAdapter adapter) {
        super(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT);
        mAppAdapter = adapter;
    }
    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
        return false;
    }

    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
//        mAppAdapter.removeItem(viewHolder.getAdapterPosition());
    }
}
