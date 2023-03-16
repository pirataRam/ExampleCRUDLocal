package com.example.examplecrudlocal.ui.base

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.examplecrudlocal.R
import com.example.examplecrudlocal.databinding.DialogInfoBinding
import com.example.examplecrudlocal.tools.ARGS_EXTRAS

abstract class BaseActivity : AppCompatActivity() {

    private val dialogLoading by lazy {
        val dialogAlertDialog = AlertDialog.Builder(this@BaseActivity, R.style.CustomDialog)
        with(dialogAlertDialog){
            setView(R.layout.loading)
            setCancelable(false)
            create()
        }
    }

    fun showLoading(loading: Boolean){
        if (loading){
            if (!dialogLoading.isShowing)
                dialogLoading.show()
        } else {
            if (dialogLoading.isShowing)
                dialogLoading.dismiss()
        }
    }

    fun showErrorMessage(message: String){
        val dialog = AlertDialog.Builder(this@BaseActivity)
        val binding: DialogInfoBinding = DialogInfoBinding.inflate(LayoutInflater.from(this@BaseActivity), null, false)
        with(dialog){
            setView(binding.root)
            setCancelable(false)
            setPositiveButton(R.string.btn_close) { dialog2, _ ->
                dialog2.dismiss()
            }
        }
        with(binding){
            infoActvTitle.text = null
            infoActvMsg.text = message
        }
        dialog.create().show()
    }

    fun showQuestionMessage(title: String, message: String, onAccept: (accept: Boolean) -> Unit){
        val dialog = AlertDialog.Builder(this@BaseActivity)
        val binding: DialogInfoBinding = DialogInfoBinding.inflate(LayoutInflater.from(this@BaseActivity), null, false)
        with(dialog){
            setView(binding.root)
            setCancelable(false)
            setPositiveButton(R.string.btn_positive) { dialog1, _ ->
                dialog1.dismiss()
                onAccept(true)
            }
            setNegativeButton(R.string.btn_negative) { dialog2, _ ->
                dialog2.dismiss()
                onAccept(false)
            }
        }
        with(binding){
            infoActvTitle.text = title
            infoActvMsg.text = message
        }
        dialog.create().show()
    }

    fun <T> launchActivity(
        clazz: Class<T>
    ) {
        val intent = Intent(this, clazz).apply {
            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
        }
        startActivity(intent)
    }

    fun <T> launchActivity(
        clazz: Class<T>,
        bundle: Bundle
    ) {
        val intent = Intent(this, clazz).apply {
            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
        }

        intent.putExtra(ARGS_EXTRAS, bundle)
        startActivity(intent)
    }

    fun showToastMessage(message: String){
        Toast.makeText(this@BaseActivity, message, Toast.LENGTH_LONG).show()
    }

    abstract fun setListeners()

    abstract fun setObservers()

}