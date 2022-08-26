package com.drovisfrovis.runningandworkout.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.drovisfrovis.runningandworkout.R
import com.drovisfrovis.runningandworkout.misc.Constants.ACTION_SHOW_TRACKING_FRAGMENT
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        navigateToTrackingFragmentIfNeeded(intent)
        setSupportActionBar(toolbar)
        bottomNavigationView.setupWithNavController(navHostFragment.findNavController())
        bottomNavigationView.setOnNavigationItemReselectedListener { /* NO-OP */ }

        navHostFragment.findNavController().addOnDestinationChangedListener { _, destination, _ ->
            when(destination.id) {
                R.id.runFragment -> {
                    bottomNavigationView.visibility = View.VISIBLE
                }
                R.id.settingsFragment, R.id.statisticsFragment, R.id.plansFragment ->
                    bottomNavigationView.visibility = View.VISIBLE
                else -> bottomNavigationView.visibility = View.GONE
            }
        }
        
    }

    override fun onBackPressed() {
        when(navHostFragment.findNavController().currentDestination?.id) {
            R.id.runFragment -> {
                for(i in 0..navHostFragment.childFragmentManager.backStackEntryCount)
                    navHostFragment.findNavController().popBackStack()
                super.onBackPressed()
            }
            R.id.statisticsFragment, R.id.settingsFragment,R.id.plansFragment -> {
                navHostFragment.findNavController().navigate(R.id.action_global_runFragment)
            }
            else -> {
                super.onBackPressed()
            }
        }

    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        navigateToTrackingFragmentIfNeeded(intent)
    }

    private fun navigateToTrackingFragmentIfNeeded(intent: Intent?){
        if(intent?.action == ACTION_SHOW_TRACKING_FRAGMENT){
            navHostFragment.findNavController().navigate(R.id.action_global_trackingFragment)
        }

    }


}