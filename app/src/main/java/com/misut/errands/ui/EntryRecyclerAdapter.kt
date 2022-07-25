package com.misut.errands.ui

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.misut.errands.databinding.RecyclerEntryBinding
import java.nio.file.Path
import kotlin.io.path.fileSize
import kotlin.io.path.isDirectory
import kotlin.io.path.listDirectoryEntries
import kotlin.io.path.Path


class EntryRecyclerAdapter(
    private val onEntryClickListener: ((Path) -> Unit),
    private val onEntryLongClickListener: ((Path) -> Unit),
): RecyclerView.Adapter<EntryRecyclerAdapter.ViewHolder>() {
    private var directoryPath: Path = Path("/storage/emulated/0")
    private val filePaths: List<Path>
        get() = directoryPath.listDirectoryEntries().sorted()

    override fun getItemCount(): Int = directoryPath.listDirectoryEntries().size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(position)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = RecyclerEntryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateData(directoryPath: Path) {
        this.directoryPath = directoryPath
        notifyDataSetChanged()
    }

    inner class ViewHolder(private val binding: RecyclerEntryBinding) : RecyclerView.ViewHolder(binding.root), View.OnClickListener, View.OnLongClickListener {
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
            binding.titleTextView.text = path.fileName.toString()

            if (path.isDirectory()) {
                binding.subtitleTextView.text = "${path.listDirectoryEntries().size} Files"
            } else {
                binding.subtitleTextView.text = "${path.fileSize()} Bytes"
            }
        }
    }
}
