package com.example.weatherTJ.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.weatherTJ.R

class FavoritesFragment : Fragment() {


    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val favoritesContainer =
            inflater.inflate(R.layout.fragment_favorites, container, false)

        val textView = favoritesContainer.findViewById<TextView>(R.id.text_favorites)

        textView.text = getString(R.string.title_favorites)

        return favoritesContainer
    }
}