package com.macrew.medirydes.dashboard.view.activities

import android.Manifest
import android.R.attr
import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore.Images
import android.util.Log
import android.view.View
import android.view.Window
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.androidbuffer.kotlinfilepicker.KotConstants
import com.androidbuffer.kotlinfilepicker.KotRequest
import com.androidbuffer.kotlinfilepicker.KotResult
import com.bumptech.glide.Glide
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.macrew.medirydes.R
import com.macrew.medirydes.annotation.Status
import com.macrew.medirydes.dashboard.model.profile.ProfileModel
import com.macrew.medirydes.dashboard.view.fragments.HomeFragment
import com.macrew.medirydes.dashboard.viewModel.DashboardViewModel
import com.macrew.medirydes.retrofit.WebResponse
import com.macrew.medirydes.utils.RealPathUtil
import com.macrew.medirydes.utils.SharedPrefrencesUtils
import com.macrew.medirydes.utils.Static
import com.macrew.medirydes.utils.getFilePathForN
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.content_loading.*
import kotlinx.android.synthetic.main.fragment_profile.*
import kotlinx.android.synthetic.main.tab_bar.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.ByteArrayOutputStream
import java.io.File


class ProfileActivity : AppCompatActivity() {
    val static = Static()
    private lateinit var genderListName: ArrayList<String>
    var gender = ""
    lateinit var dashboardViewModel: DashboardViewModel
    var imageEncoded: String? = ""
    private var REQUEST_CAMERA = 101
    private var REQUEST_GALLERY = 102

