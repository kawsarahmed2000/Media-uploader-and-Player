package com.vasukammedia.kawsar

import android.app.Dialog
import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.view.View
import android.view.Window
import android.view.inputmethod.InputMethodManager
import android.webkit.MimeTypeMap
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.vasukammedia.kawsar.adapter.ViewPagerAdapter
import com.vasukammedia.kawsar.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity(){

    lateinit var dialog:Dialog
    var uploadv: Button? = null
    var progressDialog: ProgressDialog? = null

    var mediaType=""
    var mediaId=""

    private lateinit var binding: ActivityMainBinding
    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        tablLayout()
        floatingActionButtons()

    }


    @RequiresApi(Build.VERSION_CODES.M)
    private fun floatingActionButtons(){

        val addFab = findViewById<ExtendedFloatingActionButton>(R.id.add_fab)

        val addAudioBtn = findViewById<FloatingActionButton>(R.id.add_audio_fab)
        val addVideoBtn = findViewById<FloatingActionButton>(R.id.add_video_fab)

        val addAudioText = findViewById<TextView>(R.id.add_audio_action_text)
        val addVideoText = findViewById<TextView>(R.id.add_video_action_text)


        var isAllFabVisible: Boolean? = null


        addAudioBtn.visibility = View.GONE
        addVideoBtn.visibility = View.GONE
        addAudioText.visibility = View.GONE
        addVideoText.visibility = View.GONE

        isAllFabVisible = false

        addFab.shrink()

        addFab.setOnClickListener {
            isAllFabVisible = if (!isAllFabVisible!!) {

                addAudioBtn.show()
                addVideoBtn.show()
                addAudioText.visibility = View.VISIBLE
                addVideoText.visibility = View.VISIBLE

                addFab.extend()

                true
            } else {

                addAudioBtn.hide()
                addVideoBtn.hide()
                addAudioText.visibility = View.GONE
                addVideoText.visibility = View.GONE

                addFab.shrink()

                false
            }
        }

        addAudioBtn.setOnClickListener {

            dialog = Dialog(this, android.R.style.ThemeOverlay_Material_Dialog)
            //We have added a title in the custom layout. So let's disable the default title.
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            //The user will be able to cancel the dialog bu clicking anywhere outside the dialog.
            dialog.setCancelable(false)
            //Mention the name of the layout of your custom dialog.
            dialog.setContentView(R.layout.upload_media_layout)
            val closeBtn = dialog.findViewById<ImageView>(R.id.closeBtn)
            val thumbnailMedia = dialog.findViewById<ImageView>(R.id.thumbnailMedia)
            val addIV = dialog.findViewById<ImageView>(R.id.addImage)
            val uploadMediaTV = dialog.findViewById<TextView>(R.id.uploadMediaTV)
            val addMediaBtn = dialog.findViewById<Button>(R.id.addMediaBtn)
            val uploadMedia = dialog.findViewById<LinearLayout>(R.id.uploadMedia)
            val title = dialog.findViewById<EditText>(R.id.mediaTitle)
            val origin = dialog.findViewById<EditText>(R.id.mediaOriginName)
            val addTVLabel = dialog.findViewById<TextView>(R.id.addTVLabel)
            addTVLabel.text="Add Audio"
            addMediaBtn.text="Add Audio"
            uploadMediaTV.text="Upload Audio"
            title.hint="Title"
            origin.hint="Album/Film Name"

            uploadMedia.setOnClickListener {
                addImg=addIV
                uploadMediaTVs=uploadMediaTV
                progressDialog = ProgressDialog(this);
                choosevideo("audio");
                mediaType="audio"
                mediaId=System.currentTimeMillis().toString()
            }
            closeBtn.setOnClickListener {
                dialog.dismiss()
            }
            addMediaBtn.setOnClickListener {
                if (title.text.isNotEmpty()){
                    if (origin.text.isNotEmpty()){
                        if(mediaUri!=null){
                            uploadMedia(title.text, origin.text, )
                        }else{
                            Toast.makeText(this, "Select $mediaType first", Toast.LENGTH_SHORT).show()
                            uploadMedia.performClick()
                        }
                    }else{
                        origin.error="Required"
                        origin.requestFocus()
                        origin.showSoftKeyboard()
                    }
                }else{
                    title.error="Required"
                    title.requestFocus()
                    title.showSoftKeyboard()
                }
            }
            dialog.show()

            Toast.makeText(applicationContext, "Audio clicked", Toast.LENGTH_SHORT).show()
        }
        addVideoBtn.setOnClickListener {

            dialog = Dialog(this, android.R.style.ThemeOverlay_Material_Dialog)
            //We have added a title in the custom layout. So let's disable the default title.
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            //The user will be able to cancel the dialog bu clicking anywhere outside the dialog.
            dialog.setCancelable(false)
            //Mention the name of the layout of your custom dialog.
            dialog.setContentView(R.layout.upload_media_layout)
            val closeBtn = dialog.findViewById<ImageView>(R.id.closeBtn)
            val thumbnailMedia = dialog.findViewById<ImageView>(R.id.thumbnailMedia)
            val addIV = dialog.findViewById<ImageView>(R.id.addImage)
            val uploadMediaTV = dialog.findViewById<TextView>(R.id.uploadMediaTV)
            val addMediaBtn = dialog.findViewById<Button>(R.id.addMediaBtn)
            val uploadMedia = dialog.findViewById<LinearLayout>(R.id.uploadMedia)
            val title = dialog.findViewById<EditText>(R.id.mediaTitle)
            val origin = dialog.findViewById<EditText>(R.id.mediaOriginName)
            val addTVLabel = dialog.findViewById<TextView>(R.id.addTVLabel)
            addTVLabel.text="Add Video"
            addMediaBtn.text="Add Video"
            uploadMediaTV.text="Upload Video"
            title.hint="Title"
            origin.hint="Sub title"
            uploadMedia.setOnClickListener {
                addImg=addIV
                uploadMediaTVs=uploadMediaTV
                progressDialog = ProgressDialog(this);
                choosevideo("video");
                mediaType="video"
                mediaId=System.currentTimeMillis().toString()
            }
            closeBtn.setOnClickListener {
                dialog.dismiss()
            }
            addMediaBtn.setOnClickListener {
                if (title.text.isNotEmpty()){
                    if (origin.text.isNotEmpty()){
                        if(mediaUri!=null){
                            uploadMedia(title.text, origin.text, )
                        }else{
                            Toast.makeText(this, "Select $mediaType first", Toast.LENGTH_SHORT).show()
                            uploadMedia.performClick()
                        }
                    }else{
                        origin.error="Required"
                        origin.requestFocus()
                        origin.showSoftKeyboard()
                    }
                }else{
                    title.error="Required"
                    title.requestFocus()
                    title.showSoftKeyboard()
                }
            }
            dialog.show()

            Toast.makeText(applicationContext, "video Clicked", Toast.LENGTH_SHORT).show()
        }

    }

    // choose a video from phone storage
    private fun choosevideo(s: String) {
        val intent = Intent()
        intent.type = "$s/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(intent, 5)
    }

    var mediaUri: Uri? = null

    var addImg:ImageView?=null
    var uploadMediaTVs:TextView?=null

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 5 && resultCode == RESULT_OK) {
            mediaUri = data!!.data
            val filePath = mediaUri!!.path
            uploadMediaTVs!!.text="Selected"
            uploadMediaTVs!!.setTextColor(ContextCompat.getColor(applicationContext,R.color.green))
            addImg!!.setImageResource(R.drawable.media_added)
            addImg!!.setColorFilter(ContextCompat.getColor(applicationContext,R.color.green))

        }
    }

    private fun getfiletype(mediaUri: Uri): String? {
        val r = contentResolver
        // get the file type ,in this case its mp4
        val mimeTypeMap = MimeTypeMap.getSingleton()
        return mimeTypeMap.getExtensionFromMimeType(r.getType(mediaUri))
    }

    private fun uploadMedia(title: Editable, origin: Editable) {
        if (mediaUri != null) {
            progressDialog!!.setTitle("Uploading...")
            progressDialog!!.show()
            // save the selected video in Firebase storage
            val db = FirebaseStorage.getInstance().getReference("data").child(mediaType).child("$mediaId")
            db.putFile(mediaUri!!).addOnSuccessListener { taskSnapshot ->

                db.downloadUrl.addOnCompleteListener {

                    val mediaUrl = it.result
                    val data:HashMap<String,Any> = HashMap()
                    data["title"]= title.toString()
                    data["filmName"]= origin.toString()
                    data["url"]= mediaUrl
                    data["thumbnail"]= "https://www.simplilearn.com/ice9/free_resources_article_thumb/what_is_image_Processing.jpg"
                    data["visibility"]= "show"
                    data["id"]= mediaId

                    val fDb = FirebaseFirestore.getInstance()
                    fDb.collection("MediaCollection").document("$mediaType").collection("CustomUpload").document(mediaId)
                        .set(data).addOnSuccessListener {
                            progressDialog!!.setMessage("Data is storing")
                            progressDialog!!.dismiss()
                            dialog.hide()
                            binding.addFab.performClick()
                            Toast.makeText(this@MainActivity, "$mediaType Uploaded!!", Toast.LENGTH_SHORT).show()
                        }
                }


            }.addOnFailureListener { e ->

                progressDialog!!.dismiss()
                Toast.makeText(this@MainActivity, "Failed " + e.message, Toast.LENGTH_SHORT).show()

            }.addOnProgressListener { taskSnapshot ->

                val progress =
                    100.0 * taskSnapshot.bytesTransferred / taskSnapshot.totalByteCount
                progressDialog!!.setMessage("Uploaded " + progress.toInt() + "%")
            }
        }
    }


    fun EditText.showSoftKeyboard() {
        (this.context.getSystemService(AppCompatActivity.INPUT_METHOD_SERVICE) as InputMethodManager)
            .showSoftInput(this, InputMethodManager.SHOW_IMPLICIT)
    }
    private fun tablLayout() {

        val tabLayout = findViewById<TabLayout>(R.id.tab_layout)
        val viewPager2 = findViewById<ViewPager2>(R.id.view_pager_2)

        val adapter = ViewPagerAdapter(this.supportFragmentManager, lifecycle)
        viewPager2.adapter = adapter

        TabLayoutMediator(tabLayout, viewPager2) { tab, position ->
            when (position) {
                0 -> {
                    tab.text = "Audio"
                }
                1 -> {
                    tab.text = "Video"
                }
            }
        }.attach()

    }




}


