package irfan.sampling.latihanfirebaselogin

import android.Manifest
import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.webkit.MimeTypeMap
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.android.synthetic.main.upload_type_2.*
import java.io.IOException
import java.util.*


/**
 *   created by Irfan Assidiq on 5/4/19
 *   email : assidiq.irfan@gmail.com
 **/
class UploadType2 : AppCompatActivity() {
    lateinit var btnChoose: Button
    lateinit var btnUpload: Button
    lateinit var imgView : ImageView
    val PICK_IMAGE_REQUEST = 71
    val PERMISSION_REQUEST_CODE = 1001
    var value = 0.0
    lateinit var filepath : Uri
    lateinit var storage : FirebaseStorage
    lateinit var storageReference: StorageReference
    lateinit var dbref : DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.upload_type_2)
        btnChoose = findViewById(R.id.btnChoose)
        btnUpload = findViewById(R.id.btnUpload)
        imgView = findViewById(R.id.imgView)
        storage = FirebaseStorage.getInstance()
        storageReference = storage.reference
        dbref = FirebaseDatabase.getInstance().getReference("IMAGES")

        btnChoose.setOnClickListener {
            when {
                (Build.VERSION.SDK_INT >= Build
                    .VERSION_CODES.M) -> {
                    if (ContextCompat
                    .checkSelfPermission(
                    this@UploadType2,
                    Manifest.permission
                        .READ_EXTERNAL_STORAGE)
                        != PackageManager
                        .PERMISSION_GRANTED) {
                        requestPermissions(arrayOf(
                            Manifest.permission
                                .READ_EXTERNAL_STORAGE),
                            PERMISSION_REQUEST_CODE)
                    }else{
                        chooseImage()
                    }
                }

                else -> chooseImage()
            }
        }

        btnUpload.setOnClickListener {
            uploadImage()
        }

        btnSHOW.setOnClickListener {
            val intent = Intent(this@UploadType2, DisplaysAllImage::class.java)
            startActivity(intent)
        }
    }

    //create method choose image
    fun chooseImage(){
        val intent = Intent().apply {
            type = "image/*"
            action = Intent.ACTION_GET_CONTENT
        }
        startActivityForResult(Intent
            .createChooser(intent,
                "Select Picture"),
            PICK_IMAGE_REQUEST);
    }

    override fun onActivityResult(requestCode: Int,
                                  resultCode: Int,
                                  data: Intent?) {
        super.onActivityResult(requestCode,
            resultCode, data)
        if(requestCode == PICK_IMAGE_REQUEST
            && resultCode == Activity.RESULT_OK
            && data != null && data.data != null){
            filepath = data.data
            try{
                var bitmap : Bitmap = MediaStore.
                    Images.Media.getBitmap(contentResolver,
                    filepath)
                imgView.setImageBitmap(bitmap)
            }catch ( e : IOException){
                e.printStackTrace()
            }
        }
    }

    fun uploadImage(){
        val progress = ProgressDialog(this)
            .apply {
            setTitle("Uploading Picture....")
            setCancelable(false)
            setCanceledOnTouchOutside(false)
            show()
        }
        val namee = UUID.randomUUID().toString()
        var ref : StorageReference =
            storageReference.child("images/"+namee+"."+GetFileExtension(filepath)
            )
        ref.putFile(filepath)
            .addOnSuccessListener {
                taskSnapshot -> progress.dismiss()
                Toast.makeText(this@UploadType2,
                "Uploaded", Toast.LENGTH_SHORT).show();
                val imageUploadInfo = ImageUploadInfo(namee,
                    taskSnapshot.storage.bucket
                )

                // Getting image upload ID.
                val ImageUploadId = dbref.push().getKey()

                // Adding image upload id s child element into databaseReference.
                dbref.child(ImageUploadId!!).setValue(imageUploadInfo)
            }
            .addOnFailureListener{
                e -> progress.dismiss()
            Toast.makeText(this@UploadType2,
        "Failed "+e.message,
                Toast.LENGTH_SHORT).show();
            }.addOnProgressListener {
                taskSnapshot ->
                value  = (100.0*taskSnapshot
                    .getBytesTransferred()/taskSnapshot
                    .getTotalByteCount())
                progress.setMessage("Uploaded.. "
                        + value.toInt() + "%")
            }

    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<out String>,
        grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode,
            permissions, grantResults)
        when (requestCode){
            PERMISSION_REQUEST_CODE -> {
                if(grantResults.isEmpty()
                    || grantResults[0] == PackageManager
                        .PERMISSION_GRANTED)
                    Toast.makeText(
                        this@UploadType2,
                        "Oops! Permission Denied!!",
                        Toast.LENGTH_SHORT).show()
                else
                    chooseImage()
            }
        }
    }

    internal fun GetFileExtension(uri: Uri): String? {

        val contentResolver = contentResolver

        val mimeTypeMap = MimeTypeMap.getSingleton()

        // Returning the file Extension.
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri))

    }
}