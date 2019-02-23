package Notes;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.ArrayList;

public class NotesDB {
    private static final String TAG = "NotesDB";

    //variables for each column
    public static final String KEY_ROWID = "_id";
    public static final String KEY_TITLE = "_title";
    public static final String KEY_NOTE = "_note";
    public static final String KEY_DATE = "_date";

    //variables for database
    private final String DATABASE_NAME = "NotesDB";
    private final String DATABASE_TABLE = "NotesTable";
    private final int DATABASE_VERSION = 1;

    private DatabaseHelper ourHelper;
    private final Context ourContext;
    private SQLiteDatabase ourDB;

    public NotesDB(Context context){
        ourContext = context;
    }

    private class DatabaseHelper extends SQLiteOpenHelper{
        /*
        Constructor that creates new database
         */
        public DatabaseHelper(Context context){
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        /*
        Creates a table with the specified columns (id title note)
         */
        @Override
        public void onCreate(SQLiteDatabase db) {
            String sqlCode = "CREATE TABLE " + DATABASE_TABLE + " (" + KEY_ROWID +
                    " INTEGER PRIMARY KEY AUTOINCREMENT, " + KEY_TITLE + " TEXT NOT NULL, " +
                    KEY_NOTE + " TEXT, " + KEY_DATE + " LONG NOT NULL);";
            db.execSQL(sqlCode);
        }

        /*
        If there is a new version of the database table, delete old version
         */
        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE);
            onCreate(db);
        }
    }

    public NotesDB open() throws SQLException{
        ourHelper = new DatabaseHelper(ourContext);
        ourDB = ourHelper.getWritableDatabase();
        return this;
    }

    public void close(){
        ourHelper.close();
    }

    /*
      gets a single note based on the given id
       */
    public Note getNote(int id){
        String[] colms = new String[]{KEY_ROWID, KEY_TITLE, KEY_NOTE, KEY_DATE};
        Cursor c = ourDB.query(DATABASE_TABLE, colms, KEY_ROWID + "=?",
                new String[]{String.valueOf(id)}, null, null, null);
        //if id exists, then move to first colmn of row
        if (c != null){
            c.moveToFirst();
        }
        // index of each column (same for every row)
        int iRowID = c.getColumnIndex(KEY_ROWID);
        int iTitle = c.getColumnIndex(KEY_TITLE);
        int iNote = c.getColumnIndex(KEY_NOTE);
        int iDate = c.getColumnIndex(KEY_DATE);


        // creates new note using the indices above (the row depends on the id number)
        Note note = new Note(Integer.parseInt(c.getString(iRowID)), c.getString(iTitle),
                c.getString(iNote), c.getLong(iDate));
        c.close();
        return note;
    }

    /*
    returns a list of all notes in database
     */
    public ArrayList<Note> getAllNotes(){
        String[] colms = new String[]{KEY_ROWID, KEY_TITLE, KEY_NOTE, KEY_DATE};
        Cursor c = ourDB.query(DATABASE_TABLE, colms, null, null, null,
                null, KEY_DATE + " DESC");
        ArrayList<Note> notes = new ArrayList<>();
        int iRowID = c.getColumnIndex(KEY_ROWID);
        int iTitle = c.getColumnIndex(KEY_TITLE);
        int iNote = c.getColumnIndex(KEY_NOTE);
        int iDate = c.getColumnIndex(KEY_DATE);

        //System.out.println("Beginning to get notes");
        // iterates through each row and adds the note from each row to an arraylist of notes
        for(c.moveToFirst(); !c.isAfterLast(); c.moveToNext()){
            Note note = new Note();
            note.setID(Integer.parseInt(c.getString(iRowID)));
            note.setTitle(c.getString(iTitle));
            note.setNote(c.getString(iNote));
            note.setDate(c.getLong(iDate));
            notes.add(note);
        }
        c.close();
        return notes;
    }

    /*
    adds a new note to the database
     */
    public long addNote(Note note){
        ContentValues cv = new ContentValues();
        cv.put(KEY_NOTE, note.getNote());
        cv.put(KEY_TITLE, note.getTitle());
        cv.put(KEY_DATE, note.getDate());

        return ourDB.insert(DATABASE_TABLE, null, cv);
    }

    /*
    Update note if user decides to edit it
     */
    public long updateEntry(int rowId, String title, String note, long date){
        ContentValues cv = new ContentValues();
        cv.put(KEY_TITLE, title);
        cv.put(KEY_NOTE, note);
        cv.put(KEY_DATE, date);

        return ourDB.update(DATABASE_TABLE, cv, KEY_ROWID + "=?",
                new String[]{String.valueOf(rowId)});
    }

    public long deleteEntry(int rowID){
        return ourDB.delete(DATABASE_TABLE, KEY_ROWID + "=?",
                new String[]{String.valueOf(rowID)});
    }
}
