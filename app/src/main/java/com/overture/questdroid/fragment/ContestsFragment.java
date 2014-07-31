package com.overture.questdroid.fragment;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Layout;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.bitmap.BitmapDisplayConfig;
import com.lidroid.xutils.bitmap.callback.BitmapLoadFrom;
import com.lidroid.xutils.bitmap.callback.DefaultBitmapLoadCallBack;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnItemClick;
import com.overture.questdroid.ContestsActivity;
import com.overture.questdroid.R;
import com.overture.questdroid.app.VolleyController;
import com.overture.questdroid.utility.BitmapHelp;
import com.overture.questdroid.utility.CustomRequest;
import com.overture.questdroid.utility.TextDrawable;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Author: Samuel
 * Date: 14-6-29
 * Contests fragment: handle the image list of the contests and their related information
 */
public class ContestsFragment extends Fragment{

    public static BitmapUtils bitmapUtils;
    public static boolean mIsLastPage = false;
    
    Typeface font;
    
    @ViewInject(R.id.icon_user_coin)
    private TextView iconUserCoin;
    @ViewInject(R.id.img_list)
    private ListView imageListView;
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
        
        TextDrawable loadingTopFavImage = new TextDrawable(getActivity());
        loadingTopFavImage.setTypeface(font);
        loadingTopFavImage.setText(getResources().getString(R.string.fa_contests));
        loadingTopFavImage.setTextSize(96);
        loadingTopFavImage.setTextColor(getResources().getColor(R.color.loadfailure_color));
        loadingTopFavImage.setTextAlign(Layout.Alignment.ALIGN_NORMAL);
        
        bitmapUtils = BitmapHelp.getBitmapUtils(this.getActivity().getApplicationContext());
        bitmapUtils.configDefaultBitmapConfig(Bitmap.Config.RGB_565);
        
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
		String uri;
		final int pageIndex = 1;
		
		double longitude = VolleyController.getInstance().longitude;
		double latitude = VolleyController.getInstance().latitude;
		
		// Get user information
    	uri = getActivity().getString(R.string.api_getUser);
        Map<String, String> params = new HashMap<String, String>();
        
