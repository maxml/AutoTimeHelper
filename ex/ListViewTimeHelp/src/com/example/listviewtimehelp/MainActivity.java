package com.example.listviewtimehelp;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import entity.Slice;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

public class MainActivity extends ActionBarActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ListView listMain = (ListView) findViewById(R.id.listView);
        Date current = new Date(0);
        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Date date = new Date(0);




        Slice slice = new Slice("1",current,date,"Description", Slice.SliceType.WALK);
        Slice slice2 = new Slice("2",current,date,"Description", Slice.SliceType.CALL);
        Slice slice3 = new Slice("3",current,date,"Description", Slice.SliceType.REST);
        Slice slice4 = new Slice("4",current,date,"Description", Slice.SliceType.WORK);
        Slice slice5 = new Slice("5",current,date,"Description", Slice.SliceType.WALK);
        Slice slice6 = new Slice("6",current,date,"Description", Slice.SliceType.CALL);
        Slice slice7 = new Slice("7",current,date,"Description", Slice.SliceType.REST);
        Slice slice8 = new Slice("8",current,date,"Description", Slice.SliceType.WORK);

        List<Slice> list = new ArrayList<Slice>();
        list.add(slice);
        list.add(slice2);
        list.add(slice3);
        list.add(slice4);
        list.add(slice5);
        list.add(slice6);
        list.add(slice7);
        list.add(slice8);

        final MenegerAdapter adapter = new MenegerAdapter(this,
                R.layout.activity_item, list);

        listMain.setAdapter(adapter);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
