package cn.edu.uzz.activity.book.cn.edu.uzz.activity.book.ui;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
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
import java.util.Random;

import cn.edu.uzz.activity.book.R;
import cn.edu.uzz.activity.book.cn.edu.uzz.activity.book.entity.Books;
import cn.edu.uzz.activity.book.cn.edu.uzz.activity.book.util.MyAdapter;

/**
 * Created by 10616 on 2017/11/17.
 */

public class Tab01 extends Fragment implements AdapterView.OnItemClickListener,View.OnClickListener{
    private ListView mListView;
    private static  String URL="http://123.206.230.120/Book/getoldlistServ?type1=";
    List<Books> book_list;
    private ImageView pahhangbangBtn;
    private ImageView booktypesBtn;
    private ImageView newbookBtn;
    int type1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.tab01, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView();
        mListView=getView().findViewById(R.id.listviewmain);
        type1=getbookstype1();
        new NewsAsyncTask().execute(URL+type1);
        mListView.setOnItemClickListener(this);
    }

    private void initView() {
        pahhangbangBtn=getView().findViewById(R.id.paihangbang);
        booktypesBtn=getView().findViewById(R.id.booktypes);
        newbookBtn=getView().findViewById(R.id.newbook);
        pahhangbangBtn.setOnClickListener(this);
        booktypesBtn.setOnClickListener(this);
        newbookBtn.setOnClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
        /*int i=position;
        RepairInfor obj=repairs.get(i);
        Intent intent=new Intent();
        intent.putExtra("repinfor",repairs.get(i));
        intent.setClass(RepairListActivity.this,Rep_ItemActivity.class);
        startActivity(intent);*/
        Books obj=book_list.get(position);
        Intent intent=new Intent();
        intent.putExtra("book",obj);
        intent.setClass(getActivity(),BookItemActivity.class);
        startActivity(intent);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.booktypes:
                startActivity(new Intent(getActivity(),TypesActivity.class));
                break;
            case R.id.newbook:
                startActivity(new Intent(getActivity(),LoginActivity.class));
                break;
            case R.id.paihangbang:
                startActivity(new Intent(getActivity(),LikeListAvtivity.class));
                break;
            default:
                break;
        }
    }


    class NewsAsyncTask extends AsyncTask<String, Void, List<Books>> {

        @Override
        protected List<Books> doInBackground(String... strings) {
            return getJsonDate(strings[0]);
        }

        @Override
        protected void onPostExecute(List<Books> book) {
            super.onPostExecute(book);
            MyAdapter adapter=new MyAdapter(getActivity(),book,mListView);
            mListView.setAdapter(adapter);
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
                JSONArray jsonArray=jsonObject.getJSONArray("json");
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

    public int getbookstype1(){
        int max=10;
        int min=0;
        Random random = new Random();
        int s = random.nextInt(max)%(max-min+1) + min;
        return s;
    }
}



