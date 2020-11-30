package com.cj.chatlove_app.ui.message

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.cj.chatlove_app.R
import com.cj.chatlove_app.itf.OnItemClickMessage
import com.cj.chatlove_app.model.Message
import com.cj.chatlove_app.model.User
import com.google.firebase.auth.FirebaseAuth
import com.rishabhharit.roundedimageview.RoundedImageView
import de.hdodenhof.circleimageview.CircleImageView

class MessageAdapter(
    private val mContext: Context,
    private val mList: List<Message>,
    private val mListener: OnItemClickMessage
) : RecyclerView.Adapter<MessageAdapter.ItemViewHolder>() {

    val MSG_TYPE_LEFT = 1
    val MSG_TYPE_RIGHT = 0
    private var myClipboard: ClipboardManager? = null
    private var myClip: ClipData? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        var view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_message_chat, parent, false)
        myClipboard = mContext.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        return ItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        var myuid = FirebaseAuth.getInstance().currentUser!!.uid
        holder.mCIMVAvatarLeft.setOnClickListener { mListener.onAvatar() }

        if (mList[position].sender == myuid) {
            setMessageRight(holder, position)
        } else {
            setMessageLeft(holder, position)
        }
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    private fun setMessageRight(holder: MessageAdapter.ItemViewHolder, position: Int) {
        holder.mRltRight.visibility = View.VISIBLE
        holder.mRltLeft.visibility = View.GONE
        holder.mTVTimeRight.setText("${mList[position].timestamp}")
        if (mList[position].message.equals("")) {
            holder.mRIVImageRight.visibility = View.VISIBLE
            holder.mTVSMSDetailRight.visibility = View.GONE
            Glide.with(mContext).load(mList[position].message).into(holder.mRIVImageRight)
            holder.mRIVImageRight.setOnClickListener { mListener.onImage(mList[position].message) }
        } else {
            holder.mRIVImageRight.visibility = View.GONE
            holder.mTVSMSDetailRight.visibility = View.VISIBLE

            holder.mTVSMSDetailRight.setText(mList[position].message)

            holder.mTVSMSDetailRight.setOnLongClickListener {
                myClipboard?.text = mList[position].message
                Toast.makeText(
                    mContext, "Đã sao chép",
                    Toast.LENGTH_SHORT
                ).show();
                return@setOnLongClickListener true
            }
        }
    }

    private fun setMessageLeft(holder: ItemViewHolder, position: Int) {
        holder.mRltLeft.visibility = View.VISIBLE
        holder.mRltRight.visibility = View.GONE
        holder.mTVTimeLeft.setText("${mList[position].timestamp}")
        if (mList[position].message.equals("")) {
            holder.mRIVImageLeft.visibility = View.VISIBLE
            holder.mTVSMSDetailLeft.visibility = View.GONE
            Glide.with(mContext).load(mList[position].message).into(holder.mRIVImageLeft)
            holder.mRIVImageLeft.setOnClickListener { mListener.onImage(mList[position].message) }
        } else {
            holder.mRIVImageLeft.visibility = View.GONE
            holder.mTVSMSDetailLeft.visibility = View.VISIBLE

            holder.mTVSMSDetailLeft.setText(mList[position].message)

            holder.mTVSMSDetailLeft.setOnLongClickListener {
                myClipboard?.text = mList[position].message
                Toast.makeText(
                    mContext, "Đã sao chép",
                    Toast.LENGTH_SHORT
                ).show();
                return@setOnLongClickListener true
            }
        }
    }

    class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var mRltLeft: RelativeLayout
        var mCIMVAvatarLeft: CircleImageView
        var mTVSMSDetailLeft: TextView
        var mRIVImageLeft: RoundedImageView
        var mTVTimeLeft: TextView
        var mRltRight: RelativeLayout
        var mTVSMSDetailRight: TextView
        var mRIVImageRight: RoundedImageView
        var mTVTimeRight: TextView

        init {
            mRltLeft = itemView.findViewById(R.id.mRltLeft)
            mCIMVAvatarLeft = itemView.findViewById(R.id.mCIMVAvatarLeft)
            mTVSMSDetailLeft = itemView.findViewById(R.id.mTVSMSDetailLeft)
            mRIVImageLeft = itemView.findViewById(R.id.mRIVImageLeft)
            mTVTimeLeft = itemView.findViewById(R.id.mTVTimeLeft)
            mRltRight = itemView.findViewById(R.id.mRltRight)
            mTVSMSDetailRight = itemView.findViewById(R.id.mTVSMSDetailRight)
            mRIVImageRight = itemView.findViewById(R.id.mRIVImageRight)
            mTVTimeRight = itemView.findViewById(R.id.mTVTimeRight)
        }
    }
}