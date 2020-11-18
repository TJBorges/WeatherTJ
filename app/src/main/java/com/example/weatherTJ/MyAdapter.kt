package com.example.weatherTJ

import android.graphics.Rect
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.weatherTJ.model.Element
import kotlinx.android.synthetic.main.recyclerview_item.view.*

class MyAdapter(val list: MutableList<Element>?) : RecyclerView.Adapter<MyAdapter.MyViewHolder>() {

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val txtCityName: TextView = itemView.txtCityName
        private val txtCityCod: TextView = itemView.txtCityCod
        private val imgWeather: ImageView = itemView.imgCityWeather

        fun bindView(element: Element) = with(itemView) {
            txtCityName.text = element.name
            txtCityCod.text = element.id.toString()

            Glide.with(context)
                    .load("http://openweathermap.org/img/wn/${element.weather[0].icon}@4x.png")
                    .placeholder(R.drawable.ic_launcher_background)
                    .error(R.drawable.ic_launcher_background)
                    .into(imgWeather)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyAdapter.MyViewHolder {
        return  MyViewHolder(LayoutInflater.from(parent.context)
                            .inflate(R.layout.recyclerview_item, parent, false))
    }

    override fun getItemCount(): Int {
        return list?.size ?: 0
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        when (holder) {
            is MyViewHolder -> {
                if(position < (list?.size ?: 0)) {
                    val element = list?.get(position)
                    if (element != null){
                        holder.bindView(element)
                    }
                }
            }
        }
    }

    fun addItems(newElements: MutableList<Element>?) {
        list?.clear()
        newElements?.forEach{ list?.add(it) }
        notifyDataSetChanged()
    }

    class MyItemDecoration(private val height: Int) : RecyclerView.ItemDecoration() {

        override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
            //super.getItemOffsets(outRect, view, parent, state)
            with(outRect) {
                if (parent.getChildAdapterPosition(view) == 0 ) {
                    top = height
                }
                left = height
                right = height
                bottom = height
            }
        }

    }


}
