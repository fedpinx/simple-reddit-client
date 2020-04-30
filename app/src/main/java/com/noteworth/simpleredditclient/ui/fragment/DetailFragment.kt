package com.noteworth.simpleredditclient.ui.fragment

import android.Manifest.permission.WRITE_EXTERNAL_STORAGE
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Bundle
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import com.noteworth.simpleredditclient.Globals.PERMISSION_REQUEST_THUMBNAIL
import com.noteworth.simpleredditclient.Globals.THUMBNAIL_FILE_NAME
import com.noteworth.simpleredditclient.R
import com.noteworth.simpleredditclient.model.RedditPost
import kotlinx.android.synthetic.main.detail_fragment.*
import java.io.File
import java.io.FileOutputStream

class DetailFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View =
        inflater.inflate(R.layout.detail_fragment, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        arguments.let {
            val redditPost = it?.getSerializable(redditPostParam) as RedditPost

            loadData(redditPost)
        }
    }

    private fun loadData(redditPost: RedditPost) {
        redditPost.data?.let {
            author.text = it.author
            title.text = it.title

            // I would definitely do this different with more time, it makes no sense
            // to call again the URL having the image in memory.
            // Some thumbnails comes with 'self' value which I don't really know what it does
            if (it.thumbnail.isNotEmpty() && it.thumbnail != "self") {
                Glide.with(this)
                    .load(it.thumbnail)
                    .into(thumbnail)

                thumbnail.setOnClickListener { showSaveDialog() }
            }
        }
    }

    // FIX move this to an isolated component
    private fun showSaveDialog() {
        AlertDialog.Builder(requireActivity())
            .setTitle(getString(R.string.save_thumbnail_dialog_title))
            .setMessage(getString(R.string.save_thumbnail_dialog_message))
            .setPositiveButton(getString(R.string.yes)) { _, _ -> saveThumbnailToDevice() }
            .setNegativeButton(getString(R.string.no)) { dialog, _ -> dialog.dismiss() }
            .create()
            .show()
    }

    // FIX move this to an isolated component
    private fun saveThumbnailToDevice() {
        if (!checkPermission()) {
            val thumbnailBitmap = thumbnail.drawable.toBitmap()
            val externalStorageDirectory = Environment.getExternalStorageDirectory().toString()
            val file = File(externalStorageDirectory, THUMBNAIL_FILE_NAME)
            val outStream = FileOutputStream(file)

            thumbnailBitmap.compress(Bitmap.CompressFormat.PNG, 100, outStream)
            outStream.flush()
            outStream.close()

            Snackbar.make(
                view!!,
                String.format(
                    getString(R.string.save_thumbnail_dialog_success),
                    THUMBNAIL_FILE_NAME
                ),
                Snackbar.LENGTH_LONG
            ).show()
        } else {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(WRITE_EXTERNAL_STORAGE),
                PERMISSION_REQUEST_THUMBNAIL
            )
        }
    }

    // FIX move this to an isolated component
    private fun checkPermission(): Boolean =
        ContextCompat.checkSelfPermission(requireActivity(), WRITE_EXTERNAL_STORAGE) !=
                PackageManager.PERMISSION_GRANTED

    // FIX move this to an isolated component
    // Runtime permissions
    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            PERMISSION_REQUEST_THUMBNAIL -> {
                // If request is cancelled, the result arrays are empty.
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    saveThumbnailToDevice()
                }

                return
            }
        }
    }

    companion object {
        const val redditPostParam = "Param:RedditPost"

        fun newInstance(redditPost: RedditPost) = DetailFragment().apply {
            arguments = Bundle().apply {
                putSerializable(redditPostParam, redditPost)
            }
        }
    }
}
