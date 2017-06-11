package criminalintent.android.bignerdranch.com.criminalintent;

import java.util.Date;
import java.util.UUID;

public class Crime {

	//Crime ID
	private UUID mId;
	//Crime title
	private String mTitle;
	//Date
	private Date mDate;
	//Time
	private boolean mSolved;

	public Crime() {
		mId = UUID.randomUUID();
		mDate = new Date();
	}

	public UUID getId() {
		return mId;
	}

	public String getTitle() {
		return mTitle;
	}

	public void setTitle(String title) {
		mTitle = title;
	}

	public Date getmDate() {
		return mDate;
	}

	public void setmDate(Date mDate) {
		this.mDate = mDate;
	}

	public boolean ismSolved() {
		return mSolved;
	}

	public void setmSolved(boolean mSolved) {
		this.mSolved = mSolved;
	}

}