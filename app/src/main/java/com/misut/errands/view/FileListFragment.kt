package com.misut.errands.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.misut.errands.R
import com.misut.errands.infrastructure.LocalFileService
import kotlinx.android.synthetic.main.fragment_file_list.*
import java.nio.file.Path
import kotlin.io.path.Path


class FileListFragment : Fragment() {
    private lateinit var fileRecyclerAdapter: FileRecyclerAdapter

    companion object Factory {
        private const val ARG_PATH: String = "FileListFragment.path"

        fun build(path: Path) = FileListFragment().apply {
            arguments = Bundle().apply {
                putString(ARG_PATH, path.toString())
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

        fileRecyclerView.layoutManager = LinearLayoutManager(context)
        fileRecyclerAdapter = FileRecyclerAdapter()
        fileRecyclerView.adapter = fileRecyclerAdapter

        val path = arguments?.getString(ARG_PATH)?.let { Path(it) } ?: Path("/storage/emulated/0")
        Log.d("Errands", "On path: $path")

        val localFileService = LocalFileService()
        val paths = localFileService.listPaths(path)
        if (paths.isEmpty()) {
            emptyFolderLayout.visibility = View.VISIBLE
        } else {
            emptyFolderLayout.visibility = View.INVISIBLE
        }
        fileRecyclerAdapter.updateData(paths)
    }
}
