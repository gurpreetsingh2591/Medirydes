package com.macrew.medirydes.interfaces

import androidx.fragment.app.Fragment

interface HomeFragmentInterface {
    fun showFragment(fragment: Fragment?, string: String?)
    fun backPressed(string: String?, fragment: Fragment?)
    fun openDrawer()
}