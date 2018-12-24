package cn.edu.uzz.activity.book.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMMessage;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import cn.edu.uzz.activity.book.R;
import cn.edu.uzz.activity.book.adapter.ChatAdapter;
import cn.edu.uzz.activity.book.util.GetMessageEvent;
import cn.edu.uzz.activity.book.util.MyCallBack;
import cn.edu.uzz.activity.book.util.ThreadUtils;

public class ChatActivity extends BaseActivity implements View.OnClickListener{

    private List<EMMessage> messageList = new ArrayList<>();
    private Toolbar tbToolbar;
    private TextView tv_title;
    private Button btn_send;
    private EditText et_msg;
    private RecyclerView rv_chat;
    private String username;
    private ChatAdapter adapter;

    // 发送消息按钮
    private Button buttonSend;
    // +号按钮
    private Button buttonMore;
    // 输入聊天内容的EditText
    private EditText editTextContent;
//    private Button buttonSetModeVoice;
    private LinearLayout moreLayout;
    private RelativeLayout edittextLayout;
//    private LinearLayout linearLayoutPressToSpeak;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        tbToolbar = (Toolbar) findViewById(R.id.tb_toolbar);
        tv_title = (TextView) findViewById(R.id.tv_title);
        btn_send = (Button) findViewById(R.id.btn_send);
        et_msg = (EditText) findViewById(R.id.et_sendmessage);
        rv_chat = (RecyclerView) findViewById(R.id.rv_chat);

        buttonSend = (Button) findViewById(R.id.btn_send);                       //发送按钮
        buttonMore = (Button) findViewById(R.id.btn_more);                       //+号按钮
        moreLayout = (LinearLayout) findViewById(R.id.ll_more_container);
        edittextLayout = (RelativeLayout) findViewById(R.id.edittext_layout);
//        buttonSetModeVoice = (Button) findViewById(R.id.btn_set_mode_voice);
//        linearLayoutPressToSpeak = (LinearLayout) findViewById(R.id.ll_press_to_speak);
        editTextContent = (EditText) findViewById(R.id.et_sendmessage);  //输入聊天内容的文本域
        editTextContent.requestFocus();
        editTextContent.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                moreLayout.setVisibility(View.GONE);
            }
        });
        editTextContent.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // 当内容不是空时，显示发送按钮，隐藏更多按钮
                if (!TextUtils.isEmpty(s)) {
                    buttonMore.setVisibility(View.GONE);
                    buttonSend.setVisibility(View.VISIBLE);
                } else {
                    buttonMore.setVisibility(View.VISIBLE);
                    buttonSend.setVisibility(View.GONE);
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });



        btn_send.setOnClickListener(this);//给按钮设置点击事件
        rv_chat.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ChatAdapter(null);
        rv_chat.setAdapter(adapter);

//        Intent intent = getIntent();
        //从上一个页面传递过来的 好友的用户名
