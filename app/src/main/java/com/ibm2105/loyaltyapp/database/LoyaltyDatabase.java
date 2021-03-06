package com.ibm2105.loyaltyapp.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {Account.class, Notification.class, Cart.class, CartItem.class, Code.class, Item.class, News.class, Reward.class}, version = 1, exportSchema = false)
public abstract class LoyaltyDatabase extends RoomDatabase {

    public abstract AccountDao accountDao();
    public abstract NotificationDao notificationDao();
    public abstract CartDao cartDao();
    public abstract CartItemDao cartItemDao();
    public abstract CodeDao codeDao();
    public abstract ItemDao itemDao();
    public abstract NewsDao newsDao();
    public abstract RewardDao rewardDao();

    private static volatile LoyaltyDatabase INSTANCE;
    private static final int NUMBER_OF_THREADS = 4;
    public static final ExecutorService databaseWriteExecutor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    public static LoyaltyDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (LoyaltyDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(), LoyaltyDatabase.class, "flash_loyalty_database").createFromAsset("flash_loyalty_database.db").build();
                }
            }
        }

        return INSTANCE;
    }
}
