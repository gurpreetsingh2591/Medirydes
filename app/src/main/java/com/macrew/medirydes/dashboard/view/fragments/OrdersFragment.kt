package com.macrew.medirydes.dashboard.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.macrew.medirydes.R
import com.macrew.medirydes.annotation.FragmentType
import com.macrew.medirydes.interfaces.MainFragmentListener
import com.macrew.medirydes.utils.Static
import kotlinx.android.synthetic.main.fragment_orders.*

class OrdersFragment : Fragment() {
    val static = Static()

    //private lateinit var dashboardViewModel: DashboardViewModel
    private lateinit var mainFragmentListener: MainFragmentListener

    companion object {
        private val bundle = Bundle()
        private var ARG_PARAM = "currencyList"
        fun getInstance(): Fragment {
            return OrdersFragment()
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
        return inflater.inflate(R.layout.fragment_orders, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val args = arguments
        initUI()
    }

    private fun initUI() {
        cvCurrentSchedule.setOnClickListener {
            mainFragmentListener.showFragment(FragmentType.TRIP_DETAIL_FRAGMENT, "")
        }
    }
}