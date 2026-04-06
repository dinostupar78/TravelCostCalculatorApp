package hr.tvz.android.kalkulatorstupar
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import androidx.core.os.LocaleListCompat
import hr.tvz.android.kalkulatorstupar.databinding.ActivityMainBinding
import java.util.Locale

class MainActivity : AppCompatActivity() {

    companion object {
        private const val PREFS_NAME = "app_settings"
        private const val KEY_LANGUAGE = "selected_language"
        private const val LANG_HR = "hr"
        private const val LANG_EN = "en"
        private const val LANG_DE = "de"
    }

    private lateinit var binding: ActivityMainBinding
    private var spinnerTextSizeSp: Float = 16f
    private var spinnerAdapter: ArrayAdapter<String>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        applySavedLanguage()
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)

        setupSpinner()
        setupFontSizeControl()

        binding.tvResultDetails.visibility = View.GONE

        binding.btnCalculate.setOnClickListener {
            calculateResults()
        }

    }

    private fun applySavedLanguage() {
        val savedLanguage = getSharedPreferences(PREFS_NAME, MODE_PRIVATE)
            .getString(KEY_LANGUAGE, LANG_HR) ?: LANG_HR

        AppCompatDelegate.setApplicationLocales(
            LocaleListCompat.forLanguageTags(savedLanguage)
        )
    }

    private fun showLanguageDialog() {
        val languages = arrayOf("Hrvatski", "English", "Deutsch")
        val languageTags = arrayOf(LANG_HR, LANG_EN, LANG_DE)

        val currentLang = resources.configuration.locales[0].language
        val checkedItem = languageTags.indexOf(currentLang).takeIf { it >= 0 } ?: 0

        androidx.appcompat.app.AlertDialog.Builder(this)
            .setTitle(getString(R.string.menu_language))
            .setSingleChoiceItems(languages, checkedItem) { dialog, which ->
                val selectedLanguage = languageTags[which]
                if (selectedLanguage != currentLang) {
                    setAppLanguage(selectedLanguage)
                }
                dialog.dismiss()
            }
            .setNegativeButton(android.R.string.cancel, null)
            .show()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_language, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_language -> {
                showLanguageDialog()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun setAppLanguage(languageTag: String) {
        getSharedPreferences(PREFS_NAME, MODE_PRIVATE)
            .edit()
            .putString(KEY_LANGUAGE, languageTag)
            .apply()

        AppCompatDelegate.setApplicationLocales(
            LocaleListCompat.forLanguageTags(languageTag)
        )
    }

    private fun setupSpinner(){
        val fuelTypes = resources.getStringArray(R.array.fuel_types)
        spinnerAdapter = object : ArrayAdapter<String>(
            this,
            android.R.layout.simple_spinner_item,
            fuelTypes
        ) {
            override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
                val view = super.getView(position, convertView, parent)
                return (view as TextView).apply {
                    val textColorRes = if (position == 0) R.color.textGray else R.color.textDark
                    setTextColor(ContextCompat.getColor(context, textColorRes))
                    textSize = spinnerTextSizeSp
                    setTypeface(typeface, android.graphics.Typeface.NORMAL)
                    setPadding(dpToPx(14), paddingTop, dpToPx(44), paddingBottom)
                }
            }

            override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
                val view = super.getDropDownView(position, convertView, parent)
                return (view as TextView).apply {
                    val textColorRes = if (position == 0) R.color.textGray else R.color.textDark
                    setTextColor(ContextCompat.getColor(context, textColorRes))
                    textSize = spinnerTextSizeSp
                    setPadding(dpToPx(14), dpToPx(12), dpToPx(14), dpToPx(12))
                }
            }
        }
        spinnerAdapter?.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerFuelType.adapter = spinnerAdapter
        binding.spinnerFuelType.setSelection(0)
    }

    private fun setupFontSizeControl() {
        binding.sliderFont.value = 1f

        updateFontSizeUI(1)
        applyFontSize(1)

        binding.sliderFont.addOnChangeListener { _, value, _ ->
            val progress = value.toInt()
            updateFontSizeUI(progress)
            applyFontSize(progress)
        }
    }

    private fun applyFontSize(progress: Int) {
        val labelSize = when (progress) {
            0 -> 16f
            1 -> 18f
            2 -> 20f
            else -> 18f
        }

        val inputSize = when (progress) {
            0 -> 15f
            1 -> 17f
            2 -> 19f
            else -> 17f
        }

        val buttonSize = when (progress) {
            0 -> 16f
            1 -> 18f
            2 -> 20f
            else -> 18f
        }

        val resultSize = when (progress) {
            0 -> 14f
            1 -> 16f
            2 -> 18f
            else -> 16f
        }

        spinnerTextSizeSp = when (progress) {
            0 -> 14f
            1 -> 16f
            2 -> 18f
            else -> 16f
        }

        binding.tvKilometersLabel.textSize = labelSize
        binding.tvLitersLabel.textSize = labelSize
        binding.tvPriceLabel.textSize = labelSize
        binding.tvFuelTypeLabel.textSize = labelSize
        binding.tvFontLabel.textSize = labelSize
        binding.btnCalculate.textSize = buttonSize
        binding.tvResultTitle.textSize = labelSize + 1f
        binding.tvResultDetails.textSize = resultSize

        binding.etKilometers.textSize = inputSize
        binding.etLiters.textSize = inputSize
        binding.etPrice.textSize = inputSize

        binding.tvFontSmall.textSize = resultSize
        binding.tvFontMedium.textSize = resultSize
        binding.tvFontLarge.textSize = resultSize

        spinnerAdapter?.notifyDataSetChanged()
        val selected = binding.spinnerFuelType.selectedItemPosition
        binding.spinnerFuelType.setSelection(selected, false)
    }

    private fun updateFontSizeUI(progress: Int) {
        val activeColor = ContextCompat.getColor(this, R.color.primaryBlue)
        val inactiveColor = ContextCompat.getColor(this, R.color.textGray)

        binding.tvFontSmall.setTextColor(if (progress == 0) activeColor else inactiveColor)
        binding.tvFontMedium.setTextColor(if (progress == 1) activeColor else inactiveColor)
        binding.tvFontLarge.setTextColor(if (progress == 2) activeColor else inactiveColor)
    }

    private fun dpToPx(dp: Int): Int = (dp * resources.displayMetrics.density).toInt()

    private fun calculateResults(){
        val kmInput = binding.etKilometers.text.toString()
        val litersInput = binding.etLiters.text.toString()
        val priceInput = binding.etPrice.text.toString()

        if(kmInput.isEmpty() || litersInput.isEmpty() || priceInput.isEmpty()){
            binding.tvResultDetails.visibility = View.GONE
            Toast.makeText(this, getString(R.string.error_empty_fields), Toast.LENGTH_SHORT).show()
            return
        }

        val kilometers = kmInput.toDoubleOrNull()
        val liters = litersInput.toDoubleOrNull()
        val pricePerLiter = priceInput.toDoubleOrNull()

        if(kilometers == null || liters == null || pricePerLiter == null){
            binding.tvResultDetails.visibility = View.GONE
            Toast.makeText(this, getString(R.string.error_invalid_values), Toast.LENGTH_SHORT).show()
            return
        }

        if(kilometers <= 0.0 || liters <= 0.0 || pricePerLiter <= 0.0){
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
        binding.tvResultDetails.visibility = View.VISIBLE


    }
}