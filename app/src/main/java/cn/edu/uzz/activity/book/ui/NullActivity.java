package cn.edu.uzz.activity.book.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import cn.edu.uzz.activity.book.R;

public class NullActivity extends AppCompatActivity {
	private ImageView nu_return;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_null);
		nu_return=findViewById(R.id.nu_return);
		nu_return.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				finish();
			}
		});
	}
}
