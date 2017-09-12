package hu.hanprog.game

import com.almasb.fxgl.annotation.SetEntityFactory
import com.almasb.fxgl.annotation.Spawns
import com.almasb.fxgl.entity.Entities
import com.almasb.fxgl.entity.EntityFactory
import com.almasb.fxgl.entity.SpawnData
import com.almasb.fxgl.entity.component.CollidableComponent
import com.almasb.fxgl.physics.BoundingShape
import com.almasb.fxgl.physics.HitBox
import com.almasb.fxgl.physics.PhysicsComponent
import com.almasb.fxgl.physics.box2d.dynamics.BodyType
import com.almasb.fxgl.physics.box2d.dynamics.FixtureDef
import javafx.geometry.Point2D
import javafx.scene.paint.Color
import javafx.scene.shape.Rectangle

/**
 *
 * Created by hunyadym on 2017. 09. 11..
 */
@SetEntityFactory
class GameEntityFactory : EntityFactory {

    @Spawns("Enemy")
    fun newEnemy(spawnData: SpawnData) = Entities.builder()
            .from(spawnData)
            .type(EntityType.ENEMY)
            .viewFromNodeWithBBox(Rectangle(40.0, 40.0, Color.RED))
            .with(CollidableComponent(true))
            .with(PhysicsComponent().apply {
                setBodyType(BodyType.DYNAMIC)
            })
            .build()

    @Spawns("Bullet")
    fun newBullet(spawnData: SpawnData) = Entities.builder()
            .from(spawnData)
            .type(EntityType.BULLET)
            .with(CollidableComponent(true))

            .viewFromNodeWithBBox(Rectangle(10.0, 2.0, Color.BLACK))
            .build()

    @Spawns("Player")
    fun newPlayer(spawnData: SpawnData) = Entities.builder()
            .from(spawnData)
            .type(EntityType.PLAYER)
            .viewFromNodeWithBBox(Rectangle(40.0, 40.0, Color.BLUE))
            .with(PhysicsComponent().apply {
                setBodyType(BodyType.DYNAMIC)
                val fixtureDef = FixtureDef()
                fixtureDef.density = 0.7f
                fixtureDef.restitution = 0.3f
                setFixtureDef(fixtureDef)
            })
            .with(CollidableComponent(true))
            .build()

    @Spawns("Floor")
    fun newFloor(spawnData: SpawnData) = Entities.builder()
            .from(spawnData)
            .type(BlockType.GROUND)
            .at(0.0, 500.0)
            .viewFromNode(Rectangle(400.0, 100.0, Color.GRAY))
            .bbox(HitBox("Main", BoundingShape.chain(
                    Point2D(0.0, 0.0),
                    Point2D(400.0, 0.0),
                    Point2D(400.0, 100.0),
                    Point2D(0.0, 100.0)
            )))
            .with(PhysicsComponent())
         //   .viewFromNodeWithBBox(Rectangle(40.0, 40.0, Color.DARKGREEN))
          //  .with(CollidableComponent(true))
            .build()
}