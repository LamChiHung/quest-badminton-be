package com.quest.badminton.service.criteria;

import com.quest.badminton.config.specifications.Criteria;
import com.quest.badminton.config.specifications.filter.Filter;
import com.quest.badminton.config.specifications.filter.LongFilter;
import com.quest.badminton.config.specifications.filter.StringFilter;
import com.quest.badminton.entity.enumaration.Gender;
import com.quest.badminton.entity.enumaration.PlayerPairType;
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
    private TypeFilter type;

    public PlayerPairCriteria(PlayerPairCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.tourId = other.tourId == null ? null : other.tourId.copy();
        this.playerId = other.playerId == null ? null : other.playerId.copy();
        this.teamId = other.teamId == null ? null : other.teamId.copy();
        this.type = other.type == null ? null : other.type.copy();
    }

    @Override
    public Criteria copy() {
        return new PlayerPairCriteria(this);
    }

    @NoArgsConstructor
    public static class TypeFilter extends Filter <PlayerPairType> {
        public TypeFilter(TypeFilter other) {
            super(other);
        }

        @Override
        public TypeFilter copy() {
            return new TypeFilter(this);
        }
    }

}
