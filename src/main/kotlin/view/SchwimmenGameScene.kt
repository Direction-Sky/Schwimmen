package view

import service.RootService
import tools.aqua.bgw.core.BoardGameScene
import tools.aqua.bgw.dialog.Dialog
import tools.aqua.bgw.dialog.DialogType
import tools.aqua.bgw.visual.ImageVisual

class SchwimmenGameScene(private val rootService: RootService): BoardGameScene(1920, 1080), Refreshable {
    init {
        background = ImageVisual("background.jpg", 1920, 1080)
    }
}