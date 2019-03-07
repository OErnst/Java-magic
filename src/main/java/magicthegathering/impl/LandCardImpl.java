package magicthegathering.impl;

import magicthegathering.game.AbstractCard;
import magicthegathering.game.LandCard;
import magicthegathering.game.LandCardType;
import magicthegathering.game.ManaType;

import java.util.Objects;
/**
 * @author Ondřej Ernst
 */
public class LandCardImpl extends AbstractCard implements LandCard {
    private LandCardType type;

    /**
     * constructor
     * @param type type of card
     */
    public LandCardImpl(LandCardType type) {
        if (type == null) {
            throw new IllegalArgumentException("no null");
        }
        this.type = type;
    }

    @Override
    public LandCardType getLandType() {
        return type;
    }

    @Override
    public ManaType getManaType() {
        switch (type) {
            case MOUNTAIN:
                return ManaType.RED;
            case SWAMP:
                return ManaType.BLACK;
            case FOREST:
                return ManaType.GREEN;
            case ISLAND:
                return ManaType.BLUE;
            case PLAINS:
                return ManaType.WHITE;
                default:
                    return null;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LandCardImpl landCard = (LandCardImpl) o;
        return type == landCard.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(type);
    }

    @Override
    public String toString() {
        return "Land " + type.toString().toLowerCase() + ", " + getManaType();
    }
}
