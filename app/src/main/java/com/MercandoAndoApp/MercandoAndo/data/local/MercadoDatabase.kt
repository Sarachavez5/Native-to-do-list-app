package com.MercandoAndoApp.MercandoAndo.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.MercandoAndoApp.MercandoAndo.data.local.dao.ItemMercadoDao
import com.MercandoAndoApp.MercandoAndo.data.local.dao.ListaMercadoDao
import com.MercandoAndoApp.MercandoAndo.data.local.dao.UsuarioDao
import com.MercandoAndoApp.MercandoAndo.data.model.ItemMercado
import com.MercandoAndoApp.MercandoAndo.data.model.ListaMercado
import com.MercandoAndoApp.MercandoAndo.data.model.Usuario

/**
 * Base de datos principal de la aplicación usando Room
 */
@Database(
    entities = [
        Usuario::class,
        ListaMercado::class,
        ItemMercado::class
    ],
    version = 1,
    exportSchema = false
)
abstract class MercadoDatabase : RoomDatabase() {
    
    abstract fun usuarioDao(): UsuarioDao
    abstract fun listaMercadoDao(): ListaMercadoDao
    abstract fun itemMercadoDao(): ItemMercadoDao
    
    companion object {
        @Volatile
        private var INSTANCE: MercadoDatabase? = null
        
        fun getInstance(context: Context): MercadoDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    MercadoDatabase::class.java,
                    "mercado_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}

