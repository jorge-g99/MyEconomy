package br.edu.utfpr.usandosqlite.adapter

import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import br.edu.utfpr.myeconomy.FormActivity
import br.edu.utfpr.myeconomy.R
import br.edu.utfpr.usandosqlite.entity.Register
import br.edu.utfpr.myeconomy.database.DatabaseHandler.Companion.COD
import br.edu.utfpr.myeconomy.database.DatabaseHandler.Companion.DATE
import br.edu.utfpr.myeconomy.database.DatabaseHandler.Companion.DESCRIPTION
import br.edu.utfpr.myeconomy.database.DatabaseHandler.Companion.TYPE
import br.edu.utfpr.myeconomy.database.DatabaseHandler.Companion.VALUE

class MyAdapter(val context : Context, val cursor : Cursor, private val onDeleteItem: (Int) -> Unit) : BaseAdapter() {
    override fun getCount(): Int {
        return cursor.count
    }

    override fun getItem(position: Int): Any {
        cursor.moveToPosition( position )
        val register = Register(
            cursor.getInt( COD ),
            cursor.getString( TYPE ),
            cursor.getString( DESCRIPTION ),
            cursor.getFloat( VALUE ),
            cursor.getString( DATE ),
        )
        return register
    }

    override fun getItemId(position: Int): Long {
        cursor.moveToPosition( position )
        return cursor.getLong( COD )
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val inflater = context.getSystemService( Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = inflater.inflate( R.layout.list_items, null)

        val tvDescription = view.findViewById<TextView>( R.id.tvDescription )
        val tvValue = view.findViewById<TextView>( R.id.tvValue )
        val tvDate = view.findViewById<TextView>( R.id.tvDate )
        val btEditItem = view.findViewById<ImageButton>( R.id.btEditItem )
        val btDelete = view.findViewById<ImageButton>( R.id.btDelete )

        val ivExpense = view.findViewById<ImageView>(R.id.ivExpense)
        val ivRevenue = view.findViewById<ImageView>(R.id.ivRevenue)

        cursor.moveToPosition( position )

        val type = cursor.getString( TYPE )

        when (type) {
            "Débito" -> {
                ivExpense.visibility = View.VISIBLE
                ivRevenue.visibility = View.GONE
            }
            else -> {
                ivExpense.visibility = View.GONE
                ivRevenue.visibility = View.VISIBLE
            }
        }

        tvDescription.setText( cursor.getString( DESCRIPTION ) )
        tvValue.setText( cursor.getFloat( VALUE ).toString() )
        tvDate.setText( cursor.getString( DATE ) )

        btEditItem.setOnClickListener {
            cursor.moveToPosition( position )
            val intent = Intent( context, FormActivity::class.java )

            intent.putExtra( "cod", cursor.getInt( COD ) )
            intent.putExtra( "type", cursor.getString( TYPE ) )
            intent.putExtra( "description", cursor.getString( DESCRIPTION ) )
            intent.putExtra( "value", cursor.getFloat( VALUE ) )
            intent.putExtra( "date", cursor.getString( DATE ) )

            context.startActivity( intent )
        }

        btDelete.setOnClickListener {
            cursor.moveToPosition(position)
            val itemId = cursor.getInt(COD)
            onDeleteItem(itemId)
        }

        return view
    }

}