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

import java.util.List;

import cn.edu.uzz.activity.book.R;
import cn.edu.uzz.activity.book.cn.edu.uzz.activity.book.entity.RentCar;

/**
 * Created by 10616 on 2017/12/16.
 */

public class CarAdapter extends BaseAdapter implements AbsListView.OnScrollListener,View.OnClickListener{
	private List<RentCar> mList;
	private LayoutInflater inflater;
	private ViewHolder viewHolder;
	private CarImageLoader imageLoader;
	private int mStart,mEnd;
	public static String[] URLS;
	private boolean mFirstIn;
	private CallBack mcallBack;

	public CarAdapter(Context context, List<RentCar> data, ListView listView,CallBack callBack){
		this.mList = data;
		mcallBack=callBack;
		this.inflater = LayoutInflater.from(context);
		imageLoader=new CarImageLoader(listView);
		URLS=new String[data.size()];
		for (int i=0;i<data.size();i++){
			URLS[i]=data.get(i).getPicture();
		}
		mFirstIn=true;
		//注册对应的事件
		listView.setOnScrollListener(this);
	}

	//定义一个接口用于回调点击事件
	public interface CallBack{
		public void click(View v);
	}

	@Override
	public int getCount() {
		return mList.size();
	}

	@Override
	public Object getItem(int position) {
		return mList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View view, ViewGroup viewGroup) {
		RentCar rentCar=mList.get(position);
		if(view==null){
			viewHolder=new ViewHolder();
			view=inflater.inflate(R.layout.item_rentcar,null);
			viewHolder.car_book_name=view.findViewById(R.id.car_bookname);
			viewHolder.car_bookpic=view.findViewById(R.id.car_bookpic);
			viewHolder.car_endtime=view.findViewById(R.id.car_endtime);
			viewHolder.cancelBtn=view.findViewById(R.id.cancel_car);
			view.setTag(viewHolder);
		}else {
			viewHolder = (ViewHolder) view.getTag();
		}
		viewHolder.car_bookpic.setImageResource(R.mipmap.ic_launcher);
		String url=mList.get(position).getPicture();
		viewHolder.car_bookpic.setTag(url);
		imageLoader.showImageByAsyncTask(viewHolder.car_bookpic,url);
		viewHolder.car_book_name.setText(mList.get(position).getBookanme());
		viewHolder.car_endtime.setText(mList.get(position).getEndtime());
		viewHolder.cancelBtn.setOnClickListener(this);
		viewHolder.cancelBtn.setTag(position);
		return view;
	}

	@Override
	public void onScrollStateChanged(AbsListView absListView, int i) {
		if (i==SCROLL_STATE_IDLE){
			//加载可见项
			imageLoader.loadImages(mStart,mEnd);
		}else{
			//停止加载任务
			imageLoader.cancelAllTasks();
		}
	}

	@Override
	public void onScroll(AbsListView absListView, int firstVisibleItem, int visibleItemCount, int i2) {
		mStart=firstVisibleItem;
		mEnd=firstVisibleItem+visibleItemCount;
		//第一次现实的时候调用
		if(mFirstIn&&visibleItemCount>0){
			imageLoader.loadImages(mStart,mEnd);
			mFirstIn=false;
		}
	}

	public void refresh(List<RentCar> list) {
		mList = list;//传入list，然后调用notifyDataSetChanged方法
		notifyDataSetChanged();
	}


	@Override
	public void onClick(View view) {
		mcallBack.click(view);
	}

	class ViewHolder {

		TextView car_book_name;
		TextView car_endtime;
		ImageView car_bookpic;
		private TextView cancelBtn;

	}
}
