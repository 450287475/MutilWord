package com.wangdao.mutilword.utils;

import android.util.SparseArray;
import android.view.View;
/**
 * Created by yxd on 2016/4/21 for exam part.
 */
public class ViewHolder {
	    // I added a generic return type to reduce the casting noise in client code
	    @SuppressWarnings("unchecked")
	    public static <T extends View> T get(View view, int id) {
	        SparseArray<View> viewHolder = (SparseArray<View>) view.getTag();
	        if (viewHolder == null) {
	            viewHolder = new SparseArray<View>();
	            view.setTag(viewHolder);
	        }
	        View childView = viewHolder.get(id);
	        if (childView == null) {
	            childView = view.findViewById(id);
	            viewHolder.put(id, childView);
	        }
	        return (T) childView;
	    }
}
