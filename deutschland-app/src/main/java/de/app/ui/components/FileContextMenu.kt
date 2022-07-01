package de.app.ui.components

import android.content.Context
import android.view.MenuItem
import android.view.View
import android.widget.PopupMenu
import de.app.R

class FileContextMenu(
    private val onRemoveClicked: () -> Unit,
    private val onDownloadClicked: () -> Unit,
    view:View,
    context: Context
    ): PopupMenu.OnMenuItemClickListener {

    val menu = PopupMenu(context, view).apply {
            setOnMenuItemClickListener(this@FileContextMenu)
            inflate(R.menu.file_actions)
        }

    override fun onMenuItemClick(item: MenuItem?): Boolean {
        return when (item?.itemId) {
            R.id.remove_file -> {
                onRemoveClicked()
                true
            }
            R.id.download_file -> {
                onDownloadClicked()
                true
            }
            else -> false
        }
    }

    fun show(){
        menu.show()
    }
}