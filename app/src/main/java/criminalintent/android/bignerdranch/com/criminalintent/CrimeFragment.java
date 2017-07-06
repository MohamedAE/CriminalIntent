package criminalintent.android.bignerdranch.com.criminalintent;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.ShareCompat;
import android.text.format.DateFormat;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

public class CrimeFragment extends Fragment {

	private static final String ARG_CRIME_ID = "crime_id";
	private static final String DIALOG_DATE = "DialogDate";
	private static final String DIALOG_TIME = "DialogTime";
	private static final String DIALOG_SUSPECT = "DialogSuspect";

	//Request codes for target fragment (DatePickerFragment)
	private static final int REQUEST_DATE = 0;
	private static final int REQUEST_TIME = 1;
	private static final int REQUEST_CONTACT = 2;
	private static final int REQUEST_PHOTO = 3;

	private Crime mCrime;
	private EditText mTitleField;
	private Button mDateButton;
	private Button mTimeButton;
	private CheckBox mSolvedCheckBox;
	private Button mReportButton;
	private Button mSuspectButton;
	private ImageButton mPhotoButton;
	private ImageView mPhotoView;
	private Point mPhotoViewSize;

	private File mPhotoFile;

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

		mPhotoFile = CrimeLab.get(getActivity()).getPhotoFile(mCrime);
	}

	@Override
	public void onPause() {
		super.onPause();
		//On pause, update this crime in the database
		CrimeLab.get(getActivity()).updateCrime(mCrime);
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
	Called by host Activity (CrimeListFragment)
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

		mReportButton = (Button) v.findViewById(R.id.crime_report);
		mReportButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				/*
				//Define action of implicit intent
				Intent i = new Intent(Intent.ACTION_SEND);
				//Define data type
				i.setType("text/plain");
				//Package crime report as an intent extra
				i.putExtra(Intent.EXTRA_TEXT, getCrimeReport());
				i.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.crime_report_suspect));
				startActivity(i);
				*/
				ShareCompat.IntentBuilder.from(getActivity())
						.setType("text/plain")
						.setText(getCrimeReport())
						.setSubject(getString(R.string.crime_report_suspect))
						.setChooserTitle(getString(R.string.send_report))
						.startChooser();
			}
		});

		//Implicit intent asking for a contact
		final Intent pickContact = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
		mSuspectButton = (Button) v.findViewById(R.id.crime_suspect);
		mSuspectButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				//Start contact picker
				startActivityForResult(pickContact, REQUEST_CONTACT);
			}
		});
		//Display suspect on button
		if (mCrime.getSuspect() != null) {
			mSuspectButton.setText(mCrime.getSuspect());
		}

		//Check which part of the OS called PackageManager
		//Protects against not having a contacts app
		PackageManager packageManager = getActivity().getPackageManager();
		//.resolveActivity(intent) - determines how best handle the given intent
		//If no valid app exists, the Suspect button is disabled on creation
		if (packageManager.resolveActivity(pickContact, PackageManager.MATCH_DEFAULT_ONLY) == null) {
			mSuspectButton.setEnabled(false);
		}

		/* OPEN CAMERA BUTTON */
		mPhotoButton = (ImageButton) v.findViewById(R.id.crime_camera);

		//Implicit intent asking for permission to capture image
		final Intent captureImage = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

		//Determine if device has valid camera app
		boolean canTakePhoto = mPhotoFile != null && captureImage.resolveActivity(packageManager) != null;
		//If true, enable button
		mPhotoButton.setEnabled(canTakePhoto);

		//If true, put image in intent
		if (canTakePhoto) {
			Uri uri = Uri.fromFile(mPhotoFile);
			captureImage.putExtra(MediaStore.EXTRA_OUTPUT, uri);
		}

		mPhotoButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				startActivityForResult(captureImage, REQUEST_PHOTO);
			}
		});

		/*
		mPhotoView = (ImageView) v.findViewById(R.id.crime_photo);
		mPhotoView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (mPhotoFile != null && mPhotoFile.exists()) {
					FragmentManager fm = getFragmentManager();
					PhotoPreviewFragment.newInstance(mPhotoFile).show(fm, DIALOG_SUSPECT);
				}
			}
		});
		updatePhotoView();
		*/

		mPhotoView = (ImageView) v.findViewById(R.id.crime_photo);
		mPhotoView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
			@Override
			public void onGlobalLayout() {
				boolean isFirstPass = (mPhotoViewSize == null);
				mPhotoViewSize = new Point();
				mPhotoViewSize.set(mPhotoView.getWidth(), mPhotoView.getHeight());

				if (isFirstPass) {
					updatePhotoView();
				}
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

	//Construct and return a complete crime report
	private String getCrimeReport() {
		//Indicate crime solved status
		String solvedString = null;
		if (mCrime.isSolved()) {
			solvedString = getString(R.string.crime_report_solved);
		} else {
			solvedString = getString(R.string.crime_report_unsolved);
		}

		//Format date of crime
		String dateFormat = "EEE, MMM dd";
		String dateString = DateFormat.format(dateFormat, mCrime.getDate()).toString();

		//Format suspect
		String suspect = mCrime.getSuspect();
		if (suspect == null) {
			suspect = getString(R.string.crime_report_no_suspect);
		} else {
			suspect = getString(R.string.crime_report_suspect);
		}

		//Assemble elements of report
		String report = getString(R.string.crime_report, mCrime.getTitle(), dateString, solvedString, suspect);

		return report;
	}

	private void updatePhotoView() {
		/*
		//If photo not found; blank
		if (mPhotoFile == null || !mPhotoFile.exists()) {
			mPhotoView.setImageDrawable(null);
		} else {
			Bitmap bitmap = PictureUtils.getScaledBitmap(mPhotoFile.getPath(), getActivity());
			mPhotoView.setImageBitmap(bitmap);
		}
		*/
		if (mPhotoFile == null || !mPhotoFile.exists()) {
			mPhotoView.setImageDrawable(null);
			return;
		}

		Bitmap bitmap;
		if (mPhotoViewSize == null) {
			bitmap = PictureUtils.getScaledBitmap(mPhotoFile.getPath(), getActivity());
		} else {
			bitmap = PictureUtils.getScaledBitmap(mPhotoFile.getPath(), mPhotoViewSize.x, mPhotoViewSize.y);
		}

		mPhotoView.setImageBitmap(bitmap);
	}

	//Method to respond to Intent received from target Fragment (DatePickerFragment)
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode != Activity.RESULT_OK) {
			return;
		}

		//Extract Date object from Intent
		//Date date = (Date) data.getSerializableExtra(DatePickerFragment.EXTRA_DATE);
		//mCrime.setDate(date);

		if (requestCode == REQUEST_DATE) {
			Date date = (Date) data.getSerializableExtra(DatePickerFragment.EXTRA_DATE);
			mCrime.setDate(date);
			updateDate();
		} else if (requestCode == REQUEST_TIME) {
			updateTime();
		} else if (requestCode == REQUEST_CONTACT && data == null) {
			Uri contactUri = data.getData();

			//Specify which fields query should return values for
			String[] queryFields = new String[] {
					//ContactsContract - access to contacts database
					ContactsContract.Contacts.DISPLAY_NAME
			};

			//Perform query - contactUri is like a where clause
			Cursor c = getActivity().getContentResolver().query(contactUri, queryFields, null, null, null);

			try {
				//Double-check that results were returned
				if (c.getCount() == 0) {
					return;
				}

				//Pull out the first column of the first row of data - suspect's name
				c.moveToFirst();
				String suspect = c.getString(0);
				mCrime.setSuspect(suspect);
				mSuspectButton.setText(suspect);
			} finally {
				c.close();
			}
		} else if (requestCode == REQUEST_PHOTO) {
			updatePhotoView();
		}
	}

}