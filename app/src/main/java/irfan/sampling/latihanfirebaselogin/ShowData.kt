package irfan.sampling.latihanfirebaselogin

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.ListView
import com.google.firebase.database.*
import irfan.sampling.latihanfirebaselogin.AdapterC.Adapter
import irfan.sampling.latihanfirebaselogin.Add_Data.Users


/**
 *   created by Irfan Assidiq on 5/3/19
 *   email : assidiq.irfan@gmail.com
 **/
class ShowData : AppCompatActivity() {

    lateinit var refDb : DatabaseReference
    lateinit var list : MutableList<Users>
    lateinit var listView: ListView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.show_data)

        refDb = FirebaseDatabase.getInstance().getReference("USERS")
        list = mutableListOf()
        listView = findViewById(R.id.listView)

        refDb.addValueEventListener(object : ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {
            }

            override fun onDataChange(p0: DataSnapshot) {
                if (p0!!.exists()) {

                    for (h in p0.children) {
                        val user = h.getValue(Users::class.java)
                        list.add(user!!)
                    }
                    val adapter = Adapter(applicationContext, R.layout.show_user, list)
                    listView.adapter = adapter
                }
            }
        })

    }
}