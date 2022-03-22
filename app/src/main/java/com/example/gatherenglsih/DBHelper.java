package com.example.gatherenglsih;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class DBHelper extends SQLiteOpenHelper {

    private final Context fContext;

    public static String DATABASE_NAME = "Userdata.db";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_USER = "User";
    private static final String TABLE_FLASHCARD = "Flashcard";
    private static final String TABLE_USERCARD = "UserCard";
    private static final String TABLE_EXERCISE = "Exercise";

    private static final String KEY_USER_ID = "userID";
    private static final String KEY_CARD_ID = "cardID";
    private static final String KEY_EXERCISE_ID = "exerciseID";
    private static final String KEY_USERNAME = "UserName";
    private static final String KEY_REGISTER_DATE = "RegisterDate";
    private static final String KEY_COIN_AMOUNT = "CoinAmount";
    private static final String KEY_CARD_TITLE = "cardTitle";
    private static final String KEY_CARD_TYPE = "cardType";
    private static final String KEY_CARD_CATEGORY = "cardCategory";
    private static final String KEY_CARD_DIAGRAM = "cardDiagram";
    private static final String KEY_CARD_AUDIO = "cardAudio";
    private static final String KEY_CARD_OBTAINED_UPGRADED_DATE = "cardObtainUpgradeDate";
    private static final String KEY_CARD_STATUS = "cardStatus";
    private static final String KEY_EXERCISE_TYPE = "exerciseType";
    private static final String KEY_EXERCISE_DATE = "exerciseDate";
    private static final String KEY_NO_CORRECT_ANSWER = "noCorrectAns";
    private static final String KEY_NO_TOTAL_QUESTION = "noTotalQues";

    private static final String CREATE_TABLE_USER = "CREATE TABLE "
            + TABLE_USER + "(" + KEY_USER_ID + " TEXT PRIMARY KEY,"
            + KEY_USERNAME + " TEXT," + KEY_REGISTER_DATE + " TEXT," + KEY_COIN_AMOUNT + " INTEGER);";
    private static final String CREATE_TABLE_FLASHCARD = "CREATE TABLE "
            + TABLE_FLASHCARD + "(" + KEY_CARD_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + KEY_CARD_TITLE + " TEXT," + KEY_CARD_TYPE + " TEXT," + KEY_CARD_CATEGORY + " TEXT,"
            + KEY_CARD_DIAGRAM + " INTEGER," + KEY_CARD_AUDIO + " TEXT);";
    private static final String CREATE_TABLE_USERCARD = "CREATE TABLE "
            + TABLE_USERCARD + "(" + KEY_CARD_ID + " INTEGER PRIMARY KEY," + KEY_CARD_OBTAINED_UPGRADED_DATE + " TEXT,"
            + KEY_CARD_STATUS + " TEXT);";
    private static final String CREATE_TABLE_EXERCISE = "CREATE TABLE "
            + TABLE_EXERCISE + "(" + KEY_EXERCISE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + KEY_EXERCISE_TYPE + " TEXT," + KEY_EXERCISE_DATE + " TEXT," + KEY_NO_CORRECT_ANSWER + " INTEGER,"
            + KEY_NO_TOTAL_QUESTION + " INTEGER);";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        fContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase DB) {
        DB.execSQL(CREATE_TABLE_USER);
        DB.execSQL(CREATE_TABLE_FLASHCARD);
        DB.execSQL(CREATE_TABLE_USERCARD);
        DB.execSQL(CREATE_TABLE_EXERCISE);

        //Initialize the data for flashcard table
        ContentValues values = new ContentValues();

        Resources res = fContext.getResources();


        int[] diagramArray = {R.drawable.sofa1, R.drawable.sofa, R.drawable.lamp, R.drawable.lamp1, R.drawable.table1,
                R.drawable.table, R.drawable.chair, R.drawable.chair1, R.drawable.bed, R.drawable.bed1, R.drawable.clock,
                R.drawable.clock1, R.drawable.door, R.drawable.door1};
        String[] nameArray = res.getStringArray(R.array.card_name);
        String[] typeArray = res.getStringArray(R.array.card_type);
        String[] catArray = res.getStringArray(R.array.card_category);
        int[] audioArray = {R.raw.sofa, R.raw.sofa, R.raw.lamp, R.raw.lamp, R.raw.table, R.raw.table, R.raw.chair,
                R.raw.chair, R.raw.bed, R.raw.bed, R.raw.clock, R.raw.clock, R.raw.door, R.raw.door};

        for (int i = 0; i < nameArray.length; i++) {
            values.put(KEY_CARD_TITLE, nameArray[i]);
            values.put(KEY_CARD_TYPE, typeArray[i]);
            values.put(KEY_CARD_CATEGORY, catArray[i]);
            values.put(KEY_CARD_DIAGRAM, diagramArray[i]);
            values.put(KEY_CARD_AUDIO, audioArray[i]);
            DB.insert(TABLE_FLASHCARD, null, values);
        }

    }

    @Override
    public void onUpgrade(SQLiteDatabase DB, int i, int i1) {
        DB.execSQL("DROP TABLE IF EXISTS '" + TABLE_USER + "'");
        DB.execSQL("DROP TABLE IF EXISTS '" + TABLE_FLASHCARD + "'");
        DB.execSQL("DROP TABLE IF EXISTS '" + TABLE_USERCARD + "'");
        DB.execSQL("DROP TABLE IF EXISTS '" + TABLE_EXERCISE + "'");
        onCreate(DB);

    }

    // User Table Functions
    public boolean addNewUser(String userID, String UserName, String RegisterDate, int CoinAmount) {
        SQLiteDatabase DB = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(KEY_USER_ID, userID);
        values.put(KEY_USERNAME, UserName);
        values.put(KEY_REGISTER_DATE, RegisterDate);
        values.put(KEY_COIN_AMOUNT, CoinAmount);

        long result = DB.insert(TABLE_USER, null, values);

        return result != -1;
    }

    public boolean updateCoinAmount(String userID, int CoinAmount) {
        SQLiteDatabase DB = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(KEY_COIN_AMOUNT, CoinAmount);

        String sql = "Select * from " + TABLE_USER + " where " + KEY_USER_ID + "= ?";
        Cursor cursor = DB.rawQuery(sql, new String[]{userID});
        if (cursor.getCount() > 0) {

            cursor.close();
            long result = DB.update(TABLE_USER, values, KEY_USER_ID + "= ?", new String[]{userID});

            return result != -1;
        } else {
            return false;
        }

    }

    public boolean getUserID(String userID) {
        SQLiteDatabase DB = this.getWritableDatabase();

        String sql = "Select * from " + TABLE_USER + " where " + KEY_USER_ID + "= ?";
        Cursor cursor = DB.rawQuery(sql, new String[]{userID});

        if (cursor != null) {
            if (cursor.getCount() > 0) {
                cursor.close();
                return true;
            } else {
                return false;
            }
        }
        return false;
    }

    public String getUserName(String userID) {
        SQLiteDatabase DB = this.getWritableDatabase();

        String sql = "Select " + KEY_USERNAME + " from " + TABLE_USER + " where " + KEY_USER_ID + "= ?";
        Cursor cursor = DB.rawQuery(sql, new String[]{userID});

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                @SuppressLint("Range") String username = cursor.getString(cursor.getColumnIndex(KEY_USERNAME));
                cursor.close();

                return username;
            }
        }
        return "username";
    }

    public int getCoinAmount(String userID) {
        SQLiteDatabase DB = this.getWritableDatabase();

        String sql = "Select " + KEY_COIN_AMOUNT + " from " + TABLE_USER + " where " + KEY_USER_ID + "= ?";
        Cursor cursor = DB.rawQuery(sql, new String[]{userID});

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                @SuppressLint("Range") int coinAmount = cursor.getInt(cursor.getColumnIndex(KEY_COIN_AMOUNT));
                cursor.close();

                return coinAmount;
            }
        }
        return 999;
    }

    //Flashcard Table Functions
    public int getCardID(String cardTitle) {
        SQLiteDatabase DB = this.getReadableDatabase();

        String sql = "Select " + KEY_CARD_ID + " from " + TABLE_FLASHCARD + " WHERE " + KEY_CARD_TITLE
                + " = ?";
        Cursor cursor = DB.rawQuery(sql, new String[]{cardTitle});

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                @SuppressLint("Range") int id = cursor.getInt(cursor.getColumnIndex(KEY_CARD_ID));
                cursor.close();

                return id;
            }
        }
        return 0;
    }

    public String getCardTitle(int cardID) {
        SQLiteDatabase DB = this.getReadableDatabase();

        String sql = "Select " + KEY_CARD_TITLE + " from " + TABLE_FLASHCARD + " WHERE " + KEY_CARD_ID
                + " = ? ";
        Cursor cursor = DB.rawQuery(sql, new String[]{String.valueOf(cardID)});

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                @SuppressLint("Range") String title = cursor.getString(cursor.getColumnIndex(KEY_CARD_TITLE));
                cursor.close();

                return title;
            }
        }
        return null;
    }

    public int getCardDiagram(String cardCat) {
        SQLiteDatabase DB = this.getReadableDatabase();

        String sql = "Select " + KEY_CARD_DIAGRAM + " from " + TABLE_FLASHCARD + " WHERE " + KEY_CARD_TITLE
                + " = ? AND " + KEY_CARD_TYPE + " = ?";
        Cursor cursor = DB.rawQuery(sql, new String[]{cardCat, "LV1"});

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                @SuppressLint("Range") int diagram = cursor.getInt(cursor.getColumnIndex(KEY_CARD_DIAGRAM));
                cursor.close();

                return diagram;
            }
        }
        return 0;
    }

    public int getCardAudio(int cardID) {
        SQLiteDatabase DB = this.getReadableDatabase();

        String sql = "Select " + KEY_CARD_AUDIO + " from " + TABLE_FLASHCARD + " WHERE " + KEY_CARD_ID
                + " = ?";
        Cursor cursor = DB.rawQuery(sql, new String[]{String.valueOf(cardID)});

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                @SuppressLint("Range") int diagram = cursor.getInt(cursor.getColumnIndex(KEY_CARD_AUDIO));
                cursor.close();

                return diagram;
            }
        }
        return 0;
    }

    public int getCardIDbyDiagram(String cardDiagram) {
        SQLiteDatabase DB = this.getReadableDatabase();

        String sql = "Select " + KEY_CARD_ID + " from " + TABLE_FLASHCARD + " WHERE " + KEY_CARD_DIAGRAM
                + " = ?";
        Cursor cursor = DB.rawQuery(sql, new String[]{cardDiagram});

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                @SuppressLint("Range") int id = cursor.getInt(cursor.getColumnIndex(KEY_CARD_ID));
                cursor.close();

                return id;
            }
        }
        return 0;
    }

    //userCard Table Functions
    public boolean storeObtainCard(int cardId, String obtUpgDate) {
        SQLiteDatabase DB = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(KEY_CARD_ID, cardId);
        values.put(KEY_CARD_STATUS, "display");
        values.put(KEY_CARD_OBTAINED_UPGRADED_DATE, obtUpgDate);

        long result = DB.insert(TABLE_USERCARD, null, values);

        return result != -1;
    }

    public boolean storeUpgradeCard(int cardId, String obtUpgDate) {
        SQLiteDatabase DB = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        int upgradeCardID = cardId + 1;

        values.put(KEY_CARD_ID, upgradeCardID);
        values.put(KEY_CARD_STATUS, "display");
        values.put(KEY_CARD_OBTAINED_UPGRADED_DATE, obtUpgDate);

        long result = DB.insert(TABLE_USERCARD, null, values);

        if (result == -1) {
            return false;
        } else {
            ContentValues updateValue = new ContentValues();

            updateValue.put(KEY_CARD_STATUS, "hide");

            String sql = "Select * from " + TABLE_USERCARD + " WHERE " + KEY_CARD_ID + "= ?";
            Cursor cursor = DB.rawQuery(sql, new String[]{String.valueOf(cardId)});
            if (cursor.getCount() > 0) {
                cursor.close();

                long result2 = DB.update(TABLE_USERCARD, updateValue, KEY_CARD_ID + "= ?", new String[]{String.valueOf(cardId)});

                return result2 != -1;
            } else {
                return false;
            }
        }
    }

    public boolean checkAvailability(int cardID) {
        SQLiteDatabase DB = this.getWritableDatabase();

        String sql = "Select * from " + TABLE_USERCARD + " WHERE " + KEY_CARD_ID + "= ?";
        Cursor cursor = DB.rawQuery(sql, new String[]{String.valueOf(cardID)});

        if (cursor.getCount() > 0) {
            cursor.close();
            return true;
        } else {
            return false;
        }
    }

    public boolean swapCardDiagram(int cardID) {
        SQLiteDatabase DB = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        ContentValues values2 = new ContentValues();

        values.put(KEY_CARD_STATUS, "display");
        values2.put(KEY_CARD_STATUS, "hide");

        String sql = "Select * from " + TABLE_USERCARD + " WHERE " + KEY_CARD_ID + "= ?";

        if (cardID % 2 == 0) {//if displaying card is LV2
            int lv1CardID = cardID - 1;
            Cursor cursor = DB.rawQuery(sql, new String[]{String.valueOf(cardID)});
            if (cursor.getCount() > 0) {
                cursor.close();

                long result = DB.update(TABLE_USERCARD, values2, KEY_CARD_ID + "= ?", new String[]{String.valueOf(cardID)});

                if (result == -1) {
                    return false;
                } else {
                    Cursor cursor2 = DB.rawQuery(sql, new String[]{String.valueOf(lv1CardID)});
                    if (cursor2.getCount() > 0) {
                        cursor2.close();

                        long result2 = DB.update(TABLE_USERCARD, values, KEY_CARD_ID + "= ?", new String[]{String.valueOf(lv1CardID)});

                        return result2 != -1;
                    } else {
                        return false;
                    }
                }
            } else {
                return false;
            }
        } else {
            int lv2CardID = cardID + 1;
            Cursor cursor = DB.rawQuery(sql, new String[]{String.valueOf(cardID)});
            if (cursor.getCount() > 0) {
                cursor.close();

                long result = DB.update(TABLE_USERCARD, values2, KEY_CARD_ID + "= ?", new String[]{String.valueOf(cardID)});

                if (result == -1) {
                    return false;
                } else {
                    Cursor cursor2 = DB.rawQuery(sql, new String[]{String.valueOf(lv2CardID)});
                    if (cursor2.getCount() > 0) {
                        cursor2.close();

                        long result2 = DB.update(TABLE_USERCARD, values, KEY_CARD_ID + "= ?", new String[]{String.valueOf(lv2CardID)});

                        return result2 != -1;
                    } else {
                        return false;
                    }
                }
            } else {
                return false;
            }
        }
    }

    public int getDiagram(int cardID) {
        SQLiteDatabase DB = this.getReadableDatabase();

        String sql = "SELECT " + KEY_CARD_DIAGRAM + " FROM " + TABLE_FLASHCARD + " INNER JOIN "
                + TABLE_USERCARD + " ON " + TABLE_FLASHCARD + "." + KEY_CARD_ID + " = " + TABLE_USERCARD + "." + KEY_CARD_ID
                + " WHERE " + TABLE_USERCARD + "." + KEY_CARD_ID + " = ?" + " AND " + KEY_CARD_STATUS + " = ?";
        Cursor cursor = DB.rawQuery(sql, new String[]{String.valueOf(cardID), "display"});

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                @SuppressLint("Range") int diagram = cursor.getInt(cursor.getColumnIndex(KEY_CARD_DIAGRAM));
                cursor.close();

                return diagram;
            }
        }
        return 0;
    }

    public ArrayList<Integer> getFlashcardDiagrams() {
        SQLiteDatabase DB = this.getReadableDatabase();

        String sql = "SELECT " + KEY_CARD_DIAGRAM + " FROM " + TABLE_FLASHCARD + " INNER JOIN "
                + TABLE_USERCARD + " ON " + TABLE_FLASHCARD + "." + KEY_CARD_ID + " = " + TABLE_USERCARD + "." + KEY_CARD_ID
                + " WHERE " + KEY_CARD_STATUS + " = ?";
        Cursor cursor = DB.rawQuery(sql, new String[]{"display"});

        ArrayList<Integer> diagram = new ArrayList<>();

        if (cursor != null) {
            while (cursor.moveToNext()) {
                @SuppressLint("Range") int cDiagram = cursor.getInt(cursor.getColumnIndex(KEY_CARD_DIAGRAM));
                diagram.add(cDiagram);
            }
            cursor.close();
        }
        return diagram;
    }

    public boolean checkCardQty() {
        SQLiteDatabase DB = this.getReadableDatabase();

        String checkSql = "Select * from " + TABLE_USERCARD;
        Cursor check = DB.rawQuery(checkSql, null);

        if (check.getCount() >= 5) {
            check.close();
            return true;
        } else {
            return false;
        }
    }


    public void getListeningQuestions(ArrayList<ListeningQuizModel> quizModelArrayList) {
        SQLiteDatabase DB = this.getReadableDatabase();
        ArrayList<Integer> audioList = new ArrayList<>();
        ArrayList<Integer> diagramList = new ArrayList<>();
        ArrayList<String> titleList = new ArrayList<>();
        ArrayList<Integer> indexList = new ArrayList<>();

        String sql = "SELECT * FROM " + TABLE_FLASHCARD + " INNER JOIN "
                + TABLE_USERCARD + " ON " + TABLE_FLASHCARD + "." + KEY_CARD_ID + " = " + TABLE_USERCARD + "." + KEY_CARD_ID
                + " WHERE " + KEY_CARD_TYPE + " = ? " + " ORDER BY RANDOM() LIMIT 4";

        Cursor cursor = DB.rawQuery(sql, new String[]{"LV1"});

        if (cursor != null) {
            while (cursor.moveToNext()) {
                @SuppressLint("Range") int audio = cursor.getInt(cursor.getColumnIndex(KEY_CARD_AUDIO));
                @SuppressLint("Range") int diagram = cursor.getInt(cursor.getColumnIndex(KEY_CARD_DIAGRAM));
                @SuppressLint("Range") String title = cursor.getString(cursor.getColumnIndex(KEY_CARD_TITLE));

                audioList.add(audio);
                diagramList.add(diagram);
                titleList.add(title);
            }
            cursor.close();
        }

        Random random = new Random();

        for (int i = 0; i < 4; i++) {
            indexList.add(i);
        }
        Collections.shuffle(indexList);

        int quesAudio = indexList.get(random.nextInt(indexList.size()));
        int opt1 = indexList.get(0);
        int opt2 = indexList.get(1);
        int opt3 = indexList.get(2);
        int opt4 = indexList.get(3);

        int answer = 0;
        for (int i = 0; i < 4; i++){
            int opt = indexList.get(i);
            if (quesAudio == opt){
                answer = i+1;
            }
        }

        boolean contains = false;
        for(ListeningQuizModel a : quizModelArrayList){
            if (a.getQuestionAudio() == audioList.get(quesAudio)){
                contains = true;
            }
        }

        if (contains == false){
            quizModelArrayList.add(new ListeningQuizModel(audioList.get(quesAudio), titleList.get(opt1), diagramList.get(opt1),
                    titleList.get(opt2), diagramList.get(opt2), titleList.get(opt3), diagramList.get(opt3), titleList.get(opt4),
                    diagramList.get(opt4), answer));
        }

    }
}




