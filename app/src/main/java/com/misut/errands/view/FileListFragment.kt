package com.misut.errands.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.misut.errands.R
import kotlinx.android.synthetic.main.fragment_file_list.*
import java.nio.file.Path
import kotlin.io.path.Path
import kotlin.io.path.listDirectoryEntries


class FileListFragment : Fragment() {
    private lateinit var fileRecyclerAdapter: FileRecyclerAdapter

    interface OnEntryClickListener {
        fun onClick(path: Path)
        fun onLongClick(path: Path)
    }

    companion object Factory {
        private const val ARG_PATH: String = "FileListFragment.path"

        fun build(directoryPath: Path) = FileListFragment().apply {
            arguments = Bundle().apply {
                putString(ARG_PATH, directoryPath.toString())
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        return inflater.inflate(R.layout.fragment_file_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val onEntryClickListener = context as OnEntryClickListener
        fileRecyclerAdapter = FileRecyclerAdapter(
            onEntryClickListener = { onEntryClickListener.onClick(it) },
            onEntryLongClickListener = { onEntryClickListener.onLongClick(it) },
        )

        fileRecyclerView.adapter = fileRecyclerAdapter
        fileRecyclerView.layoutManager = LinearLayoutManager(context)

        val directoryPath = arguments?.getString(ARG_PATH)?.let { Path(it) } ?: Path("/storage/emulated/0")
        Log.d("Errands", "On directory: $directoryPath")

        if (directoryPath.listDirectoryEntries().isEmpty()) {
            emptyFolderLayout.visibility = View.VISIBLE
        } else {
            emptyFolderLayout.visibility = View.INVISIBLE
        }
        fileRecyclerAdapter.updateData(directoryPath)
    }
}
