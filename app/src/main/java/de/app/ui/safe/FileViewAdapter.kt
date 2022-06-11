package de.app.ui.safe

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import de.app.data.model.FileHeader
import de.app.databinding.FragmentDataSafeFileItemBinding
import de.app.ui.util.openFile

class FileViewAdapter(
    private val context: Context,
    private val fileHeaders: List<FileHeader>
) : RecyclerView.Adapter<FileViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            FragmentDataSafeFileItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val fileHeader: FileHeader = fileHeaders[position]
        holder.fileView.setOnClickListener {
            context.openFile(Uri.parse(fileHeader.fileUri), fileHeader.mimeType)
        }
//        val subjectView = holder.subjectView
//        subjectView.text = message.subject
    }

    override fun getItemCount(): Int = fileHeaders.size

    inner class ViewHolder(binding: FragmentDataSafeFileItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val fileView: ImageView = binding.file
    }

}