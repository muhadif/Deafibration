package id.infiniteuny.deafibration.utils.dialog

import id.infiniteuny.deafibration.R

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.View
import android.widget.ImageView
import android.widget.TextView

import androidx.appcompat.app.AlertDialog
import com.bumptech.glide.Glide

class WarningDialog(private val mContext: Context, style: Int) :
    AlertDialog.Builder(mContext, style) {
    private val mTitle: TextView? = null
    private val mIcon: ImageView? = null
    private var ivBootm : ImageView? = null
    private var message : TextView? = null

    init {
        val customMessage = View.inflate(mContext, R.layout.warning_dialog, null)
        ivBootm = customMessage.findViewById(R.id.iv_bottom_warning)
        message = customMessage.findViewById(R.id.tv_message)
        setView(customMessage)
    }

    override fun setTitle(textResId: Int): WarningDialog {
        mTitle!!.setText(textResId)
        return this
    }

    override fun setTitle(text: CharSequence?): WarningDialog {
        mTitle!!.text = text
        return this
    }

    override fun setMessage(textResId: Int): WarningDialog {
        return this
    }

    override fun setMessage(text: CharSequence?): WarningDialog {
        message!!.text = text
            return this
    }

    override fun setIcon(drawableResId: Int): WarningDialog {
        mIcon!!.setImageResource(drawableResId)
        return this
    }

    override fun setIcon(icon: Drawable?): WarningDialog {
        mIcon!!.setImageDrawable(icon)
        return this
    }

    fun setBottom(id : Int): WarningDialog {
        Glide.with(context)
            .load(id)
            .into(ivBootm!!)
        return this
    }

}
