package com.sitramobile.adapter

import android.annotation.SuppressLint
import android.app.Activity
import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.gson.JsonObject
import com.sitramobile.R
import com.sitramobile.modelRequest.FormDropdownwithlist
import com.sitramobile.modelRequest.Masterdropwithlist
import com.sitramobile.modelResponse.FieldResponse
import com.sitramobile.utils.Helper
import java.util.*

class DynamicFormAdapter(
    var context: Context,
    var list_data: List<FieldResponse?>?,
    var dataList: JsonObject?,
    var dropdownList: List<FormDropdownwithlist?>?
) : RecyclerView.Adapter<DynamicFormAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(context).inflate(R.layout.dynamic_form_adapter, parent, false)
        return ViewHolder(view)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val model = list_data!![position]
        Log.e("",model!!.DataType.toString())

       /* if(model.Field_Name.equals(context.PrimaryKey_Name))
        {
            if (validate(model.Field_Name, dataList)) {
               Helper.PT_Id=dataList!![model.Field_Name].asString
            }else
            {♂
                Helper.PT_Id=null
            }
        }*/

        if(dropdownList!=null && dropdownList!!.size>0) {
            val list=ArrayList<String>()
            for (data in dropdownList!!)
            {
                list.add(data!!.fieldName)
            }

            val found = list.contains(model.Field_Name)
            Log.e("listname",list.toString())
            Log.e("dropdownFieldfound",found.toString()+"-"+model.Field_Name.trim())

            if(found) {
                for (mylist in dropdownList!!) {
                    if (mylist!!.fieldName.equals(model.Field_Name)) {
                        Log.e("model.Field_Name)", model.Field_Name.toString());
                        if ((model.Mandatory == "1")) holder.drop_down_layout.hint = model.DispName + " *"
                        else holder.drop_down_layout.hint = model.DispName

                        if (validate(model.Field_Name, dataList)) {
                            model.fieldValue = dataList!![model.Field_Name].asString
                            holder.drop_down.setText(dataList!![model.Field_Name].asString)
                        }
                        holder.edit_text_layout.visibility = View.GONE
                        holder.edit_numeric_layout.visibility = View.GONE
                        holder.drop_down_layout.visibility = View.VISIBLE
                        holder.date_pick_layout.visibility = View.GONE

                        val mAdapter = ArrayAdapter<String>(context, android.R.layout.simple_dropdown_item_1line, mylist.fieldList)
                        mAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                        holder.drop_down.setAdapter(mAdapter)
                        holder.drop_down.setOnTouchListener(View.OnTouchListener { paramView, paramMotionEvent ->
                            hideKeyboardFrom(context, holder.drop_down)
                            holder.drop_down.showDropDown()
                            holder.drop_down.requestFocus()
                            false
                        })

                        holder.drop_down.onItemClickListener =
                            AdapterView.OnItemClickListener { parent, view, position, id ->
                                hideKeyboardFrom(context, holder.drop_down_layout)
                                model.fieldValue = Objects.requireNonNull(holder.drop_down.text).toString()
                                //Log.e("name", parent.getItemAtPosition(position).toString())
                                // val selectedItem = parent.getItemAtPosition(position).toString()
                            }
                        break;
                    }
                }
            }
            else{
                when (model.DataType.trim { it <= ' ' }) {

                    "Character", "character" -> {

                        if ((model.Mandatory == "1")) holder.edit_text_layout.hint = model.DispName + " *"
                        else holder.edit_text_layout.hint = model.DispName



                        if (validate(model.Field_Name, dataList)) {
                            model.fieldValue = dataList!![model.Field_Name].asString
                            holder.text_field_name.setText(dataList!![model.Field_Name].asString)
                        }

                        holder.edit_text_layout.visibility = View.VISIBLE
                        holder.edit_numeric_layout.visibility = View.GONE
                        holder.drop_down_layout.visibility = View.GONE
                        holder.date_pick_layout.visibility = View.GONE


                    }

                    "Numeric", "numeric" -> {

                        if ((model.Mandatory == "1")) holder.edit_numeric_layout.hint = model.DispName + " *"
                        else holder.edit_numeric_layout.hint = model.DispName
                        if (validate(model.Field_Name, dataList)) {
                            model.fieldValue = dataList!![model.Field_Name].asString
                            holder.number_field_name.setText(dataList!![model.Field_Name].asString)
                        }

                        holder.edit_text_layout.visibility = View.GONE
                        holder.edit_numeric_layout.visibility = View.VISIBLE
                        holder.drop_down_layout.visibility = View.GONE
                        holder.date_pick_layout.visibility = View.GONE
                    }

                    "Date", "date" -> {
                        if ((model.Mandatory == "1")) holder.date_pick_layout.hint = model.DispName + " *"
                        else holder.date_pick_layout.hint = model.DispName

                        if (validate(model.Field_Name, dataList)) {
                            model.fieldValue = dataList!![model.Field_Name].asString
                            holder.date_field.setText(dataList!![model.Field_Name].asString)
                        }
                        holder.edit_text_layout.visibility = View.GONE
                        holder.edit_numeric_layout.visibility = View.GONE
                        holder.drop_down_layout.visibility = View.GONE
                        holder.date_pick_layout.visibility = View.VISIBLE

                        holder.date_field.setOnClickListener(object : View.OnClickListener {
                            override fun onClick(v: View) {
                                val c = Calendar.getInstance()
                                val mYear = c[Calendar.YEAR]
                                val mMonth = c[Calendar.MONTH]
                                val mDay = c[Calendar.DAY_OF_MONTH]
                                val datePickerDialog = DatePickerDialog(
                                    context,
                                    OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                                        holder.date_field.setText(
                                            dayOfMonth.toString() + "-" + (monthOfYear + 1) + "-" + year
                                        )
                                    }, mYear, mMonth, mDay
                                )
                                datePickerDialog.show()
                            }
                        })
                    }

                    else -> {
                        if ((model.Mandatory == "1")) holder.drop_down_layout.hint =
                            model.DispName + " *" else holder.drop_down_layout.hint = model.DispName
                        if (validate(model.Field_Name, dataList)) {
                            model.fieldValue = dataList!![model.Field_Name].asString
                            holder.drop_down.setText(dataList!![model.Field_Name].asString)
                        }
                        holder.edit_text_layout.visibility = View.GONE
                        holder.edit_numeric_layout.visibility = View.GONE
                        holder.drop_down_layout.visibility = View.VISIBLE
                        holder.date_pick_layout.visibility = View.GONE
                    }
                }
            }
        }
        else{
            when (model.DataType.trim { it <= ' ' }) {
                "Character", "character" -> {
                    if ((model.Mandatory == "1")) holder.edit_text_layout.hint = model.DispName + " *"
                    else holder.edit_text_layout.hint = model.DispName
                    if (validate(model.Field_Name, dataList)) {
                        model.fieldValue = dataList!![model.Field_Name].asString
                        holder.text_field_name.setText(dataList!![model.Field_Name].asString)
                    }
                    holder.edit_text_layout.visibility = View.VISIBLE
                    holder.edit_numeric_layout.visibility = View.GONE
                    holder.drop_down_layout.visibility = View.GONE
                    holder.date_pick_layout.visibility = View.GONE
                }
                "Numeric", "numeric" -> {
                    if ((model.Mandatory == "1")) holder.edit_numeric_layout.hint = model.DispName + " *"
                    else holder.edit_numeric_layout.hint = model.DispName
                    if (validate(model.Field_Name, dataList)) {
                        model.fieldValue = dataList!![model.Field_Name].asString
                        holder.number_field_name.setText(dataList!![model.Field_Name].asString)
                    }
                    holder.edit_text_layout.visibility = View.GONE
                    holder.edit_numeric_layout.visibility = View.VISIBLE
                    holder.drop_down_layout.visibility = View.GONE
                    holder.date_pick_layout.visibility = View.GONE
                }

                "Date", "date" -> {
                    if ((model.Mandatory == "1")) holder.date_pick_layout.hint = model.DispName + " *"
                    else holder.date_pick_layout.hint = model.DispName
                    if (validate(model.Field_Name, dataList)) {
                        model.fieldValue = dataList!![model.Field_Name].asString
                        holder.date_field.setText(dataList!![model.Field_Name].asString)
                    }
                    holder.edit_text_layout.visibility = View.GONE
                    holder.edit_numeric_layout.visibility = View.GONE
                    holder.drop_down_layout.visibility = View.GONE
                    holder.date_pick_layout.visibility = View.VISIBLE

                    holder.date_field.setOnClickListener(object : View.OnClickListener {
                        override fun onClick(v: View) {
                            val c = Calendar.getInstance()
                            val mYear = c[Calendar.YEAR]
                            val mMonth = c[Calendar.MONTH]
                            val mDay = c[Calendar.DAY_OF_MONTH]
                            val datePickerDialog = DatePickerDialog(
                                context,
                                OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                                    holder.date_field.setText(
                                        dayOfMonth.toString() + "-" + (monthOfYear + 1) + "-" + year
                                    )
                                }, mYear, mMonth, mDay
                            )
                            datePickerDialog.show()
                        }
                    })
                }

                else -> {
                    if ((model.Mandatory == "1"))
                        holder.drop_down_layout.hint = model.DispName + " *"
                    else holder.drop_down_layout.hint = model.DispName

                    if (validate(model.Field_Name, dataList)) {
                        model.fieldValue = dataList!![model.Field_Name].asString
                        holder.date_field.setText(dataList!![model.Field_Name].asString)
                    }
                    holder.edit_text_layout.visibility = View.GONE
                    holder.edit_numeric_layout.visibility = View.GONE
                    holder.drop_down_layout.visibility = View.VISIBLE
                    holder.date_pick_layout.visibility = View.GONE
                }
            }
           /* holder.date_field.setOnClickListener(object : View.OnClickListener {
                override fun onClick(v: View) {
                    val c = Calendar.getInstance()
                    val mYear = c[Calendar.YEAR]
                    val mMonth = c[Calendar.MONTH]
                    val mDay = c[Calendar.DAY_OF_MONTH]
                    val datePickerDialog = DatePickerDialog(
                        context,
                        OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                            holder.date_field.setText(
                                dayOfMonth.toString() + "-" + (monthOfYear + 1) + "-" + year
                            )
                        }, mYear, mMonth, mDay
                    )
                    datePickerDialog.show()
                }
            })*/
        }

        holder.text_field_name.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                model.fieldValue=Objects.requireNonNull(holder.text_field_name.text).toString()
            }

            override fun afterTextChanged(s: Editable) {}
        })
        holder.number_field_name.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                model.fieldValue= Objects.requireNonNull(holder.number_field_name.text).toString()

            }

            override fun afterTextChanged(s: Editable) {}
        })
        holder.drop_down.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                model.fieldValue=Objects.requireNonNull(holder.drop_down.text).toString()
            }

            override fun afterTextChanged(s: Editable) {}
        })
        holder.date_field.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                model.fieldValue=Objects.requireNonNull(holder.date_field.text).toString()
            }

            override fun afterTextChanged(s: Editable) {}
        })

    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun getItemCount(): Int {
        return list_data!!.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var edit_text_layout: TextInputLayout
        var edit_numeric_layout: TextInputLayout
        var drop_down_layout: TextInputLayout
        var date_pick_layout: TextInputLayout
        var text_field_name: TextInputEditText
        var number_field_name: TextInputEditText
        //var drop_down: TextInputEditText
        var drop_down: AutoCompleteTextView
        var date_field: TextInputEditText

        init {
            edit_text_layout = itemView.findViewById(R.id.edit_text_layout)
            edit_numeric_layout = itemView.findViewById(R.id.edit_numeric_layout)
            drop_down_layout = itemView.findViewById(R.id.drop_down_layout)
            date_pick_layout = itemView.findViewById(R.id.date_pick_layout)
            text_field_name = itemView.findViewById(R.id.text_field_name)
            number_field_name = itemView.findViewById(R.id.number_field_name)
            drop_down = itemView.findViewById(R.id.drop_down)
            date_field = itemView.findViewById(R.id.date_field)
        }
    }

    private fun validate(key: String?, dataList: JsonObject?): Boolean {
        return (dataList != null) && (key != null) && (dataList[key] != null) && (dataList[key].asString != null)
    }

    fun hideKeyboardFrom(context: Context, view: View) {
        val imm = context.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }
}