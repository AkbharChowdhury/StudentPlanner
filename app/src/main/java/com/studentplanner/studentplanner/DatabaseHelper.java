package com.studentplanner.studentplanner;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.util.Log;

import com.studentplanner.studentplanner.models.Classes;
import com.studentplanner.studentplanner.models.Coursework;
import com.studentplanner.studentplanner.models.ImageHandler;
import com.studentplanner.studentplanner.models.Module;
import com.studentplanner.studentplanner.models.ModuleTeacher;
import com.studentplanner.studentplanner.models.Semester;
import com.studentplanner.studentplanner.models.Student;
import com.studentplanner.studentplanner.models.Teacher;
import com.studentplanner.studentplanner.tables.ClassTable;
import com.studentplanner.studentplanner.tables.CourseworkTable;
import com.studentplanner.studentplanner.tables.ModuleTable;
import com.studentplanner.studentplanner.tables.ModuleTeacherTable;
import com.studentplanner.studentplanner.tables.SemesterTable;
import com.studentplanner.studentplanner.tables.StudentTable;
import com.studentplanner.studentplanner.tables.TeacherTable;
import com.studentplanner.studentplanner.utils.AccountPreferences;
import com.studentplanner.studentplanner.utils.CalendarUtils;
import com.studentplanner.studentplanner.utils.Encryption;
import com.studentplanner.studentplanner.utils.Helper;

