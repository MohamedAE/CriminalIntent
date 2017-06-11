package criminalintent.android.bignerdranch.com.criminalintent;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;

public abstract class SingleFragmentActivity extends FragmentActivity {

	protected abstract Fragment createFragment();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_fragment);

		//Instantiate new FragmentManager; explicit calls made from this object
		FragmentManager fm = getSupportFragmentManager();
		//Retrieve CrimeFragment from the FragmentManager
		Fragment fragment = fm.findFragmentById(R.id.fragment_container);

		if (fragment == null) {
			//Create new fragment; include add operation (add fragment to activity); commit
			fragment = createFragment();
			fm.beginTransaction()
					.add(R.id.fragment_container, fragment)
					.commit();
		}
	}

}