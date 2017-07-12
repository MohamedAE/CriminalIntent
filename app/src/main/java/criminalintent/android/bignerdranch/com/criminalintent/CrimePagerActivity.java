package criminalintent.android.bignerdranch.com.criminalintent;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import java.util.List;
import java.util.UUID;

public class CrimePagerActivity extends AppCompatActivity implements CrimeFragment.Callbacks {

	private static final String EXTRA_CRIME_ID = "com.bignerdranch.android.criminalintent.crime_id";

	private ViewPager mViewPager;
	private List<Crime> mCrimes;

	public static Intent newIntent(Context packageContext, UUID crimeId) {
		Intent intent = new Intent(packageContext, CrimePagerActivity.class);
		intent.putExtra(EXTRA_CRIME_ID, crimeId);
		return intent;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_crime_pager);

		UUID crimeId = (UUID) getIntent().getSerializableExtra(EXTRA_CRIME_ID);

		//Find ViewPager view
		mViewPager = (ViewPager) findViewById(R.id.activity_crime_pager_view_pager);

		//Retrieve data set
		mCrimes = CrimeLab.get(this).getCrimes();

		//Create FragmentManager for this Activity
		FragmentManager fragmentManager = getSupportFragmentManager();

		//Set ViewPager's adapter to an instance of FragmentStatePagerAdapter
		mViewPager.setAdapter(new FragmentStatePagerAdapter(fragmentManager) {
			//Return the Fragment associated with given position
			@Override
			public Fragment getItem(int position) {
				Crime crime = mCrimes.get(position);
				return CrimeFragment.newInstance(crime.getId());
			}

			//Returns the number of items in data set
			@Override
			public int getCount() {
				return mCrimes.size();
			}
		});

		/*
		Keep ViewPager from always displaying the first item in the data set on click
		Iterate through data set searching for target object by ID
		 */
		for (int i = 0; i < mCrimes.size(); i++) {
			if (mCrimes.get(i).getId().equals(crimeId)) {
				mViewPager.setCurrentItem(i);
				break;
			}
		}
	}

	@Override
	public void onCrimeUpdated(Crime crime) {

	}

}