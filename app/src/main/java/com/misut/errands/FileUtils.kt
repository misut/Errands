package com.misut.errands

import java.io.File


fun getFilesFromPath(
    path: String,
    showHiddenFiles: Boolean = false,
    onlyFolders: Boolean = false
): List<File> {
    return File(path).listFiles()
        .filter { showHiddenFiles || !it.name.startsWith(".") }
        .filter { !onlyFolders || it.isDirectory }
        .toList()
}

fun getFileViewsFromFiles(files: List<File>): List<FileView> {
    return files.map {
        FileView(
            it.path,
            it.name,
            FileType.getFileType(it),
            it.length(),
            it.extension,
            it.listFiles().size
        )
    }
}
