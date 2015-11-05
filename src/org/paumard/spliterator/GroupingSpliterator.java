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

package org.paumard.spliterator;

import java.util.Objects;
import java.util.Spliterator;
import java.util.function.Consumer;
import java.util.stream.Stream;

/**
 *
 * @author José
 */
public class GroupingSpliterator<E> implements Spliterator<Stream<E>> {

	private final long grouping ;
	private final Spliterator<E> spliterator ;
	
	public GroupingSpliterator(Spliterator<E> spliterator, long grouping) {
		this.spliterator = spliterator ;
		this.grouping = grouping ;
	}

	@Override
	public boolean tryAdvance(Consumer<? super Stream<E>> action) {
		Objects.requireNonNull(action) ;
		boolean finished = false ;
		Stream.Builder<E> builder = Stream.builder() ;
		for (int i = 0 ; i < grouping ; i++) {
			if (!spliterator.tryAdvance(element -> { builder.add(element) ; })) {
				finished = true ;
			}
		}
		Stream<E> subStream = builder.build() ;
		action.accept(subStream) ;
		return !finished ;
	}

	@Override
	public Spliterator<Stream<E>> trySplit() {
		
		Spliterator<E> spliterator = this.spliterator.trySplit() ;
		return new GroupingSpliterator<E>(spliterator, grouping) ;
	}

	@Override
	public long estimateSize() {
		return spliterator.estimateSize() / grouping ;
	}

	@Override
	public int characteristics() {
		return spliterator.characteristics();
	}
}