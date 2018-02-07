package cn.edu.uzz.activity.book.ui;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import cn.edu.uzz.activity.book.R;
import cn.edu.uzz.activity.book.entity.Books;
import cn.edu.uzz.activity.book.util.BitmapCache;

/**
 * Created by 10616 on 2017/11/25.
 */

public class BookItemActivity extends Activity implements View.OnClickListener {
    private NetworkImageView book_pic;
    private TextView book_name;
    private TextView book_writer;
    private TextView book_publish;
    private TextView book_version;
    private TextView book_isbn;
    private TextView book_price;
    private TextView book_time;
    private ImageView item_return;
    private TextView like_word;
    String image_name;
    private LinearLayout book_like;
    private ImageButton book_like_image;
    private TextView book_like_word;
    private Button book_sub;
    private Button book_rent;
    int type;
    int id;
    String account;
    String bookname;
    Books book;
    private static String  substatus;
    private static String rentstatus;
    StringBuffer time;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.item);
        initView();

    }

    private void initView() {
        book_pic = (NetworkImageView) findViewById(R.id.item_image);
        book_name = (TextView) findViewById(R.id.item_name);
        book_writer = (TextView) findViewById(R.id.item_writer);
        book_publish = (TextView) findViewById(R.id.item_publish);
        book_version = (TextView) findViewById(R.id.item_version);
        book_isbn = (TextView) findViewById(R.id.item_isbn);
        book_price = (TextView) findViewById(R.id.item_price);
        book_time = (TextView) findViewById(R.id.item_time);
        item_return = (ImageView) findViewById(R.id.item_return);
        book_like = (LinearLayout) findViewById(R.id.item_like);
        book_like_word = (TextView) findViewById(R.id.item_like_word);
        book_like_image = (ImageButton) findViewById(R.id.item_like_image);
        book_sub = (Button) findViewById(R.id.item_subscribe);
        book_rent = (Button) findViewById(R.id.item_rent);
        like_word= (TextView) findViewById(R.id.item_like_word);

        SharedPreferences pre=getSharedPreferences("user",MODE_PRIVATE);
        account=pre.getString("account","");

        book = (Books) getIntent().getSerializableExtra("book");
        book_name.setText("书名：" + book.getName());
        bookname=book.getName();
        book_writer.setText("作者：" + book.getWriter());
        book_publish.setText("出版社：" + book.getPublish());
        book_version.setText(book.getVersion());
        book_isbn.setText(book.getISBN());
        book_price.setText("价格：" + book.getPrice());
        book_time.setText("日期：" + book.getTime());
        item_return.setOnClickListener(this);
        book_like.setOnClickListener(this);
        book_rent.setOnClickListener(this);
        book_sub.setOnClickListener(this);
        image_name = book.getPicture();
        id = book.getId();
        type = book.getType();
        //检测该用户是否收藏此书，若已收藏，则显示已收藏
        //检测书籍状态，显示是否被借阅或被预定
        check();
        getImage(image_name);//获取照片
    }


    private void check() {
        //1创建请求队列
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        //2创建请求
        JsonObjectRequest request = new JsonObjectRequest(
                "http://123.206.230.120/Book/checklikeServ?account=" + account +"&type=" + type+"&id="+id, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    int resultCode = response.getInt("resultCode");
                    substatus=response.getString("subscribe");
                    rentstatus=response.getString("rentstatus");
                    if (resultCode == 1) {
                        book_like_image.setImageResource(R.drawable.book_like_selected);
                        book_like_word.setText("已收藏");
                        book_sub.setText(substatus);
                        book_rent.setText(rentstatus);
                    }else if (resultCode==2){
                        book_like_image.setImageResource(R.drawable.book_like_normal);
                        book_like_word.setText("收藏");
                        book_sub.setText(substatus);
                        book_rent.setText(rentstatus);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }


        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

                Toast.makeText(BookItemActivity.this,"网络异常，请稍后再试",Toast.LENGTH_SHORT).show();
            }
        });
        //3请求加入队列
		request.setTag("book");
        queue.add(request);

    }

    private void getImage(String image_name) {
		RequestQueue mQueue = Volley.newRequestQueue(getApplicationContext());
		ImageLoader imageLoader = new ImageLoader(mQueue, new BitmapCache());
		book_pic.setDefaultImageResId(R.drawable.ic_launcher);
		book_pic.setErrorImageResId(R.drawable.ic_launcher);
		book_pic.setImageUrl(image_name,
				imageLoader);
        /*ImageRequest request = new ImageRequest(image_name, new Response.Listener<Bitmap>() {
            @Override
            public void onResponse(Bitmap bitmap) {
                book_pic.setImageBitmap(bitmap);
            }
        }, 500, 200, Bitmap.Config.RGB_565, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                book_pic.setBackgroundResource(R.drawable.ic_launcher);
            }
        });
        mQueue.add(request);*/
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.item_return:
                finish();
				overridePendingTransition(R.anim.anim_in,R.anim.anim_out);
				break;
            case R.id.item_like:
                if(like_word.getText().toString().equals("收藏")){getLikeBook();}
                else {cancelLikeBook();}
                break;
            case R.id.item_rent:
                if (book_rent.getText().toString().equals("已借阅")){
                    Toast.makeText(BookItemActivity.this,"对不起，此书已被借阅",Toast.LENGTH_SHORT).show();
                }else {
                    Rent();
                }
                break;
            case R.id.item_subscribe:
                if(checksub()==1){
                    Toast.makeText(BookItemActivity.this,"预定成功，请在24小时内借阅此书",Toast.LENGTH_SHORT).show();
                }else if(checksub()==2){
                    Toast.makeText(BookItemActivity.this,"抱歉，此书已被预订",Toast.LENGTH_SHORT).show();
                }else if(checksub()==3){
                    Toast.makeText(BookItemActivity.this,"抱歉，此书正在被借阅",Toast.LENGTH_SHORT).show();
                }else if (checksub()==4){
					Toast.makeText(BookItemActivity.this,"请先登录",Toast.LENGTH_SHORT).show();
				}
                break;
            default:
                break;
        }
    }

    private void Rent() {
        if (book_sub.getText().toString().equals("预定")){
            showDatePickDlg();
        }else {
            checkRent();
        }
    }

    protected void showDatePickDlg() {
		if (account==""){
			Toast.makeText(BookItemActivity.this,"请先登录",Toast.LENGTH_SHORT).show();
			return;
		}
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(BookItemActivity.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                time=new StringBuffer().append(year).append("-").append(monthOfYear+1).append("-").append(dayOfMonth);
                SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
                try {
                    Date dateend=sdf.parse(String.valueOf(time));
                    String now=sdf.format(new java.util.Date());
                    Date datenow=sdf.parse(now);
                    if (dateend.getTime()<datenow.getTime()){
                        Toast.makeText(BookItemActivity.this,"请选择正确的日期",Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(BookItemActivity.this,"您选择的时间为："+time,Toast.LENGTH_SHORT).show();//测试阶段使用 ，成功后注释掉
                        rentBook();
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();

    }

    private void checkRent() {
        //1创建请求队列
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        //2创建请求
        JsonObjectRequest request = new JsonObjectRequest(
                "http://123.206.230.120/Book/checksubServ?account=" + account +  "&bookid=" +id+ "&booktype=" + type, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    int resultCode = response.getInt("resultCode");
                    if (resultCode == 1) {
                        showDatePickDlg();
                    }else if (resultCode==2){
                        Toast.makeText(BookItemActivity.this,"此书已被其他用户预定，请等候",Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }


        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

                Toast.makeText(BookItemActivity.this,"网络异常，请稍后再试",Toast.LENGTH_SHORT).show();
            }
        });
        //3请求加入队列
		request.setTag("book");
        queue.add(request);
    }

    private void rentBook() {
        String  timeend=time.toString();
		Log.e("BBBB","enddate is "+timeend);
		//1创建请求队列
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        //2创建请求
        JsonObjectRequest request = new JsonObjectRequest(
                "http://123.206.230.120/Book/rentcarServ?account=" + account +  "&bookid=" +id+ "&booktype=" + type+"&bookname="+bookname+"&timeend="+timeend+"&picture="+image_name, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    int resultCode = response.getInt("resultCode");
                    if (resultCode == 1) {
                        Toast.makeText(BookItemActivity.this,"已添加到借阅车，请在30分钟内确认借阅",Toast.LENGTH_SHORT).show();
                    }else if (resultCode==2){
                        Toast.makeText(BookItemActivity.this,"添加到借阅车失败，请稍后再试！",Toast.LENGTH_SHORT).show();
                    }else if (resultCode==3){
                        Toast.makeText(BookItemActivity.this,"此书已被添加，勿重复添加",Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }


        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

                Toast.makeText(BookItemActivity.this,"网络异常，请稍后再试",Toast.LENGTH_SHORT).show();
            }
        });
        //3请求加入队列
		request.setTag("book");
        queue.add(request);
    }

    private int checksub() {
        if(book_sub.getText().toString().equals("已预订")){
            return 2;
        }else if (book_rent.getText().toString().equals("已借阅")){
            return 3;
        }else {
			if (account==""){
				return 4;
			}else{
				getSubscribe();//此书未被预定，进行预定操作
				return 1;
			}

        }
    }

    private void getSubscribe() {

        //1创建请求队列
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        //2创建请求
        JsonObjectRequest request = new JsonObjectRequest(
                "http://123.206.230.120/Book/subscribeServ?account=" + account +  "&bookid=" +id+ "&booktype=" + type+"&bookname="+bookname+"&picture="+image_name, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    int resultCode = response.getInt("resultCode");
                    if (resultCode == 1||resultCode==2) {
                        book_sub.setText("已预订");
                    }else if (resultCode==3){
                        Toast.makeText(BookItemActivity.this,"预定失败，请稍后再试！",Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }


        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

                Toast.makeText(BookItemActivity.this,"网络异常，请稍后再试",Toast.LENGTH_SHORT).show();
            }
        });
        //3请求加入队列
		request.setTag("book");
        queue.add(request);
    }

    private void cancelLikeBook() {
        //1创建请求队列
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        //2创建请求
        JsonObjectRequest request = new JsonObjectRequest(
                "http://123.206.230.120/Book/cancellikeServ?account=" + account +  "&bookid=" +id+ "&booktype=" + type, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    int resultCode = response.getInt("resultCode");
                    if (resultCode == 1||resultCode==3) {
                        Toast.makeText(BookItemActivity.this,"取消收藏成功",Toast.LENGTH_SHORT).show();
                        book_like_image.setImageResource(R.drawable.book_like_normal);
                        book_like_word.setText("收藏");
                    }else if (resultCode==2){
                        Toast.makeText(BookItemActivity.this,"取消收藏失败，请稍后再试！",Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }


        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

                Toast.makeText(BookItemActivity.this,"网络异常，请稍后再试",Toast.LENGTH_SHORT).show();
            }
        });
        //3请求加入队列
		request.setTag("book");
        queue.add(request);

    }

    public boolean getLikeBook() {
		if (account==""){
			Toast.makeText(BookItemActivity.this,"请先登录",Toast.LENGTH_SHORT).show();
			return false;
		}
        //1创建请求队列
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        //2创建请求
        JsonObjectRequest request = new JsonObjectRequest(
                "http://123.206.230.120/Book/likeServ?account=" + account + "&bookname=" + bookname + "&bookid=" +id+ "&booktype=" + type+"&picture="+image_name, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    int resultCode = response.getInt("resultCode");
                    if (resultCode == 1) {
                        Toast.makeText(BookItemActivity.this,"收藏成功",Toast.LENGTH_SHORT).show();
                        book_like_image.setImageResource(R.drawable.book_like_selected);
                        book_like_word.setText("已收藏");
                    }else if (resultCode==2){
                        Toast.makeText(BookItemActivity.this,"收藏失败，请稍后再试！",Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }


        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

                Toast.makeText(BookItemActivity.this,"网络异常，请稍后再试",Toast.LENGTH_SHORT).show();
            }
        });
        //3请求加入队列
		request.setTag("book");
        queue.add(request);
        return true;
    }


}
