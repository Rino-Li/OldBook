package cn.edu.uzz.activity.book.adapter;

import android.graphics.drawable.AnimationDrawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMTextMessageBody;
import com.hyphenate.util.DateUtils;

import java.util.Date;
import java.util.List;

import cn.edu.uzz.activity.book.R;

/**
 * Created by fullcircle on 2017/7/24.
 */

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.MyViewHolder> {
    public void setEmMessageList(List<EMMessage> emMessageList) {
        this.emMessageList = emMessageList;
    }

    private List<EMMessage> emMessageList;

    public ChatAdapter(List<EMMessage> emMessageList) {
        this.emMessageList = emMessageList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = null;
        //根据viewType 来决定要加载那个布局
        if(viewType == 0){
           itemView  = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_chat_item_send,parent,false);
        }else if (viewType == 1){
            itemView  = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_chat_item,parent,false);

        }
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        //获取消息的对象
        EMMessage message = emMessageList.get(position);
        //获取消息的内容 转换成EMTextMessageBody
        EMTextMessageBody body = (EMTextMessageBody) message.getBody();
        //提取消息的文字内容
        String message1 = body.getMessage();
        //通过textview展示文字内容
        holder.tv_message.setText(message1);
        //DateUtils.getTimestampString 获取时间戳对应的字符串
        holder.tv_time.setText(DateUtils.getTimestampString(new Date(message.getMsgTime())));
        if(position ==0){
            //如果是第1条消息 与当前的系统时间比较 如果消息发送/收到的时间和当前时间足够近就不需要展示
            //显示消息时间的textView
            if(DateUtils.isCloseEnough(message.getMsgTime(), System.currentTimeMillis())){
                holder.tv_time.setVisibility(View.GONE);
            }else{
                holder.tv_time.setVisibility(View.VISIBLE);
            }
        }else{
            //如果不是第一条 就需要跟当前消息的上一条消息比较
            if(DateUtils.isCloseEnough(message.getMsgTime(),emMessageList.get(position-1).getMsgTime())){
                holder.tv_time.setVisibility(View.GONE);
            }else{
                holder.tv_time.setVisibility(View.VISIBLE);
            }
        }

        //如果是自己发送的消息 需要展示发送消息的状态 是成功了还是正在发送还是发送失败了
        if(message.direct() == EMMessage.Direct.SEND){
            switch (message.status()){
                case SUCCESS:
                    //如果成功了 隐藏显示状态的图标
                    holder.iv_state.setVisibility(View.GONE);
                    break;
                case FAIL:
                    //如果失败了 显示发送状态图标为失败
                    holder.iv_state.setVisibility(View.VISIBLE);
                    holder.iv_state.setImageResource(R.mipmap.msg_error);
                    break;
                case INPROGRESS:
                    //如果是正在发送 展示旋转的帧动画
                    holder.iv_state.setVisibility(View.VISIBLE);
                    holder.iv_state.setImageResource(R.drawable.send_animation);
                    AnimationDrawable drawable = (AnimationDrawable) holder.iv_state.getDrawable();
                    drawable.start();//让动画开始
                    break;
            }
        }

    }

    @Override
    public int getItemCount() {
        return emMessageList == null ? 0:emMessageList.size();
    }

    //如果recyclerView 需要显示几种不同布局的条目 那么就要重写这个方法
    @Override
    public int getItemViewType(int position) {
        //获取当前条目的类型
        EMMessage message = emMessageList.get(position);
        //获取消息的方向 direct 方向
        EMMessage.Direct direct = message.direct();
        //方向direct 是一个枚举  一共两种取值 发送SEND  receive 接受
        return direct == EMMessage.Direct.SEND ? 0:1;
    }

    class MyViewHolder extends RecyclerView.ViewHolder{
        //显示消息时间的textView
        private TextView tv_time;
        //显示消息内容的textView
        private TextView tv_message;
        //显示发送消息状态的imageview
        private ImageView iv_state;

        public MyViewHolder(View itemView) {
            super(itemView);
            tv_message = (TextView) itemView.findViewById(R.id.tv_message);
            tv_time = (TextView) itemView.findViewById(R.id.tv_time);
            iv_state = (ImageView) itemView.findViewById(R.id.iv_state);
        }
    }
}
