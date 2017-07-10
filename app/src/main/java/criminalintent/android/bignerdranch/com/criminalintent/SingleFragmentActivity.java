package criminalintent.android.bignerdranch.com.criminalintent;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

public abstract class SingleFragmentActivity extends AppCompatActivity {

	protected abstract Fragment createFragment();

    /*
    Function to allow subclasses to override and return a layout other than the
    default activity_fragment
    */
    @LayoutRes
    protected int getLayoutResId() {
        return R.layout.activity_fragment;
    }

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        setContentView(getLayoutResId());

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