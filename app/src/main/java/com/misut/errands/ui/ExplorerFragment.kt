package com.misut.errands.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.misut.errands.R
import com.misut.errands.databinding.FragmentExplorerBinding
import java.nio.file.Path
import kotlin.io.path.Path
import kotlin.io.path.listDirectoryEntries


class ExplorerFragment : Fragment() {
    private lateinit var binding: FragmentExplorerBinding
    private lateinit var breadcrumbRecyclerAdapter: BreadcrumbRecyclerAdapter
    private lateinit var entryRecyclerAdapter: EntryRecyclerAdapter

    private val args: ExplorerFragmentArgs by navArgs()

    interface OnEntryClickListener {
        fun onClick(path: Path)
        fun onLongClick(path: Path)
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

        binding.explorerToolbar.inflateMenu(R.menu.menu_explorer)
        binding.explorerToolbar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.menuSearch -> {
                    Toast.makeText(context, "Search pressed", Toast.LENGTH_SHORT).show()
                    true
                }
                else -> false
            }
        }

        breadcrumbRecyclerAdapter = BreadcrumbRecyclerAdapter {}
        binding.breadcrumbRecyclerView.adapter = breadcrumbRecyclerAdapter
        binding.breadcrumbRecyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

        val onEntryClickListener = context as OnEntryClickListener
        entryRecyclerAdapter = EntryRecyclerAdapter(
            onEntryClickListener = { onEntryClickListener.onClick(it) },
            onEntryLongClickListener = { onEntryClickListener.onLongClick(it) },
        )

        binding.entryRecyclerView.adapter = entryRecyclerAdapter
        binding.entryRecyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

        val directoryPath = Path(args.currentPath)
        Log.d("Errands", "On directory: $directoryPath")

        if (directoryPath.listDirectoryEntries().isEmpty()) {
            binding.emptyFolderLayout.visibility = View.VISIBLE
        } else {
            binding.emptyFolderLayout.visibility = View.GONE
        }
        entryRecyclerAdapter.updateData(directoryPath)
    }
}
