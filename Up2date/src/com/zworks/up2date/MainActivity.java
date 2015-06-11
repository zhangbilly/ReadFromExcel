package com.zworks.up2date;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import com.zworks.up2date.Constants.Program;
import com.zworks.up2date.Constants.Record;
import com.zworks.up2date.util.DatabaseHelper;
import com.zworks.up2date.util.DaysUtil;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class MainActivity extends Activity {
	static final int PROGRAM_DETAIL_REQUEST = 1;
	static final int ADD_PROGRAM_REQUEST = 2;
	private ListView listView;
	private List<Map<String, String>> programList;
	private ProgramAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		listView = (ListView) findViewById(R.id.main_activity_listview);
		Calendar lastLoginDay = Calendar.getInstance(Locale.getDefault());
		Calendar today = Calendar.getInstance(Locale.getDefault());
		String[] days = getResources().getStringArray(R.array.week);
		SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sharedPref.edit();
		long LastLoginDayInLong = sharedPref.getLong("LastLoginDay", 0);
		lastLoginDay.setTimeInMillis(LastLoginDayInLong);
		boolean needUpdate = !DaysUtil.isSameDay(lastLoginDay, today)
				&& LastLoginDayInLong != 0&&lastLoginDay.before(today);
		System.out.println(lastLoginDay.toString());
		System.out.println(today.toString());
		String[] columns = new String[] { Program._ID, Program.PROGRAM_NAME,
				Program.UPDATE_TIME };
		int day_of_week = Calendar.getInstance(Locale.getDefault()).get(
				Calendar.DAY_OF_WEEK);
		day_of_week = (day_of_week + 6) % 7;
		DatabaseHelper dbHelper = new DatabaseHelper(this);
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		programList = new ArrayList<Map<String, String>>();
		Cursor cursor = db.query(Program.TABLE_NAME, columns, null, null, null,
				null, null);
		if (cursor != null && cursor.getCount() > 0) {
			cursor.moveToFirst();
			for (int i = 0; i < cursor.getCount(); i++) {
				String programName = cursor.getString(cursor
						.getColumnIndexOrThrow(Program.PROGRAM_NAME));
				String updateTime = cursor.getString(cursor
						.getColumnIndexOrThrow(Program.UPDATE_TIME));
				String programId = cursor.getString(cursor
						.getColumnIndexOrThrow(Program._ID));
				long count = DatabaseUtils.queryNumEntries(db,
						Record.TABLE_NAME, Record.PROGRAM_NAME + "=? and "
								+ Record.ISNEW + "=?", new String[] {
								programId, Integer.toString(1) });
				if (needUpdate) {

					List<Integer> intList = DaysUtil.weekStringToInt(
							updateTime, days);
					Calendar tmp = Calendar.getInstance(Locale.getDefault());
					tmp.setTimeInMillis(LastLoginDayInLong);
					tmp.add(Calendar.DATE, 1);
					List<Long> needAddDays = DaysUtil.initRecordDate(intList,
							tmp.getTimeInMillis());
					System.out.println("needAddDays:"+needAddDays);
					count += needAddDays.size();
					for (int j = 0; j < needAddDays.size(); j++) {
						ContentValues record = new ContentValues();
						record.put(Record.PROGRAM_NAME, programId);
						record.put(Record.RECORD_TIME, needAddDays.get(j));
						record.put(Record.ISNEW, 1);
						db.insert(Record.TABLE_NAME, null, record);
					}
				}

				System.out.println(count);
				Map<String, String> map = new HashMap<String, String>();
				map.put(Program.PROGRAM_NAME, programName);
				map.put(Program.UPDATE_TIME, updateTime);
				map.put(Program._ID, programId);
				map.put("COUNT", Long.toString(count));

				programList.add(map);
				// programIdlist.add(programId);
				cursor.moveToNext();
			}
			cursor.close();
			sortList();
			
		}
		adapter = new ProgramAdapter(this);
		//System.out.println("Adapter count:"+adapter.getCount());
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub

				String programName = ((TextView) view
						.findViewById(R.id.name)).getText().toString();

				Intent intent = new Intent(listView.getContext(),
						ProgramActivity.class);
				intent.putExtra("PROGRAMNAME", programName);
				intent.putExtra("UPDATETIME", programList.get(position)
						.get(Program.UPDATE_TIME));
				intent.putExtra("PROGRAMID",
						programList.get(position).get(Program._ID));
				intent.putExtra("POSITION", position);
				startActivityForResult(intent, PROGRAM_DETAIL_REQUEST);
			}

		});
		db.close();
		editor.putLong("LastLoginDay", today.getTimeInMillis());
		editor.commit();

	}

	public void sortList() {
		Collections.sort(programList, new Comparator<Map<String, String>>() {

			@Override
			public int compare(Map<String, String> lhs, Map<String, String> rhs) {
				// TODO Auto-generated method stub
				int map1value = Integer.valueOf(lhs.get("COUNT"));
				int map2value = Integer.valueOf(rhs.get("COUNT"));
				return map2value - map1value;
			}

		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu items for use in the action bar
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle presses on the action bar items
		switch (item.getItemId()) {
		case R.id.action_add:
			openAdd();
			return true;
		case R.id.action_settings:
			openSettings();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	public void openAdd() {
		Intent intent = new Intent(this, AddProgramActivity.class);
		startActivityForResult(intent, ADD_PROGRAM_REQUEST);
		
		System.out.println("Adapter:"+listView.getAdapter());
	}

	private void openSettings() {

	}

	public class ProgramAdapter extends BaseAdapter {
		private LayoutInflater mInflater = null;

		public ProgramAdapter(Context context) {
			mInflater = LayoutInflater.from(context);
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return programList.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return programList.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder;
			if (convertView == null) {
				holder = new ViewHolder();
				convertView = mInflater.inflate(R.layout.program, null);
				holder.programName = (TextView) convertView
						.findViewById(R.id.name);
				holder.isNew = (TextView) convertView.findViewById(R.id.isnew);

				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();

			}
			holder.programName.setText(programList.get(position).get(
					Program.PROGRAM_NAME));
			int count = Integer.valueOf(programList.get(position).get("COUNT"));
			if (count == 0)
				holder.isNew.setText("");
			else {
				holder.isNew.setText(String.valueOf(count));
			}

			return convertView;
		}

	}

	static class ViewHolder {
		public TextView programName;
		public TextView isNew;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// Check which request we're responding to
		System.out.println(adapter);
		if (requestCode == PROGRAM_DETAIL_REQUEST) {
			// Make sure the request was successful
			if (resultCode == RESULT_OK) {
				int position = data.getIntExtra("POSITION", 0);

				programList.remove(position);

				adapter.notifyDataSetChanged();
			} else if (resultCode == RESULT_CANCELED) {
				int position = data.getIntExtra("POSITION", 0);
				int count = data
						.getIntExtra(
								"COUNT",
								Integer.valueOf(programList.get(position).get(
										"COUNT")));
				programList.get(position).put("COUNT", String.valueOf(count));
				sortList();
				adapter.notifyDataSetChanged();
			}
		}
		if (requestCode == ADD_PROGRAM_REQUEST) {
			if (resultCode == RESULT_OK) {
				String programId = data.getStringExtra(Program._ID);
				String programName = data.getStringExtra(Program.PROGRAM_NAME);
				String updateTime = data.getStringExtra(Program.UPDATE_TIME);
				int count = data.getIntExtra("COUNT", 0);
				Map<String, String> map = new HashMap<String, String>();
				map.put(Program.PROGRAM_NAME, programName);
				map.put(Program.UPDATE_TIME, updateTime);
				map.put(Program._ID, programId);
				System.out.println("ProgramID in MainActivity:" + programId);
				map.put("COUNT", Integer.toString(count));

				programList.add(map);
				sortList();
				
				adapter.notifyDataSetChanged();
			}
		}
	}

}
