package br.edu.utfpr.myeconomy

import android.content.Intent
import android.database.Cursor
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import br.edu.utfpr.myeconomy.database.DatabaseHandler
import br.edu.utfpr.myeconomy.databinding.ActivityHomeBinding
import br.edu.utfpr.usandosqlite.adapter.MyAdapter

class HomeActivity : AppCompatActivity() {
    private lateinit var binding : ActivityHomeBinding
    private lateinit var db : DatabaseHandler

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate( layoutInflater )
        setContentView( binding.root )

        db = DatabaseHandler(this)

        binding.btAdd.setOnClickListener {
            btAddOnClick()
        }

    }

    private fun btAddOnClick() {
        val intent = Intent( this, FormActivity::class.java)
        startActivity( intent )
    }

    private fun btDeleteOnClick(itemId: Int) {
        db.delete(itemId)
        Toast.makeText(this, "Success!!!", Toast.LENGTH_LONG).show()
        onStart()
    }

    override fun onStart() {
        super.onStart()
        val registers : Cursor = db.cursorList()

        val adapter = MyAdapter(this, registers) { itemId ->
            btDeleteOnClick(itemId)
        }
        binding.lvMain.adapter = adapter

        // Calculate total balance
        val totalBalance = calculateTotalBalance(registers)
        updateTotalBalance(totalBalance)
    }

    private fun calculateTotalBalance(cursor: Cursor): Float {
        var total = 0.0f
        val typeIndex = cursor.getColumnIndex("type")
        val valueIndex = cursor.getColumnIndex("value")

        if (cursor.moveToFirst()) {
            do {
                if (typeIndex != -1 && valueIndex != -1) {
                    val type = cursor.getString(typeIndex)
                    val value = cursor.getFloat(valueIndex)

                    total += if (type == "Crédito") {
                        value
                    } else if (type == "Débito") {
                        -value
                    } else {
                        0.0f
                    }
                }
            } while (cursor.moveToNext())
        }
        return total
    }

    private fun updateTotalBalance(balance: Float) {
        val formattedBalance = String.format("%.2f", balance)
        binding.footer.tvTotalBalance.text = "Saldo Final: R$ $formattedBalance"
    }
}