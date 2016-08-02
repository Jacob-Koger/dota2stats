package com.example.jacobkoger.dota2application.util.filter;

import com.example.jacobkoger.dota2application.data.hero.Hero;
import com.example.jacobkoger.dota2application.data.history.MHMatch;
import com.example.jacobkoger.dota2application.data.history.MHPlayer;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static com.example.jacobkoger.dota2application.util.ListUtils.emptyIfNull;

/**
 * A {@link Predicate} used to determine if a search query matches a hero's name
 */
public class HeroPredicate implements Predicate<MHMatch> {

    /** Our heroes */
    private final Collection<Hero> mHeroes;
    /** The query parameter to check against */
    private final Collection<String> mQueries;

    /**
     * Constructor for HeroPredicate
     *
     * @param heroes  Our heroes
     * @param queries The query parameter to check against
     */
    public HeroPredicate(List<Hero> heroes, String... queries) {
        mHeroes = emptyIfNull(heroes);
        mQueries = emptyIfNull(Arrays.asList(queries));
    }

    @Override
    public boolean filter(MHMatch type) {
        for (final String query : mQueries) {
            for (MHPlayer player : emptyIfNull(type.getPlayers())) {
                for (Hero hero : mHeroes) {
                    if (player.getHeroId() == hero.getId()) {
                        if (hero.getLocalizedName().equalsIgnoreCase(query)) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

}
