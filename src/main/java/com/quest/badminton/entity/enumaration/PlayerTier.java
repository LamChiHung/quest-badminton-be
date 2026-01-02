package com.quest.badminton.entity.enumaration;

import lombok.Getter;

@Getter
public enum PlayerTier {
    Y(1), TBT(2), TB(3), TBC(4), TBCC(5), K(6);
    private final int point;

    PlayerTier(Integer point) {
        this.point = point;
    }
}
