package de.app.ui.components

import android.graphics.Rect
import android.view.*
import de.app.R

class FileContextMenu(
    private val onRemoveClicked: () -> Unit,
    private val onDownloadClicked: () -> Unit,
    ): ActionMode.Callback2() {
    override fun onCreateActionMode(mode: ActionMode, menu: Menu): Boolean {
        val inflater: MenuInflater = mode.menuInflater
        inflater.inflate(R.menu.file_actions, menu)
        return true
    }

    override fun onPrepareActionMode(mode: ActionMode, menu: Menu): Boolean {
        return false
    }

    override fun onActionItemClicked(mode: ActionMode, item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.remove_file -> {
                onRemoveClicked()
                mode.finish() // Action picked, so close the CAB
                true
            }
            R.id.download_file -> {
                onDownloadClicked()
                mode.finish()
                true
            }
            else -> false
        }
    }

    override fun onDestroyActionMode(mode: ActionMode?) {}

    override fun onGetContentRect(mode: ActionMode, view: View, outRect: Rect) {
        outRect.set(0, 0, 0, 0)
    }
}