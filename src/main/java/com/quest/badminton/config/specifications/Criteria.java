package com.quest.badminton.config.specifications;

/**
 * Implementation should usually contain fields of Filter instances.
 */
public interface Criteria {

    /**
     * @return a new criteria with copied filters
     */
    Criteria copy();
}
