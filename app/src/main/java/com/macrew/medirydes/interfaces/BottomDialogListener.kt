package com.macrew.medirydes.interfaces


interface BottomDialogListener {
    fun onCancel()
    fun onSave(problemText: String?,spinnerString: String)

}