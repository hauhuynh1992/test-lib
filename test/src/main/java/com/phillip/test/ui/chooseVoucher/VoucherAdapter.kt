package com.phillip.test.ui.chooseVoucher

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.phillip.test.R
import com.phillip.test.models.Voucher
import com.phillip.test.utils.Functions
import kotlinx.android.synthetic.main.item_quickpay_voucher.view.*

class VoucherAdapter(
    activity: Activity,
    list: ArrayList<Voucher>,
    private val onClickText: (product: Voucher) -> Unit,
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val mInflater: LayoutInflater =
        activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    private var mList: ArrayList<Voucher> = list
    private var mActivity: Activity = activity
    var isFocusBottom = false
    var isFocusTop = false


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val v = mInflater
            .inflate(R.layout.item_quickpay_voucher, parent, false)
        v.isFocusable = true
        v.isFocusableInTouchMode = true
        return ItemViewHolder(v)

    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as ItemViewHolder)

        holder.name.text = mList[position].lable
        if (mList[position].condition_type_label.isNotBlank() && mList[position].condition_type != 0) {
            holder.detail.visibility = View.VISIBLE
            holder.detail.text =
                mList[position].condition_type_label + " " + Functions.format(mList[position].condition_value)

        } else if (mList[position].type == 1 && mList[position].max_applied_value > 0) {
            holder.detail.visibility = View.VISIBLE
            holder.detail.text =
                "Tối đa" + " " + Functions.format(mList[position].max_applied_value)

        } else {
            holder.detail.visibility = View.GONE
        }

        holder.itemView.setOnClickListener {
            onClickText.invoke(mList[position])
        }

        holder.itemView.setOnFocusChangeListener { _, hasFocus ->
            holder.con.isSelected = hasFocus

            if (hasFocus) {
                if (position == 0) isFocusTop = true
                if (position == itemCount - 1) isFocusBottom = true

                Functions.animateScaleUp(holder.itemView, 1.05f)
            } else {
                isFocusTop = false
                isFocusBottom = false
                Functions.animateScaleDown(holder.itemView)
            }
        }
    }

    inner class ItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val detail: TextView = view.detail
        val image: ImageView = view.image
        val name: TextView = view.tvName
        val con: LinearLayout = view.con
    }

    override fun getItemCount(): Int {
        return mList.size
    }
}