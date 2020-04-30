package com.noteworth.simpleredditclient.ui.activity

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.noteworth.simpleredditclient.R
import com.noteworth.simpleredditclient.common.IntentKeys
import com.noteworth.simpleredditclient.model.RedditPost
import com.noteworth.simpleredditclient.ui.fragment.DetailFragment

class DetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.detail_activity)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        if (savedInstanceState == null) {
            intent.extras?.let {
                val redditPost = it.getSerializable(IntentKeys.EXTRA_REDDIT_POST) as RedditPost

                supportFragmentManager.beginTransaction()
                    .replace(R.id.container, DetailFragment.newInstance(redditPost))
                    .commitNow()
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> onBackPressed()
        }

        return super.onOptionsItemSelected(item)
    }
}
