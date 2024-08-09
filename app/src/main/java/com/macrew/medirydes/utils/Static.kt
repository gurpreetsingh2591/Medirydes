package com.macrew.medirydes.utils

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.content.DialogInterface
import android.graphics.Paint
import android.os.Build
import android.text.TextUtils
import android.util.Log
import android.util.Patterns
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import com.macrew.medirydes.R
import com.macrew.medirydes.annotation.Constants

import com.macrew.medirydes.interfaces.DialogClickListener
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*


class Static {

    lateinit var dialogClickListener: DialogInterface

    var count: Int = 1
    var cart_count: Int = 0


    fun statusBarColor(activity: Activity, color: Int) {

        val window: Window = activity.window
//        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
//        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
//        window.statusBarColor = ContextCompat.getColor(activity, color)


        val decor = window.decorView
        decor.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        window.statusBarColor = color;
    }

    fun toggleFullscreen(fullscreen: Boolean, activity: Activity) {
        val attrs = activity.window.attributes
        if (fullscreen) {
            attrs.flags = attrs.flags or WindowManager.LayoutParams.FLAG_FULLSCREEN
        } else {
            attrs.flags = attrs.flags and WindowManager.LayoutParams.FLAG_FULLSCREEN.inv()
        }
        activity.window.attributes = attrs
    }


    fun isValidEmail(target: CharSequence?): Boolean {
        return !TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches()
    }

    fun showDialog(activity: Activity, title: String, msg: String) {
        AlertDialog.Builder(activity)
            .setTitle(title)
            .setMessage(msg)
            .setCancelable(false)
            .setPositiveButton(
                Constants.OK
            ) { dialog, whichButton ->
                // do something...
                dialog.dismiss()
            }
            .setNegativeButton(
                Constants.CANCEL
            ) { dialog, whichButton -> dialog.dismiss() }
            .show()
    }

    fun TextView.showStrikeThrough(show: Boolean) {
        paintFlags =
            if (show) paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
            else paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
    }


    private var progressDialog: Dialog? = null

//    fun showLoadingDialog(context: Context?) {
//        if (!(progressDialog != null && progressDialog!!.isShowing)) {
//            progressDialog = Dialog(context!!)
//            progressDialog!!.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
//            progressDialog!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
//            progressDialog!!.setContentView(R.layout.progress_bar)
//            progressDialog!!.setCancelable(false)
//            progressDialog!!.show()
//        }
//    }

    fun cancelLoading() {
        if (progressDialog != null && progressDialog!!.isShowing) progressDialog!!.cancel()
    }


    fun changeNumberFormat(value: String): String {
        val nf: NumberFormat = NumberFormat.getInstance(Locale("eu", "dk"))

        return nf.format(value.toDouble()).toString()
    }


