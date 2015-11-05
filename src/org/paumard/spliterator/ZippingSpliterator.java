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

import java.util.Spliterator;
import java.util.function.BiFunction;
import java.util.function.Consumer;

/**
 *
 * @author José
 */
public class ZippingSpliterator<E1, E2, R> implements Spliterator<R> {

	private final Spliterator<E1> spliterator1 ;
	private final Spliterator<E2> spliterator2 ;
	private final BiFunction<E1, E2, R> tranform ;

	public static class Builder<E1, E2, R> {
		
		private Spliterator<E1> spliterator1;
		private Spliterator<E2> spliterator2;
		private BiFunction<E1, E2, R> tranform ;
		
		public Builder() {
		}
		
		public Builder<E1, E2, R> with(Spliterator<E1> spliterator) {
			this.spliterator1 = spliterator;
			return this;
		}
		
		public Builder<E1, E2, R> and(Spliterator<E2> spliterator) {
			this.spliterator2 = spliterator;
			return this;
		}
		
		public Builder<E1, E2, R> mergedBy(BiFunction<E1, E2, R> tranform) {
			this.tranform = tranform;
			return this;
		}
		
		public ZippingSpliterator<E1, E2, R> build() {
			return new ZippingSpliterator(spliterator1, spliterator2, tranform);
		}
	}
	
	private ZippingSpliterator(
			Spliterator<E1> spliterator1, Spliterator<E2> spliterator2, 
			BiFunction<E1, E2, R> tranform) {
		this.spliterator1 = spliterator1 ;
		this.spliterator2 = spliterator2 ;
		this.tranform = tranform ;
	}
	
	@Override
	public boolean tryAdvance(Consumer<? super R> action) {
		return spliterator1.tryAdvance(
				e1 -> {
					spliterator2.tryAdvance(e2 -> {
						action.accept(tranform.apply(e1, e2)) ;
					}) ;
				}
			) ;
	}

	@Override
	public Spliterator<R> trySplit() {
		Spliterator<E1> splitedSpliterator1 = spliterator1.trySplit() ;
		Spliterator<E2> splitedSpliterator2 = spliterator2.trySplit() ;
		return new ZippingSpliterator<E1, E2, R>(splitedSpliterator1, splitedSpliterator2, tranform) ;
	}

	@Override
	public long estimateSize() {
		return this.spliterator1.estimateSize() ;
	}

	@Override
	public int characteristics() {
		return this.spliterator1.characteristics() ;
	}
}