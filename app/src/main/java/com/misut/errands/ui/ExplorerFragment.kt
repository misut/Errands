package com.misut.errands.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.misut.errands.databinding.FragmentExplorerBinding
import java.nio.file.Path
import kotlin.io.path.Path
import kotlin.io.path.listDirectoryEntries


class ExplorerFragment : Fragment() {
    private lateinit var binding: FragmentExplorerBinding
    private lateinit var entryRecyclerAdapter: EntryRecyclerAdapter

    interface OnEntryClickListener {
        fun onClick(path: Path)
        fun onLongClick(path: Path)
    }

    companion object Factory {
        private const val ARG_PATH: String = "EntriesFragment.path"

        fun build(directoryPath: Path) = ExplorerFragment().apply {
            arguments = Bundle().apply {
                putString(ARG_PATH, directoryPath.toString())
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentExplorerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val onEntryClickListener = context as OnEntryClickListener
        entryRecyclerAdapter = EntryRecyclerAdapter(
            onEntryClickListener = { onEntryClickListener.onClick(it) },
            onEntryLongClickListener = { onEntryClickListener.onLongClick(it) },
        )

        binding.entryRecyclerView.adapter = entryRecyclerAdapter
        binding.entryRecyclerView.layoutManager = LinearLayoutManager(context)

        val directoryPath = arguments?.getString(ARG_PATH)?.let { Path(it) } ?: Path("/storage/emulated/0")
        Log.d("Errands", "On directory: $directoryPath")

        if (directoryPath.listDirectoryEntries().isEmpty()) {
            binding.emptyFolderLayout.visibility = View.VISIBLE
        } else {
            binding.emptyFolderLayout.visibility = View.INVISIBLE
        }
        entryRecyclerAdapter.updateData(directoryPath)
    }
}
