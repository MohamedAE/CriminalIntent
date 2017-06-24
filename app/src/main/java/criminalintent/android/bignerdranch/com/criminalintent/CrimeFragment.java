package criminalintent.android.bignerdranch.com.criminalintent;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

public class CrimeFragment extends Fragment {

	private static final String ARG_CRIME_ID = "crime_id";
	private static final String DIALOG_DATE = "DialogDate";
	private static final String DIALOG_TIME = "DialogTime";

	//Request codes for target fragment (DatePickerFragment)
	private static final int REQUEST_DATE = 0;
	private static final int REQUEST_TIME = 1;

	private Crime mCrime;
	private EditText mTitleField;
	private Button mDateButton;
	private Button mTimeButton;
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

		setHasOptionsMenu(true);

		UUID crimeId = (UUID) getArguments().getSerializable(ARG_CRIME_ID);

		mCrime = CrimeLab.get(getActivity()).getCrime(crimeId);
	}

	//Inflate menu instance with items defined in the file
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);
		inflater.inflate(R.menu.fragment_crime, menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.menu_item_delete_crime:
				CrimeLab.get(getActivity()).deleteCrime(mCrime);
				Toast.makeText(getActivity(), R.string.crime_deleted, Toast.LENGTH_SHORT).show();
				getActivity().finish();
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
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
		updateDate();

		//Generate and display DatePickerFragment; pass to it model object's current date
		mDateButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				FragmentManager manager = getFragmentManager();
				DatePickerFragment dateDialog = DatePickerFragment.newInstance(mCrime.getDate());
				//Set target fragment (DatePickerFragment)
				dateDialog.setTargetFragment(CrimeFragment.this, REQUEST_DATE);
				dateDialog.show(manager, DIALOG_DATE);
			}
		});

		mTimeButton = (Button) v.findViewById(R.id.crime_time);
		updateTime();

		//Generate and display DatePickerFragment; pass to it model object's current date
		mTimeButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				FragmentManager manager = getFragmentManager();
				TimePickerFragment timeDialog = TimePickerFragment.newInstance(mCrime.getDate());
				//Set target fragment (TimePickerFragment)
				timeDialog.setTargetFragment(CrimeFragment.this, REQUEST_TIME);
				timeDialog.show(manager, DIALOG_TIME);
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

	//Update Button text
	private void updateDate() {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEEE, MMM F, yyyy");
		mDateButton.setText(simpleDateFormat.format(mCrime.getDate()));
	}

	//Update Button text
	private void updateTime() {
		SimpleDateFormat simpleTimeFormat = new SimpleDateFormat("HH : mm");
		mTimeButton.setText(simpleTimeFormat.format(mCrime.getDate()));
	}

	//Method to respond to Intent received from target Fragment (DatePickerFragment)
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode != Activity.RESULT_OK) {
			return;
		}

		//Extract Date object from Intent
		Date date = (Date) data.getSerializableExtra(DatePickerFragment.EXTRA_DATE);
		mCrime.setDate(date);

		switch (requestCode) {
			case REQUEST_DATE:
				updateDate();
				break;
			case REQUEST_TIME:
				updateTime();
				break;
		}
	}

}