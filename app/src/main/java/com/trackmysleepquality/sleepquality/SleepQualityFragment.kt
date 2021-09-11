package com.trackmysleepquality.sleepquality

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.trackmysleepquality.R
import com.trackmysleepquality.database.SleepDatabase
import com.trackmysleepquality.databinding.FragmentSleepQualityBinding

class SleepQualityFragment : Fragment() {

    private lateinit var binding: FragmentSleepQualityBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Get a reference to the binding object and inflate the fragment views.
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_sleep_quality, container, false)

        val application = requireNotNull(this.activity).application

        val arguments = SleepQualityFragmentArgs.fromBundle(requireArguments())

        val dataSource = SleepDatabase.getInstance(application).sleepDatabaseDao

        val viewModelFactory = SleepQualityViewModelFactory(arguments.sleepNightKey, dataSource)

        val sleepQualityViewModel = ViewModelProvider(this, viewModelFactory).get(SleepQualityViewModel::class.java)

        binding.sleepQualityViewModel = sleepQualityViewModel

        sleepQualityViewModel.navigateToSleepTracker.observe(viewLifecycleOwner,  Observer {
            if (it == true) { // Observed state is true.
                findNavController().navigate(
                    SleepQualityFragmentDirections.actionSleepQualityFragmentToSleepTrackerFragment())
                sleepQualityViewModel.doneNavigating()
            }
        })

        return binding.root
    }

}