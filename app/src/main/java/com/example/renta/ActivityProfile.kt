package com.example.renta

import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class ActivityProfile : AppCompatActivity() {

    private lateinit var imageView: ImageView
    private lateinit var button: FloatingActionButton
    private lateinit var TitleName: TextView
    private lateinit var TitleEmail: TextView
    private lateinit var emailLabel2: TextView
    private lateinit var mobileLabel2: TextView
    private lateinit var addressLabel2: TextView
    private lateinit var databaseReference: DatabaseReference
    private lateinit var auth: FirebaseAuth


    fun onBackButtonClick(view: View?) {
        onBackPressed()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        TitleName = findViewById(R.id.TitleName)
        TitleEmail = findViewById(R.id.TitleEmail)
        emailLabel2 = findViewById(R.id.emailLabel2)
        mobileLabel2 = findViewById(R.id.mobileLabel2)
        addressLabel2 = findViewById(R.id.addressLabel2)
        imageView = findViewById(R.id.userProfPic)
        button = findViewById(R.id.floatingActionButton2)

        auth = FirebaseAuth.getInstance()
        databaseReference = FirebaseDatabase.getInstance().reference.child("users").child(auth.currentUser?.uid ?: "")

        databaseReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val name = snapshot.child("name").getValue(String::class.java)
                val email = snapshot.child("email").getValue(String::class.java)
                val phoneNumber = snapshot.child("phoneNumber").getValue(String::class.java)

                // Update UI with fetched data
                TitleName.text = name
                TitleEmail.text = email
                emailLabel2.text = email
                mobileLabel2.text = phoneNumber
                Glide.with(this@ActivityProfile)
                    .load(R.drawable.ic_account) // Default image resource
                    .into(imageView)

            }
                override fun onCancelled(error: DatabaseError) {

            }
        })


            supportActionBar?.setBackgroundDrawable(ColorDrawable(resources.getColor(R.color.white)))  // Set ActionBar background color

        button.setOnClickListener{

            ImagePicker.with(this)
                .cropSquare()	    			//Crop image(Optional), Check Customization for more option
                .compress(1024)			//Final image size will be less than 1 MB(Optional)
                .maxResultSize(1080, 1080)	//Final image resolution will be less than 1080 x 1080(Optional)
                .start()
        }
    }



    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        imageView.setImageURI(data?.data)
    }
}
