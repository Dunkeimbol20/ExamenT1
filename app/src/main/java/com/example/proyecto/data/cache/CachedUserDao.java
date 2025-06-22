package com.example.proyecto.data.cache;


import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;


@Dao
public interface CachedUserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertUser(CachedUserEntity user);

    @Query("SELECT * FROM cached_user_profile WHERE username = :username LIMIT 1")
    CachedUserEntity getUserByUsername(String username);

    @Query("SELECT * FROM cached_user_profile ORDER BY fecha_cacheado_timestamp DESC LIMIT 1")
    CachedUserEntity getMostRecentUser();

    @Query("DELETE FROM cached_user_profile WHERE username = :username")
    void deleteUserByUsername(String username);

    @Query("DELETE FROM cached_user_profile")
    void clearCache();

    @Query("DELETE FROM cached_user_profile WHERE fecha_cacheado_timestamp < :timestamp")
    void deleteOlderThan(long timestamp);

    // Si necesitas buscar por el server_id también:
    @Query("SELECT * FROM cached_user_profile WHERE server_id = :serverId LIMIT 1")
    CachedUserEntity getUserByServerId(int serverId);
}