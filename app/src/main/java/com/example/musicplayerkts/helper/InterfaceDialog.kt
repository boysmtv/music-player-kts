package com.example.musicplayerkts.helper

import android.content.Context
import android.graphics.Color
import cn.pedant.SweetAlert.SweetAlertDialog

class InterfaceDialog (val context: Context) {

    var pDialog: SweetAlertDialog = SweetAlertDialog(context, SweetAlertDialog.PROGRESS_TYPE)

    fun showDialogLoading(title: String) : SweetAlertDialog {
        pDialog = SweetAlertDialog(context, SweetAlertDialog.PROGRESS_TYPE)
        pDialog.progressHelper.barColor = Color.parseColor("#A5DC86")
        pDialog.titleText = title
        pDialog.setCancelable(false)
        pDialog.setCanceledOnTouchOutside(false)
        pDialog.show()
        return pDialog
    }

    fun dismisDialogLoading() : SweetAlertDialog {
        pDialog.dismiss()
        return pDialog
    }

    fun showDialogConfirm(integer: Int, title: String, content: String, confirmText: String): SweetAlertDialog {
        pDialog = SweetAlertDialog(context, integer)
        pDialog.titleText = title
        pDialog.contentText = content
        pDialog.confirmText = confirmText
        pDialog.setCancelable(false)
        pDialog.setCanceledOnTouchOutside(false)
        return pDialog
    }

    fun showDialogConfirmWarning(title: String, content: String): SweetAlertDialog {
        pDialog = SweetAlertDialog(context, SweetAlertDialog.WARNING_TYPE)
        pDialog.titleText = title
        pDialog.contentText = content
        pDialog.confirmText = "OK"
        pDialog.setCancelable(false)
        pDialog.setCanceledOnTouchOutside(false)
        return pDialog
    }

    fun showDialogConfirmCancel(integer: Int, title: String, content: String, confirmText: String, cancelText: String): SweetAlertDialog {
        pDialog = SweetAlertDialog(context, integer)
        pDialog.titleText = title
        pDialog.contentText = content
        pDialog.confirmText = confirmText
        pDialog.cancelText = cancelText
        pDialog.setCancelable(false)
        pDialog.setCanceledOnTouchOutside(false)
        return pDialog
    }

    fun dialogWarning(integer: Int): SweetAlertDialog {
        pDialog = SweetAlertDialog(context, integer)
        pDialog.titleText = "Are you sure?"
        pDialog.contentText = "You won't be able to recover this file!"
        pDialog.confirmText = "Delete!"
        pDialog.cancelText = "Cancel!"
        pDialog.setCancelable(false)
        pDialog.setCanceledOnTouchOutside(false)
        pDialog.setConfirmClickListener {
                sDialog ->
            sDialog.dismiss()
        }
        pDialog.setCancelClickListener {
                sDialog -> sDialog.dismiss()
        }
        return pDialog
    }

    fun showDialogWarningConfirm(strTitle: String, strContent: String, strBtn: String) {
        pDialog = SweetAlertDialog(context, SweetAlertDialog.WARNING_TYPE)
        pDialog.titleText = strTitle
        pDialog.contentText = strContent
        pDialog.confirmText = strBtn
        pDialog.setCancelable(false)
        pDialog.setCanceledOnTouchOutside(false)
        pDialog.setConfirmClickListener {
                sDialog ->
            sDialog.dismiss()
        }
        pDialog.show()
    }

}