package criminalintent.android.bignerdranch.com.criminalintent;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
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
import java.util.Date;
import java.util.UUID;

public class CrimeFragment extends Fragment {

	private static final String ARG_CRIME_ID = "crime_id";
	private static final String DIALOG_DATE = "DialogDate";

	//Request code for target fragment (DatePickerFragment)
	private static final int REQUEST_DATE = 0;

	private Crime mCrime;
	private EditText mTitleField;
	private Button mDateButton;
	private CheckBox mSolvedCheckBox;

	/*
	Accepts UUID
	Creates a Bundle and a CrimeFragment
	Attaches the Bundle to the CrimeFragment
	Returns Fragment
	*/
	public static CrimeFragment newInstance(UUID crimeId) {
		Bundle args = new Bundle();
		args.putSerializable(ARG_CRIME_ID, crimeId);

		CrimeFragment fragment = new CrimeFragment();
		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		UUID crimeId = (UUID) getArguments().getSerializable(ARG_CRIME_ID);

		mCrime = CrimeLab.get(getActivity()).getCrime(crimeId);
	}

	/*
	Called by host Activity
	Explicitly inflates fragment view; returns fragment view to host Activity
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_crime, container, false);

		//Instantiate EditText; connect listener
		mTitleField = (EditText) v.findViewById(R.id.crime_title);
		mTitleField.setText(mCrime.getTitle());
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
		mDateButton.setText(simpleDateFormat.format(mCrime.getDate()));

		//Generate and display DatePickerFragment; pass to it model object's current date
		mDateButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				FragmentManager manager = getFragmentManager();
				DatePickerFragment dialog = DatePickerFragment.newInstance(mCrime.getDate());
				//Set target fragment (DatePickerFragment)
				dialog.setTargetFragment(CrimeFragment.this, REQUEST_DATE);
				dialog.show(manager, DIALOG_DATE);
			}
		});

		mSolvedCheckBox = (CheckBox) v.findViewById(R.id.crime_solved);
		mSolvedCheckBox.setChecked(mCrime.isSolved());
		mSolvedCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				//Set the crime's solved property
				mCrime.setSolved(isChecked);
			}
		});

		return v;
	}

	//Method to respond to Intent received from target Fragment (DatePickerFragment)
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode != Activity.RESULT_OK) {
			return;
		}

		//Retrieve Date info from Intent; update
		if (requestCode == REQUEST_DATE) {
			Date date = (Date) data.getSerializableExtra(DatePickerFragment.EXTRA_DATE);
			mCrime.setDate(date);
			mDateButton.setText(mCrime.getDate().toString());
		}
	}

}