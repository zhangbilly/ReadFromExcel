package com.zworks.up2date;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import com.zworks.up2date.Constants.Program;
import com.zworks.up2date.Constants.Record;
import com.zworks.up2date.DateDialogFragment.SelectDateDialogListener;
import com.zworks.up2date.WeekDialogFragment.SelectWeekDialogListener;
import com.zworks.up2date.util.DatabaseHelper;
import com.zworks.up2date.util.DaysUtil;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class AddProgramActivity extends Activity implements SelectWeekDialogListener,SelectDateDialogListener{

	private String programName;
	private long begindate=0;
	private String updateTime;
	private List<Integer> selecteddays;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_program);
	}

	public void showWeekDialog(View view){
		WeekDialogFragment weekDialogFragment = new WeekDialogFragment();
		if(selecteddays!=null){
			weekDialogFragment.setmSelectedItems(selecteddays);
		}else{
			weekDialogFragment.setmSelectedItems(new ArrayList<Integer>());
		}
		
		weekDialogFragment.show(getFragmentManager(), "weekdialog");
		
	}
	public String getProgramName(){
		return ((TextView) findViewById(R.id.programName)).getText().toString();
	}


	public void onFinishEditDialog(List<Integer> selecteddays,String[] days) {
		// TODO Auto-generated method stub
		this.selecteddays = selecteddays;
		TextView textView = (TextView) findViewById(R.id.selectedweek);
		String dayName = DaysUtil.getDayName(selecteddays,days);
		textView.setText(dayName);
		this.updateTime = dayName;
	}
	public void showBeginDateDialog(View view){
		DateDialogFragment dateDialogFragment = new DateDialogFragment();
		Calendar selectedDate = Calendar.getInstance(Locale.getDefault());
		if(begindate!=0)
			selectedDate.setTimeInMillis(begindate);	
		dateDialogFragment.setSelectedDate(selectedDate);
		dateDialogFragment.show(getFragmentManager(), "datedialog");
	}

	public void onFinishSelectDateDialog(long date) {
		// TODO Auto-generated method stub
		this.begindate = date;
		Calendar c = Calendar.getInstance(Locale.getDefault());
		c.setTimeInMillis(begindate);
		//System.out.println(c.get(Calendar.DATE));
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd ");
		String sb = format.format(c.getTime());
		System.out.println(sb);
		TextView textView = (TextView) findViewById(R.id.selectedbegindate);
		textView.setText(sb);
	}
	public void addProgram(View view){
		if(getProgramName().matches("")){
			Toast.makeText(this, "You did not enter a programname", Toast.LENGTH_SHORT).show();
		    return;
		}
		if(begindate==0){
			Toast.makeText(this, "You did not select a begindate", Toast.LENGTH_SHORT).show();
		    return;
		}
		if(selecteddays==null){
			Toast.makeText(this, "You did not select a play time", Toast.LENGTH_SHORT).show();
		    return;
		}
		DatabaseHelper dbHelper = new DatabaseHelper(this);
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(Program.PROGRAM_NAME, getProgramName());
		values.put(Program.UPDATE_TIME, this.updateTime);
		values.put(Program.BEGIN_TIME, this.begindate);
		long programId = db.insert(Program.TABLE_NAME,null,values);
		List<Long> days = DaysUtil.initRecordDate(selecteddays, begindate);
		System.out.println("需添加的数据："+days);
		for(int i=0;i<days.size();i++){
			ContentValues record = new ContentValues();
			record.put(Record.PROGRAM_NAME, programId);
			record.put(Record.RECORD_TIME, days.get(i));
			record.put(Record.ISNEW,1);
			db.insert(Record.TABLE_NAME, null, record);
		}
		db.close();
		Intent intent = new Intent();
		// 把返回数据存入Intent
		intent.putExtra(Program.PROGRAM_NAME, getProgramName());
		intent.putExtra(Program._ID, String.valueOf(programId));
		intent.putExtra(Program.UPDATE_TIME, this.updateTime);
		intent.putExtra("COUNT", days.size());
		// 设置返回数据
		this.setResult(RESULT_OK, intent);
		this.finish();

	}
}
