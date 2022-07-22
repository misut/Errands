package com.misut.errands.infrastructure

import com.misut.errands.domain.FileService
import java.nio.file.Path
import kotlin.io.path.isDirectory
import kotlin.io.path.listDirectoryEntries


class LocalFileService: FileService() {
    override fun listPaths(dirPath: Path): List<Path> {
        if (!dirPath.isDirectory()) {
            return emptyList()
        }

        return dirPath.listDirectoryEntries().sorted()
    }
}
