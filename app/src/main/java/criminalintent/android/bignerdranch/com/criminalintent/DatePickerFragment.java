package criminalintent.android.bignerdranch.com.criminalintent;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class DatePickerFragment extends PickerDialogFragment {

	private DatePicker mDatePicker;

	public static DatePickerFragment newInstance(Date date) {
		Bundle args = getArgs(date);
		DatePickerFragment fragment = new DatePickerFragment();
		fragment.setArguments(args);
		return fragment;
	}

	/*
	Inflate DatePicker layout
	Pull current data from model object
	 */
	protected View initLayout() {
		View v = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_date, null);
		mDatePicker = (DatePicker) v.findViewById(R.id.dialog_date_date_picker);
		mDatePicker.init(
				mCalendar.get(Calendar.YEAR),
				mCalendar.get(Calendar.MONTH),
				mCalendar.get(Calendar.DAY_OF_MONTH),
				null
		);
		return v;
	}

	protected Date getDate() {
		int year = mDatePicker.getYear();
		int month = mDatePicker.getMonth();
		int day = mDatePicker.getDayOfMonth();

		int hour = mCalendar.get(Calendar.HOUR_OF_DAY);
		int minute = mCalendar.get(Calendar.MINUTE);

		return new GregorianCalendar(year, month, day, hour, minute).getTime();
	}

}