package cfvbaibai.cardfantasy.engine.skill;

import java.util.List;

import cfvbaibai.cardfantasy.CardFantasyRuntimeException;
import cfvbaibai.cardfantasy.engine.CardInfo;
import cfvbaibai.cardfantasy.engine.CardStatusType;
import cfvbaibai.cardfantasy.engine.EntityInfo;
import cfvbaibai.cardfantasy.engine.HeroDieSignal;
import cfvbaibai.cardfantasy.engine.SkillResolver;
import cfvbaibai.cardfantasy.engine.SkillUseInfo;

public final class Purify {
    public static void apply(SkillUseInfo skillUseInfo, SkillResolver resolver, EntityInfo attacker, int mode)
            throws HeroDieSignal {
        List<CardInfo> cards = null;
        if (mode == -1) {   // 神性祈求
            cards = attacker.getOwner().getField().getAliveCards();
        } else if (mode == -2) {    // 净魂领域
            if (!(attacker instanceof CardInfo)) {
                throw new CardFantasyRuntimeException("Invalid attacker. Attacker must be CardInfo when mode is -2");
            }
            CardInfo attackCard = (CardInfo)attacker;
            cards = resolver.getAdjacentCards(attacker.getOwner().getField(), attackCard.getPosition());
        } else {
            throw new CardFantasyRuntimeException("Invalid mode. Must be -1 or -2.");
        }
        resolver.getStage().getUI().useSkill(attacker, cards, skillUseInfo.getSkill(), true);
        for (CardInfo card : cards) {
            resolver.removeStatus(card, CardStatusType.迷惑);
            resolver.removeStatus(card, CardStatusType.冰冻);
            resolver.removeStatus(card, CardStatusType.锁定);
            if(card.getStatus().getStatusOf(CardStatusType.虚化).isEmpty()){
                resolver.removeStatus(card, CardStatusType.麻痹);
            }
            resolver.removeStatus(card, CardStatusType.中毒);
            resolver.removeStatus(card, CardStatusType.燃烧);
            resolver.removeStatus(card, CardStatusType.弱化);
            resolver.removeStatus(card, CardStatusType.晕眩);
            resolver.removeStatus(card, CardStatusType.裂伤);
            if( card.removeForce(CardStatusType.死印)) {
                resolver.getStage().getUI().removeCardStatus(card, CardStatusType.死印);
            }
            resolver.removeStatus(card, CardStatusType.致盲);

            // 以下为实测表明净化不能解除的状态
            // resolver.removeStatus(card, CardStatusType.王国);
            // resolver.removeStatus(card, CardStatusType.森林);
            // resolver.removeStatus(card, CardStatusType.蛮荒);
            // resolver.removeStatus(card, CardStatusType.地狱);
            // resolver.removeStatus(card, CardStatusType.魔印);
            // resolver.removeStatus(card, CardStatusType.沉默);
        }
    }
}
