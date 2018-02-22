package cn.edu.uzz.activity.book.ui;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import cn.edu.uzz.activity.book.R;

/**
 * Created by 10616 on 2017/11/2.
 */

public class RegistActivity extends Activity{
    private EditText reg_account;
    private  EditText reg_pwd;
    private EditText reg_confirm_pwd;
    private Button registBtn;
    private  String tel;
    private String account;
    private String sex;
    private String truthname;
    private String address;
    private String age;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regist);
        Intent intent = getIntent();
		account=intent.getStringExtra("account");
        tel= intent.getStringExtra("tel");
		sex=intent.getStringExtra("sex");
		truthname=intent.getStringExtra("truthname");
		address=intent.getStringExtra("address");
		age=intent.getStringExtra("age");
        //Toast.makeText(RegistActivity.this,tel,Toast.LENGTH_SHORT).show();
        init();
    }

    private void init() {
        reg_account= (EditText) findViewById(R.id.input_account);
        reg_pwd= (EditText) findViewById(R.id.input_pwd);
        reg_confirm_pwd= (EditText) findViewById(R.id.repeat_pwd);
        registBtn= (Button) findViewById(R.id.regist);
        registBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(checkIsLegal()==true) {
                    regist();
                }
            }
        });
    }

    private void regist() {
        final String user_account=reg_account.getText().toString();
        String user_pwd=reg_pwd.getText().toString();
        String con_pwd=reg_confirm_pwd.getText().toString();
        if(!user_pwd.equals(con_pwd)){
            Toast.makeText(this,"两次密码输入不相符",Toast.LENGTH_SHORT).show();
        }else {
			commitInfor();
        }
    }

	private void commitInfor() {
		RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
		StringRequest stringrequest=new StringRequest(Request.Method.POST, "http://123.206.230.120/Book/registServ", new Response.Listener<String>() {
			@Override
			public void onResponse(String s) {
				try {
					JSONObject jsonObject=new JSONObject(s);
					int resultCode=jsonObject.getInt("resultCode");
					if (resultCode==1){
						Toast.makeText(RegistActivity.this,"注册成功",Toast.LENGTH_SHORT).show();
						Infor();
						Intent intent=new Intent();
						intent.setClass(RegistActivity.this,MainActivity.class);
						startActivity(intent);
						finish();
						overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
						overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
					}else if(resultCode==2){
						Toast.makeText(RegistActivity.this, "已注册，请登录！", Toast.LENGTH_SHORT).show();
						startActivity(new Intent(RegistActivity.this,LoginActivity.class));
						finish();
						overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
						overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		}, new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError volleyError) {
				Toast.makeText(RegistActivity.this,"系统错误",Toast.LENGTH_SHORT).show();
				Log.e("VolleyError---", volleyError.getMessage(), volleyError);
				byte[] htmlBodyBytes = volleyError.networkResponse.data;  //回应的报文的包体内容
				Log.e("VolleyError body---->", new String(htmlBodyBytes), volleyError);
			}
		}){
			protected Map<String,String> getParams(){
				Map<String,String> map=new HashMap<String, String>();
				map.put("account",account);//账号
				map.put("username",reg_account.getText().toString());//昵称
				map.put("sex",sex);//性别
				map.put("tel",tel);//电话号码
				map.put("address",address);//住址
				map.put("age",age);//年龄
				map.put("password",reg_pwd.getText().toString());//密码
				map.put("truthname",truthname);//真实姓名
				return map;
			}
		};
		stringrequest.setTag("reg_infor");
		requestQueue.add(stringrequest);
	}

	private boolean checkIsLegal() {
        String user_account=reg_account.getText().toString();
        String user_pwd=reg_pwd.getText().toString();
        String con_pwd=reg_confirm_pwd.getText().toString();
        if (user_account.equals("")) {
            Toast.makeText(this, "请填写昵称", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (user_pwd.equals("")) {
            Toast.makeText(this, "请填写密码", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (con_pwd.equals("")) {
            Toast.makeText(this, "请填写确认密码", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

	private void Infor() {
		SharedPreferences pre=getSharedPreferences("user",MODE_PRIVATE);
		SharedPreferences.Editor editor=pre.edit();
		editor.putString("username",reg_account.getText().toString());
		editor.putString("account",account);
		editor.putString("sex",sex);
		editor.putString("truthname",truthname);
		editor.putString("tel",tel);
		editor.putString("address",address);
		editor.putString("age",age);
		editor.commit();
	}

}
