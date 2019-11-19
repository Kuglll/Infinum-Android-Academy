package com.example.kuglll.shows_mark

import android.Manifest
import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.os.Environment
import android.os.Environment.getExternalStoragePublicDirectory
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.NumberPicker
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.constraintlayout.widget.Group
import androidx.core.app.ActivityCompat
import androidx.core.content.FileProvider
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.example.kuglll.shows_mark.dataClasses.DataViewModel
import com.example.kuglll.shows_mark.utils.EpisodeUploadRequest
import kotlinx.android.synthetic.main.fragment_add_episode.*
import kotlinx.android.synthetic.main.toolbar.*
import kotlinx.android.synthetic.main.upload_photo_dialog.*
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

private const val SHOWID = "showid"
private const val CAMERA_PERMISSION_REQUEST = 1
private const val GALLERY_PERMISSION_REQUEST = 3
private const val REQUEST_IMAGE_CAPTURE = 2
private const val REQUEST_GALLERY_IMAGE = 4
private const val EPISODE_NUMBER = "EPISODE_NUMBER"
private const val SEASON_NUMBER = "SEASON_NUMBER"
private const val IMAGE = "IMAGE"

class AddEpisodeFragment : Fragment(), FragmentBackListener {

    var showID = -1
    var pathToFile : String = ""
    lateinit var viewModel: DataViewModel

    var token: String? = null
    var episodeNumber = 1
    var seasonNumber = 1

    companion object {
        fun returnAddEpisodeFragment(showID: String) : AddEpisodeFragment{
            val args = Bundle()
            args.putString(SHOWID, showID)
            val fragment = AddEpisodeFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_add_episode, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if(savedInstanceState != null){
            episodeNumber = savedInstanceState.getInt(EPISODE_NUMBER)
            seasonNumber = savedInstanceState.getInt(SEASON_NUMBER)
            episodeSeasonNumber.text = String.format("S %02d E %02d", seasonNumber, episodeNumber)

            pathToFile = savedInstanceState.getString(IMAGE, "")
            if(pathToFile != ""){
                Log.d("PathToFILE", pathToFile)
                displayImage()
            }
        }

        val sharedPref = requireActivity().getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE)
        token = sharedPref.getString(TOKEN, null)

        viewModel = ViewModelProviders.of(this).get(DataViewModel::class.java)

        showID = requireArguments().getInt(SHOWID, -1)
        toolbarTitle.text = "Add episode"

        initOnClickListeners()

        episodeTitleEditText.doOnTextChanged { text, start, count, after ->  validateInput()}
        episodeDescriptionEditText.doOnTextChanged { text, start, count, after -> validateInput() }

    }



    fun initOnClickListeners(){
        toolbar.setNavigationOnClickListener{activity?.onBackPressed()}

        saveButton.setOnClickListener{
            if(descriptionLength()){
                uploadEpisode()
            } else{
                Toast.makeText(requireContext(), "Description should be at least 50 characters long!", Toast.LENGTH_LONG).show()
            }
        }

        uploadPhotoGroup.setAllOnClickListeners(object : View.OnClickListener{
            override fun onClick(p0: View?) {
                displayCameraDialog()
            }
        })

        changePhotoGroup.setAllOnClickListeners(object : View.OnClickListener{
            override fun onClick(p0: View?) {
                displayCameraDialog()
            }
        })

        episodeSeasonGroup.setAllOnClickListeners(object : View.OnClickListener{
            override fun onClick(p0: View?) {
                displayNumberPicker()
            }
        })
    }

    fun uploadEpisode(){
        if(pathToFile != ""){
            viewModel.uploadMedia(File(pathToFile), token, EpisodeUploadRequest(
                showID.toString(),
                "",
                episodeTitleEditText.text.toString(),
                episodeDescriptionEditText.text.toString(),
                episodeSeasonNumber.text.toString().split(" ")[1],
                episodeSeasonNumber.text.toString().split(" ")[3]
            ))
        } else {
            viewModel.uploadEpisode(EpisodeUploadRequest(showID.toString(),
                "",
                episodeTitleEditText.text.toString(),
                episodeDescriptionEditText.text.toString(),
                episodeSeasonNumber.text.toString().split(" ")[1],
                episodeSeasonNumber.text.toString().split(" ")[3]), token)
        }

        episodeTitleEditText.setText("")
        episodeDescriptionEditText.setText("")
        activity?.onBackPressed()
    }

    fun descriptionLength(): Boolean{
        return episodeDescriptionEditText.text.length >= 50
    }

    fun Group.setAllOnClickListeners(listener: View.OnClickListener){
        referencedIds.forEach { id ->
            rootView.findViewById<View>(id).setOnClickListener(listener)
        }
    }

    fun textInInputFields() : Boolean{
        return episodeTitleEditText.text.isNotEmpty() || episodeDescriptionEditText.text.isNotEmpty()
    }

    fun validateInput() {
        val episodeTitle = episodeTitleEditText.text
        saveButton.isEnabled = episodeTitle.isNotEmpty()
    }

