package cn.edu.uzz.activity.book.cn.edu.uzz.activity.book.ui;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;

import com.xys.libzxing.zxing.encoding.EncodingUtils;

import cn.edu.uzz.activity.book.R;

/**
 * Created by 10616 on 2017/12/11.
 */

public class ErweimaActivity extends Activity{
	private ImageView ewm;
	private ImageView ewm_return;
	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_erweima);
		ewm=findViewById(R.id.ewm);
		ewm_return=findViewById(R.id.ewm_return);
		ewm_return.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				finish();
			}
		});
		make();
	}
	public void make(){
		Bitmap bitmap= EncodingUtils.createQRCode("www",256,256,null);
		ewm.setImageBitmap(bitmap);
		BitmapFactory.decodeResource(getResources(),R.drawable.ic_launcher_background);
	}
}
