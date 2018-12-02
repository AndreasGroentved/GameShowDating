package dating.innovative.gameshowdating.activity

import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.support.v7.widget.Toolbar
import android.view.Menu
import dating.innovative.gameshowdating.R
import dating.innovative.gameshowdating.data.WebSocketHandler
import dating.innovative.gameshowdating.model.RemoteUser
import dating.innovative.gameshowdating.util.BaseActivity
import kotlinx.android.synthetic.main.content_should_date.*

class ShouldDateActivity : BaseActivity() {


    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val userName = intent.extras.getString("username")

        WebSocketHandler.instance.getUser(userName) {
            if (it != null) setUser(it)
            //TODO hvis en bruger ikke kan findes
        }
        setChatButton(userName)
    }

    private fun setChatButton(userName: String) {
        start_chat_button.setOnClickListener {
            Intent(this, ChatActivity::class.java).apply {
                putExtra("username", userName)
                startActivity(this)
            }
        }
    }

    private fun setUser(user: RemoteUser) {
        if (user.profilePicture?.isEmpty() == false) {
            val selectedImage = BitmapFactory.decodeByteArray(user.profilePicture, 0, user.profilePicture.size);
            profileImage.setImageBitmap(selectedImage)
        }
        profileName.text = "${user._id}, ${user.sex}, ${user.age}"
        profileBiography.text = user.biography
    }

    override fun getLayout(): Int {
        return R.layout.activity_should_date
    }

    override fun getToolBar(): Toolbar {
        return findViewById(R.id.toolbar_should_date)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_profile, menu)
        return true
    }

}
