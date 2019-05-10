package irfan.sampling.latihanfirebaselogin

import android.app.ProgressDialog
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import com.google.firebase.database.*
import irfan.sampling.latihanfirebaselogin.AdapterC.RCAdapter
import kotlinx.android.synthetic.main.activity_display.*


/**
 *   created by Irfan Assidiq on 5/4/19
 *   email : assidiq.irfan@gmail.com
 **/
class DisplaysAllImage: AppCompatActivity() {
    lateinit var dbRef : DatabaseReference
    lateinit var adapter : RecyclerView.Adapter<RCAdapter.ViewHolder>
    var list : MutableList<ImageUploadInfo> = ArrayList<ImageUploadInfo>()
    lateinit var progress : ProgressDialog
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_display)
        progress = ProgressDialog(this).apply {
            setTitle("Plese Wait!!")
            setMessage("Loading Images From Firebase.")
            setCancelable(false)
            setCanceledOnTouchOutside(false)
            show()
        }
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(this@DisplaysAllImage)

        dbRef = FirebaseDatabase.getInstance().getReference("IMAGES")

        dbRef.addValueEventListener(
            object :ValueEventListener{
                override fun onCancelled(p0: DatabaseError) {
                    progress.dismiss()
                }

                override fun onDataChange(p0: DataSnapshot) {
                    for (postSnapshot in p0.getChildren()) {

                        val imageUploadInfo = postSnapshot.getValue(ImageUploadInfo::class.java)

                        list.add(imageUploadInfo!!)
                        adapter = RCAdapter(applicationContext, list)

                        recyclerView.adapter = adapter

                        progress.dismiss()
                    }
                }

            } )
    }
}