package criminalintent.android.bignerdranch.com.criminalintent;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.View;

import java.util.Calendar;
import java.util.Date;

public abstract class PickerDialogFragment extends DialogFragment {

	protected static final String ARG_DATE = "date";
	public static final String EXTRA_DATE = "com.bignerdranch.android.criminalintent.date";

	protected Calendar mCalendar;

	protected abstract View initLayout();
	protected abstract Date getDate();

	protected static Bundle getArgs(Date date) {
		Bundle args = new Bundle();
		args.putSerializable(ARG_DATE, date);
		return args;
	}

	//Builds custom Dialog container
	@NonNull
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		Date date = (Date) getArguments().getSerializable(ARG_DATE);
		mCalendar = Calendar.getInstance();
		mCalendar.setTime(date);

		View v = initLayout();

		//Configure dialog to display the passed in View (this fragment)
		return new AlertDialog.Builder(getActivity())
				.setTitle(R.string.date_picker_title)
				.setView(v)
				//Dialog's confirmation button returns date to CrimeFragment (parent)
				.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						Date date = getDate();
						sendResult(Activity.RESULT_OK, date);
					}
				})
				.create();
	}

	private void sendResult(int resultCode, Date date) {
		if (getTargetFragment() == null) {
			return;
		}

		Intent intent = new Intent();
		intent.putExtra(EXTRA_DATE, date);

		getTargetFragment().onActivityResult(getTargetRequestCode(), resultCode, intent);
	}

}