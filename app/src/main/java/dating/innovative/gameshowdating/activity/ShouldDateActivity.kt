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

/*  Andreas Jeppesen - ajepp09@student.sdu.dk
    Emil Jensen - emije14@student.sdu.dk
    Nicolai Jensen - nije214@student.sdu.dk */

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
            val i = Intent(this, ChatActivity::class.java).putExtra("username", userName)
            startActivity(i)
        }
    }

    private fun setUser(user: RemoteUser) {
        runOnUiThread {
            if (user.profilePicture?.isEmpty() == false) {
                val selectedImage = BitmapFactory.decodeByteArray(user.profilePicture, 0, user.profilePicture.size);
                profileImage.setImageBitmap(selectedImage)
            }
            profileName.text = "${user._id}, ${user.sex}, ${user.age}"
            profileBiography.text = user.biography
        }
    }

    override fun getLayout() = R.layout.activity_should_date
    override fun getToolBar(): Toolbar = findViewById(R.id.toolbar_should_date)


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_profile, menu)
        return true
    }

}