    companion object {
        fun startActivity(activity: Activity?, screen: String) {
            val intent = Intent(activity, ProfileActivity::class.java)
            intent.putExtra("screen", screen)
            activity?.startActivity(intent)
            //  activity?.finish()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_profile)

        initObserver()
        //  dashboardViewModel.callGetEditProfileApi("","")
        initUI()
        spInit()

        Dexter.withContext(this)
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

    fun getURLForResource(resourceId: Int): String? {
        //use BuildConfig.APPLICATION_ID instead of R.class.getPackage().getName() if both are not same
        return Uri.parse(
            "android.resource://" + R::class.java.getPackage().getName() + "/" + resourceId
        ).toString()
    }

    private fun initUI() {

        imgNotification.visibility = View.VISIBLE
        imgProfile.visibility = View.GONE
        txtAge.setText(SharedPrefrencesUtils.getAge().toString())
        txtName.setText(SharedPrefrencesUtils.getUserName().toString())
        txtEmail.setText(SharedPrefrencesUtils.getUserEmail().toString())
        txtPhone.setText(SharedPrefrencesUtils.getUserNumber().toString())
        txtCompanyName.setText(SharedPrefrencesUtils.getCompanyName().toString())
        txtLicenceNumber.setText(SharedPrefrencesUtils.getLicenceNo().toString())
        txtVehicleId.setText(SharedPrefrencesUtils.getCompanyId().toString())
        if (!SharedPrefrencesUtils.getUserImage().equals("")) {
            //  Picasso.get().load(SharedPrefrencesUtils.getUserImage()) .into(civProfileImage)

            Glide.with(this).load(SharedPrefrencesUtils.getUserImage()).into(civProfileImage);
        }
        btnSave.setOnClickListener {


            val profileData: HashMap<String, RequestBody> = hashMapOf()
            val strName = txtName.text.toString()
            val strPhone = txtPhone.text.toString()
            val strAge = txtAge.text.toString()
            val strEmail = txtEmail.text.toString()
            val strGender = gender

            val uName: RequestBody = RequestBody.create("text/plain".toMediaTypeOrNull(),strName)
            val uPhone: RequestBody = RequestBody.create("text/plain".toMediaTypeOrNull(),strPhone)
            val uAge: RequestBody = RequestBody.create("text/plain".toMediaTypeOrNull(),strAge)
            val uGender:RequestBody = RequestBody.create("text/plain".toMediaTypeOrNull(),strGender)
            val uEmail: RequestBody = RequestBody.create("text/plain".toMediaTypeOrNull(),strEmail)
            //  val Icon = BitmapFactory.decodeResource(resources, R.drawable.profile)

            Log.e("UserDetail:--",strName+" ,"+strAge+", "+strEmail+", "+strPhone+", "+strGender)

            var fileToUpload: MultipartBody.Part?
            if (imageEncoded != "") {
                val file = File(imageEncoded!!)
                val requestBody: RequestBody =
                    RequestBody.create("multipart/form-data".toMediaTypeOrNull(), file)
                fileToUpload = MultipartBody.Part.createFormData(
                    "image",
                    file.name,
                    requestBody
                )
            } else {
                val file = File(SharedPrefrencesUtils.getUserImage()!!)
                val requestBody: RequestBody =
                    RequestBody.create("multipart/form-data".toMediaTypeOrNull(), file)
                fileToUpload = MultipartBody.Part.createFormData(
                    "image",
                    file.name,
                    requestBody
                )
            }
            profileData["name"] = uName
            profileData["gender"] = uGender
            profileData["phone"] = uPhone
            profileData["age"] = uAge
            profileData["email"] = uEmail

            if (imageEncoded != "") {
                dashboardViewModel.callGetEditProfileApi(
                    profileData, fileToUpload
                )
            } else {
                dashboardViewModel.callGetEditProfileWithoutImageApi(
                    profileData
                )
            }
        }
        civProfileImage.setOnClickListener {
            showDialog()
        }

    }


    private fun spInit() {
        genderListName = ArrayList()
        //Gender  Spinner
        val productionWeeklyAdapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            genderListName
        )
        genderListName.add("Select Gender")
        genderListName.add("Male")
        genderListName.add("Female")
        genderListName.add("Transgender")
        productionWeeklyAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spGender!!.adapter = productionWeeklyAdapter

        gender = SharedPrefrencesUtils.getGender().toString()
        for (i in genderListName.indices) {
            if (gender == genderListName[i]) {
                spGender.setSelection(i)
                break
            }
        }
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
        dashboardViewModel.isLoading.observe(this) { aBoolean ->
            if (aBoolean)
                progressLoading.visibility = View.VISIBLE
            else
                progressLoading.visibility = View.GONE
        }

        dashboardViewModel.responseEditProfileData.observe(this,
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
                    Toast.makeText(this, ResponseProfile.errorMsg, Toast.LENGTH_SHORT).show()
                }
                if (ResponseProfile.status == Status.FAILURE) {
                    Toast.makeText(this, ResponseProfile.errorMsg, Toast.LENGTH_SHORT).show()
                }

            }
        )

    }

    private fun showDialog() {
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.dialog_selection)

        val llGallery = dialog.findViewById(R.id.llGallery) as LinearLayout
        val llCamera = dialog.findViewById(R.id.llCamera) as LinearLayout
        val llCancel = dialog.findViewById(R.id.llCancel) as LinearLayout

        llGallery.setOnClickListener {

            KotRequest.Gallery(this, REQUEST_GALLERY).isMultiple(false).pick()
            dialog.dismiss()

        }


        llCamera.setOnClickListener {
            KotRequest.Camera(this).setRequestCode(REQUEST_CAMERA).pick()
            // KotRequest.Camera(this, REQUEST_CAMERA).pick()
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
                imageEncoded = RealPathUtil.getRealPath(this, result?.get(0)?.uri)
                civProfileImage.setImageURI(result?.get(0)?.uri)

            }

            if (REQUEST_CAMERA == requestCode && resultCode == Activity.RESULT_OK && null != data) {

//                val photo:Bitmap = data?.extras?.get("data") as Bitmap
//                val tempUri = getImageUri(applicationContext, photo)
//
//                val finalFile = File(getRealPathFromURI(tempUri)!!)


                val result =
                    data.getParcelableArrayListExtra<KotResult>(KotConstants.EXTRA_FILE_RESULTS)
                civProfileImage.setImageURI(result?.get(0)?.uri)
                imageEncoded = getFilePathForN.getFilePathForN(result?.get(0)?.uri, this)
            }

        } catch (e: Exception) {
            Toast.makeText(this, "Something went wrong " + e, Toast.LENGTH_LONG)
                .show()
        }
        super.onActivityResult(requestCode, resultCode, data)
    }


    fun getImageUri(inContext: Context, inImage: Bitmap): Uri? {
        val bytes = ByteArrayOutputStream()
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
        val path = Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null)
        return Uri.parse(path)
    }

    fun getRealPathFromURI(uri: Uri?): String? {
        var path = ""
        if (contentResolver != null) {
            val cursor: Cursor? = contentResolver.query(uri!!, null, null, null, null)
            if (cursor != null) {
                cursor.moveToFirst()
                val idx: Int = cursor.getColumnIndex(Images.ImageColumns.DATA)
                path = cursor.getString(idx)
                cursor.close()
            }
        }
        return path
    }
}