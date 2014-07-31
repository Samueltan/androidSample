package com.overture.questdroid.utility;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.Adapter;
import android.widget.GridView;

public class OptimizeGridView extends GridView {
    /**
     * 
     *
     * @param <T>
     */
    interface OptimizeGridAdapter<T> {
        List<T> getItems();
        /**
         * Should notify the listView data is changed
         *
         * @param items
         */
        void setItems(List<T> items);
        T getNullItem();
        boolean isNullItem(T item);
    }

    public OptimizeGridView(Context context) {
        super(context);
    }

    public OptimizeGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public OptimizeGridView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @SuppressLint("DrawAllocation")
    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int numColumns = getNumColumns();
        if (numColumns != AUTO_FIT) {
            final Adapter adapter = getAdapter();
            if (!(adapter instanceof OptimizeGridAdapter)) {
                return;
            }
            final int count = adapter.getCount();
            final int remainder = count % numColumns;
            if (remainder != 0) {
                final int diff = numColumns - remainder;
                final OptimizeGridAdapter adapter2 = (OptimizeGridAdapter) adapter;
                final List items = new ArrayList();
                items.addAll(adapter2.getItems());
                for (int i = 0; i < diff; i++) {
                    items.add(adapter2.getNullItem());
                }
                adapter2.setItems(items);
            }
        }
    }

}
