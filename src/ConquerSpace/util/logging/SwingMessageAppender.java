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
package ConquerSpace.util.logging;

import java.util.ArrayList;
import org.apache.logging.log4j.core.Appender;
import org.apache.logging.log4j.core.Core;
import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.appender.AbstractAppender;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
import org.apache.logging.log4j.core.config.plugins.PluginElement;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;

/**
 *
 * @author EhWhoAmI
 */
@Plugin(
        name = "SwingAppender",
        category = Core.CATEGORY_NAME,
        elementType = Appender.ELEMENT_TYPE)
public class SwingMessageAppender extends AbstractAppender {

    public static LoggerPanel panel;

    private ArrayList<LogMessage> events = new ArrayList<>();

    protected SwingMessageAppender(String name, Filter filter) {
        super(name, filter, null);
        initFrame();
    }

    @PluginFactory
    public static SwingMessageAppender createAppender(
            @PluginAttribute("name") String name,
            @PluginElement("Filter") Filter filter) {
        return new SwingMessageAppender(name, filter);
    }

    @Override
    public void append(LogEvent event) {
        events.add(new LogMessage(event.getMessage().getFormattedMessage(), event.getTimeMillis(), event.getLevel(), event.getLoggerName(), event.getThreadName()));
    }

    private void initFrame() {
        panel = new LoggerPanel(events);
    }
}
