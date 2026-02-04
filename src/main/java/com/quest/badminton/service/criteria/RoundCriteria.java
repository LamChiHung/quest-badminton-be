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
public class RoundCriteria implements Serializable, Criteria {

    private LongFilter id;
    private StringFilter name;
    private LongFilter tourId;

    public RoundCriteria(RoundCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.name = other.name == null ? null : other.name.copy();
        this.tourId = other.tourId == null ? null : other.tourId.copy();
    }

    @Override
    public Criteria copy() {
        return new RoundCriteria(this);
    }
}
