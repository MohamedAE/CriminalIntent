package criminalintent.android.bignerdranch.com.criminalintent;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;

public class CrimeActivity extends FragmentActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_crime);

		//Instantiate new FragmentManager; explicit calls made from here
		FragmentManager fm = getSupportFragmentManager();
		//Retrieve CrimeFragment from the FragmentManager
		Fragment fragment = fm.findFragmentById(R.id.fragment_container);

		if (fragment == null) {
			//Create new fragment; include add operation (add fragment to activity); commit
			fragment = new CrimeFragment();
			fm.beginTransaction().add(R.id.fragment_container, fragment).commit();
			//ermahgerd
		}
	}

}