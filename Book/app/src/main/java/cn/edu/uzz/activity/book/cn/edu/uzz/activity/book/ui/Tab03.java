package cn.edu.uzz.activity.book.cn.edu.uzz.activity.book.ui;

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
		myLike=getView().findViewById(R.id.layout2);
		mysub=getView().findViewById(R.id.layout3);
		loginBtn=getView().findViewById(R.id.logout_btn);
		myCar=getView().findViewById(R.id.layout7);
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
    }

	@Override
	public void onClick(View view) {
		switch (view.getId()){
			case R.id.layout2:
				startActivity(new Intent(getActivity(),LikeListAvtivity.class));
				break;
			case R.id.layout1:
				break;
			case R.id.layout3:
				startActivity(new Intent(getActivity(),MySubActivity.class));
				break;
			case R.id.logout_btn:
				File file= new File("/data/data/cn.edu.uzz.activity.book/shared_prefs","user.xml");
				if(file.exists())
				{
					file.delete();
					loginBtn.setText("登录");
				}else{
					startActivity(new Intent(getActivity(),LoginActivity.class));
				}
				break;
			case R.id.layout7:
				startActivity(new Intent(getActivity(),CarActivity.class));
			default:
				break;
		}
	}
}
