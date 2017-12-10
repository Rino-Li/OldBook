package cn.edu.uzz.activity.book.cn.edu.uzz.activity.book.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
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
import com.mob.MobSDK;

import org.json.JSONObject;

import cn.edu.uzz.activity.book.MainActivity;
import cn.edu.uzz.activity.book.R;
import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;


/**
 * Created by 10616 on 2017/11/4.
 */

public class ForgetPwdActivity extends AppCompatActivity implements View.OnClickListener{
    String APPKEY = "2216245c23ed4";
    String APPSECRETE = "8407c2da25d4cdfcdbad0043b99907c4";

    // 手机号输入框
    private EditText inputaccount;

    // 验证码输入框
    private EditText inputCodeEt;

    // 获取验证码按钮
    private Button requestCodeBtn;

    // 注册按钮
    private Button commitBtn;

    private TextView phonenum;

    //
    int i = 60;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgetpwd);

        init();


        android.support.v7.app.ActionBar actionBar=getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    /**
     * 初始化控件
     */
    private void init() {
        inputaccount = (EditText) findViewById(R.id.login_input_account_et);
        inputCodeEt = (EditText) findViewById(R.id.login_input_code_et);
        requestCodeBtn = (Button) findViewById(R.id.login_request_code_btn);
        commitBtn = (Button) findViewById(R.id.login_commit_btn);
        phonenum= (TextView) findViewById(R.id.phonenum);
        requestCodeBtn.setOnClickListener(this);
        commitBtn.setOnClickListener(this);

        // 启动短信验证sdk

        MobSDK.init(this, APPKEY, APPSECRETE);
        EventHandler eventHandler = new EventHandler() {
            @Override
            public void afterEvent(int event, int result, Object data) {
                Message msg = new Message();
                msg.arg1 = event;
                msg.arg2 = result;
                msg.obj = data;
                handler.sendMessage(msg);
            }
        };
        //注册回调监听接口
        SMSSDK.registerEventHandler(eventHandler);
    }

    @Override
    public void onClick(View v) {

        /*String phoneNums = phonenum.getText().toString();
        Toast.makeText(ForgetPwdActivity.this,phoneNums,Toast.LENGTH_SHORT).show();*/
        switch (v.getId()) {
            case R.id.login_request_code_btn:
                String stu_id=inputaccount.getText().toString();
                findTel(stu_id);
                break;

            case R.id.login_commit_btn:
                //将收到的验证码和手机号提交再次核对
                SMSSDK.submitVerificationCode("86", phonenum.getText().toString(), inputCodeEt
                        .getText().toString());
                break;
        }
    }

    /**
     *
     */
    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            if (msg.what == -9) {
                requestCodeBtn.setText("重新发送(" + i + ")");
            } else if (msg.what == -8) {
                requestCodeBtn.setText("获取验证码");
                requestCodeBtn.setClickable(true);
                i = 60;
            } else {
                int event = msg.arg1;
                int result = msg.arg2;
                Object data = msg.obj;
                Log.e("event", "event=" + event);
                if (result == SMSSDK.RESULT_COMPLETE) {
                    // 短信注册成功后，返回MainActivity,然后提示
                    if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {// 提交验证码成功
                        Toast.makeText(getApplicationContext(), "提交验证码成功",
                                Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(ForgetPwdActivity.this,
                                MainActivity.class);
                        startActivity(intent);

                        //findstuInfor(inputaccount.getText().toString());
                        //intent.putExtra("stu_id",inputaccount.getText().toString());
                        finish();
                    } else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE) {
                        Toast.makeText(getApplicationContext(), "正在获取验证码",
                                Toast.LENGTH_SHORT).show();
                    } else {
                        ((Throwable) data).printStackTrace();
                    }
                }
            }
        }
    };


    /**
     * 判断手机号码是否合理
     *
     * @param phoneNums
     */
    private boolean judgePhoneNums(String phoneNums) {
        if (isMatchLength(phoneNums, 11)
                && isMobileNO(phoneNums)) {
            return true;
        }
        Toast.makeText(this, "手机号码输入有误！", Toast.LENGTH_SHORT).show();
        return false;
    }

    /**
     * 判断一个字符串的位数
     *
     * @param str
     * @param length
     * @return
     */
    public static boolean isMatchLength(String str, int length) {
        if (str.isEmpty()) {
            return false;
        } else {
            return str.length() == length ? true : false;
        }
    }

    /**
     * 验证手机格式
     */
    public static boolean isMobileNO(String mobileNums) {
    /*
     * 移动：134、135、136、137、138、139、150、151、157(TD)、158、159、187、188
     * 联通：130、131、132、152、155、156、185、186 电信：133、153、180、189、（1349卫通）
     * 总结起来就是第一位必定为1，第二位必定为3或5或8，其他位置的可以为0-9
     */
        String telRegex = "[1][43578]\\d{9}";// "[1]"代表第1位为数字1，"[34578]"代表第二位可以为3、4，5、7，8中的一个，"\\d{9}"代表后面是可以是0～9的数字，有9位。
        if (TextUtils.isEmpty(mobileNums))
            return false;
        else
            return mobileNums.matches(telRegex);
    }



    @Override
    protected void onDestroy() {
        SMSSDK.unregisterAllEventHandler();
        super.onDestroy();
    }

    public void findTel(String stu_account){
        phonenum.setText("17863203270");
        /*RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        //2创建请求
        JsonObjectRequest request=new JsonObjectRequest(
                "http://123.206.230.120/SchoolLife/findtelServ?stu_account="+stu_account , null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                try {
                    int code = jsonObject.getInt("resultCode");
                    if(code==0){
                        String phone=jsonObject.getString("phone");
                        phonenum.setText(phone);*/
                        String phone="17863203270";
                        if (!judgePhoneNums(phone)) {
                            return;
                        } // 2. 通过sdk发送短信验证
                        SMSSDK.getVerificationCode("86", phone);
                        // 3. 把按钮变成不可点击，并且显示倒计时（正在获取）
                        requestCodeBtn.setClickable(false);
                        requestCodeBtn.setText("重新发送(" + i + ")");
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                for (; i > 0; i--) {
                                    handler.sendEmptyMessage(-9);
                                    if (i <= 0) {
                                        break;
                                    }
                                    try {
                                        Thread.sleep(1000);
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                }
                                handler.sendEmptyMessage(-8);
                            }
                        }).start();

                    /*}
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
            }
        });
        //3请求加入队列
        queue.add(request);*/
    }

    public void findstuInfor(final String stu_account){
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        JsonObjectRequest request=new JsonObjectRequest(
                "http://123.206.230.120/SchoolLife/findstuinforServ?stu_account="+stu_account , null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                try {
                    int code = jsonObject.getInt("resultCode");
                    if (code == 0) {
                        String id = jsonObject.getString("id");
                        String name = jsonObject.getString("name");
                        String sex = jsonObject.getString("sex");
                        String tel = jsonObject.getString("tel");
                        String depart = jsonObject.getString("depart");
                        String major = jsonObject.getString("major");
                        SharedPreferences pre=getSharedPreferences("stu_account",MODE_PRIVATE);
                        SharedPreferences.Editor editor=pre.edit();
                        editor.remove("stu_account");
                        editor.remove("name");
                        editor.remove("sex");
                        editor.remove("tel");
                        editor.remove("depart");
                        editor.remove("major");
                        editor.putString("stu_account",stu_account);
                        editor.putString("name",name);
                        editor.putString("sex",sex);
                        editor.putString("tel",tel);
                        editor.putString("depart",depart);
                        editor.putString("major",major);
                        editor.commit();
                    }else{
                        Toast.makeText(ForgetPwdActivity.this,"获取失败",Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(ForgetPwdActivity.this,"获取失败",Toast.LENGTH_SHORT).show();
            }
        });
        queue.add(request);
    }
}
