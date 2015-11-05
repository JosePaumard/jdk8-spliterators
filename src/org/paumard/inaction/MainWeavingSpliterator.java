/*
 * Copyright (C) 2015 José Paumard
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */

package org.paumard.inaction;

import org.paumard.spliterator.WeavingSpliterator;

import java.util.Spliterator;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 *
 * @author José
 */
public class MainWeavingSpliterator {

    public static void main(String... args) {

        Stream<Integer> stream1 = Stream.of(1, 2, 3, 4, 5);
        Stream<Integer> stream10 = Stream.of(10, 20, 30, 40, 50);

        Spliterator<String> spliterator1 = stream1.map(Object::toString).spliterator();
        Spliterator<String> spliterator10 = stream10.map(Object::toString).spliterator();

        WeavingSpliterator<String> weavingSpliterator = new WeavingSpliterator<>(spliterator1, spliterator10);
        Stream<String> stream = StreamSupport.stream(weavingSpliterator, false);

        stream.forEach(System.out::println);
    }
}
