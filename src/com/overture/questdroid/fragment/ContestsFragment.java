package com.overture.questdroid.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import com.android.volley.Request.Method;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.bitmap.BitmapCommonUtils;
import com.lidroid.xutils.bitmap.BitmapDisplayConfig;
import com.lidroid.xutils.bitmap.callback.BitmapLoadFrom;
import com.lidroid.xutils.bitmap.callback.DefaultBitmapLoadCallBack;
import com.lidroid.xutils.util.LogUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnItemClick;
import com.lidroid.xutils.view.annotation.event.OnTouch;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.overture.questdroid.BitmapHelp;
import com.overture.questdroid.R;
import com.overture.questdroid.app.VolleyController;
import com.overture.questdroid.swipelistview.BaseSwipeListViewListener;
import com.overture.questdroid.swipelistview.SwipeListView;
import com.overture.questdroid.utility.CustomReqListener;
import com.overture.questdroid.utility.CustomRequest;

/**
 * Author: Samuel
 * Date: 14-6-29
 * Contests fragment: handle the image list of the contests and their related information
 */
public class ContestsFragment extends Fragment{

    public static BitmapUtils bitmapUtils;
    Typeface font;
    
    @ViewInject(R.id.icon_user_coin)
    private TextView iconUserCoin;
    @ViewInject(R.id.img_list)
    private SwipeListView imageListView;
    private ImageListAdapter imageListAdapter;
    ArrayList<ItemRow> itemData;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tab_fragment_contests, container, false); // Load fragment layout
        ViewUtils.inject(this, view); 		//Bind view and event

        // Load fondawesome icons
        font = Typeface.createFromAsset(getActivity().getAssets(), "fontawesome-webfont.ttf" );
        iconUserCoin.setTypeface(font);
        
        // Initialize the image utility class which is used for the image list handling
        bitmapUtils = BitmapHelp.getBitmapUtils(this.getActivity().getApplicationContext());
        bitmapUtils.configDefaultLoadingImage(R.drawable.ic_launcher);
        bitmapUtils.configDefaultLoadFailedImage(R.drawable.ic_launcher);
        bitmapUtils.configDefaultBitmapConfig(Bitmap.Config.RGB_565);

        //Swipe listview settings
        imageListView.setSwipeMode(SwipeListView.SWIPE_MODE_BOTH); // there are five swiping modes
        imageListView.setSwipeActionLeft(SwipeListView.SWIPE_ACTION_REVEAL); //there are four swipe actions 
        imageListView.setSwipeActionRight(SwipeListView.SWIPE_ACTION_REVEAL);
        imageListView.setSwipeCloseAllItemsWhenMoveList(true);
        
        imageListView.setAnimationTime(500); // Animation time
        imageListView.setSwipeOpenOnLongPress(true); // enable or disable SwipeOpenOnLongPress
        
        imageListView.setSwipeListViewListener(new BaseSwipeListViewListener() {
        	@Override
        	public void onOpened(int position, boolean toRight) {
        		LogUtils.d(String.format("onOpened position = %d; toRight = %b", position, toRight));
        	}

        	@Override
        	public void onClosed(int position, boolean fromRight) {
        		LogUtils.d(String.format("onClosed position =%d; fromRight = %b", position, fromRight));
        	}

        	@Override
        	public void onMove(int position, float x) {
        		LogUtils.d(String.format("onMove position = %d; x = %f", position, x));
        	}

        	@Override
        	public void onStartOpen(int position, int action, boolean right) {
        		LogUtils.d(String.format("onStartOpen position = %d; action = %d; right = %b", position, action, right));
        		RelativeLayout layoutDetails =(RelativeLayout)imageListView.findViewById(R.id.img_detailsView);
        		LinearLayout  layoutRules =(LinearLayout)imageListView.findViewById(R.id.img_rulesView);
        		if(right){
        			layoutDetails.setVisibility(View.VISIBLE);
        			layoutRules.setVisibility(View.GONE);
        		}else{
        			layoutDetails.setVisibility(View.GONE);
        			layoutRules.setVisibility(View.VISIBLE);
        		}
        	}

        	@Override
        	public void onStartClose(int position, boolean right) {
//        		LogUtils.d(String.format("onStartClose position = %d; right = %b", position, right));
//        		RelativeLayout layoutDetails =(RelativeLayout)imageListView.findViewById(R.id.img_detailsView);
//        		LinearLayout  layoutRules =(LinearLayout)imageListView.findViewById(R.id.img_rulesView);
//        		if(right && layoutRules.getVisibility() == View.VISIBLE){
//        			layoutDetails.setVisibility(View.GONE);
//        			layoutRules.setVisibility(View.VISIBLE);
//        		}else{
//        			layoutDetails.setVisibility(View.VISIBLE);
//        			layoutRules.setVisibility(View.GONE);
//        		}
        	}
        });
        
        itemData=new ArrayList<ItemRow>();
        imageListAdapter = new ImageListAdapter(getActivity(), R.layout.bitmap_item, itemData);
        imageListView.setAdapter(imageListAdapter);          

        // Load URL, return the images from the urls and store into the list view
        loadListData();

        return view;
    }

    @OnItemClick(R.id.img_list)
    public void onImageItemClick(AdapterView<?> parent, View view, int position, long id) {
    }
    
    // 
    
    /**
     * Load the images from the top favorite list (test data)
     * @param url
     */
    private void loadListData() {

		final String tag_getUser = "json_getUser_req"; 
		final String tag_getGlobalList = "json_getGlobalList"; 
		Integer pageIndex = Integer.valueOf(1);

		Date date = new Date();
		double longitude = VolleyController.getInstance().longitude;
		double latitude = VolleyController.getInstance().latitude;
		
		// Get user information
    	String uri = "https://test.snaapiq.com/api2/user";
        Map<String, String> params = new HashMap<String, String>();
        
        CustomRequest userReq = new CustomRequest(Method.GET, uri, params, 
        										CustomReqListener.successListenerUser(getActivity()),
        										CustomReqListener.ErrorListener(getActivity())
                                                );
        VolleyController.getInstance().addToRequestQueue(userReq, tag_getUser);
        
        // Get global list           
        uri = "https://test.snaapiq.com/api2/quest?minStatus=2&maxStatus=2";
        params = new HashMap<String, String>();

        if(longitude ==0 &&latitude==0) {
        	params.put("date", String.valueOf(date.getTime()));
        	params.put("page.page", pageIndex.toString());
        	params.put("page.size", "10");
            CustomRequest globalListReq = new CustomRequest(Method.GET, uri, params, 
            										CustomReqListener.successListenerGlobalList(this),
            										CustomReqListener.ErrorListener(getActivity())
                                                    );
            VolleyController.getInstance().addToRequestQueue(globalListReq, tag_getGlobalList);
       }
        else{
            CustomRequest globalListReq = new CustomRequest(Method.GET, uri, params, 
            										CustomReqListener.successListenerGlobalList(this),
            										CustomReqListener.ErrorListener(getActivity())
                                                    );
            VolleyController.getInstance().addToRequestQueue(globalListReq, tag_getGlobalList);
        }
    }
    
    /**
     * Load the images from the top favorite list
     * @param url
     */
    public void injectListData(ItemRow row) {
    	imageListAdapter.addRowData(row);
        
        imageListAdapter.notifyDataSetChanged();	//notify the listview to update data
    }
    



    
    /**
     * Image Adapter that handles the image related data
     * @author samueltango
     *
     */
    public class ImageListAdapter extends ArrayAdapter<ItemRow> {

        private Context context;
        private ArrayList<ItemRow> rowData;
    	int layoutResID;

        public ImageListAdapter(Context context, int layoutResourceId, ArrayList<ItemRow> data) {
        	super(context, layoutResourceId, data);
        	
        	this.rowData = data;
        	this.context = context;
        	this.layoutResID=layoutResourceId;
        	
        }
        public void addRowData(ItemRow row) {
            this.rowData.add(row);
        }

        @Override
        public int getCount() {
            return rowData.size();
        }

        @Override
        public ItemRow getItem(int position) {
            return rowData.get(position);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(final int position, View view, ViewGroup parent) {
            ImageItemHolder holder = null;
     	   View row = view;
            if (row == null) {
                row = getActivity().getLayoutInflater().inflate(R.layout.bitmap_item, parent, false);
                holder = new ImageItemHolder();
                ViewUtils.inject(holder, row);
                row.setTag(holder);

            } else {
                holder = (ImageItemHolder) row.getTag();
            }
            holder.imgPb.setProgress(0);            
            holder.iconContestCoin.setTypeface(font);
            holder.iconClock.setTypeface(font);
            holder.iconColabration.setTypeface(font);
            holder.iconLocation.setTypeface(font);
            holder.iconCamera.setTypeface(font);
            holder.setContent(rowData.get(position));
            
            // Display the images and related data in the view
//            LogUtils.d("===== position = " + position);
            bitmapUtils.display(holder.imgItem, rowData.get(position).getImgSrc());
            bitmapUtils.display(holder.ivRectBanner, rowData.get(position).getRectBanner());

//            RelativeLayout layoutDetails = (RelativeLayout)row.findViewById(R.id.img_itemDetailsView);
//            layoutDetails.setVisibility(View.GONE);
       
            return row;
        }
    }
    
    private class ImageItemHolder {
    	@ViewInject(R.id.img_item)
    	private ImageView imgItem;

    	// Dynamic data over the image
    	@ViewInject(R.id.tv_cashValue)
    	private TextView tvCashValue;    
    	@ViewInject(R.id.tv_winnerReward)
    	private TextView tvWinnerReward;    
    	@ViewInject(R.id.tv_contestName)
    	private TextView tvContestName;    
    	@ViewInject(R.id.tv_contestCoin)
    	private TextView tvContestCoin;   
    	@ViewInject(R.id.tv_beginDate)
    	private TextView tvBeginDate; 
    	@ViewInject(R.id.tv_colabrationNumber)
    	private TextView tvColabrationNumber;
    	
    	// Icons over the image
    	@ViewInject(R.id.icon_contest_coin)
    	private TextView iconContestCoin;    
    	@ViewInject(R.id.icon_clock)
    	private TextView iconClock;    
    	@ViewInject(R.id.icon_colabration)
    	private TextView iconColabration;    
    	@ViewInject(R.id.icon_location)
    	private TextView iconLocation;   
    	@ViewInject(R.id.icon_camera)
    	private TextView iconCamera;

    	@ViewInject(R.id.img_pb)
        private ProgressBar imgPb;
    	  
    	@ViewInject(R.id.tv_contestName2)
    	private TextView tvContestName2;    
    	@ViewInject(R.id.iv_rectBanner)
    	private ImageView ivRectBanner;   
    	@ViewInject(R.id.tv_winnerNums)
    	private TextView tvWinnerNums; 
    	@ViewInject(R.id.tv_description)
    	private TextView tvDescription;
    	
    	public void setContent(ItemRow row){
    		tvCashValue.setText(row.getCashValue());
    		tvWinnerReward.setText(row.getWinnerReward());
    		tvContestName.setText(row.getContestName());
    		tvContestCoin.setText(row.getContestCoin());
    		tvBeginDate.setText(row.getBeginDate());
    		tvColabrationNumber.setText(row.getColabrationNumber());

    		tvContestCoin.setText(row.getContestCoin());
    		tvBeginDate.setText(row.getBeginDate());
    		tvColabrationNumber.setText(row.getColabrationNumber());

    		// Update detail information
    		tvContestName2.setText(row.getContestName());
    		//          ivRectBanner
    		tvWinnerNums.setText(row.getWinnerNums());
    		tvDescription.setText(row.getDescription());
    	}
    }

    public class CustomBitmapLoadCallBack extends DefaultBitmapLoadCallBack<ImageView> {
        private final ImageItemHolder holder;

        public CustomBitmapLoadCallBack(ImageItemHolder holder) {
            this.holder = holder;
        }

        @Override
        public void onLoading(ImageView container, String uri, BitmapDisplayConfig config, long total, long current) {
            this.holder.imgPb.setProgress((int) (current * 100 / total));
        }

        @Override
        public void onLoadCompleted(ImageView container, String uri, Bitmap bitmap, BitmapDisplayConfig config, BitmapLoadFrom from) {
            //super.onLoadCompleted(container, uri, bitmap, config, from);
            fadeInDisplay(container, bitmap);
            this.holder.imgPb.setProgress(100);
        }
    }

    private static final ColorDrawable TRANSPARENT_DRAWABLE = new ColorDrawable(android.R.color.transparent);

    private void fadeInDisplay(ImageView imageView, Bitmap bitmap) {
        final TransitionDrawable transitionDrawable =
                new TransitionDrawable(new Drawable[]{
                        TRANSPARENT_DRAWABLE,
                        new BitmapDrawable(imageView.getResources(), bitmap)
                });
        imageView.setImageDrawable(transitionDrawable);
        transitionDrawable.startTransition(500);
    }
}
