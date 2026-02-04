package com.quest.badminton.service.criteria;

import com.quest.badminton.config.specifications.Criteria;
import com.quest.badminton.config.specifications.filter.Filter;
import com.quest.badminton.config.specifications.filter.LongFilter;
import com.quest.badminton.config.specifications.filter.StringFilter;
import com.quest.badminton.entity.enumaration.Gender;
import com.quest.badminton.entity.enumaration.PlayerStatus;
import com.quest.badminton.entity.enumaration.PlayerTier;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PlayerPairCriteria implements Serializable, Criteria {

    private LongFilter id;
    private LongFilter tourId;
    private LongFilter playerId;
    private LongFilter teamId;

    public PlayerPairCriteria(PlayerPairCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.tourId = other.tourId == null ? null : other.tourId.copy();
        this.playerId = other.playerId == null ? null : other.playerId.copy();
        this.teamId = other.teamId == null ? null : other.teamId.copy();
    }

    @Override
    public Criteria copy() {
        return new PlayerPairCriteria(this);
    }

}
