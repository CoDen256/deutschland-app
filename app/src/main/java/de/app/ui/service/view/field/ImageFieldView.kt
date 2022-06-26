package de.app.ui.service.view.field

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.squareup.picasso.Picasso
import de.app.api.service.form.ImageField
import de.app.databinding.ApplicationFormImageBinding
import de.app.ui.util.loadImageFromUrl
import java.util.concurrent.Executors


class ImageFieldView (  private val binding: ApplicationFormImageBinding
): FieldView {

    class Inflater {
        private lateinit var binding: ApplicationFormImageBinding

        fun inflate(inflater: LayoutInflater, parent: ViewGroup): Inflater = apply {
            binding = ApplicationFormImageBinding.inflate(inflater, parent, true)
        }

        fun populate(field: ImageField, fragment: Fragment): Inflater = apply {
            binding.label.text = field.label

            Picasso.get()
                .load(field.imageUri)
                .into(binding.image)
        }

        fun build() = ImageFieldView(binding)
    }
}