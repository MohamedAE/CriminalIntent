package criminalintent.android.bignerdranch.com.criminalintent;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;

import java.text.SimpleDateFormat;

public class CrimeFragment extends Fragment {

	private Crime mCrime;
	private EditText mTitleField;
	private Button mDateButton;
	private CheckBox mSolvedCheckBox;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mCrime = new Crime();
	}

	/*
	Called by host Activity
	Explicitly inflates fragment view; returns fragment view to host Activity (caller)
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_crime, container, false);

		//Instantiate EditText; connect listener
		mTitleField = (EditText) v.findViewById(R.id.crime_title);
		mTitleField.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {

			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				mCrime.setTitle(s.toString());
			}

			@Override
			public void afterTextChanged(Editable s) {

			}
		});

		mDateButton = (Button) v.findViewById(R.id.crime_date);
		//Display date on button
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEEE, MMM F, yyyy");
		mDateButton.setText(simpleDateFormat.format(mCrime.getmDate()));
		//Disable button
		mDateButton.setEnabled(false);

		mSolvedCheckBox = (CheckBox) v.findViewById(R.id.crime_solved);
		mSolvedCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				//Set the crime's solved property
				mCrime.setmSolved(isChecked);
			}
		});

		return v;
	}

}