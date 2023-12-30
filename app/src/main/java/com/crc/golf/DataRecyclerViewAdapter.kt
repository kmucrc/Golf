package com.crc.golf

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.crc.golf.base.MyApplication
import com.crc.golf.databinding.ItemDataBinding
import com.crc.golf.db.GolfEntity

class DataRecyclerViewAdapter(private val dataList: ArrayList<GolfEntity>, private val listener : OnItemClickListener, private val buttonListener: OnItemClickListener) : RecyclerView.Adapter<DataRecyclerViewAdapter.MyViewHolder>()  {
    inner class MyViewHolder(binding: ItemDataBinding) : RecyclerView.ViewHolder(binding.root) {

        val tvTitle = binding.tvTitle
        val tvDownload = binding.tvDownloaded
        val btDownload = binding.btDownload
        val tvType = binding.tvType

        val root = binding.root
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding : ItemDataBinding = ItemDataBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val swingData = dataList[position]

        holder.tvTitle.text = swingData.createddate
        holder.tvType.text = swingData.type

        if(swingData.download) {
            holder.tvDownload.text = MyApplication.ApplicationContext().getString(R.string.app_data_downloaded)
            holder.btDownload.isEnabled = false
        } else {
            holder.tvDownload.text = MyApplication.ApplicationContext().getString(R.string.app_data_download)
        }

        holder.btDownload.setOnClickListener {
            buttonListener.onDataButtonClick(position)
        }

        holder.root.setOnClickListener {
            listener.onDataItemClick(position)
            false
        }
    }

    override fun getItemCount(): Int {
        return dataList.size
    }
}