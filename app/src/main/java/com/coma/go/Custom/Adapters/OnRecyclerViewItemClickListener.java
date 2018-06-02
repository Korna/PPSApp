package com.coma.go.Custom.Adapters;

import android.view.View;

public interface OnRecyclerViewItemClickListener<Model> {
    void onItemClick(View view, Model model);
}