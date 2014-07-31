package com.overture.questdroid.fragment;

import android.R.integer;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.*;

import com.android.volley.Response;
import com.android.volley.Request.Method;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.bitmap.BitmapCommonUtils;
import com.lidroid.xutils.util.LogUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.Date;

import org.json.JSONArray;
import org.json.JSONObject;

import com.overture.questdroid.utility.BitmapHelp;
import com.overture.questdroid.R;
import com.overture.questdroid.app.VolleyController;
import com.overture.questdroid.utility.CircleImageView;
import com.overture.questdroid.utility.CustomReqListener;
import com.overture.questdroid.utility.CustomRequest;
import com.overture.questdroid.utility.OptimizeGridView;
import com.overture.questdroid.utility.TabButton;

/**
 * Author: Samuel & Wei
 * Date: 14-6-29
 * Contests fragment: handle the image list of the contests and their related information
 */
public class ProfileFragment extends Fragment{

    public static BitmapUtils bitmapUtils;
    public static boolean isListView = true;
    public static boolean isQuestMedia = true;
    public final int pageIndex = 1;
    Typeface font;
    @ViewInject(R.id.profile_setting_icon)
    private TextView settingIcon;
    @ViewInject(R.id.profile_star_icon)
    private TextView avgStar;
    @ViewInject(R.id.profile_coin_icon1)
    private TextView coinIcon1;
    @ViewInject(R.id.profile_coin_icon2)
    private TextView coinIcon2;
    @ViewInject(R.id.profile_toggle_icon)
    private Button toggleIcon;
    @ViewInject(R.id.profile_grid_icon)
    private Button gridIcon;
    private TabButton profileLeft;
    private TabButton profileRight;  



    @ViewInject(R.id.profile_img_list)
    private ListView profileImgList;
    @ViewInject(R.id.profile_img_grid)
    private OptimizeGridView profileImgGrid;
    

    private ImageListAdapter imageListAdapter;
    private JSONObject user = VolleyController.getInstance().user;
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tab_fragment_profile, container, false); // Load fragment layout
        ViewUtils.inject(this, view); 		//Bind view and event
        
        bitmapUtils = BitmapHelp.getBitmapUtils(getActivity().getApplicationContext());
        bitmapUtils.configDefaultLoadingImage(R.drawable.ic_launcher);
        bitmapUtils.configDefaultLoadFailedImage(R.drawable.ic_launcher);
        bitmapUtils.configDefaultBitmapConfig(Bitmap.Config.RGB_565);
        
        profileLeft = (TabButton) getActivity().findViewById(R.id.btn_profile);
        profileRight = (TabButton) getActivity().findViewById(R.id.btn_reward);
        
        TextView profileHeader = (TextView) view.findViewById(R.id.profile_header_title);
        profileHeader.setText(user.optString("username"));
        CircleImageView userAvatar  = (CircleImageView) view.findViewById(R.id.profile_usr_avatar);
        bitmapUtils.display(userAvatar, user.optString("avatarUrl"));
        TextView profileUsername = (TextView) view.findViewById(R.id.profile_usrname);
        profileUsername.setText(user.optString("username"));
        TextView profileTotalCoin = (TextView) view.findViewById(R.id.profile_total_earned);
        profileTotalCoin.setText("Total Earned: "+user.optString("maxHistoryCoin"));
        TextView profileAvailableCoin = (TextView) view.findViewById(R.id.profile_available_coin);
        profileAvailableCoin.setText("Available: " + user.optString("coin"));
        TextView profileAvgRate= (TextView) view.findViewById(R.id.profile_average_star);
        //profileAvgRate.setText(user.optString("coin"));
        profileAvgRate.setText("5");
        // Load fondawesome icons
        font = Typeface.createFromAsset(getActivity().getAssets(), "fontawesome-webfont.ttf" );
        settingIcon.setTypeface(font);
        avgStar.setTypeface(font);
        coinIcon1.setTypeface(font);
        coinIcon2.setTypeface(font);
        toggleIcon.setTypeface(font);
        gridIcon.setTypeface(font);

        // Load image when scrolling (No loading if scroll too quickly)

        imageListAdapter = new ImageListAdapter(inflater.getContext());
        profileImgList.setAdapter(imageListAdapter);
        profileImgGrid.setAdapter(imageListAdapter);
        toggleIcon.setOnClickListener(new OnClickListener() {
        	@Override
        	public void onClick(View v) {
        		if(isQuestMedia) {
        			isQuestMedia = false;
        			imageListAdapter.clearPreviousData();
        			getUserQuest(pageIndex);
        		}
        		else {
        			isQuestMedia = true;
        			imageListAdapter.clearPreviousData();
        			getUserQuestMedia(pageIndex);
        		}
        	}
        });
        gridIcon.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
            	if(isListView) {
            		isListView = false;
            		profileImgList.setVisibility(View.GONE);
            		profileImgGrid.setVisibility(View.VISIBLE);
            	}
            	else{
            		isListView = true;
            		profileImgList.setVisibility(View.VISIBLE);
            		profileImgGrid.setVisibility(View.GONE);
            	}
            }
        });
        // Load URL, return the images from the urls and store into the list view
        getUserQuestMedia(pageIndex);//loadListData();

        return view;
    }

