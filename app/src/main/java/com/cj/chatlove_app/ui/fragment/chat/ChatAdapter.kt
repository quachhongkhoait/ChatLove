package com.cj.chatlove_app.ui.fragment.chat

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.cj.chatlove_app.R
import com.cj.chatlove_app.itf.OnItemClick
import com.cj.chatlove_app.model.User
import de.hdodenhof.circleimageview.CircleImageView

class ChatAdapter(
    private val mList: List<User>,
    private val mListener: OnItemClick
) : RecyclerView.Adapter<ChatAdapter.ItemViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        var view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_user, parent, false)
        return ItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        var str = mList[position].displayName.toCharArray()[0].toString().toUpperCase()
        holder.tvName.setText(str)
        holder.tvTitle.setText(mList[position].displayName)
//        holder.tvTitle.setText("T")
        holder.tvPhoneNumber.setText(mList[position].email)
        holder.relativeLayout.setOnClickListener {
            mListener.onItem(position)
        }
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var circleImage: CircleImageView
        var relativeLayout: RelativeLayout
        var tvTitle: TextView
        var tvName: TextView
        var tvPhoneNumber: TextView

        init {
            tvName = itemView.findViewById(R.id.tvNameChar)
            tvTitle = itemView.findViewById(R.id.mTVName)
            circleImage = itemView.findViewById(R.id.mCircleImageView)
            relativeLayout = itemView.findViewById(R.id.onclick)
            tvPhoneNumber = itemView.findViewById(R.id.mTVEmail)
        }
    }
}