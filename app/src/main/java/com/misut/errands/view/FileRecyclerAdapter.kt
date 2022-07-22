package com.misut.errands.view

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.misut.errands.R
import com.misut.errands.infrastructure.LocalFileService
import kotlinx.android.synthetic.main.recycler_file.view.*
import java.nio.file.NoSuchFileException
import java.nio.file.Path
import kotlin.io.path.fileSize
import kotlin.io.path.isDirectory
import kotlin.io.path.listDirectoryEntries


class FileRecyclerAdapter: RecyclerView.Adapter<FileRecyclerAdapter.ViewHolder>() {
    var paths: List<Path> = emptyList()

    override fun getItemCount(): Int = paths.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.recycler_file, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(position)

    @SuppressLint("NotifyDataSetChanged")
    fun updateData(paths: List<Path>) {
        val upperPath = if (paths.isEmpty()) { this.paths[1].parent } else { paths[0].parent.parent }
        this.paths = listOf<Path>(upperPath) + paths
        Log.d("Errands", "Upper Path: $upperPath")
        notifyDataSetChanged()
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(p0: View?) {
            val path = paths[adapterPosition]
            if (!path.isDirectory()) {
                Log.d("Errands", "Clicked a file(${path.fileName})")
                return
            }

            val localFileService = LocalFileService()
            val olderPath = paths
            try {
                updateData(localFileService.listPaths(path))
            } catch (err: NoSuchFileException) {
                paths = olderPath
                Log.d("Errands", "Cannot access $path")
            }

            Log.d("Errands", "Clicked a directory(${path.fileName})")
        }

        fun bind(position: Int) {
            val path = paths[position]
            if (position == 0) {
                itemView.titleTextView.text = ".."
                itemView.subtitleTextView.text = ""
                Log.d("Errands", "${path.fileName}")
                return
            }
            itemView.titleTextView.text = path.fileName.toString()

            if (path.isDirectory()) {
                itemView.subtitleTextView.text = "${path.listDirectoryEntries().size} Files"
            } else {
                itemView.subtitleTextView.text = "${path.fileSize()} Bytes"
            }
        }
    }
}
