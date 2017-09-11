package hu.hanprog.game

import com.almasb.fxgl.annotation.SetEntityFactory
import com.almasb.fxgl.annotation.Spawns
import com.almasb.fxgl.ecs.Entity
import com.almasb.fxgl.entity.Entities
import com.almasb.fxgl.entity.EntityFactory
import com.almasb.fxgl.entity.SpawnData
import com.almasb.fxgl.entity.component.CollidableComponent
import com.almasb.fxgl.entity.control.ProjectileControl
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
    fun newEnemy(spawnData: SpawnData): Entity {
        return Entities.builder()
                .from(spawnData)
                .type(EntityType.ENEMY)
                .viewFromNodeWithBBox(Rectangle(40.0, 40.0, Color.RED))
                .with(CollidableComponent(true))
                .build()
    }

    @Spawns("Bullet")
    fun newBullet(spawnData: SpawnData): Entity {
        return Entities.builder()
                .from(spawnData)
                .type(EntityType.BULLET)
                .with(CollidableComponent(true))
                .with(ProjectileControl(Point2D(1.0, 0.0), 400.0))
                .viewFromNodeWithBBox(Rectangle(10.0, 2.0, Color.BLACK))
                .build()
    }
}