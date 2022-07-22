package com.misut.errands.view

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.misut.errands.R
import kotlinx.android.synthetic.main.recycler_file.view.*
import java.nio.file.Path
import kotlin.io.path.fileSize
import kotlin.io.path.isDirectory
import kotlin.io.path.listDirectoryEntries
import kotlin.io.path.Path


class FileRecyclerAdapter(
    private val onEntryClickListener: ((Path) -> Unit),
    private val onEntryLongClickListener: ((Path) -> Unit),
): RecyclerView.Adapter<FileRecyclerAdapter.ViewHolder>() {
    var directoryPath: Path = Path("/storage/emulated/0")

    val filePaths: List<Path>
        get() = directoryPath.listDirectoryEntries()

    override fun getItemCount(): Int = directoryPath.listDirectoryEntries().size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.recycler_file, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(position)

    @SuppressLint("NotifyDataSetChanged")
    fun updateData(directoryPath: Path) {
        this.directoryPath = directoryPath
        notifyDataSetChanged()
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener, View.OnLongClickListener {
        init {
            itemView.setOnClickListener(this)
            itemView.setOnLongClickListener(this)
        }

        private val filePath: Path
            get() = filePaths[adapterPosition]

        override fun onClick(v: View?) {
            onEntryClickListener.invoke(filePath)
        }

        override fun onLongClick(v: View?): Boolean {
            onEntryLongClickListener.invoke(filePath)
            return true
        }

        fun bind(position: Int) {
            val path = filePaths[position]
            itemView.titleTextView.text = path.fileName.toString()

            if (path.isDirectory()) {
                itemView.subtitleTextView.text = "${path.listDirectoryEntries().size} Files"
            } else {
                itemView.subtitleTextView.text = "${path.fileSize()} Bytes"
            }
        }
    }
}
