package cn.edu.uzz.activity.book.ui;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import java.io.File;

import cn.edu.uzz.activity.book.R;

/**
 * Created by 10616 on 2017/11/17.
 */

public class Tab03 extends Fragment implements View.OnClickListener{
	private LinearLayout myLike;
	private LinearLayout mysub;
	private Button loginBtn;
	private LinearLayout myCar;
	private LinearLayout myInfor;
	private LinearLayout myRenting;
	private LinearLayout myHistory;
	private LinearLayout mySuggest;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.tab03, container, false);
    }

	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		initView();
	}

	private void initView() {
		myLike= (LinearLayout) getView().findViewById(R.id.layout2);
		mysub= (LinearLayout) getView().findViewById(R.id.layout3);
		loginBtn= (Button) getView().findViewById(R.id.logout_btn);
		myCar= (LinearLayout) getView().findViewById(R.id.layout7);
		myRenting=getView().findViewById(R.id.layout4);
		myInfor= getView().findViewById(R.id.layout1);
		myHistory=getView().findViewById(R.id.layout6);
		mySuggest=getView().findViewById(R.id.layout8);
		File file= new File("/data/data/cn.edu.uzz.activity.book/shared_prefs","user.xml");
		if(file.exists())
		{
			loginBtn.setText("退出登录");
		}else{
			loginBtn.setText("登录");
		}
		loginBtn.setOnClickListener(this);
		myLike.setOnClickListener(this);
		mysub.setOnClickListener(this);
		myCar.setOnClickListener(this);
		myInfor.setOnClickListener(this);
		myRenting.setOnClickListener(this);
		myHistory.setOnClickListener(this);
		mySuggest.setOnClickListener(this);
    }

	@Override
	public void onClick(View view) {
		switch (view.getId()){
			case R.id.layout2:
				startActivity(new Intent(getActivity(),LikeListAvtivity.class));
				((Activity) getContext()).overridePendingTransition(R.anim.anim_in,R.anim.anim_out);
				break;
			case R.id.layout1:
				startActivity(new Intent(getActivity(),MyInforActivity.class));
				break;
			case R.id.layout3:
				startActivity(new Intent(getActivity(),MySubActivity.class));
				((Activity) getContext()).overridePendingTransition(R.anim.anim_in,R.anim.anim_out);
				break;
			case R.id.logout_btn:
				File file= new File("/data/data/cn.edu.uzz.activity.book/shared_prefs","user.xml");
				if(file.exists())
				{
					file.delete();
					loginBtn.setText("登录");
					Intent intent = getActivity().getPackageManager()
							.getLaunchIntentForPackage(getActivity().getPackageName());
					PendingIntent restartIntent = PendingIntent.getActivity(getActivity(), 0, intent, PendingIntent.FLAG_ONE_SHOT);
					AlarmManager mgr = (AlarmManager) getContext().getSystemService(Context.ALARM_SERVICE);
					mgr.set(AlarmManager.RTC, System.currentTimeMillis()+500 , restartIntent); // 1秒钟后重启应用
					System.exit(0);
				}else{
					startActivity(new Intent(getActivity(),LoginActivity.class));
					((Activity) getContext()).overridePendingTransition(R.anim.anim_in,R.anim.anim_out);
				}
				break;
			case R.id.layout7:
				startActivity(new Intent(getActivity(),CarActivity.class));
				((Activity) getContext()).overridePendingTransition(R.anim.anim_in,R.anim.anim_out);
				break;
			case R.id.layout4:
				startActivity(new Intent(getActivity(),RentingActivity.class));
				((Activity) getContext()).overridePendingTransition(R.anim.anim_in,R.anim.anim_out);
				break;
			case R.id.layout6:
				startActivity(new Intent(getActivity(),RentHistoryActivity.class));
				((Activity) getContext()).overridePendingTransition(R.anim.anim_in,R.anim.anim_out);
				break;
			case R.id.layout8:
				//发起会话
				break;
			default:
				break;
		}
	}
}
