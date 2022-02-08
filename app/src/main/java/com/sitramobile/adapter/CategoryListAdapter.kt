package com.sitramobile.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.sitramobile.R
import com.sitramobile.activity.DynamicForm
import com.sitramobile.activity.Machinematerdata
import com.sitramobile.modelResponse.CategoryModel

class CategoryListAdapter constructor(var context: Context, var list_data: List<CategoryModel>) :
    RecyclerView.Adapter<CategoryListAdapter.ViewHolder>() {

    var selectedPosition: Int = -1
    public override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View =
            LayoutInflater.from(context).inflate(R.layout.category_list_row, parent, false)
        return ViewHolder(view)
    }

    public override fun onBindViewHolder(holder: ViewHolder, @SuppressLint("RecyclerView") position: Int) {
        holder.category_name.setText(list_data.get(position).ScreenName)
        if (selectedPosition == position) {
            holder.itemView.setBackgroundColor(
                context.getResources().getColor(R.color.colorPrimary)
            )
            holder.category_name.setTextColor(context.getResources().getColor(R.color.white))
            DrawableCompat.setTint(
                DrawableCompat.wrap(holder.icon_drop.getDrawable()),
                ContextCompat.getColor(context, R.color.white)
            )
        } else {
            holder.itemView.setBackgroundColor(context.getResources().getColor(R.color.white))
            holder.category_name.setTextColor(context.getResources().getColor(R.color.black))
            DrawableCompat.setTint(
                DrawableCompat.wrap(holder.icon_drop.getDrawable()),
                ContextCompat.getColor(context, R.color.colorPrimary)
            )
        }

        holder.itemView.setOnClickListener(object : View.OnClickListener {
            public override fun onClick(v: View) {

                val i: Intent = Intent(context, DynamicForm::class.java)
                i.putExtra("category_name", list_data.get(position).ScreenName)
                i.putExtra("screen_no", list_data.get(position).ScreenNo)
                //i.putExtra("PT_id", list_data.get(position).Machinery)
                Log.e("category_name",list_data.get(position).ScreenName)
                Log.e("screen_no",list_data.get(position).ScreenNo)
                Log.e("Machinery",list_data.get(position).Machinery)
                context.startActivity(i)
                selectedPosition = position
                notifyDataSetChanged()

                /*MaterialAlertDialogBuilder(context)
                    .setTitle("Select Field")
                    .setMessage("Please Select the Form")
//                    .setNeutralButton(resources.getString(R.string.cancel)) { dialog, which ->
//                        // Respond to neutral button press
//                    }
                    .setNegativeButton("Form Data") { dialog, which ->
                        dialog.dismiss()
                        // Respond to negative button press
                        val i: Intent = Intent(context, DynamicForm::class.java)
                        i.putExtra("category_name", list_data.get(position).ScreenName)
                        i.putExtra("screen_no", list_data.get(position).ScreenNo)
                        i.putExtra("PT_id", list_data.get(position).Machinery)
                        Log.e("category_name",list_data.get(position).ScreenName)
                        Log.e("screen_no",list_data.get(position).ScreenNo)
                        context.startActivity(i)
                        selectedPosition = position
                        notifyDataSetChanged()

                    }
                    .setPositiveButton("Master Data") { dialog, which ->
                        dialog.dismiss()
                        // Respond to positive button press
                        val i: Intent = Intent(context, Machinematerdata::class.java)
                        i.putExtra("category_name", list_data.get(position).ScreenName)
                        i.putExtra("screen_no", list_data.get(position).ScreenNo)
                        Log.e("category_name",list_data.get(position).ScreenName)
                        Log.e("screen_no",list_data.get(position).ScreenNo)
                        context.startActivity(i)
                        selectedPosition = position
                        notifyDataSetChanged()
                    }
                    .show()*/


            }
        })
    }

    public override fun getItemCount(): Int {
        return list_data.size
    }

    class ViewHolder constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var category_name: TextView
        var icon_drop: ImageView

        init {
            category_name = itemView.findViewById(R.id.category_name)
            icon_drop = itemView.findViewById(R.id.icon_drop)
        }
    }
}