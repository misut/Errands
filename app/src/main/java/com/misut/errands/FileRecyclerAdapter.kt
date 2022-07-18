package com.misut.errands

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.recycler_file.view.*

class FileRecyclerAdapter: RecyclerView.Adapter<FileRecyclerAdapter.ViewHolder>() {
    var fileList = listOf<FileView>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.recycler_file, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = fileList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(position)

    fun updateData(fileList: List<FileView>) {
        this.fileList = fileList
        notifyDataSetChanged()
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(position: Int) {
            val fileView = fileList[position]
            itemView.nameTextView.text = fileView.name

            when (fileView.fileType) {
                FileType.FOLDER -> {
                    itemView.folderTextView.visibility = View.VISIBLE
                    itemView.folderTextView.text = "(${fileView.subFiles} files)"
                    itemView.totalSizeTextView.visibility = View.GONE
                }
                FileType.FILE -> {
                    itemView.folderTextView.visibility = View.GONE
                    itemView.totalSizeTextView.visibility = View.VISIBLE
                    itemView.totalSizeTextView.text = "${fileView.sizeInBytes} bytes"
                }
            }
        }
    }
}
