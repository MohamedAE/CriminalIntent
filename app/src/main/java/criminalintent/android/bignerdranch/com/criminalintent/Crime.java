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
	//Suspect
	private String mSuspect;

	public Crime() {
		this(UUID.randomUUID());
	}

	public Crime(UUID id) {
		mId = id;
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

	public Date getDate() {
		return mDate;
	}

	public void setDate(Date mDate) {
		this.mDate = mDate;
	}

	public boolean isSolved() {
		return mSolved;
	}

	public void setSolved(boolean mSolved) {
		this.mSolved = mSolved;
	}

	public String getSuspect() {
		return mSuspect;
	}

	public void setSuspect(String suspect) {
		mSuspect = suspect;
	}

	public String getPhotoFilename() {
		return "IMG_" + getId().toString() + ".jpg";
	}

}