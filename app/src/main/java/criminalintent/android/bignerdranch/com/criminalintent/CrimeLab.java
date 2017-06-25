package criminalintent.android.bignerdranch.com.criminalintent;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import criminalintent.android.bignerdranch.com.criminalintent.database.CrimeBaseHelper;
import criminalintent.android.bignerdranch.com.criminalintent.database.CrimeDbSchema;
import criminalintent.android.bignerdranch.com.criminalintent.database.CrimeDbSchema.CrimeTable;

public class CrimeLab {

	private static CrimeLab sCrimeLab;

	private Context mContext;
	private SQLiteDatabase mDatabase;

	private CrimeLab(Context context) {
		mContext = context.getApplicationContext();
		//Initialize/upgrade Database
		mDatabase = new CrimeBaseHelper(mContext).getWritableDatabase();
	}

	public static CrimeLab get(Context context) {
		if (sCrimeLab == null) {
			sCrimeLab = new CrimeLab(context);
		}
		return sCrimeLab;
	}

	public void addCrime(Crime c) {
		ContentValues values = getContentValues(c);
		//.insert(target table, column hack, ContentValues)
		mDatabase.insert(CrimeTable.NAME, null, values);
	}

	public void deleteCrime(Crime c) {
		String uuidString = c.getId().toString();

		mDatabase.delete(
				CrimeTable.NAME,
				CrimeTable.Cols.UUID + " = ?",
				new String[] { uuidString }
		);
	}

	public List<Crime> getCrimes() {
		List<Crime> crimes = new ArrayList<>();

		CrimeCursorWrapper cursor = queryCrimes(null, null);
		//Iterate through table rows; add each to arraylist; return arraylist
		try {
			cursor.moveToFirst();
			while (!cursor.isAfterLast()) {
				crimes.add(cursor.getCrime());
				cursor.moveToNext();
			}
		} finally {
			cursor.close();
		}

		return crimes;
	}

	public Crime getCrime(UUID id) {
		//Query for specific crime by ID; point cursor at entry
		CrimeCursorWrapper cursor = queryCrimes(
				CrimeTable.Cols.UUID + " = ?",
				new String[] { id.toString() }
		);

		try {
			if (cursor.getCount() == 0) {
				return  null;
			}

			cursor.moveToFirst();
			return cursor.getCrime();
		} finally {
			cursor.close();
		}
	}

	public void updateCrime(Crime crime) {
		String uuidString = crime.getId().toString();
		ContentValues values = getContentValues(crime);

		/*
		.update(target table,
		ContentValues,
		where clause,
		arguments of where clause)
		*/
		mDatabase.update(CrimeTable.NAME,
				values,
				CrimeTable.Cols.UUID + " = ?",
				new String[] {uuidString});
	}

	private static ContentValues getContentValues(Crime crime) {
		ContentValues values = new ContentValues();
		//.put(column names, string)
		values.put(CrimeTable.Cols.UUID, crime.getId().toString());
		values.put(CrimeTable.Cols.TITLE, crime.getTitle());
		values.put(CrimeTable.Cols.DATE, crime.getDate().getTime());
		values.put(CrimeTable.Cols.SOLVED, crime.isSolved() ? 1 : 0);

		return values;
	}

	private CrimeCursorWrapper queryCrimes(String whereClause, String[] whereArgs) {
		Cursor cursor = mDatabase.query(
				CrimeTable.NAME,
				null,			//Columns (null - select all columns)
				whereClause,
				whereArgs,
				null,			//groupBy
				null,			//having
				null			//orderBy
		);

		return new CrimeCursorWrapper(cursor);
	}

}