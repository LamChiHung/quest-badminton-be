package com.quest.badminton.service.criteria;

import com.quest.badminton.config.specifications.Criteria;
import com.quest.badminton.config.specifications.filter.LongFilter;
import com.quest.badminton.config.specifications.filter.StringFilter;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MatchCriteria implements Serializable, Criteria {

    private LongFilter id;
    private LongFilter groupMatchId;
    private LongFilter roundId;
    private LongFilter tourId;


    public MatchCriteria(MatchCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.groupMatchId = other.groupMatchId == null ? null : other.groupMatchId.copy();
        this.roundId = other.roundId == null ? null : other.roundId.copy();
        this.tourId = other.tourId == null ? null : other.tourId.copy();
    }

    @Override
    public Criteria copy() {
        return new MatchCriteria(this);
    }
}
