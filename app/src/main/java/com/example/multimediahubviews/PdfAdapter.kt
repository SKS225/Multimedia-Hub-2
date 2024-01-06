package com.example.multimediahubviews

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.io.File


class PdfAdapter(private var list: List<File>, private var activity: Activity): RecyclerView.Adapter<PdfAdapter.ViewHolder>() {

    @SuppressLint("NotifyDataSetChanged")
    fun filterlist(list: List<File>) {
        this.list = list
        this.notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.pdf_rv_item, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val file = list[position]
        holder.name.text = file.name
        holder.size.text = parseFileLength(file.length())
        holder.lastModified.text = convertEpochToDate(file.lastModified())


        holder.itemView.setOnClickListener {
            val intent = Intent(activity, PdfViewerActivity::class.java)
            intent.putExtra("name", file.name)
            intent.putExtra("path", file.path)
            activity.startActivity(intent)
        }
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var name: TextView
        var size: TextView
        var lastModified: TextView
        init {
            name = itemView.findViewById(R.id.pdf_file_name)
            size = itemView.findViewById(R.id.pdf_file_size)
            lastModified = itemView.findViewById(R.id.pdf_last_modified)
        }
    }
}