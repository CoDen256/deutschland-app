package de.app.ui.service.data.result

import android.net.Uri
import android.os.Bundle

/**
 * User details post authentication that is exposed to the UI
 */
data class FormView(
    val uri: Result<Uri>,
    val bundle: Result<Bundle>
)


