package com.misut.errands.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.misut.errands.databinding.RecyclerBreadcrumbBinding
import java.nio.file.Path
import kotlin.io.path.Path
import kotlin.io.path.listDirectoryEntries
import kotlin.io.path.name


class BreadcrumbRecyclerAdapter(
    private val onItemClickListener: ((Path) -> Unit)
) : RecyclerView.Adapter<BreadcrumbRecyclerAdapter.ViewHolder>() {
    private var directoryPath: Path = Path("/storage/emulated/0")
    private val directoryPaths: List<Path>
        get() = directoryPath.toList()

    override fun getItemCount(): Int = directoryPaths.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(position)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = RecyclerBreadcrumbBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    inner class ViewHolder(private val binding: RecyclerBreadcrumbBinding) : RecyclerView.ViewHolder(binding.root), View.OnClickListener {
        init {
            itemView.setOnClickListener(this)
        }

        private val directoryName: String
            get() = directoryPaths[adapterPosition].toString()

        override fun onClick(v: View?) {
            onItemClickListener.invoke(directoryPaths[adapterPosition])
        }

        fun bind(position: Int) {
            binding.nameTextView.text = directoryName
        }
    }
}
