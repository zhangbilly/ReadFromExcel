package com.zworks.up2date.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

public class DaysUtil {
	public static String getDayName(List<Integer> selecteddays,String[] days){
		if(selecteddays.size()==0){
			return "";
		}
		StringBuilder sb = new StringBuilder();
		Collections.sort(selecteddays);
		
		for(Integer i:selecteddays){
			sb.append(days[i]);
			sb.append(" ");
		}
		sb.deleteCharAt(sb.length()-1);
		return sb.toString();
		
		
	}
	public static List<Long> initRecordDate(List<Integer> updatetime,long beginTime){
		Calendar today = Calendar.getInstance(Locale.getDefault());
		
		List<Long> days = new ArrayList<Long>();
		
		Collections.sort(updatetime);		
		for(int i:updatetime){
			Calendar beginday = Calendar.getInstance(Locale.getDefault());
			beginday.setTimeInMillis(beginTime);
			int initdays;
			long weeks;
			System.out.println("播放日："+i+"\n"+"开始："+beginday.get(Calendar.DATE)+"\n");
			int b_day_of_week = (beginday.get(Calendar.DAY_OF_WEEK)+5)%7;
			int e_day_of_week = (today.get(Calendar.DAY_OF_WEEK)+5)%7;
			long diffInDays = ( (today.getTimeInMillis() - beginTime) / (1000 * 60 * 60 * 24) );
			System.out.println(b_day_of_week+"\n"+e_day_of_week+"\n");
			weeks=diffInDays/7;
			if(b_day_of_week<=e_day_of_week&&(i>=b_day_of_week&&i<=e_day_of_week))
				weeks++;
			if(b_day_of_week>e_day_of_week&&(i>=b_day_of_week||i<=e_day_of_week)){
				weeks++;
			}
			System.out.println(weeks+"\n");
			if(i>=b_day_of_week)
				initdays=i-b_day_of_week;
			else
				initdays = 7-b_day_of_week+i;
			System.out.println("初始天数："+initdays+"\n");
			beginday.add(Calendar.DATE, initdays);
			if(!beginday.after(today))
				days.add(beginday.getTimeInMillis());
			for(i=1;i<weeks;i++){
				beginday.add(Calendar.DATE, 7);
				days.add(beginday.getTimeInMillis());
			}
		}
		Collections.sort(days);
		System.out.println(days);
		return days;
		
	}
	public static boolean isSameDay(Calendar cal1,Calendar cal2){
		return cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) && cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR);
	}
	public static List<Integer> weekStringToInt(String week,String[] days){
		List<Integer> intlist = new ArrayList<>();
		List<String> daylist = Arrays.asList(days);
		String[] daysInString = week.split(" ");
		for(String s:daysInString){
			intlist.add(daylist.indexOf(s));
		}
		
		return intlist;
		
	}
	

	
}
