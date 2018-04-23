package com.app.database;

import com.raizlabs.android.dbflow.annotation.Database;
import com.raizlabs.android.dbflow.annotation.Migration;
import com.raizlabs.android.dbflow.config.FlowManager;
import com.raizlabs.android.dbflow.sql.SQLiteType;
import com.raizlabs.android.dbflow.sql.migration.AlterTableMigration;
import com.raizlabs.android.dbflow.structure.database.DatabaseWrapper;
import com.app.domain.PersonModel;
import com.app.domain.SuggestionToMeInfoModel;

@Database(name = AppDatabase.NAME, version = AppDatabase.VERSION)
public class AppDatabase {

    public static final String NAME = "app_data";

    public static final int VERSION = 5;

    @Migration(version = 2, database = AppDatabase.class)
    public static class Migration2 extends AlterTableMigration<SuggestionToMeInfoModel> {

        public Migration2() {
            super(SuggestionToMeInfoModel.class);
        }

        @Override
        public void onPreMigrate() {
            addColumn(SQLiteType.INTEGER, "matchId");
        }
    }

    @Migration(version = 3, database = AppDatabase.class)
    public static class Migration3 extends AlterTableMigration<PersonModel> {

        public Migration3() {
            super(PersonModel.class);
        }

        @Override
        public void onPreMigrate() {
            addColumn(SQLiteType.INTEGER, "cupidCash");
            addColumn(SQLiteType.INTEGER, "redeemableCupidCash");
        }
    }

    @Migration(version = 4, database = AppDatabase.class)
    public static class Migration4 extends AlterTableMigration<PersonModel> {

        public Migration4() {
            super(PersonModel.class);
        }

        @Override
        public void onPreMigrate() {
            addColumn(SQLiteType.INTEGER, "ethnicity");
            addColumn(SQLiteType.TEXT, "inCommon");
        }
    }

    @Migration(version = 5, database = AppDatabase.class)
    public static class Migration5 extends AlterTableMigration<SuggestionToMeInfoModel> {

        public Migration5() {
            super(SuggestionToMeInfoModel.class);
        }

        @Override
        public void onPreMigrate() {
            addColumn(SQLiteType.INTEGER, "daterId");
        }
    }

    public interface TransactionAction {
        void start();
    }

    public static void transaction(TransactionAction transactionAction) {
        DatabaseWrapper database = FlowManager.getDatabase(NAME).getWritableDatabase();
        database.beginTransaction();
        try {
            transactionAction.start();
            database.setTransactionSuccessful();
        } finally {
            database.endTransaction();
        }
    }
}
