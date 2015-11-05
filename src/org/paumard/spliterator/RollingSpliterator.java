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
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;
import java.util.stream.Stream;

/**
 *
 * @author José
 */
public class RollingSpliterator<E> implements Spliterator<Stream<E>> {

	private final int grouping ;
	private final Spliterator<E> spliterator ;
	private Object [] buffer ; // we cant create arrays of E
	private AtomicInteger bufferWriteIndex = new AtomicInteger(0) ;
	private AtomicInteger bufferReadIndex = new AtomicInteger(0) ;
	
	public RollingSpliterator(Spliterator<E> spliterator, int grouping) {
		this.spliterator = spliterator ;
		this.grouping = grouping ;
		this.buffer = new Object[grouping + 1] ;
	}

	@Override
	public boolean tryAdvance(Consumer<? super Stream<E>> action) {
		Objects.requireNonNull(action) ;
		boolean finished = false ;

		if (bufferWriteIndex.get() == bufferReadIndex.get()) {
			for (int i = 0 ; i < grouping ; i++) {
				if (!advanceSpliterator()) {
					finished = true ;
				}
			}
		}
		if (!advanceSpliterator()) {
			finished = true ;
		}
		
		Stream<E> subStream = buildSubstream() ;
		action.accept(subStream) ;
		return !finished ;
	}

	private boolean advanceSpliterator() {
		return spliterator.tryAdvance(
					element -> { 
						buffer[bufferWriteIndex.get() % buffer.length] = element ; 
						bufferWriteIndex.incrementAndGet() ;
				});
	}

	@Override
	public Spliterator<Stream<E>> trySplit() {
		return new RollingSpliterator<E>(spliterator.trySplit(), grouping) ;
	}
	
	@SuppressWarnings("unchecked")
	private Stream<E> buildSubstream() {
		
		Stream.Builder<E> subBuilder = Stream.builder() ;
		for (int i = 0 ; i < grouping ; i++) {			
			subBuilder.add((E)buffer[(i + bufferReadIndex.get()) % buffer.length]) ;
		}
		bufferReadIndex.incrementAndGet() ;
		Stream<E> subStream = subBuilder.build() ;
		return subStream ;
	}

	@Override
	public long estimateSize() {
		return spliterator.estimateSize() - grouping ;
	}

	@Override
	public int characteristics() {
		return spliterator.characteristics() ;
	}
}