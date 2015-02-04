package org.goodev.discourse.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import org.goodev.discourse.database.tables.CategoriesTable;
import org.goodev.discourse.database.tables.Category_group_permissionsTable;
import org.goodev.discourse.database.tables.Category_propertiesTable;
import org.goodev.discourse.database.tables.Featured_usersTable;
import org.goodev.discourse.database.tables.SiteTable;
import org.goodev.discourse.database.tables.Suggested_topicsTable;
import org.goodev.discourse.database.tables.Topic_postersTable;
import org.goodev.discourse.database.tables.TopicsDetailsTable;
import org.goodev.discourse.database.tables.TopicsParticipantsTable;
import org.goodev.discourse.database.tables.TopicsTable;
import org.goodev.discourse.database.tables.Topics_propertiesTable;
import org.goodev.discourse.database.tables.Topics_usersTable;
import org.goodev.discourse.database.tables.UserInfoTable;

/**
 * This database class extends the SQLiteOpenHelper
 * A database file is created: mdsdacpdatabase.db
 * <p/>
 * It is possible to implement an own mechanism to store data on database updates:
 * Write your code inside the defined block inside the "onUpgrade" method!
 * <p/>
 * More details about sqlite databases in android:
 *
 * @author MDSDACP Team - goetzfred@fh-bingen.de
 * @date 2013.09.22
 * @see <a href="http://developer.android.com/guide/topics/data/data-storage.html#db">Tutorial</a>
 * @see <a href="http://developer.android.com/reference/android/database/sqlite/SQLiteOpenHelper.html">Reference</a>
 * <p/>
 * Generated Class. Do not modify!
 */
public class Database extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "mdsdacpdatabase.db";
    private static final int DATABASE_VERSION = 11;

    public Database(final Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public final void onCreate(final SQLiteDatabase db) {
        db.execSQL(SiteTable.SQL_CREATE);
        db.execSQL(UserInfoTable.SQL_CREATE);
        db.execSQL(Featured_usersTable.SQL_CREATE);
        db.execSQL(Category_propertiesTable.SQL_CREATE);
        db.execSQL(CategoriesTable.SQL_CREATE);
        db.execSQL(TopicsTable.SQL_CREATE);
        db.execSQL(TopicsDetailsTable.SQL_CREATE);
        db.execSQL(TopicsParticipantsTable.SQL_CREATE);
        db.execSQL(Suggested_topicsTable.SQL_CREATE);
        db.execSQL(Category_group_permissionsTable.SQL_CREATE);
        db.execSQL(Topics_usersTable.SQL_CREATE);
        db.execSQL(Topics_propertiesTable.SQL_CREATE);
        db.execSQL(Topic_postersTable.SQL_CREATE);
    }

    @Override
    public final void onUpgrade(final SQLiteDatabase db, final int oldVersion, final int newVersion) {
        /*PROTECTED REGION ID(DatabaseUpdate) ENABLED START*/

        // TODO Implement your database update functionality here and remove the following method call!
        onUpgradeDropTablesAndCreate(db);

		/*PROTECTED REGION END*/
    }

    /**
     * This basic upgrade functionality will destroy all old data on upgrade
     */
    private final void onUpgradeDropTablesAndCreate(final SQLiteDatabase db) {
        db.execSQL(SiteTable.SQL_DROP);
        db.execSQL(UserInfoTable.SQL_DROP);
        db.execSQL(Featured_usersTable.SQL_DROP);
        db.execSQL(Category_propertiesTable.SQL_DROP);
        db.execSQL(CategoriesTable.SQL_DROP);
        db.execSQL(TopicsTable.SQL_DROP);
        db.execSQL(TopicsDetailsTable.SQL_DROP);
        db.execSQL(TopicsParticipantsTable.SQL_DROP);
        db.execSQL(Suggested_topicsTable.SQL_DROP);
        db.execSQL(Category_group_permissionsTable.SQL_DROP);
        db.execSQL(Topics_usersTable.SQL_DROP);
        db.execSQL(Topics_propertiesTable.SQL_DROP);
        db.execSQL(Topic_postersTable.SQL_DROP);
        onCreate(db);
    }
}
