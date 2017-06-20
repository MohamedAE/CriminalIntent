package criminalintent.android.bignerdranch.com.criminalintent;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class DatePickerFragment extends android.support.v4.app.DialogFragment {

	public static final String EXTRA_DATE = "com.bignerdranch.android.criminalintent.date";

	private static final String ARG_DATE = "date";

	private DatePicker mDatePicker;

	/*
	Generates a new instance of DatePickerFragment
	Bundles it with the Date object (parameter)
	*/
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
	Builds a dialog window with:
		a title
		an OK button
	*/
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		Date date = (Date) getArguments().getSerializable(ARG_DATE);

		//Break Date object's complete timestamp into components
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

		//Pass Context to AlertDialog.Builder constructor; return instance of AlertDialog.Builder
		return new AlertDialog.Builder(getActivity())
				.setView(v)
				.setTitle(R.string.date_picker_title)
				//When Dialog's positive button is pressed, execute
				.setPositiveButton(android.R.string.ok,
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								//Extract Date info from DatePicker
								int year = mDatePicker.getYear();
								int month = mDatePicker.getMonth();
								int day = mDatePicker.getDayOfMonth();
								Date date = new GregorianCalendar(year, month, day).getTime();
								sendResult(Activity.RESULT_OK, date);
							}
						})
				.create();
	}


	private void sendResult(int resultCode, Date date) {
		if (getTargetFragment() == null) {
			return;
		}

		//Packages date into an Intent
		Intent intent = new Intent();
		intent.putExtra(EXTRA_DATE, date);

		//Return a Fragment to that which targets this Fragment
		getTargetFragment().onActivityResult(getTargetRequestCode(), resultCode, intent);
	}

}