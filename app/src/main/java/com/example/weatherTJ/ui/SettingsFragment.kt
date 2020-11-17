package com.example.weatherTJ.ui

import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.weatherTJ.R

class SettingsFragment : Fragment() {

    private lateinit var prefs: SharedPreferences

    private lateinit var rgTemperature: RadioGroup
    private lateinit var rbCelcius: RadioButton
    private lateinit var rbFahrenheit: RadioButton

    private lateinit var rgLanguage: RadioGroup
    private lateinit var rbPortuguese: RadioButton
    private lateinit var rbEnglish: RadioButton

    private var temperatureUnit = ""
    private var language = ""


    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {

        val settingsContainer =
            inflater.inflate(R.layout.fragment_settings, container, false)

        val textView = settingsContainer.findViewById<TextView>(R.id.text_settings)

        textView.text = getString(R.string.title_settings)

        return settingsContainer
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        prefs = view.context.getSharedPreferences("my_app_prefs", 0)

        rbCelcius = view.findViewById(R.id.rbC)
        rbFahrenheit = view.findViewById(R.id.rbF)
        rbPortuguese = view.findViewById(R.id.rbPortuguese)
        rbEnglish = view.findViewById(R.id.rbEnglish)


        temperatureUnit = prefs?.getString("temperature_unit", "F").toString()
        when(temperatureUnit){
            "C" -> rbCelcius.isChecked = true
            "F" -> rbFahrenheit.isChecked = true
        }

        language = prefs?.getString("language", "EN").toString()
        when(language){
            "PT" -> rbPortuguese.isChecked = true
            "EN" -> rbEnglish.isChecked = true
        }

        rgTemperature = view.findViewById(R.id.rgTemperature)
        rgLanguage = view.findViewById(R.id.rgLanguage)

        rgTemperature.setOnCheckedChangeListener { view, id ->
            val radioButton = view.findViewById<RadioButton>(id)
            val editor = prefs?.edit()

            if (radioButton.isChecked) {
                when (radioButton.id) {
                    R.id.rbC -> editor?.apply {putString("temperature_unit", "C").apply() }
                    R.id.rbF -> editor?.apply {putString("temperature_unit", "F").apply() }
                }
                Toast.makeText(this.context, "Saved", Toast.LENGTH_SHORT).show()
            }
        }

        rgLanguage.setOnCheckedChangeListener { view, id ->
            val radioButton = view.findViewById<RadioButton>(id)
            val editor = prefs?.edit()

            if (radioButton.isChecked) {
                when (radioButton.id) {
                    R.id.rbPortuguese -> editor?.apply {putString("language", "PT").apply() }
                    R.id.rbEnglish -> editor?.apply {putString("language", "EN").apply() }
                }
                Toast.makeText(this.context, "Saved", Toast.LENGTH_SHORT).show()
            }
        }
    }


}