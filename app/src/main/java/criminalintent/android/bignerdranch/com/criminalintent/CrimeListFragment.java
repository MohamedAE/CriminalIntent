package criminalintent.android.bignerdranch.com.criminalintent;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

public class CrimeListFragment extends Fragment {

	private static final String SAVED_SUBTITLE_VISIBLE = "subtitle";

	private RecyclerView mCrimeRecyclerView;
	//Parent of TextView overlay (displayed when no crimes are available)
	private LinearLayout mLinearLayout;
	private CrimeAdapter mAdapter;
	private boolean mSubTitleVisible;
	private int mLastAdapterClickPosition = -1;
	private Callbacks mCallbacks;

	//Required interface for hosting activities
	public interface Callbacks {
		void onCrimeSelected(Crime crime);
	}

	//Called when fragment is attached to an activity
	@Override
	public void onAttach(Context context) {
		super.onAttach(context);
		mCallbacks = (Callbacks) context;
	}

	@Override
	public void onCreate(Bundle onSavedInstanceState) {
		super.onCreate(onSavedInstanceState);
		//Report to FragmentManager that this Fragment has a menu
		setHasOptionsMenu(true);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		//Inflate list fragment
		View view = inflater.inflate(R.layout.fragment_crime_list, container, false);

		//Instantiate RecyclerView
		mCrimeRecyclerView = (RecyclerView) view.findViewById(R.id.crime_recycler_view);
		//Give it a LayoutManager
		mCrimeRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

		//Maintain subtitle visibility
		if (savedInstanceState != null) {
			mSubTitleVisible = savedInstanceState.getBoolean(SAVED_SUBTITLE_VISIBLE);
		}

		mLinearLayout = (LinearLayout) view.findViewById(R.id.crime_list_linear_layout);

		updateUI();

		return view;
	}

	@Override
	public void onResume() {
		super.onResume();
		updateUI();
	}

	//Preserve subtitle visibility
	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putBoolean(SAVED_SUBTITLE_VISIBLE, mSubTitleVisible);
	}

	//On fragment detachment, set Callback variable to null
	@Override
	public void onDetach() {
		super.onDetach();
		mCallbacks = null;
	}

	//Inflate menu instance with items defined in the file
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);
		//Inflate menu
		inflater.inflate(R.menu.fragment_crime_list, menu);

		//Toggle subtitle label
		MenuItem subtitleItem = menu.findItem(R.id.menu_item_show_subtitle);
		if (mSubTitleVisible) {
			subtitleItem.setTitle(R.string.hide_subtitle);
		} else {
			subtitleItem.setTitle(R.string.show_subtitle);
		}
	}

	/*
	Receives an instance of MenuItem describing the user's menu selection
	Responds to selection
	*/
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			//Create new crime
			case R.id.menu_item_new_crime:
				//Create new Crime object
				Crime crime = new Crime();
				//Add crime to data set
				CrimeLab.get(getActivity()).addCrime(crime);
				//Update list view (since it remains visible on tablets)
				updateUI();
				mCallbacks.onCrimeSelected(crime);
				return true;
			case R.id.menu_item_show_subtitle:
				mSubTitleVisible = !mSubTitleVisible;
				//Recreate options menu
				getActivity().invalidateOptionsMenu();
				updateSubtitle();
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}

	/*
	Shows number of items on data set when Toolbar button is pressed
	*/
	private void updateSubtitle() {
		CrimeLab crimelab = CrimeLab.get(getActivity());
		int crimeCount = crimelab.getCrimes().size();
		String subtitle = getResources().getQuantityString(R.plurals.subtitle_plural, crimeCount, crimeCount);

		if (!mSubTitleVisible) {
			subtitle = null;
		}

		AppCompatActivity activity = (AppCompatActivity) getActivity();
		activity.getSupportActionBar().setSubtitle(subtitle);
	}

	//Update RecyclerView
	public void updateUI() {
		CrimeLab crimeLab = CrimeLab.get(getActivity());
		List<Crime> crimes = crimeLab.getCrimes();

		if (mAdapter == null) {
			mAdapter = new CrimeAdapter(crimes);
			mCrimeRecyclerView.setAdapter(mAdapter);
		} else {
			mAdapter.setCrimes(crimes);
			mAdapter.notifyDataSetChanged();
		}

		if (crimes.size() > 0) {
			mLinearLayout.setVisibility(View.GONE);
		} else {
			mLinearLayout.setVisibility(View.VISIBLE);
		}

		updateSubtitle();
	}

	/*
	ViewHolder
	Holds individual elements in RecyclerView list
	*/
	private class CrimeHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
		private Crime mCrime;
		private TextView mTitleTextView;
		private TextView mDateTextView;
		private CheckBox mSolvedCheckBox;

		public CrimeHolder(View itemView) {
			super(itemView);
			itemView.setOnClickListener(this);

			mTitleTextView = (TextView) itemView.findViewById(R.id.list_item_crime_title_text_view);
			mDateTextView = (TextView) itemView.findViewById(R.id.list_item_crime_date_text_view);
			mSolvedCheckBox = (CheckBox) itemView.findViewById(R.id.list_item_crime_solved_check_box);
		}

		@Override
		public void onClick(View v) {
			mLastAdapterClickPosition = getAdapterPosition();
			mCallbacks.onCrimeSelected(mCrime);
		}

		/*
		Binds CrimeHolder to given Crime
		Update View with Crime's data
		*/
		public void bindCrime(Crime crime) {
			mCrime = crime;
			mTitleTextView.setText(mCrime.getTitle());
			mDateTextView.setText(mCrime.getDate().toString());
			mSolvedCheckBox.setChecked(mCrime.isSolved());
		}
	}

	/*
	Adapter
	Communicates between CrimeAdapter and Crime object
	*/
	private class CrimeAdapter extends RecyclerView.Adapter<CrimeHolder> {
		private List<Crime> mCrimes;

		public CrimeAdapter(List<Crime> crimes) {
			mCrimes = crimes;
		}

		//Create a new View; wrap it in a ViewHolder
		@Override
		public CrimeHolder onCreateViewHolder(ViewGroup parent, int viewType) {
			//Create View; wrap it in ViewHolder
			LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
			View view = layoutInflater.inflate(R.layout.list_item_crime, parent, false);
			return new CrimeHolder(view);
		}

		/*
		Binds ViewHolder's View to model object
		@params - ViewHolder; index of model in collection
		*/
		@Override
		public void onBindViewHolder(CrimeHolder holder, int position) {
			Crime crime = mCrimes.get(position);
			holder.bindCrime(crime);
		}

		@Override
		public int getItemCount() {
			return mCrimes.size();
		}

		//Update field mCrimes with current database values
		public void setCrimes(List<Crime> crimes) {
			mCrimes = crimes;
		}
	}

}