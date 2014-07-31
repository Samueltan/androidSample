package com.overture.questdroid;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTabHost;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;

import com.android.volley.Request.Method;
import com.lidroid.xutils.ViewUtils;
import com.overture.questdroid.app.VolleyController;
import com.overture.questdroid.fragment.*;
import com.overture.questdroid.utility.CustomReqListener;
import com.overture.questdroid.utility.CustomRequest;
import com.lidroid.xutils.util.LogUtils;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;

// Bind the main view for the activity
@ContentView(R.layout.main)
public class ContestsActivity extends FragmentActivity{

	// Bind the tab view
    @ViewInject(R.id.tabhost)
    private FragmentTabHost mTabHost;

    // Fragment for each corresponding tab
    private Class fragmentArray[] = {
    		ContestsFragment.class,
    		ContestsFragment.class,
            ContestsFragment.class,
            ContestsFragment.class,
            ProfileFragment.class,};
    // Tab icon for each tab item (from FontAwesome)
    private int iconArray[] = {
            R.string.fa_contests,
            R.string.fa_rate,
            R.string.fa_market,
            R.string.fa_activity,
            R.string.fa_profile};
    // Tab item description
    private int titleArray[] = {
    		R.string.tab1,
    		R.string.tab2,
    		R.string.tab3,
    		R.string.tab4,
    		R.string.tab5};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
		
        super.onCreate(savedInstanceState);

        LogUtils.customTagPrefix = "questDroid-debug:";
        LogUtils.allowI = false;

        // Bind the views with activity
        ViewUtils.inject(this);

//        setupHttpRequest();
        setupTabView();        
    }
    
    /**
     * Load the tab view with tab items
     */
    private void setupTabView() {
        mTabHost.setup(this, getSupportFragmentManager(), R.id.realtabcontent);
        mTabHost.getTabWidget().setDividerDrawable(null);

        int count = fragmentArray.length;

        for (int i = 0; i < count; i++) {
            TabHost.TabSpec tabSpec = mTabHost.newTabSpec(getString(titleArray[i])).setIndicator(getTabItemView(i));
            mTabHost.addTab(tabSpec, fragmentArray[i], null);
//            mTabHost.getTabWidget().getChildAt(i).setBackgroundResource(R.drawable.tab_item);
        }

    }

    /**
     * 
     * @param index: tab item index
     * @return:	tab view
     */
    private View getTabItemView(int index) {
        LayoutInflater layoutInflater = LayoutInflater.from(this);
        View view = layoutInflater.inflate(R.layout.tab_bottom_nav, null);
        
        TextView imageView = (TextView) view.findViewById(R.id.iv_icon);
        imageView.setText(iconArray[index]);
        // User coins information
        Typeface font = Typeface.createFromAsset(getAssets(), "fontawesome-webfont.ttf" );
        imageView.setTypeface(font);

        TextView textView = (TextView) view.findViewById(R.id.tv_icon);
        textView.setText(titleArray[index]);

        return view;
    }
}