//    @OnItemClick(R.id.img_list)
//    public void onImageItemClick(AdapterView<?> parent, View view, int position, long id) {
////        Intent intent = new Intent(this.getActivity(), ImageActivity.class);
////        String s = imageListAdapter.getItem(position).toString();
////        intent.putExtra("url", s);
////        LogUtils.d("imageListAdapter.getItem(position=" + position + ") = " + s);
////        this.getActivity().startActivity(intent);
//    }
//    
    // 
    
    /**
     * Load the images from the top favorite list (test data)
     * @param url
     */
    
    public void getUserQuestMedia( final int mPageIndex){
    	final String tag = "json_getProfile_req";  

		Date date = new Date();

        // Get global list
		
        String uri = String.format("https://test.snaapiq.com/api2/user/%1$s/questMedia?date=%2$s&size=%3$s&page.page=%4$s",
                					user.optString("id"),
                					String.valueOf(date.getTime()),
                					"12",
                					String.valueOf(pageIndex));
            CustomRequest ProfileQMReq = new CustomRequest(Method.GET, uri, null, 
            										new Response.Listener<JSONObject>() {
											            @Override
											            public void onResponse(JSONObject response){
											            	VolleyController.getInstance().userlist = response;
											            	injectQMData(response);
											            	if(!response.optJSONObject("meta").optBoolean("lastPage")) {
											            		getUserQuestMedia(mPageIndex+1);
											            	}
											            }
            										},
            										CustomReqListener.ErrorListener(getActivity())
                                                    );
            VolleyController.getInstance().addToRequestQueue(ProfileQMReq, tag);
    }
    
    public void getUserQuest( final int mPageIndex){
    	final String tag = "json_getProfile_req";  

		//Date date = new Date();

        // Get global list
		
        String uri = String.format("https://test.snaapiq.com/api2/user/%1$s/quest?size=%2$s&page.page=%3$s",
                					user.optString("id"),
                					"12",
                					String.valueOf(pageIndex));
            CustomRequest ProfileListReq = new CustomRequest(Method.GET, uri, null, 
            										new Response.Listener<JSONObject>() {
											            @Override
											            public void onResponse(JSONObject response){
											            	VolleyController.getInstance().userlist = response;
											            	injectQuestData(response);
											            	if(!response.optJSONObject("meta").optBoolean("lastPage")) {
											            		getUserQuest(mPageIndex+1);
											            	}
											            }
            										},
            										CustomReqListener.ErrorListener(getActivity())
                                                    );
            VolleyController.getInstance().addToRequestQueue(ProfileListReq, tag);
    }
    
    /**
     * Load the images from the top favorite list
     * @param url
     */
    public void injectQMData(JSONObject userlist) {
    	JSONArray contentArr = userlist.optJSONArray("content");
        ArrayList<String> questName = new ArrayList<String>();
        ArrayList<Integer> avgStar = new ArrayList<Integer>();
        ArrayList<String> picUrl = new ArrayList<String>();
        //ArrayList<String> iswinner;
        
    	for(int i=0; i<contentArr.length(); ++i){
    		JSONObject content = contentArr.optJSONObject(i);
    		
        	//String isWinner = content.optString("iswinner");
        	questName.add(i, content.optJSONObject("quest").optString("name"));
        	avgStar.add(i, content.optInt("rateAvg"));
        	picUrl.add(i, content.optString("objectSignedUrl"));
    	}
        //image
    	imageListAdapter.updateData(questName, picUrl, avgStar);
        imageListAdapter.notifyDataSetChanged();	//notify the listview to update data
    }
    
    public void injectQuestData(JSONObject userlist) {
    	JSONArray contentArr = userlist.optJSONArray("content");
        ArrayList<String> questName = new ArrayList<String>();
        ArrayList<Integer> avgStar = new ArrayList<Integer>();
        ArrayList<String> picUrl = new ArrayList<String>();
        //ArrayList<String> iswinner;
        
    	for(int i=0; i<contentArr.length(); ++i){
    		JSONObject content = contentArr.optJSONObject(i);
    		
        	//String isWinner = content.optString("iswinner");
        	questName.add(i, content.optString("name"));
        	avgStar.add(i, content.optInt("rateAvg"));
        	picUrl.add(i, content.optJSONObject("relevantTopQuestMedia").optString("objectSignedUrl"));
    	}
        //image
    	imageListAdapter.updateData(questName, picUrl, avgStar);
        imageListAdapter.notifyDataSetChanged();	//notify the listview to update data
    }
    

    /**
     * Image Adapter that handles the image related data
     * @author samueltango
     *
     */
    public class ImageListAdapter extends BaseAdapter {

        private Context mContext;
        private final LayoutInflater mInflater;
        private ArrayList<String> mQuestName;
        private ArrayList<String> mImgUrl;
        private ArrayList<Integer> mAvgStar;
        //private ArrayList<String> contestNameList;
        
        public void clearPreviousData() {
        	mQuestName = new ArrayList<String>();
            mImgUrl = new ArrayList<String>();
            mAvgStar = new ArrayList<Integer>();
        }

        public ImageListAdapter(Context context) {
            super();
            this.mContext = context;
            mInflater = LayoutInflater.from(context);
            mQuestName = new ArrayList<String>();
            mImgUrl = new ArrayList<String>();
            mAvgStar = new ArrayList<Integer>();
            //contestNameList = new ArrayList<String>();

        }
        public void updateData (ArrayList<String> questName, ArrayList<String> imgUrl, ArrayList<Integer> AvgStar) {
        	mQuestName.addAll(questName);
        	mAvgStar.addAll(AvgStar);
        	mImgUrl.addAll(imgUrl);
        }
        
        @Override
        public int getCount() {
            return mImgUrl.size();
        }

        @Override
        public Object getItem(int position) {
            return mImgUrl.get(position);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(final int position, View view, ViewGroup parent) {
            
        	if(isListView) {
	        	ImageListHolder holder = null;
	            if (view == null) {
	                view = mInflater.inflate(R.layout.profile_list_view_item, null);
	                holder = new ImageListHolder();
	                ViewUtils.inject(holder, view);
	                view.setTag(holder);
	            } else {
	                holder = (ImageListHolder) view.getTag();
	            }
	            //holder.imgPb.setProgress(0);         
	            
	            //if(isWinner)
	            holder.profileIsWinner.setTypeface(font);
	            holder.profileAvgStar.setRating(2);
	            holder.profileQuestName.setText(mQuestName.get(position));
	            bitmapUtils.configDefaultBitmapMaxSize(BitmapCommonUtils.getScreenSize(getActivity()).scaleDown(3));
	            
	            // Display the images and related data in the view
	            LogUtils.d("===== position = " + position);
	            //bitmapUtils.display(holder.profileListItem, mImgUrl.get(position));
	            bitmapUtils.display(holder.profileListItem, "https://explora-snaapiq-media-test-b1.s3.amazonaws.com/eeee68e8-fe06-4943-9775-724694fdab37_profile_image.png");
	            
	       
	            return view;
        	}
        	else {
        		ImageGridHolder holder = null;
	            if (view == null) {
	                view = mInflater.inflate(R.layout.profile_grid_view_item, null);
	                holder = new ImageGridHolder();
	                ViewUtils.inject(holder, view);
	                view.setTag(holder);
	            } else {
	                holder = (ImageGridHolder) view.getTag();
	            }
	            //holder.imgPb.setProgress(0);            
	            holder.profileIsWinner.setTypeface(font);
	
	            //holder.profileQuestName.setText(mQuestName.get(position));
	            bitmapUtils.configDefaultBitmapMaxSize(BitmapCommonUtils.getScreenSize(getActivity()).scaleDown(3));
	            
	            // Display the images and related data in the view
	            LogUtils.d("===== position = " + position);
	            bitmapUtils.display(holder.profileGridItem, mImgUrl.get(position));
	            //bitmapUtils.display(holder.profileGridItem, "https://explora-snaapiq-media-test-b1.s3.amazonaws.com/eeee68e8-fe06-4943-9775-724694fdab37_profile_image.png");
	            
	       
	            return view;
        	}
        }
    }
    
    private class ImageListHolder {
    	@ViewInject(R.id.profile_list_item)
    	private ImageView profileListItem;

    	// Dynamic data over the image
    	@ViewInject(R.id.profile_iswinner)
    	private TextView profileIsWinner;    
    	@ViewInject(R.id.profile_ratingBar)
    	private RatingBar profileAvgStar;    
    	@ViewInject(R.id.profile_quest_decription)
    	private TextView profileQuestName;    
    	
    }
    
    private class ImageGridHolder {
    	@ViewInject(R.id.profile_grid_item)
    	private ImageView profileGridItem;

    	// Dynamic data over the image
    	@ViewInject(R.id.profile_grid_iswinner)
    	private TextView profileIsWinner;      
    	
    }

}
