package dating.innovative.gameshowdating

import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import dating.innovative.gameshowdating.data.CallObject
import dating.innovative.gameshowdating.data.WebSocketHandler
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*


class MainActivity : BaseActivity() {
    override fun getLayout(): Int = R.layout.activity_main
    override fun getToolBar(): Toolbar = toolbar

    override fun setUpToolBar() = setSupportActionBar(toolbar)

    private var ws: WebSocketHandler? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        switchScreen.setOnClickListener {
            println("switch")
            val intent = Intent(applicationContext, LoginActivity::class.java)
            startActivity(intent)
        }
        WebSocketHandler().apply { run() }
    }

    private fun testWs() {
        ws?.send<String>(CallObject("test", "test")) {
            println(it.response)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }
}
