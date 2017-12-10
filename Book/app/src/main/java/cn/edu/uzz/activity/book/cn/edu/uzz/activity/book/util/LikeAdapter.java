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
import cn.edu.uzz.activity.book.cn.edu.uzz.activity.book.entity.Like;

/**
 * Created by 10616 on 2017/12/3.
 */

public class LikeAdapter extends BaseAdapter implements AbsListView.OnScrollListener,View.OnClickListener{
	private List<Like> mList;
	private LayoutInflater inflater;
	private LikeAdapter.ViewHolder viewHolder;
	private LikeImageLoader mImageLoader;
	private int mStart,mEnd;
	public static String[] URLS;
	private boolean mFirstIn;
	private CallBack mcallBack;

	public LikeAdapter(Context context, List<Like> data, ListView listView,CallBack callBack) {
		this.mList = data;
		mcallBack=callBack;
		this.inflater = LayoutInflater.from(context);
		mImageLoader=new LikeImageLoader(listView);
		URLS=new String[data.size()];
		for (int i=0;i<data.size();i++){
			URLS[i]=data.get(i).getPicture();
		}
		mFirstIn=true;
		//注册对应的事件
		listView.setOnScrollListener(this);
	}

	public void refresh(List<Like> list) {
		mList = list;//传入list，然后调用notifyDataSetChanged方法
		notifyDataSetChanged();
	}

	@Override
	public void onClick(View view) {
		mcallBack.click(view);
	}

	//定义一个接口用于回调点击事件
	public interface CallBack{
		public void click(View v);
	}

	public void onDateChange(ArrayList<Like> like_list) {
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
		Like like = mList.get(position);
		if (convertView == null) {
			viewHolder = new LikeAdapter.ViewHolder();
			convertView = inflater.inflate(R.layout.activity_likelist, null);
			viewHolder.book_name=convertView.findViewById(R.id.like_name);
			viewHolder.book_pic=convertView.findViewById(R.id.like_pic);
			viewHolder.cancelBtn=convertView.findViewById(R.id.cancel_like);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (LikeAdapter.ViewHolder) convertView.getTag();
		}
		viewHolder.book_pic.setImageResource(R.mipmap.ic_launcher);
		String url=mList.get(position).getPicture();
		viewHolder.book_pic.setTag(url);
		//new ImageLoader().showImageByAsyncTask(viewHolder.ivIcon,url);
		mImageLoader.showImageByAsyncTask(viewHolder.book_pic,url);
		viewHolder.book_name.setText(mList.get(position).getBookname());
		viewHolder.cancelBtn.setOnClickListener(this);
		viewHolder.cancelBtn.setTag(position);
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
		private TextView book_name;
		/*private TextView book_writer;
		private TextView book_publish;
		private TextView book_version;*/
		private ImageView book_pic;
		private TextView cancelBtn;
	}



}
