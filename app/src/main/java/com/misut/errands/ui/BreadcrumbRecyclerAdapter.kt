package com.misut.errands.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.misut.errands.databinding.RecyclerBreadcrumbBinding
import java.nio.file.Path
import kotlin.io.path.Path
import kotlin.io.path.listDirectoryEntries


class BreadcrumbRecyclerAdapter(
    private val onItemClickListener: ((Path) -> Unit)
) : RecyclerView.Adapter<BreadcrumbRecyclerAdapter.ViewHolder>() {
    private var directoryPath: Path = Path("/storage/emulated/0")
    private val filePaths: List<Path>
        get() = directoryPath.listDirectoryEntries()

    override fun getItemCount(): Int = filePaths.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(position)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = RecyclerBreadcrumbBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    inner class ViewHolder(private val binding: RecyclerBreadcrumbBinding) : RecyclerView.ViewHolder(binding.root), View.OnClickListener {
        init {
            itemView.setOnClickListener(this)
        }

        private val filePath: Path
            get() = filePaths[adapterPosition]

        override fun onClick(v: View?) {
            onItemClickListener.invoke(filePath)
        }

        fun bind(position: Int) {
            val path: Path = filePaths[position]
            binding.nameTextView.text = path.fileName.toString()
        }
    }
}
