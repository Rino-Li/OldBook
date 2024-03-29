package cn.edu.uzz.activity.book.cn.edu.uzz.activity.book.loader;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.util.LruCache;
import android.widget.ImageView;
import android.widget.ListView;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashSet;
import java.util.Set;

import cn.edu.uzz.activity.book.R;
import cn.edu.uzz.activity.book.cn.edu.uzz.activity.book.adapter.CarAdapter;

/**
 * Created by 10616 on 2017/12/16.
 */

public class CarImageLoader {
	private ImageView mImageView;
	private LruCache<String,Bitmap> mCaches;
	private String mUrl;
	private ListView mListView;
	private Set<CarAsyncTask> mTask;

	public CarImageLoader(ListView listView){
		mListView=listView;
		mTask=new HashSet<>();
		int maxMemory= (int) Runtime.getRuntime().maxMemory();
		int cacheSize=maxMemory/4;
		mCaches=new LruCache<String,Bitmap>(cacheSize){
			@Override
			protected int sizeOf(String key, Bitmap value) {
				//在每次存入缓存时调用
				return value.getByteCount();
			}
		};

	}

	public void addBitmapToCache(String url,Bitmap bitmap){
		if(getBitmapFromCache(url)==null){
			mCaches.put(url,bitmap);
		}
	}

	public Bitmap getBitmapFromCache(String url){
		return mCaches.get(url);
	}



	private Handler mHandler=new Handler(){
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if(mImageView.getTag().equals(mUrl)){
				mImageView.setImageBitmap((Bitmap) msg.obj);
			}
		}
	};

	public Bitmap getBitmapFromUrl(String urlString){
		Bitmap bitmap;
		InputStream is=null;
		try {
			URL url=new URL(urlString);
			HttpURLConnection connection= (HttpURLConnection) url.openConnection();
			is=new BufferedInputStream(connection.getInputStream());
			bitmap= BitmapFactory.decodeStream(is);
			connection.disconnect();
			return bitmap;
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	public void showImageByAsyncTask(ImageView imageView, final String url){
		mImageView=imageView;
		mUrl=url;
		//从缓存中取出对应图片
		Bitmap bitmap=getBitmapFromCache(url);
		//若缓存没有，则加载
		if(bitmap==null){
			imageView.setImageResource(R.mipmap.ic_launcher);
		}else {
			imageView.setImageBitmap(bitmap);
		}
	}

	public void loadImages(int start,int end){
		for (int i=start;i<end;i++){
			String url= CarAdapter.URLS[i];
			//从缓存中取出对应图片
			Bitmap bitmap=getBitmapFromCache(url);
			//若缓存没有，则加载
			if(bitmap==null){
				CarAsyncTask task=new CarAsyncTask(url);
				task.execute(url);
				mTask.add(task);

			}else {
				ImageView imageView= (ImageView) mListView.findViewWithTag(url);
				imageView.setImageBitmap(bitmap);
			}
		}
	}

	public void cancelAllTasks() {
		if(mTask!=null){
			for (CarAsyncTask task:mTask){
				task.cancel(false);
			}
		}
	}

	private class CarAsyncTask extends AsyncTask<String ,Void,Bitmap> {

		//private ImageView mImageView;
		private String mUrl;

		public CarAsyncTask(String url){
			mUrl=url;
		}
		@Override
		protected Bitmap doInBackground(String... strings) {
			String url=strings[0];
			//从网络获取图片
			Bitmap bitmap=getBitmapFromUrl(url);
			if(bitmap!=null){
				//将不在缓存的图片加入缓存
				addBitmapToCache(url,bitmap);
			}
			return bitmap;
		}
		@Override
		protected void onPostExecute(Bitmap bitmap) {
			super.onPostExecute(bitmap);
			ImageView imageView= (ImageView) mListView.findViewWithTag(mUrl);
			if(imageView!=null&&bitmap!=null){
				imageView.setImageBitmap(bitmap);
			}
			mTask.remove(this);
		}
	}
}
