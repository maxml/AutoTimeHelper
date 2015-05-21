package com.maxml.timer.ui.activity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import com.maxml.timer.R;
import com.maxml.timer.controllers.TableController;
import com.maxml.timer.entity.Line;
import com.maxml.timer.entity.Point;
import com.maxml.timer.entity.Slice;
import com.maxml.timer.entity.Slice.SliceType;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class ManualActivity extends Activity {
	private TableController controller = new TableController();

	private TextView title;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.manual_activity);

		title = (TextView) findViewById(R.id.title);
		title.setText(CharSequence());
	}

	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.butCall:
			buildButtonPendingIntent(SliceType.CALL);
			title.setText(CharSequence());
			break;
		case R.id.butWork:
			buildButtonPendingIntent(SliceType.WORK);
			title.setText(CharSequence());
			break;
		case R.id.butRest:
			buildButtonPendingIntent(SliceType.REST);
			title.setText(CharSequence());
			break;
		case R.id.butWalk:
			buildButtonPendingIntent(SliceType.WALK);
			title.setText(CharSequence());
			break;
		}
	}

	public void buildButtonPendingIntent(SliceType type) {
		Point point = new Point(777, 777);
		Line line = new Line(point, point, "sada");

		if (controller.getTable().getList().isEmpty()) {
			Slice start = new Slice("123", line, new Date(), new Date(),
					"OLololo", type);
			start.setStartDate(new Date());
			start.setType(type);

//			controller.getTable().addSlise(start);
			 controller.addSlise(start);
		}

		ArrayList<Slice> slices = controller.getTable().getList();
		slices.get(slices.size() - 1).setEndDate(new Date());

		Slice manualstart = new Slice("123", line, new Date(), new Date(),
				"OLololo", type);
		manualstart.setStartDate(new Date());
		manualstart.setType(type);

//		 slices.add(manualstart);
		controller.addSlise(manualstart);
	}

	public String CharSequence() {
		SimpleDateFormat sdf = new SimpleDateFormat("kk:mm:ss");
		String currentDateandTime = sdf.format(new Date());
		return currentDateandTime;
	}
}
