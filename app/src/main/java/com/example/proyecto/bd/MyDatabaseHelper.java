package com.example.proyecto.bd;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class MyDatabaseHelper  extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "Proyecto.db";
    private static final int DATABASE_VERSION = 1;
    public MyDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    public void onCreate(SQLiteDatabase db) {
        //tabla usuario
        String createTable = "CREATE TABLE usuarios (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "nombre TEXT," +
                "apellido TEXT," +
                "fecha TEXT," +
                "username TEXT,"+
                "contrasena TEXT)";
        db.execSQL(createTable);
        //tabla recetas
        String createRecetasTable = "CREATE TABLE recetas (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "title VARCHAR(50)," +
                "dificultad VARCHAR(50)," +
                "estacion VARCHAR(50)," +
                "tiempo VARCHAR(100)," +
                "estado_economico VARCHAR(100)," +
                "ingredientes TEXT," +
                "preparacion TEXT," +
                "user_id INTEGER," +
                "FOREIGN KEY(user_id) REFERENCES users(id));";
        db.execSQL(createRecetasTable);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS recetas");
        db.execSQL("DROP TABLE IF EXISTS usuarios");
        onCreate(db);
    }
    public void insertarUsuario(String Nombre, String Username,String Apellido,String Fecha,String Contrasena) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("nombre", Nombre);
        values.put("apellido", Apellido);
        values.put("fecha",Fecha);
        values.put("username", Username);
        values.put("contrasena",Contrasena);
        db.insert("usuarios", null, values);
        db.close();
    }
    public boolean verificarLogin(String username, String contrasena){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(
                "SELECT * FROM usuarios WHERE username=? AND contrasena=?",
                new String[]{username, contrasena});
        boolean result = cursor.getCount() > 0;
        cursor.close();
        db.close();
        return result;
    }


}
