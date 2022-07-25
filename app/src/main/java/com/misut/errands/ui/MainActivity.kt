package com.misut.errands.ui

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.Settings
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.navigation.NavigationBarView
import com.misut.errands.*
import com.misut.errands.databinding.ActivityMainBinding
import com.misut.errands.util.*
import java.nio.file.Path
import kotlin.io.path.Path
import kotlin.io.path.isDirectory


class MainActivity : AppCompatActivity(), ExplorerFragment.OnEntryClickListener {
    private lateinit var binding: ActivityMainBinding

    companion object {
        const val EXTERNAL_STORAGE_PERMISSION_REQUEST_CODE = 12345
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        Log.d("Errands", "App launched")

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.navHostFragment) as NavHostFragment
        val navController = navHostFragment.navController
        binding.navView.setupWithNavController(navController)

        if (!checkPermission()) {
            requestPermission()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_toolbar, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menuSearch -> {
                Toast.makeText(applicationContext, "Search pressed", Toast.LENGTH_SHORT).show()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        if (supportFragmentManager.backStackEntryCount == 0) {
            finish()
        }

        super.onBackPressed()
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
            addEntriesFragment(path)
        } else {
            launchFileIntent(path)
        }
    }

    override fun onLongClick(path: Path) {
        TODO("Not yet implemented")
    }

    private fun addEntriesFragment(path: Path) {
        val explorerFragment = ExplorerFragment.build(path)
        supportFragmentManager.beginTransaction()
            .replace(R.id.navHostFragment, explorerFragment)
            .addToBackStack(path.toString())
            .commit()
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
