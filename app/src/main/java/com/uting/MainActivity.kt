package com.uting

import androidx.appcompat.widget.Toolbar
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.uting.base.BaseActivity
import com.uting.manager.PlayServiceManager

class MainActivity : BaseActivity() {

    private lateinit var mAppBarConfiguration : AppBarConfiguration

    override fun getContentView(): Int {
        return R.layout.activity_main
    }

    override fun setUpView() {
        super.setUpView()
        mToolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(mToolbar)

        val host: NavHostFragment = supportFragmentManager
            .findFragmentById(R.id.my_nav_host_fragment) as NavHostFragment? ?: return
        val navController = host.navController
        mAppBarConfiguration = AppBarConfiguration(setOf(R.id.home_dest, R.id.mine_dest))

        setupActionBarWithNavController(navController, mAppBarConfiguration)

        setupBottomNavMenu(navController)

        PlayServiceManager.startPlayService(this)
    }

    private fun setupBottomNavMenu(navController: NavController) {
        val bottomNav = findViewById<BottomNavigationView>(R.id.bottom_nav_view)
        bottomNav?.setupWithNavController(navController)
    }

    override fun onSupportNavigateUp(): Boolean {
        return findNavController(R.id.my_nav_host_fragment).navigateUp(mAppBarConfiguration)
    }
}
