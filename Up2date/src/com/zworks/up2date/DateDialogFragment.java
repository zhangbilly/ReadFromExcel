package com.zworks.up2date;


import java.util.Calendar;

import java.util.Locale;
import com.squareup.timessquare.CalendarPickerView;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

public class DateDialogFragment extends DialogFragment{
	CalendarPickerView calendar = null;
	Calendar selectedDate;
	public void setSelectedDate(Calendar selectedDate) {
		this.selectedDate = selectedDate;
	}
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		
	    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
	    // Get the layout inflater
	    LayoutInflater inflater = getActivity().getLayoutInflater();
	    Calendar nextYear = Calendar.getInstance(Locale.getDefault());
		nextYear.add(Calendar.YEAR, 1);
	    final Calendar lastYear = Calendar.getInstance(Locale.getDefault());
	    lastYear.add(Calendar.YEAR, -1);
		View view = inflater.inflate(R.layout.calendar_picker,null);
		calendar = (CalendarPickerView) view.findViewById(R.id.calendar_view);
		final Calendar maxDay = Calendar.getInstance(Locale.getDefault());
		maxDay.add(Calendar.DATE, 1);
		calendar.init(lastYear.getTime(),maxDay.getTime()).withSelectedDate(selectedDate.getTime());
	    // Inflate and set the layout for the dialog
	    // Pass null as the parent view because its going in the dialog layout
	    builder.setView(view)
	    // Add action buttons
	           .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
	               public void onClick(DialogInterface dialog, int id) {
	            	   SelectDateDialogListener activity = (SelectDateDialogListener) getActivity();
	            	   long date = calendar.getSelectedDate().getTime();
	           		
		       			activity.onFinishSelectDateDialog(date);
	               }
	           })
	           .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
	               public void onClick(DialogInterface dialog, int id) {
	            	   DateDialogFragment.this.getDialog().cancel();
	               }
	           });      
	    return builder.create();
	}
	public interface SelectDateDialogListener {
		void onFinishSelectDateDialog(long date);
	}
}
