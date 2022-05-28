package de.app.ui.service.view.field

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import de.app.data.Result
import de.app.data.model.service.form.ImageField
import de.app.databinding.ApplicationFormImageBinding
import de.app.ui.util.getImage
import java.net.HttpURLConnection
import java.net.URL
import java.util.concurrent.Executors


class ImageFieldView (  private val binding: ApplicationFormImageBinding
): FieldView {

    override fun getView(): View {
        return binding.root
    }

    class Inflater {
        private lateinit var binding: ApplicationFormImageBinding

        fun inflate(inflater: LayoutInflater, parent: ViewGroup): Inflater = apply {
            binding = ApplicationFormImageBinding.inflate(inflater, parent, false)
        }

        fun populate(field: ImageField, fragment: Fragment): Inflater = apply {
            binding.label.text = field.label
            Executors.newSingleThreadExecutor().execute {

                val result = getImage(field.imageUrl)

                fragment.requireActivity().runOnUiThread {
                    when(result){
                        is Result.Success ->
                            binding.image.setImageBitmap(result.data)
                        is Result.Error ->
                            binding.label.error = "Failed to download image: ${result.exception.message}"
                    }
                }
            }
        }

        fun build() = ImageFieldView(binding)
    }
}