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
import android.widget.ToggleButton;

public class ManualActivity extends Activity {
	private TableController controller = new TableController();
	private String SELECT_ACTION = "Select an action!";
	Point point = new Point(1, 5);
	Line line = new Line(point, point, "sada");
	Slice start = new Slice("123", line, new Date(), new Date(), "ololo",
			SliceType.CALL);

	private TextView title;
	private ToggleButton butCall;
	private ToggleButton butWork;
	private ToggleButton butRest;
	private ToggleButton butWalk;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.manual_activity);

		butCall = (ToggleButton) findViewById(R.id.butCall);
		butWork = (ToggleButton) findViewById(R.id.butWork);
		butRest = (ToggleButton) findViewById(R.id.butRest);
		butWalk = (ToggleButton) findViewById(R.id.butWalk);

		title = (TextView) findViewById(R.id.title);
		title.setText(SELECT_ACTION);

	}

	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.butCall:
			title.setText(CharSequence());
			buildButtonPendingIntent(SliceType.CALL);
			butWork.setChecked(false);
			butWalk.setChecked(false);
			butRest.setChecked(false);
			break;
		case R.id.butWork:
			title.setText(CharSequence());
			buildButtonPendingIntent(SliceType.WORK);
			butCall.setChecked(false);
			butWalk.setChecked(false);
			butRest.setChecked(false);
			break;
		case R.id.butRest:
			title.setText(CharSequence());
			buildButtonPendingIntent(SliceType.REST);
			butCall.setChecked(false);
			butWalk.setChecked(false);
			butWork.setChecked(false);
			break;
		case R.id.butWalk:
			title.setText(CharSequence());
			buildButtonPendingIntent(SliceType.WALK);
			butCall.setChecked(false);
			butRest.setChecked(false);
			butWork.setChecked(false);
			break;
		}
	}

	public void buildButtonPendingIntent(SliceType type) {
		Point point = new Point(1, 5);
		Line line = new Line(point, point, "sada");

		if (controller.getTable().getList().isEmpty()) {
			Slice manualstart = new Slice("123", line, new Date(), new Date(),
					"ololo", type);
			// manualstart.setStartDate(new Date());
			// manualstart.setType(type);
			// controller.getTable().addSlise(start);

			controller.addSlise(manualstart);
		}

		ArrayList<Slice> slices = controller.getTable().getList();
		slices.get(slices.size() - 1).setEndDate(new Date());

		Slice manualnext = new Slice("123", line, new Date(), new Date(),
				"ololo", type);
		// manualnext.setStartDate(new Date());
		// manualnext.setType(type);
		// slices.add(manualnext);
		controller.addSlise(manualnext);
	}

	public String CharSequence() {
		SimpleDateFormat sdf = new SimpleDateFormat("kk:mm:ss");
		String currentDateandTime = "Start at:" + sdf.format(new Date());
		return currentDateandTime;
	}
}
