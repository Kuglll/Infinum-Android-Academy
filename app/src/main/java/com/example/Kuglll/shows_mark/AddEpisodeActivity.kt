package com.example.Kuglll.shows_mark

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.os.Environment.getExternalStoragePublicDirectory
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.Toolbar
import androidx.constraintlayout.widget.Group
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.core.graphics.drawable.toDrawable
import androidx.core.widget.doOnTextChanged
import kotlinx.android.synthetic.main.activity_add_episode.*
import kotlinx.android.synthetic.main.toolbar.*
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

private const val SHOWID = "showid"
private const val CAMERA_PERMISSION_REQUEST = 1
private const val REQUEST_IMAGE_CAPTURE = 2

class AddEpisodeActivity : AppCompatActivity() {

    var showID = -1
    var pathToFile : String = ""

    companion object {
        fun startAddEpisodeActvity(context : Context, showID : Int): Intent {
            val intent = Intent(context, AddEpisodeActivity::class.java)
            intent.putExtra(SHOWID, showID)
            return intent
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_episode)

        showID = intent.getIntExtra(SHOWID, -1)
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        toolbarTitle.text = "Add episode"

        toolbar.setNavigationOnClickListener(object : View.OnClickListener{
            override fun onClick(p0: View?) {
                onBackPressed()
            }
        })

        initOnClickListeners()

        episodeTitleEditText.doOnTextChanged { text, start, count, after ->  validateInput()}
        episodeDescriptionEditText.doOnTextChanged { text, start, count, after -> validateInput() }

    }

    fun initOnClickListeners(){
        saveButton.setOnClickListener(object : View.OnClickListener{
            override fun onClick(p0: View?) {
                ShowActivity.storage.shows[showID].addEpisode(Episode(episodeTitleEditText.text.toString()))
                setResult(Activity.RESULT_OK)
                finish()
            }
        })

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
    }

    fun Group.setAllOnClickListeners(listener: View.OnClickListener){
        referencedIds.forEach { id ->
            rootView.findViewById<View>(id).setOnClickListener(listener)
        }
    }

    override fun onBackPressed() {
        if(textInInputFields()){
            displayDialog()
        } else{
            super.onBackPressed()
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
        val builder = AlertDialog.Builder(this@AddEpisodeActivity)
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

    fun startCamera(){
        //if both permissions are granted, start the camera
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
            Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
                takePictureIntent.resolveActivity(packageManager)?.also {
                    val photoFile = createPhotoFile()
                    pathToFile = photoFile.absolutePath
                    val photoURI = FileProvider.getUriForFile(this, "com.example.Kuglll.shows_mark.fileprovider", photoFile)
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
                }
            }
        }else{ //request for both permissions
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE), CAMERA_PERMISSION_REQUEST)
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
            ShowActivity.storage.drawable = drawable
        }
    }

    override fun onResume() {
        super.onResume()
        var drawable = ShowActivity.storage.drawable
        if (drawable != null){
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
                Toast.makeText(this, "To use camera, we need your permissions", Toast.LENGTH_LONG).show()
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
