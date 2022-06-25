package de.app.ui.util

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import de.app.data.model.FileHeader
import de.app.databinding.FragmentDataSafeFileItemBinding

class FileViewAdapter(
    context: Context,
    fileHeaders: List<FileHeader>
)  : ListViewAdapter<FileHeader, FragmentDataSafeFileItemBinding>(
    {inflater, parent -> FragmentDataSafeFileItemBinding.inflate(inflater, parent, false) },
    fileHeaders,
    {file, binding ->
        binding.file.setOnClickListener {
            context.openFile(file.fileUri, file.mimeType)
        }
        binding.fileName.text = file.name
    }
)