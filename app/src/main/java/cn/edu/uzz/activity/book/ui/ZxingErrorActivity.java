package cn.edu.uzz.activity.book.ui;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;

import cn.edu.uzz.activity.book.R;

/**
 * Created by 10616 on 2018/3/19.
 */

public class ZxingErrorActivity extends Activity{
	private ImageView saomiao_return;
	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_zxing_error);
		saomiao_return=findViewById(R.id.saomiao_return);
		saomiao_return.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				finish();
			}
		});
	}
}
