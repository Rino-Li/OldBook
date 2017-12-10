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

public class NoLoginActivity extends Activity{
	private TextView title;
	private ImageView faile_return;
	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login_fail);
		title=findViewById(R.id.change_title);
		String chage_t=getIntent().getStringExtra("title");
		title.setText(chage_t);

		faile_return=findViewById(R.id.fail_return);
		faile_return.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				finish();
			}
		});
	}
}