        CustomRequest userReq = new CustomRequest(Method.GET, uri, params, 
        										successListenerUser(getActivity()),
        										ErrorListener(getActivity())
                                                );
        VolleyController.getInstance().addToRequestQueue(userReq, tag_getUser);  
        // Get global list        
        getImageGlobalList(pageIndex, this);
        if(longitude != 0 && latitude != 0) {   	
        	getImageLocalList(pageIndex, this, latitude, longitude);
        }
    }
    
    
    public void getImageLocalList(int pageIndex, Fragment fragment, double latitude, double longitude) {
    	
        //params = new HashMap<String, String>();
//    	double longitude = VolleyController.getInstance().longitude;
//		double latitude = VolleyController.getInstance().latitude;
    	String uri = String.format(getResources().getString(R.string.api_getImageLocalList),
				"2",
				String.valueOf(new Date().getTime()),
				String.valueOf(pageIndex),
				String.valueOf(latitude),
				String.valueOf(longitude));
        CustomRequest globalListReq = new CustomRequest(Method.GET, uri, null, 
        										successListenerLocalList(fragment, pageIndex,latitude, longitude),
        										ErrorListener(fragment.getActivity())
                                                );
        VolleyController.getInstance().addToRequestQueue(globalListReq, null);
    }
    
    public void getImageGlobalList(int pageIndex, Fragment fragment) {
    	
        //params = new HashMap<String, String>();
        
    	String uri = String.format(getResources().getString(R.string.api_getImageGlobalList),
				"2",
				String.valueOf(new Date().getTime()),
				String.valueOf(pageIndex));
        CustomRequest globalListReq = new CustomRequest(Method.GET, uri, null, 
        										successListenerGlobalList(fragment, pageIndex),
        										ErrorListener(fragment.getActivity())
                                                );
        VolleyController.getInstance().addToRequestQueue(globalListReq, null);
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
        	this.layoutResID = layoutResourceId;
        	
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
//            holder.imgPb.setProgress(0);            
            holder.iconContestCoin.setTypeface(font);
            holder.iconClock.setTypeface(font);
            holder.iconColabration.setTypeface(font);
            holder.iconLocation.setTypeface(font);
            holder.iconCamera.setTypeface(font);
            holder.iconWinnerNums.setTypeface(font);
            holder.setContent(rowData.get(position));
            
            // Display the images and related data in the view
//            LogUtils.d(" position = " + position);
            TextDrawable loadFailedTopFavImage = new TextDrawable(getActivity());
            loadFailedTopFavImage.setTypeface(font);
            loadFailedTopFavImage.setText(getResources().getString(R.string.fa_contests));
            loadFailedTopFavImage.setTextSize(96);
            loadFailedTopFavImage.setTextColor(getResources().getColor(R.color.loadfailure_color));
            loadFailedTopFavImage.setTextAlign(Layout.Alignment.ALIGN_NORMAL);

            String topFavImgSrc = rowData.get(position).getImgSrc();
//            FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams)holder.imgItem.getLayoutParams();fa-files-o [&#xf0c5;]
//            // Make the image square
//            lp.height = lp.width;
//        	holder.imgItem.setLayoutParams(lp);
            if(topFavImgSrc.isEmpty()){
            	holder.imgItem.setScaleType(ScaleType.CENTER);
            	holder.imgItem.setImageDrawable(loadFailedTopFavImage);
            }else{
            	holder.imgItem.setScaleType(ScaleType.FIT_XY);
            	bitmapUtils.display(holder.imgItem, topFavImgSrc);     
            }

            String rectBannerImgSrc = rowData.get(position).getRectBanner();
            if(rectBannerImgSrc.isEmpty() || rectBannerImgSrc.equals("null")){
            	holder.ivRectBanner.setImageResource(R.drawable.snaapiq_logo);
            }else{
            	bitmapUtils.display(holder.ivRectBanner, rectBannerImgSrc);
            }

            final HorizontalScrollView scrollView = (HorizontalScrollView)row.findViewById(R.id.h_scrollView);
            final FrameLayout mainLayout = (FrameLayout) ((LinearLayout)scrollView.getChildAt(0)).getChildAt(1);
            RelativeLayout ruleLayout = (RelativeLayout) ((LinearLayout)scrollView.getChildAt(0)).getChildAt(0);
            RelativeLayout detailLayout = (RelativeLayout) ((LinearLayout)scrollView.getChildAt(0)).getChildAt(2);
            android.view.ViewGroup.LayoutParams mainParams = mainLayout.getLayoutParams();
            android.view.ViewGroup.LayoutParams ruleParams = ruleLayout.getLayoutParams();
            android.view.ViewGroup.LayoutParams detailParams = detailLayout.getLayoutParams();
            WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
            Display display = wm.getDefaultDisplay();
            Point size = new Point();
            display.getSize(size);
            int width = size.x;
            int height = size.y;
            mainParams.width = width;
            mainParams.height = width;
            ruleParams.width = width;
            ruleParams.height = width;
            detailParams.width = width;
            detailParams.height = width;
            scrollView.post(new Runnable() {
                @Override
                public void run() {
                	scrollView.scrollTo(mainLayout.getLeft(), 0);
                } 
            });
            return row;
        }
    }
    
    private class ImageItemHolder {
    	
    	// 1. Image main view
    	@ViewInject(R.id.img_item)
    	private ImageView imgItem;


    	// 1.1 Dynamic data over the image
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
    	
    	// 1.2 Icons over the image
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

//    	@ViewInject(R.id.img_pb)
//        private ProgressBar imgPb;
    	  
    	// 2. Image details view
    	@ViewInject(R.id.tv_contestName2)
    	private TextView tvContestName2;    
    	@ViewInject(R.id.iv_rectBanner)
    	private ImageView ivRectBanner;   
    	@ViewInject(R.id.icon_winnerNums)
    	private TextView iconWinnerNums; 
    	@ViewInject(R.id.tv_winnerNums)
    	private TextView tvWinnerNums; 
    	@ViewInject(R.id.tv_description)
    	private TextView tvDescription;

    	// 3. Image rules view
    	@ViewInject(R.id.tv_rulesBody)
    	private TextView tvRulesBody;
    	
    	public void setContent(ItemRow row){
    		// Update main view
    		tvCashValue.setText(row.getCashValue());
    		tvWinnerReward.setText(row.getWinnerReward());
    		tvContestName.setText(row.getContestName());
    		tvContestCoin.setText(row.getContestCoin());
    		tvBeginDate.setText(row.getBeginDate());
    		tvColabrationNumber.setText(row.getColabrationNumber());

    		// Update details view
    		tvContestName2.setText(row.getContestName());
    		//          ivRectBanner
    		tvWinnerNums.setText(row.getWinnerNums());
    		tvDescription.setText(row.getDescription());

    		// Update rules view
    		tvRulesBody.setText(row.getRuleDescription());
    	}
    }

    public class CustomBitmapLoadCallBack extends DefaultBitmapLoadCallBack<ImageView> {
        private final ImageItemHolder holder;

        public CustomBitmapLoadCallBack(ImageItemHolder holder) {
            this.holder = holder;
        }

        @Override
        public void onLoading(ImageView container, String uri, BitmapDisplayConfig config, long total, long current) {
//            this.holder.imgPb.setProgress((int) (current * 100 / total));
        }

        @Override
        public void onLoadCompleted(ImageView container, String uri, Bitmap bitmap, BitmapDisplayConfig config, BitmapLoadFrom from) {
            //super.onLoadCompleted(container, uri, bitmap, config, from);
            fadeInDisplay(container, bitmap);
//            this.holder.imgPb.setProgress(100);
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

    public static Response.ErrorListener ErrorListener(final Context context) {
        return new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            	Toast.makeText(context, new String (error.getMessage() + "\n" +
            error.networkResponse.data), Toast.LENGTH_LONG).show();
            }
        };
    }

    public Response.Listener<JSONObject> successListenerGlobalList(final Fragment fragment, final int pageIndex) {
		return new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                	VolleyController.getInstance().globalList = response;
                	mIsLastPage = response.optJSONObject("meta").optBoolean("lastPage");
                	
                	JSONArray contentArr = VolleyController.getInstance().globalList.optJSONArray("content");
        
                	for(int i=0; i<contentArr.length(); ++i){
                		JSONObject content = contentArr.optJSONObject(i);
                		
                		// Get overview information
                    	String cashValue = "$" + content.optString("cashValue");
                    	String winnerReward = content.optString("winnerReward");
                    	String contestName = content.optString("name");
                    	String contestCoin = content.optString("winnerCoin");
                    	String beginDate = longToDayCounts(content.optLong("beginDate"), false);
                    	String Collabration = content.optString("collaborationTotal");
                    	
                		JSONObject topfav = content.optJSONObject("topFavorQuestMedia");
                		String imgUrl = "";
                		if(topfav!=null)
                			imgUrl = topfav.optString("objectSignedUrl");
                		
                    	// Get detailed information
                    	int winnerNums = Integer.parseInt(content.optString("winnerNums"));
                    	String strWinnerNums = "Up to " + winnerNums + " Possible Winner";
                    	if(winnerNums > 1) strWinnerNums += "s";
                    	String rectBanner = content.optString("rectBanner");		// To be updated!
                    	String description = content.optString("description");
                    	String ruleDescription = content.optString("description");		// To be updated!
                    	
                    	ContestsFragment cFragment = (ContestsFragment)fragment;
                    	ItemRow row = new ItemRow(imgUrl, cashValue, winnerReward, contestName, 
                    			contestCoin, beginDate, Collabration,
                    			rectBanner, strWinnerNums, description, ruleDescription);
                    	cFragment.injectListData(row);
                	}
                	if(!mIsLastPage)
                		getImageGlobalList(pageIndex+1, fragment);
                	
                } catch (Exception e) {
					e.printStackTrace();
				}
            }
        };
    }
    
    public Response.Listener<JSONObject> successListenerLocalList(final Fragment fragment, final int pageIndex, final double latitude, final double longitude) {
		return new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                	VolleyController.getInstance().globalList = response;
                	mIsLastPage = response.optJSONObject("meta").optBoolean("lastPage");
                	
                	JSONArray contentArr = VolleyController.getInstance().globalList.optJSONArray("content");
        
                	for(int i=0; i<contentArr.length(); ++i){
                		JSONObject content = contentArr.optJSONObject(i);
                		
                		// Get overview information
                    	String cashValue = content.optString("cashValue");
                    	String winnerReward = content.optString("winnerReward");
                    	String contestName = content.optString("name");
                    	String contestCoin = content.optString("winnerCoin");
                    	String beginDate = longToDayCounts(content.optLong("beginDate"), false);
                    	String Collabration = content.optString("collaborationTotal");
                    	
                		JSONObject topfav = content.optJSONObject("topFavorQuestMedia");
                		String imgUrl = "";
                		if(topfav!=null)
                			imgUrl = topfav.optString("objectSignedUrl");
                		
                    	// Get detailed information
                    	int winnerNums = Integer.parseInt(content.optString("winnerNums"));
                    	String strWinnerNums = "Up to " + winnerNums + " Possible Winner";
                    	if(winnerNums > 1) strWinnerNums += "s";
                    	String rectBanner = imgUrl;		// To be updated!
                    	String description = content.optString("description");
                    	String ruleDescription = content.optString("description");		// To be updated!
                    	
                    	ContestsFragment cFragment = (ContestsFragment)fragment;
                    	ItemRow row = new ItemRow(imgUrl, cashValue, winnerReward, contestName, 
                    			contestCoin, beginDate, Collabration,
                    			rectBanner, strWinnerNums, description, ruleDescription);
                    	cFragment.injectListData(row);
                    	cFragment.imageListAdapter.notifyDataSetChanged();
                	}
                	if(!mIsLastPage)
                		getImageLocalList(pageIndex+1, fragment, latitude, longitude);
                	
                } catch (Exception e) {
					e.printStackTrace();
				}
            }
        };
    }
    
	
	/**
	 * Function to convert the date long to a valid day count number
	 */
	public static String longToDayCounts(long beginDateLong, boolean plFlag){
		String result = "";
		double leftDays = (((new Date().getTime() - beginDateLong) / 1000.0) / 3600.0) / 24.0;
		int leftWeek, leftMonth, leftYear;
		if(leftDays < 7){			// Less than 1 week
			if(leftDays < 2)
				result = "1d";
			else{
				result = (int)leftDays + "d";
				if(plFlag) result += "s";
			}
		}else if(leftDays < 30){	// Less than 1 month
			leftWeek = (int)(leftDays / 7);
			if(leftWeek < 2)
				result = "1w";
			else{
				result = leftWeek + "w";
				if(plFlag) result += "s";
			}
		}else if(leftDays < 365){	// Less than 1 year
			leftMonth = (int)(leftDays / 30);
			if(leftMonth < 2)
				result = "1m";
			else{
				result = leftMonth + "m";
				if(plFlag) result += "s";
			}
		}else if(leftDays < 365 * 2){
			result = "1y";
		}else{
			leftYear = (int)(leftDays / 365);
			result = leftYear + "y";
			if(plFlag) result += "s";
		}
		
		return result;		
	}

	public static Response.Listener<JSONObject> successListenerUser(final Context context) {
		
		return new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                	VolleyController.getInstance().user = response;

                	JSONObject user = VolleyController.getInstance().user;	
                	String coin = user.getString("coin");
                    
                	ContestsActivity ca = (ContestsActivity)context;
                    View view = ca.findViewById(android.R.id.content);
                    TextView userCoin = (TextView)view.findViewById(R.id.tv_user_coin_amount);
                    userCoin.setText(coin);
                } 
                catch (Exception e) {
                	Toast.makeText(context, "fail to request the data", Toast.LENGTH_SHORT).show();
					e.printStackTrace();
				}
            }
        };
    }
}
