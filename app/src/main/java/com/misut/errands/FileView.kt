package com.misut.errands

data class FileView(
    val path: String,
    val name: String,
    val fileType: FileType,
    val sizeInBytes: Long,
    val extension: String = "",
    val subFiles: Int = 0
)
