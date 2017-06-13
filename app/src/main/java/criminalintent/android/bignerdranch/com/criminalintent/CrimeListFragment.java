package criminalintent.android.bignerdranch.com.criminalintent;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.List;

public class CrimeListFragment extends Fragment {

	private RecyclerView mCrimeRecyclerView;
	private CrimeAdapter mAdapter;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		//Inflate list fragment
		View view = inflater.inflate(R.layout.fragment_crime_list, container, false);

		//Instantiate RecyclerView
		mCrimeRecyclerView = (RecyclerView) view.findViewById(R.id.crime_recycler_view);
		//Give it a LayoutManager
		mCrimeRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

		updateUI();

		return view;
	}

	private void updateUI() {
		CrimeLab crimeLab = CrimeLab.get(getActivity());
		List<Crime> crimes = crimeLab.getCrimes();

		mAdapter = new CrimeAdapter(crimes);
		mCrimeRecyclerView.setAdapter(mAdapter);
	}

	//ViewHolder
	private class CrimeHolder extends RecyclerView.ViewHolder {
		public TextView mTitleTextView;

		public CrimeHolder(View itemView) {
			super(itemView);

			mTitleTextView = (TextView) itemView;
			mTitleTextView.setText("lorem ipsum");
		}
	}
	/*
	Adapter
	 */
	private class CrimeAdapter extends RecyclerView.Adapter<CrimeHolder> {
		private List<Crime> mCrimes;

		public CrimeAdapter(List<Crime> crimes) {
			mCrimes = crimes;
		}

		/*
		Create a new View; wrap it in a ViewHolder
		simple_list_item_1 - Android standard lib for generic list item
		*/
		@Override
		public CrimeHolder onCreateViewHolder(ViewGroup parent, int viewType) {
			//Create View; wrap it in ViewHolder
			LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
			View view = layoutInflater.inflate(android.R.layout.simple_list_item_1, parent, false);
			return new CrimeHolder(view);
		}

		/*
		Binds ViewHolder's View to model object
		@params - ViewHolder; index of model in collection
		 */
		@Override
		public void onBindViewHolder(CrimeHolder holder, int position) {
			Crime crime = mCrimes.get(position);
			holder.mTitleTextView.setText(crime.getTitle());
		}

		@Override
		public int getItemCount() {
			return mCrimes.size();
		}
	}

}