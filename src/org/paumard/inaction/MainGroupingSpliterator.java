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

import org.paumard.spliterator.GroupingSpliterator;

import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 *
 * @author José
 */
public class MainGroupingSpliterator {

    public static void main(String... args) {

        Stream<Integer> integerStream = Stream.of(1, 2, 3, 4, 5, 6, 7, 8, 9);

        GroupingSpliterator<Integer> groupingSpliterator = new GroupingSpliterator<>(integerStream.spliterator(), 3);
        Stream<Stream<Integer>> groupedStream = StreamSupport.stream(groupingSpliterator, false);

        groupedStream.forEach(
                stream -> {
                    String s = stream.map(Object::toString).collect(Collectors.joining(", "));
                    System.out.println(s);
                }
        );
    }
}
