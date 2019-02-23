package Todo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class TodoDB {
    private static final String TAG = "TodoDB";

    //variables for each column
    public static final String KEY_ROWID = "_id";
    public static final String KEY_TITLE = "_title";
    public static final String KEY_DESCR = "_descr";
    //higher priority items are shown first in the list
    public static final String KEY_PRIORITY = "_priority";

    //variables for database
    private final String DATABASE_NAME = "TodoDB";
    private final String DATABASE_TABLE = "TodoTable";
    private final int DATABASE_VERSION = 1;

    private TodoDB.DatabaseHelper ourHelper;
    private final Context ourContext;
    private SQLiteDatabase ourDB;

    public TodoDB(Context context){
        ourContext = context;
    }

    private class DatabaseHelper extends SQLiteOpenHelper {
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
                    KEY_DESCR + " TEXT, " + KEY_PRIORITY + " INTEGER NOT NULL);";
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

    public TodoDB open() throws SQLException {
        ourHelper = new TodoDB.DatabaseHelper(ourContext);
        ourDB = ourHelper.getWritableDatabase();
        return this;
    }

    public void close(){
        ourHelper.close();
    }

    public Todo getTodo(int id){
        String[] colms = new String[]{KEY_ROWID, KEY_TITLE, KEY_DESCR, KEY_PRIORITY};
        Cursor c = ourDB.query(DATABASE_TABLE, colms, KEY_ROWID + "=?",
                new String[]{String.valueOf(id)}, null, null, null);
        //if id exists, then move to first colmn of row
        if (c != null){
            c.moveToFirst();
        }
        // index of each column (same for every row)
        int iRowID = c.getColumnIndex(KEY_ROWID);
        int iTitle = c.getColumnIndex(KEY_TITLE);
        int iDescr = c.getColumnIndex(KEY_DESCR);
        int iPriority = c.getColumnIndex(KEY_PRIORITY);


        // creates new note using the indices above (the row depends on the id number)
        Todo todo = new Todo(Integer.parseInt(c.getString(iRowID)), c.getString(iTitle),
                c.getString(iDescr), c.getInt(iPriority));
        c.close();
        return todo;
    }

    public ArrayList<Todo> getTodoBasedOnPriority(int priority){
        String[] colms = new String[]{KEY_ROWID, KEY_TITLE, KEY_DESCR, KEY_PRIORITY};
        Cursor c = ourDB.query(DATABASE_TABLE, colms, KEY_PRIORITY + "=?",
                new String[]{String.valueOf(priority)}, null, null, null);
        ArrayList<Todo> todos = new ArrayList<>();
        // index of each column (same for every row)
        int iRowID = c.getColumnIndex(KEY_ROWID);
        int iTitle = c.getColumnIndex(KEY_TITLE);
        int iDescr = c.getColumnIndex(KEY_DESCR);
        int iPriority = c.getColumnIndex(KEY_PRIORITY);

        // iterates through each row and adds the note from each row to an arraylist of notes
        for(c.moveToFirst(); !c.isAfterLast(); c.moveToNext()){
            Todo todo = new Todo();
            todo.setID(Integer.parseInt(c.getString(iRowID)));
            todo.setTitle(c.getString(iTitle));
            todo.setDescr(c.getString(iDescr));
            todo.setPriority(Integer.parseInt(c.getString(iPriority)));
            todos.add(todo);
        }
        c.close();
        return todos;
    }

    /*
    adds a new todo to the database
     */
    public long addTodo(Todo todo){
        ContentValues cv = new ContentValues();
        cv.put(KEY_DESCR, todo.getDescr());
        cv.put(KEY_TITLE, todo.getTitle());
        cv.put(KEY_PRIORITY, todo.getPriority());

        return ourDB.insert(DATABASE_TABLE, null, cv);
    }

    /*
    Deletes todo when user marks complete
     */
    public long deleteTodo(int rowID){
        return ourDB.delete(DATABASE_TABLE, KEY_ROWID + "=?",
                new String[]{String.valueOf(rowID)});
    }
}
