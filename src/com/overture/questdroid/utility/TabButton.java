package com.overture.questdroid.utility;

import com.overture.questdroid.R;

import android.content.Context;  
import android.content.res.TypedArray;  
import android.graphics.Color;  
import android.media.Rating;
import android.util.AttributeSet;  
import android.widget.Button;  
import android.widget.RatingBar;
  
public class TabButton extends Button {  
    private int normal_bg_res;  
    private int selected_bg_res;  
  
    public TabButton(Context context) {  
        super(context);  
    }  
  
    public TabButton(Context context, AttributeSet attrs) {
        super(context, attrs);  
          
        TypedArray typeArray = context.obtainStyledAttributes(attrs, R.styleable.TabButton);  
        normal_bg_res = typeArray.getResourceId(R.styleable.TabButton_normal_bg_res, 0);  
        selected_bg_res = typeArray.getResourceId(R.styleable.TabButton_selected_bg_res, 0);  
          
        typeArray.recycle();  
    }  
      
    /* 
     * 这里本来是准备自定义一个方法，以便在Activity中调用， 
     * 但是写完发现Button的父类TextView中已经有了同名方法，于是自定义变成了覆盖，不过无所谓，不影响效果 
     */  
    public void setSelected(boolean selected) {  
        if (selected) {  
            setBackgroundResource(selected_bg_res);  
            setTextColor(Color.WHITE);  
        } else {  
            setBackgroundResource(normal_bg_res);  
            setTextColor(Color.GRAY);  
        }  
    }  
      
}  