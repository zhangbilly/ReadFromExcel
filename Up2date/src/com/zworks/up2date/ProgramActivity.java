package com.zworks.up2date;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import com.zworks.up2date.Constants.Program;
import com.zworks.up2date.Constants.Record;
import com.zworks.up2date.ItemDeleteDialogFragment.DeleteRecordItemDialogListener;
import com.zworks.up2date.MainActivity.ProgramAdapter;
import com.zworks.up2date.MainActivity.ViewHolder;
import com.zworks.up2date.util.DatabaseHelper;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class ProgramActivity extends Activity implements DeleteRecordItemDialogListener{
	private ListView listView;
	private List<String> itemIdList;
	private String programName;
	private String updateTime;
	private String programId;
	private int position;
	private int count;
	private RecordAdapter adapter;
	private List<Map<String,String>> recordList;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_program);
		Intent intent = getIntent();
	    programName = intent.getStringExtra("PROGRAMNAME");
	    updateTime = intent.getStringExtra("UPDATETIME");
	    programId = intent.getStringExtra("PROGRAMID");
	    System.out.println("ProgramID:"+programId);
	    position = intent.getIntExtra("POSITION", 0);
	    ((TextView)findViewById(R.id.activity_program_name)).setText(programName);;
	    ((TextView)findViewById(R.id.activity_program_updatetime)).setText(updateTime);
	    listView = (ListView)findViewById(R.id.record_list);

       
        String[] columns = new String[]{Record._ID,Record.RECORD_TIME};

        DatabaseHelper dbHelper = new DatabaseHelper(this);
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		
		recordList = new ArrayList<Map<String,String>>();
		itemIdList = new ArrayList<String>();
		Cursor cursor = db.query(Record.TABLE_NAME, columns, Record.ISNEW+"=? and "+Record.PROGRAM_NAME+"= ?", new String[]{"1",programId}, null, null, Record.RECORD_TIME);
		if(cursor!=null&&cursor.getCount()>0){
			SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
			cursor.moveToFirst();
			count = cursor.getCount();
			for(int i=0;i<count;i++){
				long time = cursor.getLong(cursor.getColumnIndexOrThrow(Record.RECORD_TIME));
				String id = cursor.getString(cursor.getColumnIndexOrThrow(Record._ID));
				Calendar c = Calendar.getInstance(Locale.getDefault());
				c.setTimeInMillis(time);
				String recordTime = formatter.format(c.getTime());
				
				Map<String,String> map = new HashMap<String,String>();
				map.put(Record.RECORD_TIME, recordTime);
				map.put(Record._ID, id);
				//System.out.println(recordTime);
				recordList.add(map);
				itemIdList.add(id);
				cursor.moveToNext();
				
			}
			
			//System.out.println(list);
			adapter = new RecordAdapter(this);
			
	        listView.setAdapter(adapter);
	        listView.setOnItemClickListener(new OnItemClickListener(){

				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					// TODO Auto-generated method stub

					String recordTime = ((TextView)view.findViewById(R.id.record_time)).getText().toString();
					String itemId = itemIdList.get(position);
					ItemDeleteDialogFragment itemDeleteDialogFragment = new ItemDeleteDialogFragment();
					itemDeleteDialogFragment.setRecordDate(recordTime);
					itemDeleteDialogFragment.setRecordId(itemId);
					itemDeleteDialogFragment.setPosition(position);
					itemDeleteDialogFragment.show(getFragmentManager(), "itemdeletedialog");

				}
	        	
	        });
	        
		}
		db.close();
	}
		
	


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.program, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		
	    switch (item.getItemId()) {
        case R.id.delete_program:
            deleteProgram();
            return true;
        case R.id.action_settings:
            openSettings();
            return true;
        default:
            return super.onOptionsItemSelected(item);
    }
	}

	private void deleteProgram() {
		// TODO Auto-generated method stub
		DatabaseHelper dbHelper = new DatabaseHelper(this);
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		db.delete(Record.TABLE_NAME, Record.PROGRAM_NAME+"=?", new String[]{programId});
		db.delete(Program.TABLE_NAME, Program._ID+"=?", new String[]{programId});
		db.close();
		Intent intent = new Intent();
		// 把返回数据存入Intent
		intent.putExtra("POSITION", position);
		//intent.putExtra("COUNT", count);
		// 设置返回数据
		this.setResult(RESULT_OK, intent);
		// 关闭Activity
		this.finish();
	}
	private void openSettings() {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void onDeleteRecordItem(String recordId,int position) {
		// TODO Auto-generated method stub
		 DatabaseHelper dbHelper = new DatabaseHelper(this);
		 SQLiteDatabase db = dbHelper.getWritableDatabase();
		 ContentValues contentValues = new ContentValues();
		 contentValues.put(Record.ISNEW, 0);
		 db.update(Record.TABLE_NAME, contentValues, Record._ID+"=?", new String[]{recordId});
		 db.close();
		 recordList.remove(position);
		 count--;
		 this.refresh();
	}
	@Override
	public void onBackPressed() {
	   // super.onBackPressed();

	    Intent intent = new Intent();
	    intent.putExtra("COUNT", count);
	    intent.putExtra("POSITION", position);
	    setResult(RESULT_CANCELED, intent);
	    finish();
	}
	public void refresh(){
		if(adapter==null){
			adapter = new RecordAdapter(this);
			listView.setAdapter(adapter);
		}
			
		adapter.notifyDataSetChanged();
	}

	public class RecordAdapter extends BaseAdapter{
		private LayoutInflater mInflater = null;
		public RecordAdapter(Context context){
			mInflater = LayoutInflater.from(context);
		}
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return recordList.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return recordList.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			ViewHolder holder;
            if(convertView == null)
            {
                holder = new ViewHolder();
                convertView = mInflater.inflate(R.layout.record_item, null);
                holder.recordTime = (TextView)convertView.findViewById(R.id.record_time) ;
               
                convertView.setTag(holder);
            }else
            {
                holder = (ViewHolder)convertView.getTag();
                

            }
            holder.recordTime.setText(recordList.get(position).get(Record.RECORD_TIME));

            
            return convertView;
		}
		
	}
    static class ViewHolder
    {
        public TextView recordTime;
    }	

}
