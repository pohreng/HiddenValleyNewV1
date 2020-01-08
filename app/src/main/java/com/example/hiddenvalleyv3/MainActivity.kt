package com.example.hiddenvalleyv3

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.res.Configuration
import android.content.res.Resources
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.AlteredCharSequence
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.after_login.*
import kotlinx.android.synthetic.main.cal_game.*
import kotlinx.android.synthetic.main.change_password.*
import kotlinx.android.synthetic.main.game.*
import kotlinx.android.synthetic.main.login.*
import kotlinx.android.synthetic.main.number_game.*
import kotlinx.android.synthetic.main.user_profile.*
import kotlinx.android.synthetic.main.user_registration.*
import kotlinx.android.synthetic.main.user_registration.pass
import kotlinx.android.synthetic.main.user_registration.username
import kotlinx.android.synthetic.main.user_setting.*
import kotlinx.android.synthetic.main.user_setting.btn_point1
import kotlinx.android.synthetic.main.user_setting.btn_setting1
import kotlinx.android.synthetic.main.user_setting.current_username2
import java.util.*


class  MainActivity : AppCompatActivity() {

    lateinit var handler: AccDatabase
    lateinit var diceImage1 : ImageView
    lateinit var diceImage2 : ImageView
    lateinit var starImage : ImageView

    //var manager = supportFragmentManager
    //val animationIn = AnimationUtils.loadAnimation(this,R.anim.zoom_in)
    //val animationOut = AnimationUtils.loadAnimation(this,R.anim.zoom_out)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        handler = AccDatabase(this)

        val actionBar = supportActionBar
        actionBar!!.title = resources.getString(R.string.app_name)

        showHome()


        val clickerLetter1 = findViewById<ImageView>(R.id.img1)
        val clickerNumber1 = findViewById<ImageView>(R.id.img2)
        val clickerMatch1 = findViewById<ImageView>(R.id.img3)
        val clickerCal1 = findViewById<ImageView>(R.id.img4)


        clickerLetter1.setOnClickListener{
            Toast.makeText(this, getString(R.string.log_in_first), Toast.LENGTH_SHORT).show()
        }
        clickerNumber1.setOnClickListener{
            Toast.makeText(this, getString(R.string.log_in_first), Toast.LENGTH_SHORT).show()
        }
        clickerMatch1.setOnClickListener{
            Toast.makeText(this, getString(R.string.log_in_first), Toast.LENGTH_SHORT).show()
        }
        clickerCal1.setOnClickListener{
            Toast.makeText(this, getString(R.string.log_in_first), Toast.LENGTH_SHORT).show()
        }


        registration_button.setOnClickListener{
            showUserReg()
        }

        login_button.setOnClickListener{
            showLogin()
        }

        sign_button.setOnClickListener {
            if(username.text.toString().isNotEmpty()){
                if (handler.verifyUsername(username.text.toString())) {
                    if (pass.text.toString().isNotEmpty() && pass.text.toString() == password.text.toString()) {
                        handler.insertUserData(username.text.toString(), pass.text.toString())
                        showHome()
                    } else {
                        Toast.makeText(this, getString(R.string.text_incorect_second_pass), Toast.LENGTH_SHORT).show()
                        showUserReg()
                    }
                } else
                    Toast.makeText(this, getString(R.string.username_been_used), Toast.LENGTH_SHORT).show()
            } else
                Toast.makeText(this, getString(R.string.username_cant_be_empty), Toast.LENGTH_SHORT).show()
        }

