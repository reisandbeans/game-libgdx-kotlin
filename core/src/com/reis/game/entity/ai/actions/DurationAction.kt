package com.reis.game.entity.ai.actions

import com.reis.game.entity.GameEntity

/**
 * Created by bernardoreis on 12/31/17.
 */
abstract class DurationAction(priority: Int, protected val duration: Float): Action(priority) {

    private var elapsedTime: Float = 0f

    override fun onUpdate(delta: Float, entity: GameEntity) {
        if (this.duration < 0) {
            return
        }
        this.elapsedTime += delta
        if (this.elapsedTime > this.duration) {
            this.finish()
        }
    }

    fun reset(): DurationAction {
        this.elapsedTime = 0f
        return this
    }
}