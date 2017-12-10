package cn.edu.uzz.activity.book.cn.edu.uzz.activity.book.ui;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import cn.edu.uzz.activity.book.R;

/**
 * Created by 10616 on 2017/12/10.
 */

public class NullActivity extends Activity{
	private TextView n_title;
	private TextView hint;
	private ImageView null_return;
	public static Activity instance;
	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_null);
		String title=getIntent().getStringExtra("title");
		String main=getIntent().getStringExtra("main");

		n_title=findViewById(R.id.n_title);
		n_title.setText(title);

		hint=findViewById(R.id.null_hint);
		hint.setText("您还未"+main+"任何书籍哦！");

		null_return=findViewById(R.id.null_return);
		null_return.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				finish();
			}
		});
	}
}
