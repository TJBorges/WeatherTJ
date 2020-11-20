package com.example.weatherTJ.ui

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.weatherTJ.MyAdapter
import com.example.weatherTJ.R
import com.example.weatherTJ.dataBase.WeatherTJDataBase
import com.example.weatherTJ.manager.OpenWeatherManager
import com.example.weatherTJ.model.City
import com.example.weatherTJ.model.CityDataBase
import com.example.weatherTJ.model.Element
import com.example.weatherTJ.model.Root
import kotlinx.android.synthetic.main.fragment_search.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class SearchFragment : Fragment(), View.OnClickListener {


    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?,
    ): View? {

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

        fabFavorite.setOnClickListener(this)

    }

    private fun clickSearch() {

        activity?.let { closeKeyboard(it) }

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

                                    if (root?.list?.size!! > 0) {

                                        root?.list?.forEach {
                                            elements.add(it)
                                        }

                                        (recyclerView.adapter as MyAdapter).addItems(elements)
                                        recyclerView.layoutManager = LinearLayoutManager(context)
                                        recyclerView.addItemDecoration(MyAdapter.MyItemDecoration(30))

                                        pbSearch.visibility = View.GONE
                                    } else {
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

    fun closeKeyboard(activity: Activity) {
        val imm: InputMethodManager = activity.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        var view = activity.currentFocus

        if (view == null) {
            view = View(activity)
        }
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

    override fun onClick(v: View?) {
        val city = txtSearch.text.toString()
        val service = OpenWeatherManager().getOpenWeatherService()

        val call = service.getCityWeather(city)

        call.enqueue(object : Callback<City> {
            override fun onResponse(call: Call<City>, response: Response<City>) {
                when(response.isSuccessful){
                    true -> {
                        val city = response.body()
                        val db = context?.applicationContext.let { it?.let { it1 ->
                            WeatherTJDataBase.getInstance(it1)
                        } }

                        val cityDataBase = CityDataBase(city!!.id, city!!.name)


                        db?.cityDataBaseDao()?.save(cityDataBase)

                        Toast.makeText(context, "Salvou", Toast.LENGTH_SHORT).show()

                    }
                    false -> {
                        Toast.makeText(context, "Selecione uma Cidade", Toast.LENGTH_SHORT).show()
                    }
                }
            }

            override fun onFailure(call: Call<City>, t: Throwable) {
                Toast.makeText(context, "Faiou2", Toast.LENGTH_SHORT).show()
            }

        })
    }


}