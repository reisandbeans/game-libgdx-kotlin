package com.reis.game.entity.components

import com.reis.game.entity.GameEntity
import com.reis.game.entity.ai.controllers.EntityController
import com.reis.game.entity.ai.actions.EntityAction
import com.reis.game.entity.ai.actions.ActionQueue
import com.reis.game.entity.ai.actions.Idle

/**
 * Created by bernardoreis on 12/25/17.
 */
class EntityControllerComponent constructor(entity: GameEntity,
    private val entityController: EntityController) : EntityComponent(entity) {

    private val actionProcessor: ActionQueue = ActionQueue()
    private var currentAction: EntityAction = actionProcessor.idle

    init {
        currentAction.start(entity)
    }

    override fun update(delta: Float) {
        entityController.update(delta)
        val nextAction = actionProcessor.getNextAction()
        if (shouldReplaceCurrentAction(nextAction)) {
            replaceCurrentAction(nextAction)
        }
        // Action update already happens as part of the action update
        // currentAction.update(delta, entity)
    }

    private fun shouldReplaceCurrentAction(nextAction: EntityAction): Boolean {
        if (currentAction === nextAction) {
            return false
        }

        val hasPriorityOverCurrentAction = nextAction.getPriority() > currentAction.getPriority()
        val isSelfReplaceable = currentAction::class == nextAction::class &&
                currentAction.isSelfReplaceable()

        return currentAction.isFinished() ||
                hasPriorityOverCurrentAction ||
                isSelfReplaceable
    }

    private fun replaceCurrentAction(nextAction: EntityAction) {
        if (!currentAction.isFinished()) {
            currentAction.stop(entity)
        }
        currentAction = nextAction
        currentAction.start(entity)
    }

    fun addAction(action: EntityAction) {
        actionProcessor.addAction(action)
    }

    fun getActionProcessor(): ActionQueue {
        return actionProcessor
    }

    fun getCurrentAction(): EntityAction {
        return currentAction
    }
}