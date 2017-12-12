package cn.edu.uzz.activity.book.cn.edu.uzz.activity.book.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import cn.edu.uzz.activity.book.R;

/**
 * Created by 10616 on 2017/11/17.
 */

public class Tab02 extends Fragment implements View.OnClickListener{
	private Button makeBtn;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.tab02, container, false);
    }

	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		initView();
	}

	private void initView() {
    	makeBtn=getView().findViewById(R.id.jinruerweima);
    	makeBtn.setOnClickListener(this);
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()){
			case R.id.jinruerweima:
				Intent intent=new Intent();
				/*intent.putExtra("type",1);
				intent.putExtra("id",1);*/
				intent.setClass(getActivity(),ErweimaActivity.class);
				startActivity(intent);
		}
	}
}
