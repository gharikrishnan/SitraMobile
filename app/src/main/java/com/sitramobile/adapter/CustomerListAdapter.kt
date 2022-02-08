package com.sitramobile.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.sitramobile.R
import com.sitramobile.activity.CategoryMenuList
import com.sitramobile.modelResponse.CustomerModel
import com.sitramobile.utils.Constants
import com.sitramobile.utils.Helper.setCustomerID
import com.sitramobile.utils.MyFunctions

class CustomerListAdapter constructor(var context: Context, var list_data: List<CustomerModel?>?) :
    RecyclerView.Adapter<CustomerListAdapter.ViewHolder>()
{
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View =
            LayoutInflater.from(context).inflate(R.layout.customer_list_row, parent, false)
        return ViewHolder(view)
    }

    public override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int
    ) {
        val model: CustomerModel? = list_data!!.get(position)
        holder.customer_name.setText(model!!.name)
        holder.address.setText(model!!.address1 + ", " + model!!.city + ", " + model!!.state + "-" + model!!.pincode)
        holder.mobile_number.setText("Mob : " + model!!.mobile + "\n" + "Ph  : " + model!!.phone)
        holder.itemView.setOnClickListener {
            MyFunctions.setStringSharedPref(context, Constants.CUSTOMER_ID, model.custid)
            context.setCustomerID(model.custid.toString())
            context.startActivity(Intent(context, CategoryMenuList::class.java))
        }
    }

    public override fun getItemCount(): Int
    {
        return list_data!!.size
    }

    inner class ViewHolder constructor(itemView: View) : RecyclerView.ViewHolder(itemView)
    {
        var customer_name: TextView
        var address: TextView
        var mobile_number: TextView

        init {
            customer_name = itemView.findViewById<View>(R.id.customer_name) as TextView
            address = itemView.findViewById<View>(R.id.address) as TextView
            mobile_number = itemView.findViewById<View>(R.id.mobile_number) as TextView
        }
    }
}