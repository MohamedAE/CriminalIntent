package criminalintent.android.bignerdranch.com.criminalintent;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;

import java.util.Calendar;
import java.util.Date;

public class DatePickerFragment extends android.support.v4.app.DialogFragment {

	private static final String ARG_DATE = "date";

	private DatePicker mDatePicker;

	public static DatePickerFragment newInstance(Date date) {
		//Create Bundle
		Bundle args = new Bundle();
		//Place Date parameter into Bundle
		args.putSerializable(ARG_DATE, date);

		//Generate DatePickerFragment
		DatePickerFragment fragment = new DatePickerFragment();
		//Associate DatePickerFragment with Bundle
		fragment.setArguments(args);
		return fragment;
	}

	/*
	Builds a dialog window with
		a title
		an OK button
	 */
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		Date date = (Date) getArguments().getSerializable(ARG_DATE);

		/*
		Date objects contain a complete timestamp; must be broken into components
		 */
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		int year = calendar.get(Calendar.YEAR);
		int month = calendar.get(Calendar.MONTH);
		int day = calendar.get(Calendar.DAY_OF_MONTH);

		//Inflate DatePicker layout
		View v = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_date, null);
		//Pass layout to new DatePicker
		mDatePicker = (DatePicker) v.findViewById(R.id.dialog_date_date_picker);
		mDatePicker.init(year, month, day, null);

		//Pass Context to AlertDialog.Builder constructor; returns instance
		return new AlertDialog.Builder(getActivity())
				.setView(v)
				.setTitle(R.string.date_picker_title)
				.setPositiveButton(android.R.string.ok, null)
				.create();
	}

}