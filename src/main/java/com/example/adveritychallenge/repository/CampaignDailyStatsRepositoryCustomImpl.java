package com.example.adveritychallenge.repository;

import com.example.adveritychallenge.data.CampaignDailyStats;
import com.example.adveritychallenge.data.CampaignStatsAggregate;
import com.example.adveritychallenge.data.DailyStatsGroupBy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Repository
class CampaignDailyStatsRepositoryCustomImpl implements CampaignDailyStatsRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Page<CampaignDailyStats> findAllBetweenDates(
            LocalDate since,
            LocalDate until,
            List<String> campaignFilters,
            List<String> datasourceFilters,
            Pageable pageable
    ) {
        var criteriaBuilder = entityManager.getCriteriaBuilder();
        var criteriaQuery = criteriaBuilder.createQuery(CampaignDailyStats.class);
        var root = criteriaQuery.from(CampaignDailyStats.class);

        var predicates = getPredicatesForFindAllBetweenDates(criteriaBuilder, root, since, until, campaignFilters, datasourceFilters);
        criteriaQuery.where(predicates.toArray(new Predicate[0]));

        var query = entityManager.createQuery(criteriaQuery)
                .setFirstResult(pageable.getPageNumber() * pageable.getPageSize())
                .setMaxResults(pageable.getPageSize());

        var totalRows = getTotalRowsForFindAllBetweenDates(criteriaBuilder, since, until, campaignFilters,
                datasourceFilters, null);

        return new PageImpl<>(query.getResultList(), pageable, totalRows);
    }

    @Override
    public Page<CampaignStatsAggregate> findAllBetweenDatesGroupedBy(
            LocalDate since,
            LocalDate until,
            List<String> campaignFilters,
            List<String> datasourceFilters,
            DailyStatsGroupBy groupBy,
            Pageable pageable
    ) {
        var criteriaBuilder = entityManager.getCriteriaBuilder();
        var criteriaQuery = criteriaBuilder.createQuery(CampaignStatsAggregate.class);
        var root = criteriaQuery.from(CampaignDailyStats.class);

        var predicates = getPredicatesForFindAllBetweenDates(criteriaBuilder, root, since, until, campaignFilters, datasourceFilters);
        criteriaQuery.where(predicates.toArray(new Predicate[0]));

        if (groupBy != null) {
            criteriaQuery.groupBy(root.get(groupBy.getValue()));
            var groupByExpression = criteriaBuilder.literal(groupBy.getValue());
            criteriaQuery.select(criteriaBuilder.construct(
                    CampaignStatsAggregate.class,
                    groupByExpression,
                    root.get(groupBy.getValue()),
                    criteriaBuilder.sumAsLong(root.get("impressions")),
                    criteriaBuilder.sumAsLong(root.get("clicks")),
                    // MySQL is rounding down, so to fix the precision issue the value is multiplied by 1000 and has to be divided by 10 in java.
                    criteriaBuilder.prod(1000.0, criteriaBuilder.quot(criteriaBuilder.sumAsDouble(root.get("clicks")), criteriaBuilder.sumAsDouble(root.get("impressions"))))
            ));
        }

        var query = entityManager.createQuery(criteriaQuery)
                .setFirstResult(pageable.getPageNumber() * pageable.getPageSize())
                .setMaxResults(pageable.getPageSize());

        var totalRows = getTotalRowsForFindAllBetweenDates(criteriaBuilder, since, until, campaignFilters,
                datasourceFilters, groupBy);

        return new PageImpl<>(query.getResultList(), pageable, totalRows);
    }

    private Long getTotalRowsForFindAllBetweenDates(
            CriteriaBuilder criteriaBuilder,
            LocalDate since,
            LocalDate until,
            List<String> campaignFilters,
            List<String> datasourceFilters,
            DailyStatsGroupBy groupBy
    ) {
        var criteriaQuery = criteriaBuilder.createQuery(Long.class);
        var root = criteriaQuery.from(CampaignDailyStats.class);
        var predicatesForCount = getPredicatesForFindAllBetweenDates(criteriaBuilder, root, since, until, campaignFilters, datasourceFilters);
        criteriaQuery.where(predicatesForCount.toArray(new Predicate[0]));

        if (groupBy != null) {
            criteriaQuery.select(criteriaBuilder.countDistinct(root.get(groupBy.getValue())));
        } else {
            criteriaQuery.select(criteriaBuilder.count(root));
        }

        return entityManager.createQuery(criteriaQuery).getSingleResult();
    }

    private List<Predicate> getPredicatesForFindAllBetweenDates(
            CriteriaBuilder criteriaBuilder,
            Root<CampaignDailyStats> root,
            LocalDate since,
            LocalDate until,
            List<String> campaignFilters,
            List<String> datasourceFilters
    ) {
        var predicates = new ArrayList<Predicate>();

        predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("day"), since));
        predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("day"), until));

        if (!campaignFilters.isEmpty()) {
            predicates.add(root.get("campaign").in(campaignFilters));
        }

        if (!datasourceFilters.isEmpty()) {
            predicates.add(root.get("datasource").in(datasourceFilters));
        }

        return predicates;
    }
}
