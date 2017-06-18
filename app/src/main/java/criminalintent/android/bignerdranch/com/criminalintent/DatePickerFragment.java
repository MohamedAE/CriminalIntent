package criminalintent.android.bignerdranch.com.criminalintent;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

public class DatePickerFragment extends android.support.v4.app.DialogFragment {

	/*
	Builds a dialog window with
		a title
		an OK button
	 */
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		//Inflate DatePicker layout
		View v = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_date, null);

		//Pass Context to AlertDialog.Builder constructor; returns instance
		return new AlertDialog.Builder(getActivity())
				.setView(v)
				.setTitle(R.string.date_picker_title)
				.setPositiveButton(android.R.string.ok, null)
				.create();
	}

}