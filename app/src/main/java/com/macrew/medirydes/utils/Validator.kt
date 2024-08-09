package com.macrew.medirydes.utils

import android.app.Activity
import android.util.Patterns
import android.widget.EditText
import com.macrew.medirydes.R
//import com.hbb20.CountryCodePicker
import java.util.regex.Pattern

/**
 * This class is used to validate data like email, phone, password policy, etc.
 * It also sets error to the EditText or TextInputLayout of the EditText if needed.
 */
class Validator {

    companion object {

        // You need pass activity to access string validation messages.
        var activity: Activity? = null

        /**
         * Retrieve string data from the parameter.
         * @param data - can be EditText or String
         * @return - String extracted from EditText or data if its data type is Strin.
         */
        fun getText(data: Any): String {
            var str = ""
            if (data is EditText) {
                str = data.text.toString()
            } else if (data is String) {
                str = data
            }
            return str
        }

        /**
         * Checks if the name is valid.
         * @param data - can be EditText or String
         * @param updateUI - if true and if data is EditText, the function sets error to the EditText or its TextInputLayout
         * @return - true if the name is valid.
         */
        fun isValidName(data: Any, updateUI: Boolean = true): Boolean {
            val str = getText(data)
            var valid = true
            if (str.isEmpty()) {
                valid = false
                if (updateUI) {
                    val error: String? = activity?.getString(R.string.name_required_msg)
                    setError(data, error)
                }
            } else {
                valid = str.trim().length > 2

                // Set error if required
                if (updateUI) {
                    val error: String? =
                        if (valid) null else activity?.getString(R.string.name_validation_msg)
                    setError(data, error)
                }
            }

            return valid
        }



        fun isValidText(data: Any, updateUI: Boolean = true, name: String): Boolean {
            val str = getText(data)
            var valid = true
            if (str.isEmpty()) {
                valid = false
                if (updateUI) {
                    val error: String? = activity?.getString(R.string.required_msg, name)
                    setError(data, error)
                }
            } else {
                valid = str.trim().length > 2

                // Set error if required
                if (updateUI) {
                    val error: String? =
                        if (valid) null else activity?.getString(R.string.validation_msg, name)
                    setError(data, error)
                }
            }

            return valid
        }


        fun isValidZipCode(data: Any, updateUI: Boolean = true): Boolean {
            val str = getText(data)
            var valid = true
            if (str.isEmpty()) {
                valid = false
                if (updateUI) {
                    val error: String? = activity?.getString(R.string.zip_required_msg)
                    setError(data, error)
                }
            } else {
                valid = str.trim().length > 4

                // Set error if required
                if (updateUI) {
                    val error: String? =
                        if (valid) null else activity?.getString(R.string.zip_validation_msg)
                    setError(data, error)
                }
            }

            return valid
        }

        fun isValidLocation(data: Any, updateUI: Boolean = true): Boolean {
            val str = getText(data)
            var valid = true
            if (str.isEmpty()) {
                valid = false
                if (updateUI) {
                    val error: String? = activity?.getString(R.string.location_required_msg)
                    setError(data, error)
                }
            } else {
                valid = str.trim().length > 2

                // Set error if required
                if (updateUI) {
                    val error: String? =
                        if (valid) null else activity?.getString(R.string.location_validation_msg)
                    setError(data, error)
                }
            }

            return valid
        }

        /**
         * Checks if the email is valid.
         * @param data - can be EditText or String
         * @param updateUI - if true and if data is EditText, the function sets error to the EditText or its TextInputLayout
         * @return - true if the email is valid.
         */
        fun isValidEmail(data: Any, updateUI: Boolean = true): Boolean {
            val str = getText(data)
            var valid = true
            if (str.isEmpty()) {
                valid = false
                if (updateUI) {
                    val error: String? = activity?.getString(R.string.email_required_msg)
                    setError(data, error)
                }
            } else {
                valid = Patterns.EMAIL_ADDRESS.matcher(str).matches()

                // Set error if required
                if (updateUI) {
                    val error: String? =
                        if (valid) null else activity?.getString(R.string.email_validation_msg)
                    setError(data, error)
                }
            }

            return valid
        }

        /**
         * Checks if the phone is valid.
         * @param data - can be EditText or String
         * @param updateUI - if true and if data is EditText, the function sets error to the EditText or its TextInputLayout
         * @return - true if the phone is valid.
         */
//        fun isValidPhone(data: Any, cpp: CountryCodePicker, updateUI: Boolean = true): Boolean {
//            val str = getText(data)
//            var valid = true
//            if (str.isEmpty()) {
//                valid = false
//                if (updateUI) {
//                    val error: String? = activity?.getString(R.string.phone_required_msg)
//                    setError(data, error)
//                }
//            } else {
//                valid = cpp.isValidFullNumber//Patterns.PHONE.matcher(str).matches()
//
//                // Set error if required
//                if (updateUI) {
//                    val error: String? =
//                        if (valid) null else activity?.getString(R.string.phone_validation_msg)
//
//                    setError(data, error)
//                }
//            }
//
//
//            return valid
//        }


        /**
         * Checks if the password is valid as per the following password policy.
         * Password should be minimum minimum 8 characters long.
         * Password should contain at least one number.
         * Password should contain at least one capital letter.
         * Password should contain at least one small letter.
         * Password should contain at least one special character.
         * Allowed special characters: "~!@#$%^&*()-_=+|/,."';:{}[]<>?"
         *
         * @param data - can be EditText or String
         * @param updateUI - if true and if data is EditText, the function sets error to the EditText or its TextInputLayout
         * @return - true if the password is valid as per the password policy.
         */
        fun isValidPassword(data: Any, updateUI: Boolean = true): Boolean {
            val str = getText(data)
            var valid = true

            // Password policy check
            // Password should be minimum minimum 8 characters long
            if (str.length < 8) {
                valid = false
            }
            // Password should contain at least one number
            var exp = ".*[0-9].*"
            var pattern = Pattern.compile(exp, Pattern.CASE_INSENSITIVE)
            var matcher = pattern.matcher(str)
            if (!matcher.matches()) {
                valid = false
            }

            // Password should contain at least one capital letter
            exp = ".*[A-Z].*"
            pattern = Pattern.compile(exp)
            matcher = pattern.matcher(str)
            if (!matcher.matches()) {
                valid = false
            }

            // Password should contain at least one small letter
            exp = ".*[a-z].*"
            pattern = Pattern.compile(exp)
            matcher = pattern.matcher(str)
            if (!matcher.matches()) {
                valid = false
            }

            // Password should contain at least one special character
            // Allowed special characters : "~!@#$%^&*()-_=+|/,."';:{}[]<>?"
            exp = ".*[~!@#\$%\\^&*()\\-_=+\\|\\[{\\]};:'\",<.>/?].*"
            pattern = Pattern.compile(exp)
            matcher = pattern.matcher(str)
            if (!matcher.matches()) {
                valid = false
            }

            // Set error if required
            if (updateUI) {
                val error: String? =
                    if (valid) null else activity?.getString(R.string.password_policy)
                setError(data, error)
            }

            return valid
        }

        /**
         * Sets error on EditText or TextInputLayout of the EditText.
         * @param data - Should be EditText
         * @param error - Message to be shown as error, can be null if no error is to be set
         */
        fun setError(data: Any, error: String?) {
//            if (data is EditText) {
//                if (data.parent.parent is TextInputLayout) {
//                    (data.parent.parent as TextInputLayout).error = error
//                } else {
//                    data.setError(error)
//                }
//            }

            if (data is EditText) {
                data.error = error
                data.requestFocus()
            }
        }

    }

}
