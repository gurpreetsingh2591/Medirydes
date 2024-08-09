package com.macrew.medirydes.dashboard.view.fragments

import android.Manifest
import android.app.Activity
import android.app.Dialog
import android.content.ClipData
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.LinearLayout
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.androidbuffer.kotlinfilepicker.KotConstants
import com.androidbuffer.kotlinfilepicker.KotRequest
import com.androidbuffer.kotlinfilepicker.KotResult
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.macrew.medirydes.R
import com.macrew.medirydes.annotation.Status
import com.macrew.medirydes.dashboard.model.profile.ProfileModel
import com.macrew.medirydes.dashboard.view.activities.MainActivity
import com.macrew.medirydes.dashboard.viewModel.DashboardViewModel
import com.macrew.medirydes.retrofit.WebResponse
import com.macrew.medirydes.utils.RealPathUtil.getRealPath
import com.macrew.medirydes.utils.SharedPrefrencesUtils
import com.macrew.medirydes.utils.Static
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.content_loading.*
import kotlinx.android.synthetic.main.fragment_profile.*
import kotlinx.android.synthetic.main.fragment_profile.txtName
import okhttp3.MultipartBody

class ProfileFragments : Fragment() {
    val static = Static()
    private lateinit var genderListName: ArrayList<String>
    var gender = ""
    lateinit var dashboardViewModel: DashboardViewModel

    companion object {
        private val bundle = Bundle()

        private var ARG_PARAM = "currencyList"

        fun getInstance(): Fragment {
            return ProfileFragments()
        }
    }


    var imageEncoded: String? = ""
    private lateinit var imagesEncodedList: ArrayList<Uri?>
    private var imagesRealPath: ArrayList<String?> = arrayListOf()
    private var files: ArrayList<MultipartBody.Part> = arrayListOf()
    var REQUEST_CAMERA = 101
    var REQUEST_GALLERY = 102
    var REQUEST_FILE = 103
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initObserver()
        //  dashboardViewModel.callGetEditProfileApi("","")
        val args = arguments
        initUI()
        spInit()