    fun fadeIn(view: View, activity: Activity) {
        val animMoveToTop = AnimationUtils.loadAnimation(activity, R.anim.fadein);
        view.startAnimation(animMoveToTop)

        animMoveToTop.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationRepeat(animation: Animation?) {

            }

            override fun onAnimationEnd(animation: Animation?) {
                view.visibility = View.VISIBLE

            }

            override fun onAnimationStart(animation: Animation?) {
            }

        })
    }


    fun slideToTop(activity: Activity, view: View, animation: Int) {
        val animMoveToTop = AnimationUtils.loadAnimation(activity, animation);
        view.startAnimation(animMoveToTop)

        animMoveToTop.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationRepeat(animation: Animation?) {

            }

            override fun onAnimationEnd(animation: Animation?) {
                view.visibility = View.VISIBLE

            }

            override fun onAnimationStart(animation: Animation?) {
            }

        })
    }

    fun getCurrentDate(): String {
        val SERVER_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss"
        val locale: Locale = Locale.getDefault()
        val sdf = SimpleDateFormat(Constants.DATE_FORMAT, locale)
        return sdf.format(Date())
    }

    @SuppressLint("SimpleDateFormat")
    fun getDateFormat(date: String): String {

        val formatIn = SimpleDateFormat(Constants.DATE_FORMAT)
        val formatOut = SimpleDateFormat(
            Constants.FORMAT_EEE_dd_MM,
            Locale(Constants.LOCALE_DANISH)
        )
        val calendar = Calendar.getInstance()
        calendar.time = formatIn.parse(date)

        return formatOut.format(calendar.time)

    }

    fun getCDate(date: String): String {
        val locale: Locale = Locale.getDefault()
        val sdf = SimpleDateFormat(Constants.FORMAT_EEE_dd_MM, Locale(Constants.LOCALE_DANISH))
        return sdf.format(date)
    }

    fun getText(data: Any): String {
        var str = ""
        if (data is EditText) {
            str = data.text.toString()
        } else if (data is String) {
            str = data
        }
        return str
    }

    fun isValidMobile(data: Any, updateUI: Boolean = true): Boolean {
        val str = getText(data)
        var valid = true
        if (str.isEmpty()) {
            valid = false
            if (updateUI) {
                val error: String? = Validator.activity?.getString(R.string.phone_required_msg)
                Validator.setError(data, error)
            }
        } else {
            valid = str.trim().length > 10

            // Set error if required
            if (updateUI) {
                val error: String? =
                    if (valid) null else Validator.activity?.getString(R.string.full_phone_required_msg)
                Validator.setError(data, error)
            }
        }

        return valid
    }

    fun dialog(
        activity: Activity,
        msg: String,
        toastMsg: String,
        dialogClickListener: DialogClickListener
    ) {
        val builder = AlertDialog.Builder(activity)
        builder.setMessage(msg)
        builder.setPositiveButton(Constants.YES) { dialog, _ ->

            dialogClickListener.onClickYes(Constants.YES)
            dialog.cancel()
            Toast.makeText(activity, toastMsg, Toast.LENGTH_LONG)
                .show()
        }
        builder.setNegativeButton(Constants.NO) { _, _ ->

            dialogClickListener.onClickNo(Constants.NO)

        }
        val alertDialog: AlertDialog = builder.create()
        alertDialog.show()
    }

    fun checkList(selected: ArrayList<String?>) {
        val selectedlist: ArrayList<String?> = selected
    }


    fun capitalizeString(str: String): String {
        var retStr = str
        try { // We can face index out of bound exception if the string is null
            retStr = str.substring(0, 1).toUpperCase() + str.substring(1)
        } catch (e: Exception) {
        }
        return retStr
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun endDateFormatString(str: String): String {

        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm a")
        val parse: LocalDateTime = LocalDateTime.parse(str, formatter)
        println("VALUE1=$parse")

//        val format = DateTimeFormatter.ofPattern("HH:mm")
//        val localDate: LocalDate = LocalDate.parse(str, format)
//        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm a", Locale.getDefault())
        return parse.format(formatter)
    }

    @SuppressLint("SimpleDateFormat")
    fun dateFormatString(date: String): String {
        val parser = SimpleDateFormat("yyyy-MM-dd")
        val formatter = SimpleDateFormat("dd/MM")

//        val format = DateTimeFormatter.ofPattern("yyyy-MM-dd")
//        val localDate: LocalDate = LocalDate.parse(str, format)
//        val formatter = DateTimeFormatter.ofPattern("dd/MM", Locale.getDefault())

        return formatter.format(parser.parse(date)!!)
    }

    @SuppressLint("SimpleDateFormat")
    fun dateFormat2String(date: String): String {
        val parser = SimpleDateFormat("yyyy-MM-dd")
        val formatter = SimpleDateFormat("dd MMM yyyy")
//        val format = DateTimeFormatter.ofPattern("yyyy-MM-dd")
//        val localDate: LocalDate = LocalDate.parse(str, format)
//        val formatter = DateTimeFormatter.ofPattern("dd MMM yyyy", Locale.getDefault())

        return formatter.format(parser.parse(date)!!)
    }


    @SuppressLint("SimpleDateFormat")
    @RequiresApi(Build.VERSION_CODES.O)
    fun timeCalculationString(startTime: String, endTime: String): String {
        val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")

        val date1 = simpleDateFormat.parse(startTime)
        val date2 = simpleDateFormat.parse(endTime)

        val difference: Long = date2.time - date1.time
        val strDiff: String = difference.toString()
        if (strDiff.contains("-")) {
            return "00H 00M 00S"
        } else {
            val days = (difference / (1000 * 60 * 60 * 24)).toInt()
            var hours = ((difference - 1000 * 60 * 60 * 24 * days) / (1000 * 60 * 60)).toInt()
            val min = (difference - 1000 * 60 * 60 * 24 * days - 1000 * 60 * 60 * hours).toInt() / (1000 * 60)
            hours = if (hours < 0) -hours else hours
            Log.i("======= Hours", " :: $hours")
            return "$hours H $min M 00 S"
        }
    }


}

