package cn.edu.uzz.activity.book.cn.edu.uzz.activity.book.util;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cn.edu.uzz.activity.book.R;
import cn.edu.uzz.activity.book.cn.edu.uzz.activity.book.entity.Rent;

/**
 * Created by 10616 on 2018/2/3.
 */

public class RentingAdapter extends BaseAdapter implements AbsListView.OnScrollListener{
	private List<Rent> mList;
	private LayoutInflater inflater;
	private ViewHolder viewHolder;
	private RentingImageLoader mImageLoader;
	private int mStart,mEnd;
	public static String[] URLS;
	private boolean mFirstIn;

	public RentingAdapter(Context context, List<Rent> data, ListView listView) {
		this.mList = data;
		this.inflater = LayoutInflater.from(context);
		mImageLoader=new RentingImageLoader(listView);
		URLS=new String[data.size()];
		for (int i=0;i<data.size();i++){
			URLS[i]=data.get(i).getPicture();
		}
		mFirstIn=true;
		//注册对应的事件
		listView.setOnScrollListener(this);
	}

	public void refresh(List<Rent> list) {
		mList = list;//传入list，然后调用notifyDataSetChanged方法
		notifyDataSetChanged();
	}

	//定义一个接口用于回调点击事件
	public interface CallBack{
		public void click(View v);
	}

	public void onDateChange(ArrayList<Rent> like_list) {
		this.mList = like_list;
		this.notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		return mList.size();

	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return mList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		Rent car = mList.get(position);
		if (convertView == null) {
			viewHolder = new ViewHolder();
			convertView = inflater.inflate(R.layout.item_renting, null);
			viewHolder.reinting_book_name= (TextView) convertView.findViewById(R.id.renting_bookname);
			viewHolder.renting_bookpic= (ImageView) convertView.findViewById(R.id.renting_bookpic);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		viewHolder.renting_bookpic.setImageResource(R.mipmap.ic_launcher);
		String url=mList.get(position).getPicture();
		viewHolder.renting_bookpic.setTag(url);
		//new ImageLoader().showImageByAsyncTask(viewHolder.ivIcon,url);
		mImageLoader.showImageByAsyncTask(viewHolder.renting_bookpic,url);
		viewHolder.reinting_book_name.setText(mList.get(position).getBookanme());
		return convertView;
	}

	@Override
	public void onScrollStateChanged(AbsListView absListView, int i) {
		if (i==SCROLL_STATE_IDLE){
			//加载可见项
			mImageLoader.loadImages(mStart,mEnd);
		}else{
			//停止加载任务
			mImageLoader.cancelAllTasks();
		}
	}

	@Override
	public void onScroll(AbsListView absListView, int firstVisibleItem, int visibleItemCount, int i2) {
		mStart=firstVisibleItem;
		mEnd=firstVisibleItem+visibleItemCount;
		//第一次现实的时候调用
		if(mFirstIn&&visibleItemCount>0){
			mImageLoader.loadImages(mStart,mEnd);
			mFirstIn=false;
		}
	}

	class ViewHolder {
		TextView reinting_book_name;
		TextView renting_endtime;
		ImageView renting_bookpic;

	}



}
