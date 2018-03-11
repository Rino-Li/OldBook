package cn.edu.uzz.activity.book.ui;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

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
import cn.edu.uzz.activity.book.adapter.MyAdapter;
import cn.edu.uzz.activity.book.entity.Books;

public class SearchResultActivity extends AppCompatActivity implements View.OnClickListener,AdapterView.OnItemClickListener{

	private ImageView returnBtn;
	String search;
	private ListView listView;
	private List<Books> book_list;
	private static  String URL="http://123.206.230.120/Book/searchServ?searchName=";
	private TextView search_fail_text;
	private ImageView search_fail_img;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search_result);
		search=getIntent().getStringExtra("searchName");
		initView();
		new NewsAsyncTask().execute(URL+search);
	}

	private void initView() {
		listView= (ListView) findViewById(R.id.searchlist);
		returnBtn= (ImageView) findViewById(R.id.search_return);
		search_fail_img=findViewById(R.id.search_fail_img);
		search_fail_text=findViewById(R.id.search_fail_text);
		returnBtn.setOnClickListener(this);
		listView.setOnItemClickListener(this);
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()){
			case R.id.search_return:
				finish();
				overridePendingTransition(R.anim.anim_in,R.anim.anim_out);
				break;
			default:
				break;
		}
	}

	@Override
	public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
		Books obj=book_list.get(position);
		Intent intent=new Intent();
		intent.putExtra("book",obj);
		intent.setClass(SearchResultActivity.this,BookItemActivity.class);
		startActivity(intent);
	}

	class NewsAsyncTask extends AsyncTask<String, Void, List<Books>> {
		@Override
		protected List<Books> doInBackground(String... strings) {
			return getJsonDate(strings[0]);
		}

		@Override
		protected void onPostExecute(List<Books> book) {
			super.onPostExecute(book);
			MyAdapter adapter=new MyAdapter(SearchResultActivity.this,book,listView);
			listView.setAdapter(adapter);
		}
	}

	private List<Books> getJsonDate(String URL) {
		book_list=new ArrayList<>();
		try {
			String jsonString=readStream(new URL(URL).openStream());
			JSONObject jsonObject;
			Books books;
			try {
				jsonObject=new JSONObject(jsonString);
				JSONArray jsonArray=jsonObject.getJSONArray("searchlist");
				if (jsonArray.length()!=0){
					new Thread(new Runnable() {
						@Override
						public void run() {
							mHandler.post(new Runnable() {
								@Override
								public void run() {
									search_fail_img.setVisibility(View.INVISIBLE);
									search_fail_text.setVisibility(View.INVISIBLE);
								}
							});
						}
					}).start();
				}
				for (int i=0;i<jsonArray.length();i++){
					jsonObject=jsonArray.getJSONObject(i);
					books=new Books();
					books.setId(jsonObject.getInt("id"));
					books.setName(jsonObject.getString("name"));
					books.setPublish(jsonObject.getString("publish"));
					books.setVersion(jsonObject.getString("version"));
					books.setPicture("http://123.206.230.120/Book/images/img/"+jsonObject.getString("picture"));
					books.setRentstatus(jsonObject.getString("rentstatus"));
					books.setSubstatus(jsonObject.getString("substatus"));
					books.setPrice(jsonObject.getString("price"));
					books.setTime(jsonObject.getString("time"));
					books.setISBN(jsonObject.getString("ISBN"));
					books.setWriter(jsonObject.getString("writer"));
					books.setType(jsonObject.getInt("type"));
					book_list.add(books);
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
	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if (msg.what == 100) {
			}
		}
	};
}
