package view

import service.CardImageLoader
import tools.aqua.bgw.visual.CompoundVisual
import tools.aqua.bgw.visual.ImageVisual

/**
 * This class is responsible for managing effects on a card image based on mouse events. It
 * simply manipulates the visual. How it functions: all methods here take a given visual of
 * a UI component in the context of [CompoundVisual], splits it in layers, adds or removes the
 * corresponding layer and then returns the new [CompoundVisual].
 * Please note: any card will have the front image on layer 0, and back image on layer 1.
 */
class CardViewEffect() {
    /**
     * Just for loading resources
     */
    private val loader: CardImageLoader = CardImageLoader()
    private val selectionVisual: ImageVisual = ImageVisual(loader.getImageByCoordinates(3, 4))
    private val hoverVisual: ImageVisual = ImageVisual(loader.getImageByCoordinates(2, 4))

    /**
     * Visualises selection effect in the UI.
     * @param visual is the forwarded [CompoundVisual] at its current state.
     * @return the modified [visual] as a [CompoundVisual].
     */
    fun select(visual: CompoundVisual): CompoundVisual {
        val result = CompoundVisual()
        val layers = visual.children.toMutableList()
        layers.add(selectionVisual)
        result.children = layers.toList()
        return result
    }

    /**
     * Removes selection effect in the UI.
     * @param visual is the forwarded [CompoundVisual] at its current state.
     * @return the modified [visual] as a [CompoundVisual].
     */
    fun unselect(visual: CompoundVisual): CompoundVisual {
        val result = CompoundVisual()
        val layers = visual.children.toMutableList()
        layers.remove(selectionVisual)
        result.children = layers.toList()
        return result
    }

    /**
     * Visualises hover effect in the UI.
     * @param visual is the forwarded [CompoundVisual] at its current state.
     * @return the modified [visual] as a [CompoundVisual].
     */
    fun hover(visual: CompoundVisual): CompoundVisual {
        val result = CompoundVisual()
        val layers = visual.children.toMutableList()
        layers.add(hoverVisual)
        result.children = layers.toList()
        return result
    }

    /**
     * Removes hover effect in the UI.
     * @param visual is the forwarded [CompoundVisual] at its current state.
     * @return the modified [visual] as a [CompoundVisual].
     */
    fun unhover(visual: CompoundVisual): CompoundVisual {
        val result = CompoundVisual()
        val layers = visual.children.toMutableList()
        layers.remove(hoverVisual)
        result.children = layers.toList()
        return result
    }

    /**
     * Shows front face of a card.
     * @param visual is the forwarded [CompoundVisual] at its current state.
     * @return the modified [visual] as a [CompoundVisual].
     */
    fun show(visual: CompoundVisual): CompoundVisual {
        val result = CompoundVisual()
        val layers = visual.children.toMutableList()
        layers[1]?.let {
            layers.removeAt(1)
        }
        result.children = layers.toList()
        return result
    }

    /**
     * Shows back face of a card.
     * @param visual is the forwarded [CompoundVisual] at its current state.
     * @return the modified [visual] as a [CompoundVisual].
     */
    fun hide(visual: CompoundVisual): CompoundVisual {
        val result = CompoundVisual()
        val layers = visual.children.toMutableList()
        layers.add(1, ImageVisual(loader.backImage))
        result.children = layers.toList()
        return result
    }
}