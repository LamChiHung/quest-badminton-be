package com.quest.badminton.service.dto.criteria;

import com.quest.badminton.config.specifications.Criteria;
import com.quest.badminton.config.specifications.filter.Filter;
import com.quest.badminton.config.specifications.filter.InstantFilter;
import com.quest.badminton.config.specifications.filter.LongFilter;
import com.quest.badminton.config.specifications.filter.StringFilter;
import com.quest.badminton.entity.enumaration.TourStatus;
import com.quest.badminton.entity.enumaration.TourType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TourCriteria implements Serializable, Criteria {

    private LongFilter id;
    private StringFilter name;
    private InstantFilter fromStartDate;
    private InstantFilter toStartDate;
    private StringFilter location;
    private StatusFilter status;
    private TypeFilter type;
    public TourCriteria(TourCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.name = other.name == null ? null : other.name.copy();
        this.fromStartDate = other.fromStartDate == null ? null : other.fromStartDate.copy();
        this.toStartDate = other.toStartDate == null ? null : other.toStartDate.copy();
        this.location = other.location == null ? null : other.location.copy();
        this.status = other.status == null ? null : other.status.copy();
        this.type = other.type == null ? null : other.type.copy();
    }

    @Override
    public Criteria copy() {
        return new TourCriteria(this);
    }

    @NoArgsConstructor
    public static class StatusFilter extends Filter <TourStatus> {
        public StatusFilter(StatusFilter other) {
            super(other);
        }

        @Override
        public StatusFilter copy() {
            return new StatusFilter(this);
        }
    }

    @NoArgsConstructor
    public static class TypeFilter extends Filter <TourType> {
        public TypeFilter(TypeFilter other) {
            super(other);
        }

        @Override
        public TypeFilter copy() {
            return new TypeFilter(this);
        }
    }

}
