package criminalintent.android.bignerdranch.com.criminalintent;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.List;

public class CrimeListFragment extends Fragment {

	private RecyclerView mCrimeRecyclerView;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		//Inflate list fragment
		View view = inflater.inflate(R.layout.fragment_crime_list, container, false);

		//Instantiate RecyclerView
		mCrimeRecyclerView = (RecyclerView) view.findViewById(R.id.crime_recycler_view);
		//Give it a LayoutManager
		mCrimeRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

		return view;
	}

	/*
	ViewHolder
	 */
	private class CrimeHolder extends RecyclerView.ViewHolder {
		public TextView mTitleTextView;

		public CrimeHolder(View itemView) {
			super(itemView);

			mTitleTextView = (TextView) itemView;
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
	}

}