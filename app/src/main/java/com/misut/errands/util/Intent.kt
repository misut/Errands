package com.misut.errands.util

import android.content.Context
import android.content.Intent
import androidx.core.content.FileProvider
import java.nio.file.Path


fun Context.launchFileIntent(path: Path) {
    val intent = Intent(Intent.ACTION_VIEW)
    intent.data = FileProvider.getUriForFile(this, packageName, path.toFile())
    intent.flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
    startActivity(Intent.createChooser(intent, "Select Application"))
}
