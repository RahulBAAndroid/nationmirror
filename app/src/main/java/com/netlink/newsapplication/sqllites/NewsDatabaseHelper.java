package com.netlink.newsapplication.sqllites;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.netlink.newsapplication.models.NewsModel;


import java.util.ArrayList;
import java.util.List;
public class NewsDatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "newsDatabase";
    private static final int DATABASE_VERSION = 1;

    // Table Name
    private static final String TABLE_NAME = "news";

    // Column Names
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_TITLE = "title";
    private static final String COLUMN_CONTENT = "content";
    private static final String COLUMN_IMAGE_URL = "image_url";
    private static final String COLUMN_CATEGORY = "category";

    public NewsDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Create Table Query
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE_QUERY = "CREATE TABLE " + TABLE_NAME + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY, " +
                COLUMN_TITLE + " TEXT, " +
                COLUMN_CONTENT + " TEXT, " +
                COLUMN_IMAGE_URL + " TEXT, " +
                COLUMN_CATEGORY + " TEXT);";
        db.execSQL(CREATE_TABLE_QUERY);

        // Insert dummy news
        insertDummyNews(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    // Insert News Data
    public void insertNews(List<NewsModel> newsList, String category) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();
        try {
            for (NewsModel newsModel : newsList) {
                ContentValues values = new ContentValues();
                values.put(COLUMN_ID, newsModel.getId());
                values.put(COLUMN_TITLE, newsModel.getTitle().getRendered());
                values.put(COLUMN_CONTENT, newsModel.getContent().getRendered());
                values.put(COLUMN_IMAGE_URL, newsModel.getYoastHeadJson().getOgImage().get(0).getUrl());
                values.put(COLUMN_CATEGORY, category);

                db.insert(TABLE_NAME, null, values);
            }
            db.setTransactionSuccessful();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
        }
    }

    // Get All News from Database
    @SuppressLint("Range")
    public List<NewsModel> getAllNews() {
        List<NewsModel> newsList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME, null, null, null, null, null, null);

        if (cursor != null) {
            while (cursor.moveToNext()) {
                NewsModel newsModel = new NewsModel();
                newsModel.setId(cursor.getInt(cursor.getColumnIndex(COLUMN_ID)));
                NewsModel.Title title = new NewsModel.Title();
                title.setRendered(cursor.getString(cursor.getColumnIndex(COLUMN_TITLE)));
                newsModel.setTitle(title);
                NewsModel.Content content = new NewsModel.Content();
                content.setRendered(cursor.getString(cursor.getColumnIndex(COLUMN_CONTENT)));
                newsModel.setContent(content);
                NewsModel.YoastHeadJson yoastHeadJson = new NewsModel.YoastHeadJson();
                List<NewsModel.OgImage> ogImages = new ArrayList<>();
                NewsModel.OgImage ogImage = new NewsModel.OgImage();
                ogImage.setUrl(cursor.getString(cursor.getColumnIndex(COLUMN_IMAGE_URL)));
                ogImages.add(ogImage);
                yoastHeadJson.setOgImage(ogImages);
                newsModel.setYoastHeadJson(yoastHeadJson);
                newsList.add(newsModel);
            }
            cursor.close();
        }
        return newsList;
    }
    // Delete All News from the Database
    public void deleteAllNews() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, null, null); // This will delete all rows from the table
    }


    //Inserting dummy data
    private void insertDummyNews(SQLiteDatabase db) {
        String dummyNewsQuery = "INSERT INTO " + TABLE_NAME + " (" +
                COLUMN_TITLE + ", " +
                COLUMN_CONTENT + ", " +
                COLUMN_IMAGE_URL + ", " +
                COLUMN_CATEGORY +
                ") VALUES " +
                "('ट्रक और मुर्गी लोड पिकअप की टक्टर 2 की मौत', 'छत्तीसगढ़ के रायगढ़ जिले में तेज रफ्तार ट्रक और पिकअप की आमने-सामने टक्कर हो गई। इसमें पिकअप सवार 2 लोगों की मौके पर मौत हो गई। हादसे के बाद क्रेन की मदद से दोनों की लाश को बाहर निकाला गया।', 'https://resize.indiatvnews.com/en/centered/newbucket/1200_675/2024/09/file-image-2024-09-02t141553-1725266765.jpg', 'General')," +
                "('IPL 2025 ऋषभ पंत को रिलीज कर अक्षर पटेल को दिल्ली कैपिटल्स की कप्तानी सौंपने की तैयारी', 'दिल्ली कैपिटल्स फ्रेंचाइजी ने आईपीएल 2025 के लिए टीम में बड़ा बदलाव करने का फैसला लिया है। मेगा ऑक्शन से पहले फ्रेंचाइजी ने सिर्फ चार खिलाड़ियों को रिटेन किया है, जिसमें कप्तान ऋषभ पंत का नाम शामिल नहीं है।', 'https://resize.indiatvnews.com/en/centered/newbucket/1200_675/2024/09/file-image-2024-09-02t141553-1725266765.jpg', 'Sports')," +
                "('44 साल का फिल्मी सफर खत्म कर रहे Rakesh Roshan', 'राकेश रोशन ने बॉलीवुड में अपने 44 साल के फिल्मी करियर को अलविदा कहने का ऐलान कर दिया है। एक अभिनेता और निर्देशक के तौर पर उनका सफर बेहद शानदार रहा है', 'https://resize.indiatvnews.com/en/centered/newbucket/1200_675/2024/09/file-image-2024-09-02t141553-1725266765.jpg', 'Entertainment');";
        db.execSQL(dummyNewsQuery);
    }
}
