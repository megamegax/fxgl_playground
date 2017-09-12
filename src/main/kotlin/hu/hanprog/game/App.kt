package hu.hanprog.game

import com.almasb.fxgl.annotation.OnUserAction
import com.almasb.fxgl.app.GameApplication
import com.almasb.fxgl.core.math.FXGLMath
import com.almasb.fxgl.ecs.Entity
import com.almasb.fxgl.entity.GameEntity
import com.almasb.fxgl.entity.control.ProjectileControl
import com.almasb.fxgl.input.ActionType
import com.almasb.fxgl.input.InputMapping
import com.almasb.fxgl.input.UserAction
import com.almasb.fxgl.physics.CollisionHandler
import com.almasb.fxgl.physics.PhysicsComponent
import com.almasb.fxgl.settings.GameSettings
import javafx.application.Application
import javafx.geometry.Point2D
import javafx.scene.input.KeyCode
import javafx.scene.text.Text
import javafx.util.Duration

/**
 *
 * Created by hunyadym on 2017. 09. 11..
 */
class App : GameApplication() {
    lateinit var player: GameEntity
    //lateinit var playerControl: PlayerControl
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
        vars?.run {
            put("enemies", 0)
            put("score", 0)
        }
    }

    override fun initGame() {
        gameWorld.spawn("Floor", 0.0, height - 40.0)
        player = gameWorld.spawn("Player", 10.0, height - 100.0) as GameEntity
        // player.addControl(playerControl)
        // for (i in 1..100) {

        // }
        masterTimer.runAtInterval(Runnable {
            val numEnemies = gameState.getInt("enemies")
            if (numEnemies < 5) {
                gameWorld.spawn("Enemy", FXGLMath.random(width - 40).toDouble(), (height - 100).toDouble())
            }
            gameState.increment("enemies", 1)
        }, Duration.seconds(1.0))

    }

    override fun initUI() {
        val textScore = Text()
        textScore.translateX = 30.0
        textScore.translateY = 30.0
        gameScene.addUINode(textScore)
        textScore.textProperty().bind(gameState.intProperty("score").asString())


    }

    override fun initInput() {
        input.addAction(
                object : UserAction("shoot") {
                    override fun onActionBegin() {
                        val vectorToMouse = input.getVectorToMouse(Point2D(player.x, player.y))
                        this@App.gameWorld.spawn("Bullet", player.x, player.y).addControl(ProjectileControl(vectorToMouse, 400.0))
                    }
                },
                KeyCode.SPACE
        )
        input.addInputMapping(InputMapping("jump", KeyCode.W))
        input.addInputMapping(InputMapping("left", KeyCode.A))
        input.addInputMapping(InputMapping("right", KeyCode.D))

    }

    @OnUserAction(name = "left", type = ActionType.ON_ACTION)
    fun onLeft() {
        if (player.x > 15) {
            // player.translateX(-5.0)
            player.getComponent(PhysicsComponent::class.java).linearVelocity = Point2D(-100.0, 0.0)
        }
    }

    @OnUserAction(name = "right", type = ActionType.ON_ACTION)
    fun onRight() {
        if (player.x < width - 15) {
            player.getComponent(PhysicsComponent::class.java).linearVelocity = Point2D(100.0, 0.0)
            //player.translate(5.0, 0.0)
        }
    }

    @OnUserAction(name = "jump", type = ActionType.ON_ACTION_BEGIN)
    fun onJump() {
        //player.translate(0.0, -5.0)
        player.getComponent(PhysicsComponent::class.java).linearVelocity = Point2D(0.0, -300.0)

    }

    override fun initPhysics() {
        physicsWorld.addCollisionHandler(object : CollisionHandler(EntityType.BULLET, EntityType.ENEMY) {
            override fun onCollisionBegin(a: Entity, b: Entity) {
                b.removeFromWorld()
                a.removeFromWorld()
                gameState.increment("enemies", -1)
                gameState.increment("score", 1)
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