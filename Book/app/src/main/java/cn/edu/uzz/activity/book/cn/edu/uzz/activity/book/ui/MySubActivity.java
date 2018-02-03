package cn.edu.uzz.activity.book.cn.edu.uzz.activity.book.ui;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import cn.edu.uzz.activity.book.R;
import cn.edu.uzz.activity.book.DemoApplication;
import cn.edu.uzz.activity.book.cn.edu.uzz.activity.book.entity.Subscribe;
import cn.edu.uzz.activity.book.cn.edu.uzz.activity.book.util.SubAdapter;

/**
 * Created by 10616 on 2017/12/9.
 */

public class MySubActivity extends Activity implements AdapterView.OnItemClickListener,View.OnClickListener{
	private ListView listView;
	List<Subscribe> book_list;
	private static  String URL="http://123.206.230.120/Book/getsubServ?account=";
	String account;
	private ImageView sub_return;
	private TextView cancel_sub;
	SubAdapter adapter;
	private TextView sub_fail_text;
	private ImageView sub_fail_img;
	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mysub);
		initView();
		SharedPreferences pre=getSharedPreferences("user",MODE_PRIVATE);
		account=pre.getString("account","");
		if(account.equals("")){
			Intent intent=new Intent();
			intent.putExtra("title","我的预定");
			intent.setClass(MySubActivity.this,NoLoginActivity.class);
			startActivity(intent);
			finish();
		}
		new MySubActivity.NewsAsyncTask().execute(URL+account);
		listView.setOnItemClickListener(this);
	}

	private void initView() {
		sub_return= (ImageView) findViewById(R.id.sub_return);
		listView= (ListView) findViewById(R.id.sublist);
		sub_fail_text=findViewById(R.id.sub_fail_text);
		sub_fail_img=findViewById(R.id.sub_fail_img);
		sub_return.setOnClickListener(this);
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()){
			case R.id.sub_return:
				finish();
				overridePendingTransition(R.anim.anim_in,R.anim.anim_out);
				break;
			default:
				break;
		}
	}

	@Override
	public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {//点击进入书籍详情界面
		Subscribe obj=book_list.get(position);
		Intent intent=new Intent();
		intent.putExtra("type",obj.getBooktype());
		intent.putExtra("id",obj.getBookid());
		intent.setClass(MySubActivity.this,SpecialItemActivity.class);//后期改成特殊的 book页面
		startActivity(intent);
		overridePendingTransition(R.anim.anim_in,R.anim.anim_out);
	}

	class NewsAsyncTask extends AsyncTask<String, Void, List<Subscribe>> implements SubAdapter.CallBack ,AdapterView.OnItemClickListener{

		@Override
		protected List<Subscribe> doInBackground(String... strings) {
			return getJsonDate(strings[0]);
		}

		@Override
		protected void onPostExecute(List<Subscribe> book) {
			super.onPostExecute(book);
			adapter=new SubAdapter(MySubActivity.this,book,listView, this);
			listView.setAdapter(adapter);
			listView.setOnItemClickListener(this);
		}

		@Override
		public void click(View v) {//点击取消收藏按钮，通过getTag（）获得position
			int position=(Integer) v.getTag();
			int type=book_list.get(position).getBooktype();
			int id=book_list.get(position).getBookid();
			cancelSubBook(account,type,id,position);
		}

		@Override
		public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {//点击item，通过position获取type和id，进入详情页后加载信息
			Subscribe obj=book_list.get(position);
			Intent intent=new Intent();
			intent.putExtra("type",obj.getBooktype());
			intent.putExtra("id",obj.getBookid());
			intent.setClass(MySubActivity.this,SpecialItemActivity.class);//后期改成特殊的 book页面
			startActivity(intent);
		}
	}

	private List<Subscribe> getJsonDate(String URL) {
		book_list=new ArrayList<>();
		if (account.equals("")){
			return book_list;
		}
		try {
			String jsonString=readStream(new URL(URL).openStream());
			JSONObject jsonObject;
			Subscribe subscribe;
			try {
				jsonObject=new JSONObject(jsonString);
				JSONArray jsonArray=jsonObject.getJSONArray("json");
				if (jsonArray.length()!=0){
					new Thread(new Runnable() {
						@Override
						public void run() {
							mHandler.post(new Runnable() {
								@Override
								public void run() {
									sub_fail_img.setVisibility(View.INVISIBLE);
									sub_fail_text.setVisibility(View.INVISIBLE);
								}
							});
						}
					}).start();
				}
				for (int i=0;i<jsonArray.length();i++){
					jsonObject=jsonArray.getJSONObject(i);
					subscribe=new Subscribe();
					subscribe.setAccount(jsonObject.getString("account"));
					subscribe.setBookname(jsonObject.getString("bookname"));
					subscribe.setBooktype(jsonObject.getInt("booktype"));
					subscribe.setBookid(jsonObject.getInt("bookid"));
					subscribe.setPicture(jsonObject.getString("picture"));
					book_list.add(subscribe);
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return book_list;

	}

	private String readStream(InputStream is){
		InputStreamReader isr;
		String result="";
		try {
			String line="";
			isr=new InputStreamReader(is,"utf-8");
			BufferedReader br=new BufferedReader(isr);
			while ((line=br.readLine())!=null){
				result+=line;
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return result;
	}

	private void cancelSubBook(String account, int type, int id, final int position) {
		//1创建请求队列
		RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
		//2创建请求
		JsonObjectRequest request = new JsonObjectRequest(
				"http://123.206.230.120/Book/cancelsubServ?account=" + account +  "&bookid=" +id+ "&booktype=" + type, null, new Response.Listener<JSONObject>() {
			@Override
			public void onResponse(JSONObject response) {
				try {
					int resultCode = response.getInt("resultCode");
					if (resultCode == 1||resultCode==3) {
						Toast.makeText(MySubActivity.this,"取消预定成功",Toast.LENGTH_SHORT).show();
						book_list.remove(position);
						adapter.refresh(book_list);
					}else if (resultCode==2){
						Toast.makeText(MySubActivity.this,"取消预定失败，请稍后再试！",Toast.LENGTH_SHORT).show();
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		}, new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError volleyError) {

				Toast.makeText(MySubActivity.this,"网络异常，请稍后再试",Toast.LENGTH_SHORT).show();
			}
		});
		//3请求加入队列
		request.setTag("mysub");
		queue.add(request);
	}

	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if (msg.what == 100) {
			}
		}
	};

	@Override
	protected void onStop() {
		super.onStop();
		DemoApplication.getHttpQueues().cancelAll("mysub");
	}
}