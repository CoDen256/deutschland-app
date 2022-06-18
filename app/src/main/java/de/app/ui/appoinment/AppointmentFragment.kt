package de.app.ui.appoinment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import dagger.hilt.android.AndroidEntryPoint
import de.app.api.appointment.AppointmentService
import de.app.databinding.FragmentAppointmentBinding
import javax.inject.Inject

@AndroidEntryPoint
class AppointmentFragment : Fragment() {

    @Inject
    lateinit var appointmentService: AppointmentService
    private lateinit var binding: FragmentAppointmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAppointmentBinding.inflate(inflater, container, false)
        return binding.root
    }
}