        Dexter.withContext(requireActivity())
            .withPermissions(
                Manifest.permission.CAMERA,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ).withListener(object : MultiplePermissionsListener {
                override fun onPermissionsChecked(report: MultiplePermissionsReport) { /* ... */
                }

                override fun onPermissionRationaleShouldBeShown(
                    permissions: List<PermissionRequest?>?,
                    token: PermissionToken?
                ) { /* ... */
                }
            }).check()
    }

    private fun initUI() {
        txtAge.setText(SharedPrefrencesUtils.getAge().toString())
        txtName.setText(SharedPrefrencesUtils.getUserName().toString())
        txtEmail.setText(SharedPrefrencesUtils.getUserEmail().toString())
        txtPhone.setText(SharedPrefrencesUtils.getUserNumber().toString())
        txtCompanyName.setText(SharedPrefrencesUtils.getCompanyName().toString())
        txtLicenceNumber.setText(SharedPrefrencesUtils.getUserEmail().toString())
        txtVehicleId.setText(SharedPrefrencesUtils.getCompanyId().toString())
        if (!SharedPrefrencesUtils.getUserImage().equals("")) {
            Picasso.get().load(SharedPrefrencesUtils.getUserImage())
                .into(civProfileImage)
        }
        btnSave.setOnClickListener {
//            dashboardViewModel.callGetEditProfileApi(
//                txtName.text.toString(),
//                SharedPrefrencesUtils.getUserEmail().toString(),
//                txtPhone.text.toString(),
//                txtAge.text.toString(),
//                gender,
//                imageEncoded
//            )
        }
        civProfileImage.setOnClickListener {
            showDialog()
        }

    }


    private fun spInit() {
        genderListName = ArrayList()
        //Gender  Spinner
        val productionWeeklyAdapter = ArrayAdapter(
            requireActivity(),
            android.R.layout.simple_spinner_item,
            genderListName
        )
        genderListName.add("Select Gender")
        genderListName.add("Male")
        genderListName.add("Female")
        genderListName.add("Transgender")
        productionWeeklyAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spGender!!.adapter = productionWeeklyAdapter

        spGender.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                gender = genderListName[position]
            }
        }
    }

    //initialise Observer
    private fun initObserver() {
        dashboardViewModel = ViewModelProvider(this).get(DashboardViewModel::class.java)
        dashboardViewModel.isLoading.observe(requireActivity()) { aBoolean ->
            if (aBoolean)
                progressLoading.visibility = View.VISIBLE
            else
                progressLoading.visibility = View.GONE
        }

        dashboardViewModel.responseEditProfileData.observe(requireActivity(),
            { ResponseProfile: WebResponse<ProfileModel> ->
                if (ResponseProfile.status == Status.SUCCESS) {
                    SharedPrefrencesUtils.setUserLogin(
                        ResponseProfile.data?.result!!.user?.name,
                        ResponseProfile.data?.result!!.user?.email,
                        ResponseProfile.data?.result!!.user?.phone.toString(),
                        ResponseProfile.data?.result!!.user?.token,
                        ResponseProfile.data?.result!!.user?.user_detail!!.gender,
                        ResponseProfile.data?.result!!.user?.user_detail!!.age,
                        ResponseProfile.data?.result!!.user?.user_detail!!.image_url,
                    )
                    MainActivity.txtTabName.text = ResponseProfile.data?.result!!.user?.name
                    HomeFragment.txtUName.text = ResponseProfile.data?.result!!.user?.name
                    Picasso.get().load(ResponseProfile.data?.result!!.user?.user_detail?.image_url)
                        .into(MainActivity.imageTabProfile)
                    Picasso.get().load(ResponseProfile.data?.result!!.user?.user_detail?.image_url)
                        .into(MainActivity.imageViewProfile)
                    initUI()

                }
                if (ResponseProfile.status == Status.FAILURE) {
                    Toast.makeText(activity, ResponseProfile.errorMsg, Toast.LENGTH_SHORT).show()
                }

            }
        )

    }

    private fun showDialog() {
        val dialog = Dialog(requireActivity())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.dialog_selection)

        val llGallery = dialog.findViewById(R.id.llGallery) as LinearLayout
        val llCamera = dialog.findViewById(R.id.llCamera) as LinearLayout
        val llCancel = dialog.findViewById(R.id.llCancel) as LinearLayout

        llGallery.setOnClickListener {

            KotRequest.Gallery(requireActivity(), REQUEST_GALLERY).isMultiple(false).pick()
            dialog.dismiss()

        }


        llCamera.setOnClickListener {
            KotRequest.Camera(requireActivity()).setRequestCode(REQUEST_CAMERA).pick()
            // KotRequest.Camera(requireActivity(), REQUEST_CAMERA).pick()
            dialog.dismiss()
        }
        llCancel.setOnClickListener { dialog.dismiss() }
        dialog.show()

    }

    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ) {
        try {
            // When an Image is picked
            if (requestCode == REQUEST_GALLERY && resultCode == Activity.RESULT_OK && null != data
            ) {

                val result =
                    data.getParcelableArrayListExtra<KotResult>(KotConstants.EXTRA_FILE_RESULTS)
                imageEncoded = getRealPath(requireContext(), result?.get(0)?.uri)
                civProfileImage.setImageURI(result?.get(0)?.uri)

            }

            if (REQUEST_CAMERA == requestCode && resultCode == Activity.RESULT_OK) {
                val result =
                    data?.getParcelableArrayListExtra<KotResult>(KotConstants.EXTRA_FILE_RESULTS)
                civProfileImage.setImageURI(result?.get(0)?.uri)
                imageEncoded = getRealPath(requireActivity(), result?.get(0)?.uri)
            }

        } catch (e: Exception) {
            Toast.makeText(requireContext(), "Something went wrong", Toast.LENGTH_LONG)
                .show()
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

}