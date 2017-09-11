package hu.hanprog.game

import com.almasb.fxgl.app.GameApplication
import com.almasb.fxgl.core.math.FXGLMath
import com.almasb.fxgl.ecs.Entity
import com.almasb.fxgl.input.UserAction
import com.almasb.fxgl.physics.CollisionHandler
import com.almasb.fxgl.settings.GameSettings
import javafx.application.Application
import javafx.scene.input.MouseButton
import javafx.util.Duration

/**
 *
 * Created by hunyadym on 2017. 09. 11..
 */
class App : GameApplication() {

    override fun initSettings(settings: GameSettings?) {
        settings?.let {
            settings.width = 800
            settings.height = 600
            settings.title = "Game App"
            settings.version = "0.0.1"
            settings.isIntroEnabled = false
            settings.isMenuEnabled = false
            settings.isCloseConfirmation = false
            settings.isProfilingEnabled = false
        }
    }

    override fun initGameVars(vars: MutableMap<String, Any>?) {
        vars?.put("enemies", 0)
    }

    override fun initGame() {
        masterTimer.runAtInterval(Runnable {
            val numEnemies = gameState.getInt("enemies")
            if (numEnemies < 5) {
                gameWorld.spawn("Enemy", FXGLMath.random(width - 40).toDouble(), FXGLMath.random(height - 50).toDouble())
            }
            gameState.increment("enemies", 1)
        }, Duration.seconds(1.0))

    }

    override fun initInput() {
        input.addAction(
                object : UserAction("shoot") {
                    override fun onActionBegin() {
                        this@App.gameWorld.spawn("Bullet", input.getMousePositionWorld().x, input.getMousePositionWorld().y)
                    }
                },
                MouseButton.PRIMARY
        )
    }

    override fun initPhysics() {
        physicsWorld.addCollisionHandler(object : CollisionHandler(EntityType.BULLET, EntityType.ENEMY) {
            override fun onCollisionBegin(a: Entity, b: Entity) {
                b.removeFromWorld()
                a.removeFromWorld()
                gameState.increment("enemies", -1)
            }
        })
    }

    companion object {
        @JvmStatic
        fun main(args: Array<String>?) {
            Application.launch(App::class.java, "")
        }
    }


}
