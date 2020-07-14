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
package ConquerSpace.client.gui.encyclopedia;

import java.util.ArrayList;

/**
 *
 * @author EhWhoAmI
 */
public class EncyclopediaTable implements EncyclopediaElement{

    private String[] colunmNames = {};
    ArrayList<ArrayList<Object>> data;

    public EncyclopediaTable(String[] colunmNames) {
        data = new ArrayList<>();
        this.colunmNames = colunmNames;
        for (int i = 0; i < colunmNames.length; i++) {
            data.add(new ArrayList<>());
        }
    }

    public void addRow(Object[] o) {
        if (o.length != colunmNames.length) {
            throw new IllegalArgumentException("The number of colunms passed must equal the number of colunms " + (colunmNames.length) + "of the table!");
        } 
        //Or else make new thing
        for(int i = 0; i < o.length; i++) {
            data.get(i).add(o[i]);
        }
    }

    public Object getData(int row, int colunm) {
        return data.get(colunm).get(row);
    }
    
    public String[] getColunmNames() {
        return colunmNames;
    }

    public int getColunmCount() {
        return colunmNames.length;
    }
}
