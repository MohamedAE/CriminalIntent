package criminalintent.android.bignerdranch.com.criminalintent;

import android.os.Bundle;
import android.support.v4.app.Fragment;

public class CrimeFragment extends Fragment {

	private Crime mCrime;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mCrime = new Crime();
	}

}