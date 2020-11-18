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
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.weatherTJ.MyAdapter
import com.example.weatherTJ.R
import com.example.weatherTJ.manager.OpenWeatherManager
import com.example.weatherTJ.model.City
import com.example.weatherTJ.model.Element
import com.example.weatherTJ.model.Root
import kotlinx.android.synthetic.main.fragment_search.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class SearchFragment : Fragment() {


    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?): View? {

        val searchContainer = inflater.inflate(R.layout.fragment_search, container, false)

        val btnSearch = searchContainer.findViewById<Button>(R.id.btnSearch)
        val txtSearch = searchContainer.findViewById<TextView>(R.id.txtSearch)

        btnSearch.setOnClickListener {
            clickSearch()
        }

        return searchContainer

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView.adapter = MyAdapter(mutableListOf())

    }

    private fun clickSearch() {
        if(txtSearch.text.toString().trim().isEmpty()) {
            Toast.makeText(this.requireContext(), "Cidade Invalida", Toast.LENGTH_SHORT).show()
        }
        else {
            when {
                !isConnectivityAvaliable(this.requireContext()) ->
                    Toast.makeText(this.requireContext(), getText(R.string.toastConectiveFail), Toast.LENGTH_SHORT).show()
                else -> {

                    pbSearch.visibility = View.VISIBLE
                    val city = txtSearch.text.toString()
                    val service = OpenWeatherManager().getOpenWeatherService()

                    val call = service.getTemperatures(city)


                    call.enqueue(object : Callback<Root> {
                        override fun onResponse(call: Call<Root>, response: Response<Root>) {
                            when (response.isSuccessful) {
                                true -> {
                                    val root = response.body()
                                    val elements = mutableListOf<Element>()

                                    if(root?.list?.size!! > 0) {

                                        root?.list?.forEach {
                                            elements.add(it)
                                        }

                                        (recyclerView.adapter as MyAdapter).addItems(elements)
                                        recyclerView.layoutManager = LinearLayoutManager(context)
                                        recyclerView.addItemDecoration(MyAdapter.MyItemDecoration(30))

                                        pbSearch.visibility = View.GONE
                                    }
                                    else{
                                        Toast.makeText(context, "Cidade nao encontrada", Toast.LENGTH_SHORT).show()
                                        pbSearch.visibility = View.GONE
                                    }
                                }
                                else -> {
                                    Toast.makeText(context, "Cidade nao encontrada", Toast.LENGTH_SHORT).show()
                                    pbSearch.visibility = View.GONE
                                }
                            }
                        }

                        override fun onFailure(call: Call<Root>, t: Throwable) {
                            TODO("Not yet implemented")
                        }
                    })
                }
            }
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