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
package ConquerSpace.client.gui;

import java.util.ArrayList;
import java.util.function.Consumer;
import javax.swing.AbstractListModel;

/**
 *
 * @author EhWhoAmI
 */
public class ObjectListModel<E> extends AbstractListModel<String> {

    private ObjectListModelHandler<E> handler = ObjectListModel::defaultHandler;
    private ArrayList<E> elements;

    public ObjectListModel() {
        elements = new ArrayList<>();
    }
    
    public void addElement(E obj) {
        elements.add(obj);
    }
    
    public E getObject(int i) {
        return (elements.get(i));
    }
    
    @Override
    public int getSize() {
        return elements.size();
    }

    public void setElements(ArrayList<E> elements) {
        this.elements = elements;
    }

    @Override
    public String getElementAt(int index) {
        return (handler.toString(elements.get(index)));
    }

    public void setHandler(ObjectListModelHandler<E> handler) {
        this.handler = handler;
    }
    
    public void clear() {
        elements.clear();
    }

    private static String defaultHandler(Object object) {
        return object.toString();
    }

    public interface ObjectListModelHandler<E> {
        public String toString(E object);
    }
}
