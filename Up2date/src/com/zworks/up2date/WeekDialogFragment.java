package com.zworks.up2date;

import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

public class WeekDialogFragment extends DialogFragment{
	List<Integer> mSelectedItems = null;
	String[] days =null;
	

	public List getmSelectedItems() {
		return mSelectedItems;
	}
	
	public void setmSelectedItems(List<Integer> SelectedItems){
		this.mSelectedItems = SelectedItems;
	}
	public interface SelectWeekDialogListener {
		void onFinishEditDialog(List<Integer> selecteddays,String[] days);
	}


	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		days =getResources().getStringArray(R.array.week);
		//mSelectedItems = new ArrayList(); // Where we track the selected items
		// Use the Builder class for convenient dialog construction
		boolean[] checkedItem = new boolean[7];
		for(int i=0;i<days.length;i++){
			if(mSelectedItems.contains(i)){
				checkedItem[i]=true;
			}else{
				checkedItem[i]=false;
			}
		}
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setTitle(R.string.title_week).setMultiChoiceItems(R.array.week, checkedItem,new DialogInterface.OnMultiChoiceClickListener() {
							
							public void onClick(DialogInterface dialog,int which, boolean isChecked) {
								if (isChecked) {
									// If the user checked the item, add it to
									// the selected items
									mSelectedItems.add(which);
								} else if (mSelectedItems.contains(which)) {
									// Else, if the item is already in the
									// array, remove it
									
									mSelectedItems.remove(mSelectedItems.indexOf(which));
								}
							}
							
						}).setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
				               
				               public void onClick(DialogInterface dialog, int id) {
				                   // User clicked OK, so save the mSelectedItems results somewhere
				                   // or return them to the component that opened the dialog
				       			SelectWeekDialogListener activity = (SelectWeekDialogListener) getActivity();
		
				       			activity.onFinishEditDialog(mSelectedItems,days);
				    			
				               }
				           })
				           .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
				               
				               public void onClick(DialogInterface dialog, int id) {
				                  
				               }
				           });
		// Create the AlertDialog object and return it
		return builder.create();
	}



}

