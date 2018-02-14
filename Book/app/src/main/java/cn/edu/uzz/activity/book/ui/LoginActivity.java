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
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import cn.edu.uzz.activity.book.R;
import cn.jpush.android.api.JPushInterface;

/**
 * Created by 10616 on 2017/11/2.
 */

public class LoginActivity extends Activity {
	private EditText user_account;
	private EditText user_pwd;
	private Button loginBtn;
	private TextView forgetBtn;
	private TextView registBtn;
	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		init();
	}

	private void init() {
		user_account= (EditText) findViewById(R.id.user_account);
		user_pwd= (EditText) findViewById(R.id.user_pwd);
		loginBtn= (Button) findViewById(R.id.login);
		forgetBtn= (TextView) findViewById(R.id.forget);
		registBtn= (TextView) findViewById(R.id.regist);
		loginBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				final String account=user_account.getText().toString();
				String pwd=user_pwd.getText().toString();
				//1创建请求队列
				RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
				//2创建请求
				JsonObjectRequest request=new JsonObjectRequest(
						"http://123.206.230.120/Book/loginServ?username="+account+"&password="+pwd, null, new Response.Listener<JSONObject>() {
					@Override
					public void onResponse(JSONObject response) {
						try {
							int resultCode = response.getInt("resultCode");
							if (resultCode == 3) {
								getInfor();
								Toast.makeText(LoginActivity.this,"登录成功",Toast.LENGTH_SHORT).show();
								startActivity(new Intent(LoginActivity.this, MainActivity.class));
								JPushInterface.setAlias(LoginActivity.this,account,null);
								/*这里不能忘记修改*/
								Log.e("BBBB","修改成功");
								finish();
								overridePendingTransition(R.anim.anim_in,R.anim.anim_out);
							} else if (resultCode == 4) {
								Toast.makeText(LoginActivity.this, "密码错误，请重新输入！", Toast.LENGTH_SHORT).show();
							} else if (resultCode == 2) {
								Toast.makeText(LoginActivity.this, "用户未注册，请先注册！", Toast.LENGTH_SHORT).show();
							}

						} catch (JSONException e) {
							e.printStackTrace();
						}


					}


				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError volleyError) {

						Toast.makeText(LoginActivity.this, "登录失败，请检查网络链接！", Toast.LENGTH_SHORT).show();
					}
				});
				//3请求加入队列
				request.setTag("login");
				queue.add(request);
			}
		});
		forgetBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				startActivity(new Intent(LoginActivity.this,ForgetPwdActivity.class));
				overridePendingTransition(R.anim.anim_in,R.anim.anim_out);
			}
		});
		registBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				startActivity(new Intent(LoginActivity.this,Reg_TelActivity.class));
				overridePendingTransition(R.anim.anim_in,R.anim.anim_out);
			}
		});
	}

	private void getInfor() {
		final String account=user_account.getText().toString();
		RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
		JsonObjectRequest request=new JsonObjectRequest(
				"http://123.206.230.120/Book/findinforServ?account="+account , null, new Response.Listener<JSONObject>() {
			@Override
			public void onResponse(JSONObject jsonObject) {
				try {
					int code = jsonObject.getInt("resultCode");
					if (code == 0) {
						String username = jsonObject.getString("username");
						String sex = jsonObject.getString("usersex");
						String tel = jsonObject.getString("phone");
						String address = jsonObject.getString("addr");
						String age = jsonObject.getString("userage");
						String name=jsonObject.getString("name");
						SharedPreferences pre=getSharedPreferences("user",MODE_PRIVATE);
						SharedPreferences.Editor editor=pre.edit();
						editor.putString("username",username);
						editor.putString("account",account);
						editor.putString("sex",sex);
						editor.putString("truthname",name);
						editor.putString("tel",tel);
						editor.putString("address",address);
						editor.putString("age",age);
						editor.commit();
					}else{
						Toast.makeText(LoginActivity.this,"个人信息获取失败",Toast.LENGTH_SHORT).show();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}, new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError volleyError) {
				Toast.makeText(LoginActivity.this,"个人信息获取失败",Toast.LENGTH_SHORT).show();
			}
		});
		request.setTag("login");
		queue.add(request);
	}


}
