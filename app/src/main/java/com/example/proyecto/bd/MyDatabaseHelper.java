package com.example.proyecto.bd;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.HashMap;
import java.util.Map;


public class MyDatabaseHelper  extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "Proyecto.db";
    private static final int DATABASE_VERSION = 1;
    public MyDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    public void onCreate(SQLiteDatabase db) {
        //tabla usuario
        String createTable =
                "CREATE TABLE usuarios (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "nombre TEXT ," +
                "apellido TEXT ," +
                "imagen_perfil BLOB," +
                "fecha_nacimiento TEXT ," +
                "fecha_registro TEXT," +
                "username TEXT ," +
                "contrasena TEXT)";
        db.execSQL(createTable);
        //tabla recetas
        String createRecetasTable =
                "CREATE TABLE recetas (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "titulo TEXT ," +
                "fecha_publicacion TEXT," +
                "imagen_receta BLOB," +
                "dificultad TEXT," +
                "estacion TEXT," +
                "tiempo TEXT," +
                "estado_economico TEXT," +
                "ingredientes TEXT," +
                "preparacion TEXT," +
                "user_id INTEGER," +
                "FOREIGN KEY(user_id) REFERENCES usuarios(id));";
        db.execSQL(createRecetasTable);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + "usuarios");
        db.execSQL("DROP TABLE IF EXISTS " + "recetas");
        onCreate(db);

    }
    public void insertarUsuario(String Nombre, String Username,String Apellido,String Fecha_nacimiento,String Contrasena) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("nombre", Nombre);
        values.put("apellido", Apellido);
        values.put("fecha_nacimiento",Fecha_nacimiento);
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
    public Map<String, Object> getUserData(String username) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;
        Map<String, Object> userData = null;

        try {
            String[] columns = {
                    "nombre",
                    "apellido",
                    "imagen_perfil",
                    //"fecha_nacimiento",
                    "username"
            };

            String selection = "username" + " = ?";
            String[] selectionArgs = {username};

            cursor = db.query(
                    "usuarios",
                    columns,
                    selection,
                    selectionArgs,
                    null,
                    null,
                    null
            );

            if (cursor != null && cursor.moveToFirst()) {
                userData = new HashMap<>();

                int nombreIndex = cursor.getColumnIndexOrThrow("nombre");
                userData.put("nombre", cursor.getString(nombreIndex));

                int apellidoIndex = cursor.getColumnIndexOrThrow("apellido");
                userData.put("apellido", cursor.getString(apellidoIndex));

                int imagenIndex = cursor.getColumnIndexOrThrow("imagen_perfil");
                byte[] imagenBytes = cursor.getBlob(imagenIndex);
                userData.put("imagen_perfil", imagenBytes);

                //int fechaNacimientoIndex = cursor.getColumnIndexOrThrow("fecha_nacimiento");
                //userData.put("fecha_nacimiento", cursor.getString(fechaNacimientoIndex));

                int usernameIndex = cursor.getColumnIndexOrThrow("username");
                userData.put("username", cursor.getString(usernameIndex));
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            db.close();
        }

        return userData;
    }
    public int updateUserDetails(String username, Map<String, String> userDetails) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        int rowsAffected = -1;

        try {
            // Ponemos los nuevos valores de los campos de texto en ContentValues
            // Solo agregamos las columnas que están en el Map userDetails
            if (userDetails != null) {
                for (Map.Entry<String, String> entry : userDetails.entrySet()) {
                    // Verificamos si la clave corresponde a una columna que queremos actualizar
                    if (entry.getKey().equals("nombre") ||
                            entry.getKey().equals("apellido") ||
                            //entry.getKey().equals("fecha_nacimiento") ||
                            entry.getKey().equals("contrasena") ||
                            entry.getKey().equals("username")) { // Si permites cambiar el username
                        values.put(entry.getKey(), entry.getValue());
                    }
                }
            }

            // Solo procede si hay valores para actualizar
            if (values.size() > 0) {
                String selection = "username" + " = ?";
                String[] selectionArgs = {username};

                rowsAffected = db.update(
                        "usuarios",
                        values,
                        selection,
                        selectionArgs
                );
            } else {
                // No hay valores de texto para actualizar
                rowsAffected = 0;
            }

        } catch (Exception e) {
            e.printStackTrace();
            rowsAffected = -1; // Indicar error
        } finally {
            db.close(); // Cerrar la base de datos
        }

        return rowsAffected;
    }
    public int updateProfilePicture(String username, byte[] imageData) {
        SQLiteDatabase db = this.getWritableDatabase(); // Obtenemos una instancia escribible
        ContentValues values = new ContentValues();
        int rowsAffected = -1;

        try {
            // Si imageData es null, establecer la columna imagen_perfil a NULL en la base de datos
            if (imageData != null) {
                values.put("imagen_perfil", imageData);
            } else {
                values.putNull("imagen_perfil"); // Usa putNull para establecer el valor a NULL
            }

            // Definimos la cláusula WHERE para actualizar por nombre de usuario
            String selection = "username" + " = ?";
            // Definimos los argumentos para la cláusula WHERE
            String[] selectionArgs = {username};

            // Realizamos la operación de actualización
            // Solo procedemos si hay un valor en values (que siempre debería haber si imageData es null o no)
            if (values.size() > 0) {
                rowsAffected = db.update(
                        "usuarios",  // La tabla a actualizar (nombre de tu tabla)
                        values,          // Los nuevos valores (solo la imagen de perfil o NULL)
                        selection,       // La cláusula WHERE
                        selectionArgs    // Los argumentos de la cláusula WHERE
                );
            } else {
                rowsAffected = 0; // No hay nada que actualizar (situación poco probable aquí)
            }

        } catch (Exception e) {
            // Manejar posibles errores de la base de datos
            e.printStackTrace();
            rowsAffected = -1; // Indicar error
        } finally {
            db.close(); // Cerrar la base de datos
        }

        return rowsAffected; // Retorna el número de filas afectadas
    }

    public int deleteProfilePicture(String username) {
        return updateProfilePicture(username, null); // Llama a updateProfilePicture pasando null
    }

    public long insertarReceta(int user_id, String titulo, String fecha_publicacion, String imagen_receta_path,
                               String dificultad, String estacion, String tiempo, String estado_economico,
                               String ingredientes, String preparacion) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put("user_id", user_id);
        values.put("titulo", titulo);
        values.put("fecha_publicacion", fecha_publicacion);
        values.put("imagen_receta", imagen_receta_path);
        values.put("dificultad", dificultad);
        values.put("estacion", estacion);
        values.put("tiempo", tiempo);
        values.put("estado_economico", estado_economico);
        values.put("ingredientes", ingredientes);
        values.put("preparacion", preparacion);

        // Insertar la nueva fila y obtener el ID de la fila insertada
        long newRowId = db.insert("recetas", null, values); // Asegúrate de que el nombre de la tabla sea "recetas"

        db.close(); // Cerrar la conexión a la base de datos

        return newRowId; // Retorna el ID de la fila insertada, o -1 si hay un error
    }
    public int getUserIdByUsername(String username) {
        int userId = -1;
        SQLiteDatabase db = this.getReadableDatabase();

        String[] projection = {"id"}; // Queremos obtener la columna 'id'
        String selection = "username = ?"; // Donde el username coincide
        String[] selectionArgs = {username};

        Cursor cursor = db.query(
                "usuarios",       // La tabla a consultar
                projection,    // Las columnas a retornar
                selection,     // Las columnas para la cláusula WHERE
                selectionArgs, // Los valores para la cláusula WHERE
                null,          // No agrupar las filas
                null,          // No filtrar por grupos de filas
                null           // El orden de clasificación
        );

        if (cursor != null && cursor.moveToFirst()) {
            // Obtener el índice de la columna 'id' y luego su valor entero
            int idColumnIndex = cursor.getColumnIndexOrThrow("id");
            userId = cursor.getInt(idColumnIndex);
        }

        if (cursor != null) {
            cursor.close();
        }
        db.close();

        return userId;
    }


}
