package criminalintent.android.bignerdranch.com.criminalintent;

import android.database.Cursor;
import android.database.CursorWrapper;

import java.util.Date;
import java.util.UUID;

import criminalintent.android.bignerdranch.com.criminalintent.database.CrimeDbSchema.CrimeTable;

/* Thin wrapper to Cursor */
public class CrimeCursorWrapper extends CursorWrapper {

	public CrimeCursorWrapper(Cursor cursor) {
		super(cursor);
	}

	//Pulls column data from database; returns crime object
	public Crime getCrime() {
		//Save elements of database entry
		String uuidString = getString(getColumnIndex(CrimeTable.Cols.UUID));
		String title = getString(getColumnIndex(CrimeTable.Cols.TITLE));
		long date = getLong(getColumnIndex(CrimeTable.Cols.DATE));
		int isSolved = getInt(getColumnIndex(CrimeTable.Cols.SOLVED));
		String suspect = getString(getColumnIndex(CrimeTable.Cols.SUSPECT));

		//Instantiate crime object
		Crime crime = new Crime(UUID.fromString(uuidString));
		crime.setTitle(title);
		crime.setDate(new Date(date));
		crime.setSolved(isSolved != 0);
		crime.setSuspect(suspect);

		return crime;
	}

}