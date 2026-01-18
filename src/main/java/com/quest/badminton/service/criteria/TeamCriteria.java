package com.quest.badminton.service.criteria;

import com.quest.badminton.config.specifications.Criteria;
import com.quest.badminton.config.specifications.filter.*;
import com.quest.badminton.entity.enumaration.TourMatchType;
import com.quest.badminton.entity.enumaration.TourStatus;
import com.quest.badminton.entity.enumaration.TourType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TeamCriteria implements Serializable, Criteria {

    private LongFilter id;
    private LongFilter tourId;
    private StringFilter name;

    public TeamCriteria(TeamCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.tourId = other.tourId == null ? null : other.tourId.copy();
        this.name = other.name == null ? null : other.name.copy();
    }

    @Override
    public Criteria copy() {
        return new TeamCriteria(this);
    }
}