//        username = intent.getStringExtra("username");
        username="abcde";
        tv_title.setText("图书客服");

        initToolbar();
        getAllMessage(username);
    }
    private void initToolbar() {
        tbToolbar.setTitle("");
        setSupportActionBar(tbToolbar);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ActionBar actionBar = getSupportActionBar();
        if (null != actionBar) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        tbToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        }

    private void getAllMessage(String username) {
        //获取跟当前联系人的会话对象
        EMConversation conversation = EMClient.getInstance().chatManager().getConversation(username);
        if(conversation != null){
            //获取当前会话中所有消息的数量
            int allMsgCount = conversation.getAllMsgCount();
            //获取最近一条消息
            EMMessage lastMessage = conversation.getLastMessage();
            //加载所有的消息到数据库  环信所有的消息都是保存到本地数据库的   参数1 最后一条消息的消息id  从这条消息基础之上 继续加载多少条消息
            List<EMMessage> emMessages = conversation.loadMoreMsgFromDB(lastMessage.getMsgId(), allMsgCount);
            messageList.clear();
            messageList.addAll(emMessages);
            //加入最后一条消息 loadMoreMsgFromDB 不包含 第一个参数id 对应的这一条消息
            messageList.add(lastMessage);
            //更新界面
            adapter.setEmMessageList(messageList);
            adapter.notifyDataSetChanged();
            rv_chat.smoothScrollToPosition(messageList.size()-1);
            //清除未读消息的数量
            EMClient.getInstance().chatManager().getConversation(username).markAllMessagesAsRead();
        }
    }



    @Override
    public void onClick(View v) {
        //从edittext中获取用户输入
        String content = et_msg.getText().toString().trim();
        //判断用户输入是否为空 如果不为空就 发送消息
        if(!TextUtils.isEmpty(content)){
            sendMsg(content,username);
            et_msg.setText("");
        }
    }

    public void sendMsg(String content, String username) {
        //创建一条文本消息  第一个参数 消息的内容 第二个参数 当前消息发送的对象
        final EMMessage message = EMMessage.createTxtSendMessage(content, username);
        //给当前消息设置状态监听  只要状态发生了变化 就更新一下这条消息的状态
        message.setMessageStatusCallback(new MyCallBack() {
            @Override
            public void success() {
                adapter.notifyDataSetChanged();
            }

            @Override
            public void error(int i, String s) {
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onProgress(int i, String s) {
                adapter.notifyDataSetChanged();
            }
        });

        ThreadUtils.runOnSubThread(new Runnable() {
            @Override
            public void run() {
                //在子线程中 发送消息
                EMClient.getInstance().chatManager().sendMessage(message);
            }
        });
        //只要调用了发送的方法 就要在界面上把这条消息展示出来
        //先把消息放到集合中
        messageList.add(message);
        //更新界面
        adapter.setEmMessageList(messageList);
        adapter.notifyDataSetChanged();
        rv_chat.smoothScrollToPosition(messageList.size()-1);
    }



    /**
     * 设置当前输入模式为文字输入模式
     *
     * @param view
     */
    public void setModeKeyboard(View view) {
        edittextLayout.setVisibility(View.VISIBLE);
        moreLayout.setVisibility(View.GONE);
//        view.setVisibility(View.GONE);
//        buttonSetModeVoice.setVisibility(View.VISIBLE);
        editTextContent.requestFocus();
//        linearLayoutPressToSpeak.setVisibility(View.GONE);
        // 如果输入框中有文字，显示发送按钮，隐藏+号按钮
        // 如果里面没有文字，显示+号按钮，隐藏发送按钮
        if (TextUtils.isEmpty(editTextContent.getText())) {
            buttonMore.setVisibility(View.VISIBLE);
            buttonSend.setVisibility(View.GONE);
        } else {
            buttonMore.setVisibility(View.GONE);
            buttonSend.setVisibility(View.VISIBLE);
        }
    }
    /**
     * 隐藏软键盘
     */
    private void hideKeyboard() {
        if (getWindow().getAttributes().softInputMode != WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN) {
            if (getCurrentFocus() != null) {
                InputMethodManager manager;
                manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                manager.hideSoftInputFromWindow(getCurrentFocus()
                        .getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }
    }
    /**
     * 显示或隐藏更多
     *
     * @param view
     */
    public void more(View view) {
        if (moreLayout.getVisibility() == View.GONE) {
            hideKeyboard();
            moreLayout.setVisibility(View.VISIBLE);
        } else {
            moreLayout.setVisibility(View.GONE);
        }
    }



    @Override
    public void onStart() {
        super.onStart();
        //当页面加载之后会执行这个onStart方法 执行了onStart之后 页面就可以被看见了
        EventBus.getDefault().register(this); //当页面可见的时候 注册eventbus的订阅者
    }
    @Override
    public void onStop() {
        super.onStop();
        //当页面要跑到后台去的时候 会执行这个方法  走了onStop 之后页面就不可见了
        EventBus.getDefault().unregister(this); //当页面不可见的时候 要注销EventBus的订阅者
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onNewMsgReceived(GetMessageEvent event){
        getAllMessage(username);
    }
}

