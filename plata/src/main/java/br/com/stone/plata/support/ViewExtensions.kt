package br.com.stone.plata.support

import android.text.Editable
import android.text.TextWatcher
import android.widget.TextView

/**
 * @author filpgame
 * @since 23/06/2017
 */
fun TextView.textWatcher(init: KTextWatcher.() -> Unit) = addTextChangedListener(KTextWatcher().apply(init))

class KTextWatcher : TextWatcher {

    private var beforeTextChanged: ((CharSequence?, Int, Int, Int) -> Unit)? = null
    private var onTextChanged: ((CharSequence?, Int, Int, Int) -> Unit)? = null
    private var afterTextChanged: ((Editable?) -> Unit)? = null

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        beforeTextChanged?.invoke(s, start, count, after)
    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        onTextChanged?.invoke(s, start, before, count)
    }

    override fun afterTextChanged(s: Editable?) {
        afterTextChanged?.invoke(s)
    }

    fun beforeTextChanged(listener: (CharSequence?, Int, Int, Int) -> Unit) {
        beforeTextChanged = listener
    }

    fun onTextChanged(listener: (CharSequence?, Int, Int, Int) -> Unit) {
        onTextChanged = listener
    }

    fun afterTextChanged(listener: (Editable?) -> Unit) {
        afterTextChanged = listener
    }
}