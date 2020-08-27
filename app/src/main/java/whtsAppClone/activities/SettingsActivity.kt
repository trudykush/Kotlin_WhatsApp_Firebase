package whtsAppClone.activities

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import com.kush.learningkotlin.R
import com.squareup.picasso.Picasso
import com.theartofdev.edmodo.cropper.CropImage
import id.zelory.compressor.Compressor
import kotlinx.android.synthetic.main.activity_settings.*
import java.io.ByteArrayOutputStream
import java.io.File

class SettingsActivity : AppCompatActivity() {

    var mDatabase: DatabaseReference? = null
    var mCurrentUser: FirebaseUser? = null
    var mStorageReference:  StorageReference? = null

    var GALLERY_ID: Int = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        mStorageReference = FirebaseStorage.getInstance().reference

        mCurrentUser = FirebaseAuth.getInstance().currentUser
        val userID = mCurrentUser!!.uid

        mDatabase = FirebaseDatabase.getInstance().reference
            .child("Users")
            .child(userID)

        mDatabase!!.addValueEventListener(object : ValueEventListener {

            override fun onDataChange(dataSnapshot: DataSnapshot) {

                val displayName = dataSnapshot.child("DisplayName").value
                var image: String? = dataSnapshot.child("image").value as String?
                val userStatus = dataSnapshot.child("Status").value
                var thumbNail = dataSnapshot.child("thumb_image").value

                settingStatus.text = userStatus.toString()
                settingsDisplayName.text = displayName.toString()

                if (image!! != "default") {
                    Picasso.get()
                        .load(image)
                        .placeholder(R.drawable.profile_img)
                        .into(settingProfileID)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })

        settingsChangeStatus.setOnClickListener{
            val intent = Intent(this, StatusActivity::class.java)
            intent.putExtra("Status", settingStatus.text.toString().trim())
            startActivity(intent)
        }

        settingChangeImgBtn.setOnClickListener {
            val galleryIntent = Intent()
            galleryIntent.type = "image/*"
            galleryIntent.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(Intent.createChooser(galleryIntent, "Select Image"), GALLERY_ID)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == GALLERY_ID && resultCode == Activity.RESULT_OK) {

            var image: Uri? = data!!.data

            CropImage.activity(image)
                .setAspectRatio(1, 1)
                .start(this)
        }

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            val result = CropImage.getActivityResult(data)

            if (resultCode == Activity.RESULT_OK) {
                val resultUri = result.uri

                var userID = mCurrentUser!!.uid
                var thumbFile = File(resultUri.path)

                var thumbBitMap = Compressor(this)
                    .setMaxWidth(200)
                    .setMaxHeight(200)
                    .setQuality(65)
                    .compressToBitmap(thumbFile)

                var byteArray = ByteArrayOutputStream()
                thumbBitMap.compress(Bitmap.CompressFormat.JPEG, 100, byteArray)

                var thumByteArray: ByteArray
                thumByteArray = byteArray.toByteArray()

                var filePath = mStorageReference!!.child("chat_profile_images")
                    .child("$userID.jpg")

                var thumFilePath = mStorageReference!!.child("chat_profile_images")
                    .child("thumbs")
                    .child("$userID.jpg")

                filePath.putFile(resultUri).addOnSuccessListener {
                    filePath.downloadUrl.addOnCompleteListener { uri ->
                        val downUri = uri.result.toString()

                        var uploadTask: UploadTask = thumFilePath.putBytes(thumByteArray)

                        uploadTask.addOnCompleteListener{
                            taskSnapshot ->
                            var url = taskSnapshot.result.toString()

                            if (uri.isSuccessful) {
                                var updateObj = HashMap<String, Any>()
                                updateObj["image"] = downUri
                                updateObj["thumb_image"] = url

                                mDatabase!!.updateChildren(updateObj)
                                    .addOnCompleteListener{
                                            task: Task<Void> ->
                                        if (task.isSuccessful) {
                                            Toast.makeText(this, "Profile Image Saved",
                                                Toast.LENGTH_LONG).show()
                                        } else {

                                        }
                                    }
                            }
                        }
                    }
                }

            }
        }
    }
}