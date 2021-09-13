package com.trackmysleepquality.sleeptracker

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.trackmysleepquality.R
import com.trackmysleepquality.convertDurationToFormatted
import com.trackmysleepquality.convertNumericQualityToString
import com.trackmysleepquality.database.SleepNight
import com.trackmysleepquality.databinding.ListItemSleepNightBinding

class SleepNightAdapter : ListAdapter<SleepNight, SleepNightAdapter.ViewHolder>(SleepNightDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    class ViewHolder private constructor(val binding: ListItemSleepNightBinding) : RecyclerView.ViewHolder(binding.root){

        companion object {
             fun from(parent: ViewGroup): ViewHolder {
                 val layoutInflater = LayoutInflater.from(parent.context)
                 val binding = ListItemSleepNightBinding.inflate( layoutInflater,parent,false)
                 return ViewHolder(binding)
            }
        }

        fun bind(item: SleepNight) {
            binding.sleep = item
            binding.executePendingBindings()
        }

    }

}

class SleepNightDiffCallback: DiffUtil.ItemCallback<SleepNight>(){

    // called when item is moved from Last position to First position
    override fun areItemsTheSame(oldItem: SleepNight, newItem: SleepNight): Boolean {
        return oldItem.nightId == newItem.nightId // unique thing in the item
    }

    // called when item data is updated,removed,added
    override fun areContentsTheSame(oldItem: SleepNight, newItem: SleepNight): Boolean {
        return oldItem == newItem
    }

}



//
//
//           Perfect Sample of RecyclerView and ViewHolder
//
//
//class SleepNightAdapter : RecyclerView.Adapter<SleepNightAdapter.ViewHolder>() {
//
//    var data = listOf<SleepNight>()
//        set(value) {
//            field = value
//            notifyDataSetChanged()
//        }
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
//        return ViewHolder.from(parent)
//    }
//
//    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
//        val item = data[position]
//        holder.bind(item)
//    }
//
//    override fun getItemCount(): Int {
//        return data.size
//    }
//
//    class ViewHolder private constructor(itemView: View) : RecyclerView.ViewHolder(itemView){
//
//        val res = itemView.context.resources
//
//        val sleepLength: TextView = itemView.findViewById(R.id.sleep_length)
//        val quality: TextView = itemView.findViewById(R.id.quality_string)
//        val qualityImage: ImageView = itemView.findViewById(R.id.quality_image)
//
//        companion object {
//            fun from(parent: ViewGroup): ViewHolder {
//                val layout = LayoutInflater.from(parent.context)
//                    .inflate(R.layout.list_item_sleep_night, parent, false)
//                return ViewHolder(layout)
//            }
//        }
//
//        fun bind(item: SleepNight){
//            sleepLength.text = convertDurationToFormatted(item.startTimeMilli, item.endTimeMilli, res)
//            quality.text = convertNumericQualityToString(item.sleepQuality, res)
//            qualityImage.setImageResource(
//                when (item.sleepQuality) {
//                    0 -> R.drawable.ic_sleep_0
//                    1 -> R.drawable.ic_sleep_1
//                    2 -> R.drawable.ic_sleep_2
//                    3 -> R.drawable.ic_sleep_3
//                    4 -> R.drawable.ic_sleep_4
//                    5 -> R.drawable.ic_sleep_5
//                    else -> R.drawable.ic_sleep_active
//                }
//            )
//
//        }
//    }
//
//
//}
