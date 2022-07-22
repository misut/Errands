package com.misut.errands.domain

import java.nio.file.Path


abstract class FileService {
    abstract fun listPaths(dirPath: Path): List<Path>
}