        back_button.setOnClickListener{
            showHome()
        }
        back_button1.setOnClickListener{
            showHome()
        }
        login.setOnClickListener{
            login()
        }
    }

    private fun login(){
        if(handler.userPresent(login_username.text.toString(),login_pass.text.toString())) {
            Toast.makeText(this, getString(R.string.login_success), Toast.LENGTH_SHORT).show()

            var data = handler.retrieveData(login_username.text.toString())

            afterLoginPages()

            afterUsername.text = ""
            for(i in 0..(data.size-1)){
                afterUsername.append(data.get(i).username)
            }
            val clickerProfile = findViewById<ImageView>(R.id.current_profile)
            val clickerLetter = findViewById<ImageView>(R.id.letterIMG)
            val clickerNumber = findViewById<ImageView>(R.id.numberIMG)
            val clickerMatch = findViewById<ImageView>(R.id.matchIMG)
            val clickerCal = findViewById<ImageView>(R.id.calculateIMG)

            clickerProfile.setOnClickListener{
                profile()
            }
            clickerLetter.setOnClickListener{
                showLetGame()
            }
            clickerNumber.setOnClickListener{
                showNumGame()
                starImage = findViewById(R.id.star_img)
                starImage.setImageResource(R.drawable.star0)
                num_btn.setOnClickListener{
                    rollStar()
                }
            }
            clickerMatch.setOnClickListener{
                showMatchGame()
            }
            clickerCal.setOnClickListener{
                showCalGame()
                diceImage1 = findViewById(R.id.dice_img1)
                diceImage2 = findViewById(R.id.dice_img2)

                diceImage1.setImageResource(R.drawable.dice_1)
                diceImage2.setImageResource(R.drawable.dice_1)

                roll_button.setOnClickListener{
                    rollDice()
                }
            }
        }else {
            Toast.makeText(this, getString(R.string.username_pass_wrong), Toast.LENGTH_SHORT).show()
            showLogin()
        }
    }


    private fun rollDice(){

        val randomInt1 = Random().nextInt(6)+1
        val randomInt2 = Random().nextInt(6)+1

        val dice1 = when (randomInt1) {
            1 -> R.drawable.dice_1
            2 -> R.drawable.dice_2
            3 -> R.drawable.dice_3
            4 -> R.drawable.dice_4
            5 -> R.drawable.dice_5
            else -> R.drawable.dice_6
        }
        diceImage1.setImageResource(dice1)

        val dice2 = when (randomInt2) {
            1 -> R.drawable.dice_1
            2 -> R.drawable.dice_2
            3 -> R.drawable.dice_3
            4 -> R.drawable.dice_4
            5 -> R.drawable.dice_5
            else -> R.drawable.dice_6
        }
        diceImage2.setImageResource(dice2)
        if(cal_answer.text.toString().isNotEmpty()) {
            answer_calculation.setOnClickListener {
                countTotal(randomInt1, randomInt2)
            }
        }else{
            Toast.makeText(this, getString(R.string.please_enter_ans), Toast.LENGTH_SHORT).show()
        }
        backOrNext.setOnClickListener{
            afterLoginPages()
        }
    }
    private fun countTotal(num1 : Int,num2 : Int){
        val ans = cal_answer.text.toString().toInt()
        val streak = cal_game_streak.text.toString().toInt() + 1
        val total = num1 + num2
        if(ans == total) {
            Toast.makeText(this, getString(R.string.ans_correct), Toast.LENGTH_SHORT).show()
            val plusPoints = 10 + streak
            handler.increasePoint(login_username.text.toString(),plusPoints.toString())
            val newStreak = streak + 0
            cal_game_streak.text = newStreak.toString()
            rollDice()
        }
        else {
            val newStreak = 0
            cal_game_streak.text = newStreak.toString()
            Toast.makeText(this, getString(R.string.wrong_answer), Toast.LENGTH_SHORT).show()
        }
    }

    private fun rollStar(){
        val randomInt1 = Random().nextInt(8)+1

        val star = when (randomInt1) {
            1 -> R.drawable.star1
            2 -> R.drawable.star2
            3 -> R.drawable.star3
            4 -> R.drawable.star4
            5 -> R.drawable.star5
            6 -> R.drawable.star6
            7 -> R.drawable.star7
            else -> R.drawable.star8
        }
        starImage.setImageResource(star)

        if(num_answer.text.toString().isNotEmpty()) {
            ans_number.setOnClickListener {
                countTotalStar(randomInt1)
            }
        }else{
            Toast.makeText(this, getString(R.string.please_enter_ans), Toast.LENGTH_SHORT).show()
        }
        backOrNext_number.setOnClickListener{
            afterLoginPages()
        }
    }
    private fun countTotalStar(num1 : Int){
        val ans = num_answer.text.toString().toInt()
        val streak = num_game_streak.text.toString().toInt() + 1
        if(ans == num1) {
            Toast.makeText(this, getString(R.string.ans_correct), Toast.LENGTH_SHORT).show()
            val plusPoints = 10 + streak
            handler.increasePoint(login_username.text.toString(),plusPoints.toString())
            val newStreak = streak + 0
            num_game_streak.text = newStreak.toString()
            rollStar()
        }
        else {
            val newStreak = 0
            num_game_streak.text = newStreak.toString()
            Toast.makeText(this, getString(R.string.wrong_answer), Toast.LENGTH_SHORT).show()
        }
    }

    /*private fun loopLogin(){
    var data = handler.retrieveData(login_username.text.toString())

    setContentView(R.layout.after_login)

    afterUsername.text = ""
    for(i in 0..(data.size-1)){
        afterUsername.append(data.get(i).username)
    }

    val clickerLetter = findViewById<ImageView>(R.id.letterIMG)
    val clickerNumber = findViewById<ImageView>(R.id.numberIMG)
    val clickerMatch = findViewById<ImageView>(R.id.matchIMG)
    val clickerCal = findViewById<ImageView>(R.id.calculateIMG)

    clickerLetter.setOnClickListener{
        setContentView(R.layout.game)
        showLetterGame()
        back_home.setOnClickListener {
            login()
        }
    }
    clickerNumber.setOnClickListener{
        setContentView(R.layout.game)
        showNumberGame()
        back_home.setOnClickListener {
            login()
        }
    }
    clickerMatch.setOnClickListener{
        setContentView(R.layout.game)
        showMatchGame()
        back_home.setOnClickListener {
            login()
        }
    }
    clickerCal.setOnClickListener{
        setContentView(R.layout.game)
        showCalGame()
        back_home.setOnClickListener {
            login()
        }
    }
}*/
    /*private fun showCalGame(){
        val transaction = manager.beginTransaction()
        val fragment = Cal_game_fragment()
        transaction.replace(R.id.fragment_holder,fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }*/
    /*private fun showLetterGame(){
        val transaction = manager.beginTransaction()
        val fragment = Letter_game_fragment()
        transaction.replace(R.id.fragment_holder,fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }*/
    /*private fun showMatchGame(){
        val transaction = manager.beginTransaction()
        val fragment = Match_game_fragment()
        transaction.replace(R.id.fragment_holder,fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }*/
    /* private fun showNumberGame(){
        val transaction = manager.beginTransaction()
        val fragment = Number_game_fragment()
        transaction.replace(R.id.fragment_holder,fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }*/
    /*private fun changeImg(){

        val randomInt = Random().nextInt(8)+1

        val newProfilePic = when (randomInt){
            1 -> R.drawable.profilepic1
            2 -> R.drawable.profilepic2
            3 -> R.drawable.profilepic3
            4 -> R.drawable.profilepic4
            5 -> R.drawable.profilepic5
            6 -> R.drawable.profilepic6
            7 -> R.drawable.profilepic7
            else -> R.drawable.profilepic8
        }

        profilePic.setImageResource(newProfilePic)
    }*/

    private fun changePassword() {
        showChangePass()
        confirm.setOnClickListener {
            if (login_pass.text.toString() == old_pass.text.toString() ) {
                if (change_pass.text.toString() == change_password.text.toString()) {
                    handler.changePass(login_username.text.toString(), change_pass.text.toString())
                    Toast.makeText(this, getString(R.string.change_success), Toast.LENGTH_SHORT).show()
                    recreate()
                } else {
                    Toast.makeText(this, getString(R.string.second_pass_incorect), Toast.LENGTH_SHORT).show()
                }
            }else {
                Toast.makeText(this, getString(R.string.wrong_old_pass), Toast.LENGTH_SHORT).show()
            }
        }
        back.setOnClickListener{
            setting()
        }
        val clickerPic = findViewById<ImageView>(R.id.current_profile12)
        clickerPic.setOnClickListener{
            afterLoginPages()
        }

        btn_point.setOnClickListener{
            profile()
        }
        btn_setting.setOnClickListener{
            setting()
        }
    }
    private fun changeLang(){
        val listItems = arrayOf("华语","Malay","English")

                val mBuilder = AlertDialog.Builder(this@MainActivity)
                mBuilder.setTitle(getString(R.string.choose_lang))
                mBuilder.setSingleChoiceItems(listItems,-1){dialog, which ->
                    if(which == 0){
                        setLocate("zh")
                        Toast.makeText(this, getString(R.string.text_log_in_first), Toast.LENGTH_SHORT).show()
                        recreate()
                    }
                    else if(which == 1){
                        setLocate("ms")
                        Toast.makeText(this, getString(R.string.text_log_in_first), Toast.LENGTH_SHORT).show()
                        recreate()
                    }else if(which == 2){
                        setLocate("en")
                        Toast.makeText(this, getString(R.string.text_log_in_first), Toast.LENGTH_SHORT).show()
                        recreate()
                    }
            dialog.dismiss()
        }
        val mDialog = mBuilder.create()
        mDialog.show()
    }
    private fun setLocate(Lang : String){
        val locale = Locale(Lang)

        Locale.setDefault(locale)

        val config = Configuration()

        config.locale = locale
        baseContext.resources.updateConfiguration(config, baseContext.resources.displayMetrics)

        val editor = getSharedPreferences("Settings", Context.MODE_PRIVATE).edit()
        editor.putString("My_Lang", Lang)
        editor.apply()
    }
    /*private fun loadLocate(){
        val sharedPreferences = getSharedPreferences("Settings", Activity.MODE_PRIVATE)
        val language = sharedPreferences.getString("My_Lang", "")
        setLocate(language)
    }*/
    private fun profile(){
        var data1 = handler.retrieveData(login_username.text.toString())
        showProfile()
        current_username1.text = ""
        current_points.text = ""
        for(i in 0..(data1.size-1)){
            current_username1.append(data1.get(i).username)
            current_points.append(data1.get(i).points.toString())
        }
        //var data = handler.retrievePoints(login_username.text.toString())
        /*for(i in 0..(data.size-1)){
        }*/

        val clickerPic = findViewById<ImageView>(R.id.current_profile12)
        clickerPic.setOnClickListener{
            afterLoginPages()
        }

        btn_point.setOnClickListener{
            profile()
        }
        btn_setting.setOnClickListener{
            setting()
        }
    }
    private fun setting(){
        var data1 = handler.retrieveData(login_username.text.toString())
        showUserSetting()
        current_username2.text = ""
        for(i in 0..(data1.size-1)){
            current_username2.append(data1.get(i).username)
        }
        val clickerPic = findViewById<ImageView>(R.id.current_profile123)
        clickerPic.setOnClickListener{
            afterLoginPages()
        }
        val btnChg = findViewById<Button>(R.id.change_language)
        reset_points.setOnClickListener{
            confirmResetPoint()
        }
        chg_pass.setOnClickListener{
            changePassword()
        }
        btnChg.setOnClickListener{
            changeLang()
        }
        sign_out.setOnClickListener{
            showHome()
        }
        btn_point1.setOnClickListener{
            profile()
        }
        btn_setting1.setOnClickListener{
            setting()
        }
    }

    private fun confirmResetPoint(){
        val listItems = arrayOf(getString(R.string.confirmation),getString(R.string.cancelation))

        val mBuilder = AlertDialog.Builder(this@MainActivity)
        mBuilder.setTitle(getString(R.string.confirmation_reset))
        mBuilder.setSingleChoiceItems(listItems,-1){dialog, which ->
            if(which == 0){
                handler.resetPoint(login_username.text.toString())
                Toast.makeText(this, getString(R.string.point_reset), Toast.LENGTH_SHORT).show()
                setting()
            }
            else if(which == 1){
                setting()
            }
            dialog.dismiss()
        }
        val mDialog = mBuilder.create()
        mDialog.show()
    }

    private fun showUserReg(){
        let_game.visibility=View.GONE
        match_game.visibility=View.GONE
        num_game.visibility=View.GONE
        cal_game.visibility=View.GONE
        registration_layout.visibility=View.VISIBLE
        login_layout.visibility=View.GONE
        main123.visibility=View.GONE
        afterLogin_layout.visibility=View.GONE
        user_profile.visibility=View.GONE
        user_setting.visibility=View.GONE
        change_password_page.visibility=View.GONE
    }
    private fun showLogin(){
        let_game.visibility=View.GONE
        match_game.visibility=View.GONE
        num_game.visibility=View.GONE
        cal_game.visibility=View.GONE
        registration_layout.visibility=View.GONE
        login_layout.visibility=View.VISIBLE
        main123.visibility=View.GONE
        afterLogin_layout.visibility=View.GONE
        user_profile.visibility=View.GONE
        user_setting.visibility=View.GONE
        change_password_page.visibility=View.GONE
    }
    private fun showHome(){
        let_game.visibility=View.GONE
        match_game.visibility=View.GONE
        num_game.visibility=View.GONE
        cal_game.visibility=View.GONE
        registration_layout.visibility=View.GONE
        login_layout.visibility=View.GONE
        main123.visibility=View.VISIBLE
        afterLogin_layout.visibility=View.GONE
        user_profile.visibility=View.GONE
        user_setting.visibility=View.GONE
        change_password_page.visibility=View.GONE
    }
    private fun afterLoginPages(){
        let_game.visibility=View.GONE
        match_game.visibility=View.GONE
        num_game.visibility=View.GONE
        cal_game.visibility=View.GONE
        registration_layout.visibility=View.GONE
        login_layout.visibility=View.GONE
        main123.visibility=View.GONE
        afterLogin_layout.visibility=View.VISIBLE
        user_profile.visibility=View.GONE
        user_setting.visibility=View.GONE
        change_password_page.visibility=View.GONE
    }
    private fun showProfile(){
        let_game.visibility=View.GONE
        match_game.visibility=View.GONE
        num_game.visibility=View.GONE
        cal_game.visibility=View.GONE
        registration_layout.visibility=View.GONE
        login_layout.visibility=View.GONE
        main123.visibility=View.GONE
        afterLogin_layout.visibility=View.GONE
        user_profile.visibility=View.VISIBLE
        user_setting.visibility=View.GONE
        change_password_page.visibility=View.GONE
    }
    private fun showUserSetting(){
        let_game.visibility=View.GONE
        match_game.visibility=View.GONE
        num_game.visibility=View.GONE
        cal_game.visibility=View.GONE
        registration_layout.visibility=View.GONE
        login_layout.visibility=View.GONE
        main123.visibility=View.GONE
        afterLogin_layout.visibility=View.GONE
        user_profile.visibility=View.GONE
        user_setting.visibility=View.VISIBLE
        change_password_page.visibility=View.GONE
    }

    private fun showNumGame(){
        let_game.visibility=View.GONE
        match_game.visibility=View.GONE
        num_game.visibility=View.VISIBLE
        cal_game.visibility=View.GONE
        registration_layout.visibility=View.GONE
        login_layout.visibility=View.GONE
        main123.visibility=View.GONE
        afterLogin_layout.visibility=View.GONE
        user_profile.visibility=View.GONE
        user_setting.visibility=View.GONE
        change_password_page.visibility=View.GONE
    }
    private fun showLetGame(){
        let_game.visibility=View.VISIBLE
        match_game.visibility=View.GONE
        num_game.visibility=View.GONE
        cal_game.visibility=View.GONE
        registration_layout.visibility=View.GONE
        login_layout.visibility=View.GONE
        main123.visibility=View.GONE
        afterLogin_layout.visibility=View.GONE
        user_profile.visibility=View.GONE
        user_setting.visibility=View.GONE
        change_password_page.visibility=View.GONE
    }

    private fun showMatchGame(){
        let_game.visibility=View.GONE
        match_game.visibility=View.VISIBLE
        num_game.visibility=View.GONE
        cal_game.visibility=View.GONE
        match_game.visibility=View.GONE
        registration_layout.visibility=View.GONE
        login_layout.visibility=View.GONE
        main123.visibility=View.GONE
        afterLogin_layout.visibility=View.GONE
        user_profile.visibility=View.GONE
        user_setting.visibility=View.GONE
        change_password_page.visibility=View.GONE

    }
    private fun showCalGame(){
        let_game.visibility=View.GONE
        match_game.visibility=View.GONE
        num_game.visibility=View.GONE
        cal_game.visibility=View.VISIBLE
        registration_layout.visibility=View.GONE
        login_layout.visibility=View.GONE
        main123.visibility=View.GONE
        afterLogin_layout.visibility=View.GONE
        user_profile.visibility=View.GONE
        user_setting.visibility=View.GONE
        change_password_page.visibility=View.GONE
    }
    private fun showChangePass(){
        let_game.visibility=View.GONE
        match_game.visibility=View.GONE
        num_game.visibility=View.GONE
        cal_game.visibility=View.GONE
        registration_layout.visibility=View.GONE
        login_layout.visibility=View.GONE
        main123.visibility=View.GONE
        afterLogin_layout.visibility=View.GONE
        user_profile.visibility=View.GONE
        user_setting.visibility=View.GONE
        change_password_page.visibility=View.VISIBLE
    }
}