import java.text.MessageFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "StudentPlanner.db";
    private static final int DATABASE_VERSION = 1;

    private final SQLiteDatabase db;
    @SuppressLint("StaticFieldLeak")
    private static DatabaseHelper instance;
    private final Context context;
    private static final String ERROR_TAG = "ERROR";

    private String getErrorMessage(Exception e) {
        String methodName = Thread.currentThread().getStackTrace()[3].getMethodName();
        return MessageFormat.format("There was a problem in method: {0}\nError: \n{1}",methodName, e.getMessage());

    }

    public static synchronized DatabaseHelper getInstance(Context context) {
        if (instance == null) {
            instance = new DatabaseHelper(context);
        }
        return instance;
    }

    private DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
        // must be writable for queries to run from on create method
        db = getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        createAllTables(db);
        addDefaultTableValues(db);

    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + DATABASE_NAME);
        Log.v(getClass().getName(), MessageFormat.format("{0} database upgrade to version {1}  - old data lost", DATABASE_NAME, newVersion));
        onCreate(db);
    }

    @Override
    public void onConfigure(SQLiteDatabase db) {
        super.onConfigure(db);
        db.setForeignKeyConstraintsEnabled(true);
    }

    private void addDefaultTableValues(SQLiteDatabase db) {
        addStudent(db);
    }


    private void createAllTables(SQLiteDatabase db) {
        createStudentTable(db);

        createModuleTable(db);
        createCourseworkTable(db);

        createTeacherTable(db);
        createModuleTeacherTable(db);

        createSemesterTable(db);
        createClassesTable(db);

    }


    private void createStudentTable(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + StudentTable.TABLE_NAME + " ("
                + StudentTable.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + StudentTable.COLUMN_FIRSTNAME + " TEXT NOT NULL,"
                + StudentTable.COLUMN_LASTNAME + " TEXT NOT NULL,"
                + StudentTable.COLUMN_EMAIL + " TEXT NOT NULL UNIQUE,"
                + StudentTable.COLUMN_PHONE + " TEXT,"
                + StudentTable.COLUMN_PASSWORD + " TEXT NOT NULL,"
                + StudentTable.COLUMN_REGISTERED_DATE + " TEXT NOT NULL"
                + ");"
        );

    }

    private void createModuleTable(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + ModuleTable.TABLE_NAME + " ("
                + ModuleTable.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + ModuleTable.COLUMN_STUDENT_ID + " INTEGER NOT NULL,"
                + ModuleTable.COLUMN_MODULE_C0DE + " TEXT NOT NULL,"
                + ModuleTable.COLUMN_MODULE_NAME + " TEXT NOT NULL, "
                + "FOREIGN KEY (" + ModuleTable.COLUMN_STUDENT_ID + ") REFERENCES " + StudentTable.TABLE_NAME + "(" + StudentTable.COLUMN_ID + ") ON UPDATE CASCADE ON DELETE CASCADE "
                + ");"
        );

    }

    private void createCourseworkTable(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + CourseworkTable.TABLE_NAME + " ("
                + CourseworkTable.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + CourseworkTable.COLUMN_MODULE_ID + " INTEGER NOT NULL,"
                + CourseworkTable.COLUMN_TITLE + " TEXT NOT NULL,"
                + CourseworkTable.COLUMN_DESCRIPTION + " TEXT, "
                + CourseworkTable.COLUMN_PRIORITY + " TEXT NOT NULL, "
                + CourseworkTable.COLUMN_DEADLINE + " TEXT NOT NULL, "
                + CourseworkTable.COLUMN_DEADLINE_TIME + " TEXT NOT NULL, "
                + CourseworkTable.COLUMN_COMPLETED + " TEXT NOT NULL DEFAULT 'No', "
                + CourseworkTable.COLUMN_IMAGE + " BLOB, "
                + "FOREIGN KEY (" + CourseworkTable.COLUMN_MODULE_ID + ") REFERENCES " + ModuleTable.TABLE_NAME + "(" + ModuleTable.COLUMN_ID + ") ON UPDATE CASCADE ON DELETE CASCADE "
                + ");"
        );

    }

    private void createTeacherTable(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TeacherTable.TABLE_NAME + " ("
                + TeacherTable.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + TeacherTable.COLUMN_STUDENT_ID + " INTEGER NOT NULL,"
                + TeacherTable.COLUMN_FIRSTNAME + " TEXT NOT NULL,"
                + TeacherTable.COLUMN_LASTNAME + " TEXT NOT NULL,"
                + TeacherTable.COLUMN_EMAIL + " TEXT NOT NULL UNIQUE,"
                + "FOREIGN KEY (" + TeacherTable.COLUMN_STUDENT_ID + ") REFERENCES " + StudentTable.TABLE_NAME + "(" + StudentTable.COLUMN_ID + ") ON UPDATE CASCADE ON DELETE CASCADE"
                + ");"
        );

    }

    private void createModuleTeacherTable(SQLiteDatabase db) {

        db.execSQL("CREATE TABLE " + ModuleTeacherTable.TABLE_NAME + " ("
                + ModuleTeacherTable.COLUMN_TEACHER_ID + " INTEGER NOT NULL, "
                + ModuleTeacherTable.COLUMN_MODULE_ID + " INTEGER NOT NULL, "
                + "PRIMARY KEY(" + ModuleTeacherTable.COLUMN_TEACHER_ID + ", " + ModuleTeacherTable.COLUMN_MODULE_ID + "),"
                + "FOREIGN KEY (" + ModuleTeacherTable.COLUMN_TEACHER_ID + ") REFERENCES " + TeacherTable.TABLE_NAME + "(" + TeacherTable.COLUMN_ID + ") ON UPDATE CASCADE ON DELETE CASCADE,"
                + "FOREIGN KEY (" + ModuleTeacherTable.COLUMN_MODULE_ID + ") REFERENCES " + ModuleTable.TABLE_NAME + "(" + ModuleTable.COLUMN_ID + ") ON UPDATE CASCADE ON DELETE CASCADE"
                + ");"
        );

    }


    public void createSemesterTable(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + SemesterTable.TABLE_NAME + " ("
                + SemesterTable.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + SemesterTable.COLUMN_STUDENT_ID + " INTEGER NOT NULL,"
                + SemesterTable.COLUMN_NAME + " TEXT NOT NULL,"
                + SemesterTable.COLUMN_START_DATE + " TEXT NOT NULL, "
                + SemesterTable.COLUMN_END_DATE + " TEXT NOT NULL, "
                + "FOREIGN KEY (" + SemesterTable.COLUMN_STUDENT_ID + ") REFERENCES " + StudentTable.TABLE_NAME + "(" + StudentTable.COLUMN_ID + ") ON UPDATE CASCADE ON DELETE CASCADE "
                + ");"
        );

    }


    public void createClassesTable(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + ClassTable.TABLE_NAME + " ("
                + ClassTable.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + ClassTable.COLUMN_MODULE_ID + " INTEGER NOT NULL,"
                + ClassTable.COLUMN_SEMESTER_ID + " INTEGER NOT NULL,"
                + ClassTable.COLUMN_DOW + " INTEGER NOT NULL, "
                + ClassTable.COLUMN_START_TIME + " TEXT NOT NULL, "
                + ClassTable.COLUMN_END_TIME + " TEXT NOT NULL, "
                + ClassTable.COLUMN_ROOM + " TEXT, "
                + ClassTable.COLUMN_TYPE + " TEXT NOT NULL, "
                + "FOREIGN KEY (" + ClassTable.COLUMN_MODULE_ID + ") REFERENCES " + ModuleTable.TABLE_NAME + "(" + ModuleTable.COLUMN_ID + ") ON UPDATE CASCADE ON DELETE CASCADE, "
                + "FOREIGN KEY (" + ClassTable.COLUMN_SEMESTER_ID + ") REFERENCES " + SemesterTable.TABLE_NAME + "(" + SemesterTable.COLUMN_ID + ") ON UPDATE CASCADE ON DELETE CASCADE "
                + ")"
        );

    }


    public boolean columnExists(String fieldValue, String column, String table) {
        String[] columns = {column};
        SQLiteDatabase db = getReadableDatabase();
        String selection = column + " LIKE ?";
        String[] selectionArgs = {fieldValue};
        // select the email field and compare it to the entered email
        try (Cursor cursor = db.query(table, columns, selection, selectionArgs, null, null, null)) {
            int cursorCount = cursor.getCount();
            return cursorCount > 0;
        } catch (Exception e) {
            Log.d(ERROR_TAG, getErrorMessage(e));
            Log.d(ERROR_TAG, getErrorMessage(e));
            return false;
        }

    }

    @SuppressLint("Range")
    public boolean emailExists(String email, String excludedEmail) {
        int studentID = AccountPreferences.getStudentID(context);
        String[] columns = {TeacherTable.COLUMN_EMAIL, TeacherTable.COLUMN_STUDENT_ID};
        String emailColumn = TeacherTable.COLUMN_EMAIL;
        SQLiteDatabase db = getReadableDatabase();
        String selection = emailColumn + " LIKE ?"
                + " AND " + emailColumn
                + " NOT LIKE ?"
                + "AND " + ModuleTable.COLUMN_STUDENT_ID + "= ?";

        String[] selectionArgs = {
                email,
                excludedEmail,
                String.valueOf(studentID),

        };
        try (Cursor cursor = db.query(TeacherTable.TABLE_NAME, columns, selection, selectionArgs, null, null, null)) {

            return cursor.getCount() > 0;

        } catch (Exception e) {
            String s = Thread.currentThread().getStackTrace()[3].getMethodName();
            Log.d(ERROR_TAG, getErrorMessage(e));
            return false;

        }

    }


    @SuppressLint("Range")
    public boolean isAuthorised(String email, String password) {
        String[] columns = {StudentTable.COLUMN_ID};
        SQLiteDatabase db = getReadableDatabase();
        String selection = StudentTable.COLUMN_EMAIL + " = ?" + " AND " + StudentTable.COLUMN_PASSWORD + " = ?";
        String[] selectionArgs = {email, password};

        try (Cursor cursor = db.query(StudentTable.TABLE_NAME, columns, selection, selectionArgs, null, null, null)) {
            return cursor.getCount() > 0;
        } catch (Exception e) {
            Log.d(ERROR_TAG, getErrorMessage(e));
            return false;

        }

    }

    @SuppressLint("Range")
    public boolean moduleCodeExists(String moduleCode) {
        int studentID = AccountPreferences.getStudentID(context);
        String[] columns = {ModuleTable.COLUMN_MODULE_C0DE, ModuleTable.COLUMN_STUDENT_ID};
        SQLiteDatabase db = getReadableDatabase();
        // selection criteria
        String selection = ModuleTable.COLUMN_MODULE_C0DE + " LIKE ?" + " AND " + ModuleTable.COLUMN_STUDENT_ID + " = ?";
        String[] selectionArgs = {moduleCode, String.valueOf(studentID)};

        try (Cursor cursor = db.query(ModuleTable.TABLE_NAME, columns, selection, selectionArgs, null, null, null)) {

            return cursor.getCount() > 0;

        } catch (Exception e) {
            Log.d(ERROR_TAG, getErrorMessage(e));
            return false;

        }

    }


    @SuppressLint("Range")
    public boolean classExists(int moduleID, int semesterID, String type) {
        int studentID = AccountPreferences.getStudentID(context);
        SQLiteDatabase db = getReadableDatabase();

        try (Cursor cursor = db.rawQuery("""
                SELECT
                  COUNT(*) class_exists
                                                            
                FROM classes c
                JOIN modules m
                  ON m.module_id = c.module_id
                WHERE m.student_id = ?
                AND c.module_id = ?
                AND semester_id = ?
                AND type = ?
                """, new String[]{
                String.valueOf(studentID),
                String.valueOf(moduleID),
                String.valueOf(semesterID),
                type,


        })) {
            if (cursor.moveToFirst()) {
                return cursor.getInt(0) > 0;

            }

        } catch (Exception e) {
            Log.d(ERROR_TAG, getErrorMessage(e));
        }
        return false;

    }

    @SuppressLint("Range")
    public boolean moduleCodeExists(String moduleCode, String excludedModuleCode) {
        int studentID = AccountPreferences.getStudentID(context);
        String[] columns = {ModuleTable.COLUMN_MODULE_C0DE, ModuleTable.COLUMN_STUDENT_ID};
        SQLiteDatabase db = getReadableDatabase();
        String selection = ModuleTable.COLUMN_MODULE_C0DE + " LIKE ?"
                + " AND " + ModuleTable.COLUMN_MODULE_C0DE
                + " NOT LIKE ?"
                + "AND " + ModuleTable.COLUMN_STUDENT_ID + "= ?";

        String[] selectionArgs = {
                moduleCode,
                excludedModuleCode,
                String.valueOf(studentID),

        };
        try (Cursor cursor = db.query(ModuleTable.TABLE_NAME, columns, selection, selectionArgs, null, null, null)) {

            return cursor.getCount() > 0;

        } catch (Exception e) {
            Log.d(ERROR_TAG, getErrorMessage(e));
            return false;

        }

    }

    @SuppressLint("Range")
    public int getStudentID(String email) {
        int studentID = 0;
        String[] columns = {StudentTable.COLUMN_ID};
        SQLiteDatabase db = getReadableDatabase();
        String selection = StudentTable.COLUMN_EMAIL + " = ?";
        String[] selectionArgs = {email};
        try (Cursor cursor = db.query(StudentTable.TABLE_NAME, columns, selection, selectionArgs, null, null, null)) {
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                studentID = cursor.getInt(0);
            }

        } catch (Exception e) {
            Log.d(ERROR_TAG, getErrorMessage(e));
            return studentID;

        }
        return studentID;

    }

    @SuppressLint("Range")
    public String getStudentEmail(int studentID) {
        String email = "";
        String[] columns = {StudentTable.COLUMN_EMAIL};
        SQLiteDatabase db = getReadableDatabase();
        String selection = StudentTable.COLUMN_ID + " = ?";
        String[] selectionArgs = {String.valueOf(studentID)};

        try (Cursor cursor = db.query(StudentTable.TABLE_NAME, columns, selection, selectionArgs, null, null, null)) {
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                email = cursor.getString(0);
            }

        } catch (Exception e) {
            Log.d(ERROR_TAG, getErrorMessage(e));
            return email;

        }
        return email;

    }

    @SuppressLint("Range")
    public Student getUserFirstAndLastName(int userID) {
        String[] columns = {StudentTable.COLUMN_FIRSTNAME, StudentTable.COLUMN_LASTNAME};
        SQLiteDatabase db = getReadableDatabase();
        String selection = StudentTable.COLUMN_ID + " = ?";
        String[] selectionArgs = {String.valueOf(userID)};
        Student student = new Student();

        try (Cursor cursor = db.query(StudentTable.TABLE_NAME, columns, selection, selectionArgs, null, null, null)) {

            if (cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    student.setFirstname(cursor.getString(cursor.getColumnIndex(StudentTable.COLUMN_FIRSTNAME)));
                    student.setLastname(cursor.getString(cursor.getColumnIndex(StudentTable.COLUMN_LASTNAME)));
                }

            }


        } catch (Exception e) {
            Log.d(ERROR_TAG, getErrorMessage(e));
            return student;

        }
        return student;

    }

    public boolean registerStudent(Student student) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(StudentTable.COLUMN_FIRSTNAME, student.getFirstname());
        cv.put(StudentTable.COLUMN_LASTNAME, student.getLastname());
        cv.put(StudentTable.COLUMN_EMAIL, student.getEmail());
        if (student.getPhone() != null) {
            cv.put(StudentTable.COLUMN_PHONE, student.getPhone());
        }
        cv.put(StudentTable.COLUMN_PASSWORD, Encryption.encode(student.getPassword()));
        cv.put(StudentTable.COLUMN_REGISTERED_DATE, String.valueOf(CalendarUtils.getCurrentDate()));
        long result = db.insert(StudentTable.TABLE_NAME, null, cv);
        return result != -1;

    }

    public boolean addModule(Module module) {

        int studentID = AccountPreferences.getStudentID(context);

        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(ModuleTable.COLUMN_STUDENT_ID, studentID);
        cv.put(ModuleTable.COLUMN_MODULE_C0DE, module.getModuleCode());
        cv.put(ModuleTable.COLUMN_MODULE_NAME, module.getModuleName());
        long result = db.insert(ModuleTable.TABLE_NAME, null, cv);
        return result != -1;

    }

    public boolean addSemester(Semester semester) {

        int studentID = AccountPreferences.getStudentID(context);
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(SemesterTable.COLUMN_STUDENT_ID, studentID);
        cv.put(SemesterTable.COLUMN_NAME, semester.getName());
        cv.put(SemesterTable.COLUMN_START_DATE, semester.getStart().toString());
        cv.put(SemesterTable.COLUMN_END_DATE, semester.getEnd().toString());
        long result = db.insert(SemesterTable.TABLE_NAME, null, cv);
        return result != -1;

    }


    public boolean addCoursework(Coursework coursework) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(CourseworkTable.COLUMN_MODULE_ID, coursework.getModuleID());
        cv.put(CourseworkTable.COLUMN_TITLE, coursework.getTitle());
        cv.put(CourseworkTable.COLUMN_DESCRIPTION, coursework.getDescription());
        cv.put(CourseworkTable.COLUMN_PRIORITY, coursework.getPriority());
        cv.put(CourseworkTable.COLUMN_DEADLINE, coursework.getDeadline());
        cv.put(CourseworkTable.COLUMN_DEADLINE_TIME, coursework.getDeadlineTime());

        if (coursework.getImage() != null) {
            cv.put(CourseworkTable.COLUMN_IMAGE, ImageHandler.getBitmapAsByteArray(coursework.getImage()));

        }


        long result = db.insert(CourseworkTable.TABLE_NAME, null, cv);
        return result != -1;

    }

    public boolean addClass(Classes classes) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(ClassTable.COLUMN_MODULE_ID, classes.getModuleID());
        cv.put(ClassTable.COLUMN_SEMESTER_ID, classes.getSemesterID());
        cv.put(ClassTable.COLUMN_DOW, classes.getDow());
        cv.put(ClassTable.COLUMN_START_TIME, classes.getStartTime());
        cv.put(ClassTable.COLUMN_END_TIME, classes.getEndTime());
        cv.put(ClassTable.COLUMN_ROOM, classes.getRoom());
        cv.put(ClassTable.COLUMN_TYPE, classes.getClassType());


        long result = db.insert(ClassTable.TABLE_NAME, null, cv);
        return result != -1;

    }


    private void addStudent(SQLiteDatabase db) {
        ContentValues cv = new ContentValues();
        cv.put(StudentTable.COLUMN_FIRSTNAME, "tom");
        cv.put(StudentTable.COLUMN_LASTNAME, "Smith");
        cv.put(StudentTable.COLUMN_EMAIL, "tom@gmail.com");
        cv.put(StudentTable.COLUMN_PASSWORD, Encryption.encode("password"));
        cv.put(StudentTable.COLUMN_REGISTERED_DATE, String.valueOf(CalendarUtils.getCurrentDate()));
        db.insert(StudentTable.TABLE_NAME, null, cv);

    }

    public boolean addTeacher(Teacher teacher) {
        int studentID = AccountPreferences.getStudentID(context);

        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(TeacherTable.COLUMN_STUDENT_ID, studentID);
        cv.put(TeacherTable.COLUMN_FIRSTNAME, teacher.getFirstname());
        cv.put(TeacherTable.COLUMN_LASTNAME, teacher.getLastname());
        cv.put(TeacherTable.COLUMN_EMAIL, teacher.getEmail());

        long result = db.insert(TeacherTable.TABLE_NAME, null, cv);
        return result != -1;

    }

    public boolean addModuleTeacher(List<Integer> teacherIDs, int moduleID) {

        SQLiteDatabase db = getWritableDatabase();
        for (Integer teacherID : teacherIDs) {
            ContentValues cv = new ContentValues();
            cv.put(ModuleTeacherTable.COLUMN_TEACHER_ID, teacherID);
            cv.put(ModuleTeacherTable.COLUMN_MODULE_ID, moduleID);
            db.insert(ModuleTeacherTable.TABLE_NAME, null, cv);
        }
        return true;

    }

    public boolean updateModuleTeacher(List<Integer> teacherIDs, int moduleID) {
        deleteSelectedTeacherModules(moduleID);
        SQLiteDatabase db = getWritableDatabase();
        for (Integer teacherID : teacherIDs) {
            ContentValues cv = new ContentValues();
            cv.put(ModuleTeacherTable.COLUMN_TEACHER_ID, teacherID);
            cv.put(ModuleTeacherTable.COLUMN_MODULE_ID, moduleID);
            db.insert(ModuleTeacherTable.TABLE_NAME, null, cv);
        }
        return true;

    }


    @SuppressLint("Range")
    public List<Module> getModules() {
        List<Module> modules = new ArrayList<>();
        int studentID = AccountPreferences.getStudentID(context);
        SQLiteDatabase db = getReadableDatabase();
        String selection = ModuleTable.COLUMN_STUDENT_ID + " = ?";
        String[] selectionArgs = {String.valueOf(studentID)};
        try (Cursor cursor = db.query(ModuleTable.TABLE_NAME, null, selection, selectionArgs, null, null, null)) {
            if (!isCursorEmpty(cursor)) {
                while (cursor.moveToNext()) {
                    modules.add(new Module(
                            cursor.getInt(cursor.getColumnIndex(ModuleTable.COLUMN_ID)),
                            cursor.getString(cursor.getColumnIndex(ModuleTable.COLUMN_MODULE_C0DE)),
                            cursor.getString(cursor.getColumnIndex(ModuleTable.COLUMN_MODULE_NAME))
                    ));
                }
            }
        } catch (Exception e) {
            Log.d(ERROR_TAG, getErrorMessage(e));
        }
        return modules;
    }


    @SuppressLint("Range")
    public ArrayList<Integer> getModuleTeacherByModuleID(int moduleID) {
        ArrayList<Integer> teacherIds = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        String selection = ModuleTeacherTable.COLUMN_MODULE_ID + " = ?";
        String[] selectionArgs = {String.valueOf(moduleID)};
        try (Cursor cursor = db.query(ModuleTeacherTable.TABLE_NAME, null, selection, selectionArgs, null, null, null)) {
            if (!isCursorEmpty(cursor)) {
                while (cursor.moveToNext()) {
                    teacherIds.add(cursor.getInt(cursor.getColumnIndex(ModuleTeacherTable.COLUMN_TEACHER_ID)));
                }
            }
        } catch (Exception e) {
            Log.d(ERROR_TAG, getErrorMessage(e));
        }
        return teacherIds;
    }


    @SuppressLint("Range")
    public List<Teacher> getTeachers() {
        List<Teacher> teachers = new ArrayList<>();
        int studentID = AccountPreferences.getStudentID(context);
        SQLiteDatabase db = getReadableDatabase();
        String selection = TeacherTable.COLUMN_STUDENT_ID + " = ?";
        String[] selectionArgs = {String.valueOf(studentID)};
        try (Cursor cursor = db.query(TeacherTable.TABLE_NAME, null, selection, selectionArgs, null, null, null)) {
            if (!isCursorEmpty(cursor)) {
                while (cursor.moveToNext()) {
                    teachers.add(new Teacher(
                            cursor.getInt(cursor.getColumnIndex(TeacherTable.COLUMN_ID)),
                            cursor.getString(cursor.getColumnIndex(TeacherTable.COLUMN_FIRSTNAME)),
                            cursor.getString(cursor.getColumnIndex(TeacherTable.COLUMN_LASTNAME)),
                            cursor.getString(cursor.getColumnIndex(TeacherTable.COLUMN_EMAIL))

                    ));
                }
            }
        } catch (Exception e) {
            Log.d(ERROR_TAG, getErrorMessage(e));
        }
        return teachers;
    }


    @SuppressLint("Range")
    public List<Classes> getClasses() {
        int studentID = AccountPreferences.getStudentID(context);
        List<Classes> classesList = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        final String SQL = """
                SELECT
                  c.*
                FROM classes c
                JOIN modules m
                  ON m.module_id = c.module_id
                WHERE m.student_id = ?
                """;
        try (Cursor cursor = db.rawQuery(SQL, new String[]{String.valueOf(studentID)});) {
            if (!isCursorEmpty(cursor)) {
                while (cursor.moveToNext()) {

                    classesList.add(new Classes(

                            cursor.getInt(cursor.getColumnIndex(ClassTable.COLUMN_ID)),
                            cursor.getInt(cursor.getColumnIndex(ClassTable.COLUMN_MODULE_ID)),
                            cursor.getInt(cursor.getColumnIndex(ClassTable.COLUMN_SEMESTER_ID)),
                            cursor.getInt(cursor.getColumnIndex(ClassTable.COLUMN_DOW)),
                            cursor.getString(cursor.getColumnIndex(ClassTable.COLUMN_START_TIME)),
                            cursor.getString(cursor.getColumnIndex(ClassTable.COLUMN_END_TIME)),
                            cursor.getString(cursor.getColumnIndex(ClassTable.COLUMN_ROOM)),
                            cursor.getString(cursor.getColumnIndex(ClassTable.COLUMN_TYPE))


                    ));
                }
            }
        } catch (Exception e) {
            Log.d(ERROR_TAG, getErrorMessage(e));
        }
        return classesList;
    }


    // manage add module teacher form
    @SuppressLint("Range")
    public List<Module> getModuleClassesAdd() {
        int studentID = AccountPreferences.getStudentID(context);
        List<Module> list = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        final String SQL = "SELECT * FROM modules WHERE module_id NOT IN (SELECT  module_id  FROM module_teacher) AND student_id = ?";
        try (Cursor cursor = db.rawQuery(SQL, new String[]{String.valueOf(studentID)});) {
            if (!isCursorEmpty(cursor)) {
                while (cursor.moveToNext()) {
                    list.add(new Module(
                            cursor.getInt(cursor.getColumnIndex(ModuleTable.COLUMN_ID)),
                            cursor.getString(cursor.getColumnIndex(ModuleTable.COLUMN_MODULE_C0DE)),
                            cursor.getString(cursor.getColumnIndex(ModuleTable.COLUMN_MODULE_NAME))
                    ));
                }
            }
        } catch (Exception e) {
            Log.d(ERROR_TAG, getErrorMessage(e));
        }
        return list;
    }

    public boolean deleteSelectedTeacherModules(int moduleID) {
        long result = db.delete(ModuleTeacherTable.TABLE_NAME, ModuleTeacherTable.COLUMN_MODULE_ID + "=?", new String[]{String.valueOf(moduleID)});
        return result != 0;

    }

    @SuppressLint("Range")
    public List<Coursework> getCoursework() {
        int studentID = AccountPreferences.getStudentID(context);
        List<Coursework> courseworkList = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();

        final String SQL = """
                SELECT
                  c.*,
                  m.module_code,
                  m.module_name,
                  m.student_id
                FROM coursework c
                JOIN modules m
                  ON m.module_id = c.module_id
                WHERE student_id = ?
                """;

        try (Cursor cursor = db.rawQuery(SQL, new String[]{String.valueOf(studentID)})) {
            if (!isCursorEmpty(cursor)) {
                while (cursor.moveToNext()) {
                    Coursework coursework = new Coursework(
                            cursor.getInt(cursor.getColumnIndex(CourseworkTable.COLUMN_ID)),
                            cursor.getInt(cursor.getColumnIndex(CourseworkTable.COLUMN_MODULE_ID)),
                            cursor.getString(cursor.getColumnIndex(CourseworkTable.COLUMN_TITLE)),
                            cursor.getString(cursor.getColumnIndex(CourseworkTable.COLUMN_DESCRIPTION)),
                            cursor.getString(cursor.getColumnIndex(CourseworkTable.COLUMN_PRIORITY)),
                            cursor.getString(cursor.getColumnIndex(CourseworkTable.COLUMN_DEADLINE)),
                            cursor.getString(cursor.getColumnIndex(CourseworkTable.COLUMN_DEADLINE_TIME))
                    );
                    byte[] image = cursor.getBlob(cursor.getColumnIndex(CourseworkTable.COLUMN_IMAGE));
                    coursework.setImage(image);
                    coursework.setCompleted(cursor.getString(cursor.getColumnIndex(CourseworkTable.COLUMN_COMPLETED)).equalsIgnoreCase("Yes"));
                    courseworkList.add(coursework);

                }

            }
        } catch (Exception e) {
            Log.d(ERROR_TAG, getErrorMessage(e));
        }
        return courseworkList;
    }

    @SuppressLint("Range")
    public List<Coursework> getUpComingCourseworkByMonth() {
        int studentID = AccountPreferences.getStudentID(context);
        List<Coursework> courseworkList = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        final String SQL = """
                SELECT c.*,
                       m.module_code,
                       m.module_name,
                       m.student_id
                FROM coursework c
                    JOIN modules m
                        ON m.module_id = c.module_id
                WHERE student_id = ?
                      AND c.deadline
                      BETWEEN DATE('now', 'start of month') AND DATE('now', 'start of month', '+1 month', '-1 day')
                ORDER by c.deadline DESC
                """;
        try (Cursor cursor = db.rawQuery(SQL, new String[]{String.valueOf(studentID)});) {
            if (!isCursorEmpty(cursor)) {
                while (cursor.moveToNext()) {
                    Coursework coursework = new Coursework(
                            cursor.getInt(cursor.getColumnIndex(CourseworkTable.COLUMN_ID)),
                            cursor.getInt(cursor.getColumnIndex(CourseworkTable.COLUMN_MODULE_ID)),
                            cursor.getString(cursor.getColumnIndex(CourseworkTable.COLUMN_TITLE)),
                            cursor.getString(cursor.getColumnIndex(CourseworkTable.COLUMN_DESCRIPTION)),
                            cursor.getString(cursor.getColumnIndex(CourseworkTable.COLUMN_PRIORITY)),
                            cursor.getString(cursor.getColumnIndex(CourseworkTable.COLUMN_DEADLINE)),
                            cursor.getString(cursor.getColumnIndex(CourseworkTable.COLUMN_DEADLINE_TIME))

                    );


                    coursework.setCompleted(cursor.getString(cursor.getColumnIndex(CourseworkTable.COLUMN_COMPLETED)).equalsIgnoreCase("Yes"));
                    byte[] image = cursor.getBlob(cursor.getColumnIndex(CourseworkTable.COLUMN_IMAGE));
                    coursework.setImage(image);

                    courseworkList.add(coursework);

                }

            }
        } catch (Exception e) {
            Log.d(ERROR_TAG, getErrorMessage(e));
        }
        return courseworkList;
    }


    @SuppressLint("Range")
    public List<ModuleTeacher> getModuleTeachers() {
        int studentID = AccountPreferences.getStudentID(context);
        List<ModuleTeacher> list = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        final String SQL = """
                SELECT
                  mt.module_id,
                  group_concat(mt.teacher_id) teacher_id_list
                FROM module_teacher mt
                JOIN modules m
                  ON m.module_id = mt.module_id
                WHERE student_id = ?
                GROUP BY mt.module_id
                """;
        try (Cursor cursor = db.rawQuery(SQL, new String[]{String.valueOf(studentID)});) {
            if (!isCursorEmpty(cursor)) {

                while (cursor.moveToNext()) {
                    int moduleID = cursor.getInt(cursor.getColumnIndex(ModuleTeacherTable.COLUMN_MODULE_ID));
                    String teacherIDs = cursor.getString(cursor.getColumnIndex("teacher_id_list"));
                    List<String> teacherIDListStr = new ArrayList<>(Arrays.asList(teacherIDs.split(",")));
                    List<Integer> TeacherIDList = Helper.convertStringArrayToIntArrayList(teacherIDListStr);
                    list.add(new ModuleTeacher(moduleID, TeacherIDList));

                }

            }
        } catch (Exception e) {
            Log.d(ERROR_TAG, getErrorMessage(e));
        }
        return list;
    }

    @SuppressLint("Range")
    public List<String> getTeachersForSelectedModuleID(int module) {
        List<String> list = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        final String SQL = """
                SELECT
                  t.firstname,
                  t.lastname
                FROM module_teacher mt
                JOIN teachers t
                  ON t.teacher_id = mt.teacher_id
                WHERE module_id = ?
                """;
        try (Cursor cursor = db.rawQuery(SQL, new String[]{String.valueOf(module)});) {
            if (!isCursorEmpty(cursor)) {

                while (cursor.moveToNext()) {
                    String firstname = cursor.getString(cursor.getColumnIndex(TeacherTable.COLUMN_FIRSTNAME));
                    String lastname = cursor.getString(cursor.getColumnIndex(TeacherTable.COLUMN_LASTNAME));

                    list.add(String.format("%s %s", firstname, lastname));

                }

            }
        } catch (Exception e) {
            Log.d(ERROR_TAG, getErrorMessage(e));
        }
        return list;
    }


    @SuppressLint("Range")
    public Coursework getSelectedCoursework(int id) {
        Coursework coursework = null;
        SQLiteDatabase db = getReadableDatabase();
        String selection = CourseworkTable.COLUMN_ID + " =?";
        String[] selectionArgs = {String.valueOf(id)};
        try (Cursor cursor = db.query(CourseworkTable.TABLE_NAME, null, selection, selectionArgs,
                null, null, null)) {
            if (cursor.moveToLast()) {
                coursework = new Coursework(
                        cursor.getInt(cursor.getColumnIndex(CourseworkTable.COLUMN_ID)),
                        cursor.getInt(cursor.getColumnIndex(CourseworkTable.COLUMN_MODULE_ID)),
                        cursor.getString(cursor.getColumnIndex(CourseworkTable.COLUMN_TITLE)),
                        cursor.getString(cursor.getColumnIndex(CourseworkTable.COLUMN_DESCRIPTION)),
                        cursor.getString(cursor.getColumnIndex(CourseworkTable.COLUMN_PRIORITY)),
                        cursor.getString(cursor.getColumnIndex(CourseworkTable.COLUMN_DEADLINE)),
                        cursor.getString(cursor.getColumnIndex(CourseworkTable.COLUMN_DEADLINE_TIME))

                );
                coursework.setImage(cursor.getBlob(cursor.getColumnIndex(CourseworkTable.COLUMN_IMAGE)));

                coursework.setCompleted(cursor.getString(
                                cursor.getColumnIndex(CourseworkTable.COLUMN_COMPLETED))
                        .equalsIgnoreCase("Yes"));
            }
        } catch (Exception e) {
            Log.d(ERROR_TAG, getErrorMessage(e));
            return null;
        }
        return coursework;
    }


    @SuppressLint("Range")
    public Classes getSelectedClass(int id) {
        SQLiteDatabase db = getReadableDatabase();
        String selection = ClassTable.COLUMN_ID + " =?";
        String[] selectionArgs = {String.valueOf(id)};
        try (Cursor cursor = db.query(ClassTable.TABLE_NAME, null, selection, selectionArgs,
                null, null, null)) {
            if (cursor.moveToLast()) {
                return new Classes(
                        cursor.getInt(cursor.getColumnIndex(ClassTable.COLUMN_ID)),
                        cursor.getInt(cursor.getColumnIndex(ClassTable.COLUMN_MODULE_ID)),
                        cursor.getInt(cursor.getColumnIndex(ClassTable.COLUMN_SEMESTER_ID)),
                        cursor.getInt(cursor.getColumnIndex(ClassTable.COLUMN_DOW)),
                        cursor.getString(cursor.getColumnIndex(ClassTable.COLUMN_START_TIME)),
                        cursor.getString(cursor.getColumnIndex(ClassTable.COLUMN_END_TIME)),
                        cursor.getString(cursor.getColumnIndex(ClassTable.COLUMN_ROOM)),
                        cursor.getString(cursor.getColumnIndex(ClassTable.COLUMN_TYPE))

                );
            }
        } catch (Exception e) {
            Log.d(ERROR_TAG, getErrorMessage(e));
            return null;
        }
        return null;
    }


    private boolean isCursorEmpty(Cursor cursor) {
        return cursor.getCount() == 0;
    }

    @SuppressLint("Range")
    public List<Semester> getSemester() {
        List<Semester> semesterList = new ArrayList<>();
        int studentID = AccountPreferences.getStudentID(context);
        SQLiteDatabase db = getReadableDatabase();


        String selection = SemesterTable.COLUMN_STUDENT_ID + " = ?";
        String[] selectionArgs = {String.valueOf(studentID)};

        try (Cursor cursor = db.query(
                SemesterTable.TABLE_NAME,
                null,
                selection,
                selectionArgs,
                null,
                null,
                null
        )) {
            if (!isCursorEmpty(cursor)) {
                while (cursor.moveToNext()) {
                    semesterList.add(new Semester(
                            cursor.getInt(cursor.getColumnIndex(SemesterTable.COLUMN_ID)),
                            cursor.getString(cursor.getColumnIndex(SemesterTable.COLUMN_NAME)),
                            LocalDate.parse(cursor.getString(cursor.getColumnIndex(SemesterTable.COLUMN_START_DATE))),
                            LocalDate.parse(cursor.getString(cursor.getColumnIndex(SemesterTable.COLUMN_END_DATE)))
                    ));
                }

            }

        } catch (Exception e) {
            Log.d(ERROR_TAG, getErrorMessage(e));

        }
        return semesterList;
    }

    @SuppressLint("Range")
    public Module getSelectedModule(int id) {
        Module module = null;
        int studentID = AccountPreferences.getStudentID(context);
        SQLiteDatabase db = getReadableDatabase();
        String selection = ModuleTable.COLUMN_ID + " = ?" + " AND " + ModuleTable.COLUMN_STUDENT_ID + "= ?";
        String[] selectionArgs = {String.valueOf(id), String.valueOf(studentID)};
        String[] columns = {ModuleTable.COLUMN_MODULE_C0DE, ModuleTable.COLUMN_MODULE_NAME};
        try (Cursor cursor = db.query(ModuleTable.TABLE_NAME, columns, selection, selectionArgs,
                null, null, null)) {
            if (cursor.moveToLast()) {
                module = new Module(
                        cursor.getString(cursor.getColumnIndex(ModuleTable.COLUMN_MODULE_C0DE)),
                        cursor.getString(cursor.getColumnIndex(ModuleTable.COLUMN_MODULE_NAME)));
            }
        } catch (Exception e) {
            Log.d(ERROR_TAG, getErrorMessage(e));
            return null;

        }
        return module;
    }


    @SuppressLint("Range")
    public List<Integer> getModuleTeachersFiltered(String module) {
        int studentID = AccountPreferences.getStudentID(context);
        List<Integer> list = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        final String SQL = """
                SELECT
                  DISTINCT mt.module_id,
                  m.module_code || ' ' || m.module_name AS details
                FROM
                  module_teacher mt
                  JOIN modules m ON m.module_id = mt.module_id
                WHERE
                  student_id = ?
                  AND details LIKE ?
                 """;

        try (Cursor cursor = db.rawQuery(SQL, new String[]{
                String.valueOf(studentID),
                MessageFormat.format("%{0}%", module)
        })) {

            while (cursor.moveToNext()) {
                int moduleID = cursor.getInt(cursor.getColumnIndex(ModuleTeacherTable.COLUMN_MODULE_ID));

                list.add(moduleID);

            }

        } catch (Exception e) {
            Log.d(ERROR_TAG, getErrorMessage(e));
        }
        return list;
    }


    @SuppressLint("Range")
    public Teacher getSelectedTeacher(int id) {
        Teacher teacher = null;
        int studentID = AccountPreferences.getStudentID(context);
        SQLiteDatabase db = getReadableDatabase();
        String selection = TeacherTable.COLUMN_ID + " = ?" + " AND " + TeacherTable.COLUMN_STUDENT_ID + "= ?";
        String[] selectionArgs = {String.valueOf(id), String.valueOf(studentID)};
        try (Cursor cursor = db.query(TeacherTable.TABLE_NAME, null, selection, selectionArgs,
                null, null, null)) {
            if (cursor.moveToLast()) {
                teacher = new Teacher(
                        cursor.getInt(cursor.getColumnIndex(TeacherTable.COLUMN_ID)),
                        cursor.getString(cursor.getColumnIndex(TeacherTable.COLUMN_FIRSTNAME)),
                        cursor.getString(cursor.getColumnIndex(TeacherTable.COLUMN_LASTNAME)),
                        cursor.getString(cursor.getColumnIndex(TeacherTable.COLUMN_EMAIL)));
            }
        } catch (Exception e) {
            Log.d(ERROR_TAG, getErrorMessage(e));
            return null;

        }
        return teacher;
    }


    @SuppressLint("Range")
    public Semester getSelectedSemester(int id) {
        SQLiteDatabase db = getReadableDatabase();
        String selection = SemesterTable.COLUMN_ID + " = ?";
        String[] selectionArgs = {String.valueOf(id)};
        try (Cursor cursor = db.query(SemesterTable.TABLE_NAME, null, selection, selectionArgs,
                null, null, null)) {
            if (cursor.moveToLast()) {
                return new Semester(
                        cursor.getInt(cursor.getColumnIndex(SemesterTable.COLUMN_ID)),
                        cursor.getString(cursor.getColumnIndex(SemesterTable.COLUMN_NAME)),
                        LocalDate.parse(cursor.getString(cursor.getColumnIndex(SemesterTable.COLUMN_START_DATE))),
                        LocalDate.parse(cursor.getString(cursor.getColumnIndex(SemesterTable.COLUMN_END_DATE)))
                );
            }
        } catch (Exception e) {
            Log.d(ERROR_TAG, getErrorMessage(e));
            return null;

        }
        return null;
    }

    public boolean updateModule(Module module) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(ModuleTable.COLUMN_MODULE_C0DE, module.getModuleCode());
        cv.put(ModuleTable.COLUMN_MODULE_NAME, module.getModuleName());

        long result = db.update(ModuleTable.TABLE_NAME, cv, ModuleTable.COLUMN_ID + "=?", new String[]{String.valueOf(module.getModuleID())});
        return result != -1;


    }


    public boolean updateTeacher(Teacher teacher) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        int studentID = AccountPreferences.getStudentID(context);

        cv.put(TeacherTable.COLUMN_STUDENT_ID, studentID);
        cv.put(TeacherTable.COLUMN_FIRSTNAME, teacher.getFirstname());
        cv.put(TeacherTable.COLUMN_LASTNAME, teacher.getLastname());
        cv.put(TeacherTable.COLUMN_EMAIL, teacher.getEmail());

        long result = db.update(TeacherTable.TABLE_NAME, cv, TeacherTable.COLUMN_ID + "=?", new String[]{String.valueOf(teacher.getUserID())});
        return result != -1;


    }

    public boolean updateSemester(Semester semester) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(SemesterTable.COLUMN_START_DATE, semester.getStart().toString());
        cv.put(SemesterTable.COLUMN_END_DATE, semester.getEnd().toString());
        cv.put(SemesterTable.COLUMN_NAME, semester.getName());

        long result = db.update(SemesterTable.TABLE_NAME, cv, SemesterTable.COLUMN_ID + "=?", new String[]{String.valueOf(semester.getSemesterID())});
        return result != -1;
    }


    public boolean updateCoursework(Coursework coursework, boolean deleteImage) {

        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(CourseworkTable.COLUMN_MODULE_ID, coursework.getModuleID());
        cv.put(CourseworkTable.COLUMN_TITLE, coursework.getTitle());
        cv.put(CourseworkTable.COLUMN_DESCRIPTION, coursework.getDescription());
        cv.put(CourseworkTable.COLUMN_PRIORITY, coursework.getPriority());
        cv.put(CourseworkTable.COLUMN_DEADLINE, coursework.getDeadline());
        cv.put(CourseworkTable.COLUMN_DEADLINE_TIME, coursework.getDeadlineTime());
        cv.put(CourseworkTable.COLUMN_COMPLETED, coursework.isCompleted() ? "Yes" : "No");
        ContentValues values = deleteImage(cv, deleteImage, coursework.getImage());
        long result = db.update(CourseworkTable.TABLE_NAME, values, CourseworkTable.COLUMN_ID + "=?", new String[]{String.valueOf(coursework.getCourseworkID())});
        return result != -1;


    }
    private ContentValues deleteImage(ContentValues cv, boolean deleteImage, Bitmap image){
        if (deleteImage) {
            cv.putNull(CourseworkTable.COLUMN_IMAGE);
        } else {
            if (image != null) {
                cv.put(CourseworkTable.COLUMN_IMAGE, ImageHandler.getBitmapAsByteArray(image));
            }
        }
        return cv;

    }

    public boolean updateClass(Classes classes) {

        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(ClassTable.COLUMN_MODULE_ID, classes.getModuleID());
        cv.put(ClassTable.COLUMN_SEMESTER_ID, classes.getSemesterID());
        cv.put(ClassTable.COLUMN_DOW, classes.getDow());
        cv.put(ClassTable.COLUMN_START_TIME, classes.getStartTime());
        cv.put(ClassTable.COLUMN_END_TIME, classes.getEndTime());
        cv.put(ClassTable.COLUMN_ROOM, classes.getRoom());
        cv.put(ClassTable.COLUMN_TYPE, classes.getClassType());

        long result = db.update(ClassTable.TABLE_NAME, cv, ClassTable.COLUMN_ID + "=?", new String[]{String.valueOf(classes.getClassID())});
        return result != -1;


    }

    public boolean deleteRecord(String table, String idField, int id) {
        String whereClause = idField + "=?";
        String[] whereArgs = new String[]{String.valueOf(id)};
        long result = db.delete(table, whereClause, whereArgs);
        return result != -1;
    }

    public int getCourseworkCountByDate(LocalDate deadlineDate) {

        SQLiteDatabase db = getReadableDatabase();
        int count = 0;
        final String SQL = """
                SELECT
                  COUNT (deadline)
                FROM
                  coursework c
                JOIN modules m ON m.module_id = c.module_id
                                           
                WHERE
                  student_id = ?
                AND deadline = ?
                 """;

        try (Cursor cursor = db.rawQuery(
                SQL,
                new String[]{
                        String.valueOf(AccountPreferences.getStudentID(context)),
                        deadlineDate.toString()}
        )) {

            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                count = cursor.getInt(0);
            }
        } catch (Exception e) {

            Log.d(ERROR_TAG, getErrorMessage(e));

        }
        return count;


    }


}


