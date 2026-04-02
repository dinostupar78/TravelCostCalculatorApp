package hr.tvz.android.kalkulatorstupar
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import hr.tvz.android.kalkulatorstupar.databinding.ActivityMainBinding
import java.util.Locale

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupSpinner()
        // Default state: show only title, no image/details until first valid calculation.
        binding.ivResultIcon.visibility = View.GONE
        binding.tvResultDetails.visibility = View.GONE

        binding.btnCalculate.setOnClickListener {
            calculateResults()
        }

    }

    private fun setupSpinner(){
        val fuelTypes = resources.getStringArray(R.array.fuel_types)
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, fuelTypes)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerFuelType.adapter = adapter
    }

    private fun calculateResults(){
        val kmInput = binding.etKilometers.text.toString()
        val litersInput = binding.etLiters.text.toString()
        val priceInput = binding.etPrice.text.toString()

        if(kmInput.isEmpty() || litersInput.isEmpty() || priceInput.isEmpty()){
            binding.ivResultIcon.visibility = View.GONE
            binding.tvResultDetails.visibility = View.GONE
            Toast.makeText(this, getString(R.string.error_empty_fields), Toast.LENGTH_SHORT).show()
            return
        }

        val kilometers = kmInput.toDoubleOrNull()
        val liters = litersInput.toDoubleOrNull()
        val pricePerLiter = priceInput.toDoubleOrNull()

        if(kilometers == null || liters == null || pricePerLiter == null){
            binding.ivResultIcon.visibility = View.GONE
            binding.tvResultDetails.visibility = View.GONE
            Toast.makeText(this, getString(R.string.error_invalid_values), Toast.LENGTH_SHORT).show()
            return
        }

        if(kilometers <= 0.0 || liters <= 0.0 || pricePerLiter <= 0.0){
            binding.ivResultIcon.visibility = View.GONE
            binding.tvResultDetails.visibility = View.GONE
            Toast.makeText(this, getString(R.string.error_invalid_values), Toast.LENGTH_SHORT).show()
            return
        }

        val fuelConsumption = (liters / kilometers) * 100
        val totalCost = liters * pricePerLiter
        val costPer100km = (totalCost / kilometers) * 100

        val resultText = String.format(
            Locale.getDefault(),
            "%s: %.2f L / 100 km\n%s: %.2f EUR\n%s: %.2f EUR / 100 km",
            getString(R.string.result_consumption), fuelConsumption,
            getString(R.string.result_total_cost), totalCost,
            getString(R.string.result_cost_100), costPer100km
        )

        binding.tvResultDetails.text = resultText
        binding.ivResultIcon.visibility = View.VISIBLE
        binding.tvResultDetails.visibility = View.VISIBLE


    }
}