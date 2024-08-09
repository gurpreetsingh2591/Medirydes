package com.macrew.medirydes.interfaces

import com.macrew.medirydes.annotation.FragmentType


interface MainFragmentListener {
    fun showFragment(@FragmentType tag:String, payload:Any?)
    fun popTillFragment( tag:String,  flag:Int)
    fun popTopMostFragment()
    fun backTopMostFragment()
    fun drawerOpen()
}