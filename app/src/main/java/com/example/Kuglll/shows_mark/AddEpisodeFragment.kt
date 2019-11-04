package com.example.Kuglll.shows_mark

import android.Manifest
import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.os.Environment
import android.os.Environment.getExternalStoragePublicDirectory
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.NumberPicker
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.Toolbar
import androidx.constraintlayout.widget.Group
import androidx.core.app.ActivityCompat
import androidx.core.content.FileProvider
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.example.Kuglll.shows_mark.DataClasses.DataViewModel
import com.example.Kuglll.shows_mark.DataClasses.Episode
import kotlinx.android.synthetic.main.fragment_add_episode.*
import kotlinx.android.synthetic.main.toolbar.*
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

private const val SHOWID = "showid"
private const val CAMERA_PERMISSION_REQUEST = 1
private const val REQUEST_IMAGE_CAPTURE = 2

class AddEpisodeFragment : Fragment() {

    var showID = -1
    var pathToFile : String = ""
    lateinit var viewModel: DataViewModel

    var episodeNumber = 1
    var seasonNumber = 1

    companion object {
        fun returnAddEpisodeFragment(showID: Int) : AddEpisodeFragment{
            val args = Bundle()
            args.putInt(SHOWID, showID)
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

        viewModel = ViewModelProviders.of(this).get(DataViewModel::class.java)

        showID = arguments!!.getInt(SHOWID, -1)
        val toolbar = view.findViewById<Toolbar>(R.id.toolbar)
        toolbarTitle.text = "Add episode"

        toolbar.setNavigationOnClickListener{onBackPressed()}

        initOnClickListeners()

        episodeTitleEditText.doOnTextChanged { text, start, count, after ->  validateInput()}
        episodeDescriptionEditText.doOnTextChanged { text, start, count, after -> validateInput() }

    }



    fun initOnClickListeners(){
        saveButton.setOnClickListener{
                MainActivity.storage.shows[showID].addEpisode(Episode(episodeTitleEditText.text.toString(), episodeNumber ,seasonNumber))
                viewModel.episodeInserted.value = true
                activity?.onBackPressed()

        }

        uploadPhotoGroup.setAllOnClickListeners(object : View.OnClickListener{
            override fun onClick(p0: View?) {
                startCamera()
            }
        })

        changePhotoGroup.setAllOnClickListeners(object : View.OnClickListener{
            override fun onClick(p0: View?) {
                startCamera()
            }
        })

        episodeSeasonGroup.setAllOnClickListeners(object : View.OnClickListener{
            override fun onClick(p0: View?) {
                displayNumberPicker()
            }
        })
    }

    fun Group.setAllOnClickListeners(listener: View.OnClickListener){
        referencedIds.forEach { id ->
            rootView.findViewById<View>(id).setOnClickListener(listener)
        }
    }

    fun onBackPressed() {
        if(textInInputFields()){
            displayDialog()
        } else{
            activity?.onBackPressed()
        }
    }

    fun textInInputFields() : Boolean{
        return episodeTitleEditText.text.isNotEmpty() || episodeDescriptionEditText.text.isNotEmpty()
    }

    fun validateInput() {
        val episodeTitle = episodeTitleEditText.text
        saveButton.isEnabled = episodeTitle.length >= 1
    }

    fun displayDialog(){
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Are you sure you want to quit?")
        builder.setMessage("You left text in input fields.")

        builder.setPositiveButton("YES"){dialog, which ->
            episodeTitleEditText.setText("")
            episodeDescriptionEditText.setText("")
            onBackPressed()
        }

        builder.setNegativeButton("No"){dialog,which ->
            //I think I can ignore this
        }

        val dialog: AlertDialog = builder.create()
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
            episodeSeasonNumber.text = String.format("S%02d E%02d", seasonNumber, episodeNumber)
            dialog.dismiss()
        }

        dialog.show()
    }

    fun startCamera(){
        //if both permissions are granted, start the camera
        if(ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
            Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
                takePictureIntent.resolveActivity(activity!!.packageManager)?.also {
                    val photoFile = createPhotoFile()
                    pathToFile = photoFile.absolutePath
                    val photoURI = FileProvider.getUriForFile(requireContext(), "com.example.Kuglll.shows_mark.fileprovider", photoFile)
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
                }
            }
        }else{ //request for both permissions
            ActivityCompat.requestPermissions(requireActivity(), arrayOf(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE), CAMERA_PERMISSION_REQUEST)
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
            var drawable = Drawable.createFromPath(pathToFile)
            MainActivity.storage.drawable = drawable
        }
    }

    override fun onResume() {
        super.onResume()
        var drawable = MainActivity.storage.drawable
        if (drawable != null) {
            changePhotoGroup.visibility = View.VISIBLE
            episodePhoto.setImageDrawable(drawable)
            changePhotoTextView.text = "Change photo"
            uploadPhotoGroup.visibility = View.GONE
        } else {
            changePhotoGroup.visibility = View.GONE
            changePhotoTextView.text = ""
            uploadPhotoGroup.visibility = View.VISIBLE
        }
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
        } else{
            super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        }
    }

    fun allPermisionGranted(grantResults: IntArray): Boolean{
        for (grantResult in grantResults){
            if(grantResult == PackageManager.PERMISSION_DENIED) return false
        }
        return true
    }
}
