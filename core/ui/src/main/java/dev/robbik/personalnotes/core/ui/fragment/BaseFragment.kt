package dev.robbik.personalnotes.core.ui.fragment

import android.widget.Toast
import androidx.fragment.app.Fragment


abstract class BaseFragment : Fragment() {
    fun showToast(message: String) =
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
}