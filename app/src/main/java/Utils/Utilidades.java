package Utils;

public class Utilidades {

    //Constantes tabla puntuaciones
    public static final String TABLA_PUNTUACIONES = "Puntuaciones";
    public static final String CAMPO_ID = "Id";
    public static final String CAMPO_NOMBRE = "Nombre";
    public static final String CAMPO_TIEMPO = "Tiempo";
    public static final String CREAR_TABLA_PUNTUACIONES = "CREATE TABLE " +  TABLA_PUNTUACIONES + " (" + CAMPO_ID +
            " INTEGER PRIMARY KEY AUTOINCREMENT," + CAMPO_NOMBRE + " TEXT NOT NULL, " + CAMPO_TIEMPO +  " INTEGER NOT NULL)";
}
