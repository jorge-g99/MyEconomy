package br.edu.utfpr.myeconomy

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.RadioButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import br.edu.utfpr.myeconomy.database.DatabaseHandler
import br.edu.utfpr.myeconomy.databinding.ActivityFormBinding
import br.edu.utfpr.usandosqlite.entity.Register
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class FormActivity : AppCompatActivity() {

    private lateinit var binding : ActivityFormBinding
    private lateinit var db : DatabaseHandler
    private val descriptionsByType = mapOf(
        "Crédito" to listOf("Salário", "Extras"),
        "Débito" to listOf("Alimentação", "Transporte", "Saúde", "Moradia")
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFormBinding.inflate( layoutInflater )
        setContentView(binding.root)

        setSpinners()
        setButtonListener()

        if ( intent.getIntExtra("cod", 0) != 0 ) {
            val type = intent.getStringExtra("type") ?: return
            val description = intent.getStringExtra("description")
            val value = intent.getFloatExtra("value", 0.0f)
            val dateString = intent.getStringExtra("date") ?: ""

            binding.spType.setSelection(descriptionsByType.keys.indexOf(type))

            binding.spType.post {
                binding.spDescription.setSelection(descriptionsByType[type]?.indexOf(description) ?: 0)
            }

            val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            val date = dateFormat.parse(dateString)
            val calendar = Calendar.getInstance().apply { time = date }
            binding.dpDate.updateDate(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH))

            binding.etValue.setText(value.toString())
        }

        db = DatabaseHandler(this)
    }

    private fun setSpinners() {
        // keys de descriptionsByType como types
        val types = descriptionsByType.keys.toList()
        binding.spType.adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, types)

        // values de descriptionsByType como descriptions
        binding.spType.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                val selectedType = types[position]
                val descAdapter = ArrayAdapter(this@FormActivity, android.R.layout.simple_list_item_1, descriptionsByType[selectedType]!!)
                binding.spDescription.adapter = descAdapter
                binding.spDescription.setSelection(0)
            }
            override fun onNothingSelected(parent: AdapterView<*>) {}
        }
    }

    private fun setButtonListener() {
        binding.btSend.setOnClickListener {
            btSendOnClick()
        }
    }

    private fun btSendOnClick() {
        val dtFormatDate = "${binding.dpDate.dayOfMonth}/${binding.dpDate.month + 1}/${binding.dpDate.year}"

        val register = Register(
            intent.getIntExtra("cod", 0),
            binding.spType.selectedItem.toString(),
            binding.spDescription.selectedItem.toString(),
            binding.etValue.text.toString().toFloat(),
            dtFormatDate
        )

        if (register._id == 0) {
            db.insert(register)
        } else {
            db.update(register)
        }

        Toast.makeText(this, "Success!!", Toast.LENGTH_LONG).show()
        finish()
    }
}