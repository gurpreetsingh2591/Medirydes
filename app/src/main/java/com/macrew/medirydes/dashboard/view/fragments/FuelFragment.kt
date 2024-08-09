package com.macrew.medirydes.dashboard.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.macrew.medirydes.R
import com.macrew.medirydes.fuel.view.AddFuelReceiptActivity
import com.macrew.medirydes.interfaces.MainFragmentListener
import com.macrew.medirydes.utils.Static
import kotlinx.android.synthetic.main.fragment_fuel.*

class FuelFragment : Fragment() {
    val static = Static()
    private lateinit var mainFragmentListener: MainFragmentListener
    companion object {
        private val bundle = Bundle()
        private var ARG_PARAM = "currencyList"
        fun getInstance(): Fragment {
            return FuelFragment()
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        mainFragmentListener = (activity as MainFragmentListener?)!!
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_fuel, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        // initObserver()
        // dashboardViewModel.callGetDashboardApi()
        val args = arguments
        initUI()
    }

    private fun initUI() {
        btnAddFuel.setOnClickListener {
            AddFuelReceiptActivity.startActivity(activity, "")
        }

    }

}