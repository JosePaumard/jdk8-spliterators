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

/**
 *
 * @author José
 */
public class WeavingSpliterator<E> implements Spliterator<E> {

	private Spliterator<E> [] spliterators ;
	private AtomicInteger whichOne = new AtomicInteger() ;

	@SafeVarargs
	public WeavingSpliterator(Spliterator<E>... spliterators) {
		this.spliterators = spliterators ;
	}
	
	@Override
	public boolean tryAdvance(Consumer<? super E> action) {
		Objects.requireNonNull(action) ;
		return spliterators[whichOne.getAndIncrement() % spliterators.length].tryAdvance(action) ;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Spliterator<E> trySplit() {
		Spliterator<E> [] spliterators = new Spliterator[this.spliterators.length] ;
		int index = 0 ;
		for (Spliterator<E> spliterator : this.spliterators) {
			spliterators[index++] = spliterator.trySplit() ;
		}
		return new WeavingSpliterator<E>(spliterators) ;
	}

	@Override
	public long estimateSize() {
		int size = 0 ;
		for (Spliterator<E> spliterator : this.spliterators) {
			size += spliterator.estimateSize() ;
		}
		return size ;
	}

	@Override
	public int characteristics() {
		return this.spliterators[0].characteristics() ;
	}
}