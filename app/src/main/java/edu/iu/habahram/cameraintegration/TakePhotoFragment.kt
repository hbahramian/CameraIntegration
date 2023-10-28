package edu.iu.habahram.cameraintegration

import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import edu.iu.habahram.cameraintegration.databinding.FragmentTakePhotoBinding
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date


/**
 * A simple [Fragment] subclass.
 * Use the [TakePhotoFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class TakePhotoFragment : Fragment() {
    private var uri: Uri? = null
    val TAG = "TakePhotoFragment"
    private var _binding: FragmentTakePhotoBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentTakePhotoBinding.inflate(inflater, container, false)
        val view = binding.root
        val photoFile = createImageFile()
        try {
            uri = FileProvider.getUriForFile(this.requireContext(), "edu.iu.habahram.fileprovider", photoFile)

        } catch (e: Exception) {
            Log.e(TAG, "Error: ${e.message}")
        }
        val pickMedia = registerForActivityResult(ActivityResultContracts.TakePicture()) { success ->
            // Callback is invoked after the user takes a picture or closes the
            // camera application.
            if (success) {
                Log.d(TAG, "Image location : $uri")
                Glide.with(this.requireContext()).load(uri).into(binding.imageView)
            } else {
                Log.e(TAG, "Image not saved.")
            }
        }
        binding.btnTakePicture.setOnClickListener {
            Log.i(TAG, "Open up the camera application on device")
            // Launch the camera application
            pickMedia.launch(uri)
        }
        return view
    }

    fun createImageFile() : File {
        var timestamp = SimpleDateFormat("yyyyMMdd_HHmmss")
            .format(Date())
        val imageDirectory = this.context?.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile("file_${timestamp}"
            , ".jpg"
            , imageDirectory)
    }


}