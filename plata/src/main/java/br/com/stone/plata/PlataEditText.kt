package br.com.stone.plata

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.widget.EditText
import br.com.stone.plata.support.KTextWatcher
import br.com.stone.plata.support.parseCentsToCurrency
import br.com.stone.plata.support.textWatcher
import java.util.*

/**
 * @author filpgame
 * @since 23/06/2017
 */
class PlataEditText : EditText {
    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        setAttributes(context, attrs)
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        setAttributes(context, attrs)
    }

    private var locale = Locale("pt", "BR")

    private var plataChangeListener: OnPlataChangeListener? = null

    private val plataTextWatcher: KTextWatcher.() -> Unit = {
        var beforeValue = 0L

        beforeTextChanged { charSequence, _, _, _ ->
            beforeValue = safeRemoveSymbols(charSequence)
        }

        onTextChanged { charSequence, _, _, _ ->
            removeTextChangedListener(this)

            val amount = safeRemoveSymbols(charSequence)
            val overTheMaximum: Boolean
            if (maxAmount != 0L && amount > maxAmount) {
                setText(beforeValue.parseCentsToCurrency(locale))
                overTheMaximum = true
            } else {
                setText(amount.parseCentsToCurrency(locale))
                overTheMaximum = false
            }
            setSelection(text.length)
            addTextChangedListener(this)

            plataChangeListener?.onPlataChanged?.invoke(beforeValue, this@PlataEditText.amount, overTheMaximum)
        }
    }

    var maxAmount = 0L

    var amount: Long = 0
        get() = safeRemoveSymbols(text)
        set(value) {
            field = value
            setText(value.toString())
        }

    init {
        textWatcher(plataTextWatcher)
    }

    private fun setAttributes(context: Context, attrs: AttributeSet?) {
        val args = context.theme.obtainStyledAttributes(attrs, R.styleable.PlataEditText, 0, 0)
        maxAmount = args.getInt(R.styleable.PlataEditText_maxAmount, 0).toLong()
    }

    private fun safeRemoveSymbols(amount: CharSequence?): Long {
        try {
            return amount.toString().replace("[^\\d]".toRegex(), "").toLong()
        } catch (e: NumberFormatException) {
            Log.e("Plata", "Error removing symbols from string: $amount", e)
            return 0L
        }
    }

    fun plataChangeListener(listener: (OnPlataChangeListener.() -> Unit)) {
        plataChangeListener = OnPlataChangeListener().apply(listener)
    }

    inner class OnPlataChangeListener {
        var onPlataChanged: ((beforeAmount: Long, currentAmount: Long, overTheMax: Boolean) -> Unit)? = null

        fun onPlataChanged(event: (beforeAmount: Long, currentAmount: Long, overTheMax: Boolean) -> Unit) {
            onPlataChanged = event
        }
    }
}