package com.example.weatherTJ.ui

import android.annotation.SuppressLint
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.weatherTJ.R
import kotlinx.android.synthetic.main.fragment_search.*


class SearchFragment : Fragment() {


    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?): View? {

        val searchContainer =
                inflater.inflate(R.layout.fragment_search, container, false)

        val btnPesquisa = searchContainer.findViewById<Button>(R.id.btnSearch)

        btnPesquisa.setOnClickListener {
            clickLabel()
        }

        return searchContainer
    }

    private fun clickLabel() {
        when{
            !isConnectivityAvaliable(this.requireContext()) ->
                Toast.makeText(this.requireContext(), resources.getString(R.string.toastConectiveFail), Toast.LENGTH_SHORT).show()
            else ->
                
                Toast.makeText(this.requireContext(), "Conectado: ${txtSearch.text}" , Toast.LENGTH_SHORT).show()
        }
    }

    @SuppressLint("WrongConstant")
    private fun isConnectivityAvaliable(context: Context) : Boolean {
        var result = false
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            cm.getNetworkCapabilities(cm.activeNetwork)?.run {
                result = when {
                    hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                    hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                    else -> false
                }
            }
        }
        return result
    }



}