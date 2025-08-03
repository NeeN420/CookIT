package cz.mendelu.pef.cookit.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.github.appintro.AppIntro
import com.github.appintro.AppIntroFragment
import cz.mendelu.pef.cookit.R
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AppIntroActivity : AppIntro(){

    companion object {
        fun createIntent(context: Context): Intent {
            return Intent(context, AppIntroActivity::class.java)
        }
    }

    private val viewModel: AppIntroViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        showAppIntro()
    }

    private fun showAppIntro() {
        addSlide(
            AppIntroFragment.newInstance(
                title = getString(R.string.welcome_to) + " " + getString(R.string.app_name),
                description = getString(R.string.intro_text_1),
                titleColor = ContextCompat.getColor(this, R.color.black),
                descriptionColor = ContextCompat.getColor(this, R.color.black),
                imageDrawable = R.drawable.undraw_post_eok2
            )
        )
        addSlide(
            AppIntroFragment.newInstance(
                title = getString(R.string.compose),
                description = getString(R.string.intro_text_2),
                titleColor = ContextCompat.getColor(this, R.color.black),
                descriptionColor = ContextCompat.getColor(this, R.color.black),
                imageDrawable = R.drawable.undraw_empty_cart_574u
            )
        )

        showSeparator(true)
        setSeparatorColor(ContextCompat.getColor(this, R.color.black))
        setColorDoneText(ContextCompat.getColor(this, R.color.black))
        setDoneText(getString(R.string.continue_to_app))
        setIndicatorColor(
            ContextCompat.getColor(this, R.color.black),
            ContextCompat.getColor(this, android.R.color.darker_gray))
        setNextArrowColor(ContextCompat.getColor(this, R.color.black))
    }

    override fun onSkipPressed(currentFragment: Fragment?) {
        super.onSkipPressed(currentFragment)
        continueToMainActivity()
    }

    override fun onDonePressed(currentFragment: Fragment?) {
        super.onDonePressed(currentFragment)
        continueToMainActivity()
    }

    private fun continueToMainActivity() {
        lifecycleScope.launch {
            viewModel.setFirstRun()
        }.invokeOnCompletion {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }
}