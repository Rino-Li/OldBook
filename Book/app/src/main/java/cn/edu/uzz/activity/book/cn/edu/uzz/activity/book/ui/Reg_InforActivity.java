package cn.edu.uzz.activity.book.cn.edu.uzz.activity.book.ui;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

import cn.edu.uzz.activity.book.DemoApplication;
import cn.edu.uzz.activity.book.R;

/**
 * Created by 10616 on 2017/11/4.
 */

public class Reg_InforActivity extends Activity{
    private EditText user_name;
    private String user_tel;
    private RadioGroup sex_user;
    private EditText user_address;
    private EditText user_age;
    String stu_sex="男";
    private Button confirmBtn;
    String user_account;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reginfor);
        user_name= (EditText) findViewById(R.id.input_name);
        user_address= (EditText) findViewById(R.id.input_address);
        user_age= (EditText) findViewById(R.id.input_age);
        sex_user= (RadioGroup) findViewById(R.id.input_sex);
        confirmBtn= (Button) findViewById(R.id.confirm_infor);
        Intent intent=getIntent();
        user_account=intent.getStringExtra("account");
        user_tel=intent.getStringExtra("phone");
        sex_user.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                if(checkedId==R.id.radio_male){
                    stu_sex="男";
                }else if (checkedId==R.id.radio_female){
                    stu_sex="女";
                }
            }
        });
        confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                commitInfor();
            }
        });
    }

    private void commitInfor() {
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        StringRequest stringrequest=new StringRequest(Request.Method.POST, "http://123.206.230.120/Book/reginforServ", new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                Toast.makeText(Reg_InforActivity.this,"提交成功",Toast.LENGTH_SHORT).show();
                Infor();
                startActivity(new Intent(Reg_InforActivity.this,MainActivity.class));
                finish();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(Reg_InforActivity.this,"提交失败",Toast.LENGTH_SHORT).show();
            }
        }){
            protected Map<String,String> getParams(){
                Map<String,String> map=new HashMap<String, String>();
                map.put("id",user_account);
                map.put("name",user_name.getText().toString());
                map.put("sex",stu_sex);
                map.put("tel",user_tel);
                map.put("address",user_address.getText().toString());
                map.put("age",user_age.getText().toString());
                return map;
            }
        };
        stringrequest.setTag("reg_infor");
        requestQueue.add(stringrequest);
    }

    private void Infor() {
        SharedPreferences pre=getSharedPreferences("user",MODE_PRIVATE);
        SharedPreferences.Editor editor=pre.edit();
        editor.putString("username",user_account);
        editor.putString("account",user_tel);
        editor.putString("sex",stu_sex);
        editor.putString("truthname",user_name.getText().toString());
        editor.putString("tel",user_tel);
        editor.putString("address",user_address.getText().toString());
        editor.putString("age",user_age.getText().toString());
        editor.commit();
    }

	@Override
	protected void onStop() {
		super.onStop();
		DemoApplication.getHttpQueues().cancelAll("reg_infor");
	}
}
