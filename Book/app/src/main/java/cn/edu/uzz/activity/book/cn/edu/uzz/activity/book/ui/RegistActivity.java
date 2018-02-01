package cn.edu.uzz.activity.book.cn.edu.uzz.activity.book.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

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
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regist);
        Intent intent = getIntent();
        tel= intent.getStringExtra("phone");
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
            RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
            JsonObjectRequest request=new JsonObjectRequest("http://123.206.230.120/Book/registServ?username="+user_account+"&password="+user_pwd+"&account="+tel,null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject jsonObject) {
                    try {
                        int code = jsonObject.getInt("resultCode");
                        if(code==1){//注册成功
                            Toast.makeText(RegistActivity.this,"注册成功",Toast.LENGTH_SHORT).show();
                            Intent intent=new Intent();
                            intent.putExtra("account",user_account);
                            intent.putExtra("phone",tel);
                            intent.setClass(RegistActivity.this,Reg_InforActivity.class);
                            startActivity(intent);

                        }else if(code==2){//用户已注册
                            Toast.makeText(RegistActivity.this, "已注册，请登录！", Toast.LENGTH_SHORT).show();
                            Log.e("tag","用户已存在了");
                        }


                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {

                }
            });
            request.setTag("regist");
            queue.add(request);
        }
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

	@Override
	protected void onStop() {
		super.onStop();
		DemoApplication.getHttpQueues().cancelAll("regist");
	}
}
