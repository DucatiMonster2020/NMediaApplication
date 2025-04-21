package ru.netology.nmedia.view

import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import ru.netology.nmedia.R
import ru.netology.nmedia.databinding.ActivityNewPostBinding

class NewPostActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityNewPostBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val editingText = intent?.getStringExtra(Intent.EXTRA_TEXT)
            if (editingText != null) {
                binding.content.setText(editingText)
                binding.content.setSelection(editingText.length)
            }

        binding.ok.setOnClickListener {
            val text = binding.content.text.toString()
            if (text.isBlank()) {
                setResult(RESULT_CANCELED)
            } else {
                setResult(RESULT_OK, Intent().apply { putExtra(Intent.EXTRA_TEXT, text) })
            }
            finish()
        }
    }
}
object NewPostContract : ActivityResultContract<String?, String?>() {
    override fun createIntent(context: Context, input: String?) = Intent(context, NewPostActivity::class.java).apply {
        putExtra(Intent.EXTRA_TEXT, input)
    }


    override fun parseResult(resultCode: Int, intent: Intent?) = if (resultCode == RESULT_OK) {
        intent?.getStringExtra(Intent.EXTRA_TEXT)
    } else {
        null
    }
}