package criminalintent.android.bignerdranch.com.criminalintent;

import java.util.UUID;

public class Crime {

	//Crime ID
	private UUID mId;
	//Crime title
	private String mTitle;

	public Crime() {
		mId = UUID.randomUUID();
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

}