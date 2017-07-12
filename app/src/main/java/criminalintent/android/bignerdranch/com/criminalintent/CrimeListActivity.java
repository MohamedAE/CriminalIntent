package criminalintent.android.bignerdranch.com.criminalintent;

import android.content.Intent;
import android.support.v4.app.Fragment;

public class CrimeListActivity extends SingleFragmentActivity implements CrimeListFragment.Callbacks, CrimeFragment.Callbacks {

	@Override
	public Fragment createFragment() {
		return new CrimeListFragment();
	}

	@Override
	protected int getLayoutResId() {
		return R.layout.activity_masterdetail;
	}

	@Override
	public void onCrimeSelected(Crime crime) {
		/*
		If non-tablet interface, start new CrimeFragment view
		*/
		if (findViewById(R.id.detail_fragment_container) == null) {
			Intent intent = CrimePagerActivity.newIntent(this, crime.getId());
			startActivity(intent);

		/*
		If tablet interface, remove current CrimeFragment (if exists),
		add desired CrimeFragment in its place
		*/
		} else {
			Fragment newDetail = CrimeFragment.newInstance(crime.getId());

			getSupportFragmentManager().beginTransaction()
					.replace(R.id.detail_fragment_container, newDetail)
					.commit();
		}
	}

	//Reload list
	public void onCrimeUpdated(Crime crime) {
		CrimeListFragment listFragment = (CrimeListFragment)
				getSupportFragmentManager()
				.findFragmentById(R.id.fragment_container);
		listFragment.updateUI();
	}

}