package com.trackmysleepquality.sleeptracker

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.trackmysleepquality.R
import com.trackmysleepquality.database.SleepNight
import com.trackmysleepquality.databinding.ListItemSleepNightBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.ClassCastException

private const val ITEM_VIEW_TYPE_HEADER = 0
private const val ITEM_VIEW_TYPE_SLEEPNIGHT = 1

class SleepNightAdapter(val clickListener: SleepNightListener) : ListAdapter<DataItem, RecyclerView.ViewHolder>(SleepNightDiffCallback()) {

    private val adapterScope = CoroutineScope(Dispatchers.Default)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when(viewType){
            ITEM_VIEW_TYPE_HEADER -> TextViewHolder.from(parent)
            ITEM_VIEW_TYPE_SLEEPNIGHT -> ViewHolder.from(parent)
            else -> throw ClassCastException("Unknown viewType ${viewType}")
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when(getItem(position)){
            is DataItem.Header -> ITEM_VIEW_TYPE_HEADER
            is DataItem.SleepNightItem -> ITEM_VIEW_TYPE_SLEEPNIGHT
        }
    }

    fun addHeaderAndSubmitList(list: List<SleepNight>?){
        adapterScope.launch {
            val items = when(list){
                null -> listOf(DataItem.Header)
                else -> listOf(DataItem.Header) + list.map { DataItem.SleepNightItem(it) }
            }
            withContext(Dispatchers.Main){
                submitList(items)
            }
        }
    }
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(holder){
            is ViewHolder -> {
                val nightItem = getItem(position) as DataItem.SleepNightItem
                holder.bind(nightItem.sleepNight,clickListener)
            }
        }

    }

    class ViewHolder private constructor(val binding: ListItemSleepNightBinding) : RecyclerView.ViewHolder(binding.root){

        companion object {
             fun from(parent: ViewGroup): ViewHolder {
                 val layoutInflater = LayoutInflater.from(parent.context)
                 val binding = ListItemSleepNightBinding.inflate( layoutInflater,parent,false)
                 return ViewHolder(binding)
            }
        }

        fun bind(item: SleepNight, clickListener: SleepNightListener) {
            binding.sleep = item
            binding.clickListener = clickListener
            binding.executePendingBindings()
        }

    }

    class TextViewHolder(view: View): RecyclerView.ViewHolder(view) {
        companion object {
            fun from(parent: ViewGroup): TextViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val view = layoutInflater.inflate(R.layout.header, parent, false)
                return TextViewHolder(view)
            }
        }
    }

}

class SleepNightDiffCallback: DiffUtil.ItemCallback<DataItem>(){

    // called when item is moved from Last position to First position
    override fun areItemsTheSame(oldItem: DataItem, newItem: DataItem): Boolean {
        return oldItem.id == newItem.id // unique thing in the item
    }

    // called when item data is updated,removed,added
    override fun areContentsTheSame(oldItem: DataItem, newItem: DataItem): Boolean {
        return oldItem == newItem
    }

}

// CallBack Way
class SleepNightListener(val clickListener:(nightId: Long) ->Unit){
    fun onClick (night:SleepNight) {
        clickListener(night.nightId)
    }
}

// it is closed type and all data types must be defined here
sealed class DataItem{

    data class SleepNightItem(val sleepNight: SleepNight): DataItem()
    {
        override val id: Long
            get() = sleepNight.nightId
    }

    object Header: DataItem() // Since it does not have any data so '''object''' data type is ok
    {
        override val id: Long
            get() = Long.MIN_VALUE
    }


    // this is because the DiffUtil has to compare the ID's
    // so that's why we implemented
    abstract val id : Long
}
//
//
////////////////////////////////////////////////////////////////////////////////////////////////////
//
//
//
//
//         1:  Perfect Sample of RecyclerView and ViewHolder with Specific DataType
//
//
//
//
//
//class SleepNightAdapter(val clickListener: SleepNightListener) : ListAdapter<SleepNight, SleepNightAdapter.ViewHolder>(SleepNightDiffCallback()) {
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
//        return ViewHolder.from(parent)
//    }
//
//    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
//        val item = getItem(position)
//        holder.bind(item!!,clickListener)
//    }
//
//    class ViewHolder private constructor(val binding: ListItemSleepNightBinding) : RecyclerView.ViewHolder(binding.root){
//
//        companion object {
//            fun from(parent: ViewGroup): ViewHolder {
//                val layoutInflater = LayoutInflater.from(parent.context)
//                val binding = ListItemSleepNightBinding.inflate( layoutInflater,parent,false)
//                return ViewHolder(binding)
//            }
//        }
//
//        fun bind(item: SleepNight, clickListener: SleepNightListener) {
//            binding.sleep = item
//            binding.clickListener = clickListener
//            binding.executePendingBindings()
//        }
//
//    }
//
//}
//
//class SleepNightDiffCallback: DiffUtil.ItemCallback<SleepNight>(){
//
//    // called when item is moved from Last position to First position
//    override fun areItemsTheSame(oldItem: SleepNight, newItem: SleepNight): Boolean {
//        return oldItem.nightId == newItem.nightId // unique thing in the item
//    }
//
//    // called when item data is updated,removed,added
//    override fun areContentsTheSame(oldItem: SleepNight, newItem: SleepNight): Boolean {
//        return oldItem == newItem
//    }
//
//}
//
//// CallBack Way
//class SleepNightListener(val clickListener:(nightId: Long) ->Unit){
//    fun onClick (night:SleepNight) {
//        clickListener(night.nightId)
//    }
//}
//
//
//
////////////////////////////////////////////////////////////////////////////////////////////////////
//
//
//
//
//         2:  Perfect Sample of RecyclerView and ViewHolder
//
//
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
