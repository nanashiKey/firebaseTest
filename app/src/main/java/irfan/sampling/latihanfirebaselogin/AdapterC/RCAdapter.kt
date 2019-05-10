package irfan.sampling.latihanfirebaselogin.AdapterC

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import irfan.sampling.latihanfirebaselogin.ImageUploadInfo
import irfan.sampling.latihanfirebaselogin.R


/**
 *   created by Irfan Assidiq on 5/4/19
 *   email : assidiq.irfan@gmail.com
 **/
class RCAdapter : RecyclerView.Adapter<RCAdapter.ViewHolder>{

    lateinit var context : Context
    lateinit var mainImageUploadInfo : List<ImageUploadInfo>

    constructor(){

    }
    constructor(ctx : Context, Templist : List<ImageUploadInfo> ){
        this.context = ctx
        this.mainImageUploadInfo = Templist
    }

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): RCAdapter.ViewHolder {
        var view : View = LayoutInflater.from(p0.context).inflate(R.layout.rc_items, p0,
            false)
        var viewHolder = ViewHolder(view)
        return viewHolder
    }

    override fun getItemCount(): Int {
        return mainImageUploadInfo.size
    }

    override fun onBindViewHolder(p0: RCAdapter.ViewHolder, p1: Int) {
        var uploadInfo : ImageUploadInfo = mainImageUploadInfo.get(p1)
        p0.imageNameTextView.setText(uploadInfo.imageName)

        Glide.with(context).load(uploadInfo.imageURL).into(p0.imageView)

    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var imageView: ImageView
        var imageNameTextView: TextView

        init {

            imageView = itemView.findViewById(R.id.imageView) as ImageView

            imageNameTextView = itemView.findViewById(R.id.ImageNameTextView) as TextView
        }
    }
}