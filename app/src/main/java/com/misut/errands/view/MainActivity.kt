package com.misut.errands.view

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.Settings
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.misut.errands.*
import com.misut.errands.util.*
import java.nio.file.Path
import kotlin.io.path.Path
import kotlin.io.path.isDirectory


const val EXTERNAL_STORAGE_PERMISSION_REQUEST_CODE = 12345


class MainActivity : AppCompatActivity(), FileListFragment.OnEntryClickListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Log.d("Errands", "App launched")

        if (savedInstanceState == null) {
            val fileListFragment = FileListFragment.build(Path(Environment.getExternalStorageDirectory().absolutePath))

            supportFragmentManager.beginTransaction()
                .add(R.id.container, fileListFragment)
                .addToBackStack(Environment.getExternalStorageDirectory().absolutePath)
                .commit()
        }

        if (!checkPermission()) {
            requestPermission()
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()

        if (supportFragmentManager.backStackEntryCount == 0) {
            finish()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == EXTERNAL_STORAGE_PERMISSION_REQUEST_CODE) {
            if (!checkPermission()) {
                Log.d("Errands", "Permission denied")
            } else {
                Log.d("Errands", "Permission granted")
            }
        }
    }

    override fun onClick(path: Path) {
        if (path.isDirectory()) {
            addFileListFragment(path)
        } else {
            launchFileIntent(path)
        }
    }

    override fun onLongClick(path: Path) {
        TODO("Not yet implemented")
    }

    private fun addFileListFragment(path: Path) {
        val fileListFragment = FileListFragment.build(path)
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.container, fileListFragment)
        fragmentTransaction.addToBackStack(path.toString())
        fragmentTransaction.commit()
    }

    private fun checkPermission(): Boolean {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.R) {
            val readPermission = ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED

            val writePermission = ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED

            return readPermission && writePermission
        }

        return Environment.isExternalStorageManager()
    }

    private fun requestPermission() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.R) {
            requestPermissions(
                arrayOf(
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                ),
                EXTERNAL_STORAGE_PERMISSION_REQUEST_CODE,
            )
            return
        }

        val intent = Intent(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION)
        startActivity(intent)
    }
}
