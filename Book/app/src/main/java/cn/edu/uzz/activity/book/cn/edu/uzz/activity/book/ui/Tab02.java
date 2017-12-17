package cn.edu.uzz.activity.book.cn.edu.uzz.activity.book.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

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
import cn.edu.uzz.activity.book.cn.edu.uzz.activity.book.entity.RentCar;
import cn.edu.uzz.activity.book.cn.edu.uzz.activity.book.util.CarAdapter;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by 10616 on 2017/11/17.
 */

public class Tab02 extends Fragment implements View.OnClickListener{
	private Button makeBtn;
	private ListView mListView;
	 List<RentCar> book_list;
	 String account;
	 CarAdapter carAdapter;
	private static  String URL="http://123.206.230.120/Book/getcarServ?account=";
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.tab02, container, false);
    }

	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		initView();
		SharedPreferences pre=getActivity().getSharedPreferences("user",MODE_PRIVATE);
		account=pre.getString("account","");
		new NewsAsyncTask().execute(URL+2);
	}

	private void initView() {
    	makeBtn=getView().findViewById(R.id.jinruerweima);
    	makeBtn.setOnClickListener(this);
    	mListView=getView().findViewById(R.id.car_list);
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()){
			case R.id.jinruerweima:
				Intent intent=new Intent();
				/*intent.putExtra("type",1);
				intent.putExtra("id",1);*/
				intent.setClass(getActivity(),ErweimaActivity.class);
				startActivity(intent);
		}
	}

	class NewsAsyncTask extends AsyncTask<String, Void, List<RentCar>> {

		@Override
		protected List<RentCar> doInBackground(String... strings) {
			return getJsonDate(strings[0]);
		}

		@Override
		protected void onPostExecute(List<RentCar> book) {
			super.onPostExecute(book);
			carAdapter=new CarAdapter(getActivity(),book,mListView);
			mListView.setAdapter(carAdapter);
		}
	}

	private List<RentCar> getJsonDate(String URL) {
		book_list=new ArrayList<>();
		try {
			String jsonString=readStream(new URL(URL).openStream());
			JSONObject jsonObject;
			RentCar books;
			try {
				jsonObject=new JSONObject(jsonString);
				JSONArray jsonArray=jsonObject.getJSONArray("json");
				for (int i=0;i<jsonArray.length();i++){
					jsonObject=jsonArray.getJSONObject(i);
					books=new RentCar();
					books.setAccount(jsonObject.getString("account"));
					books.setPicture(jsonObject.getString("picture"));
					books.setBookid(jsonObject.getInt("bookid"));
					books.setBooktype(jsonObject.getInt("booktype"));
					books.setBookanme(jsonObject.getString("bookname"));
					books.setEndtime(jsonObject.getString("willtime"));
					books.setNowdate(jsonObject.getString("nowdate"));
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
}
