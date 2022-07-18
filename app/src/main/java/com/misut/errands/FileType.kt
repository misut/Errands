package com.misut.errands

import java.io.File


enum class FileType {
    FILE,
    FOLDER;

    companion object {
        fun getFileType(file: File) = if (file.isDirectory) FOLDER else FILE
    }
}
