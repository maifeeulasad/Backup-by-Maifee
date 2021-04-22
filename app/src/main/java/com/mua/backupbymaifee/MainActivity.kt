package com.mua.backupbymaifee

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import com.mua.backupbymaifee.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var navHostFragment: NavHostFragment
    private lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = initBinding()
        initNav(binding)
    }

    private fun initBinding(): ActivityMainBinding {
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        return binding
    }

    private fun initNav(binding: ActivityMainBinding) {
        setSupportActionBar(binding.toolbar)
        supportActionBar!!.setDisplayShowTitleEnabled(false)

        navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment?
            ?: return

        with(navHostFragment.navController) {
            appBarConfiguration = AppBarConfiguration(graph)
            setupActionBarWithNavController(this, appBarConfiguration)
        }
    }


}