    fun displayDialog(){
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Are you sure you want to quit?")
        builder.setMessage("You left text in input fields.")

        builder.setPositiveButton("YES"){dialog, which ->
            episodeTitleEditText.setText("")
            episodeDescriptionEditText.setText("")
            activity?.onBackPressed()
        }

        builder.setNegativeButton("No"){dialog,which ->
            //I think I can ignore this
        }

        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    fun displayCameraDialog(){
        val dialog = Dialog(context!!)
        dialog.setContentView(R.layout.upload_photo_dialog)

        val selectCamera : TextView = dialog.findViewById(R.id.selectCamera)
        val selectGalery : TextView = dialog.findViewById(R.id.selectGalery)

        selectCamera.setOnClickListener{
            startCamera()
            dialog.dismiss()
        }
        selectGalery.setOnClickListener{
            startGallery()
            dialog.dismiss()
        }

        dialog.show()
    }

    fun displayNumberPicker() {
        val dialog = Dialog(context!!)
        dialog.setContentView(R.layout.number_picker_dialog)

        val episodeNumberPicker: NumberPicker = dialog.findViewById(R.id.episodeNumberPicker)
        val seasonNumberPicker: NumberPicker = dialog.findViewById(R.id.seasonNumberPicker)
        val saveEpisodeSeason: TextView = dialog.findViewById(R.id.saveEpisodeSeason)

        episodeNumberPicker.minValue = 1
        episodeNumberPicker.maxValue = 99

        seasonNumberPicker.minValue = 1
        seasonNumberPicker.maxValue = 20

        saveEpisodeSeason.setOnClickListener {
            episodeNumber = episodeNumberPicker.value
            seasonNumber = seasonNumberPicker.value
            episodeSeasonNumber.text = String.format("S %02d E %02d", seasonNumber, episodeNumber)
            dialog.dismiss()
        }

        dialog.show()
    }

    fun startGallery(){
        //if permission is granted start the gallery
        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            val galleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(galleryIntent, REQUEST_GALLERY_IMAGE)
        } else { //request for permission
            requestPermissions(arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), GALLERY_PERMISSION_REQUEST)
        }
    }

    fun startCamera(){
        //if both permissions are granted, start the camera
        if(ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
            Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
                takePictureIntent.resolveActivity(activity!!.packageManager)?.also {
                    val photoFile = createPhotoFile()
                    pathToFile = photoFile.absolutePath
                    val photoURI = FileProvider.getUriForFile(requireContext(), "com.example.kuglll.shows_mark.fileprovider", photoFile)
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
                }
            }
        }else{ //request for both permissions
            requestPermissions(arrayOf(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE), CAMERA_PERMISSION_REQUEST)
        }
    }

    fun createPhotoFile(): File {
        val name : String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val storageDir : File = getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
        val image : File = File.createTempFile(name, ".jpg", storageDir)

        return image
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_IMAGE_CAPTURE){
            val drawable = Drawable.createFromPath(pathToFile)
            episodePhoto.setImageDrawable(drawable)
        }else if(resultCode == Activity.RESULT_OK && requestCode == REQUEST_GALLERY_IMAGE && data != null){
            val selectedImage = data.data
            try{
                val photoFile = createPhotoFile()
                pathToFile = photoFile.absolutePath
                val bitmap = MediaStore.Images.Media.getBitmap(requireActivity().contentResolver, selectedImage)
                val out = FileOutputStream(pathToFile)
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, out)
                val drawable = Drawable.createFromPath(pathToFile)
                episodePhoto.setImageDrawable(drawable)
            }catch (exception: IOException){
                Log.d("Gallery exception", exception.toString())
            }
        }else{
            pathToFile = ""
        }
    }

    override fun onSaveInstanceState(bundle: Bundle) {
        super.onSaveInstanceState(bundle)
        bundle.putInt(EPISODE_NUMBER, episodeNumber)
        bundle.putInt(SEASON_NUMBER, seasonNumber)
        bundle.putString(IMAGE, pathToFile)
    }

    fun displayImage(){
        var drawable = Drawable.createFromPath(pathToFile)

        changePhotoGroup.visibility = View.VISIBLE
        episodePhoto.setImageDrawable(drawable)
        changePhotoTextView.text = "Change photo"
        uploadPhotoGroup.visibility = View.GONE
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if(requestCode == CAMERA_PERMISSION_REQUEST){
            if(grantResults.isNotEmpty() && allPermisionGranted(grantResults)){
                startCamera()
            } else{
                Toast.makeText(requireContext(), "To use camera, we need your permissions", Toast.LENGTH_LONG).show()
            }
        }else if(requestCode == GALLERY_PERMISSION_REQUEST){
            if(grantResults.isNotEmpty() && grantResults.first() == PackageManager.PERMISSION_GRANTED){
                startGallery()
            } else{
                Toast.makeText(requireContext(), "To use gallery, we need your permissions", Toast.LENGTH_LONG).show()
            }
        }else{
            super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        }
    }

    fun allPermisionGranted(grantResults: IntArray): Boolean{
        for (grantResult in grantResults){
            if(grantResult == PackageManager.PERMISSION_DENIED) return false
        }
        return true
    }

    override fun onBackPressed(): Boolean {
        if(textInInputFields()){
            displayDialog()
        } else{
            return false
        }
        return true
    }
}
