package irfan.sampling.latihanfirebaselogin

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.add_data.*


/**
 *   created by Irfan Assidiq on 5/3/19
 *   email : assidiq.irfan@gmail.com
 **/
class Add_Data_Act : AppCompatActivity() {
    lateinit var refDb : DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.add_data)

        refDb = FirebaseDatabase.getInstance().getReference("USERS")

        btnSave.setOnClickListener {
            saveData()
        }
    }

    private fun saveData() {
        val nama = inputNama.text.toString()
        val status = inputStatus.text.toString()

        val user = Users(nama, status)
        val userId = refDb.push().key.toString()

        refDb.child(userId).setValue(user).addOnCompleteListener{
            Toast.makeText(this, "Successs",Toast.LENGTH_SHORT).show()
            inputNama.setText("")
            inputStatus.setText("")
        }
    }
}