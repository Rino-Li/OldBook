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
	private int type1;
	private int id1;
	private int type2;
	private int id2;
	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_erweima);
		initView();

	}

	private void initView() {
		ewm=findViewById(R.id.ewm);
		ewm_return=findViewById(R.id.ewm_return);
		ewm_return.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				finish();
			}
		});
		int length=getIntent().getIntExtra("length",0);
		if (length==1){
			type1=getIntent().getIntExtra("type1",0);
			id1=getIntent().getIntExtra("id1",0);
			make();
		}else{
			type1=getIntent().getIntExtra("type1",0);
			id1=getIntent().getIntExtra("id1",0);
			type2=getIntent().getIntExtra("type2",0);
			id2=getIntent().getIntExtra("id2",0);
			make2();
		}

	}

	private void make2() {
		Bitmap bitmap= EncodingUtils.createQRCode(type1+"+"+id1+"+"+type2+"+"+id2,256,256,null);
		ewm.setImageBitmap(bitmap);
		BitmapFactory.decodeResource(getResources(),R.drawable.ic_launcher_background);
	}

	public void make(){
		Bitmap bitmap= EncodingUtils.createQRCode(type1+"+"+id1,256,256,null);
		ewm.setImageBitmap(bitmap);
		BitmapFactory.decodeResource(getResources(),R.drawable.ic_launcher_background);
	}
}
