package com.misut.errands

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_file_list.*
import kotlinx.android.synthetic.main.recycler_file.*

class FileListFragment : Fragment() {
    private lateinit var fileRecyclerAdapter: FileRecyclerAdapter

    companion object Factory {
        private const val ARG_PATH: String = "com.misut.errands.filelist.path"

        fun build(path: String): FileListFragment {
            val args = Bundle()
            val fragment = FileListFragment()

            args.putString(ARG_PATH, path)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_file_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fileRecyclerView.layoutManager = LinearLayoutManager(context)
        fileRecyclerAdapter = FileRecyclerAdapter()
        fileRecyclerView.adapter = fileRecyclerAdapter

        val filePath = arguments?.getString(ARG_PATH)
        if (filePath == null) {
            Toast.makeText(context, "Path shouldn't be null!", Toast.LENGTH_SHORT).show()
            return
        }

        val files = getFileViewsFromFiles(getFilesFromPath(filePath))
        if (files.isEmpty()) {
            emptyFolderLayout.visibility = View.VISIBLE
        } else {
            emptyFolderLayout.visibility = View.INVISIBLE
        }

        fileRecyclerAdapter.updateData(files)
    }
}
