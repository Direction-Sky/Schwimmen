package service

import entity.CardSuit
import entity.CardValue
import entity.SchwimmenCard
import java.awt.image.BufferedImage
import javax.imageio.ImageIO

private const val CARDS_FILE = "/images/Deck32WithBack.png"
private const val IMG_HEIGHT = 375
private const val IMG_WIDTH = 300

/**
 * Provides access to the src/main/resources/card_deck.png file that contains all card images
 * in a raster. The returned [BufferedImage] objects of [frontImageFor], [blankImage],
 * and [backImage] are 130x200 pixels.
 */
class CardImageLoader {

    /**
     * The full raster image containing the suits as rows (plus one special row for blank/back)
     * and values as columns (starting with the ace). As the ordering does not correctly reflect
     * the order in which the suits are declared in [CardSuit], mappings via [row] and [column]
     * are required.
     */
    private val image : BufferedImage = ImageIO.read(CardImageLoader::class.java.getResource(CARDS_FILE))

    /**
     * Provides the card image for the given [CardSuit] and [CardValue]
     */
    fun frontImageFor(suit: CardSuit, value: CardValue) =
        getImageByCoordinates(value.column, suit.row)

    /**
     * Provides the card image for the given [SchwimmenCard].
     */
    fun frontImageForCard(card: SchwimmenCard) =
        getImageByCoordinates(card.value.column, card.suit.row)

    /**
     * Provides a blank (white) card
     */
    val blankImage : BufferedImage get() = getImageByCoordinates(1, 4)

    /**
     * Provides the back side image of the card deck
     */
    val backImage: BufferedImage get() = getImageByCoordinates(0, 4)

    /**
     * retrieves from the full raster image [image] the corresponding sub-image
     * for the given column [x] and row [y]
     *
     * @param x column in the raster image, starting at 0
     * @param y row in the raster image, starting at 0
     *
     */
    fun getImageByCoordinates (x: Int, y: Int) : BufferedImage =
        image.getSubimage(
            x * IMG_WIDTH,
            y * IMG_HEIGHT,
            IMG_WIDTH,
            IMG_HEIGHT
        )

}

/**
 * As the [CARDS_FILE] does not have the same ordering of suits
 * as they are in [CardSuit], this extension property provides
 * a corresponding mapping to be used when addressing the row.
 *
 */
private val CardSuit.row get() = when (this) {
    CardSuit.CLUBS -> 0
    CardSuit.DIAMONDS -> 1
    CardSuit.HEARTS -> 2
    CardSuit.SPADES -> 3
}


 /**
 * As the [CARDS_FILE] does not have the same ordering of values
 * as they are in [CardValue], this extension property provides
 * a corresponding mapping to be used when addressing the column.
 */
private val CardValue.column get() = when (this) {
    CardValue.ACE -> 0
    CardValue.SEVEN -> 1
    CardValue.EIGHT -> 2
    CardValue.NINE -> 3
    CardValue.TEN -> 4
    CardValue.JACK -> 5
    CardValue.QUEEN -> 6
    CardValue.KING -> 7
     else -> 0
}
