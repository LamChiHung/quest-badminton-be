package com.quest.badminton.service.dto.criteria;

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
public class PlayerCriteria implements Serializable, Criteria {

    private LongFilter id;
    private LongFilter tourId;
    private LongFilter userId;
    private StringFilter email;
    private StringFilter name;
    private LongFilter teamId;
    private StatusFilter status;
    private TierFilter tier;
    private GenderFilter gender;

    public PlayerCriteria(PlayerCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.tourId = other.tourId == null ? null : other.tourId.copy();
        this.userId = other.userId == null ? null : other.userId.copy();
        this.email = other.email == null ? null : other.email.copy();
        this.name = other.name == null ? null : other.name.copy();
        this.teamId = other.teamId == null ? null : other.teamId.copy();
        this.status = other.status == null ? null : other.status.copy();
        this.tier = other.tier == null ? null : other.tier.copy();
        this.gender = other.gender == null ? null : other.gender.copy();
    }

    @Override
    public Criteria copy() {
        return new PlayerCriteria(this);
    }

    @NoArgsConstructor
    public static class StatusFilter extends Filter <PlayerStatus> {
        public StatusFilter(StatusFilter other) {
            super(other);
        }

        @Override
        public StatusFilter copy() {
            return new StatusFilter(this);
        }
    }

    @NoArgsConstructor
    public static class TierFilter extends Filter <PlayerTier> {
        public TierFilter(TierFilter other) {
            super(other);
        }

        @Override
        public TierFilter copy() {
            return new TierFilter(this);
        }
    }

    @NoArgsConstructor
    public static class GenderFilter extends Filter <Gender> {
        public GenderFilter(GenderFilter other) {
            super(other);
        }

        @Override
        public GenderFilter copy() {
            return new GenderFilter(this);
        }
    }

}
