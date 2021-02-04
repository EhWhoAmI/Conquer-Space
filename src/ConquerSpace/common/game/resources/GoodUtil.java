/*
 * Conquer Space - Conquer Space!
 * Copyright (C) 2019 EhWhoAmI
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published
 * by the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 * 
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package ConquerSpace.common.game.resources;

import ConquerSpace.common.GameState;
import java.util.ArrayList;

/**
 *
 * @author EhWhoAmI
 */
public class GoodUtil {

    public static StoreableReference findGoodByTag(GameState gameState, String tagSearched) {
        Good resource = null;
        for (Good res : gameState.getGoodArrayList()) {
            for (String tag : res.tags) {
                if (tag.equals(tagSearched)) {
                    resource = res;
                    break;
                }
            }
        }
        if (resource != null) {
            return resource.getId();
        } else {
            return GoodReference.INVALID_REFERENCE;
        }
    }

    public static ArrayList<StoreableReference> findGoodsByTag(GameState gameState, String tagSearched) {
        ArrayList<StoreableReference> list = new ArrayList<>();
        for (Good res : gameState.getGoodArrayList()) {
            for (String tag : res.tags) {
                if (tag.equals(tagSearched)) {
                    list.add(res.getId());
                    break;
                }
            }
        }
        return list;
    }
}
