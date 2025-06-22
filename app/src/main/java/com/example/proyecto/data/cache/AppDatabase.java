package com.example.proyecto.data.cache;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverter;
import androidx.room.TypeConverters;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.proyecto.data.local.dao.RecetaDao;
import com.example.proyecto.data.local.dao.UsuarioDao;
import com.example.proyecto.data.local.model.Usuario;

import java.util.Date;

@Database(
        entities = {Usuario.class,
                CachedUserEntity.class, RecetaEntity.class},
        version = 3,
        exportSchema = false
)
@TypeConverters({AppDatabase.Converters.class})
public abstract class AppDatabase extends RoomDatabase {

    public abstract CachedUserDao cachedUserDao();
    public abstract UsuarioDao usuarioDao();
    public abstract RecetaDao recetaDao();
    private static volatile AppDatabase INSTANCE;
    private static final String DATABASE_NAME = "user_profile_cache_db";
    public static AppDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(
                                    context.getApplicationContext(),
                                    AppDatabase.class,
                                    DATABASE_NAME
                            )
                            .addMigrations(MIGRATION_4_5)
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    public static class Converters {
        @TypeConverter
        public static Date fromTimestamp(Long value) {
            return value == null ? null : new Date(value);
        }

        @TypeConverter
        public static Long dateToTimestamp(Date date) {
            return date == null ? null : date.getTime();
        }
    }
    static final Migration MIGRATION_4_5 = new Migration(3, 4) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            database.execSQL(
                    "CREATE TABLE IF NOT EXISTS `recetas` (" +
                            "`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                            "`titulo` TEXT, " +
                            "`fecha_publicacion` INTEGER, " + // Date se almacena como Long (timestamp)
                            "`imagen_receta_url` TEXT, " +
                            "`dificultad` TEXT, " +
                            "`estacion` TEXT, " +
                            "`tiempo_preparacion` TEXT, " +
                            "`costo_aproximado` TEXT, " +
                            "`ingredientes` TEXT, " +
                            "`preparacion` TEXT, " +
                            "`categorias` TEXT, " +
                            "`user_creator_username` TEXT, " +
                            "FOREIGN KEY(`user_creator_username`) REFERENCES `cached_user_profile`(`username`) ON UPDATE NO ACTION ON DELETE CASCADE DEFERRABLE INITIALLY DEFERRED " +
                            ")");
            database.execSQL(
                    "CREATE INDEX IF NOT EXISTS `index_recetas_user_creator_username` ON `recetas`(`user_creator_username`)"
            );

        }
    };
}