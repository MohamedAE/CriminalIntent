package criminalintent.android.bignerdranch.com.criminalintent.database;

public class CrimeDbSchema {

	public static final class CrimeTable {
		//Database name
		public static final String NAME = "crimes";

		//Database entry columns
		public static final class Cols {
			public static final String UUID = "uuid";
			public static final String TITLE = "title";
			public static final String DATE = "date";
			public static final String SOLVED = "solved";
		}
	}

}