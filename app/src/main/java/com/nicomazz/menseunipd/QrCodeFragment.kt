package com.nicomazz.menseunipd


import android.annotation.SuppressLint
import android.app.AlertDialog
import android.graphics.Color
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.crashlytics.android.Crashlytics
import com.crashlytics.android.answers.Answers
import com.crashlytics.android.answers.CustomEvent
import com.nicomazz.menseunipd.data.QrCodeManager
import kotlinx.android.synthetic.main.fragment_qr_code.*
import net.glxn.qrgen.android.QRCode
import org.jetbrains.anko.backgroundColor


/**
 * A simple [Fragment] subclass.
 * Use the [QrCodeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class QrCodeFragment : Fragment() {


    private lateinit var rootView: View


    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        rootView = inflater!!.inflate(R.layout.fragment_qr_code, container, false)

        return rootView

    }

    override fun onStart() {
        super.onStart()
        activity?.title = "QR Code"

        initQrCodeSettings()
        initResetQrCode()
        handleState()
    }


    private fun handleState() {
        qr_code_setting.visibility = View.GONE
        remove_qr_code.visibility = View.GONE
        qr_code_image.visibility = View.GONE

        if (hasQrCode()) {
            showQrCode(QrCodeManager.getQrCodeString(context));
        } else
            qr_code_setting.visibility = View.VISIBLE;
    }

    private fun showQrCode(qrCodeString: String?) {
        remove_qr_code.visibility = View.VISIBLE
        qr_code_image.visibility = View.VISIBLE

        val myBitmap = QRCode.from(qrCodeString).withSize(700, 700).bitmap()
        qr_code_image.setImageBitmap(myBitmap)
    }


    private fun hasQrCode() = QrCodeManager.hasQrCodeString(context);

    private fun setQrCode(s: String) {
        Answers.getInstance().logCustom(CustomEvent("QRCode setted"))
        QrCodeManager.setQrCodeString(context, s)
    }

    private fun initQrCodeSettings() {
        save_code.setOnClickListener {
            setQrCode(qr_code_code.text.toString());
            qr_code_code.clearFocus()
            handleState()
        }
        qr_code_code.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            @SuppressLint("SetTextI18n")
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                handleTextChanged(s?.toString())

            }
        })
    }

    private fun handleTextChanged(s: String?) {
        if (s == null) {
            return
        }

        if (s.contains("http://")) {
            val newStr = s.toString().replace("http://", "")
            qr_code_code.setText(newStr)
        }

        when {
            s.length > 60 -> error_message.text = "${getString(R.string.too_long)} ${s.length}/51"
            s.length < 51 -> error_message.text = "${getString(R.string.too_short)} ${s.length}/51"
            else -> error_message.text = ""

        }
    }

    private fun initResetQrCode() {
        remove_qr_code.setOnClickListener {
            val dialog = AlertDialog.Builder(context)
                .setTitle(getString(R.string.about))
                .setMessage(getString(R.string.modify_qr_code))
                .setPositiveButton(getString(R.string.Yes)) { _, _ ->
                    setQrCode("")
                    handleState()
                }
                .setNegativeButton(getString(R.string.No)) { _, _ -> }
                .create()
            dialog.show()
        }
    }




}
